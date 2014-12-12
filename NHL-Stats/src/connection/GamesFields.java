package connection;

import connection.DbConnector.Table;

public enum GamesFields implements TableField {
	GAMEID(1), DATE(2), HOMETEAM(3), HOMESCORE(4), AWAYTEAM(5), AWAYSCORE(6);
	
	private static final Table table = Table.GAMES;
	private int columnNumber;
	
	private GamesFields(int columnNumber){
		this.columnNumber = columnNumber;
	}

	@Override
	public int getColumnNumber(){
		return columnNumber;
	}
	
	@Override
	public FieldType getType(){
		switch (this){
			case AWAYSCORE:
				return FieldType.BYTE;
			case AWAYTEAM:
				return FieldType.STRING;
			case DATE:
				return FieldType.DATE;
			case HOMESCORE:
				return FieldType.BYTE;
			case HOMETEAM:
				return FieldType.STRING;
			case GAMEID:
				return FieldType.INT;
			default:
				return null;
		}
	}
	
	@Override
	public Table getTable() {
		return table;
	}
}
