package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {


    private static Connection conn = null;

    public static Connection getConnetion() {
        if (conn == null) {
            try {
                MysqlDataSource dataSource = new MysqlDataSource();

                Properties props = loadProperties();

                dataSource.setAllowPublicKeyRetrieval(Boolean.parseBoolean(props.getProperty("AllowPublicKeyRetrieval")));
                dataSource.setUseSSL(Boolean.parseBoolean(props.getProperty("useSSL")));
                dataSource.setServerName(props.getProperty("server"));
                dataSource.setDatabaseName(props.getProperty("database"));
                dataSource.setPortNumber(Integer.parseInt(props.getProperty("port")));
                dataSource.setUser(props.getProperty("user"));
                dataSource.setPassword(props.getProperty("password"));

                conn = dataSource.getConnection();

                return conn;
                //String url = props.getProperty("dburl");
                //conn = DriverManager.getConnection(url, props);

            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet (ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
