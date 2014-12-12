package connection;

import connection.DbConnector.Table;

interface TableField {
	enum FieldType{
		BYTE, SHORT, INT, DATE, STRING, BOOLEAN;
	}
	
	int getColumnNumber();
	Table getTable();
	FieldType getType();
}
