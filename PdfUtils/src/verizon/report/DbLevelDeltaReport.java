/**
 * 
 */
package verizon.report;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ciena.rm.constraints.api.db.entity.Constraints;
import com.ciena.rm.uinavigation.model.UIContainer;
import com.ciena.rm.uinavigation.model.UINode;

import context.BaseContextSetUp;
import telestra.TscConnectionDetails;
import utils.OneControlConnection;
import utils.OneControlOracleConnection;
import utils.OneControlSqlConnection;
import verizon.handlers.ConstraintHandler;
import verizon.handlers.ContainerHandler;
import verizon.handlers.FreDataHandler;
import verizon.handlers.NodeHandler;

/**
 * This class will compare Data Base level entity and provide the delta
 * 
 * @author prdubey
 * 
 *         1) User provisioned Links => UI_LINK & CORE_LINK 2) Drawn Links,
 *         Containers, Nodes => UI_NODE, UI_CONTAINER, UI_LINK 3) Customer name
 *         , Connection Label => FRE 4) Annotation => ANNOTATION,
 *         ANNOTATION_ENTRY, UI_CONNECTION 5) Proposed connection Name, Last
 *         Mesh restoration time in case CP Connections ,Diversity parameters
 *         for CP SNCP connections = > FRE ,FRE ATTRIBUTE 6) USR and CBR
 *         Constraints in case of CP Connections => FRE, CONSTRAINTS 7)
 *         TODR/DOWR Profiles => PROFILE, PROFILE_ATTRIBUTES,
 *         PROFILE_FRE_ASSOCIATION
 * 
 */
public class DbLevelDeltaReport {

    public static final String SOURCE_DB = "resources/utils/dataBase1ConnectionProperties";
    public static final String TARGET_DB = "resources/utils/dataBase2ConnectionProperties";

    public static String REPORT_FILE_PARENT = "resources/report/";

    private OneControlConnection sourceDb;
    private OneControlConnection targetDb;

    List<UIContainer> deltaContainerList = new ArrayList<UIContainer>();
    List<UINode> deltaNodeList = new ArrayList<UINode>();
    List<Constraints> deltaConstraints = new ArrayList<Constraints>();

    List<TscConnectionDetails> deltaMissingFre = new ArrayList<TscConnectionDetails>();
    List<TscConnectionDetails> deltaParameterMistMatchFre = new ArrayList<TscConnectionDetails>();

    public static void main(String args[]) {

	if (args.length != 6) {
	    System.err.println("Invalid arguments" + Arrays.toString(args));
	    System.exit(1);
	}

	String sourceDbConfigFile = args[0];
	boolean isSourceOracleDbType = args[1].equals("ORACLE");

	String targetDbConfigFile = args[2];
	boolean istargetOracleDbType = args[3].equals("ORACLE");

	BaseContextSetUp.ONE_CONTROL_PROVIDER_URL = args[4];
	REPORT_FILE_PARENT=args[5];

	DbLevelDeltaReport dbLevelDeltaReport = new DbLevelDeltaReport(sourceDbConfigFile, isSourceOracleDbType, targetDbConfigFile, istargetOracleDbType);

	dbLevelDeltaReport.generateReports();
    }

    public DbLevelDeltaReport(String sourceDbFile, boolean isSourceOracleDbType, String targetDbFile, boolean istargetOracleDbType) {
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

    public void generateReports() {

	getContainerReport();
	getNodeReport();

	getConstraintReport();
	getFreDataReport();

	printReport();

    }

    private void getFreDataReport() {

	FreDataHandler freDataHandler = new FreDataHandler(sourceDb, targetDb);
	List<TscConnectionDetails> srcConnectionDetails = freDataHandler.retrieveFreDataBasedOnFreKeyFromActive();

	freDataHandler.changeActiveDb(true);
	List<TscConnectionDetails> tgtConnectionDetails = freDataHandler.retrieveFreDataBasedOnFreKeyFromActive();

	freDataHandler.changeActiveDb(false);

	boolean isTscMacthing = false;
	boolean isMactchingParameter = false;
	int result = -1;

	for (TscConnectionDetails src : srcConnectionDetails) {

	    isTscMacthing = false;
	    isMactchingParameter = false;
	    result = -1;

	    for (TscConnectionDetails dst : tgtConnectionDetails) {
		if (src.getFreKey().equalsIgnoreCase(dst.getFreKey())) {
		    isTscMacthing = true;
		    result = dst.compareTo(src);
		    if (result == 0) {
			isMactchingParameter = true;
			break;
		    }
		}
	    }
	    if (!isTscMacthing) {
		deltaMissingFre.add(src);
	    }
	    if (isTscMacthing && !isMactchingParameter) {
		deltaParameterMistMatchFre.add(src);
	    }
	}

    }

    private void getConstraintReport() {
	ConstraintHandler constraintHandler = new ConstraintHandler(sourceDb, targetDb);

	List<Constraints> srcConstratints = constraintHandler.getAllConstraints();

	constraintHandler.changeActiveDb(true);
	List<Constraints> dstConstratints = constraintHandler.getAllConstraints();
	constraintHandler.changeActiveDb(false);

	boolean isConstraintFound = false;

	for (Constraints src : srcConstratints) {
	    isConstraintFound = false;
	    for (Constraints dst : dstConstratints) {
		if (src.getConstraintKey().equals(dst.getConstraintKey())) {
		    isConstraintFound = true;
		    break;
		}
	    }
	    if (!isConstraintFound) {
		deltaConstraints.add(src);
	    }
	}
    }

    private void getNodeReport() {
	try {
	    NodeHandler nodeHandler = new NodeHandler(sourceDb, targetDb);

	    List<UINode> srcNodeList = nodeHandler.getAllNodes();

	    nodeHandler.changeActiveDb(true);
	    List<UINode> dstNodeList = nodeHandler.getAllNodes();
	    nodeHandler.changeActiveDb(false);

	    boolean isNodeFound = false;

	    for (UINode srcNode : srcNodeList) {
		isNodeFound = false;
		for (UINode dstNode : dstNodeList) {
		    if (srcNode.getNodeKey().equals(dstNode.getNodeKey())) {
			isNodeFound = true;
			break;
		    }
		}
		if (!isNodeFound) {
		    deltaNodeList.add(srcNode);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void getContainerReport() {
	try {
	    ContainerHandler containerHandler = new ContainerHandler(sourceDb, targetDb);

	    List<UIContainer> srcContainerList = containerHandler.getAllContainer();

	    containerHandler.changeActiveDb(true);
	    List<UIContainer> dstContainerList = containerHandler.getAllContainer();
	    containerHandler.changeActiveDb(false);

	    boolean isContainerFound = false;

	    for (UIContainer srcUiContainer : srcContainerList) {
		isContainerFound = false;
		for (UIContainer dstuiContainer : dstContainerList) {

		    if (srcUiContainer.getContainerKey().equals(dstuiContainer.getContainerKey())) {
			isContainerFound = true;
			break;
		    }
		}
		if (!isContainerFound) {
		    deltaContainerList.add(srcUiContainer);
		}

	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public void printReport() {
	  BufferedWriter writer = null;
	try {

	    writer = new BufferedWriter(new FileWriter(REPORT_FILE_PARENT + System.currentTimeMillis() + ".txt"));

	    System.out.println("########################################################################");

	    if (deltaContainerList != null) {
		System.out.println("Contanier Delta is  : " + deltaContainerList.size());
		writer.write("\n Contanier Delta is  : " + deltaContainerList.size());
		for (UIContainer uiContainer : deltaContainerList) {
		    System.out.println(uiContainer.getContainerKey() + "    " + uiContainer.getContainerType() + "      " + uiContainer.getName());
		    writer.write("\n" + uiContainer.getContainerKey() + "    " + uiContainer.getContainerType() + "      " + uiContainer.getName());
		}
	    }
	    System.out.println("**************************************************************************");
	    writer.write("\n########################################################################");
	    if (deltaNodeList != null) {
		System.out.println("Node  Delta is  : " + deltaNodeList.size());
		writer.write("\n Node  Delta is  : " + deltaNodeList.size());
		for (UINode uiNode : deltaNodeList) {
		    System.out.println(uiNode.getNodeKey() + "    " + uiNode.getNodeBasic().getName());
		    writer.write("\n" + uiNode.getNodeKey() + "    " + uiNode.getNodeBasic().getName());

		}
	    }
	    System.out.println("**************************************************************************");
	    writer.write("\n########################################################################");
	    if (deltaConstraints != null) {
		System.out.println("Constraints Delta are  : " + deltaConstraints.size());
		writer.write("\n" + "Constraints Delta are  : " + deltaConstraints.size());
		for (Constraints constraint : deltaConstraints) {
		    System.out.println(constraint.getConstraintKey() + "    " + constraint.getConstraintName());
		    writer.write("\n" + constraint.getConstraintKey() + "    " + constraint.getConstraintName());
		}
	    }
	    System.out.println("**************************************************************************");
	    writer.write("\n########################################################################");
	    if (deltaMissingFre != null) {
		System.out.println("Fres Not Present   : " + deltaMissingFre.size());
		writer.write("\n" + "Fres Not Present   : " + deltaMissingFre.size());
		for (TscConnectionDetails connectionDetails : deltaMissingFre) {
		    System.out.println(connectionDetails.toString());
		    writer.write("\n" + connectionDetails.toString());

		}
	    }

	    System.out.println("**************************************************************************");
	    writer.write("\n########################################################################");
	    if (deltaParameterMistMatchFre != null) {
		System.out.println("Fres Not having Matching Paremeters are    : " + deltaParameterMistMatchFre.size());
		writer.write("\n" + "Fres Not having Matching Paremeters are    : " + deltaParameterMistMatchFre.size());
		for (TscConnectionDetails connectionDetails : deltaParameterMistMatchFre) {
		    System.out.println(connectionDetails.toString());
		    writer.write("\n" + connectionDetails.toString());

		}
	    }
	    System.out.print("##########################################################################");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	finally{
	    if(writer !=null){
		try {
	            writer.close();
                } catch (IOException e) {
	            e.printStackTrace();
                }
	    }
	}

    }

}
