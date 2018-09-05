package verizon.handlers;

import utils.OneControlConnection;
import verizon.entity.UIContainerWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.ciena.rm.model.ManagedObjectKeys;
import com.ciena.rm.uinavigation.model.UIContainer;
import com.ciena.rm.uinavigation.model.UIContainerType;
import com.ciena.rm.uinavigation.model.UILink;
import com.ciena.rm.uinavigation.model.UIPlacementType;

public class DrawLinkHandler extends Handler {
    
    public DrawLinkHandler(OneControlConnection sourceDb, OneControlConnection targetDb){
	super(sourceDb, targetDb);
    }
    
    public List<UILink> getUILinks() {
	List<UILink> links = new ArrayList<UILink>();
	
	UILink link = new UILink();
	
	return links;
    }
    
    private List<UILink> getUIContainer(String query) {
	
   	List<UILink> uiLinkList = new ArrayList<UILink>();

   	PreparedStatement preparedStatement = null;
   	ResultSet rs = null;

   	try {
   	    // Get Data from Source DB
   	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
   	    rs = preparedStatement.executeQuery();

   	    while (rs.next()) {}

   	    rs.close();
   	    preparedStatement.close();
   	    getActiveDb().getConnection().close();

   	} catch (SQLException e) {

   	    e.printStackTrace();
   	} finally {
   	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
   	}
   	System.out.println("Link Data is  : " + uiLinkList);

   	return uiLinkList;
       }
    

}
