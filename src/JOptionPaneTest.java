import javax.swing.JOptionPane;

public class JOptionPaneTest {

	public static void main(String[] args) {
		
		//예0, 아니오1
		int var = JOptionPane.showConfirmDialog(null,"Do you want to receive the file?","Approval",JOptionPane.YES_NO_OPTION);
		System.out.println(var);

	}

}
