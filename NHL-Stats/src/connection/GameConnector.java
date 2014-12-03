package connection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import connection.Franchise.TeamName;

interface GameConnector {
	
	public enum Season{
		_2003_2004("2003-10-08 2004-04-04"), _2004_2005(""), _2005_2006("2005-10-05 2006-04-18"), _2006_2007("2006-10-04 2007-04-08"), 
		_2007_2008("2007-09-29 2008-04-06"), _2008_2009("2008-10-04 2009-04-12"), _2009_2010("2009-10-01 2010-04-11"), _2010_2011("2010-10-07 2011-04-10"), 
		_2011_2012("2011-10-06 2012-04-07"), _2012_2013("2013-01-19 2013-04-28"), _2013_2014("2013-10-01 2014-04-13"), _2014_2015("2014-10-08 2015-04-11");
	
		private Date startDate;
		private Date endDate;
		
		private Season(String dateString) {
			try {
				String[] dates = dateString.split(" ");
				this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(dates[0]);
				this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(dates[1]);
			} catch (ParseException e) {
				// Do nothing (Season _2004_2005 was a lockout-season and no dates so can't parse date
				//e.printStackTrace();
			}
		}
		
		public Date getStartDate(){
			return startDate;
		}
		
		public Date getEndDate(){
			return endDate;
		}
	}
	
	Game[] getGames() throws SQLException;
	Game[] getGames(TeamName teamName) throws SQLException;
	Game[] getGames(Season season) throws SQLException;
	Game getGame(int id) throws SQLException;
	Game getGame(Date date, TeamName teamName) throws SQLException;
}
