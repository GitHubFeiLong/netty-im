<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
    <!-- ${table.comment}分页列表 -->
    <select id="page" resultType="${package.Parent}.dto.bo.${entity}BO">
        <!-- 使用bind标签调用Java静态方法并将结果存储到变量 -->
        <bind name="safeSorts" value="@com.feilong.im.util.MyBatisUtil@getSafeSorts(pageQuery)"/>
        SELECT
        <include refid="Base_Column_List"/>
        FROM
        `${table.name}`
        <where>
            <#list table.fields as field>
                <#if field.keyFlag>
                    <if test="pageQuery.${field.propertyName} != null">
                        AND `${field.name}` = ${r"#{"}pageQuery.${field.propertyName}${r"}"}
                    </if>
                </#if>
            </#list>
            <#list table.fields as field>
                <#if !field.keyFlag>
                    <#if field.propertyType == "String">
                        <if test="pageQuery.${field.propertyName} != null and pageQuery.${field.propertyName}.trim().length > 0">
                            AND `${field.name}` LIKE CONCAT('%',  ${r"#{"}pageQuery.${field.propertyName}${r"}"},'%')
                        </if>
                    <#else>
                        <if test="pageQuery.${field.propertyName} != null">
                            AND `${field.name}` =  ${r"#{"}pageQuery.${field.propertyName}${r"}"}
                        </if>
                    </#if>
                </#if>
            </#list>
            <if test="pageQuery.startTime != null">
                AND `created_time` &gt;=  ${r"#{"}pageQuery.startTime${r"}"}
            </if>
            <if test="pageQuery.endTime != null">
                AND `created_time` &lt;= ${r"#{"}pageQuery.endTime${r"}"}
            </if>
        </where>
        <if test="safeSorts != null and safeSorts.size() > 0">
            ORDER BY
            <foreach item="sort" index="index" collection="safeSorts" separator=",">
                ${r"${sort.field}"} ${r"${sort.order}"}
            </foreach>
        </if>
    </select>
</mapper>
