/**
 * 
 */
package verizon.handlers;

import utils.OneControlConnection;
import verizon.entity.OneControlSql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.ciena.rm.constraints.api.db.entity.ConstraintDetails;
import com.ciena.rm.constraints.api.db.entity.Constraints;
import com.ciena.rm.model.ManagedObjectKeys;


import com.ciena.rm.model.constraint.*;

/**
 * @author prdubey
 * 
 */
public class ConstraintHandler extends Handler {

    

    public ConstraintHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }

    public List<Constraints> getAllConstraints() {
	List<Constraints> constraintList = getCostraint();
	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_CONSTRAINTDETILS;
	} else {
	    query = OneControlSql.MY_SQL_GET_CONSTRAINTDETAILS;
	}

	for (Constraints constraints : constraintList) {
	    retrieveConstraintDetails(constraints, query);
	}

	return constraintList;

    }

    private void retrieveConstraintDetails(Constraints constraints, String query) {
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    preparedStatement.setLong(1, constraints.getId());
	    rs = preparedStatement.executeQuery();
	    ConstraintDetails constraintDetails;
	    while (rs.next()) {

		ConstraintDetailsData constraintDetailsData = new ConstraintDetailsData();

		String inclusionType = rs.getString("INCLUSION_TYPE");

		if (inclusionType != null) {
		    ConstraintInclusionType constraintInclusionType = ConstraintInclusionType.valueOf(inclusionType);
		    constraintDetailsData.setConstraintObjetKey(ManagedObjectKeys.newKey(rs.getString("CONSTRAINT_OBJECT_KEY")));
		    
		    String objectType = rs.getString("OBJECT_TYPE");
		    
		    if(objectType !=null){
			constraintDetailsData.setObjectType(ConstraintObjectType.valueOf(objectType));
		    }
		    
		    
		    
		    if (constraintInclusionType.name().equalsIgnoreCase(ConstraintInclusionType.EXCLUDE.name())) {
			constraintDetails = new ConstraintDetails(constraintDetailsData, ConstraintInclusionType.EXCLUDE);
			constraints.addConstraintDetails(constraintDetails);
		    } else if (constraintInclusionType.name().equalsIgnoreCase(ConstraintInclusionType.INCLUDE.name())) {
			constraintDetails = new ConstraintDetails(constraintDetailsData, ConstraintInclusionType.INCLUDE);
			constraints.addConstraintDetails(constraintDetails);
		    }
		}

	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}

    }

    private List<Constraints> getCostraint() {

	String query = "";

	if (getActiveDb().isOracle()) {
	    query = OneControlSql.ORACLE_GET_CONSTRAINTS;
	} else {
	    query = OneControlSql.MY_SQL_GET_CONSTRAINTS;
	}

	List<Constraints> constraintList = new ArrayList<Constraints>();

	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {

		ConstraintData constraintData = new ConstraintData();

		String constraintDiversityType = rs.getString("CONSTRAINT_DIVERSITY");

		if (constraintDiversityType != null) {
		    constraintData.setConstraintDiversityType(ConstraintDiversityType.valueOf(constraintDiversityType));
		}

		constraintData.setConstraintKey(ManagedObjectKeys.newKey(rs.getString("CONSTRAINT_KEY")));
		constraintData.setConstraintLabel(rs.getString("CONSTRAINT_LABEL"));

		String constraintType = rs.getString("CONSTRAINT_TYPE");
		if (constraintType != null) {
		    constraintData.setConstraintType(ConstraintType.valueOf(constraintType));
		}

		constraintData.setId(rs.getString("ID"));
		constraintData.setName(rs.getString("CONSTRAINT_NAME"));

		Constraints constraints = new Constraints(constraintData);

		constraintList.add(constraints);
	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}

	return constraintList;
    }
    
}
