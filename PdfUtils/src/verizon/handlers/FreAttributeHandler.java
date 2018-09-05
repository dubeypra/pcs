package verizon.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import telestra.TscConnectionDetails;
import utils.OneControlConnection;
import verizon.entity.OneControlSql;

public class FreAttributeHandler extends Handler {

    public FreAttributeHandler(OneControlConnection sourceDb, OneControlConnection targetDb) {
	super(sourceDb, targetDb);
    }
    
   

}
