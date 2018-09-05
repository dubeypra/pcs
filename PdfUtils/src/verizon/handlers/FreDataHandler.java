package verizon.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import telestra.TscConnectionDetails;
import utils.OneControlConnection;
import verizon.entity.OneControlSql;

public class FreDataHandler extends Handler {
    
    public FreDataHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }
    
    
    public void SychAllConnectionParemeters(){
   	List<TscConnectionDetails> connectionDetails;
   	/**
   	 * FRE Update based on Connection Key. This will update both the
   	 * UMS_ALIAS_NAME and NATIVE_NAME
   	 */
   	
   	  connectionDetails = retrieveFreDataBasedOnFreKeyFromActive(); 
   	  updateFreDataBasedOnConnectionNameOnBakup(connectionDetails);
   	 
   	  /**
   	   * UI_Connections Updated
   	   * data fetched from Active Server and forcefully updated in the back up server
   	   */
   	 matchUIConnections();
   	
       }

       /**
        * @return : Fetch List of Connections Details
        */
       public List<TscConnectionDetails> retrieveFreDataBasedOnFreKeyFromActive() {
   	List<TscConnectionDetails> tscConnectionDetails = new ArrayList();

   	OneControlConnection oneControlConnection = getActiveDb();

   	Connection connection = oneControlConnection.getConnection();

   	PreparedStatement preparedStatement = null;
   	ResultSet rs = null;
   	TscConnectionDetails connectionDetails;
   	try {
   	    preparedStatement = connection.prepareStatement(OneControlSql.GET_FRE_KEY_NAME_ACTIVE);
   	    rs = preparedStatement.executeQuery();

   	    while (rs.next()) {
   		connectionDetails = new TscConnectionDetails();

   		connectionDetails.setFreKey(rs.getString(1));
   		connectionDetails.setDisplayNetworkName(rs.getString(2));
   		connectionDetails.setDisplayName(rs.getString(3));
   		connectionDetails.setLabel(rs.getString(4));
   		connectionDetails.setCustomer(rs.getString(5));
   		connectionDetails.setFreIsManaged(rs.getString(6).charAt(0));

   		System.out.println(" FRE: Data Extracted from Active Server  " + connectionDetails);

   		tscConnectionDetails.add(connectionDetails);
   	    }

   	    rs.close();
   	    preparedStatement.close();
   	    connection.close();

   	    System.out.println("Data Retrieved !!");

   	} catch (SQLException e) {

   	    e.printStackTrace();
   	} finally {
   	    oneControlConnection.cleanResources(rs, connection, null, preparedStatement);

   	}

   	return tscConnectionDetails;
       }

       /**
        * Update the Connection State on back server based on the state at the
        * active server
        * 
        * @return
        */
       public boolean updateUIConnectionsDataonBackUp(List<TscConnectionDetails> connectionNameList) {

   	boolean isTransactionSuccessfull = false;

   	int updatedCount = 0;

   	OneControlConnection oneControlConnection = getTargetDb();
   	Connection connection = oneControlConnection.getConnection();

   	PreparedStatement preparedStatement = null;

   	System.out.println("####### Total No of UI_Connections Details retrieved from Active for update : " + connectionNameList.size());

   	try {
   	    for (TscConnectionDetails tscConnectionDetails : connectionNameList) {

   		try {
   		    preparedStatement = connection.prepareStatement(OneControlSql.UPDATE_UI_CONNECTIONS_BACKUP);

   		    preparedStatement.setString(1, String.valueOf(tscConnectionDetails.getUiInProvisioning()));

   		    preparedStatement.setString(2, tscConnectionDetails.getDisplayName());

   		    preparedStatement.setString(3, tscConnectionDetails.getDisplayNetworkName());

   		    preparedStatement.setString(4, tscConnectionDetails.getConnectionKey());

   		    preparedStatement.setLong(5, tscConnectionDetails.getFreId());

   		    int noOfRowsUpdated = preparedStatement.executeUpdate();
   		    System.out.println(tscConnectionDetails.getDisplayName() + " No of Records get Updated : " + noOfRowsUpdated);

   		    updatedCount = updatedCount + noOfRowsUpdated;

   		} catch (SQLException e) {
   		    isTransactionSuccessfull = false;
   		    e.printStackTrace();
   		}
   	    }

   	} finally {
   	    oneControlConnection.cleanResources(null, connection, preparedStatement, null);
   	}

   	System.out.println("####### Total No of UI_Connections Updated at back up Server  " + updatedCount);

   	return isTransactionSuccessfull;
       }

       /**
        * Update the Connection State on back server based on the state at the
        * active server
        * 
        * @return
        */
       public boolean updateFreDataBasedOnConnectionNameOnBakup(List<TscConnectionDetails> connectionNameList) {

   	boolean isTransactionSuccessfull = false;
   	int updatedCount = 0;

   	OneControlConnection oneControlConnection = getTargetDb();
   	Connection connection = oneControlConnection.getConnection();

   	System.out.println("####### Total No of FRE_ENTITY retrieved from Active for update : " + connectionNameList.size());
   	PreparedStatement preparedStatement = null;

   	try {
   	    for (TscConnectionDetails tscConnectionDetails : connectionNameList) {

   		try {
   		    preparedStatement = connection.prepareStatement(OneControlSql.UPDATE_FRE_KEY_NAME_BACKUP);

   		    preparedStatement.setString(1, tscConnectionDetails.getDisplayNetworkName());
   		    preparedStatement.setString(2, tscConnectionDetails.getDisplayName());

   		    preparedStatement.setString(3, String.valueOf(tscConnectionDetails.getFreIsManaged()));

   		    preparedStatement.setString(4, String.valueOf(tscConnectionDetails.getLabel()));

   		    preparedStatement.setString(5, String.valueOf(tscConnectionDetails.getCustomer()));

   		    preparedStatement.setString(6, tscConnectionDetails.getFreKey());

   		    int noOfRowsUpdated = preparedStatement.executeUpdate();

   		    System.out.println(tscConnectionDetails.getDisplayName() + " No of Records get Updated : " + noOfRowsUpdated);
   		    updatedCount = updatedCount + noOfRowsUpdated;

   		} catch (SQLException e) {
   		    isTransactionSuccessfull = false;
   		    e.printStackTrace();
   		}
   	    }

   	} finally {
   	    oneControlConnection.cleanResources(null, connection, preparedStatement, null);
   	}

   	System.out.println("####### Total No of FRE_ENTITY Updated at back up Server  " + updatedCount);

   	return isTransactionSuccessfull;
       }

       public void matchUIConnections() {

   	List<TscConnectionDetails> tscConnectionDetails = new ArrayList();

   	OneControlConnection oneControlConnection = getActiveDb();

   	Connection connection = oneControlConnection.getConnection();

   	PreparedStatement preparedStatement = null;
   	ResultSet rs = null;
   	TscConnectionDetails connectionDetails;
   	try {
   	    preparedStatement = connection.prepareStatement(OneControlSql.RETRIEVE_UI_CONNECTIONS);
   	    rs = preparedStatement.executeQuery();

   	    while (rs.next()) {
   		connectionDetails = new TscConnectionDetails();

   		connectionDetails.setFreKey(rs.getString(1));
   		connectionDetails.setDisplayName(rs.getString(2));
   		connectionDetails.setDisplayNetworkName(rs.getString(3));
   		connectionDetails.setConnectionKey(rs.getString(4));
   		connectionDetails.setUiInProvisioning(rs.getString(5).charAt(0));

   		tscConnectionDetails.add(connectionDetails);
   	    }

   	    rs.close();
   	    preparedStatement.close();
   	    connection.close();

   	    System.out.println("Count of UI_Connection Retreieved data : " + tscConnectionDetails.size());
   	    System.out.println(" Retrieve UI COnnection Data from Acitve Server" + tscConnectionDetails);

   	} catch (SQLException e) {
   	    e.printStackTrace();
   	} finally {
   	    oneControlConnection.cleanResources(rs, connection, null, preparedStatement);

   	}

   	if (!tscConnectionDetails.isEmpty()) {

   	    oneControlConnection = getTargetDb();
   	    connection = oneControlConnection.getConnection();
   		
   	    PreparedStatement preparedStatementBkp = null;
   	    try {
   		for (TscConnectionDetails tscConnectionDetail : tscConnectionDetails) {

   		    preparedStatementBkp = connection.prepareStatement(OneControlSql.RETRIEVE_FRE_ID);

   		    preparedStatementBkp.setString(1, tscConnectionDetail.getFreKey());
   		    rs = preparedStatementBkp.executeQuery();

   		    while (rs.next()) {
   			tscConnectionDetail.setFreId(rs.getLong(1));
   		    }
   		    rs.close();
   		    preparedStatementBkp.close();

   		}

   		System.out.println("Updated FREID from back up server : " + tscConnectionDetails);
   		connection.close();
   	    } catch (SQLException e) {

   		e.printStackTrace();
   	    } finally {
   		oneControlConnection.cleanResources(rs, connection, null, preparedStatement);

   	    }

   	}
   	updateUIConnectionsDataonBackUp(tscConnectionDetails);
       }
   

}
