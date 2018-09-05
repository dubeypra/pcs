package database;

public class TableDetails {
    
    private String tableName;
    private int rowCount;
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public int getRowCount() {
        return rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    
    @Override
    public String toString() {
       return tableName+" : "+rowCount;
    }

}
