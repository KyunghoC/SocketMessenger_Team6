import java.sql.Connection;
import java.sql.DriverManager;

public class Login {
	// Tab에서 로그인 할 경우 ID와 Password를 확인과 중복로그인 여부 확인

	public static int client_login(String client_id, String client_password) {
		// TODO Auto-generated method stub
		Connection conn;
		String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // network 스키마
		String user = "root"; // 데이터베이스 아이디
		String passwd = "12345"; // 데이터베이스 비번
		final int WRONGPASSWORD = 1; // 잘못된 비밀번호일 경우
		final int OVERLAPLOGIN = 0; // 중복 로그인 될 경우
		final int SUCCESSLOGIN = 2; // 성공적인 로그인 경우
		int LogCheck = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("---------------------");
			System.out.println("친구 찾기 DB접속 성공");

			String sql = "select log from login_check where client_id=?"; // 물음표에는 동적으로 변화하는 값을 넣기 위함
			java.sql.PreparedStatement pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement
																			// 클래스
			pstmt.setString(1, client_id);
			java.sql.ResultSet result = pstmt.executeQuery();
			String logCh = null;
			while (result.next()) {
				logCh = result.getString("log"); // 로그인 체크 여부 확인 //즉 중복 로그인 검사
			}

			if (logCh.equals("logout")) {
				sql = "select * from client_list where client_id=? and client_password=?"; // 물음표에는 동적으로 변화하는 값을 넣기 위함
				pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement 클래스
				pstmt.setString(1, client_id); // 동적으로 변화하는 값을 전달 만약 전달하는 값이 정수이면 setInt(index,정수) 이런 식으로 하면됨.
				pstmt.setString(2, client_password);
				result = pstmt.executeQuery();

				String data[] = new String[4];
				String line = "";

				// result.beforeFirst();
				while (result.next()) {
					data[0] = result.getString("client_id");

					data[1] = result.getString("client_name");

					data[2] = result.getString("client_email");

					data[3] = result.getString("client_phone");

					if (data[0].equals(client_id)) // ID와 검색한 ID가 동일할때

					{
						LogCheck = SUCCESSLOGIN;
						break;
					}
				}
				// 로그인시 로그인체크를 업데이트 시킴.
				if (LogCheck == SUCCESSLOGIN) {
					sql = "update login_check set log='login' where client_id=?;";
					pstmt = conn.prepareStatement(sql); // 매 검색시 변화하는 값을 검색하기 위한 PreparedStatement 클래스

					pstmt.setString(1, client_id); // 동적으로 변화하는 값을 전달 만약 전달하는 값이 정수이면 setInt(index,정수) 이런 식으로 하면됨.
					pstmt.executeUpdate();
				} else {
					LogCheck = WRONGPASSWORD; // 비밀번호를 잘못 입력한 경우.

				}
			} // 중복검사 체크
			System.out.println("---------------------");
			conn.close(); // 연결 끊기
		} catch (Exception e) {
			System.out.println("DB접속 오류 " + e);
		}

		return LogCheck;
	}

}
