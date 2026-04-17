package com.feilong.im.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author cfl 2026/04/14
 */
@Slf4j
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
            "sys_user"
    );

    /**
     * 文件覆盖配置
     */
    private static final Map<String, Boolean> FILE_OVERRIDE_MAP;

    static {
        Map<String, Boolean> map = new LinkedHashMap<>();
        map.put("entity", true);
        map.put("mapper", true);
        map.put("service", true);
        map.put("controller", true);
        map.put("dto", true);
        map.put("vo", true);
        map.put("bo", true);
        map.put("pageQuery", true);
        map.put("form", true);
        map.put("formSave", true);
        map.put("formUpdate", true);
        map.put("entityMapper", true);
        FILE_OVERRIDE_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * 执行前，必须要先提交备份代码，保证代码覆盖也会有备份进行恢复！！！
     * @param args 参数
     */
    public static void main(String[] args) {
        // 执行前
        if (!beforeGenerator()) {
            return;
        }

        // 开始执行
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
                            // .addExclude("flyway_schema_history", "sys_app")
                            // 设置需要生成的表名
                            .addInclude(getTables())
                        ;

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
                    if (FILE_OVERRIDE_MAP.getOrDefault("entity", false)) {
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
                    if (FILE_OVERRIDE_MAP.getOrDefault("mapper", false)) {
                        mapperBuilder.enableFileOverride();
                    }

                    // service 配置
                    Service.Builder serviceBuilder = strategyBuilder.serviceBuilder()
                            .serviceTemplate("/templates/service.java") // 设置 Service 模板
                            .serviceImplTemplate("/templates/serviceImpl.java") // 设置 ServiceImpl 模板
                            .formatServiceFileName("%sService");
                    if (FILE_OVERRIDE_MAP.getOrDefault("service", false)) {
                        serviceBuilder.enableFileOverride();
                    }

                    // controller 配置
                    Controller.Builder controllerBuilder = strategyBuilder.controllerBuilder()
                            .template("/templates/controller.java")
                            .enableFileOverride() // 开启覆盖已生成的文件
                            .formatFileName("%sController")
                            .enableHyphenStyle()
                            .enableRestStyle();
                    if (FILE_OVERRIDE_MAP.getOrDefault("controller", false)) {
                        controllerBuilder.enableFileOverride();
                    }
                })
                // 注入配置(设置扩展类的模板路径和包路径)
                .injectionConfig(consumer -> {

                    List<CustomFile> customFiles = getCustomFiles();

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
                        pojoPkgs.add("com.feilong.im.dto.form." + entityName + "SaveForm");
                        pojoPkgs.add("com.feilong.im.dto.form." + entityName + "UpdateForm");
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
     * 添加自定义代码模板
     * @return 自定义文件集合
     */
    private static List<CustomFile> getCustomFiles() {
        log.info("添加自定义模板文件: DTO、VO、BO、PageQuery、Form、SaveForm、UpdateForm、EntityMapper");
        // 自定义文件
        List<CustomFile> customFiles = new ArrayList<>();

        // DTO文件生成
        CustomFile.Builder dtoFileBuilder = new CustomFile
                .Builder()
                // 创建的DTO文件后缀，例如sys_dept实体创建的是SysDeptDTO.java,其中DTO.java就是这个属性控制
                // 可通过在package里面获取自定义包全路径,低版本下无法获取,示例:${package.DTO}
                .fileName("DTO.java")
                //指定生成模板路径
                .templatePath("/templates/dto.java.ftl")
                //包名，在全局配置的包目录下
                .packageName("dto")
                ;
        if (FILE_OVERRIDE_MAP.getOrDefault("dto", false)) {
            dtoFileBuilder.enableFileOverride();
        }
        CustomFile dtoFile = dtoFileBuilder.build();
        customFiles.add(dtoFile);
        log.info("DTO文件配置：{}", dtoFile);


        // VO文件生成
        CustomFile.Builder voFileBuilder = new CustomFile
                .Builder()
                .fileName("VO.java")
                .templatePath("/templates/vo.java.ftl")
                .packageName("dto.vo")
                ;
        if (FILE_OVERRIDE_MAP.getOrDefault("vo", false)) {
            voFileBuilder.enableFileOverride();
        }
        CustomFile voFile = voFileBuilder.build();
        customFiles.add(voFile);
        log.info("VO文件配置：{}", voFile);


        // BO文件生成
        CustomFile.Builder boFileBuilder = new CustomFile.Builder()
                .fileName("BO.java")
                .templatePath("/templates/bo.java.ftl")
                .packageName("dto.bo");
        if (FILE_OVERRIDE_MAP.getOrDefault("bo", false)) {
            boFileBuilder.enableFileOverride();
        }
        CustomFile boFile = boFileBuilder.build();
        customFiles.add(boFile);
        log.info("BO文件配置：{}", boFile);


        // PageQuery文件生成
        CustomFile.Builder pageQueryFileBuilder = new CustomFile.Builder()
                .fileName("PageQuery.java")
                .templatePath("/templates/pageQuery.java.ftl")
                .packageName("dto.page.query");
        if (FILE_OVERRIDE_MAP.getOrDefault("pageQuery", false)) {
            pageQueryFileBuilder.enableFileOverride();
        }
        CustomFile pageQueryFile = pageQueryFileBuilder.build();
        customFiles.add(pageQueryFile);
        log.info("PageQuery文件配置：{}", pageQueryFile);


        // 表单
        CustomFile.Builder formFileBuilder = new CustomFile.Builder()
                .fileName("Form.java")
                .templatePath("/templates/form.java.ftl")
                .packageName("dto.form");
        if (FILE_OVERRIDE_MAP.getOrDefault("form", false)) {
            formFileBuilder.enableFileOverride();
        }
        CustomFile formFile = formFileBuilder.build();
        customFiles.add(formFile);
        log.info("Form文件配置：{}", formFile);


        // 表单，用于新增
        CustomFile.Builder saveFormFileBuilder = new CustomFile.Builder()
                .fileName("SaveForm.java")
                .templatePath("/templates/formSave.java.ftl")
                .packageName("dto.form");
        if (FILE_OVERRIDE_MAP.getOrDefault("saveForm", false)) {
            saveFormFileBuilder.enableFileOverride();
        }
        CustomFile saveFormFile = saveFormFileBuilder.build();
        customFiles.add(saveFormFile);
        log.info("SaveForm文件配置：{}", saveFormFile);


        // 表单，用于修改
        CustomFile.Builder updateFormFileBuilder = new CustomFile.Builder()
                .fileName("UpdateForm.java")
                .templatePath("/templates/formUpdate.java.ftl")
                .packageName("dto.form");
        if (FILE_OVERRIDE_MAP.getOrDefault("updateForm", false)) {
            updateFormFileBuilder.enableFileOverride();
        }
        CustomFile updateFormFile = updateFormFileBuilder.build();
        customFiles.add(updateFormFile);
        log.info("UpdateForm文件配置：{}", updateFormFile);


        // mapstruct接口文件
        CustomFile.Builder entityMapperFileBuilder = new CustomFile.Builder()
                .fileName("EntityMapper.java")
                .templatePath("/templates/entityMapper.java.ftl")
                .packageName("mapstruct");
        if (FILE_OVERRIDE_MAP.getOrDefault("entityMapper", false)) {
            entityMapperFileBuilder.enableFileOverride();
        }
        CustomFile entityMapperFile = entityMapperFileBuilder.build();
        customFiles.add(entityMapperFile);
        log.info("EntityMapper文件配置：{}", entityMapperFile);

        return customFiles;
    }

    /**
     * 执行前打印注意事项,和确认是否需要真的执行
     * @return true-执行，false-取消
     */
    private static boolean beforeGenerator() {
        log.info("即将执行代码生成,相关参数将打印");
        log.error("注意:生成代码前,请将代码提交备份!!!");
        log.error("注意:生成代码前,请将代码提交备份!!!");
        log.error("注意:生成代码前,请将代码提交备份!!!");

        log.warn("本次需要生成的表如下：\n{}", String.join("\n", getTables()));

        log.error("注意:生成代码前,请将代码提交备份!!!");
        // 格式化输出文件覆盖配置（对齐显示）
        log.info("文件覆盖配置 FILE_OVERRIDE_MAP:\n{}", printAlignedMap(FILE_OVERRIDE_MAP));

        Scanner scanner = new Scanner(System.in);
        log.error("注意:生成代码前,请将代码提交备份!!!");
        log.info("请输入是否需要覆盖已生成的文件(y/n)：");
        String input = scanner.next();
        boolean isRun = "y".equals(input) || "Y".equals(input);
        if (isRun) {
            log.info("即将开始执行代码生成");
        } else {
            log.info("已取消代码生成");
        }
        return isRun;
    }

    /**
     * 获取需要生成的表名列表
     * @return 表名数组
     */
    private static String[] getTables() {
        if (TABLE_NAMES.isEmpty()) {
            // 空数组表示生成所有表
            return new String[0];
        }
        return TABLE_NAMES.stream().filter(f -> !f.isBlank()).distinct().toArray(String[]::new);
    }

    /**
     * 格式化打印 Map，键值对对齐显示
     * @param map 要打印的 Map
     */
    private static String printAlignedMap(Map<String, Boolean> map) {
        if (map == null || map.isEmpty()) {
            log.info("  (empty)");
            return "";
        }

        // 计算最长键的长度
        int maxKeyLength = map.keySet().stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        // 格式化输出，左对齐
        String format = "  %-" + maxKeyLength + "s = %s";
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) ->
            sb.append(String.format(format, key, value)).append("\n")
        );

        return sb.toString();
    }
}
