import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JFrame;

public class LoginPanel extends JFrame { // 로그인을 했을 경우 성공적인 경우와 비밀번호를 틀렸을 경우 중복로그인을 했을 경우로 나누게 됨
	// 그 후 상태의 메시지를 사용자에게 알리기 위한 GUI
	Button checkBtn;
	int check;

	public LoginPanel(String title, int check) {

		super(title);
		setLayout(new FlowLayout());
		this.check = check;
		init(check);
	}

	public void init(int i) {
		Label lbl1;
		checkBtn = new Button("확인");
		if (i == 0) {
			lbl1 = new Label("이미 로그인 되어있거나 없는 아이디입니다.");
		} else if (i == 1) {
			lbl1 = new Label("비밀번호를 잘못 입력하셨습니다.");
		} else {
			lbl1 = new Label("로그인 되었습니다.");
		}
		checkBtn.addActionListener(new ExitButtonListener());
		this.add(lbl1);
		this.add(checkBtn);

		setSize(280, 100);
		setVisible(true);
		setResizable(false); // false 일때 크기 고정
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FrameLocation.setLocation(this);
	}

	public class ExitButtonListener implements ActionListener { // ======================나가기
		// 버튼
		public void actionPerformed(ActionEvent event) {

			dispose(); // 해당 프레임만 끄기

		}
	}
}
