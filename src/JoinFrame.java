import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JoinFrame extends JFrame {
	/* Panel */
	JPanel panel = new JPanel();

	/* Label */
	JLabel idL = new JLabel("���̵�");
	JLabel pwL = new JLabel("��й�ȣ");

	/* TextField */
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();

	/* Button */
	JButton joinBtn = new JButton("�����ϱ�");
	JButton cancelBtn = new JButton("�������");

	JoinFrame() {

		setTitle("ȸ������");

		/* Label ũ�� �۾� */
		idL.setPreferredSize(new Dimension(50, 30));
		pwL.setPreferredSize(new Dimension(50, 30));

		/* TextField ũ�� �۾� */
		id.setPreferredSize(new Dimension(140, 30));
		pw.setPreferredSize(new Dimension(140, 30));

		/* Button ũ�� �۾� */
		joinBtn.setPreferredSize(new Dimension(95, 25));
		cancelBtn.setPreferredSize(new Dimension(95, 25));

		/* Panel �߰� �۾� */
		setContentPane(panel);

		panel.add(idL);
		panel.add(id);

		panel.add(pwL);
		panel.add(pw);

		panel.add(cancelBtn);
		panel.add(joinBtn);

		/* Button �̺�Ʈ ������ �߰� */
		ButtonListener bl = new ButtonListener();

		cancelBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);

		setSize(250, 150);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	/* Button �̺�Ʈ ������ */
	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			/* TextField�� �Էµ� ȸ�� �������� ������ �ʱ�ȭ */
			String uid = id.getText();
			String upass = "";
			for (int i = 0; i < pw.getPassword().length; i++) {
				upass = upass + pw.getPassword()[i];
			}

			/* ������� ��ư �̺�Ʈ */
			if (b.getText().equals("�������")) {
				dispose();
			}

			/* �����ϱ� ��ư �̺�Ʈ */
			else if (b.getText().equals("�����ϱ�")) {
				if (uid.equals("") || upass.equals("")) {
					JOptionPane.showMessageDialog(null, "��� ������ �������ּ���", "ȸ������ ����", JOptionPane.ERROR_MESSAGE);
					System.out.println("ȸ������ ���� > ȸ������ ���Է�");
				}

				else if (!uid.equals("") && !upass.equals("")) {
					if (joinCheck(uid, upass)) {
						System.out.println("ȸ������ ����");
						JOptionPane.showMessageDialog(null, "ȸ�����Կ� �����Ͽ����ϴ�");
						dispose();
					} else {
						System.out.println("ȸ������ ����");
						JOptionPane.showMessageDialog(null, "ȸ�����Կ� �����Ͽ����ϴ�");
						id.setText("");
						pw.setText("");
					}
				}
			}
		}
	}

	boolean joinCheck(String _i, String _p) {
		boolean flag = false;

		String id = _i;
		String pw = _p;
		String[] encryptPW = Salt.encrypt(_p);
		String salt = encryptPW[0];
		pw = encryptPW[1];

		// Server�� salt, PW �����ϱ� �� ID validate.

		// �����ϴ� ID ������ salt, PW �����Ͽ� Join
		Connection con = null;
		Statement stmt = null;
		String url = "jdbc:mysql://localhost/network?serverTimezone=Asia/Seoul"; // network ��Ű��
		String user = "root"; // �����ͺ��̽� ���̵�
		String passwd = "12345"; // �����ͺ��̽� ���

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			String insertStr = "INSERT INTO client_list (client_id, client_password) VALUES('" + id + "', '" + pw
					+ "')";
			stmt.executeUpdate(insertStr);
			insertStr = "INSERT INTO login_check VALUES('" + id + "', 'logout')";
			stmt.executeUpdate(insertStr);
			flag = true;
			System.out.println("ȸ������ ����");
		} catch (Exception e) {
			flag = false;
			System.out.println("ȸ������ ���� > " + e.toString());
		}

		return flag;
	}
}