

import java.sql.*;

public class getFriendList { // 대화창에서 친구목록을 불러오는 클래스

	/**
	 * @param args
	 */
	public static String[] getSearch(String client_id) {
		// TODO Auto-generated method stub
		Connection conn;
		String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // network 스키마
		String user = "root"; // 데이터베이스 아이디
		String passwd = "1234"; // 데이터베이스 비번
		String[] frList = new String[20];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("---------------------");
			System.out.println("친구 찾기 DB접속 성고");

			String sql = "select client_friend_list.client_id,client_friend_list.friend_id from client_friend_list  join login_check on(client_friend_list.friend_id = login_check.client_id) where client_friend_list.client_id=? and login_check.log='login';";
			// 물음표에는 동적으로 변화하는 값을 넣기 위함
			// 로그인된 친구만 리스트에서 나옴
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement
																			// 클래스
			pstmt.setString(1, client_id); // 동적으로 변화하는 값을 전달 만약 전달하는 값이 정수이면 setInt(index,정수) 이런 식으로 하면됨.
			java.sql.ResultSet result = pstmt.executeQuery();

			String data[] = new String[2];
			String line = "";

			int i = 0;
			// result.beforeFirst();
			while (result.next()) {
				data[0] = result.getString("client_id");

				data[1] = result.getString("friend_id");

				frList[i] = data[1];
				i++;

			}
			System.out.println("---------------------");

			conn.close(); // 연결 끊기
		} catch (Exception e) {
			System.out.println("DB접속 오류 " + e);
		}

		return frList; // 친구목록전송

	}

}
