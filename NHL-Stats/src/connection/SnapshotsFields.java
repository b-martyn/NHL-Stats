package connection;

import connection.DbConnection.Table;

public enum SnapshotsFields implements TableField {
	SNAPSHOTID(1), GAMEID(2), PERIOD(3), ELAPSEDSECONDS(4), SECONDSLEFT(5);
	
	private static final Table table = Table.SNAPSHOTS;
	private int columnNumber;
	
	private SnapshotsFields(int columnNumber){
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
			case SNAPSHOTID:
				return FieldType.INT;
			case GAMEID:
				return FieldType.INT;
			case PERIOD:
				return FieldType.BYTE;
			case ELAPSEDSECONDS:
				return FieldType.SHORT;
			case SECONDSLEFT:
				return FieldType.SHORT;
			default:
				return null;
		}
	}
}
