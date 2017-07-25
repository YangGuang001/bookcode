package org.smart4j.framework.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.PropsUtil.CollectionUtil;
import org.smart4j.framework.util.PropsUtil.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yz on 2017/7/22.
 */
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();

        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 查询所有的条数
     * @param sql
     * @param entryClass
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntryList(String sql, Class<T> entryClass, Object... params){
        List<T> entryList;
        try {
            Connection conn = getConnection();
            entryList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entryClass), params);
        }catch (SQLException e){
            LOGGER.error("query entry list failure", e);
            throw new RuntimeException(e);
        }
        return entryList;
    }

    /**
     * 查询单条语句
     * @param sql
     * @param entryclass
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntry(String sql, Class<T> entryclass, Object... params){
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entryclass), params);
        } catch (SQLException e) {
            LOGGER.error("query entry failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object...params){
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int executeUpdate(String sql, Object... params){
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static <T> boolean insertEntry(Class<T> entryClass, Map<String, Object> filedMap){
        if (CollectionUtil.isEmpty(filedMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "insert into " + getTableName(entryClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : filedMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + "VALUES " + values;

        Object[] params = filedMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "update " + getTableName(entityClass) + " set";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " where id=?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean deleteEntry(Class<T> entityClass, long id){
        String sql = "delete from " + getTableName(entityClass) + " where id=?";
        return executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null){
                DatabaseHelper.executeUpdate(sql);
            }
        }catch (IOException e){
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        try {
            conn = DATA_SOURCE.getConnection();
        }catch (SQLException e){
            LOGGER.error("get connection failure", e);
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.set(conn);
        }
        return conn;
    }


    /**
     * 开启事物
     */
    public static void beginTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }

    /**
     * 提交事物
     */
    public static void commitTrasaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
}
