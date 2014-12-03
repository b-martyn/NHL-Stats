package connection;

import connection.DbConnection.Table;

public enum ShotsFields implements TableField {
	ID(1), SNAPSHOTID(2), SHOTTYPE(3), DISTANCE(4), FIRSTPLAYERID(5), SECONDPLAYERID(6), THIRDPLAYERID(7), MISSEDSHOTLOCATION(8), GOAL(8);
	
	private static final Table table = Table.SHOTS;
	private int columnNumber;
	
	private ShotsFields(int columnNumber){
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
			case DISTANCE:
				return FieldType.BYTE;
			case FIRSTPLAYERID:
				return FieldType.INT;
			case GOAL:
				return FieldType.BOOLEAN;
			case ID:
				return FieldType.INT;
			case MISSEDSHOTLOCATION:
				return FieldType.STRING;
			case SECONDPLAYERID:
				return FieldType.INT;
			case SHOTTYPE:
				return FieldType.STRING;
			case SNAPSHOTID:
				return FieldType.INT;
			case THIRDPLAYERID:
				return FieldType.INT;
			default:
				return null;
		}
	}

}
