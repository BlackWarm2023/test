package com.CodeGenerator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @Description: MP 代码生成器
 * @author: blackwarm
 * @date: 2023年 09月 15日  10:20
 */
public class CodeGenerator {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&charcterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String moduleName = "sys";
        String mapperLocation = "D:\\IDEA\\work-space\\EAZYTEST\\eazyTest\\src\\main\\resources\\mapper\\" + moduleName;
//        String tables = "x_user,x_role,x_menu,x_user_role,x_role_menu";
        String tables = "x_role_menu";


        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("BlackWarm") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\IDEA\\work-space\\EAZYTEST\\eazyTest\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("x_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
