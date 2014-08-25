package be.simonraes.telemeter.domain;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

/**
 * Sends the SOAP request and receives the response XML.
 * Created by Simon Raes on 13/06/2014.
 */
public class TelemeterSoap extends AsyncTask<String, Void, String> {

    private Context context;
    private TelemeterSoapResponse delegate;

    private String username, password;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    private String envelope;

    private String telemeterResponse;

    public TelemeterSoap(Context context,TelemeterSoapResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {

        if (strings[0] != null && strings[1] != null) {
            username = strings[0];
            password = strings[1];
        } else {
            username = "x";
            password = "x";
        }
        envelope =
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
            telemeterResponse = inputStreamToString(response.getEntity().getContent());
        } catch (Exception e) {
            System.out.println("exception during download: " + e.getMessage());
        }
        return telemeterResponse;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        delegate.loadComplete(o);
    }

    //Converts the received inputStream to a string response
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

    //Sends the response to the delegate
    public interface TelemeterSoapResponse {
        public void loadComplete(String response);
    }
}
