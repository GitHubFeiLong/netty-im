package com.feilong.im.util;

import com.feilong.im.dto.page.query.BasePageQuery;
import com.feilong.im.dto.page.query.DatabaseSortItem;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mybatis工具类
 * @author cfl 2026/4/15
 */
@Slf4j
public class MyBatisUtil {

    /**
     * 获取安全的排序字段
     * <p>mybatis xml中使用进行过滤排序参数</p>
     * @param query 查询对象
     * @return 安全的排序字段集合
     */
    public static List<DatabaseSortItem> getSafeSorts(BasePageQuery query) {
        if (query != null && query.getSorts() != null && !query.getSorts().isEmpty()) {
            log.debug("过滤前mybatis的排序字段是：{}", query.getSorts());
            List<DatabaseSortItem> safeSorts = query.getSorts().stream().filter(f -> isSafeIdentifier(f.getField())).collect(Collectors.toList());
            log.debug("过滤后mybatis的排序字段是：{}", safeSorts);
            return safeSorts;
        }
        return Collections.emptyList();
    }

    /**
     * 验证排序字段是否为安全的标识符
     * @param field 待验证的字段名
     * @return 是否安全
     */
    public static boolean isSafeIdentifier(String field) {
        // 只允许字母、数字、下划线，且不能以数字开头
        return field != null && !field.isEmpty() && field.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
}
