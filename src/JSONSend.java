import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.simple.JSONObject;

public class JSONSend {
	public static void main(String[] args) throws IOException {
		Socket socket = null;

		// ���� ��û //
		socket = new Socket(); // ��ü ����
		socket.connect(new InetSocketAddress("IPNUMBER", 50001)); // ���� ���� (������ �� ��Ʈ��ȣ �ο�)
		System.out.println("Connect Success!"); // ��¹�

		// ����� ��Ʈ�� ��� //
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();

		// Ŭ���̾�Ʈ -> ���� //
		JSONObject jsonRoot = new JSONObject(); // jsonObject ����
		jsonRoot.put("action", "join"); // �Ӽ��� �Ӽ���

		JSONObject jsonData = new JSONObject(); // data ��ü ����
		jsonData.put("StatusCode", 01);// 01: ����, 02: ���� ����, 03: ��� Ʋ�� // If�� ó���Ͽ� Statcode ���� ���� Put, Get �ٸ���.
		jsonData.put("id", "testID");
		jsonData.put("name", "trstUserName");
		jsonData.put("password", "testPassword");
		jsonData.put("statMessage", "�޷�:P");
		jsonData.put("lastCon", "2022.12.05 12:03:34");
		// ��� ��� �߰�

		jsonRoot.put("data", jsonData); // json ����

		String json = jsonRoot.toString(); // String ��ȯ

		System.out.println(json); // ����غ���

		os = socket.getOutputStream(); // outputStream
		PrintWriter pw = new PrintWriter(os); // PrintWriter�� ����
		pw.println(json); // println�� �̿��Ͽ� \r\n�� ���� ����
		pw.flush(); // flush
		os.flush();

	}

}
