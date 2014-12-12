package connection;

import connection.DbConnector.Table;

public enum ShotPlayersFields implements TableField {
	SHOTID(1), SHOTPLAYERID(2);
	
	private static final Table table = Table.SHOTPLAYERS;
	private int columnNumber;
	
	private ShotPlayersFields(int columnNumber){
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
		case SHOTPLAYERID:
			return FieldType.INT;
		case SHOTID:
			return FieldType.INT;
		default:
			return null;
		}
	}
}
