package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;

/**
 * This class used to compare the schema of the two databases
 * 
 * @author prdubey
 * 
 */
public class DatabaseComparator {
    
    public static final String SCHEMA_1C="oneodb";
    public static final String SCHEMA_OC="oc";

    public static final String dataBase1 = "resources/utils/dataBase1ConnectionProperties";
    public static final String dataBase2 = "resources/utils/dataBase2ConnectionProperties";

    public static final String MYSQL_1C_SCHEMA = "SELECT TABLE_NAME,TABLE_ROWS FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'oneodb' order by TABLE_NAME;";
    public static final String MYSQL_OC_SCHEMA = "SELECT TABLE_NAME,TABLE_ROWS FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'oc' order by TABLE_NAME;";

    public static final String ORACLE_1C_SCHEMA = "select table_name, num_rows from SYS.user_tables ORDER BY TABLE_NAME"; //WHERE OWNER='ONEODBADMIN' ORDER BY TABLE_NAME";
    public static final String ORACLE_OC_SCHEMA = "select table_name, num_rows from SYS.user_tables ORDER BY TABLE_NAME";// WHERE OWNER='OCUSER' ORDER BY TABLE_NAME";

    public static void main(String args[]) {
	System.out.println("Welcome in database schema Comparison !!!");
	
	/*
	 * DataBase Details must be defined in the Property file. While executing this script
	 * user must know database is oracle or My sql
	 * This information can be fetched in command line too.
	 */

	//1c Comparison
	List<TableDetails> srcTables = fetchDatabaseDetails(dataBase1,SCHEMA_1C,false);
	System.out.println("Data Collected  from DB1 :"+srcTables);
	
	List<TableDetails> dstTables = fetchDatabaseDetails(dataBase2,SCHEMA_1C,true);
	System.out.println("Data Collected  from DB1 :"+dstTables);
	
	System.out.println("Compared Data is \n");
	List<CompareResult> result = compareTables("onxl4367",srcTables,"onxl4366",dstTables);
	
	for (CompareResult compareResult : result) {
	    System.out.println(compareResult);
        }
	
	//OC Comparison
	
	
	
    }

    private static List<CompareResult> compareTables(String srcDb, List<TableDetails> srcTables, String dstDb, List<TableDetails> dstTables) {
	List<CompareResult> nonMatchingTable=new ArrayList<CompareResult>();
	
	boolean isTableFound = false;
	for (TableDetails srcTable : srcTables) {
	    isTableFound = false;
	    
	    for (TableDetails dstTable : dstTables) {
		
		if(srcTable.getTableName().equalsIgnoreCase(dstTable.getTableName())){
		    
		    if(srcTable.getRowCount()!=dstTable.getRowCount()){	
			nonMatchingTable.add(createNonMatchingRow(srcDb,srcTable,dstDb,dstTable));
		    }
		    isTableFound = true;
		    break;
		    
		}
            }
	    if(!isTableFound){
		nonMatchingTable.add(createNonMatchingRow(srcDb,srcTable,dstDb,null));
	    }
	    
        }
	return nonMatchingTable;
    }

    private static CompareResult createNonMatchingRow(String srcDb, TableDetails srcTable, String dstDb, TableDetails dstTable) {
	CompareResult compareResult = new CompareResult();

	compareResult.setSrcdbName(srcDb);
	
	if (srcTable != null) {
	    compareResult.setSrcTableName(srcTable.getTableName());
	    compareResult.setSrcRowCount(srcTable.getRowCount());
	}

	compareResult.setTgtdbName(dstDb);
	
	if(dstTable !=null ){
	    compareResult.setTgtTableName(dstTable.getTableName());
	    compareResult.setTgtRowCount(dstTable.getRowCount());
	}
	return compareResult;
    }

    private static List<TableDetails> fetchDatabaseDetails(String configFile, String schema, boolean isOracle) {
	List<TableDetails> tableDetails = new ArrayList<TableDetails>();

	if (isOracle) {

	    OneControlOracleConnection controlOracleConnection = new OneControlOracleConnection();
	    Connection connection = controlOracleConnection.getConnection(configFile);

	    if (schema.equalsIgnoreCase(SCHEMA_1C)) {
		tableDetails = executeQuery(connection, ORACLE_1C_SCHEMA, controlOracleConnection);
	    } else if (schema.equalsIgnoreCase(SCHEMA_OC)) {
		tableDetails = executeQuery(connection, ORACLE_OC_SCHEMA, controlOracleConnection);
	    }

	} else {

	    OneControlSqlConnection controlSqlConnection = new OneControlSqlConnection();
	    Connection connection = controlSqlConnection.getConnection(configFile);

	    if (schema.equalsIgnoreCase(SCHEMA_1C)) {
		tableDetails = executeQuery(connection, MYSQL_1C_SCHEMA, controlSqlConnection);
	    } else if (schema.equalsIgnoreCase(SCHEMA_OC)) {
		tableDetails = executeQuery(connection, MYSQL_OC_SCHEMA, controlSqlConnection);
	    }

	}
	return tableDetails;
    }

    private static List<TableDetails> executeQuery(Connection connection, String query, OneControlConnection oneControlConnection) {
	List<TableDetails> tableDetailsList = new ArrayList<TableDetails>();
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;
	TableDetails tableDetails;
	try {
	    preparedStatement = connection.prepareStatement(query);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		tableDetails = new TableDetails();
		tableDetails.setTableName(rs.getString(1));
		tableDetails.setRowCount(rs.getInt(2));
		tableDetailsList.add(tableDetails);
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

	return tableDetailsList;
    }

}
