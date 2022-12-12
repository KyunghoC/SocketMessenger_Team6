import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Salt {
	public String[] encrypt(String pwd) {
		Salt en = new Salt();

//		System.out.println("pwd : " + pwd);

		// Salt ����
		String salt = en.getSalt();
//		System.out.println("salt : " + salt);

		// ���� pwd ����
		String res = en.getEncrypt(pwd, salt);
		String[] tmpStrings = new String[2];
		tmpStrings[0] = salt;
		tmpStrings[1] = res;
		return tmpStrings;
	}

	public String getSalt() {

		// 1. Random, byte ��ü ����
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[20];

		// 2. ���� ����
		r.nextBytes(salt);

		// 3. byte To String (10������ ���ڿ��� ����)
		StringBuffer sb = new StringBuffer();
		for (byte b : salt) {
			sb.append(String.format("%02x", b));
		}
		;

		return sb.toString();
	}

	public String getEncrypt(String pwd, String salt) {

		String result = "";
		try {
			// 1. SHA256 �˰��� ��ü ����
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// 2. pwd�� salt ��ģ ���ڿ��� SHA 256 ����
//			System.out.println("pwd + salt ���� �� : " + pwd + salt);
			md.update((pwd + salt).getBytes());
			byte[] pwdsalt = md.digest();

			// 3. byte To String (10������ ���ڿ��� ����)
			StringBuffer sb = new StringBuffer();
			for (byte b : pwdsalt) {
				sb.append(String.format("%02x", b));
			}

			result = sb.toString();
//			System.out.println("pwd + salt ���� �� : " + result);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
}
