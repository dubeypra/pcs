/**
 * 
 */
package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class will provide the connection Instance of the provide database
 * 
 * @author prdubey
 * 
 */
public class OneControlSqlConnection extends OneControlConnection {

    public OneControlSqlConnection() {
    };

    public OneControlSqlConnection(String fileName) {
	setConfigFile(fileName);
	setOracle(false);
    }

    public Connection getConnection(String fileName) {
	Connection connection = null;
	Properties config = getConnectionProperties(fileName);

	connection = createConnection(config);

	return connection;
    }

    public Connection getConnection() {
	try {
	    if (connection == null || connection.isClosed()) {
	        Properties config = getConnectionProperties(getConfigFile());

	        connection = createConnection(config);
	    }
        } catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
        }

	return connection;
    }
}
