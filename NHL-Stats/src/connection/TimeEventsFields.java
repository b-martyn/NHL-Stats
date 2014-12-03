package connection;

import connection.DbConnection.Table;

public enum TimeEventsFields implements TableField {
	ID(1), SNAPSHOTID(2), TIMEEVENTTYPE(3), STARTINGCLOCK(4);
	
	private static final Table table = Table.TIMEEVENTS;
	private int columnNumber;
	
	private TimeEventsFields(int columnNumber){
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
		switch (this){
			case ID:
				return FieldType.INT;
			case SNAPSHOTID:
				return FieldType.INT;
			case STARTINGCLOCK:
				return FieldType.BOOLEAN;
			case TIMEEVENTTYPE:
				return FieldType.STRING;
			default:
				return null;
		}
	}

}
