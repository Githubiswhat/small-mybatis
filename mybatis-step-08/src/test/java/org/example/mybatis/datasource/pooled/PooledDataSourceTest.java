package org.example.mybatis.datasource.pooled;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class PooledDataSourceTest {

    private PooledDataSource pooledDataSourceUnderTest;

    @Before
    public void setUp() {
        pooledDataSourceUnderTest = new PooledDataSource();
    }

    @Test
    public void testPushConnection() throws Exception {
        // Setup
        final PooledDataSource dataSource = new PooledDataSource();
        dataSource.setLogger(null);
        dataSource.setPoolMaximumActiveConnection(0);
        dataSource.setPoolMaximumIdleConnection(0);
        dataSource.setPoolMaximumCheckoutTime(0);
        dataSource.setPoolTimeToWait(0);
        dataSource.setPoolPingQuery("poolPingQuery");
        dataSource.setPoolPingEnabled(false);
        dataSource.setPoolPingConnectionsNotUsedFor(0);
        dataSource.setExpectedConnectionTypeCode(0);
        final PooledConnection connection = new PooledConnection(null, dataSource);

        // Run the test
        pooledDataSourceUnderTest.pushConnection(connection);

        // Verify the results
    }

    @Test
    public void testPushConnection_ThrowsSQLException() {
        // Setup
        final PooledDataSource dataSource = new PooledDataSource();
        dataSource.setLogger(null);
        dataSource.setPoolMaximumActiveConnection(0);
        dataSource.setPoolMaximumIdleConnection(0);
        dataSource.setPoolMaximumCheckoutTime(0);
        dataSource.setPoolTimeToWait(0);
        dataSource.setPoolPingQuery("poolPingQuery");
        dataSource.setPoolPingEnabled(false);
        dataSource.setPoolPingConnectionsNotUsedFor(0);
        dataSource.setExpectedConnectionTypeCode(0);
        final PooledConnection connection = new PooledConnection(null, dataSource);

        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.pushConnection(connection));
    }

    @Test
    public void testPopConnection() throws Exception {
        // Setup
        final PooledDataSource dataSource = new PooledDataSource();
        dataSource.setLogger(null);
        dataSource.setPoolMaximumActiveConnection(0);
        dataSource.setPoolMaximumIdleConnection(0);
        dataSource.setPoolMaximumCheckoutTime(0);
        dataSource.setPoolTimeToWait(0);
        dataSource.setPoolPingQuery("poolPingQuery");
        dataSource.setPoolPingEnabled(false);
        dataSource.setPoolPingConnectionsNotUsedFor(0);
        dataSource.setExpectedConnectionTypeCode(0);
        final PooledConnection expectedResult = new PooledConnection(null, dataSource);

        // Run the test
        final PooledConnection result = pooledDataSourceUnderTest.popConnection("username", "password");

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPopConnection_ThrowsSQLException() {
        // Setup
        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.popConnection("username", "password"));
    }

    @Test
    public void testForceCloseAll() {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.forceCloseAll();

        // Verify the results
    }

    @Test
    public void testPingConnection() {
        // Setup
        final PooledDataSource dataSource = new PooledDataSource();
        dataSource.setLogger(null);
        dataSource.setPoolMaximumActiveConnection(0);
        dataSource.setPoolMaximumIdleConnection(0);
        dataSource.setPoolMaximumCheckoutTime(0);
        dataSource.setPoolTimeToWait(0);
        dataSource.setPoolPingQuery("poolPingQuery");
        dataSource.setPoolPingEnabled(false);
        dataSource.setPoolPingConnectionsNotUsedFor(0);
        dataSource.setExpectedConnectionTypeCode(0);
        final PooledConnection connection = new PooledConnection(null, dataSource);

        // Run the test
        final boolean result = pooledDataSourceUnderTest.pingConnection(connection);

        // Verify the results
        assertFalse(result);
    }

    @Test
    public void testUnwrapConnections() {
        // Setup
        final Connection connection = null;

        // Run the test
        final Connection result = PooledDataSource.unwrapConnections(connection);

        // Verify the results
    }

    @Test
    public void testFinalize() throws Throwable {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.finalize();

        // Verify the results
    }

    @Test
    public void testFinalize_ThrowsThrowable() {
        // Setup
        // Run the test
        assertThrows(Throwable.class, () -> pooledDataSourceUnderTest.finalize());
    }

    @Test
    public void testSetDriverClassName() {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.setDriverClassName("driver");

        // Verify the results
    }

    @Test
    public void testSetUrl() {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.setUrl("url");

        // Verify the results
    }

    @Test
    public void testSetUsername() {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.setUsername("username");

        // Verify the results
    }

    @Test
    public void testSetPassword() {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.setPassword("password");

        // Verify the results
    }

    @Test
    public void testGetConnection1() throws Exception {
        // Setup
        // Run the test
        final Connection result = pooledDataSourceUnderTest.getConnection();

        // Verify the results
    }

    @Test
    public void testGetConnection1_ThrowsSQLException() {
        // Setup
        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.getConnection());
    }

    @Test
    public void testGetConnection1_ThrowsSQLTimeoutException() {
        // Setup
        // Run the test
        assertThrows(SQLTimeoutException.class, () -> pooledDataSourceUnderTest.getConnection());
    }

    @Test
    public void testGetConnection2() throws Exception {
        // Setup
        // Run the test
        final Connection result = pooledDataSourceUnderTest.getConnection("username", "password");

        // Verify the results
    }

    @Test
    public void testGetConnection2_ThrowsSQLException() {
        // Setup
        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.getConnection("username", "password"));
    }

    @Test
    public void testGetConnection2_ThrowsSQLTimeoutException() {
        // Setup
        // Run the test
        assertThrows(SQLTimeoutException.class, () -> pooledDataSourceUnderTest.getConnection("username", "password"));
    }

    @Test
    public void testGetLogWriter() throws Exception {
        // Setup
        // Run the test
        final PrintWriter result = pooledDataSourceUnderTest.getLogWriter();

        // Verify the results
    }

    @Test
    public void testGetLogWriter_ThrowsSQLException() {
        // Setup
        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.getLogWriter());
    }

    @Test
    public void testSetLogWriter() throws Exception {
        // Setup
        final PrintWriter out = new PrintWriter(new ByteArrayOutputStream(), false, StandardCharsets.UTF_8);

        // Run the test
        pooledDataSourceUnderTest.setLogWriter(out);

        // Verify the results
    }

    @Test
    public void testSetLogWriter_BrokenOut() {
        // Setup
        final PrintWriter out = new PrintWriter(new OutputStream() {

            private final IOException exception = new IOException("Error");

            @Override
            public void write(final int b) throws IOException {
                throw exception;
            }

            @Override
            public void flush() throws IOException {
                throw exception;
            }

            @Override
            public void close() throws IOException {
                throw exception;
            }
        }, false, StandardCharsets.UTF_8);

        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.setLogWriter(out));
    }

    @Test
    public void testSetLoginTimeout() throws Exception {
        // Setup
        // Run the test
        pooledDataSourceUnderTest.setLoginTimeout(0);

        // Verify the results
    }

    @Test
    public void testSetLoginTimeout_ThrowsSQLException() {
        // Setup
        // Run the test
        assertThrows(SQLException.class, () -> pooledDataSourceUnderTest.setLoginTimeout(0));
    }

    @Test
    public void testGetLoginTimeout() throws Exception {
        assertEquals(0, pooledDataSourceUnderTest.getLoginTimeout());
    }

    @Test
    public void testGetParentLogger() throws Exception {
        // Setup
        // Run the test
        final Logger result = pooledDataSourceUnderTest.getParentLogger();

        // Verify the results
    }

    @Test
    public void testGetParentLogger_ThrowsSQLFeatureNotSupportedException() {
        // Setup
        // Run the test
        assertThrows(SQLFeatureNotSupportedException.class, () -> pooledDataSourceUnderTest.getParentLogger());
    }

    @Test
    public void testUnwrap() {
        assertThrows(RuntimeException.class, () -> pooledDataSourceUnderTest.unwrap(String.class));
    }

    @Test
    public void testIsWrapperFor() throws Exception {
        assertFalse(pooledDataSourceUnderTest.isWrapperFor(String.class));
    }
}
