package connection;

import connection.DbConnector.Table;

public enum TimeEventsFields implements TableField {
	TIMEEVENTID(1), SNAPSHOTID(2), TIMEEVENTTYPE(3), STARTINGCLOCK(4);
	
	private static final Table table = Table.TIMEEVENTS;
	private int columnNumber;
	
	private TimeEventsFields(int columnNumber){
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
		switch (this){
			case TIMEEVENTID:
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
