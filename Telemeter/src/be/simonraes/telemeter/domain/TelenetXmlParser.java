package be.simonraes.telemeter.domain;

import android.os.AsyncTask;
import be.simonraes.telemeter.model.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Parses the received XML response and creates a TelemeterData object.
 * Created by Simon Raes on 11/06/2014.
 */
public class TelenetXmlParser extends AsyncTask<String, Void, TelemeterData> {

    private TelenetXmlResponse delegate;

    public TelenetXmlParser(TelenetXmlResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected TelemeterData doInBackground(String... strings) {

        String input;

        if (strings[0] != null) {
            input = strings[0];
        } else {
            input = "";
        }

        TelemeterData telemeterData = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            telemeterData = new TelemeterData();

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(input));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    //Ticket
                    if (parser.getName().equals("Ticket")) {
                        telemeterData.setTicket(readTicket(parser));
                    }

                    //FUP
                    if (parser.getName().equals("Period")) {
                        telemeterData.setPeriod(readPeriod(parser));
                    }
                    if (parser.getName().equals("Usage")) {
                        telemeterData.setUsage(readUsage(parser));
                    }

                    //Status
                    if (parser.getName().equals("Status")) {
                        telemeterData.setStatus(parser.nextText());
                    }
                    //StatusDescription
                    if (parser.getName().equals("StatusDescription")) {
                        telemeterData.setStatusDescription(readStatusDescription(parser));
                    }

                    //Volume
                    if (parser.getName().equals("Volume")) {
                        telemeterData.setVolume(readVolume(parser));
                    }

                    //Fault
                    if (parser.getName().equals("Fault")) {
                        telemeterData.setFault(readFault(parser));
                    }


                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }

        return telemeterData;
    }


    private Ticket readTicket(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Ticket");

        Date timestamp = null;
        Date expiryTimestamp = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Timestamp")) {
                timestamp = timeStampStringToDate(readText(parser));
            } else if (name.equals("ExpiryTimestamp")) {
                expiryTimestamp = timeStampStringToDate(readText(parser));
            } else {
                skip(parser);
            }
        }
        return new Ticket(timestamp, expiryTimestamp);
    }

    private Volume readVolume(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Volume");

        String unit = null;
        String limit = null;
        String totalUsage = null;
        ArrayList<DailyUsage> dailyUsages = new ArrayList<DailyUsage>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Unit")) {
                unit = readText(parser);
            } else if (name.equals("Limit")) {
                limit = readText(parser);
            } else if (name.equals("TotalUsage")) {
                totalUsage = readText(parser);
            } else if (name.equals("DailyUsage")) {
                dailyUsages.add(readDailyUsage(parser));
            } else {
                skip(parser);
            }
        }
        return new Volume(unit, limit, totalUsage, dailyUsages);
    }

    private DailyUsage readDailyUsage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "DailyUsage");

        Date day = null;
        String usage = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Day")) {
                day = timeStampStringToDate(readText(parser));
            } else if (name.equals("Usage")) {
                usage = readText(parser);
            } else {
                skip(parser);
            }
        }
        return new DailyUsage(day, usage);
    }

    private Period readPeriod(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Period");

        Date from = null;
        Date till = null;
        String currentDay = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("From")) {
                from = periodStringToDate(readFrom(parser));
            } else if (name.equals("Till")) {
                till = periodStringToDate(readText(parser));
            } else if (name.equals("CurrentDay")) {
                currentDay = readText(parser);
            } else {
                skip(parser);
            }
        }
        return new Period(from, till, currentDay);
    }

    private Usage readUsage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Usage");

        double totalUsage = 0;
        double minxUsageRemaining = 0;
        double maxUsageRemaining = 0;
        String unit = "";
        Date lastUpdate = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TotalUsage")) {
                totalUsage = Double.parseDouble(readText(parser));
            } else if (name.equals("MinUsageRemaining")) {
                minxUsageRemaining = Double.parseDouble(readText(parser));
            } else if (name.equals("MaxUsageRemaining")) {
                maxUsageRemaining = Double.parseDouble(readText(parser));
            } else if (name.equals("Unit")) {
                unit = readText(parser);
            } else if (name.equals("LastUpdate")) {
                lastUpdate = periodStringToDate(readText(parser));
            } else {
                skip(parser);
            }
        }
        return new Usage(totalUsage, minxUsageRemaining, maxUsageRemaining, unit, lastUpdate);
    }

    private StatusDescription readStatusDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "StatusDescription");
        String nl = null;
        String fr = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("NL")) {
                nl = readText(parser);
            } else if (name.equals("FR")) {
                fr = readText(parser);
            } else {
                skip(parser);
            }
        }
        return new StatusDescription(nl, fr);
    }

    private Fault readFault(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Fault");
        String faultcode = null;
        String faultstring = null;
        Detail detail = null;

        System.out.println("readFault");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("faultcode")) {
                faultcode = readText(parser);
            } else if (name.equals("faultstring")) {
                faultstring = readText(parser);
            } else if (name.equals("detail")) {
                detail = readDetail(parser);
            } else {
                skip(parser);
            }
        }
        return new Fault(faultcode, faultstring, detail);
    }

    private Detail readDetail(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "detail");
        Detail detail = null;

        System.out.println("readDetail");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            System.out.println(name);
            if (name.equals("TelemeterFault")) {
                detail = readDetailContent(parser);
            } else {
                skip(parser);
            }
        }
        return detail;
    }

    private Detail readDetailContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "TelemeterFault");
        String code = null;
        String description = null;

        System.out.println("readDetailContent");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Code")) {
                code = readText(parser);
            } else if (name.equals("Description")) {
                description = readText(parser);
            } else {
                skip(parser);
            }
        }
        System.out.println("read " + code + " " + description);
        return new Detail(code, description);
    }

    private String readFrom(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "From");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "From");
        return title;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    protected void onPostExecute(TelemeterData telemeterData) {
        super.onPostExecute(telemeterData);
        delegate.parseComplete(telemeterData);
    }

    private Date timeStampStringToDate(String timestamp) {
        SimpleDateFormat formatter, FORMATTER;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        try {
            date = formatter.parse(timestamp.substring(0, 23));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date periodStringToDate(String timestamp) {
        SimpleDateFormat formatter, FORMATTER;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(timestamp.substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public interface TelenetXmlResponse {
        public void parseComplete(TelemeterData response);
    }
}
