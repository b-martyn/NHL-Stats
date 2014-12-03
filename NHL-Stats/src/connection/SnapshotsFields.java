package connection;

import connection.DbConnection.Table;

public enum SnapshotsFields implements TableField {
	ID(1), GAMEID(2), PERIOD(3), ELAPSEDSECONDS(4), SECONDSLEFT(5), HOMEPLAYERSONICE(6), AWAYPLAYERSONICE(7);
	
	private static final Table table = Table.SNAPSHOTS;
	private int columnNumber;
	
	private SnapshotsFields(int columnNumber){
		this.columnNumber = columnNumber;
	}
	
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
			case AWAYPLAYERSONICE:
				return FieldType.STRING;
			case ELAPSEDSECONDS:
				return FieldType.SHORT;
			case GAMEID:
				return FieldType.INT;
			case HOMEPLAYERSONICE:
				return FieldType.STRING;
			case ID:
				return FieldType.INT;
			case PERIOD:
				return FieldType.BYTE;
			case SECONDSLEFT:
				return FieldType.SHORT;
			default:
				return null;
		}
	}

}
