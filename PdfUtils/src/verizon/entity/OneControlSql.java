/**
 * 
 */
package verizon.entity;

/**
 * @author prdubey
 *
 */
public class OneControlSql {
    
    public static String MY_SQL_SCHEMA=" ";
    public static String ORACLE_SCHEMA=" ";
    
    //Container Handler
    
    public static final String MY_SQL_GET_MANUAL_CONTAINER = "Select * from"+MY_SQL_SCHEMA+"UI_CONTAINER where CONTAINER_TYPE='MANUAL' and PARENT_ID IN (SELECT ID FROM "+MY_SQL_SCHEMA+"UI_CONTAINER where NAME='Top Level Map')";
    public static final String ORACLE_GET_MANUAL_CONTAINER = "Select * from"+ORACLE_SCHEMA +"UI_CONTAINER where CONTAINER_TYPE='MANUAL' AND PARENT_ID IN (SELECT ID FROM "+ORACLE_SCHEMA +"UI_CONTAINER WHERE NAME ='Top Level Map')";

    public static final String MY_SQL_GET_CONTAINER = "Select * from "+MY_SQL_SCHEMA+"UI_CONTAINER where ID= ";
    public static final String ORACLE_GET_CONTAINER = "Select * from "+ORACLE_SCHEMA +"UI_CONTAINER where ID= ";
    
    public static final String MY_SQL_GET_CONTAINER_WITH_PARENT_ID = "Select * from "+MY_SQL_SCHEMA+"UI_CONTAINER where PARENT_ID= ";
    public static final String ORACLE_GET_CONTAINER_WITH_PARENT_ID = "Select * from "+ORACLE_SCHEMA +"UI_CONTAINER where PARENT_ID= ";

    
    //Node Handler
    
    public static final String MY_SQL_GET_MANUAL_NODE= "select * from "+MY_SQL_SCHEMA+"UI_NODE where MANUAL =1";
    public static final String ORACLE_GET_MANUAL_NODE= "select * from "+ORACLE_SCHEMA +"UI_NODE where MANUAL =1";
    
    public static final String MY_SQL_GET_LAYOUTS="Select * from "+MY_SQL_SCHEMA+"UI_NODE_LAYOUT where NODE_ID = ?";
    public static final String ORACLE_GET_LAYOUTS="Select * from "+ORACLE_SCHEMA +"UI_NODE_LAYOUT where NODE_ID = ?";
    
    //Constraint Handler
    public static final String MY_SQL_GET_CONSTRAINTS = "Select * from "+MY_SQL_SCHEMA+"`CONSTRAINTS`";
    public static final String ORACLE_GET_CONSTRAINTS = "Select * from "+ORACLE_SCHEMA +"\"CONSTRAINTS\"";
    
    public static final String MY_SQL_INSERT_CONSTRAINTS = "insert into "+MY_SQL_SCHEMA+"`CONSTRAINTS`"+"values(";
    public static final String ORACLE_INSERT_CONSTRAINTS = "Select * from "+ORACLE_SCHEMA +"\"CONSTRAINTS\"";

    public static final String MY_SQL_GET_CONSTRAINTDETAILS = "Select * from "+MY_SQL_SCHEMA+"CONSTRAINT_DETAILS where CONSTRAINT_ID = ?";
    public static final String ORACLE_GET_CONSTRAINTDETILS = "Select * from  "+ORACLE_SCHEMA +"CONSTRAINT_DETAILS where CONSTRAINT_ID = ?";
    
    //FRE Data
    
    public static final String UPDATE_UI_CONNECTIONS_BACKUP = "update UI_CONNECTION A  "
		+ "SET A.IN_PROVISIONING =? ,A.DISPLAYNAME=?,A.DISPLAYNETWORKNAME=?,A.CONNECTION_KEY=?"
		+ " where A.FRE_ID =?";
    
    public static final String GET_FRE_KEY_NAME_ACTIVE = "select B.FRE_KEY, B.NATIVE_NAME_ALIAS,B.UMS_NAME_ALIAS,B.LABEL,B.CUSTOMER,B.IS_MANAGED from FRE B";
    
    public static final String UPDATE_FRE_KEY_NAME_BACKUP = "UPDATE FRE B SET B.NATIVE_NAME_ALIAS=?,B.UMS_NAME_ALIAS=?,B.IS_MANAGED=?,B.LABEL=?,B.CUSTOMER=? "
		+ "where B.FRE_KEY=?";
    
    public static final String RETRIEVE_UI_CONNECTIONS = "Select B.FRE_KEY,A.DISPLAYNAME,DISPLAYNETWORKNAME,A.CONNECTION_KEY,A.IN_PROVISIONING from UI_CONNECTION A,FRE B Where A.FRE_ID=B.ID";
    public static final String RETRIEVE_FRE_ID = "Select A.ID from FRE A where A.FRE_KEY=?";

}
