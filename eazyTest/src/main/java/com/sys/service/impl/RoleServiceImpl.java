package com.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sys.entity.Role;
import com.sys.entity.RoleMenu;
import com.sys.mapper.RoleMapper;
import com.sys.mapper.RoleMenuMapper;
import com.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-18
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    // 确保在方法执行过程中，如果出现异常或错误，所有对数据库的操作都将被回滚，以保证数据的一致性。
    @Transactional
    public void addRole(Role role) {
        // 写入 角色 表
        roleMapper.insert(role);
        // 写入 角色菜单关系 表
        setRoleMenu(role);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        // 修改角色表
        roleMapper.updateById(role);

        // 删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);

        // 新增权限
        setRoleMenu(role);
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        // 删除角色表
        roleMapper.deleteById(id);

        // 删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);

    }

    private void setRoleMenu(Role role) {
        if (role.getMenuIdList() != null) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, menuId, role.getRoleId()));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = roleMapper.selectById(id);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }
}
