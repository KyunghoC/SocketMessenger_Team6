import javax.swing.JOptionPane;

public class JOptionPaneTest {

	public static void main(String[] args) {
		
		//��0, �ƴϿ�1
		int var = JOptionPane.showConfirmDialog(null,"Do you want to receive the file?","Approval",JOptionPane.YES_NO_OPTION);
		System.out.println(var);

	}

}
