import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONSend {
	public static void main(String[] args) throws IOException, ParseException {
		Socket socket = null;

		// ���� ��û //
		socket = new Socket(); // ��ü ����
		socket.connect(new InetSocketAddress("IPNUMBER", 50001)); // ���� ���� (������ �� ��Ʈ��ȣ �ο�)
		System.out.println("Connect Success!"); // ��¹�

		// ����� ��Ʈ�� ��� //
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();

		// ���� -> Ŭ���̾�Ʈ //
		JSONObject jsonRoot = new JSONObject(); // jsonObject ����
		jsonRoot.put("StatusCode", 01);// 01: ����, 02: ���� ����, 03: ��� Ʋ�� // If�� ó���Ͽ� Statcode ���� ���� Put, Get �ٸ���.

		JSONObject jsonData = new JSONObject(); // data ��ü ����
		jsonData.put("id", "testID");
		jsonData.put("name", "testUserName");
		jsonData.put("password", "testPassword");
		jsonData.put("statMessage", "�޷�:P");
		jsonData.put("lastCon", "2022.12.05 12:03:34");
		// ��� ��� �߰�

		jsonRoot.put("data", jsonData); // json ����

		String json = jsonRoot.toJSONString(); // String ��ȯ

		System.out.println(json); // ����غ���

		os = socket.getOutputStream(); // outputStream
		PrintWriter pw = new PrintWriter(os); // PrintWriter�� ����
		pw.println(json); // println�� �̿��Ͽ� \r\n�� ���� ����
		pw.flush(); // flush
		os.flush();

		// Ŭ���̾�Ʈ -> ���� //
		is = socket.getInputStream(); // inputStream

		Reader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		String strJson = br.readLine();

		JSONParser parser = new JSONParser();

		JSONObject jsonResult = (JSONObject) parser.parse(strJson); // json��ü ���� (json �Ľ�)
		Integer StatusCode = (Integer) jsonResult.get("StatusCode");
		if (StatusCode == 01) {
			JSONObject jsoninner = (JSONObject) jsonResult.get("data");
			String name = (String) jsoninner.get("name");
			String statMessage = (String) jsoninner.get("statMessage");
			String time = (String) jsoninner.get("lastCon");

		}

		System.out.println("StatCode: " + Integer.toString(StatusCode)); // action ���

		is.close(); // InputStream Close
		os.close();

	}

}
