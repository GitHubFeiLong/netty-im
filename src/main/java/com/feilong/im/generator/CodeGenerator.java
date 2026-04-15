package com.feilong.im.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;
import com.feilong.im.entity.BaseEntity;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.sf.jsqlparser.schema.Column;

import java.util.*;

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
            "sys_app"
    );

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_URL, DATA_SOURCE_USERNAME, DATA_SOURCE_PASSWORD)
                // 全局配置
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .outputDir(OUTPUT_DIR)
                        .commentDate("yyyy/MM/dd")
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
                        // 排除不需要生成的表 不能和 addInclude 同时使用
                        // .addExclude("flyway_schema_history")
                        // 实体配置
                        .entityBuilder()
                        .javaTemplate("/templates/entity.java")
                        .idType(IdType.AUTO)
                        .naming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略，默认下划线转驼峰
                        .enableSerialAnnotation() // 启用 java.io.Serial注解
                        .enableLombok(new ClassAnnotationAttributes("@Data", "lombok.Data"))
                        .enableChainModel()  // 启用链式模型
                        .enableTableFieldAnnotation() // 启用字段注解
                        .enableFileOverride() // 开启覆盖已生成的文件
                        .enableRemoveIsPrefix() // 开启移除is前缀
                        .versionColumnName("version")
                        .logicDeleteColumnName("deleted") // 逻辑删除字段名
                        .addTableFills(
                                new com.baomidou.mybatisplus.generator.fill.Column("created_time", FieldFill.INSERT),
                                new com.baomidou.mybatisplus.generator.fill.Column("updated_time", FieldFill.INSERT_UPDATE)
                        )
                        // mapper配置
                        .mapperBuilder()
                        .mapperTemplate("/templates/mapper.java")
                        .mapperXmlTemplate("/templates/mapper.xml")
                        .enableFileOverride() // 开启覆盖已生成的文件(小心数据丢失)
                        .enableBaseColumnList()  // 启用 BaseColumnList
                        .enableBaseResultMap()// 启用 BaseResultMap
                        // service 配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .serviceTemplate("/templates/service.java") // 设置 Service 模板
                        .serviceImplTemplate("/templates/serviceImpl.java") // 设置 ServiceImpl 模板
                        // controller 配置
                        .controllerBuilder()
                        .template("/templates/controller.java")
                        .formatFileName("%sController")
                        .enableHyphenStyle()
                        .enableRestStyle()
                )
                // 注入配置(设置扩展类的模板路径和包路径)
                .injectionConfig(consumer -> {

                    // 自定义文件
                    List<CustomFile> customFiles = new ArrayList<>();

                    // DTO文件生成
                    customFiles.add(new CustomFile
                            .Builder()
                            .fileName("DTO.java") // 创建的DTO文件后缀，例如sys_dept实体创建的是SysDeptDTO.java,其中DTO.java就是这个属性控制
                            .templatePath("/templates/dto.java.ftl") //指定生成模板路径
                            .packageName("dto") //包名,自3.5.10开始,可通过在package里面获取自定义包全路径,低版本下无法获取,示例:${package.DTO}
                            .enableFileOverride() // 开启覆盖已生成的文件
                            .build()
                    );

                    // VO文件生成
                    customFiles.add(new CustomFile.Builder()
                            .fileName("VO.java")
                            .templatePath("/templates/vo.java.ftl")
                            .packageName("dto.vo")
                            .enableFileOverride()
                            .build()
                    );

                    // BO文件生成
                    customFiles.add(new CustomFile.Builder()
                            .fileName("BO.java")
                            .templatePath("/templates/bo.java.ftl")
                            .packageName("dto.bo")
                            .enableFileOverride()
                            .build()
                    );

                    // 分页查询
                    customFiles.add(new CustomFile.Builder()
                            .fileName("PageQuery.java")
                            .templatePath("/templates/pageQuery.java.ftl")
                            .packageName("dto.page.query")
                            .enableFileOverride()
                            .build()
                    );
                    // 表单，用于新增和修改
                    customFiles.add(new CustomFile.Builder()
                            .fileName("Form.java")
                            .templatePath("/templates/form.java.ftl")
                            .packageName("dto.form")
                            .enableFileOverride()
                            .build()
                    );

                    // mapstruct接口文件
                    customFiles.add(new CustomFile.Builder()
                            .fileName("EntityMapper.java")
                            .templatePath("/templates/entityMapper.java.ftl")
                            .packageName("mapstruct")
                            .enableFileOverride()
                            .build()
                    );

                    consumer.customFile(customFiles);
                    consumer.beforeOutputFile((tableInfo, objectMap) -> {
                        // 为每个表生成首字母小写的实体名
                        String entityName = tableInfo.getEntityName();
                        String firstCharLowerCaseEntity = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
                        // 注入自定义参数
                        objectMap.put("firstCharLowerCaseEntity", firstCharLowerCaseEntity);
                        //
                        // objectMap.put("projectPackage", projectPackage);

                        // 检测需要导入的包
                        boolean needBigDecimal = tableInfo.getFields().stream()
                                .anyMatch(field -> "BigDecimal".equals(field.getPropertyType()));
                        objectMap.put("needBigDecimal", needBigDecimal);
                        boolean needLocalDateTime = tableInfo.getFields().stream()
                                .anyMatch(field -> "LocalDateTime".equals(field.getPropertyType()));
                        objectMap.put("needLocalDateTime", needLocalDateTime);
                        boolean needLocalDate = tableInfo.getFields().stream()
                                .anyMatch(field -> "LocalDate".equals(field.getPropertyType()));
                        objectMap.put("needLocalDate", needLocalDate);
                        boolean needLocalTime = tableInfo.getFields().stream()
                                .anyMatch(field -> "LocalTime".equals(field.getPropertyType()));
                        objectMap.put("needLocalTime", needLocalTime);

                        List<String> pojoPkgs = new ArrayList<>();
                        // DTO 包
                        pojoPkgs.add("com.feilong.im.entity." + entityName);
                        pojoPkgs.add("com.feilong.im.dto." + entityName + "DTO");
                        pojoPkgs.add("com.feilong.im.dto.bo." + entityName + "BO");
                        pojoPkgs.add("com.feilong.im.dto.vo." + entityName + "VO");
                        pojoPkgs.add("com.feilong.im.dto.form." + entityName + "Form");
                        pojoPkgs.add("com.feilong.im.dto.page.query." + entityName + "PageQuery");
                        pojoPkgs.add("com.feilong.im.core.Result");
                        objectMap.put("pojoPkgs", pojoPkgs);
                    });
                })
                // 使用 Freemarker 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())
                // 执行
                .execute();

        System.out.println("Done!");
        System.exit(0);
    }

    /**
     * 获取需要生成的表名列表
     * @return 表名数组
     */
    private static String[] getTables() {
        if (TABLE_NAMES.isEmpty()) {
            return new String[0];  // 空数组表示生成所有表
        }
        return TABLE_NAMES.toArray(new String[0]);
    }
}
