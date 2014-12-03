package connection;

import connection.DbConnection.Table;

public enum PlayersFields implements TableField {
	ID(1), ACTIVE(2), FIRSTNAME(3), LASTNAME(4), TEAM(5), POSITION(6), NUMBER(7);

	private static final Table table = Table.PLAYERS;
	private int columnNumber;
	
	private PlayersFields(int columnNumber){
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
			case ACTIVE:
				return FieldType.BOOLEAN;
			case FIRSTNAME:
				return FieldType.STRING;
			case ID:
				return FieldType.INT;
			case LASTNAME:
				return FieldType.STRING;
			case NUMBER:
				return FieldType.BYTE;
			case POSITION:
				return FieldType.STRING;
			case TEAM:
				return FieldType.STRING;
			default:
				return null;
		}
	}

}
