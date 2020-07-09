package maze;

import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class LogManager {
	private String driver;
	private String url;
	private String userid;
	private String password;

	Connection conn;
	Statement stmt;
	ResultSet rs;
	PreparedStatement pstmt;

	public LogManager() {
		this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		this.url = "jdbc:sqlserver://203.234.62.144:1433; databaseName = Maze";
		this.userid = "sonyo";
		this.password = "1234";
	}

	public boolean connectDB() {
		this.conn = null;
		try {
			Class.forName(this.driver);
			this.conn = DriverManager.getConnection(this.url, this.userid, this.password);
			this.stmt = this.conn.createStatement();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public boolean disconnectDB() {
		try {
			if (this.stmt != null)
				this.stmt.close();
			if (this.pstmt != null)
				this.pstmt.close();
			if (this.conn != null)
				this.conn.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public int getCount(String mouseName) {
		int count = 0;

		// �����ͺ��̽� ����
		connectDB();

		try {

			String sql = "select count from Log where mouse_name ='" + mouseName + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("count");
			}
//			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// �����ͺ��̽� ���� ����
		disconnectDB();
		return count;
	}

	public ArrayList<String> getRankingList() {
		ArrayList<String> rankList = new ArrayList<String>();

		// �����ͺ��̽� ����
		connectDB();

		// ���̺��� ������ ȹ��

		try {
			String sql = "select * from Log order by count asc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String Mouse = rs.getString("mouse_name");
				String timestamp = rs.getString("timestamp");
				int count = rs.getInt("count");
				rankList.add(id + "," + Mouse + "," + timestamp + "," + count);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} // �����ͺ��̽� ���� ����
		System.out.println(rankList);
		disconnectDB();
		return rankList;
	}

	public boolean putLog(String mouseName, int count) {
		boolean result = true;
		
		// �����ͺ��̽��� ����
		connectDB();

		// �ý��ۿ��� �ð��� �޾� datetime �������� �����.
		// ���̺��� �Ķ������ ���� �ִ´�
		try {
			SimpleDateFormat current_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time_stamp = current_time.format(System.currentTimeMillis());
			String sql = "insert into Log(mouse_name, timestamp, count) values ('" + mouseName + "','" + time_stamp
					+ "', " + count + ")";
//			stmt.executeLargeUpdate(sql);
			stmt.executeUpdate(sql);

			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		
		// �����ͺ��̽� ���� ����
		disconnectDB();
		return result;
	}

	public int getMinCount(String mouseName) {
		int min_count = -1;

		// �����ͺ��̽� ����
		connectDB();

		try {

			String sql = "select count as min_count from Log "
					+ "where mouse_name='" + mouseName + "';";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				min_count = rs.getInt("min_count");
			}
			System.out.println(min_count);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// �����ͺ��̽� ���� ����
		disconnectDB();
		return min_count;
	}

//	public static void main(String[] args) {
//		LogManager Log = new LogManager();
//		Log.putLog("woolin", 55);
//		Log.getMinCount("Sunyoung_mouse");
//	}

}