/**
 * 
 */
package telestra;

import java.util.Arrays;

import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;

/**
 * @author prdubey
 *
 */
public class Orchestrator {

	public static final String BACKUP_TRIGGER="backup";
	public static final String RESTORE_TRIGGER="restore";
	
	public static final String MYSQLDB="mysql";
	public static final String ORACLEDB="oracle";
	
	public  static final String configFile="resources/utils/dataBase1ConnectionProperties";
	
	public static void main(String args[]) {
		Orchestrator orchestrator = new Orchestrator();
		
		if(args.length<1) {
			System.err.println("Invalid arguments" + Arrays.toString(args));
			System.err.println("Usage "
                    + "Trigger Type (backup or restore) ,DB Type (mysql or oracle), Connection Name file Path");
            System.exit(1);
		}else {
			String triggerType = args[0];
			
			if(triggerType.equals(BACKUP_TRIGGER)) {
				if(args.length != 3) {
					System.err.println("Invalid arguments" + Arrays.toString(args));
					System.err.println("Usage "
		                    + "Trigger Type (backup or restore) ,DB Type (mysql or oracle), Connection Name file Path");
		            System.exit(1);
				}
				
				if(args[1].equals(ORACLEDB)) {
					orchestrator.createBackUpData(true, args[2],configFile);
				} else {
					orchestrator.createBackUpData(false, args[2],configFile);
				}
			}else if(triggerType.equals(RESTORE_TRIGGER)) {
				if(args.length != 2) {
					System.err.println("Invalid arguments" + Arrays.toString(args));
					System.err.println("Usage "
		                    + "Trigger Type (backup or restore) ,DB Type (mysql or oracle)");
		            System.exit(1);
				}else {
					if(args[1].equals(ORACLEDB)) {
						orchestrator.restoreBackUpData(true,configFile);
					} else {
						orchestrator.restoreBackUpData(false,configFile);
					}
				}
			}
		}
	}

	/**
	 * @param isOracleDb
	 * @param connectionNameFilePath
	 */
	public void createBackUpData(Boolean isOracleDb, String connectionNameFilePath,String configFileName) {
		OneControlConnection oneControlConnection = null;

		if (isOracleDb) {
			oneControlConnection = new OneControlOracleConnection();
		} else {
			oneControlConnection = new OneControlSqlConnection();
		}

		if (oneControlConnection != null) {
			BackUpDataCreator backUpDataCreator = new BackUpDataCreator(oneControlConnection,connectionNameFilePath);
			backUpDataCreator.createBackUpTable(configFileName);
		}
	}

	/**
	 * @param isOracleDb
	 */
	public void restoreBackUpData(Boolean isOracleDb,String configFileName) {
		OneControlConnection oneControlConnection = null;

		if (isOracleDb) {
			oneControlConnection = new OneControlOracleConnection();
		} else {
			oneControlConnection = new OneControlSqlConnection();
		}

		if (oneControlConnection != null) {
			RestoreHandler restoreHandler = new RestoreHandler(oneControlConnection);
			restoreHandler.restoreData(configFileName);
		}

	}

}
