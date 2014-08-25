package be.simonraes.telemeter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import be.simonraes.telemeter.model.TelemeterData;

import java.util.Calendar;
import java.util.Date;

/**
 * Controls the TelemeterData database table.
 * Created by Simon Raes on 22/08/2014.
 */
public class TelemeterDataDataSource {

    private Context context;
    private SQLiteDatabase database;
    private SqlHelper sqlHelper;
    private String[] matchesColumns = {
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_ID,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TIMESTAMP,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_EXPIRY_TIMESTAMP,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_FROM,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TILL,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_CURRENT_DAY,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TOTAL_USAGE,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_MIN_USAGE_REMAINING,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_MAX_USAGE_REMAINING,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_UNIT,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_LAST_UPDATE,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_NL,
            SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_FR,
    };

    public TelemeterDataDataSource(Context context) {
        this.context = context;
        sqlHelper = new SqlHelper(context);
    }

    public void open() throws SQLException {
        database = sqlHelper.getWritableDatabase();
    }

    public void close() {
        sqlHelper.close();
    }

    public void saveTelemeterData(TelemeterData data) {
        ContentValues values = new ContentValues();

        System.out.println("saving to db");

        open();
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TIMESTAMP, data.getTicket().getTimestamp().getTime());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_EXPIRY_TIMESTAMP, data.getTicket().getExpiryTimestamp().getTime());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_FROM, data.getPeriod().getFrom().getTime());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TILL, data.getPeriod().getTill().getTime());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_CURRENT_DAY, data.getPeriod().getCurrentDay());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TOTAL_USAGE, data.getUsage().getTotalUsage());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_MIN_USAGE_REMAINING, data.getUsage().getMinUsageRemaining());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_MAX_USAGE_REMAINING, data.getUsage().getMaxUsageRemaining());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_UNIT, data.getUsage().getUnit());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_LAST_UPDATE, data.getUsage().getLastUpdate().getTime());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS, data.getStatus());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_NL, data.getStatusDescription().getNl());
        values.put(SqlHelper.TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_FR, data.getStatusDescription().getNl());

        database.insert(SqlHelper.TABLE_TELEMETER_DATA, null, values);
        close();
    }

    public TelemeterData getLatestTelemeterData() {
        open();
        Cursor cursor =
                database.rawQuery("SELECT * FROM " + SqlHelper.TABLE_TELEMETER_DATA +
                        " ORDER BY " + SqlHelper.TABLE_TELEMETER_DATA_COLUMN_TIMESTAMP +" DESC "+
                        " LIMIT 1;", null);
        TelemeterData data = cursorToTelemeterData(cursor);
        cursor.close();
        close();
        return data;
    }

    private TelemeterData cursorToTelemeterData(Cursor cursor) {
        TelemeterData data = new TelemeterData();
        if(cursor!=null && cursor.getColumnCount()>0 && cursor.getCount() >0) {
            cursor.moveToFirst();

            Calendar cal = Calendar.getInstance();

            // Ticket
            cal.setTimeInMillis(Long.parseLong(cursor.getString(1)));
            data.getTicket().setTimestamp(cal.getTime());

            cal.setTimeInMillis(Long.parseLong(cursor.getString(2)));
            data.getTicket().setExpiryTimestamp(cal.getTime());

            // Period
            cal.setTimeInMillis(Long.parseLong(cursor.getString(3)));
            data.getPeriod().setFrom(cal.getTime());

            cal.setTimeInMillis(Long.parseLong(cursor.getString(4)));
            data.getPeriod().setTill(cal.getTime());

            data.getPeriod().setCurrentDay(cursor.getString(5));

            // Usage
            data.getUsage().setTotalUsage(Double.parseDouble(cursor.getString(6)));
            data.getUsage().setMinUsageRemaining(Double.parseDouble(cursor.getString(7)));
            data.getUsage().setMaxUsageRemaining(Double.parseDouble(cursor.getString(8)));
            data.getUsage().setUnit(cursor.getString(9));

            cal.setTimeInMillis(Long.parseLong(cursor.getString(10)));
            data.getUsage().setLastUpdate((cal.getTime()));

            // Status
            data.setStatus(cursor.getString(11));
            data.getStatusDescription().setNl(cursor.getString(12));
            data.getStatusDescription().setFr(cursor.getString(13));
        }
        return data;
    }
}
