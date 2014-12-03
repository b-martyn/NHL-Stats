package connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbConnectionStatement {
	
	public enum Comparator{
		EQUAL("="), NOT_EQUAL("!="), GREATER_THAN(">"), LESS_THAN("<"), LESS_THAN_OR_EQUAL("<="), GREATER_THAN_OR_EQUAL(">=");
		
		private String symbol;
		
		private Comparator(String symbol){
			this.symbol = symbol;
		}
		
		public String getSymbol(){
			return symbol;
		}
	}
	
	private List<Object[]> statementInstructions = new ArrayList<Object[]>();
	
	public DbConnectionStatement(TableField field, Comparator comparator, Object value) throws SQLException{
		List<Object> instruction = new ArrayList<Object>();
		instruction.add(" WHERE ");
		instruction.add(field);
		instruction.add(comparator);
		instruction.add(value);
		statementInstructions.add(instruction.toArray());
	}
	
	void addInclusionCondition(TableField field, Comparator comparator, Object value) throws SQLException{
		List<Object> instruction = new ArrayList<Object>();
		instruction.add(" AND ");
		instruction.add(field);
		instruction.add(comparator);
		instruction.add(value);
		statementInstructions.add(instruction.toArray());
	}
	
	void addAdditionalCondition(TableField field, Comparator comparator, Object value) throws SQLException{
		List<Object> instruction = new ArrayList<Object>();
		instruction.add(" OR ");
		instruction.add(field);
		instruction.add(comparator);
		instruction.add(value);
		statementInstructions.add(instruction.toArray());
	}
	
	Object[][] getStatementInstructions(){
		return statementInstructions.toArray(new Object[statementInstructions.size()][4]);
	}
}
/*	private void addCondition(TableField field, Object value) throws SQLException {
		statement.append(field.toString().toLowerCase() + "=");
		switch(field.getType()){
			case BOOLEAN:
				if(value instanceof Boolean){
					statement.append("'" + value + "'");
					return;
				}
			case BYTE:
				if(value instanceof Byte){
					statement.append(value);
					return;
				}
			case DATE:
				if(value instanceof java.sql.Date){
					statement.append("'" + value + "'");
					return;
				}
			case INT:
				if(value instanceof Integer){
					statement.append(value);
					return;
				}
			case SHORT:
				if(value instanceof Short){
					statement.append(value);
					return;
				}
			case STRING:
				if(value instanceof java.lang.String){
					statement.append("'" + value + "'");
					return;
				}
			default:
				break;
		}

		throw new IllegalSQLValueException(field, value);
	}
}

class IllegalSQLValueException extends SQLException{
	private static final long serialVersionUID = 6097587662705340327L;

	IllegalSQLValueException(TableField field, Object value){
		super(value + " is not a valid argument for field: " + field + " (" + field.getType() + ")");
	}
}*/