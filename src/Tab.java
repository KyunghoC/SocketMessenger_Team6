


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//로그인과 친구목록, 친구 찾기 목록, 로그아웃 관리.
public class Tab extends JFrame {
	private JTabbedPane tab;
	private JPanel p1, p2, p3;
	private JButton bHome, bTab1, bTab2;
	private JLabel label1, label2;
	private TextField textField1, textField2;
	private JPanel friendList;
	JoinFrame jf;

	public Tab() {
		tab = new JTabbedPane();
		p1 = new JPanel();
		p1.setBackground(new Color(235, 235, 190));
		label1 = new JLabel("아이디 입력  :  ");
		textField1 = new TextField(15);
		label2 = new JLabel("비밀번호 입력  :  ");
		textField2 = new TextField(15);
		textField2.setEchoChar('*');
		bHome = new JButton("로그인");
		bHome.setBackground(new Color(230, 110, 100));
		bHome.setForeground(new Color(255, 255, 255));
		bTab1 = new JButton("회원가입");
		bTab1.setBackground(new Color(230, 110, 100));
		bTab1.setForeground(new Color(255, 255, 255));

		for (int i = 0; i < 2; i++) { // 생성되어진 버튼에 액션리스너와 패널에 추가

			p1.add(label1); // 로그인 패널 안에 넣기
			p1.add(textField1);
			p1.add(label2);
			p1.add(textField2);
			p1.add(bHome);
			p1.add(bTab1);

		}
		bHome.addActionListener(new LoginCheck()); // 로그인 버튼을 눌렀을때
		bTab1.addActionListener(new ActionListener() { // 회원가입 버튼을 눌렀을때
			public void actionPerformed(ActionEvent e) {
				jf = new JoinFrame();
			}
		});
		tab.add("로그인", p1); // 탭에 제목과 패널 추가
		friendList = FriendListMain.getFriendListMain();
		tab.addTab("친구목록", null, friendList); // 친구목록 불러오기
		tab.addTab("친구찾기", null, FindPanel.getFindFriend()); // 친구추가 불러오기
		tab.setEnabledAt(1, false); // 친구목록 disable
		tab.setEnabledAt(2, false); // 친구찾기 disable
		tab.addChangeListener(new TabAction());
		textField2.addKeyListener(new LogindAction());
		// tab.addChangeListener(l);
		getContentPane().add(tab); // 프레임에 탭 추가
		pack();

		setSize(320, 500);
		setVisible(true);
		setResizable(false); // false 일때 크기 고정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textField1.requestFocus();
		FrameLocation.setLocation(this);

		this.addWindowListener(new FrameListener());
	}

	public class LogindAction implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == 10) { // 엔터를 누를 때 보내기 버튼을 강제 클릭

				bHome.doClick();

			}

		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent arg0) {
		}

	}

	public class TabAction implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			System.out.println(tab.getSelectedIndex());
			String[] d = getFriendList.getSearch(New_Client.getClientName());
			FriendListMain.FriendList.setListData(d);
		}

	}

	public class LoginCheck implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Login log = new Login();
			String tempClientId, tempClientPassword;
			tempClientId = textField1.getText(); // Id와 Passwor를 받아옴.
			tempClientPassword = textField2.getText();
			int logCheck = 0;
			logCheck = log.client_login(tempClientId, tempClientPassword);
			if (logCheck == 2) { // 만약 로그인이 성공적이라면 정보를 저장.
				new LoginPanel("로그인 성공", 2);
				New_Client.runClient(); // 서버와 연결 실행
				New_Client.setClientName(tempClientId); // 회원의 ID를 서버에 같이 넘김
				Object evt = e.getSource();
				for (int i = 0; i < 2; i++)
					if (evt == bHome) { // 버튼 누른곳(친구목록) 으로 이동
						tab.setSelectedIndex(i);

					}
				tab.setEnabledAt(0, false); // 버튼 눌렀을 시 첫번째 로그인 화면 disable
				tab.setEnabledAt(1, true); // 친구목록 enable
				tab.setEnabledAt(2, true); // 친구 찾기 enable

			} else if (logCheck == 1) // 비밀번호를 잘못 입력했을 경우
			{
				new LoginPanel("비밀번호 오류", 1);
				System.out.println("비밀번호 잘못입력");
				textField1.setText("");
				textField2.setText("");
			} else {
				new LoginPanel("로그인 중복 오류", 0);
				System.out.println("이미 로그인되어 있습니다.");
				textField1.setText("");
				textField2.setText("");
			}

		}

	}// Listener end

	public class FrameListener implements WindowListener { // 윈도우 창의 이벤트 처리

		public void windowActivated(WindowEvent arg0) {
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) { // 임의 적으로 윈도우의 X표를 눌렀을 시 발생 //로그아웃 부분과 동일하게 구현해야함
			Map d = New_Client.getMap(); // 현재 회원이 들어가있는 방에대한 정보를 얻어옴
			Collection<client_2> client_2_values = d.values(); // 방에 대한 처리를 하기위해 값을 전달
			for (Iterator<client_2> it = client_2_values.iterator(); it.hasNext();) {
				it.next().exit_bt.doClick(); // 해당 방의 나가기 버튼을눌러줌.
			}
		}// 윈도우 상에서 x표를 눌렀을 시 나가기 버튼을 클릭하게 한다.

		public void windowDeactivated(WindowEvent arg0) {
		}

		public void windowDeiconified(WindowEvent arg0) {
		}

		public void windowIconified(WindowEvent arg0) {
		}

		public void windowOpened(WindowEvent arg0) {
		}

	}

}