package com.feilong.im.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis 配置
 * @author cfl 2026/4/8
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 添加插件
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件（MySQL 数据库）
        PaginationInnerInterceptor  paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置单页最大条数限制，防止恶意请求查询全部数据
        paginationInterceptor.setMaxLimit(500L);
        // 溢出总页数后是否进行处理（默认不处理，抛出异常）
        paginationInterceptor.setOverflow(false);
        interceptor.addInnerInterceptor(paginationInterceptor);
        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }


    /**
     * 自定义MyBatis Plus的审计功能
     * @author cfl 2026/4/8
     */
    @Component
    public static class AuditMetaObjectHandler implements MetaObjectHandler {

        /** 插入时的填充策略 */
        @Override
        public void insertFill(MetaObject metaObject) {
            // 1. 填充创建时间和更新时间
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }

        /** 更新时的填充策略 */
        @Override
        public void updateFill(MetaObject metaObject) {
            // 1. 始终刷新更新时间
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
