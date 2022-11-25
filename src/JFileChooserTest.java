import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class JFileChooserTest {
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		
        JFileChooser fileChooser = new JFileChooser();
        
        //���Ͽ��� ���̾�α� �� ���
		int result = fileChooser.showOpenDialog(window);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			//������ ������ ��� ��ȯ
		    File selectedFile = fileChooser.getSelectedFile();
		    
		    //��� ���
		    System.out.println(selectedFile);
		}
		
	}

}
