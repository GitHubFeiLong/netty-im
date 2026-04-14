package com.feilong.im.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.schema.Column;

import java.util.Collections;
import java.util.List;

/**
 * @author cfl 2026/04/14
 */
public class CodeGenerator {
    /**
     * 数据库配置
     */
    private static final String DATA_SOURCE_URL = "jdbc:mysql://localhost:3306/im?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static final String DATA_SOURCE_USERNAME = "root";
    private static final String DATA_SOURCE_PASSWORD = "l(=8gp_04h*&";

    /**
     * 基础配置
     */
    private static final String PARENT_PACKAGE = "com.feilong.im";
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";
    private static final String XML_OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/resources/mapper";

    /**
     * 作者名
     */
    private static final String AUTHOR = "cfl";

    /**
     * 需要生成的表名（留空则生成所有表）
     */
    private static final List<String> TABLE_NAMES = Lists.newArrayList(
            // "im_user",
            // "im_friend",
            // "im_message"
    );

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_URL, DATA_SOURCE_USERNAME, DATA_SOURCE_PASSWORD)
                // 全局配置
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .outputDir(OUTPUT_DIR)
                        .commentDate("yyyy-MM-dd")
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()  // 禁止自动打开输出目录
                )
                // 包配置
                .packageConfig(builder -> builder
                        .parent(PARENT_PACKAGE)
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, XML_OUTPUT_DIR))
                )
                // 策略配置
                .strategyConfig(builder -> builder
                        // 设置需要生成的表名
                        .addInclude(getTables())
                        // 排除不需要生成的表
                        .addExclude("flyway_schema_history")

                        // Entity 策略配置
                        .entityBuilder()
                        .enableLombok()  // 启用 Lombok
                        .enableChainModel()  // 启用链式模型
                        .logicDeleteColumnName("deleted")  // 逻辑删除字段
                        .enableTableFieldAnnotation()  // 启用字段注解
                        .naming(NamingStrategy.underline_to_camel)  // 表名映射策略：下划线转驼峰
                        .columnNaming(NamingStrategy.underline_to_camel)  // 列名映射策略
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE)
                        )

                        // Mapper 策略配置
                        .mapperBuilder()
                        .enableBaseResultMap()  // 启用 BaseResultMap
                        .enableBaseColumnList()  // 启用 BaseColumnList

                        // Service 策略配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")

                        // Controller 策略配置
                        .controllerBuilder()
                        .enableRestStyle()  // 启用 REST 风格
                        .enableHyphenStyle()  // 启用连字符风格
                )
                // 使用 Freemarker 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())
                // 执行
                .execute();
    }

    /**
     * 获取需要生成的表名列表
     * @return 表名数组
     */
    private static String[] getTables() {
        if (TABLE_NAMES == null || TABLE_NAMES.isEmpty()) {
            return new String[0];  // 空数组表示生成所有表
        }
        return TABLE_NAMES.toArray(new String[0]);
    }
}
