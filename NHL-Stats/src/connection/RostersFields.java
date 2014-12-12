package connection;

import connection.DbConnection.Table;

public enum RostersFields implements TableField {
	ROSTERID(1), TEAM(2), STARTDATE(3), ENDDATE(4);
	
	private static final Table table = Table.ROSTERS;
	private int columnNumber;
	
	private RostersFields(int columnNumber){
		this.columnNumber = columnNumber;
	}
	
	@Override
	public int getColumnNumber() {
		return columnNumber;
	}

	@Override
	public Table getTable() {
		return table;
	}

	@Override
	public FieldType getType() {
			switch(this){
			case ROSTERID:
				return FieldType.INT;
			case TEAM:
				return FieldType.STRING;
			case STARTDATE:
				return FieldType.DATE;
			case ENDDATE:
				return FieldType.DATE;
			default:
				return null;
		}
	}
	
}
