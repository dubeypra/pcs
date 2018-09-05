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
public class OneControlOracleConnection extends OneControlConnection {
    
    public OneControlOracleConnection(){}
    
    public OneControlOracleConnection(String fileName){
	setConfigFile(fileName);
	setOracle(true);
    }

	public Connection getConnection(String fileName) {
		Connection connection = null;
		Properties config = getConnectionProperties(fileName);
		
		connection = createConnection(config);
		
		return connection;
	}
	
	
    public Connection getConnection()  {
	try {
	    if (connection == null ||connection.isClosed()) {
	        Properties config = getConnectionProperties(getConfigFile());

	        connection = createConnection(config);
	    }
        } catch (SQLException e) {
	    e.printStackTrace();
        }

	return connection;
    }


}
