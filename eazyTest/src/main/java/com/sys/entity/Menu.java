package com.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author BlackWarm
 * @since 2023-09-27
 */
@TableName("x_menu")
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;
    private String component;
    private String path;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private String parentId;
    private String isLeft;
    private String hidden;
    private Integer deleted;

    // @TableField 可以使得该变量不会被 MP 识别,因为这是数据库没有的字段
    @TableField(exist = false)
    // 该注入可以使得 children 字段为 null 时不会被以 json 格式返回
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    // @TableField 可以使得该变量不会被 MP 识别,因为这是数据库没有的字段
    @TableField(exist = false)
    private Map<String, Object> meta;

    public Map<String, Object> getMeta(){
        this.meta = new HashMap<>();
        this.meta.put("title", this.title);
        this.meta.put("icon", this.icon);
        return meta;
    }
}
