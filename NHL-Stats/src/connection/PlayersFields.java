package connection;

import connection.DbConnection.Table;

public enum PlayersFields implements TableField {
	PLAYERID(1), ACTIVE(2), FIRSTNAME(3), LASTNAME(4), POSITION(5);

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
			case ACTIVE:
				return FieldType.BOOLEAN;
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
