//https://balabala.tistory.com/34

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
 
class CommandC {
    public final static int FILE_NAME = 0x00;
    public final static int FILE_SIZE = 0x01;
    public final static int SEND_BEGIN = 0x02;
    public final static int SEND_END = 0x03;
}
 
public class Client extends JFrame{
    private JTextField textField;
    private JButton selectImageButton;
    private JButton fileSendButton;
    private String filePath;
    public Client(){
        super("���� ���� Ŭ���̾�Ʈ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
 
        textField = new JTextField(20);
        selectImageButton = new JButton("���� ����");
        fileSendButton = new JButton("���� ����");
 
        c.add(textField);
        c.add(selectImageButton);
        c.add(fileSendButton);
 
        MyActionListener listener = new MyActionListener();
        selectImageButton.addActionListener(listener);
        fileSendButton.addActionListener(listener);
 
        setSize(300, 200);
        setVisible(true);
    }
    class MyActionListener implements ActionListener{
        private JFileChooser chooser;
        public MyActionListener(){
            chooser = new JFileChooser();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
 
            if(btn == selectImageButton){
            	
                int ret = chooser.showOpenDialog(null);
                if(ret != JFileChooser.APPROVE_OPTION){
                    JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�", "���", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                filePath = chooser.getSelectedFile().getPath();
                textField.setText(filePath);
            }
            else if(btn == fileSendButton){
                fileSend();
            }
        }
    }
    private static void sentInt(BufferedOutputStream fout, int value) throws IOException {
        fout.write(value & 0x000000ff);
        fout.write((value & 0x0000ff00)>>8);
        fout.write((value & 0x00ff0000)>>16);
        fout.write((value & 0xff000000)>>24);
    }
 
    public void fileSend(){
        FileInputStream fin = null; // BufferedInputStream
        File f = null;
        BufferedOutputStream fout = null;
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9999); // Ŭ���̾�Ʈ ���� ����
            f = new File(filePath);
            long length = f.length();
            fin = new FileInputStream(f); // Ű����κ����� �Է� ��Ʈ��
            fout = new BufferedOutputStream(socket.getOutputStream()); // �������� ��� ��Ʈ��
 
            byte[] fname = f.getName().getBytes(); // ��θ� ������ ���� �̸�
            int nameSize = fname.length;
            fout.write(CommandC.FILE_NAME); // FILE_NAME ��� ����
            sentInt(fout, nameSize);  // ���� �̸� ũ�� ����
            fout.write(fname); // ���� ���� �̸� ����
 
            fout.write(CommandC.FILE_SIZE); // FILE_SIZE ��� ����
            sentInt(fout, (int)length);
            sentInt(fout, (int)(length >>> 32));
 
            fout.write(CommandC.SEND_BEGIN); // SEND_BEGIN ��� ����
            int count = 0;
            byte b[] = new byte[512];
            while (length > 0) {
                int numRead = fin.read(b,0,b.length);
                if (numRead <= 0) {
                    if (length > 0) { // �� �̻� ���� ���� ���µ� ���� ���� ũ�⸸ŭ �� ���� ���
                        System.out.println("���� ������ �߻��߽��ϴ�. ���� ����Ʈ: " + count + " ���� ����Ʈ:" + length);
                        break;
                    }
                } else {
                    fout.write(b, 0, numRead);
                    length -= numRead;
                    fout.flush();
                    count += numRead;
                }
            }
            fout.write(CommandC.SEND_END); // SEND_END ��� ����
            fout.flush();
            fin.close();
            socket.close(); // Ŭ���̾�Ʈ ���� �ݱ�
            System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
        } catch (IOException e) {
            System.out.println("���� ���� �� ������ �߻��߽��ϴ�.");
        }
    }
    public static void main(String[] args){
        new Client();
    }
}