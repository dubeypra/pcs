/**
 * 
 */
package verizon;

import java.util.Arrays;

import context.BaseContextSetUp;
import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;
import verizon.handlers.ContainerHandler;
import verizon.handlers.FreDataHandler;
import verizon.handlers.NodeHandler;

/**
 * This class will orchestrate the process of matching of data base level entries from active server 
 * to back up server
 * Entities to be Matched
 * 1)UI_LINK
 * 2)CORE_LINK
 * 3)UI_NODE
 * 4)UI_CONTAINER
 * 
 * @author prdubey
 *
 */
public class VerizonOrchestrator {
    
    public static final String SOURCE_DB = "resources/utils/dataBase1ConnectionProperties";
    public static final String TARGET_DB = "resources/utils/dataBase2ConnectionProperties";
    
    private OneControlConnection sourceDb;
    private OneControlConnection targetDb;

    public static void main (String args[]) {
	if (args.length != 5) {
	    System.err.println("Invalid arguments" + Arrays.toString(args));
	    System.exit(1);
	}

	String sourceDbConfigFile = args[0];
	boolean isSourceOracleDbType = args[1].equals("ORACLE");

	String targetDbConfigFile = args[2];
	boolean istargetOracleDbType = args[3].equals("ORACLE");

	BaseContextSetUp.ONE_CONTROL_PROVIDER_URL = args[4];
	
	VerizonOrchestrator verizonOrchestrator = new VerizonOrchestrator(sourceDbConfigFile, isSourceOracleDbType, targetDbConfigFile, istargetOracleDbType);
	verizonOrchestrator.orchestrateEntitySync();
    }
    
    public VerizonOrchestrator(String sourceDbFile, boolean isSourceOracleDbType, String targetDbFile, boolean istargetOracleDbType) {

	if (isSourceOracleDbType) {
	    sourceDb = new OneControlOracleConnection(sourceDbFile);
	} else {
	    sourceDb = new OneControlSqlConnection(sourceDbFile);
	}

	if (istargetOracleDbType) {
	    targetDb = new OneControlOracleConnection(targetDbFile);
	} else {
	    targetDb = new OneControlSqlConnection(targetDbFile);
	}

    }
    
    public void orchestrateEntitySync() {
	ContainerHandler containerHandler = new ContainerHandler(sourceDb, targetDb);
	containerHandler.handleManualContainerEntity();
	
	NodeHandler nodeHandler = new NodeHandler(sourceDb, targetDb);
	nodeHandler.handleManualNodeEntity();
	
	FreDataHandler dataHandler = new FreDataHandler(sourceDb, targetDb);
	dataHandler.SychAllConnectionParemeters();
    }
    
}
