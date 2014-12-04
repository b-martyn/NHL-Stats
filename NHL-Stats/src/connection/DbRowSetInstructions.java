package connection;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

import connection.DbConnection.Table;

public class DbRowSetInstructions {
	
	public enum Comparator{
		// Not using BETWEEN("BETWEEN"), LIKE("LIKE") right now
		GREATER_THEN(">"), GREATER_THEN_EQUAL(">="), LESS_THEN("<"), LESS_THEN_EQUAL("<="), EQUAL("="), NOT_EQUAL("<>");
		
		private String symbol;
		
		private Comparator(String symbol){
			this.symbol = symbol;
		}
		
		String getSymbol(){
			return symbol;
		}
	}
	
	private Table mainTable;
	private List<Object[]> joiningTables = new ArrayList<Object[]>();
	/* 
	 * joiningTables = Object[0] = Table
	 * 				   Object[1] = TableField
	 * 				   Object[2] = TableField
	 */
	private List<Object[]> filterCriteria = new ArrayList<Object[]>();
	/*
	 * filterCriteria = Object[0] = TableField
	 * 					Object[1] = Comparator
	 * 					Object[2] = TableField/value of field
	 * 					Object[3] = boolean (true = 'AND' this criteria with the next| false = 'OR' this criteria with the next)
	 * 					Object[4] = boolean (true = grouped with next criteria)
	 */
		
	DbRowSetInstructions(Table table){
		this.mainTable = table;
	}
	
	void addJoiningTable(Table joiningTable, TableField matchColumn, TableField tableMatchingColumn){
		
		if (checkForExistingTable(tableMatchingColumn.getTable())){
			List<Object> joiningTableArray = new ArrayList<Object>();
			joiningTableArray.add(joiningTable);
			joiningTableArray.add(matchColumn);
			joiningTableArray.add(tableMatchingColumn);
			joiningTables.add(joiningTableArray.toArray());
			return;
		}else{
			throw new IllegalArgumentException("Missing table:" + tableMatchingColumn.getTable() + " for " + tableMatchingColumn);
		}
	}
	
	boolean checkForExistingTable(Table table){
		Table[] tables = new Table[joiningTables.size() + 1];
		tables[0] = mainTable;
		for(int i = 0; i < joiningTables.size(); i++){
			tables[i + 1] = (Table)joiningTables.get(i)[0];
		}
		for(Table t : tables){
			if (t.equals(table)){
				return true;
			}
		}
		return false;
	}
	
	Object[][] getJoiningTables(){
		return joiningTables.toArray(new Object[joiningTables.size()][3]);
	}
	
	void addNewFilterCriteria(TableField field, Comparator comparator, Object value){
		addFilterCriteria(field, comparator, value, false, false);
	}
	
	void addNewConditionCriteria(TableField field, Comparator comparator, Object value, boolean andCriteria){
		Object[] previousCriteria = filterCriteria.get(filterCriteria.size() - 1);
		previousCriteria[3] = andCriteria;
		previousCriteria[4] = true;
		addFilterCriteria(field, comparator, value, false, false);
	}
	
	private void addFilterCriteria(TableField field, Comparator comparator, Object value, boolean andCriteria, boolean grouping){
		
		if(value instanceof TableField){
			TableField valueField = (TableField)value;
			if(checkForExistingTable(valueField.getTable())){
				return;
			}else{
				throw new IllegalArgumentException("Missing table:" + valueField.getTable() + " for " + valueField);
			}
		}else{
			if(checkForExistingTable(field.getTable())){
				switch(field.getType()){
					case BOOLEAN:
						if(!(value instanceof Boolean)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}else if(!(comparator == Comparator.EQUAL || comparator == Comparator.NOT_EQUAL)){
							throw new IllegalArgumentException("Incorrect comparator: " + comparator +  "for Boolean field");
						}
						break;
					case DATE:
						if(!(value instanceof Date)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}
						break;
					case STRING:
						if(!(value instanceof java.lang.String)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}else if(!(comparator == Comparator.EQUAL || comparator == Comparator.NOT_EQUAL)){
							throw new IllegalArgumentException("Incorrect comparator: " + comparator +  " for String field");
						}
						break;
					case BYTE:
						if(!(value instanceof Byte)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}
						break;
					case SHORT:
						if(!(value instanceof Short)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}
						break;
					case INT:
						if(!(value instanceof Integer)){
							throw new IllegalArgumentException("Incorrect field type for:" + field + "(" + field.getType() + ")" + " with value:" + value + " as " + value.getClass());
						}
						break;
					default:
						break;
				}
			}else{
				throw new IllegalArgumentException("Missing table:" + field.getTable() + " for " + field);
			}
		}
		List<Object> criteria = new ArrayList<Object>();
		criteria.add(field);
		criteria.add(comparator);
		criteria.add(value);
		criteria.add(andCriteria);
		criteria.add(grouping);
		filterCriteria.add(criteria.toArray());
	}
	
	Table getMainTable(){
		return mainTable;
	}
	
	Predicate getMyPredicate(){
		return new MyPredicate(filterCriteria);		
	}
	
	private class MyPredicate implements Predicate{
		
		private List<TableField> fields = new ArrayList<TableField>();
		private List<Comparator> comparators = new ArrayList<Comparator>();
		private List<Object> values = new ArrayList<Object>();
		private List<Boolean> andCriteria = new ArrayList<Boolean>();
		private List<Boolean> grouping = new ArrayList<Boolean>();
		private List<Boolean> initialResults;
		private int index = 0;
		
		MyPredicate(List<Object[]> criteria){
			for(Object[] instruction : criteria){
				fields.add((TableField)instruction[0]);
				comparators.add((Comparator)instruction[1]);
				values.add(instruction[2]);
				andCriteria.add((boolean)instruction[3]);
				grouping.add((boolean)instruction[4]);
			}
		}
		
		@Override
		public boolean evaluate(RowSet rs) {
			if(fields.size() < 1){
				return true;
			}
			
			List<Boolean> finalResults = new ArrayList<Boolean>();
			initialResults = new ArrayList<Boolean>();
			for(int i = 0; i < fields.size(); i++){
				TableField field = fields.get(i);
				Comparator comparator = comparators.get(i);
				Object value = values.get(i);
				initialResults.add(compareField(rs, field, comparator, value));
			}
			for(index = 0; index < grouping.size(); index++){
				finalResults.add(groupCriteria());
			}
			for(boolean match : finalResults){
				if(match){
					return true;
				}
			}
			return false;
		}
		
		private boolean groupCriteria(){
			boolean finalResult = initialResults.get(index);
			try{
				while(grouping.get(index)){
					boolean result = finalResult;
					if(andCriteria.get(index)){
						++index;
						result = (result && groupCriteria());
					}else{
						++index;
						result = (result || groupCriteria());
					}
				}
			}catch(IndexOutOfBoundsException e){
				// TODO look me over
				System.out.println(grouping.size());
			}
			return finalResult;
		}
		
		private boolean compareField(RowSet rs, TableField field, Comparator comparator, Object value){
			try {
				switch(field.getType()){
					case BOOLEAN:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getBoolean(field.toString().toLowerCase()) == rs.getBoolean(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getBoolean(field.toString().toLowerCase()) != rs.getBoolean(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}	
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getBoolean(field.toString().toLowerCase()) == (boolean) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getBoolean(field.toString().toLowerCase()) != (boolean) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case DATE:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getDate(field.toString().toLowerCase()).after(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).after(rs.getDate(((TableField)value).toString().toLowerCase())) || rs.getDate(field.toString().toLowerCase()).equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getDate(field.toString().toLowerCase()).before(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).before(rs.getDate(((TableField)value).toString().toLowerCase())) || rs.getDate(field.toString().toLowerCase()).equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!rs.getDate(field.toString().toLowerCase()).equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).equals((Date)value)){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getDate(field.toString().toLowerCase()).after((Date)value)){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).after((Date)value) || rs.getDate(field.toString().toLowerCase()).equals((Date)value)){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getDate(field.toString().toLowerCase()).before((Date)value)){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getDate(field.toString().toLowerCase()).before((Date)value) || rs.getDate(field.toString().toLowerCase()).equals((Date)value)){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!rs.getDate(field.toString().toLowerCase()).equals((Date)value)){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case BYTE:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) == rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getByte(field.toString().toLowerCase()) > rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) >= rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getByte(field.toString().toLowerCase()) < rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) <= rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) != rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) == (byte) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getByte(field.toString().toLowerCase()) > (byte) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) >= (byte) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getByte(field.toString().toLowerCase()) < (byte) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) <= (byte) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getByte(field.toString().toLowerCase()) != (byte) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case SHORT:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) == rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getShort(field.toString().toLowerCase()) > rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) >= rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getShort(field.toString().toLowerCase()) < rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) <= rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) != rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) == (short) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getShort(field.toString().toLowerCase()) > (short) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) >= (short) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getShort(field.toString().toLowerCase()) < (short) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) <= (short) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getShort(field.toString().toLowerCase()) != (short) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case INT:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) == rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getInt(field.toString().toLowerCase()) > rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) >= rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getInt(field.toString().toLowerCase()) < rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) <= rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) != rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) == (int) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(rs.getInt(field.toString().toLowerCase()) > (int) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) >= (int) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(rs.getInt(field.toString().toLowerCase()) < (int) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) <= (int) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(rs.getInt(field.toString().toLowerCase()) != (int) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case STRING:
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(rs.getString(field.toString().toLowerCase()).equals(rs.getString(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!rs.getString(field.toString().toLowerCase()).equals(rs.getString(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(rs.getString(field.toString().toLowerCase()).equals((String) value)){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!rs.getString(field.toString().toLowerCase()).equals((String) value)){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					default:
						break;
				}
			} catch (SQLException e) {
				return false;
			}
			return false;
		}

		@Override
		public boolean evaluate(Object value, int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean evaluate(Object value, String columnName)
				throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
