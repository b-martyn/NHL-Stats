package connection;

import connection.DbConnection.Table;

interface TableField {
	enum FieldType{
		BYTE, SHORT, INT, DATE, STRING, BOOLEAN;
	}
	
	int getColumnNumber();
	Table getTable();
	FieldType getType();
}
