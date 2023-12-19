package com.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sys.entity.Menu;
import com.sys.mapper.MenuMapper;
import com.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenu(){
        // 一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = menuMapper.selectList(wrapper);
//        List<Menu> menuList = this.list(wrapper);  与上面一行效果一样
        // 填充子菜单
        setMenuChildren(menuList);
        return menuList;
    }

    private void setMenuChildren(List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList){
                // 查一级菜单对应的二级菜单
                LambdaQueryWrapper<Menu> sonWrapper = new LambdaQueryWrapper<>();
                sonWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> sonMenuList = menuMapper.selectList(sonWrapper);
                // 将查到的二级菜单填入一级菜单的 children 属性
                menu.setChildren(sonMenuList);
                // 递归 用于查询多级菜单,一般不使用递归,但是菜单数据少,可以使用递归
                setMenuChildren(sonMenuList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        // 一级菜单
        List<Menu> menuList = menuMapper.getMenuListByUserId(userId, 0);

        // 子菜单
        setMenuChildrenById(userId, menuList);

        return menuList;
    }

    private void setMenuChildrenById(Integer userId, List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList){
                //                                          这里的  menu.getMenuId() 是对应的 menu 的menuId
                List<Menu> sunMenuList = menuMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(sunMenuList);
                // 递归调用自己
                setMenuChildrenById(userId,sunMenuList);
            }
        }
    }
}
