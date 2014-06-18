package be.simonraes.telemeter.code;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;


/**
 * Created by Simon Raes on 8/06/2014.
 */
public class ServiceSmall {

    String username = "x";
    String password = "x";

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    String envelope =
            "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tel=\"http://www.telenet.be/TelemeterService/\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<tel:RetrieveUsageRequest>" +
                    "<UserId>" + username + "</UserId>" +
                    "<Password>" + password + "</Password>" +
                    "</tel:RetrieveUsageRequest>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";


    public void getTelenetResponse() {
        try {
            dataOutputStream.writeBytes(envelope);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://t4t.services.telenet.be/TelemeterService");
        httppost.addHeader("Content-Type", "text/xml");

        ByteArrayInputStream content = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(content);

        httppost.setEntity(entity);
        try {
            HttpResponse response = httpclient.execute(httppost);
            String stringReponse = inputStreamToString(response.getEntity().getContent());
            System.out.println("resp00000000000000onse: " + stringReponse);

            parseXML(stringReponse);

            /*
            Response received for FUP user:

            *   <?xml version='1.0' encoding='UTF-8'?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:RetrieveUsageResponse xmlns:ns2="http://www.telenet.be/TelemeterService/">
                            <Ticket>
                                <Timestamp>2014-06-08T21:41:39.059+02:00</Timestamp>
                                <ExpiryTimestamp>2014-06-08T22:46:59.059+02:00</ExpiryTimestamp>
                            </Ticket>
                            <FUP>
                                <Period>
                                    <From>2014-05-16+02:00</From>
                                    <Till>2014-06-15+02:00</Till>
                                    <CurrentDay>24</CurrentDay>
                                </Period>
                                <Usage>
                                    <TotalUsage>151.72</TotalUsage>
                                    <MinUsageRemaining>344.23</MinUsageRemaining>
                                    <MaxUsageRemaining>1114.3</MaxUsageRemaining>
                                    <Unit>GB</Unit>
                                    <LastUpdate>2014-06-08T21:30:49.000+02:00</LastUpdate>
                                </Usage>
                                <Status>Vrij verbruik</Status>
                                <StatusDescription>
                                    <NL>Je surft met volledige surfsnelheid.</NL>
                                    <FR>Vous surfez à vitesse normale.</FR>
                                </StatusDescription>
                            </FUP>
                        </ns2:RetrieveUsageResponse>
                    </S:Body>
                </S:Envelope>
            * */

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }


    public void parseXML(String input) {
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(input));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag " + xpp.getName());
                    if (xpp.getName().trim().equals("CurrentDay")) {
                        System.out.println("totes day!!");
                    }
                    if (xpp.getName().equals("TotalUsage")) {
                        System.out.println("FOUND USAGEFOUND USAGEFOUND USAGE " + xpp.getText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    System.out.println("End tag " + xpp.getName());
                } else if (eventType == XmlPullParser.TEXT) {
                    System.out.println("Text " + xpp.getText());
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }

    private String inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return full string
        return total.toString();
    }
}
