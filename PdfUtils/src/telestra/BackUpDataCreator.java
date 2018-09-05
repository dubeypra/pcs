/**
 * 
 */
package telestra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;

/**
 * @author prdubey
 *
 */
public class BackUpDataCreator {

	/**
	 * Master Back Up Table
	 */
	public static String MASTER_BACKUP_TABLE_CREATE_MYSQL = "CREATE TABLE oneodb.TEMP_1C_ONLY_DATA (\r\n"
			+ "  NATIVE_NAME_ALIAS VARCHAR(255) ,\r\n" + "  UMS_NAME_ALIAS VARCHAR(255),\r\n"
			+ "  CUSTOMER VARCHAR(255),\r\n" + "  LABEL VARCHAR(255),\r\n" + "  ANNOTATION_ID bigint\r\n" + ")";

	public static String MASTER_BACKUP_TABLE_CREATE_ORACLE = "CREATE TABLE\r\n"
			+ "    oneODBADMIN.TEMP_1C_ONLY_DATA\r\n" + "    (\r\n" + "        NATIVE_NAME_ALIAS VARCHAR(255) ,\r\n"
			+ "        UMS_NAME_ALIAS VARCHAR(255),\r\n" + "        CUSTOMER VARCHAR(255),\r\n"
			+ "        LABEL VARCHAR(255) ,\r\n" + "        ANNOTATION_ID NUMBER\r\n" + "    ) ";

	public static String MASTER_BACKUP_TABLE_DROP_MYSQL = "DROP TABLE IF EXISTS oneodb.TEMP_1C_ONLY_DATA";
	public static String MASTER_BACKUP_TABLE_DROP_ORACLE = "DROP TABLE oneODBADMIN.TEMP_1C_ONLY_DATA";

	public static String INSERT_RECORDS_IN_MASTER_MYSQL = "INSERT\r\n" + "INTO\r\n" + "    oneodb.TEMP_1C_ONLY_DATA\r\n"
			+ "    (\r\n" + "        ANNOTATION_ID,\r\n" + "        NATIVE_NAME_ALIAS,\r\n"
			+ "        UMS_NAME_ALIAS,\r\n" + "        CUSTOMER,\r\n" + "        LABEL\r\n" + "    )\r\n" + "SELECT\r\n"
			+ "	DISTINCT\r\n" + "   a.ANNOTATION_ID,\r\n" + "   b.NATIVE_NAME_ALIAS ,\r\n" + "    b.UMS_NAME_ALIAS,\r\n"
			+ "    b.CUSTOMER,\r\n" + "    b.LABEL\r\n" + "FROM\r\n" + "    oneodb.UI_CONNECTION a,\r\n"
			+ "    oneodb.FRE b\r\n" + "WHERE\r\n" + "	a.FRE_ID = b.ID \r\n" + "    AND b.NATIVE_NAME_ALIAS IN (";

	public static String INSERT_RECORDS_IN_MASTER_ORACLE = "INSERT\r\n" + "INTO\r\n"
			+ "    oneODBADMIN.TEMP_1C_ONLY_DATA\r\n" + "    (\r\n" + "        ANNOTATION_ID,\r\n"
			+ "        NATIVE_NAME_ALIAS,\r\n" + "        UMS_NAME_ALIAS,\r\n" + "        CUSTOMER,\r\n"
			+ "        LABEL\r\n" + "    )\r\n" + "SELECT DISTINCT\r\n" + "    a.ANNOTATION_ID,\r\n"
			+ "    b.NATIVE_NAME_ALIAS ,\r\n" + "    b.UMS_NAME_ALIAS,\r\n" + "    b.CUSTOMER,\r\n" + "    b.LABEL\r\n"
			+ "FROM\r\n" + "    oneODBADMIN.UI_CONNECTION a,\r\n" + "    oneODBADMIN.FRE b\r\n" + "WHERE\r\n"
			+ "a.FRE_ID = b.ID AND \r\n" + "    b.NATIVE_NAME_ALIAS IN (";

	/**
	 * Annotation Back Up
	 */

	public static String ANNOTATION_BACKUP_TABLE_CREATE_MYSQL = "CREATE TABLE oneodb.TEMP_ANNOTATION SELECT * FROM  oneodb.ANNOTATION";
	public static String ANNOTATION_BACKUP_TABLE_CREATE_ORACLE = "CREATE TABLE\r\n"
			+ "    oneODBADMIN.TEMP_ANNOTATION AS\r\n" + "SELECT\r\n" + "    *\r\n" + "FROM\r\n"
			+ "    oneODBADMIN.ANNOTATION";

	public static String ANNOTATION_BACKUP_TABLE_DROP_MYSQL = "DROP TABLE IF EXISTS oneodb.TEMP_ANNOTATION";
	public static String ANNOTATION_BACKUP_TABLE_DROP_ORACLE = "DROP TABLE oneODBADMIN.TEMP_ANNOTATION";

	/**
	 * Annotation Entry Back Up
	 */

	public static String ANNOTATION_ENTRY_BACKUP_TABLE_CREATE_MYSQL = "CREATE TABLE oneodb.TEMP_ANNOTATION_ENTRY SELECT * FROM  oneodb.ANNOTATION_ENTRY";
	public static String ANNOTATION_ENTRY_BACKUP_TABLE_CREATE_ORACLE = "CREATE TABLE\r\n"
			+ "    oneODBADMIN.TEMP_ANNOTATION_ENTRY AS\r\n" + "SELECT\r\n" + "    *\r\n" + "FROM\r\n"
			+ "    oneODBADMIN.ANNOTATION_ENTRY";
	
	public static String ANNOTATION_ENTRY_BACKUP_TABLE_DROP_MYSQL = "DROP TABLE IF EXISTS oneodb.TEMP_ANNOTATION_ENTRY";
	public static String ANNOTATION_ENTRY_BACKUP_TABLE_DROP_ORACLE = "DROP TABLE oneODBADMIN.TEMP_ANNOTATION_ENTRY";

	// public static String BACK_UP_CONNECTION_NAME_PATH =
	// "resources/data/conn_names.txt"; // TODO : Changes based on location
	// shared by OC

	private OneControlConnection oneControlConnection;
	private Boolean isMySqlDb;
	private String affectedConnectionFileLoc;

	public BackUpDataCreator(OneControlConnection oneControlConnection, String affectedConnectionFileLoc) {
		this.oneControlConnection = oneControlConnection;
		this.affectedConnectionFileLoc = affectedConnectionFileLoc;
		if (oneControlConnection instanceof OneControlSqlConnection) {
			isMySqlDb = true;
		} else if (oneControlConnection instanceof OneControlOracleConnection) {
			isMySqlDb = false;
		}
	}

	public boolean createBackUpTable(String configFileName) {
		boolean isTransactionSuccessfull = true;
		ArrayList<String> connectionNameList = fetchBackUpConnectionNames();
		if (!connectionNameList.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (String string : connectionNameList) {
				builder.append("?,");
			}

			if (isMySqlDb) {
				/*
				 * My SQL Block
				 */

				Connection connection = ((OneControlSqlConnection) oneControlConnection).getConnection(configFileName);
				Statement stmt = null;
				PreparedStatement preparedStatement = null;
				try {
					stmt = connection.createStatement();
					stmt.executeUpdate(MASTER_BACKUP_TABLE_DROP_MYSQL); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(MASTER_BACKUP_TABLE_CREATE_MYSQL); // Create table
					System.out.println("Created table in given database...");

					String insertStmt = INSERT_RECORDS_IN_MASTER_MYSQL
							+ builder.deleteCharAt(builder.length() - 1).toString() + ")";
					System.out.println("Insert Statement is : " + insertStmt);

					preparedStatement = connection.prepareStatement(insertStmt);

					for (int i = 0, j = 1; i < connectionNameList.size(); i++, j++) {
						preparedStatement.setString(j, connectionNameList.get(i));
					}

					preparedStatement.execute();

					System.out.println("Annotation Back Up");

					stmt.executeUpdate(ANNOTATION_BACKUP_TABLE_DROP_MYSQL); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(ANNOTATION_BACKUP_TABLE_CREATE_MYSQL); // Create table

					stmt.executeUpdate(ANNOTATION_ENTRY_BACKUP_TABLE_DROP_MYSQL); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(ANNOTATION_ENTRY_BACKUP_TABLE_CREATE_MYSQL); // Create table

					System.out.println("Back Up Created Successfully !!");
					connection.close();

					System.out.println("Commit Done !!");

				} catch (SQLException e) {
					isTransactionSuccessfull = false;

					e.printStackTrace();
				} finally {
					oneControlConnection.cleanResources(null, connection, stmt, preparedStatement);

				}

			} else {
				/*
				 * Oracle Block
				 */
				Connection connection = ((OneControlOracleConnection) oneControlConnection).getConnection(configFileName);
				Statement stmt = null;
				PreparedStatement preparedStatement = null;

				try {
					stmt = connection.createStatement();

					dropOracleTable(stmt,MASTER_BACKUP_TABLE_DROP_ORACLE); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(MASTER_BACKUP_TABLE_CREATE_ORACLE); // Create table
					System.out.println("Created table in given database...");

					String insertStmt = INSERT_RECORDS_IN_MASTER_ORACLE
							+ builder.deleteCharAt(builder.length() - 1).toString() + ")";
					System.out.println("Insert Statement is : " + insertStmt);

					preparedStatement = connection.prepareStatement(insertStmt);

					for (int i = 0, j = 1; i < connectionNameList.size(); i++, j++) {
						preparedStatement.setString(j, connectionNameList.get(i));
					}

					preparedStatement.execute();
					
					System.out.println("Annotation Back Up");

					dropOracleTable(stmt,ANNOTATION_BACKUP_TABLE_DROP_ORACLE); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(ANNOTATION_BACKUP_TABLE_CREATE_ORACLE); // Create table

					dropOracleTable(stmt,ANNOTATION_ENTRY_BACKUP_TABLE_DROP_ORACLE); // Drop table
					System.out.println("Back up table dropped successfully given database...");
					stmt.executeUpdate(ANNOTATION_ENTRY_BACKUP_TABLE_CREATE_ORACLE); // Create table

					System.out.println("Back Up Created Successfully !!");
					connection.close();

					System.out.println("Commit Done !!");

				} catch (SQLException e) {
					isTransactionSuccessfull = false;
					e.printStackTrace();
				} finally {
					oneControlConnection.cleanResources(null, connection, stmt, preparedStatement);

				}

			}
		}
		return isTransactionSuccessfull;
	}
	
	/**
	 * Required to handle in case table does not exist for oracle only
	 * @param stmt
	 * @param query
	 */
	private void dropOracleTable(Statement stmt,String query) {
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("In Oracle Drop Table : "+e.getMessage());
		}
	}

	/**
	 * Get all the connection Names for which back up has to be triggered
	 * 
	 * @return Connection Name persisted by OC
	 */
	private ArrayList<String> fetchBackUpConnectionNames() {
		ArrayList<String> connectionNameList = new ArrayList();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(new File(affectedConnectionFileLoc));
			bufferedReader = new BufferedReader(fileReader);

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				connectionNameList.add(line);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return connectionNameList;
	}

}
