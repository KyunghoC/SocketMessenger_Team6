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
        super("파일 전송 클라이언트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
 
        textField = new JTextField(20);
        selectImageButton = new JButton("파일 선택");
        fileSendButton = new JButton("파일 전송");
 
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
                    JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
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
            socket = new Socket("localhost", 9999); // 클라이언트 소켓 생성
            f = new File(filePath);
            long length = f.length();
            fin = new FileInputStream(f); // 키보드로부터의 입력 스트림
            fout = new BufferedOutputStream(socket.getOutputStream()); // 서버로의 출력 스트림
 
            byte[] fname = f.getName().getBytes(); // 경로를 제외한 파일 이름
            int nameSize = fname.length;
            fout.write(CommandC.FILE_NAME); // FILE_NAME 명령 전송
            sentInt(fout, nameSize);  // 파일 이름 크기 전송
            fout.write(fname); // 실제 파일 이름 전송
 
            fout.write(CommandC.FILE_SIZE); // FILE_SIZE 명령 전송
            sentInt(fout, (int)length);
            sentInt(fout, (int)(length >>> 32));
 
            fout.write(CommandC.SEND_BEGIN); // SEND_BEGIN 명령 전송
            int count = 0;
            byte b[] = new byte[512];
            while (length > 0) {
                int numRead = fin.read(b,0,b.length);
                if (numRead <= 0) {
                    if (length > 0) { // 더 이상 읽을 것이 없는데 아직 파일 크기만큼 못 읽은 경우
                        System.out.println("전송 오류가 발생했습니다. 읽은 바이트: " + count + " 남은 바이트:" + length);
                        break;
                    }
                } else {
                    fout.write(b, 0, numRead);
                    length -= numRead;
                    fout.flush();
                    count += numRead;
                }
            }
            fout.write(CommandC.SEND_END); // SEND_END 명령 전송
            fout.flush();
            fin.close();
            socket.close(); // 클라이언트 소켓 닫기
            System.out.println("파일 전송이 완료되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 전송 중 오류가 발생했습니다.");
        }
    }
    public static void main(String[] args){
        new Client();
    }
}