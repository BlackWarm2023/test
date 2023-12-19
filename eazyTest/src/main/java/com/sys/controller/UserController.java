package com.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.vo.Result;
import com.sys.entity.User;
import com.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-17
 */
@Api(tags = {"用户接口列表"})
@Slf4j
@RestController
@RequestMapping("/user")
//@CrossOrigin  跨域策略  单独控制器
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> list = userService.list();
        return Result.success(list,"查询成功");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        System.out.println("user:: "+user);
        Map<String, Object> data =  userService.login(user);

        if (data != null){
            return Result.success(data, "success");
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @ApiOperation("用户token验证")
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token){
        // 根据 token 获取用户信息, redis
        Map<String, Object> data = userService.getUserInfo(token);

        if (data != null){
            return Result.success(data, "success");
        }
        return Result.fail(20003,"登录信息失效,请重新登录");
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    // 分页查询
    @ApiOperation("分页查询")
    @GetMapping("/getList")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(value = "username",required = false) String username,
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam("pageNo") Long pageNo,
            @RequestParam("pageSize") Long pageSize
    ){

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.like(StringUtils.hasLength(phone),User::getPhone,phone);

        //降序排序
        wrapper.orderByDesc(User::getId);

        Page<User> page = new Page<>(pageNo,pageSize);

        userService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    //新增用户
    @ApiOperation("新增用户")
    @PostMapping("/addUser")
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userService.save(user);
        userService.addUser(user);
        return Result.success("新增用户成功");
    }

    //修改用户
    @ApiOperation("修改用户")
    @PutMapping("/updateUser")
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        userService.updateUser(user);
        return Result.success("修改用户成功");
    }

    //根据 id 查询对应用户信息
    @ApiOperation("根据 id 查询对应用户信息")
    @GetMapping("/getUserById/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    //根据 id 删除对应用户信息
    @ApiOperation("根据 id 删除对应用户信息")
    @DeleteMapping ("/deleteUserById/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id){
        userService.deleteUserById(id);
        return Result.success("删除用户成功");
    }
}
