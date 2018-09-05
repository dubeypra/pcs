/**
 * 
 */
package verizon.handlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.ciena.rm.model.Annotation;
import com.ciena.rm.model.AnnotationEntry;


import utils.OneControlConnection;

/**
 * @author prdubey
 * 
 */
public class AnnotationHandler extends Handler {

    public static final String MY_SQL_GET_ANNOTATION_ENTRY = "Select * from ANNOTATION_ENTRY where ANN_ID = ? ";
    public static final String ORACLE_GET_ANNOTATION_ENTRY = "Select * from ANNOTATION_ENTRY where ANN_ID = ?";

    public AnnotationHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }

    public Annotation getAnnotaion(long annotioationId) {
	if(annotioationId ==0){
	    return null;
	}
	Annotation annotation = new Annotation();

	Set<AnnotationEntry> annotationEntries = retrieveAnnotationEntry(annotioationId);
	for (AnnotationEntry annotationEntry : annotationEntries) {
	    annotation.addAnnotationEntry(annotationEntry);
        }
	
	return annotation;
    }

    private Set<AnnotationEntry> retrieveAnnotationEntry(long annotioationId) {
	Set<AnnotationEntry> annotationEntries = new TreeSet<AnnotationEntry>();

	String query = "";

	if (getActiveDb().isOracle()) {
	    query = ORACLE_GET_ANNOTATION_ENTRY;
	} else {
	    query = MY_SQL_GET_ANNOTATION_ENTRY;
	}
	
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;
	
	try {
	    // Get Data from Source DB
	    preparedStatement = getActiveDb().getConnection().prepareStatement(query);
	    preparedStatement.setLong(1, annotioationId);
	    rs = preparedStatement.executeQuery();

	    while (rs.next()) {
		AnnotationEntry annotationEntry = new AnnotationEntry();
		
		annotationEntry.setSource(rs.getString("ANN_SOURCE"));
		annotationEntry.setText(rs.getString("ANN_TEXT"));
		annotationEntry.setTime(rs.getLong("ANN_TIME"));
		
		annotationEntries.add(annotationEntry);
		
	    }

	    rs.close();
	    preparedStatement.close();
	    getActiveDb().getConnection().close();

	} catch (SQLException e) {

	    e.printStackTrace();
	} finally {
	    getActiveDb().cleanResources(rs, getActiveDb().getConnection(), null, preparedStatement);
	}

	return annotationEntries;
    }

}
