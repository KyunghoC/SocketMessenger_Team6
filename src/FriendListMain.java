import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;

public class FriendListMain extends JFrame {

	static String[] Friend_ID = new String[100]; // 최대 등록 가능 수
	static String Login_ID = New_Client.getClientName();
	static JList FriendList;

	static JPanel getFriendListMain() {

		// super("친구목록");
		Login_ID = New_Client.getClientName();
		final JPopupMenu FFindpopUp = new JPopupMenu();
		FriendList = new JList();

		JButton myInfo = new JButton("상세정보");
		JButton Logout = new JButton("로그아웃");
		myInfo.setBackground(new Color(230, 110, 100));
		Logout.setBackground(new Color(230, 110, 100));
		Panel Buttom = new Panel();
		Buttom.add(myInfo);
		Buttom.add(Logout);
		// Logout.add(new JLabel(new ImageIcon(getClass().getResource("1.jpg")))); // 로고
		// 삽입

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout()); //
		panel1.add("South", Buttom); //
		panel1.add("Center", FriendList); //

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("친구 목록", null, panel1, "second Panel");

		int f_count = 0;
		Connection con = null;
		/*
		 * try {
		 * 
		 * con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TingTalk",
		 * "root", "dnfxmfk8"); PreparedStatement ps = null; ResultSet rs = null; String
		 * sql = "select * from client_friend_list where client_id=?"; // String sql =
		 * "select client_friend_list.friend_id, client_name, client_email, client_phone  from client_friend_list join client_list on(client_friend_list.friend_id = client_list.client_id) where client_friend_list.client_id like ?"
		 * ; ps = con.prepareStatement(sql); ps.setString(1, Login_ID); // 자신의 아이디. rs =
		 * ps.executeQuery();
		 * 
		 * while (rs.next()) { String str = rs.getString("friend_id"); // 출력할 정보(친구의
		 * ID(또는 이름)) System.out.println(str);
		 * 
		 * Friend_ID[f_count] = str; f_count++; } }
		 * 
		 * catch (SQLException sqex) {
		 * 
		 * System.out.println("SQLException: " + sqex.getMessage());
		 * System.out.println("SQLState: " + sqex.getSQLState()); }
		 */
		// FriendList.setListData(Friend_ID);

		ActionListener LogoutAction = new ActionListener() { // 로그아웃 버튼 이벤트

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Map d = New_Client.getMap(); // 현재 회원이 들어가있는 방에대한 정보를 얻어옴
				Collection<client_2> client_2_values = d.values(); // 방에 대한 처리를 하기위해 값을 전달
				for (Iterator<client_2> it = client_2_values.iterator(); it.hasNext();) {
					it.next().exit_bt.doClick(); // 해당 방의 나가기 버튼을눌러줌.
				}

				System.exit(1);

			}

		};
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				Connection con1 = null;

				if (actionEvent.getActionCommand() == "대화하기") {

					String inviteFriendID = (String) FriendList.getSelectedValue();
					if (!(inviteFriendID == null)) { // 초대할 사람이 공백이이 않을 경우 초대
						New_Client.pw.println("52274#" + inviteFriendID); // 대화요청
						System.out.println("대화하기");
					}
				}

				else if (actionEvent.getActionCommand() == "친구정보") {

					int num = FriendList.getSelectedIndex();

					String SelFrdID = (String) FriendList.getSelectedValue();
					if (!(SelFrdID == null)) {
						System.out.println("친구 정보 보기");
						String Friend_Id = null;
						String Friend_Name = null;
						String Friend_Email = null;
						String Friend_Phone = null;

///	        	  Connection con1 = null;

						try {
							String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; //network 스키마
							String user = "root"; // 데이터베이스 아이디
							String passwd = "12345"; // 데이터베이스 비번
							Class.forName("com.mysql.cj.jdbc.Driver");
							con1 = DriverManager.getConnection(url, user, passwd);

							PreparedStatement ps1 = null;
							ResultSet rs1 = null;
							String sql1 = "select * from client_list where client_id=?";

							ps1 = con1.prepareStatement(sql1);
							ps1.setString(1, SelFrdID); // 리스트에 선택된 친구의 ID(또는 이름)
							rs1 = ps1.executeQuery();

							while (rs1.next()) {

								Friend_Id = rs1.getString("client_ID");
								Friend_Name = rs1.getString("client_name");
								Friend_Email = rs1.getString("client_email");
								Friend_Phone = rs1.getString("client_phone");
							}
						}

						catch (SQLException sqex) {
							System.out.println("SQLException: " + sqex.getMessage());
							System.out.println("SQLState: " + sqex.getSQLState());
						}
						catch (ClassNotFoundException e)
						{
							e.printStackTrace();
						}

						JFrame a = new JFrame();
						// BorderLayout f = new BorderLayout();

						Label ShowFrdName = new Label(" 이름 : " + Friend_Name);
						Label ShowFrdEmail = new Label(" E-mail : " + Friend_Email);
						Label ShowFrdPhone = new Label(" 연락처 : " + Friend_Phone);
						Label ShowFrdID = new Label(" ID : " + Friend_Id);
						Label ShowFrdInfo = new Label("< 친구 정보  >");

						a.setLayout(null);

						ShowFrdName.setBounds(30, 50, 100, 30);
						ShowFrdID.setBounds(30, 100, 100, 30);
						ShowFrdEmail.setBounds(30, 150, 200, 30);
						ShowFrdPhone.setBounds(30, 200, 200, 30);
						ShowFrdInfo.setBounds(30, 10, 100, 30);

						a.add(ShowFrdName);
						a.add(ShowFrdID);
						a.add(ShowFrdEmail);
						a.add(ShowFrdPhone);
						a.add(ShowFrdInfo);

						a.setVisible(true);
						a.setSize(300, 300);
						FrameLocation.setLocation(a);
						a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					} // if end
				}

				else if (actionEvent.getActionCommand() == "친구삭제") {

					int num = FriendList.getSelectedIndex();
					String SelFrdID = (String) FriendList.getSelectedValue();
					String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; //network 스키마
					String user = "root"; // 데이터베이스 아이디
					String passwd = "12345"; // 데이터베이스 비번
					if (!(SelFrdID == null)) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							con1 = DriverManager.getConnection(url, user, passwd);

							PreparedStatement ps2 = null;
							int rs2;
							String sql2 = "delete from client_friend_list where client_id=? and friend_id=?";

							ps2 = con1.prepareStatement(sql2);
							String MyId = New_Client.getClientName();
							ps2.setString(1, MyId);
							ps2.setString(2, SelFrdID);
							rs2 = ps2.executeUpdate();

							System.out.println("삭제된 친구 수" + rs2);

//	        			String SetFriend_ID[] = new String[100];

							int f_count = 0;

							PreparedStatement ps3 = null;
							ResultSet rs3 = null;
							String sql = "select * from client_friend_list where client_id=?";
							ps3 = con1.prepareStatement(sql);
							ps3.setString(1, Login_ID);
							rs3 = ps3.executeQuery();

							while (rs3.next()) {
								String str = rs3.getString("friend_id");
								System.out.println(str);

								Friend_ID[f_count] = str;
								f_count++;

							}
							Friend_ID[f_count] = null;
							FriendList.setListData(Friend_ID);

						}

						catch (SQLException sqex) {
							System.out.println("SQLException: " + sqex.getMessage());
							System.out.println("SQLState: " + sqex.getSQLState());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} // if end
				} else if (actionEvent.getActionCommand() == "상세정보") {
					
					System.out.println("Hi");
					String My_Id = null;
					String My_Name = null;
					String My_Email = null;
					String My_Phone = null;
					String My_password = null;

///	        	  Connection con1 = null;
					String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; //network 스키마
					String user = "root"; // 데이터베이스 아이디
					String passwd = "12345"; // 데이터베이스 비번
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						con1 = DriverManager.getConnection(url, user, passwd);

						PreparedStatement ps1 = null;
						ResultSet rs1 = null;
						String sql1 = "select * from client_list where client_id=?";

						ps1 = con1.prepareStatement(sql1);
						String MyId = New_Client.getClientName();
						ps1.setString(1, MyId); // 리스트에 선택된 친구의 ID(또는 이름)
						rs1 = ps1.executeQuery();

						while (rs1.next()) {

							My_Id = rs1.getString("client_ID");
							My_Name = rs1.getString("client_name");
							My_Email = rs1.getString("client_email");
							My_Phone = rs1.getString("client_phone");
							My_password = rs1.getString("client_password");
						}
					}

					catch (SQLException sqex) {
						System.out.println("SQLException: " + sqex.getMessage());
						System.out.println("SQLState: " + sqex.getSQLState());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					final JFrame a = new JFrame("상세 정보");
					final JFrame b = new JFrame("알림");

					Label ShowFrdName = new Label(" 이름 : ");
					Label ShowFrdEmail = new Label(" E-mail : ");
					Label ShowFrdPhone = new Label(" 연락처 : ");
					Label ShowFrdID = new Label(" ID : " + My_Id);
					Label ShowFrdPassword = new Label(" password : ");
					Label ShowFrdInfo = new Label("< 나의 정보  >");

					final JTextField Myname = new JTextField(My_Name);
					final JTextField Myemail = new JTextField(My_Email);
					final JTextField Myphone = new JTextField(My_Phone);
					final JTextField Mypassword = new JTextField(My_password);

					JButton Edit = new JButton("수정");
					JButton Enter = new JButton("확인");
					Edit.setBackground(new Color(230, 110, 100));
					Enter.setBackground(new Color(230, 110, 100));
					final JButton EditOk2 = new JButton("완료");
					EditOk2.setBackground(new Color(230, 110, 100));
					a.setLayout(null);

					ShowFrdID.setBounds(30, 50, 100, 30);

					ShowFrdPassword.setBounds(30, 100, 65, 30);
					Mypassword.setBounds(120, 100, 100, 30);

					ShowFrdName.setBounds(30, 150, 65, 30);
					Myname.setBounds(120, 150, 100, 30);

					ShowFrdEmail.setBounds(30, 200, 65, 30);
					Myemail.setBounds(120, 200, 200, 30);

					ShowFrdPhone.setBounds(30, 250, 65, 30);
					Myphone.setBounds(120, 250, 200, 30);

					ShowFrdInfo.setBounds(30, 10, 100, 30);
					Edit.setBounds(30, 350, 90, 30);
					Enter.setBounds(250, 350, 90, 30);

					Myname.disable();
					Mypassword.disable();
					Myphone.disable();
					Myemail.disable();

					a.add(ShowFrdID);

					a.add(ShowFrdPassword);
					a.add(Mypassword);

					a.add(ShowFrdName);
					a.add(Myname);

					a.add(ShowFrdEmail);
					a.add(Myemail);

					a.add(ShowFrdPhone);
					a.add(Myphone);

					a.add(ShowFrdInfo);
					a.add(Edit);
					a.add(Enter);

					a.setVisible(true);
					a.setSize(400, 450);
					FrameLocation.setLocation(a);
					FrameLocation.setLocation(b);

					ActionListener actionListener = new ActionListener() {
						public void actionPerformed(ActionEvent actionEvent) {

							Connection con1 = null;

							if (actionEvent.getActionCommand() == "수정") {
								System.out.println("Check!");
								Myname.enable();
								Mypassword.enable();
								Myphone.enable();
								Myemail.enable();
							}

							else if (actionEvent.getActionCommand() == "확인") {

								Myname.disable();
								Mypassword.disable();
								Myphone.disable();
								Myemail.disable();

								String My_Name = Myname.getText();
								String My_Email = Myemail.getText();
								String My_Phone = Myphone.getText();
								String My_password = Mypassword.getText();

								int i;
								String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; //network 스키마
								String user = "root"; // 데이터베이스 아이디
								String passwd = "12345"; // 데이터베이스 비번

								try {
									Class.forName("com.mysql.cj.jdbc.Driver");
									con1 = DriverManager.getConnection(url, user, passwd);
									PreparedStatement ps1 = null;
									ResultSet rs1 = null;
									String sql1 = "Update client_list set client_name=?, client_password=?, client_phone=?, client_email=? where client_id=?";

									ps1 = con1.prepareStatement(sql1);
									ps1.setString(1, My_Name);
									ps1.setString(2, My_password);
									ps1.setString(3, My_Phone);
									ps1.setString(4, My_Email); // password 와 바뀌어 있었음.
									String MyId = New_Client.getClientName();
									ps1.setString(5, MyId); // 리스트에 선택된 친구의 ID(또는 이름)

									i = ps1.executeUpdate();
								}

								catch (SQLException sqex) {
									System.out.println("SQLException: " + sqex.getMessage());
									System.out.println("SQLState: " + sqex.getSQLState());
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								Label EditOk1 = new Label("회원 정보가 수정되었습니다");

								b.setLayout(null);

								EditOk1.setBounds(40, 10, 200, 30);
								EditOk2.setBounds(75, 70, 70, 30);

								b.add(EditOk1);
								b.add(EditOk2);

								b.setVisible(true);
								b.setSize(230, 150);

							} else if (actionEvent.getActionCommand() == "완료") {
								a.setVisible(false);
								b.setVisible(false);
							}

						}
					};

					Edit.addActionListener(actionListener);
					Enter.addActionListener(actionListener);
					EditOk2.addActionListener(actionListener);

				}
			}
		};

		JMenuItem CreateTalk = new JMenuItem("대화하기");
		CreateTalk.addActionListener(actionListener);
		FFindpopUp.add(CreateTalk);
		Logout.addActionListener(LogoutAction);
		JMenuItem InfoFriend = new JMenuItem("친구정보");
		InfoFriend.addActionListener(actionListener);
		FFindpopUp.add(InfoFriend);

		JMenuItem DeleteFriend = new JMenuItem("친구삭제");
		DeleteFriend.addActionListener(actionListener);
		FFindpopUp.add(DeleteFriend);

		myInfo.addActionListener(actionListener);

		// getContentPane().add(tabbedPane);
		// setSize(500, 500);
		// setVisible(true);

		FriendList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON3) {
					JList c = (JList) e.getComponent();
					int x = e.getX();
					int y = e.getY();
					if (!FriendList.isSelectionEmpty()
							&& FriendList.locationToIndex(e.getPoint()) == FriendList.getSelectedIndex()) {
						int count = c.getModel().getSize();
						int cal = count * 18;
						if (y <= cal) {
							FFindpopUp.show(FriendList, x, y);
						}
					}
				}
			}
		});

		return panel1;
	}

}
