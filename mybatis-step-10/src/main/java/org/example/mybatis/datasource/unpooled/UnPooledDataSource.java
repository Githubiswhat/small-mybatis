package org.example.mybatis.datasource.unpooled;

import javassist.CtField;
import org.example.mybatis.io.Resources;

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

    private static Map<String, Driver> registeredDriverMap = new HashMap<String, Driver>();

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private Properties driverProperties;

    private Integer defaultTransactionIsolationLevel;

    private Boolean autoCommit;

    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()){
            Driver driver = drivers.nextElement();
            registeredDriverMap.put(driver.getClass().getName(), driver);
        }
    }

    public UnPooledDataSource() {
    }

    public UnPooledDataSource(String driver, String url, String username, String password) {
        this.driverClassName = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private class DriverProxy implements Driver{

        private Driver driver;

        public DriverProxy(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
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

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        Properties properties = new Properties();
        if (driverProperties != null){
            properties.putAll(properties);
        }
        if (username != null){
            properties.put("user", username);
        }
        if (password != null){
            properties.put("password", password);
        }
        return doGetConnection(properties);
    }

    private Connection doGetConnection(Properties properties) throws SQLException {
        initializeDriver();
        Connection connection = DriverManager.getConnection(url, properties);
        if (autoCommit != null && connection.getAutoCommit() != autoCommit){
            connection.setAutoCommit(autoCommit);
        }
        if (defaultTransactionIsolationLevel != null){
            connection.setTransactionIsolation(defaultTransactionIsolationLevel);
        }
        return connection;
    }

    private void initializeDriver() throws SQLException {
        if (! registeredDriverMap.containsKey(driverClassName)){
            try {
                Class<?> dirverClass = Resources.classForName(driverClassName);
                Driver driverInstance = (Driver) dirverClass.newInstance();
                DriverManager.registerDriver(new DriverProxy(driverInstance));
                registeredDriverMap.put(driverClassName, driverInstance);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
                throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
            }
        }
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
        throw new SQLException(this.getClass().getName() + " is not a wrapper. ");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    public static Map<String, Driver> getRegisteredDriverMap() {
        return registeredDriverMap;
    }

    public static void setRegisteredDriverMap(Map<String, Driver> registeredDriverMap) {
        UnPooledDataSource.registeredDriverMap = registeredDriverMap;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
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

    public Properties getDriverProperties() {
        return driverProperties;
    }

    public void setDriverProperties(Properties driverProperties) {
        this.driverProperties = driverProperties;
    }

    public Integer getDefaultTransactionIsolationLevel() {
        return defaultTransactionIsolationLevel;
    }

    public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
        this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
