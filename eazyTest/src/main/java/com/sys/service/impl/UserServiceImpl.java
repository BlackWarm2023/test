package com.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.commen.utils.JwtUtils;
import com.sys.entity.Menu;
import com.sys.entity.User;
import com.sys.entity.UserRole;
import com.sys.mapper.UserMapper;
import com.sys.mapper.UserRoleMapper;
import com.sys.service.IMenuService;
import com.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    //加密密码解密需要
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;

    @Override
    public Map<String, Object> login(User user){
        //根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser =  userMapper.selectOne(wrapper);
        //结果不为空,并且密码与传入密码匹配,则生成 token ,并将用户信息存入 redis
        if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            // 暂时使用 UUID , 终极方案是 jwt
//            String key = "user:" + UUID.randomUUID();

            // 存入 redis
            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            //创建 jwt
            System.out.println("loginUser: " + loginUser);
            String token = jwtUtils.creatToken(loginUser);
            System.out.println("token: " + token);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    /*@Override
    public Map<String, Object> login(User user){
        //根据用户名和密码查询,使用 MP 自带查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());
        User loginUser =  userMapper.selectOne(wrapper);
        //结果不为空,则生成 token ,并将用户信息存入 redis
        if (loginUser != null){
            // 暂时使用 UUID , 终极方案是 jwt
            String key = "user:" + UUID.randomUUID();

            // 存入 redis
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);
            return data;
        }
        return null;
    }*/

    @Override
    public Map<String, Object> getUserInfo(String token){
        //  根据 token 获取用户信息, redis
//        Object obj = redisTemplate.opsForValue().get(token);

        User loginUser = null;
        try {
            loginUser = jwtUtils.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 是否存入 redis
        if (loginUser != null){
            // 反序列化
//            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);

            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());

            // 角色
            List<String> roleList = userMapper.getRoleNameByUserId(loginUser.getId());
            // 将角色列表放入 data 中
            data.put("roles",roleList);

            // 权限列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);
            log.info(String.valueOf(menuList));


            return data;
        }

        return null;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);
    }

    @Override
    @Transactional
    // @Transactional 事务的注解
    public void addUser(User user) {
        // 写入用户表
        userMapper.insert(user);
        // 写入用户角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList){
                // 因为 id 自增,所以不用传值
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        // 获取当前用户
        User user = userMapper.selectById(id);

        // 使用 MP 自带的条件查询,设置查询条件
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);

        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);

        /**
         * 这段代码使用了 Java 8 中的 Stream API
         * 来对 userRoleList 中的元素进行处理。
         * 首先，使用 map() 方法将 userRoleList 中的每个元素映射到一个新的元素，
         * 这个新的元素是 userRole 的 roleId 属性。
         * 然后，使用 collect() 方法将这些元素收集到一个新的 List 中。
         * 最终，得到的 List 就是 roleIdList。
         */
        List<Integer> roleIdList = userRoleList.stream()
                .map(userRole -> {return userRole.getRoleId();})
                .collect(Collectors.toList());

        user.setRoleIdList(roleIdList);

        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 更新用户表
        userMapper.updateById(user);

        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);

        // 设置新角色
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList){
                // 因为 id 自增,所以不用传值
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);

        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);

    }
}
