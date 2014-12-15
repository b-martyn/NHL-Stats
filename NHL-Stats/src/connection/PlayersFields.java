package connection;

import connection.DbConnector.Table;

public enum PlayersFields implements TableField {
	PLAYERID(1), FIRSTNAME(2), LASTNAME(3), POSITION(4);

	private static final Table table = Table.PLAYERS;
	private int columnNumber;
	
	private PlayersFields(int columnNumber){
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
			case FIRSTNAME:
				return FieldType.STRING;
			case PLAYERID:
				return FieldType.INT;
			case LASTNAME:
				return FieldType.STRING;
			case POSITION:
				return FieldType.STRING;
			default:
				return null;
		}
	}

}
