/**
 * 
 */
package verizon.handlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ciena.gr.api.ondemand.OnDemandReplicationException;
import com.ciena.rm.model.Annotation;
import com.ciena.rm.model.ManagedObjectKeys;
import com.ciena.rm.uinavigation.model.UIContainer;
import com.ciena.rm.uinavigation.model.UIContainerReplicateData;
import com.ciena.rm.uinavigation.model.UIContainerType;
import com.ciena.rm.uinavigation.model.UIPlacementType;

import service.invoke.ContainerServiceStub;
import utils.OneControlConnection;
import verizon.entity.OneControlSql;
import verizon.entity.UIContainerWrapper;

/**
 * @author prdubey
 * 
 */
public class ContainerHandler extends Handler {

    
    public ContainerHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }

    /**
     * Only one level of parent container hierarchy is persisted
     */
    public void handleManualContainerEntity() {
	AnnotationHandler annotationHandler = new AnnotationHandler(getActiveDb(), getTargetDb());
	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_MANUAL_CONTAINER;
	} else {
	    query = OneControlSql.MY_SQL_GET_MANUAL_CONTAINER;
	}

	List<UIContainerWrapper> containerWrapperList = getUIContainer(query);// Child of Top Level Map
	syncContinerDataWithTargetServer(annotationHandler, containerWrapperList);
	
	do{
	    containerWrapperList = recursiveSync(annotationHandler, containerWrapperList,true);
	}while (containerWrapperList.size()>0);
	
    }
    
    public List<UIContainer> getAllContainer(){
	
	List<UIContainer> containerList = new ArrayList<UIContainer>();
	
	AnnotationHandler annotationHandler = new AnnotationHandler(getActiveDb(), getTargetDb());
	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_MANUAL_CONTAINER;
	} else {
	    query = OneControlSql.MY_SQL_GET_MANUAL_CONTAINER;
	}

	List<UIContainerWrapper> containerWrapperList = getUIContainer(query);// Child of Top Level Map
	//syncContinerDataWithTargetServer(annotationHandler, containerWrapperList);
	for (UIContainerWrapper uiContainerWrapper : containerWrapperList) {
	    containerList.add(uiContainerWrapper.getContainer());
        }
	
	do{
	    containerWrapperList = recursiveSync(annotationHandler, containerWrapperList,false);
	    
	    for (UIContainerWrapper uiContainerWrapper : containerWrapperList) {
		    containerList.add(uiContainerWrapper.getContainer());
	        }
	    
	}while (containerWrapperList.size()>0);
	
	return containerList;
	
    }

    private List<UIContainerWrapper> recursiveSync(AnnotationHandler annotationHandler, List<UIContainerWrapper> containerWrapperList,boolean targetSyncRequired) {
	String query;
	
	List<UIContainerWrapper> containerWrapperListLevel1 = new ArrayList<UIContainerWrapper>();
	
	for (UIContainerWrapper uiContainerWrapper : containerWrapperList) { // 
	    if (getActiveDb().isOracle()) {
		    query = OneControlSql.ORACLE_GET_CONTAINER_WITH_PARENT_ID+uiContainerWrapper.getContainer().getId();
		} else {
		    query = OneControlSql.MY_SQL_GET_CONTAINER_WITH_PARENT_ID+uiContainerWrapper.getContainer().getId();
		}
	    
	    containerWrapperListLevel1.addAll(getUIContainer(query));
	    if(targetSyncRequired)
		syncContinerDataWithTargetServer(annotationHandler, containerWrapperListLevel1);
        }
	
	return containerWrapperListLevel1;
    }

    private void syncContinerDataWithTargetServer(AnnotationHandler annotationHandler, List<UIContainerWrapper> containerWrapperList) {
	for (UIContainerWrapper containerWrapper : containerWrapperList) {
	    	UIContainer container = containerWrapper.getContainer();
	    	
	    	addParentContainers(annotationHandler, containerWrapper, container);
		updateAnnotation(annotationHandler, containerWrapper, container);
		
		UIContainerReplicateData containerReplicateData = new UIContainerReplicateData(container);
		
		try {
		    ContainerServiceStub.getContainerService().onDemandReplicateContainer(containerReplicateData);
		} catch (OnDemandReplicationException e) {
		    e.printStackTrace();
		}
        }
    }

    private void addParentContainers(AnnotationHandler annotationHandler, UIContainerWrapper containerWrapper, UIContainer container) {
	String query;
	if (containerWrapper.getParentId() != 0) {
	    if (getActiveDb().isOracle()) {
		query = OneControlSql.ORACLE_GET_CONTAINER + containerWrapper.getParentId();
	    } else {
		query = OneControlSql.MY_SQL_GET_CONTAINER + containerWrapper.getParentId();
	    }

	    UIContainerWrapper containerWrapperParent = getUIContainer(query).get(0); //Only One Parent
	    if (containerWrapperParent.getContainer() != null) {
		UIContainer parentContainer = containerWrapperParent.getContainer();

		updateAnnotation(annotationHandler, containerWrapperParent, parentContainer);
		container.setParent(parentContainer);

	    }
	}
    }

    private void updateAnnotation(AnnotationHandler annotationHandler, UIContainerWrapper containerWrapperParent, UIContainer parentContainer) {
	Annotation annotation = annotationHandler.getAnnotaion(containerWrapperParent.getAnnotationID());

	if (annotation != null) {
	    parentContainer.setAnnotation(annotation);
	}
    }

    private List<UIContainerWrapper> getUIContainer(String query) {
	
	List<UIContainerWrapper> containerWrapperList = new ArrayList<UIContainerWrapper>();

	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		
		UIContainerWrapper containerWrapper = new UIContainerWrapper();
		UIContainer container = new UIContainer();

		containerWrapper.setContainer(container);
		
		container.setId(rs.getLong("ID"));
		container.setBackgroundScale(rs.getDouble("BACKGROUND_SCALE"));
		container.setImageURL(rs.getString("IMAGE_URL"));
		container.setName(rs.getString("NAME"));
		container.setX(rs.getInt("X"));
		container.setY(rs.getInt("Y"));

		String containerType = rs.getString("CONTAINER_TYPE");
		container.setContainerType(UIContainerType.valueOf(containerType));

		String containerKey = rs.getString("CONTAINER_KEY");
		container.setContainerKey(ManagedObjectKeys.newKey(containerKey));

		containerWrapper.setParentId(rs.getInt("PARENT_ID"));
		containerWrapper.setAnnotationID(rs.getLong("ANNOTATION_ID"));

		String placementType = rs.getString("PLACEMENT");
		container.setPlacement(UIPlacementType.valueOf(placementType));
		
		containerWrapperList.add(containerWrapper);

	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}
	System.out.println("Container Data Retrieved  : " + containerWrapperList);

	return containerWrapperList;
    }

}
