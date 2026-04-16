package com.feilong.im.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;
import com.feilong.im.entity.BaseEntity;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;

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
     * 逻辑删除字段名
     */
    private static final String LOGIC_DELETE_COLUMN_NAME = "deleted";
    /**
     * 乐观锁字段名
     */
    private static final String VERSION_COLUMN_NAME = "version";

    /**
     * 需要生成的表名（留空则生成所有表）
     */
    private static final List<String> TABLE_NAMES = List.of(
            "im_conv"
            ,"im_conv_user"
            ,"im_message"
            ,"im_user"
    );

    /**
     * 文件覆盖配置
     */
    private static final Map<String, Boolean> FILE_OVERRIDE_MAP = Map.of(
            "entity", true,
            "mapper", true,
            "service", true,
            "controller", true
    );

    /**
     * 执行前，必须要先提交备份代码，保证代码覆盖也会有备份进行恢复！！！
     * @param args 参数
     */
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
                .strategyConfig(builder -> {
                    StrategyConfig.Builder strategyBuilder = builder
                            // 排除不需要生成的表 不能和 addInclude 同时使用
                            // .addExclude("flyway_schema_history")
                            // 设置需要生成的表名
                            .addInclude(getTables());

                    // 实体配置
                    var entityBuilder = strategyBuilder.entityBuilder()
                            .javaTemplate("/templates/entity.java")
                            .idType(IdType.AUTO)
                            // 数据库表字段映射到实体的命名策略，默认下划线转驼峰
                            .naming(NamingStrategy.underline_to_camel)
                            // 启用 java.io.Serial注解
                            .enableSerialAnnotation()
                            // 启用 lombok
                            .enableLombok(new ClassAnnotationAttributes("@Data", "lombok.Data"))
                            // 启用链式模型 @Accessors(chain = true)
                            .enableChainModel()
                            // 启用字段注解
                            .enableTableFieldAnnotation()
                            // 开启移除is前缀
                            .enableRemoveIsPrefix()
                            // 乐观锁字段名
                            .versionColumnName(VERSION_COLUMN_NAME)
                            // 逻辑删除字段名
                            .logicDeleteColumnName(LOGIC_DELETE_COLUMN_NAME)
                            // 审计字段
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE)
                            );
                    if (FILE_OVERRIDE_MAP.get("entity")) {
                        entityBuilder.enableFileOverride();
                    }

                    // mapper配置
                    Mapper.Builder mapperBuilder = strategyBuilder.mapperBuilder()
                            .mapperTemplate("/templates/mapper.java")
                            .mapperXmlTemplate("/templates/mapper.xml")
                            // 开启覆盖已生成的文件(小心数据丢失)
                            .enableFileOverride()
                            // 启用 BaseColumnList
                            .enableBaseColumnList()
                            // 启用 BaseResultMap
                            .enableBaseResultMap();
                    if (FILE_OVERRIDE_MAP.get("mapper")) {
                        mapperBuilder.enableFileOverride();
                    }

                    // service 配置
                    Service.Builder serviceBuilder = strategyBuilder.serviceBuilder()
                            .serviceTemplate("/templates/service.java") // 设置 Service 模板
                            .serviceImplTemplate("/templates/serviceImpl.java") // 设置 ServiceImpl 模板
                            .formatServiceFileName("%sService");
                    if (FILE_OVERRIDE_MAP.get("service")) {
                        serviceBuilder.enableFileOverride();
                    }

                    // controller 配置
                    Controller.Builder controllerBuilder = strategyBuilder.controllerBuilder()
                            .template("/templates/controller.java")
                            .enableFileOverride() // 开启覆盖已生成的文件
                            .formatFileName("%sController")
                            .enableHyphenStyle()
                            .enableRestStyle();
                    if (FILE_OVERRIDE_MAP.get("controller")) {
                        controllerBuilder.enableFileOverride();
                    }
                })
                // 注入配置(设置扩展类的模板路径和包路径)
                .injectionConfig(consumer -> {

                    // 自定义文件
                    List<CustomFile> customFiles = new ArrayList<>();

                    // DTO文件生成
                    customFiles.add(new CustomFile
                            .Builder()
                            // 创建的DTO文件后缀，例如sys_dept实体创建的是SysDeptDTO.java,其中DTO.java就是这个属性控制
                            // 可通过在package里面获取自定义包全路径,低版本下无法获取,示例:${package.DTO}
                            .fileName("DTO.java")
                            //指定生成模板路径
                            .templatePath("/templates/dto.java.ftl")
                            //包名，在全局配置的包目录下
                            .packageName("dto")
                            // 开启覆盖已生成的文件
                            .enableFileOverride()
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
                        if (tableInfo.getComment() == null || tableInfo.getComment().isBlank()) {
                            tableInfo.setComment(tableInfo.getName());
                        }

                        Map<String, TableField> tableFieldMap = tableInfo.getTableFieldMap();
                        if (tableFieldMap.containsKey(LOGIC_DELETE_COLUMN_NAME)) {
                            TableField tableField = tableFieldMap.get(LOGIC_DELETE_COLUMN_NAME);
                            String columnType = tableField.getColumnType().getType();
                            switch (columnType) {
                                case "Boolean":
                                    // 首字母大写
                                    objectMap.put("LOGIC_DELETE_COLUMN_NAME", LOGIC_DELETE_COLUMN_NAME);
                                    objectMap.put("LOGIC_DELETE_DEFAULT_VALUE", "false");
                                    break;
                                case "Long":
                                    objectMap.put("LOGIC_DELETE_COLUMN_NAME", LOGIC_DELETE_COLUMN_NAME);
                                    objectMap.put("LOGIC_DELETE_DEFAULT_VALUE", "0L");
                                    break;
                                default:
                            }
                        }

                        // 注入自定义参数
                        List<String> importEntityPackages = new ArrayList<>();
                        if (tableInfo.getFields().stream().anyMatch(field -> "BigDecimal".equals(field.getPropertyType()))) {
                            importEntityPackages.add("java.math.BigDecimal");
                        }
                        if (tableInfo.getFields().stream().anyMatch(field -> "LocalDate".equals(field.getPropertyType()))) {
                            importEntityPackages.add("java.time.LocalDate");
                        }
                        if (tableInfo.getFields().stream().anyMatch(field -> "LocalDateTime".equals(field.getPropertyType()))) {
                            importEntityPackages.add("java.time.LocalDateTime");
                        }
                        if (tableInfo.getFields().stream().anyMatch(field -> "LocalTime".equals(field.getPropertyType()))) {
                            importEntityPackages.add("java.time.LocalTime");
                        }
                        objectMap.put("importEntityPackages", importEntityPackages);

                        // DTO VO PO FORM BO PageQuery 包
                        List<String> pojoPkgs = new ArrayList<>();
                        pojoPkgs.add("com.feilong.im.entity." + entityName);
                        pojoPkgs.add("com.feilong.im.dto." + entityName + "DTO");
                        pojoPkgs.add("com.feilong.im.dto.bo." + entityName + "BO");
                        pojoPkgs.add("com.feilong.im.dto.vo." + entityName + "VO");
                        pojoPkgs.add("com.feilong.im.dto.form." + entityName + "Form");
                        pojoPkgs.add("com.feilong.im.dto.page.query." + entityName + "PageQuery");
                        objectMap.put("pojoPkgs", pojoPkgs);

                        objectMap.put("importResult", "com.feilong.im.core.Result");
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
