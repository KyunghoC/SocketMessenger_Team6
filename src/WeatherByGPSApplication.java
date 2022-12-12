import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherByGPSApplication {
    public static void main(String[] args) {
        try{
            //�����û�� ������ �浵
            String lon = "126.977948";  //�浵
            String lat = "37.566386";   //����

            //OpenAPI call�ϴ� URL
            String urlstr = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0"
                        + "lat="+lat+"&lon="+lon
                        +"%2BHbLqfWwnHjL4AQ4O6wpgtXmNbxfxOu1eiwaO9%2FMCZyEYMfPqYzNG3oRlSg1dKEDE83p%2FvaQncWUZZA2GCI0hA%3D%3D";
            URL url = new URL(urlstr);
            BufferedReader bf;
            String line;
            String result="";

            //���� ������ �޾ƿ´�.
            bf = new BufferedReader(new InputStreamReader(url.openStream()));

            //���ۿ� �ִ� ������ ���ڿ��� ��ȯ.
            while((line=bf.readLine())!=null){
                result=result.concat(line);
                //System.out.println(line);
            }

            //���ڿ��� JSON���� �Ľ�
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(result);

            //���� ���
            System.out.println("���� : " + jsonObj.get("name"));

            //���� ���
            JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
            JSONObject obj = (JSONObject) weatherArray.get(0);
            System.out.println("���� : "+obj.get("main"));

            //�µ� ���(����µ��� ��ȯ �ʿ�)
            JSONObject mainArray = (JSONObject) jsonObj.get("main");
            double ktemp = Double.parseDouble(mainArray.get("temp").toString());
            double temp = ktemp-273.15;
            System.out.printf("�µ� : %.2f\n",temp);

            bf.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}