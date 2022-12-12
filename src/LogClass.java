import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class LogClass {
	public OutputStream log_out;
	public OutputStream copy;
	public InputStream in;

	public LogClass() {
		try {
			
			Calendar call = Calendar.getInstance();
			int year = call.get(Calendar.YEAR);
			int month = call.get(Calendar.MONTH)+1;
			int day = call.get(Calendar.DATE);
			int hour = call.get(Calendar.HOUR);
			int minute = call.get(Calendar.MINUTE);
			int second = call.get(Calendar.SECOND);
			String date = " #######" + year + "년" + month + "월" + day + "일" + hour + "시 " + minute + "분 " + second
					+ "초 LOGFILE";
			String s = date + ".txt";
			log_out = new FileOutputStream(s);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void log_out(String str) {
		Calendar call = Calendar.getInstance();
		int year = call.get(Calendar.YEAR);
		int month = call.get(Calendar.MONTH)+1;
		int day = call.get(Calendar.DATE);
		int hour = call.get(Calendar.HOUR);
		int minute = call.get(Calendar.MINUTE);
		int second = call.get(Calendar.SECOND);
		String date = " #######" + year + "년 " + month + "월" + day + "일" + hour + "시 " + minute + "분 " + second
				+ "초   LOG =  ";
		String tempLog = date + str;
		byte[] arr = tempLog.getBytes();
		try {
			log_out.write(arr);
			log_out.write(13);
			log_out.write(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
