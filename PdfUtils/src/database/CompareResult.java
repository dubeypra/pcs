package database;

public class CompareResult {

    private String srcdbName;
    private String srcTableName;
    private int srcRowCount;

    private String tgtdbName;
    private String tgtTableName;
    private int tgtRowCount;

    public String getSrcdbName() {
	return srcdbName;
    }

    public void setSrcdbName(String srcdbName) {
	this.srcdbName = srcdbName;
    }

    public String getSrcTableName() {
	return srcTableName;
    }

    public void setSrcTableName(String srcTableName) {
	this.srcTableName = srcTableName;
    }

    public int getSrcRowCount() {
	return srcRowCount;
    }

    public void setSrcRowCount(int srcRowCount) {
	this.srcRowCount = srcRowCount;
    }

    public String getTgtdbName() {
	return tgtdbName;
    }

    public void setTgtdbName(String tgtdbName) {
	this.tgtdbName = tgtdbName;
    }

    public String getTgtTableName() {
	return tgtTableName;
    }

    public void setTgtTableName(String tgtTableName) {
	this.tgtTableName = tgtTableName;
    }

    public int getTgtRowCount() {
	return tgtRowCount;
    }

    public void setTgtRowCount(int tgtRowCount) {
	this.tgtRowCount = tgtRowCount;
    }

    @Override
    public String toString() {
       return srcdbName+" || "+srcTableName+" || "+srcRowCount+" ==>>  "+tgtdbName+" || "+tgtTableName+" || "+tgtRowCount;
    }
}
