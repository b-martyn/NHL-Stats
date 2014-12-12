package connection;

import connection.DbConnection.Table;

public enum RosterPlayersFields implements TableField {
	ROSTERID(1), ROSTERPLAYERID(2), PLAYERNUMBER(3);
	
	private static final Table table = Table.ROSTERPLAYERS;
	private int columnNumber;
	
	private RosterPlayersFields(int columnNumber){
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
			case ROSTERPLAYERID:
				return FieldType.INT;
			case PLAYERNUMBER:
				return FieldType.BYTE;
			default:
				return null;
		}
	}
	
}