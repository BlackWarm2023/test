package com.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.vo.Result;
import com.sys.entity.Role;
import com.sys.entity.User;
import com.sys.service.IRoleService;
import com.sys.service.IUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-18
 */
@Api(tags = {"角色接口列表"})
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // 分页查询
    @ApiOperation("分页查询")
    @GetMapping("/getList")
    public Result<Map<String, Object>> getRoleList(
            @RequestParam(value = "roleName",required = false) String roleName,
            @RequestParam("pageNo") Long pageNo,
            @RequestParam("pageSize") Long pageSize
    ){

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(roleName),Role::getRoleName,roleName);

        //降序排序
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);

        roleService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    //新增用户
    @ApiOperation("新增用户")
    @PostMapping("/addRole")
    public Result<?> addRole(@RequestBody Role role){
        roleService.addRole(role);
        return Result.success("新增角色成功");
    }

    //修改用户
    @ApiOperation("修改用户")
    @PutMapping("/updateRole")
    public Result<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    //根据 id 查询对应用户信息
    @ApiOperation("根据 id 查询对应用户信息")
    @GetMapping("/getRoleById/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    //根据 id 查询对应用户信息
    @ApiOperation("根据 id 查询对应用户信息")
    @DeleteMapping ("/deleteRoleById/{id}")
    public Result<Role> deleteRoleById(@PathVariable("id") Integer id){
//        roleService.removeById(id);
        roleService.deleteRoleById(id);
        return Result.success("删除用户成功");
    }

    //获取所有角色信息
    @ApiOperation("获取所有角色信息")
    @GetMapping("/getAllRole")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }

}
