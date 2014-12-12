package connection;

import connection.DbConnector.Table;

public enum ShotsFields implements TableField {
	SHOTID(1), SNAPSHOTID(2), SHOTTYPE(3), DISTANCE(4), PLAYERID(5), MISSEDSHOTLOCATION(6), GOAL(7);
	
	private static final Table table = Table.SHOTS;
	private int columnNumber;
	
	private ShotsFields(int columnNumber){
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
			case DISTANCE:
				return FieldType.BYTE;
			case PLAYERID:
				return FieldType.INT;
			case GOAL:
				return FieldType.BOOLEAN;
			case SHOTID:
				return FieldType.INT;
			case MISSEDSHOTLOCATION:
				return FieldType.STRING;
			case SHOTTYPE:
				return FieldType.STRING;
			case SNAPSHOTID:
				return FieldType.INT;
			default:
				return null;
		}
	}

}
