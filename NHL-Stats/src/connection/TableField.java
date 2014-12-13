package connection;

import connection.DbConnector.Table;

public interface TableField {
	enum FieldType{
		BYTE, SHORT, INT, DATE, STRING, BOOLEAN;
	}
	
	int getColumnNumber();
	Table getTable();
	FieldType getType();
}
