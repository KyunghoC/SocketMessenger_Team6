import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.management.modelmbean.XMLParseException;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class weatherPanel extends JFrame{
	
	// �г� ����
    Panel p;
    // ���̺� ����
    Label label1, label2, label3, label4, label5;
    
    enum WeatherValue {
        PTY, REH, RN1, T1H, UUU, VEC, VVV, WSD
    }
    
    // ���̺� ������
    public  weatherPanel(String str) throws IOException, ParserConfigurationException, SAXException 
    {   
    	
    	// �Է¹��� weather ��ü
        Weather weather = new Weather();

        // ���� ����
        String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String authKey = "%2BHbLqfWwnHjL4AQ4O6wpgtXmNbxfxOu1eiwaO9%2FMCZyEYMfPqYzNG3oRlSg1dKEDE83p%2FvaQncWUZZA2GCI0hA%3D%3D"; // ���� Service Ű ���

        String nx = "63"; //������ ������
        String ny = "124"; //������ ������
        String baseDate = ""; //��¥
        String baseTime = ""; //�ð�
        
        //���� ��¥
        LocalDate date = LocalDate.now();
    	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");//baseDate ���Ŀ� �°�
    	baseDate = date.format(formatter1);
    	
    	//����ð� 
    	LocalTime time = LocalTime.now();
    	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH");
    	baseTime=time.format(formatter2)+"00"; //baseTime ���Ŀ� �°�

        StringBuilder urlBuilder = new StringBuilder(apiURL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + authKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows=10", "UTF-8"));    // ���� ǥ
        urlBuilder.append("&" + URLEncoder.encode("pageNo=1", "UTF-8"));    // ������ ��
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* ��ȸ�ϰ���� ��¥*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* ��ȸ�ϰ���� �ð� AM 02�ú��� 3�ð� ���� */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //�浵
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //����

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

        // ���ڿ� Document �� �����ؼ� List ���·� �����ͼ� ��ü�� �Ľ���.
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
                // ���� Element Ȯ���Ϸ��� �Ʒ� �ּ� ����
//                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String category = eElement.getElementsByTagName("category").item(0).getTextContent();
                    double value = Double.parseDouble(eElement.getElementsByTagName("obsrValue").item(0).getTextContent());

                    WeatherValue weatherValue = WeatherValue.valueOf(category);
                    // ��ȯ�� ���� ���°���� Ȯ���Ϸ��� �Ʒ� �ּ� ����
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
    	
        //super(str);
        // �г� ����
        p = new Panel();
        // ���̺� ����
        label1=new Label(weather.toStringdate());
        label2=new Label(weather.toStringPTY());
        label3=new Label(weather.toStringT1H());
        label4=new Label(weather.toStringREH());
        label5=new Label(weather.toStringRN1());
        
        // ���̺� �� ����
        //label1.setBackground(Color.pink);
        //label2.setBackground(Color.pink);
        
        p.setLayout(new GridLayout(5,1));
        
        p.add(label1);
        p.add(label2);
        p.add(label3);
        p.add(label4);
        p.add(label5);
        add(p);
        setSize(458,200);
        setVisible(true);    
    }
    
    
	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
	      new weatherPanel("����");
	   }

}


