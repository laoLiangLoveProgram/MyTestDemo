package cn.fivestars.pagehelperdemo.inceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Mybatis Sql 打印
 *
 * @author liangchenzhou1024@163.com
 * @version 1.0
 * @date 2020/12/20
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@ConditionalOnProperty(prefix = "slow-sql", name = "enabled", havingValue = "true")
public class MybatisSqlLogInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisSqlLogInterceptor.class);

    private static final Long SECOND = 1000L;

    @Value("${slow-sql.timeout-second:3}")
    private Long timeout;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        long start = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) target;
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;

            BoundSql boundSql = statementHandler.getBoundSql();
            // 格式化Sql语句，去除换行符，替换参数
            String sql = formatSql(boundSql);
            if (time > timeout * SECOND) {
                logger.error("[WARN] SQL execute const {} ms, SQL:[{}]", time, sql);
            } else {
                logger.debug("[DEBUG] SQL execute const {} ms, SQL:[{}]", time, sql);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String formatSql(BoundSql boundSql) {
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

        // 输入sql字符串空判断
        if (sql == null || sql.length() == 0) {
            return "";
        }

        // 美化sql
        sql = beautifySql(sql);

        // 不传参数的场景，直接把Sql美化一下返回出去
        if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
            return sql;
        }

        // 定义一个没有替换过占位符的sql，用于出异常时返回
        String sqlWithoutReplacePlaceholder = sql;

        try {
            Class<?> parameterObjectClass = parameterObject.getClass();
            sql = handleParameters(boundSql);
        } catch (Exception e) {
            // 占位符替换过程中出现异常，则返回没有替换过占位符但是格式美化过的sql，这样至少保证sql语句比BoundSql中的sql更好看
            return sqlWithoutReplacePlaceholder;
        }

        return sql;
    }

    /**
     * 美化Sql
     */
    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n ]+", " ");
    }

    /**
     * 处理通用的场景
     */
    private String handleParameters(BoundSql boundSql) throws Exception {
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (ParameterMapping parameterMapping : parameterMappings) {
            String propertyName = parameterMapping.getProperty();
            String propertyValue = handleParameterMapping(boundSql, parameterObject, propertyName);
            sql = sql.replaceFirst("\\?", propertyValue);
        }

        return beautifySql(sql);
    }


    private String handleParameterMapping(BoundSql boundSql, Object parameterObject, String propertyName) {
        Class<?> parameterObjectClass = parameterObject.getClass();
        if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
            return parameterObject.toString();
        } else if (String.class.isAssignableFrom(parameterObjectClass)) {
            return "'" + parameterObject + "'";
        } else if (Enum.class.isAssignableFrom(parameterObjectClass)) {
            // 枚举存int值
            return String.valueOf(((Enum) parameterObject).ordinal());
        } else if (parameterObject instanceof String[]) {
            // 自定义的TypeHandler处理数组
            return String.join(",", ((String[]) parameterObject));
        } else if (parameterObject instanceof Date || Temporal.class.isAssignableFrom(parameterObjectClass)) {
            // 日期处理
            return "'" + parameterObject.toString() + "'";
        } else if (boundSql.hasAdditionalParameter(propertyName)) {
            Object propertyValue = boundSql.getAdditionalParameter(propertyName);
            if (String.class.isAssignableFrom(parameterObjectClass)) {
                return "'" + propertyValue + "'";
            } else {
                return String.valueOf(propertyValue);
            }
        } else if (Map.class.isAssignableFrom(parameterObjectClass)) {
            return handleMap(boundSql, (Map<String, Object>) parameterObject, propertyName);
        } else {
            return handleObj(boundSql, parameterObject, propertyName, parameterObjectClass);
        }
    }

    private String handleObj(BoundSql boundSql, Object parameterObject, String propertyName, Class<?> parameterObjectClass) {
        if (propertyName.contains(".")) {
            String first = propertyName.substring(0, propertyName.indexOf("."));
            String last = propertyName.substring(propertyName.indexOf(".") + 1);
            try {
                Field field = parameterObjectClass.getDeclaredField(first);
                field.setAccessible(true);
                Object subObj = field.get(parameterObject);
                return handleParameterMapping(boundSql, subObj, last);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return "?";
            }
        } else {
            try {
                Field field = parameterObjectClass.getDeclaredField(propertyName);
                Class<?> type = field.getType();
                field.setAccessible(true);
                Object propertyValue = field.get(parameterObject);
                if (String.class.isAssignableFrom(type)) {
                    return "'" + propertyValue + "'";
                } else {
                    return String.valueOf(propertyValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return "?";
            }
        }
    }

    private String handleMap(BoundSql boundSql, Map<String, Object> parameterObject, String propertyName) {
        if (propertyName.contains(".")) {
            String first = propertyName.substring(0, propertyName.indexOf("."));
            String last = propertyName.substring(propertyName.indexOf(".") + 1);
            Object subObj = parameterObject.get(first);
            return handleParameterMapping(boundSql, subObj, last);
        } else {
            Object propertyValue = parameterObject.get(propertyName);
            if (propertyValue != null && String.class.isAssignableFrom(propertyValue.getClass())) {
                return "'" + propertyValue + "'";
            } else if (propertyValue != null) {
                return String.valueOf(propertyValue);
            } else {
                return "?";
            }
        }
    }


    /**
     * 是否基本数据类型或者基本数据类型的包装类
     */
    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive() ||
                (parameterObjectClass.isAssignableFrom(Byte.class) || parameterObjectClass.isAssignableFrom(Short.class) ||
                        parameterObjectClass.isAssignableFrom(Integer.class) || parameterObjectClass.isAssignableFrom(Long.class) ||
                        parameterObjectClass.isAssignableFrom(Double.class) || parameterObjectClass.isAssignableFrom(Float.class) ||
                        parameterObjectClass.isAssignableFrom(Character.class) || parameterObjectClass.isAssignableFrom(Boolean.class));
    }

    /**
     * 是否DefaultSqlSession的内部类StrictMap
     */
    private boolean isStrictMap(Class<?> parameterObjectClass) {
        return DefaultSqlSession.StrictMap.class.isAssignableFrom(parameterObjectClass);
    }

    /**
     * 是否List的实现类
     */
    private boolean isList(Class<?> clazz) {
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (List.class.isAssignableFrom(interfaceClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否Map的实现类
     */
    private boolean isMap(Class<?> parameterObjectClass) {
        Class<?>[] interfaceClasses = parameterObjectClass.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (Map.class.isAssignableFrom(interfaceClass)) {
                return true;
            }
        }

        return false;
    }
}