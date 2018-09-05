package telestra;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.OneControlSqlConnection;

public class GrTscConnectionManager {

	public static final String configFileActive = "resources/utils/mySqlActiveConnectionProperties";
	public static final String configFileBackUp = "resources/utils/mySqlBackUpConnectionProperties";

	/**
	 * Algo FRE table IS_MANAGED = 1 then Managed , Connection Can be reside in
	 * PTSC or MTSC UI Connection Table IN_PROVISIONING = 0 , Connection in the
	 * MTSC IN_PROVISIONING = 1 , Connection in the PTSC
	 * 
	 */
	/*private static final String GET_UI_CONNECTIONS_ACTIVE = "Select A.DISPLAYNAME,"
			+ "A.DISPLAYNETWORKNAME,"
			+ "A.IN_PROVISIONING "
			+ "from oneodb.UI_CONNECTION A ";*/

	private static final String UPDATE_UI_CONNECTIONS_BACKUP = "update oneodb.UI_CONNECTION A  "
			+ "SET A.IN_PROVISIONING =? ,A.DISPLAYNAME=?,A.DISPLAYNETWORKNAME=?,A.CONNECTION_KEY=?"
			+ " where A.FRE_ID =?";

	/*private static final String GET_FRE_ACTIVE = "select B.UMS_NAME_ALIAS,B.LABEL,B.CUSTOMER,B.IS_MANAGED  from oneodb.FRE B";

	private static final String UPDATE_FRE_BACKUP = "UPDATE oneodb.FRE B SET B.IS_MANAGED=?,B.LABEL=?,B.CUSTOMER=? "
			+ "where B.UMS_NAME_ALIAS=?";*/

	private static final String GET_FRE_KEY_NAME_ACTIVE = "select B.FRE_KEY, B.NATIVE_NAME_ALIAS,B.UMS_NAME_ALIAS,B.LABEL,B.CUSTOMER,B.IS_MANAGED from oneodb.FRE B";

	private static final String UPDATE_FRE_KEY_NAME_BACKUP = "UPDATE oneodb.FRE B SET B.NATIVE_NAME_ALIAS=?,B.UMS_NAME_ALIAS=?,B.IS_MANAGED=?,B.LABEL=?,B.CUSTOMER=? "
			+ "where B.FRE_KEY=?";

	private static final String RETRIEVE_UI_CONNECTIONS = "Select B.FRE_KEY,A.DISPLAYNAME,DISPLAYNETWORKNAME,A.CONNECTION_KEY,A.IN_PROVISIONING from oneodb.UI_CONNECTION A,oneodb.FRE B Where A.FRE_ID=B.ID;";
	private static final String RETRIEVE_FRE_ID = "Select A.ID from oneodb.FRE A where A.FRE_KEY=?";

	public static void main(String args[]) {

		GrTscConnectionManager grTscConnectionManager = new GrTscConnectionManager();
		List<TscConnectionDetails> connectionDetails;

		/**
		 * UI_Connections Table Update Fetch data from active Server and update
		 * the data on back up server Unique connections identified based on the
		 * Connection Name ( Display Name)
		 */
		/*
		 * connectionDetails = grTscConnectionManager
		 * .retrieveUIConnectionsDataFromActive(); grTscConnectionManager
		 * .updateUIConnectionsDataonBackUp(connectionDetails);
		 */

		// FRE
		/*
		 * connectionDetails = grTscConnectionManager
		 * .retrieveFreDataBasedOnConnectionNameFromActive();
		 * grTscConnectionManager
		 * .updateFreDataBasedOnConnectionNameOnActive(connectionDetails);
		 */

		/**
		 * FRE Update based on Connection Key. This will update both the
		 * UMS_ALIAS_NAME and NATIVE_NAME
		 */
		
		  connectionDetails = grTscConnectionManager
		  .retrieveFreDataBasedOnFreKeyFromActive(); 
		  
		  grTscConnectionManager
		  .updateFreDataBasedOnConnectionNameOnBakup(connectionDetails);
		 
		  /**
		   * UI_Connections Updated
		   * data fetched from Active Server and forcefully updated in the back up server
		   */
		 grTscConnectionManager.matchUIConnections();
	}

	/**
	 * @return Get List of all the Connections which must be in MTSC at Back up
	 *         Server
	 */
	private List<TscConnectionDetails> retrieveFreDataBasedOnFreKeyFromActive() {
		List<TscConnectionDetails> tscConnectionDetails = new ArrayList();

		OneControlSqlConnection controlSqlConnectionActive = new OneControlSqlConnection();

		Connection connection = controlSqlConnectionActive
				.getConnection(configFileActive);

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TscConnectionDetails connectionDetails;
		try {
			preparedStatement = connection
					.prepareStatement(GET_FRE_KEY_NAME_ACTIVE);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				connectionDetails = new TscConnectionDetails();
				
				connectionDetails.setFreKey(rs.getString(1));
				connectionDetails.setDisplayNetworkName(rs.getString(2));
				connectionDetails.setDisplayName(rs.getString(3));
				connectionDetails.setLabel(rs.getString(4));
				connectionDetails.setCustomer(rs.getString(5));
				connectionDetails.setFreIsManaged(rs.getString(6).charAt(0));
				
				System.out
						.println(" FRE: Data Extracted from Active Server  "
								+ connectionDetails);

				tscConnectionDetails.add(connectionDetails);
			}

			rs.close();
			preparedStatement.close();
			connection.close();

			System.out.println("Data Retrieved !!");

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			controlSqlConnectionActive.cleanResources(rs, connection, null,
					preparedStatement);

		}

		return tscConnectionDetails;
	}

	/**
	 * Update the Connection State on back server based on the state at the
	 * active server
	 * 
	 * @return
	 */
	private boolean updateUIConnectionsDataonBackUp(
			List<TscConnectionDetails> connectionNameList) {
		
		boolean isTransactionSuccessfull = false;

		int updatedCount=0;
		
		OneControlSqlConnection controlSqlConnectionBackUp = new OneControlSqlConnection();
		Connection connection = controlSqlConnectionBackUp
				.getConnection(configFileBackUp);
		PreparedStatement preparedStatement = null;
		
		System.out.println("####### Total No of UI_Connections Details retrieved from Active for update : "+connectionNameList.size());
		
		try {
			for (TscConnectionDetails tscConnectionDetails : connectionNameList) {

				try {
					preparedStatement = connection
							.prepareStatement(UPDATE_UI_CONNECTIONS_BACKUP);
					
					preparedStatement.setString(1,
							String.valueOf(tscConnectionDetails
									.getUiInProvisioning()));
					
					preparedStatement.setString(2,
							tscConnectionDetails.getDisplayName());
					
					preparedStatement.setString(3,
							tscConnectionDetails.getDisplayNetworkName());
					
					preparedStatement.setString(4,
							tscConnectionDetails.getConnectionKey());
					
					preparedStatement.setLong(5, tscConnectionDetails.getFreId());

					int noOfRowsUpdated = preparedStatement.executeUpdate();
					System.out.println(tscConnectionDetails.getDisplayName()
							+ " No of Records get Updated : "+noOfRowsUpdated);
					
					updatedCount=updatedCount+noOfRowsUpdated;

				} catch (SQLException e) {
					isTransactionSuccessfull = false;
					e.printStackTrace();
				}
			}

		} finally {
			controlSqlConnectionBackUp.cleanResources(null, connection,
					preparedStatement, null);
		}
		
		System.out.println("####### Total No of UI_Connections Updated at back up Server  "+updatedCount);
		
		return isTransactionSuccessfull;
	}



	/**
	 * Update the Connection State on back server based on the state at the
	 * active server
	 * 
	 * @return
	 */
	private boolean updateFreDataBasedOnConnectionNameOnBakup(
			List<TscConnectionDetails> connectionNameList) {
		
		boolean isTransactionSuccessfull = false;
		int updatedCount=0;
		
		OneControlSqlConnection controlSqlConnectionBackUp = new OneControlSqlConnection();
		Connection connection = controlSqlConnectionBackUp
				.getConnection(configFileBackUp);
		
		System.out.println("####### Total No of FRE_ENTITY retrieved from Active for update : "+connectionNameList.size());
		PreparedStatement preparedStatement = null;
		
		try {
			for (TscConnectionDetails tscConnectionDetails : connectionNameList) {

				try {
					preparedStatement = connection
							.prepareStatement(UPDATE_FRE_KEY_NAME_BACKUP);

					preparedStatement.setString(1,tscConnectionDetails.getDisplayNetworkName());
					preparedStatement.setString(2,tscConnectionDetails.getDisplayName());
					
					preparedStatement.setString(3, String
							.valueOf(tscConnectionDetails.getFreIsManaged()));

					preparedStatement.setString(4,
							String.valueOf(tscConnectionDetails.getLabel()));
					
					preparedStatement.setString(5,
							String.valueOf(tscConnectionDetails.getCustomer()));
					
					preparedStatement.setString(6,tscConnectionDetails.getFreKey());

					int noOfRowsUpdated = preparedStatement.executeUpdate();
					
					System.out.println(tscConnectionDetails.getDisplayName()
							+ " No of Records get Updated : "
							+ noOfRowsUpdated);
					updatedCount = updatedCount+noOfRowsUpdated;

				} catch (SQLException e) {
					isTransactionSuccessfull = false;
					e.printStackTrace();
				}
			}

		} finally {
			controlSqlConnectionBackUp.cleanResources(null, connection,
					preparedStatement, null);
		}
		
		System.out.println("####### Total No of FRE_ENTITY Updated at back up Server  "+updatedCount);
		
		return isTransactionSuccessfull;
	}

	private void matchUIConnections() {

		List<TscConnectionDetails> tscConnectionDetails = new ArrayList();

		OneControlSqlConnection controlSqlConnectionActive = new OneControlSqlConnection();

		Connection connection = controlSqlConnectionActive
				.getConnection(configFileActive);

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		TscConnectionDetails connectionDetails;
		try {
			preparedStatement = connection
					.prepareStatement(RETRIEVE_UI_CONNECTIONS);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				connectionDetails = new TscConnectionDetails();

				connectionDetails.setFreKey(rs.getString(1));
				connectionDetails.setDisplayName(rs.getString(2));
				connectionDetails.setDisplayNetworkName(rs.getString(3));
				connectionDetails.setConnectionKey(rs.getString(4));
				connectionDetails
						.setUiInProvisioning(rs.getString(5).charAt(0));

				tscConnectionDetails.add(connectionDetails);
			}

			rs.close();
			preparedStatement.close();
			connection.close();

			System.out.println("Count of UI_Connection Retreieved data : "+tscConnectionDetails.size());
			System.out
			.println(" Retrieve UI COnnection Data from Acitve Server"
					+ tscConnectionDetails);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			controlSqlConnectionActive.cleanResources(rs, connection, null,
					preparedStatement);

		}

		if (!tscConnectionDetails.isEmpty()) {

			OneControlSqlConnection controlSqlConnectionBackUp = new OneControlSqlConnection();
			Connection backUpServerConnection = controlSqlConnectionBackUp
					.getConnection(configFileBackUp);
			PreparedStatement preparedStatementBkp = null;
			try {
				for (TscConnectionDetails tscConnectionDetail : tscConnectionDetails) {

					preparedStatementBkp = backUpServerConnection
							.prepareStatement(RETRIEVE_FRE_ID);
					
					preparedStatementBkp.setString(1,
							tscConnectionDetail.getFreKey());
					rs = preparedStatementBkp.executeQuery();

					while (rs.next()) {
						tscConnectionDetail.setFreId(rs.getLong(1));
					}
					rs.close();
					preparedStatementBkp.close();

				}
				
				System.out
				.println("Updated FREID from back up server : "
						+ tscConnectionDetails);
				connection.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				controlSqlConnectionActive.cleanResources(rs, connection, null,
						preparedStatement);

			}

		}
		updateUIConnectionsDataonBackUp(tscConnectionDetails);
	}
}
