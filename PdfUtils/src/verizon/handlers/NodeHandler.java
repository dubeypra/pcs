package verizon.handlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ciena.gr.api.ondemand.OnDemandReplicationException;
import com.ciena.model.ObjectKey;
import com.ciena.rm.me.model.SyncState;
import com.ciena.rm.model.CommunicationState;
import com.ciena.rm.model.EmsSource;
import com.ciena.rm.model.ManagedObjectKeys;
import com.ciena.rm.model.ManagedState;
import com.ciena.rm.model.MeType;
import com.ciena.rm.model.NeModeType;
import com.ciena.rm.model.NeRole;
import com.ciena.rm.uinavigation.model.UINode;
import com.ciena.rm.uinavigation.model.UINodeBasic;
import com.ciena.rm.uinavigation.model.UINodeLayout;

import service.invoke.ContainerServiceStub;
import service.invoke.NodeConfigProcessorStub;
import utils.OneControlConnection;
import verizon.entity.OneControlSql;
import verizon.entity.UINodeWrapper;

public class NodeHandler extends Handler {

    public NodeHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }

    public void handleManualNodeEntity() {
	AnnotationHandler annotationHandler = new AnnotationHandler(getActiveDb(), getTargetDb());

	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_MANUAL_NODE;
	} else {
	    query = OneControlSql.MY_SQL_GET_MANUAL_NODE;
	}

	List<UINodeWrapper> nodeList = getNode(query);
	UINode node;

	for (UINodeWrapper uiNodeWrapper : nodeList) {

	    node = uiNodeWrapper.getUiNode();
	    node.setAnnotation(annotationHandler.getAnnotaion(uiNodeWrapper.getAnnotationId()));
	    node.setLayouts(getNodeLayout(node.getId()));
	}
	
	/*try {
	    NodeConfigProcessorStub.getNodeConfigProcessor().createN.getContainerService().onDemandReplicateContainer(containerReplicateData);
	} catch (OnDemandReplicationException e) {
	    e.printStackTrace();
	}*/
    }

    public List<UINode> getAllNodes() {
	AnnotationHandler annotationHandler = new AnnotationHandler(getActiveDb(), getTargetDb());
	List<UINode> nodeList = new ArrayList<UINode>();

	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_MANUAL_NODE;
	} else {
	    query = OneControlSql.MY_SQL_GET_MANUAL_NODE;
	}

	List<UINodeWrapper> nodeWrapperList = getNode(query);
	UINode node;

	for (UINodeWrapper uiNodeWrapper : nodeWrapperList) {

	    node = uiNodeWrapper.getUiNode();
	    node.setAnnotation(annotationHandler.getAnnotaion(uiNodeWrapper.getAnnotationId()));
	    node.setLayouts(getNodeLayout(node.getId()));

	    nodeList.add(uiNodeWrapper.getUiNode());
	}

	return nodeList;

    }

    private List<UINodeWrapper> getNode(String query) {
	List<UINodeWrapper> nodeList = new ArrayList<UINodeWrapper>();

	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		UINodeWrapper uiNodeWrapper = new UINodeWrapper();

		uiNodeWrapper.setNodeId(rs.getLong("ID"));

		UINodeBasic uiNodeBasic = new UINodeBasic();

		uiNodeBasic.setName(rs.getString("NAME"));
		uiNodeBasic.setIpAddress(rs.getString("IP_ADDRESS"));
		uiNodeBasic.setMacAddress(rs.getString("MAC_ADDRESS"));
		uiNodeBasic.setMeType(MeType.valueOf(rs.getString("ME_TYPE")));
		uiNodeBasic.setMemberShelfTypes(rs.getString("MEMBER_SHELF_TYPES"));
		uiNodeBasic.setShelfType(rs.getString("SHELF_TYPE"));
		uiNodeBasic.setVersion(rs.getString("VERSION"));
		uiNodeBasic.setNativeLocation(rs.getString("NATIVE_LOCATION"));
		uiNodeBasic.setCommState(CommunicationState.valueOf(rs.getString("COMM_STATE")));
		uiNodeBasic.setSyncState(SyncState.valueOf(rs.getString("SYNC_STATE")));
		uiNodeBasic.setMeMgrIpAddress(rs.getString("MGR_IP_ADDRESS"));
		uiNodeBasic.setManual(rs.getInt("MANUAL") == 1 ? true : false);
		uiNodeBasic.setNeRole(NeRole.valueOf(rs.getString("ME_ROLE")));
		uiNodeBasic.setManagedState(ManagedState.valueOf(rs.getString("MANAGED_STATE")));

		String meMode = rs.getString("ME_MODE");
		if (meMode != null) {
		    uiNodeBasic.setMeMode(NeModeType.valueOf(meMode));
		}

		uiNodeBasic.setSpanID(rs.getString("SPAN_ID"));

		String emsSource = rs.getString("EMS_SOURCE");

		if (emsSource != null) {
		    uiNodeBasic.setEmsSource(EmsSource.valueOf(emsSource));
		}

		String nodeKey = rs.getString("NODE_KEY");
		ObjectKey nodeObjectKey = ManagedObjectKeys.newKey(nodeKey);
		UINode uiNode = new UINode(nodeObjectKey, uiNodeBasic, null, null);
		uiNode.setDeleteafter(rs.getTimestamp("DELETE_AFTER"));

		uiNodeWrapper.setUiNode(uiNode);
		nodeList.add(uiNodeWrapper);
	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}

	return nodeList;
    }

    /**
     * To DO : Node Layout yet to finalize
     * 
     * @param id
     * @return
     */
    private List<UINodeLayout> getNodeLayout(long id) {
	List<UINodeLayout> nodeLayoutList = new ArrayList<UINodeLayout>();

	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_LAYOUTS;
	} else {
	    query = OneControlSql.MY_SQL_GET_LAYOUTS;
	}
	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    preparedStatement.setLong(1, id);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		/*
	         * UINodeLayout uiNodeLayout = new UINodeLayout();
	         * uiNodeLayout.setChain(chain);
	         * uiNodeLayout.setChainOrder(chainOrder);
	         * uiNodeLayout.setContainer(container); uiNodeLayout.set
	         */

	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}

	return nodeLayoutList;

    }

}
