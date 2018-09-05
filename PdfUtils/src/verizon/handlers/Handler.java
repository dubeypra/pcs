/**
 * 
 */
package verizon.handlers;

import utils.OneControlConnection;

/**
 * @author prdubey
 *
 */
public class Handler {
    
    private OneControlConnection sourceDb;
    private OneControlConnection targetDb;
    
    private boolean isActiveDbChangeOver = false;
    
    
    public Handler(OneControlConnection dataBaseConnection1,OneControlConnection dataBaseConnection2) {
	this.sourceDb = dataBaseConnection1;
	this.targetDb = dataBaseConnection2;
    }
    
    public OneControlConnection getActiveDb() {
	if(isActiveDbChangeOver){
	    return targetDb;
	}
        return sourceDb;
    }
   
   public OneControlConnection getTargetDb() {
       return targetDb;
   }
    
   public void changeActiveDb(boolean forceChange){
       isActiveDbChangeOver = forceChange;
   }
}
