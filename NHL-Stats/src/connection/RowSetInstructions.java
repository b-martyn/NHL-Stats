package connection;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.Predicate;

import connection.DbConnector.Table;

public class RowSetInstructions {
	
	public enum Comparator{
		GREATER_THEN(">"), GREATER_THEN_EQUAL(">="), LESS_THEN("<"), LESS_THEN_EQUAL("<="), EQUAL("="), NOT_EQUAL("<>");
		
		private String symbol;
		
		private Comparator(String symbol){
			this.symbol = symbol;
		}
		
		String getSymbol(){
			return symbol;
		}
	}
	
	private JoiningResultSet[] joiningResultSets = new JoiningResultSet[0];
	private Filter[] filters = new Filter[0];
	private Table mainTable;
	
	public RowSetInstructions(Table table){
		this.mainTable = table;
	}
	
	public boolean tableExists(Table table){
		if(table.equals(mainTable)){
			return true;
		}
		if(joiningResultSets != null){
			for(JoiningResultSet joining : joiningResultSets){
				if(!joining.isJoiningResultSet()){
					if(joining.getJoining().equals(table)){
						return true;
					}
				}else{
					CachedRowSet joiningRowSet = (CachedRowSet)joining.getJoining();
					try {
						String[] tables = joiningRowSet.getTableName().split(",");
						for(String rowSetTable : tables){
							if(rowSetTable.equalsIgnoreCase(table.toString())){
								return true;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}
	
	public JoiningResultSet[] getJoiningResultSets(){
		return joiningResultSets;
	}
	
	public void addJoiningResultSet(JoiningResultSet joiningResultSet){
		List<JoiningResultSet> newJoiningResultSetList = new ArrayList<JoiningResultSet>();
		for(JoiningResultSet joiningSet : joiningResultSets){
			newJoiningResultSetList.add(joiningSet);
		}
		newJoiningResultSetList.add(joiningResultSet);
		joiningResultSets = newJoiningResultSetList.toArray(new JoiningResultSet[newJoiningResultSetList.size()]);
	}
	
	public Filter[] getFilters(){
		return filters;
	}
	
	public void addFilter(Filter filter){
		List<Filter> newFiltersList = new ArrayList<Filter>();
		for(Filter f : filters){
			newFiltersList.add(f);
		}
		newFiltersList.add(filter);
		filters = newFiltersList.toArray(new Filter[newFiltersList.size()]);
	}
	
	public Table getMainTable(){
		return mainTable;
	}
	
	public Predicate getMyPredicate(){
		return new MyPredicate(filters);
	}
	
	public class JoiningResultSet{
		/*--------------------------------------------------------
		  JoiningResultSet first joins all tables then resultSets
		 ---------------------------------------------------------*/


		private Object joining;
		private String matchingField;
		private String resultSetMatchingField;
		private boolean joinAfter;
		
		public JoiningResultSet(Table table, TableField matchingField, TableField resultSetMatchingField, boolean joinAfter){
			if(tableExists(resultSetMatchingField.getTable())){
				this.joining = table;
				this.matchingField = matchingField.toString().toLowerCase();
				this.resultSetMatchingField = resultSetMatchingField.toString().toLowerCase();
				this.joinAfter = joinAfter;
			}
		}
		
		public JoiningResultSet(Table table, TableField matchingField, TableField resultSetMatchingField){
			this(table, matchingField, resultSetMatchingField, true);
		}
		
		public JoiningResultSet(ResultSet resultSet, TableField matchingField, TableField resultSetMatchingField, boolean joinAfter){
			if(tableExists(resultSetMatchingField.getTable())){
				this.joining = resultSet;
				this.matchingField = matchingField.toString().toLowerCase();
				this.resultSetMatchingField = resultSetMatchingField.toString().toLowerCase();
				this.joinAfter = joinAfter;
			}
		}
		
		public JoiningResultSet(ResultSet resultSet, TableField resultSetMatchingField, boolean joinAfter){
			this(resultSet, resultSetMatchingField, resultSetMatchingField, joinAfter);
		}
		
		public JoiningResultSet(ResultSet resultSet, TableField resultSetMatchingField){
			this(resultSet, resultSetMatchingField, resultSetMatchingField, true);
		}
		
		public boolean isJoiningResultSet(){
			if(joining instanceof ResultSet){
				return true;
			}
			return false;
		}
		
		public Object getJoining(){
			return joining;
		}
		
		public String getTableMatchingField(){
			return matchingField;
		}
		
		public String getResultSetMatchField(){
			return resultSetMatchingField;
		}
		
		public boolean isJoinAfter(){
			return joinAfter;
		}
	}
	
	public class Filter{
		private TableField[] matchingFields = new TableField[0];
		private Comparator[] comparators = new Comparator[0];
		private Object[] values = new Object[0];
		private Boolean[] matchNext = new Boolean[0];
		
		public Filter(TableField field, Comparator comparator, Object value){
			filterAdditionField(field, comparator, value, false);
		}
		
		public void filterAdditionField(TableField field, Comparator comparator, Object value, boolean matchWithPreviousFilter){
			if(value instanceof TableField){
				TableField valueField = (TableField)value;
				if(!tableExists(valueField.getTable())){
					throw new IllegalArgumentException("Missing table:" + valueField.getTable() + " for " + valueField);
				}else if(!tableExists(field.getTable())){
					throw new IllegalArgumentException("Missing table:" + field.getTable() + " for " + field);
				}
			}else{
				if(tableExists(field.getTable())){
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
			TableField[] newMatchingFieldsList = new TableField[matchingFields.length + 1];
			for(int i = 0; i < matchingFields.length; i++){
				newMatchingFieldsList[i] = matchingFields[i];
			}
			Comparator[] newComparatorsList = new Comparator[comparators.length + 1];
			for(int i = 0; i < comparators.length; i++){
				newComparatorsList[i] = comparators[i];
			}
			Object[] newValuesList = new Object[values.length + 1];
			for(int i = 0; i < values.length; i++){
				newValuesList[i] = values[i];
			}
			Boolean[] newMatchNextList = new Boolean[matchNext.length + 1];
			for(int i = 0; i < matchNext.length; i++){
				newMatchNextList[i] = matchNext[i];
			}
			newMatchingFieldsList[(newMatchingFieldsList.length) - 1] = field;
			newComparatorsList[newComparatorsList.length - 1] = comparator;
			newValuesList[newValuesList.length - 1] = value;
			if(newMatchNextList.length > 1){
				newMatchNextList[newMatchNextList.length - 2] = matchWithPreviousFilter;
			}
			newMatchNextList[newMatchNextList.length - 1] = false;
			
			matchingFields = newMatchingFieldsList;
			comparators = newComparatorsList;
			values = newValuesList;
			matchNext = newMatchNextList;
		}
		
		public TableField[] getMatchingFields(){
			return matchingFields;
		}
		
		public Comparator[] getComparators(){
			return comparators;
		}
		
		public Object[] getValues(){
			return values;
		}
		
		public Boolean[] getMatchNext(){
			return matchNext;
		}
	}
	
	private class MyPredicate implements Predicate{
		
		private TableField[] matchingFields;
		private Comparator[] comparators;
		private Object[] values;
		private Boolean[] matchNext;
		private Boolean[] grouping;
		private List<Boolean> initialResults;
		private int index = 0;
		
		private MyPredicate(Filter[] filters){
			List<TableField> matchingFieldsList = new ArrayList<TableField>();
			List<Comparator> comparatorsList = new ArrayList<Comparator>();
			List<Object> valuesList = new ArrayList<Object>();
			List<Boolean> matchNextList = new ArrayList<Boolean>();
			List<Boolean> groupingList = new ArrayList<Boolean>();
			for(Filter filter : filters){
				for(TableField tableField : filter.getMatchingFields()){
					matchingFieldsList.add(tableField);
				}
				for(Comparator comparator : filter.getComparators()){
					comparatorsList.add(comparator);
				}
				for(Object value : filter.getValues()){
					valuesList.add(value);
				}
				for(int i = 0; i < filter.getMatchNext().length; i++){
					matchNextList.add(filter.getMatchNext()[i]);
					if(i == (filter.getMatchNext().length - 1)){
						groupingList.add(false);
					}else{
						groupingList.add(true);
					}
				}
			}
			matchingFields = matchingFieldsList.toArray(new TableField[matchingFieldsList.size()]);
			comparators = comparatorsList.toArray(new Comparator[comparatorsList.size()]);
			values = valuesList.toArray();
			matchNext = matchNextList.toArray(new Boolean[matchNextList.size()]);
			grouping = groupingList.toArray(new Boolean[groupingList.size()]);
		}
		
		@Override
		public boolean evaluate(RowSet rs) {
			if(matchingFields.length < 1){
				return true;
			}
			
			List<Boolean> finalResults = new ArrayList<Boolean>();
			initialResults = new ArrayList<Boolean>();
			for(int i = 0; i < matchingFields.length; i++){
				TableField field = matchingFields[i];
				Comparator comparator = comparators[i];
				Object value = values[i];
				initialResults.add(compareField(rs, field, comparator, value));
			}
			for(index = 0; index < grouping.length; index++){
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
				while(grouping[index]){
					boolean result = finalResult;
					if(matchNext[index]){
						++index;
						result = (result && groupCriteria());
					}else{
						++index;
						result = (result || groupCriteria());
					}
				}
			}catch(IndexOutOfBoundsException e){
				// TODO look me over
				System.out.println(grouping.length);
			}
			return finalResult;
		}
		
		private boolean compareField(RowSet rs, TableField field, Comparator comparator, Object value){
			try {
				switch(field.getType()){
					case BOOLEAN:
						boolean bool = rs.getBoolean(field.toString().toLowerCase());
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(bool == rs.getBoolean(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(bool != rs.getBoolean(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}	
						}else{
							switch(comparator){
								case EQUAL:
									if(bool == (boolean) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(bool != (boolean) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case DATE:
						Date date = rs.getDate(field.toString().toLowerCase());
						if(date == null){
							date = new Date(new java.util.Date().getTime());
						}
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(date.equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case GREATER_THEN:
									if(date.after(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(date.after(rs.getDate(((TableField)value).toString().toLowerCase())) || date.equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case LESS_THEN:
									if(date.before(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(date.before(rs.getDate(((TableField)value).toString().toLowerCase())) || date.equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!date.equals(rs.getDate(((TableField)value).toString().toLowerCase()))){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(date.equals((Date)value)){
										return true;
									}
									break;
								case GREATER_THEN:
									if(date.after((Date)value)){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(date.after((Date)value) || date.equals((Date)value)){
										return true;
									}
									break;
								case LESS_THEN:
									if(date.before((Date)value)){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(date.before((Date)value) || date.equals((Date)value)){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(!date.equals((Date)value)){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case BYTE:
						byte byteValue = rs.getByte(field.toString().toLowerCase());
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(byteValue == rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(byteValue > rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(byteValue >= rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(byteValue < rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(byteValue <= rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(byteValue != rs.getByte(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(byteValue == (byte) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(byteValue > (byte) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(byteValue >= (byte) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(byteValue < (byte) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(byteValue <= (byte) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(byteValue != (byte) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case SHORT:
						short shortValue = rs.getShort(field.toString().toLowerCase());
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(shortValue == rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(shortValue > rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(shortValue >= rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(shortValue < rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(shortValue <= rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(shortValue != rs.getShort(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(shortValue == (short) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(shortValue > (short) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(shortValue >= (short) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(shortValue < (short) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(shortValue <= (short) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(shortValue != (short) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case INT:
						int intValue = rs.getInt(field.toString().toLowerCase());
						if(value instanceof TableField){
							switch(comparator){
								case EQUAL:
									if(intValue == rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN:
									if(intValue > rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(intValue >= rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN:
									if(intValue < rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(intValue <= rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(intValue != rs.getInt(((TableField)value).toString().toLowerCase())){
										return true;
									}
									break;
								default:
									break;
							}
						}else{
							switch(comparator){
								case EQUAL:
									if(intValue == (int) value){
										return true;
									}
									break;
								case GREATER_THEN:
									if(intValue > (int) value){
										return true;
									}
									break;
								case GREATER_THEN_EQUAL:
									if(intValue >= (int) value){
										return true;
									}
									break;
								case LESS_THEN:
									if(intValue < (int) value){
										return true;
									}
									break;
								case LESS_THEN_EQUAL:
									if(intValue <= (int) value){
										return true;
									}
									break;
								case NOT_EQUAL:
									if(intValue != (int) value){
										return true;
									}
									break;
								default:
									break;
							}
						}
						break;
					case STRING:
						String string = rs.getString(field.toString().toLowerCase());
						if(string != null){
							if(value instanceof TableField){
								switch(comparator){
									case EQUAL:
										if(string.equals(rs.getString(((TableField)value).toString().toLowerCase()))){
											return true;
										}
										break;
									case NOT_EQUAL:
										if(!string.equals(rs.getString(((TableField)value).toString().toLowerCase()))){
											return true;
										}
										break;
									default:
										break;
								}
							}else{
								switch(comparator){
									case EQUAL:
										if(string.equals((String) value)){
											return true;
										}
										break;
									case NOT_EQUAL:
										if(!string.equals((String) value)){
											return true;
										}
										break;
									default:
										break;
								}
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
