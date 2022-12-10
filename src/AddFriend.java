

import java.sql.*;

public class AddFriend { // 대화창에서 친구 추가하기 위한 클래스

	static public void addFriend(String client_id, String friend_id) {

		Connection conn;
		String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // network 스키마
		String user = "root"; // 데이터베이스 아이디
		String passwd = "1234"; // 데이터베이스 비번
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("---------------------");
			System.out.println("친구 추가하기 DB접속 성공");
			String sql = "INSERT INTO client_friend_list (client_id,friend_id) SELECT ?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM client_friend_list WHERE client_id=? and friend_id=?)";
			// 중복된 입력을 막기위한 쿼리입니다.
			// String sql="insert into client_friend_list values(?,?)"; //물음표에는 동적으로 변화하는 값을
			// 넣기 위함
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement
																			// 클래스

			pstmt.setString(1, client_id); // 동적으로 변화하는 값을 전달 만약 전달하는 값이 정수이면 setInt(index,정수) 이런 식으로 하면됨.
			pstmt.setString(2, friend_id);
			pstmt.setString(3, client_id);
			pstmt.setString(4, friend_id);
			pstmt.executeUpdate();

			System.out.println("---------------------");

			conn.close();
		} catch (Exception e) {
			System.out.println("DB접속 오류 " + e);
		}
	}
}
