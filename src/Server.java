import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
 
class CommandS {
    public final static int FILE_NAME = 0x00;
    public final static int FILE_SIZE = 0x01;
    public final static int SEND_BEGIN = 0x02;
    public final static int SEND_END = 0x03;
}
 
public class Server extends JFrame{
    private JTextArea textArea = new JTextArea();
    private MyPanel panel = new MyPanel();
    private String receiveFileName;
    private ImageIcon icon = null;
    public Server(){
        super("���� �޴� ����");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Container c = getContentPane();
 
        JPanel textPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        textArea.setEnabled(false);
 
        c.add(textPanel, BorderLayout.NORTH);
        c.add(panel, BorderLayout.CENTER);
 
        setSize(500, 300);
        setVisible(true);
        this.fileReceiver();
    }
    class MyPanel extends JPanel{
        private Image img;
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(icon != null){
                img = icon.getImage();
                g.drawImage(img, 0, 0, this);
            }
        }
    }
 
    private static int receiveInt(BufferedInputStream fin) throws IOException {
        int value;
        value = fin.read();
        value |= fin.read() << 8;
        value |= fin.read() << 16;
        value |= fin.read() << 24;
        return value;
    }
    public void fileReceiver(){
        BufferedInputStream fin = null;
        BufferedOutputStream fout = null;
        ServerSocket listener = null;
        Socket socket = null;
        
        
      		
      	try {
            listener = new ServerSocket(9999); // ���� ���� ����
            socket = listener.accept(); // Ŭ���̾�Ʈ�κ��� ���� ��û ���
            textArea.append("�����\n");
            
            //��0, �ƴϿ�1
          	int var = JOptionPane.showConfirmDialog(null,"Do you want to receive the file?","Approval",JOptionPane.YES_NO_OPTION);
          	
          	if(var==0) {
 
	            fin = new BufferedInputStream(socket.getInputStream()); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
	            int cmd;
	            String fileName = null;
	            long length=0;
	 
	 
	            cmd = fin.read();
	            if (cmd == CommandS.FILE_NAME) { // FILE_NAME ��� ����
	                int nameSize = receiveInt(fin); // ���� �̸� ũ��
	                textArea.append("���۹��� ���� �̸� ���� :" + nameSize + "\n");
	                byte fname[] = new byte[nameSize];
	                fin.read(fname);
	                fileName = new String(fname);
	                textArea.append("���۹��� ���� �̸�:" + fileName + "\n");
	                receiveFileName = "copy_" + fileName;
	                textArea.append("������ ���� �̸�:" + receiveFileName + "\n");
	                fout = new BufferedOutputStream(new FileOutputStream(receiveFileName));
	            } else {
	                textArea.append("��� ���� ����" + cmd + "\n");
	                socket.close(); // Ŭ���̾�Ʈ�� ��ſ� ���� �ݱ�
	                listener.close(); // ���� ���� �ݱ�
	                return;
	            }
	 
	            cmd = fin.read();
	            if (cmd == CommandS.FILE_SIZE) { // FILE_SIZE ��� ����
	                int lenghLow = receiveInt(fin); // ���� ũ���� ���� 4����Ʈ
	                int lenghHigh = receiveInt(fin); // ���� ũ���� ���� 4����Ʈ
	                length = lenghHigh;
	                length <<= 32; // ���� ����Ʈ�� ����� ���� 32��Ʈ ����Ʈ
	                length += lenghLow; // ���� 4 ����Ʈ ���ϱ�
	                textArea.append("���۹��� ���� ũ��:" + length + "\n");
	            } else {
	                textArea.append("��� ���� ����" + cmd + "\n");
	                if (fout != null)
	                    fout.close();
	                socket.close(); // Ŭ���̾�Ʈ�� ��ſ� ���� �ݱ�
	                listener.close(); // ���� ���� �ݱ�
	                return;
	            }
	 
	            cmd = fin.read();
	            if (cmd == CommandS.SEND_BEGIN) {
	                int numberToRead;
	                while (length >0) {
	                    byte b[] = new byte[2048];
	                    if (length < b.length)
	                        numberToRead = (int)length;
	                    else
	                        numberToRead = b.length;
	                    int numRead = fin.read(b, 0, numberToRead);
	                    if (numRead <= 0) {
	                        if (length > 0) { // �� �̻� ���� ���� ���µ� ���� ���� ũ�⸸ŭ �� ���� ���
	                            textArea.append("���� ������ �߻��߽��ϴ�. ���� ����Ʈ: " + numRead + " ���� ����Ʈ:" + length + "\n");
	                            break;
	                        }
	                    } else {
	                        textArea.append("."); // ���� ���� ǥ��
	                        fout.write(b, 0, numRead);
	                        length -= numRead;
	                    }
	                }
	 
	                cmd = fin.read();
	                if (cmd == CommandS.SEND_END) { // SEND_END ���
	                    textArea.append("\n���� ���� ����. ���� �j���� ����Ǿ����ϴ�." + "\n");
	                    icon = new ImageIcon(receiveFileName);
	                    this.setSize(icon.getIconWidth(), icon.getIconHeight());
	                    panel.repaint();
	 
	                } else {
	                    textArea.append("\n��� ���� ����" + cmd + "\n");
	                }
	                if (fout != null)
	                    fout.close();
	                socket.close(); // Ŭ���̾�Ʈ�� ��ſ� ���� �ݱ�
	                listener.close(); // ���� ���� �ݱ�
	            }
            
          	}
          	else {
          		JOptionPane.showConfirmDialog(null,"File refused to be accepted","Approval",JOptionPane.PLAIN_MESSAGE); //���� ���� �ź� �޼���
          	}
          	
        } catch (IOException e) {
            textArea.append("���� ���� �� ������ �߻��߽��ϴ�.\n");
        }
      	
    }
 
    public static void main(String[] args){
        new Server();
    }
}
