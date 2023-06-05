package org.example.mybatis.datasource.unpooled;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class UnPooledDataSource implements DataSource {

    private ClassLoader driverClassLoader;

    private Properties driverProperties;

    private static Map<String, Driver> registeredDrivers = new HashMap<String, Driver>();

    private Boolean autoCommit;

    private Integer defaultTransactionIsolationLevel;

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()){
            Driver driver = drivers.nextElement();
            registeredDrivers.put(driver.getClass().getName(), driver);
        }
    }

    private static class DriverProxy implements Driver{

        private Driver driver;

        public DriverProxy(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return this.driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return this.driver.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return this.driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(username, password);
    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        Properties properties = new Properties();
        if (driverProperties != null){
            properties.putAll(driverProperties);
        }
        if (username != null) {
            properties.put("user", username);
        }
        if (password != null) {
            properties.put("password", password);
        }
        return doGetConnection(properties);
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        initializeDriver();
        Connection connection = DriverManager.getConnection(url, properties);
        if (autoCommit != null && autoCommit != connection.getAutoCommit()){
            connection.setAutoCommit(autoCommit);
        }

        if (defaultTransactionIsolationLevel != null){
            connection.setTransactionIsolation(defaultTransactionIsolationLevel);
        }
        return connection;
    }

    private void initializeDriver() {
        if (! registeredDrivers.containsKey(driverClassName)){
            try {
                Class<?> driverType = Class.forName(driverClassName, true, driverClassLoader);
                Driver driver = (Driver) driverType.newInstance();
                DriverManager.registerDriver(new DriverProxy(driver));
                registeredDrivers.put(driverClassName, driver);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new RuntimeException(this.getClass().getName() + "is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    public Properties getDriverProperties() {
        return driverProperties;
    }

    public void setDriverProperties(Properties driverProperties) {
        this.driverProperties = driverProperties;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Integer getDefaultTransactionIsolationLevel() {
        return defaultTransactionIsolationLevel;
    }

    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    }

    public static Map<String, Driver> getRegisteredDrivers() {
        return registeredDrivers;
    }

    public static void setRegisteredDrivers(Map<String, Driver> registeredDrivers) {
        UnPooledDataSource.registeredDrivers = registeredDrivers;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
