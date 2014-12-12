package connection;

import connection.DbConnector.Table;

public enum PlayerEventsFields implements TableField {
	PLAYEREVENTID(1), SNAPSHOTID(2), PLAYEREVENTTYPE(3), ZONE(4), PLAYERID(5), INFRACTION(6), MINUTES(7);

	private static final Table table = Table.PLAYEREVENTS;
	private int columnNumber;
	
	private PlayerEventsFields(int columnNumber){
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
			case PLAYEREVENTID:
				return FieldType.INT;
			case INFRACTION:
				return FieldType.STRING;
			case MINUTES:
				return FieldType.BYTE;
			case PLAYEREVENTTYPE:
				return FieldType.STRING;
			case PLAYERID:
				return FieldType.INT;
			case SNAPSHOTID:
				return FieldType.INT;
			case ZONE:
				return FieldType.BYTE;
			default:
				return null;
		}
	}

}
