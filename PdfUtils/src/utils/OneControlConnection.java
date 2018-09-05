/**
 * 
 */
package utils;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author prdubey
 * 
 */
public abstract class OneControlConnection {

    public static final String JDBC_DRIVER_KEY = "JDBC_DRIVER_KEY";
    public static final String DB_URL_KEY = "DB_URL";

    // Database credentials
    public static final String USER_KEY = "username";
    public static final String PASS_PWD_KEY = "password";

    public String configFile;

    private boolean isOracle = false;
    protected Connection connection = null;

    public String getConfigFile() {
	return configFile;
    }

    public void setConfigFile(String configFile) {
	this.configFile = configFile;
    }

    public boolean isOracle() {
	return isOracle;
    }

    public void setOracle(boolean isOracle) {
	this.isOracle = isOracle;
    }

    /**
     * Create Connection related parameters from the Property file
     * 
     * @param connectionPropertyPath
     *            path of the property
     * @return
     */
    public Properties getConnectionProperties(String connectionPropertyPath) {
	Properties properties = new Properties();
	FileInputStream fileInputStream = null;
	try {

	    fileInputStream = new FileInputStream(connectionPropertyPath);
	    properties.load(fileInputStream);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return properties;
    }

    /**
     * Create Connection
     * 
     * @param config
     *            Configuration
     * @return Connection
     */
    protected Connection createConnection(Properties config) {
	String JDBC_DRIVER = null;
	String DB_URL = null;
	String USER = null;
	String PASS = null;
	Connection connection = null;

	try {
	    if (config != null && !config.isEmpty()) {

		JDBC_DRIVER = config.getProperty(JDBC_DRIVER_KEY);
		DB_URL = config.getProperty(DB_URL_KEY);
		USER = config.getProperty(USER_KEY);
		PASS = config.getProperty(PASS_PWD_KEY);

		Class.forName(JDBC_DRIVER);
		connection = DriverManager.getConnection(DB_URL, USER, PASS);

	    } else {
		System.out.println("Configuration for connection not fetched properly");
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return connection;
    }

    public abstract Connection getConnection(String fileName);

    public abstract Connection getConnection();

    /**
     * @param connection
     * @param stmt
     */
    public void cleanResources(ResultSet rs, Connection connection, Statement stmt, PreparedStatement preparedStatement) {
	// finally block used to close resources
	try {
	    if (rs != null)
		rs.close();
	} catch (SQLException se) {
	}// do nothing

	try {
	    if (stmt != null)
		stmt.close();
	} catch (SQLException se) {
	}// do nothing

	try {
	    if (preparedStatement != null)
		preparedStatement.close();
	} catch (SQLException se) {
	}

	try {
	    if (connection != null)
		connection.close();
	} catch (SQLException se) {
	    se.printStackTrace();
	}
    }

}
