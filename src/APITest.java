import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APITest {
    public static void main(String[] args) throws Exception {

        // 변수 설정
        String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String authKey = "%2BHbLqfWwnHjL4AQ4O6wpgtXmNbxfxOu1eiwaO9%2FMCZyEYMfPqYzNG3oRlSg1dKEDE83p%2FvaQncWUZZA2GCI0hA%3D%3D"; // 본인 서비스 키

		// 구하고자 하는 시간과 좌표 대입
        String nx = "69";
        String ny = "100";
        String baseDate = "20221212";
        String baseTime = "1200";

        String dataType = "JSON";

        StringBuilder urlBuilder = new StringBuilder(apiURL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + authKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows=10", "UTF-8"));    // 표 개수
        urlBuilder.append("&" + URLEncoder.encode("pageNo=1", "UTF-8"));    // 페이지 수
        // JSON 형식으로 반환을 원하면 주석 제거
        // urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); // 받으려는 타입
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 조회하고 싶은 날짜
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 조회하고싶은 시간
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // x좌표
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // y좌표

        URL url = new URL(urlBuilder.toString());
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();

		// 테스트를 위해 출력
        System.out.println(result);

    }
}