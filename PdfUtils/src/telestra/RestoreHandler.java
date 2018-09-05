package telestra;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;

public class RestoreHandler {

	public final String UPDATE_FRE_GENERIC_MYSQL = "update oneodb.FRE D\r\n" + "inner JOIN \r\n" + "(\r\n"
			+ "        select FRE_ID,UMS_NAME_ALIAS,NATIVE_NAME_ALIAS from oneodb.UI_CONNECTION a,oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.DISPLAYNAME=b.UMS_NAME_ALIAS)\r\n" + "                 C ON C.FRE_ID =D.ID\r\n"
			+ "     set D.UMS_NAME_ALIAS=C.UMS_NAME_ALIAS,\r\n" + "     D.NATIVE_NAME_ALIAS=C.NATIVE_NAME_ALIAS,\r\n"
			+ "     D.IS_MANAGED=1";

	public final String UPDATE_FRE_LABEL_MYSQL = "update oneodb.FRE D\r\n" + "inner JOIN \r\n" + "(\r\n"
			+ "        select FRE_ID,UMS_NAME_ALIAS,NATIVE_NAME_ALIAS,LABEL from oneodb.UI_CONNECTION a,oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.DISPLAYNAME=b.UMS_NAME_ALIAS and  LABEL >' ' )\r\n"
			+ "                 C ON C.FRE_ID =D.ID\r\n" + "     set D.LABEL=C.LABEL";

	public final String UPDATE_FRE_CUSTOMER_MYSQL = "update oneodb.FRE D\r\n" + "inner JOIN \r\n" + "(\r\n"
			+ "        select FRE_ID,UMS_NAME_ALIAS,NATIVE_NAME_ALIAS,CUSTOMER from oneodb.UI_CONNECTION a,oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.DISPLAYNAME=b.UMS_NAME_ALIAS  and  CUSTOMER >' ')\r\n"
			+ "                 C ON C.FRE_ID =D.ID\r\n" + "     set D.CUSTOMER=C.CUSTOMER";

	public final String UPDATE_FRE_CIRCUITID_MYSQL = "     update oneodb.FRE_ATTRIBUTE D \r\n" + "inner JOIN\r\n"
			+ "(\r\n" + "	  select ID,a.UMS_NAME_ALIAS from oneodb.FRE a, oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.NATIVE_NAME_ALIAS = b.NATIVE_NAME_ALIAS and a.UMS_NAME_ALIAS=b.UMS_NAME_ALIAS)\r\n"
			+ "                 C ON C.ID=D.FRE_ID and D.IDENTIFIER = 'VS_CIRCUITID_OR_PATHTRACE'\r\n"
			+ "				 set D.VALUE=C.UMS_NAME_ALIAS";

	public final String UPDATE_UI_GENERIC_MYSQL = "update oneodb.UI_CONNECTION D \r\n" + "inner JOIN \r\n" + "(\r\n"
			+ "        select ID,a.UMS_NAME_ALIAS,a.NATIVE_NAME_ALIAS from oneodb.FRE a,oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.NATIVE_NAME_ALIAS =b.NATIVE_NAME_ALIAS and a.UMS_NAME_ALIAS=b.UMS_NAME_ALIAS)\r\n"
			+ "                 C ON C.ID=D.FRE_ID\r\n" + "     set D.DISPLAYNETWORKNAME=C.UMS_NAME_ALIAS,\r\n"
			+ "     D.IN_PROVISIONING='0'";

	public final String UPDATE_UI_ANNOTATION_MYSQL = "update oneodb.UI_CONNECTION D\r\n" + "inner JOIN \r\n" + "(\r\n"
			+ "        select ID,ANNOTATION_ID from oneodb.FRE a,oneodb.TEMP_1C_ONLY_DATA b \r\n"
			+ "                where a.NATIVE_NAME_ALIAS =b.NATIVE_NAME_ALIAS and a.UMS_NAME_ALIAS=b.UMS_NAME_ALIAS and b.ANNOTATION_ID > ' ')\r\n"
			+ "                 C ON C.ID=D.FRE_ID\r\n" + "     set D.ANNOTATION_ID=C.ANNOTATION_ID,\r\n"
			+ "		D.ANNOTATION_FLAG=1";

	public final String INSERT_ANNOTATION_MYSQL = "INSERT INTO oneodb.ANNOTATION select * FROM oneodb.TEMP_ANNOTATION";
	public final String INSERT_ANNOTATION_ENTRY_MYSQL = "INSERT INTO oneodb.ANNOTATION_ENTRY select * FROM oneodb.TEMP_ANNOTATION_ENTRY";
	
	
	
	

	private OneControlConnection oneControlConnection;
	private Boolean isMySqlDb;

	public RestoreHandler(OneControlConnection oneControlConnection) {
		this.oneControlConnection = oneControlConnection;
		if (oneControlConnection instanceof OneControlSqlConnection) {
			isMySqlDb = true;
		} else if (oneControlConnection instanceof OneControlOracleConnection) {
			isMySqlDb = false;
		}
	}

	public boolean restoreData(String configFileName) {
		boolean isTransactionSuccessfull = true;

		if (isMySqlDb) {
			/*
			 * My SQL Block
			 */
			Connection connection = ((OneControlSqlConnection) oneControlConnection).getConnection(configFileName);
			Statement stmt = null;

			try {
				stmt = connection.createStatement();

				stmt.executeUpdate(INSERT_ANNOTATION_MYSQL);

				stmt.executeUpdate(INSERT_ANNOTATION_ENTRY_MYSQL);

				stmt.executeUpdate(UPDATE_FRE_GENERIC_MYSQL);

				stmt.executeUpdate(UPDATE_FRE_LABEL_MYSQL);

				stmt.executeUpdate(UPDATE_FRE_CUSTOMER_MYSQL);

				stmt.executeUpdate(UPDATE_FRE_CIRCUITID_MYSQL);

				stmt.executeUpdate(UPDATE_UI_GENERIC_MYSQL);

				stmt.executeUpdate(UPDATE_UI_ANNOTATION_MYSQL);
				
				//Delete Temp tables
				stmt.executeUpdate(BackUpDataCreator.ANNOTATION_BACKUP_TABLE_DROP_MYSQL);
				stmt.executeUpdate(BackUpDataCreator.ANNOTATION_ENTRY_BACKUP_TABLE_DROP_MYSQL);
				stmt.executeUpdate(BackUpDataCreator.MASTER_BACKUP_TABLE_DROP_MYSQL);

			} catch (SQLException e) {
				isTransactionSuccessfull = false;
				e.printStackTrace();
			} finally {
				oneControlConnection.cleanResources(null, connection, stmt, null);
			}

		} else {
			/*
			 * Oracle block
			 */
		}

		return isTransactionSuccessfull;
	}

}
