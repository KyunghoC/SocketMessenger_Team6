import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class XMLIntegrateTest {

	// 열거형으로 정의 후 사용
    enum WeatherValue {
        PTY, REH, RN1, T1H, UUU, VEC, VVV, WSD
    }

    public static void main(String[] args) throws Exception {

        // 입력받을 weather 객체
        Weather weather = new Weather();

        // 변수 설정
        String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String authKey = "%2BHbLqfWwnHjL4AQ4O6wpgtXmNbxfxOu1eiwaO9%2FMCZyEYMfPqYzNG3oRlSg1dKEDE83p%2FvaQncWUZZA2GCI0hA%3D%3D"; // 본인 Service 키 등록

        String nx = "63"; //성남시 수정구
        String ny = "124"; //성남시 수정구
        String baseDate = ""; //날짜
        String baseTime = ""; //시간
        
        //현재 날짜
        LocalDate date = LocalDate.now();
    	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");//baseDate 형식에 맞게
    	baseDate = date.format(formatter1);
    	
    	//현재시간 
    	LocalTime time = LocalTime.now();
    	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
    	baseTime=time.format(formatter2)+"00"; //baseTime 형식에 맞게

        StringBuilder urlBuilder = new StringBuilder(apiURL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + authKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows=10", "UTF-8"));    // 숫자 표
        urlBuilder.append("&" + URLEncoder.encode("pageNo=1", "UTF-8"));    // 페이지 수
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도

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

        System.out.println(result);

        // 문자열 Document 로 변경해서 List 형태로 가져와서 객체에 파싱함.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(result));
        Document document = db.parse(is);

        try {
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            NodeList nList = document.getElementsByTagName("item");
            System.out.println("--------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                // 현재 Element 확인하려면 아래 주석 해제
//                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String category = eElement.getElementsByTagName("category").item(0).getTextContent();
                    double value = Double.parseDouble(eElement.getElementsByTagName("obsrValue").item(0).getTextContent());

                    WeatherValue weatherValue = WeatherValue.valueOf(category);
                    // 변환한 값이 몇번째인지 확인하려면 아래 주석 해제
//                    System.out.println(WeatherValue.valueOf(category).ordinal());

                    switch (weatherValue) {
                        case PTY:
                            weather.setPTY(value);
                            break;
                        case REH:
                            weather.setREH(value);
                            break;
                        case RN1:
                            weather.setRN1(value);
                            break;
                        case T1H:
                            weather.setT1H(value);
                            break;
                        case UUU:
                            weather.setUUU(value);
                            break;
                        case VEC:
                            weather.setVEC(value);
                            break;
                        case VVV:
                            weather.setVVV(value);
                            break;
                        case WSD:
                            weather.setWSD(value);
                            break;
                        default:
                            throw new XMLParseException();
                    }
                }
            }

            System.out.println(weather.toString());
        } catch (XMLParseException e) {
            e.printStackTrace();
        }
    }

}