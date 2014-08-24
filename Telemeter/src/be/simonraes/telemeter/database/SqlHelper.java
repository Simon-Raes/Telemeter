package be.simonraes.telemeter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon Raes on 19/08/2014.
 */
public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "be.simonraes.telementer.database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TELEMETER_DATA = "telemeter_data";
    public static final String TABLE_TELEMETER_DATA_COLUMN_ID = "id";
    public static final String TABLE_TELEMETER_DATA_COLUMN_TIMESTAMP = "timestamp";
    public static final String TABLE_TELEMETER_DATA_COLUMN_EXPIRY_TIMESTAMP = "expiry_timestamp";
    public static final String TABLE_TELEMETER_DATA_COLUMN_FROM = "from_time"; // "From" is a reserved sql keyword.
    public static final String TABLE_TELEMETER_DATA_COLUMN_TILL = "till_time";
    public static final String TABLE_TELEMETER_DATA_COLUMN_CURRENT_DAY = "current_day";
    public static final String TABLE_TELEMETER_DATA_COLUMN_TOTAL_USAGE = "total_usage";
    public static final String TABLE_TELEMETER_DATA_COLUMN_MIN_USAGE_REMAINING = "min_usage_remaining";
    public static final String TABLE_TELEMETER_DATA_COLUMN_MAX_USAGE_REMAINING = "max_usage_remaining";
    public static final String TABLE_TELEMETER_DATA_COLUMN_UNIT = "unit";
    public static final String TABLE_TELEMETER_DATA_COLUMN_LAST_UPDATE = "last_update";
    public static final String TABLE_TELEMETER_DATA_COLUMN_STATUS = "status";
    public static final String TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_NL = "status_description_nl";
    public static final String TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_FR = "status_description_fr";

    private static final String CREATE_TABLE_TELEMETER_DATA =
            "create table IF NOT EXISTS " + TABLE_TELEMETER_DATA + " ( " +
                    TABLE_TELEMETER_DATA_COLUMN_ID + " integer primary key autoincrement, " +
                    TABLE_TELEMETER_DATA_COLUMN_TIMESTAMP + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_EXPIRY_TIMESTAMP + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_FROM + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_TILL + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_CURRENT_DAY + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_TOTAL_USAGE + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_MIN_USAGE_REMAINING + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_MAX_USAGE_REMAINING + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_UNIT + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_LAST_UPDATE + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_STATUS + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_NL + " text, " +
                    TABLE_TELEMETER_DATA_COLUMN_STATUS_DESCRIPTION_FR + " text);";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TELEMETER_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
