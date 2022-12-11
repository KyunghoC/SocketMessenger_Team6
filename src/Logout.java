import java.sql.*;

public class Logout {
	// 사용자가 정상적이거나 비 정상적으로 로그아웃을 했을 경우 DB에 로그아웃을 알리기 위한 클래스
	/**
	 * @param args
	 */
	public static void client_logout(String client_id) {
		// TODO Auto-generated method stub
		Connection conn;
		String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // network 스키마
		String user = "root"; // 데이터베이스 아이디
		String passwd = "12345"; // 데이터베이스 비번
		boolean LogCheck = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("---------------------");
			System.out.println("로그아웃 DB 접근 성공");

			String sql;
			sql = "update login_check set log='logout' where client_id=?;";
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement
																			// 클래스

			// 로그아웃시 로그인체크를 업데이트 시킴.
			pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement 클래스
			pstmt.setString(1, client_id); // 동적으로 변화하는 값을 전달 만약 전달하는 값이 정수이면 setInt(index,정수) 이런 식으로 하면됨.
			pstmt.executeUpdate();

			System.out.println("---------------------");
			conn.close(); // 연결 끊기
		} catch (Exception e) {
			System.out.println("DB  접속 오류" + e);
		}

	}

}
