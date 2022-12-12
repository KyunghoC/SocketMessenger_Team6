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
        super("파일 받는 서버");
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
            listener = new ServerSocket(9999); // 서버 소켓 생성
            socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
            textArea.append("연결됨\n");
            
            //예0, 아니오1
          	int var = JOptionPane.showConfirmDialog(null,"Do you want to receive the file?","Approval",JOptionPane.YES_NO_OPTION);
          	
          	if(var==0) {
 
	            fin = new BufferedInputStream(socket.getInputStream()); // 클라이언트로부터의 입력 스트림
	            int cmd;
	            String fileName = null;
	            long length=0;
	 
	 
	            cmd = fin.read();
	            if (cmd == CommandS.FILE_NAME) { // FILE_NAME 명령 수신
	                int nameSize = receiveInt(fin); // 파일 이름 크기
	                textArea.append("전송받은 파일 이름 길이 :" + nameSize + "\n");
	                byte fname[] = new byte[nameSize];
	                fin.read(fname);
	                fileName = new String(fname);
	                textArea.append("전송받은 파일 이름:" + fileName + "\n");
	                receiveFileName = "copy_" + fileName;
	                textArea.append("저장할 파일 이름:" + receiveFileName + "\n");
	                fout = new BufferedOutputStream(new FileOutputStream(receiveFileName));
	            } else {
	                textArea.append("명령 수신 오류" + cmd + "\n");
	                socket.close(); // 클라이언트와 통신용 소켓 닫기
	                listener.close(); // 서버 소켓 닫기
	                return;
	            }
	 
	            cmd = fin.read();
	            if (cmd == CommandS.FILE_SIZE) { // FILE_SIZE 명령 수신
	                int lenghLow = receiveInt(fin); // 파일 크기의 하위 4바이트
	                int lenghHigh = receiveInt(fin); // 파일 크기의 상위 4바이트
	                length = lenghHigh;
	                length <<= 32; // 상위 바이트로 만들기 위해 32비트 쉬프트
	                length += lenghLow; // 하위 4 바이트 더하기
	                textArea.append("전송받은 파일 크기:" + length + "\n");
	            } else {
	                textArea.append("명령 수신 오류" + cmd + "\n");
	                if (fout != null)
	                    fout.close();
	                socket.close(); // 클라이언트와 통신용 소켓 닫기
	                listener.close(); // 서버 소켓 닫기
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
	                        if (length > 0) { // 더 이상 읽을 것이 없는데 아직 파일 크기만큼 못 읽은 경우
	                            textArea.append("전송 오류가 발생했습니다. 읽은 바이트: " + numRead + " 남은 바이트:" + length + "\n");
	                            break;
	                        }
	                    } else {
	                        textArea.append("."); // 진행 상태 표시
	                        fout.write(b, 0, numRead);
	                        length -= numRead;
	                    }
	                }
	 
	                cmd = fin.read();
	                if (cmd == CommandS.SEND_END) { // SEND_END 명령
	                    textArea.append("\n파일 수신 성공. 현재 퐅더에 저장되었습니다." + "\n");
	                    icon = new ImageIcon(receiveFileName);
	                    this.setSize(icon.getIconWidth(), icon.getIconHeight());
	                    panel.repaint();
	 
	                } else {
	                    textArea.append("\n명령 수신 오류" + cmd + "\n");
	                }
	                if (fout != null)
	                    fout.close();
	                socket.close(); // 클라이언트와 통신용 소켓 닫기
	                listener.close(); // 서버 소켓 닫기
	            }
            
          	}
          	else {
          		JOptionPane.showConfirmDialog(null,"File refused to be accepted","Approval",JOptionPane.PLAIN_MESSAGE); //파일 승인 거부 메세지
          	}
          	
        } catch (IOException e) {
            textArea.append("파일 수신 중 오류가 발생했습니다.\n");
        }
      	
    }
 
    public static void main(String[] args){
        new Server();
    }
}
