package zuna.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {
	private static Connection conn;


	public static Connection getConn() {
		return conn;
	}

	public static void getConnection(DBInfo dbInfo){
		
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn= DriverManager.getConnection(dbInfo.getUrl(),dbInfo.getId(),dbInfo.getPassword());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void closeConn(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}