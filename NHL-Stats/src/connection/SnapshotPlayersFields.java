package connection;

import connection.DbConnection.Table;

public enum SnapshotPlayersFields implements TableField {
	SNAPSHOTID(1), SNAPSHOTPLAYERID(2);
	
	private static final Table table = Table.SNAPSHOTPLAYERS;
	private int columnNumber;
	
	private SnapshotPlayersFields(int columnNumber){
		this.columnNumber = columnNumber;
	}

	@Override
	public int getColumnNumber(){
		return columnNumber;
	}
	
	@Override
	public Table getTable() {
		return table;
	}

	@Override
	public FieldType getType() {
		switch(this){
		case SNAPSHOTPLAYERID:
			return FieldType.INT;
		case SNAPSHOTID:
			return FieldType.INT;
		default:
			return null;
		}
	}
}
