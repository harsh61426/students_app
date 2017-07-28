package in.ac.iitm.students.others;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import in.ac.iitm.students.fragments.monthFragment;
import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created by DELL on 7/28/2017.
 */

public class InstiCalendar {
    static long CalID;
    private Context context;
    private String cal_ver = "0";

    public InstiCalendar(Context context) {
        this.context = context;
    }

    private static void readMonthObject(JsonReader reader, Context context) throws IOException {

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("january")) {
                readMonthArray(reader, 0, context);

            } else if (name.equals("february")) {
                readMonthArray(reader, 1, context);

            } else if (name.equals("march")) {
                readMonthArray(reader, 2, context);

            } else if (name.equals("april")) {
                readMonthArray(reader, 3, context);

            } else if (name.equals("may")) {
                readMonthArray(reader, 4, context);

            } else if (name.equals("june")) {
                readMonthArray(reader, 5, context);

            } else if (name.equals("july")) {
                readMonthArray(reader, 6, context);

            } else if (name.equals("august")) {
                readMonthArray(reader, 7, context);

            } else if (name.equals("september")) {
                readMonthArray(reader, 8, context);

            } else if (name.equals("october")) {
                readMonthArray(reader, 9, context);

            } else if (name.equals("november")) {
                readMonthArray(reader, 10, context);

            } else if (name.equals("december")) {
                readMonthArray(reader, 11, context);

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
    }

    private static void readDayObject(JsonReader reader, int month, int i, Context context) throws IOException {

        Calendar_Event event = new Calendar_Event();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("date")) {
                event.setDate(Integer.parseInt(reader.nextString()));
                monthFragment.date[month - 6][i] = String.valueOf(event.getDate());

            } else if (name.equals("day")) {
                event.setDay(reader.nextString());
                monthFragment.day[month - 6][i] = event.getDay();

            } else if (name.equals("details")) {
                event.setDetails(reader.nextString());
                monthFragment.desc[month - 6][i] = event.getDetails();

            } else if (name.equals("holiday")) {
                event.setHoliday(reader.nextString().equals("TRUE"));
                if (event.isHoliday()) {
                    monthFragment.holiday[month - 6][i] = "TRUE";
                } else {
                    monthFragment.holiday[month - 6][i] = "FALSE";
                }

            } else if (name.equals("remind")) {
                event.setRemind(reader.nextString().equals("TRUE"));

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        event.setMonth(month);
        if (event.getDetails().length() > 0 && !exists(event, context)) {
            insertEvents(CalID, event, context);
        }
    }

    private static void readMonthArray(JsonReader reader, int month, Context context) throws IOException {

        reader.beginArray();
        int i = 0;
        while (reader.hasNext()) {
            readDayObject(reader, month, i, context);
            i++;
        }
        reader.endArray();
    }

    private static void sendJsonRequest(final Context context) {
        String urlForCalendarData = "https://students.iitm.ac.in/studentsapp/calendar/calendar_php.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForCalendarData, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("kaka", response);
                InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));

                JsonReader reader = null;
                try {
                    reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                    reader.setLenient(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        readMonthObject(reader, context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private static void insertEvents(long calId, Calendar_Event event, Context context) {

        Calendar cal = new GregorianCalendar(2017, event.getMonth() - 1, event.getDate());    //Jan is 0
        cal.setTimeZone(TimeZone.getTimeZone("IST"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long start = cal.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.TITLE, event.getDetails());
        values.put(CalendarContract.Events.EVENT_LOCATION, "Chennai");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
        values.put(CalendarContract.Events.DESCRIPTION, event.isHoliday() ? "Holiday" : "");
// reasonable defaults exist:
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri =
                    context.getContentResolver().
                            insert(CalendarContract.Events.CONTENT_URI, values);
            long eventId = new Long(uri.getLastPathSegment());
            Log.i("EventID", eventId + "");
        }
    }

    private static boolean exists(Calendar_Event event, Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            String[] proj =
                    new String[]{CalendarContract.Events.TITLE};
            Calendar cal = new GregorianCalendar(2017, event.getMonth(), event.getDate());
            cal.setTimeZone(TimeZone.getTimeZone("IST"));
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long start = cal.getTimeInMillis();

            Cursor cursor = context.getContentResolver().query(
                    CalendarContract.Events.CONTENT_URI,
                    proj,
                    CalendarContract.Events.TITLE + " = ? AND " + CalendarContract.Events.DTSTART + " = ?",
                    new String[]{event.getDetails(), Long.toString(start)},
                    null);
            return cursor.moveToFirst();
        }
        return false;
    }

    private static long getCalendarId(String acc, Context context) {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ?";
        String[] selectionArgs = new String[]{acc};
// Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            String[] EVENT_PROJECTION = new String[]{
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                    CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
            };

            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            if (cur.moveToFirst()) {
                return cur.getLong(0);
            }
            Log.i("CurCount", cur.getCount() + "");
        }
        return -1;
    }

    private static long insertCalendar(String acc, String inter, String disp, Context context) {

        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, acc);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, inter);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, disp);
        //cv.put(CalendarContract.Calendars.CALENDAR_COLOR, yourColor);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, acc)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri result = context.getContentResolver().insert(calUri, cv);
        Log.i("Result", result.toString());
        return getCalendarId(acc, context);
    }

    public void fetchCalData() {
        String acc = "students.iitm";
        String disp = "IITM Calendar";
        String inter = "IITM Calendar";

        CalID = Utils.getprefLong("CalID", context);
        if (CalID == -1) {
            CalID = insertCalendar(acc, inter, disp, context);
            Log.i("CalID", CalID + "");
            Utils.saveprefLong("CalID", CalID, context);
        }
        Utils.saveprefString("Cal_Ver", "0", context);
        if (!getVersion().equalsIgnoreCase(Utils.getprefString("Cal_Ver", context))) {
            deleteallevents();
            sendJsonRequest(context);

            Utils.saveprefString("Cal_Ver", getVersion(), context);
        }
    }

    private String getVersion() {
        String calversion_url = "https://students.iitm.ac.in/studentsapp/calendar/cal_ver.php"; //url of api file

        StringRequest stringRequest = new StringRequest(Request.Method.GET, calversion_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsArray = new JSONArray(response);
                            JSONObject jsObject = jsArray.getJSONObject(0);
                            cal_ver = jsObject.getString("version");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                //Snackbar snackbar = Snackbar.make("Internet Connection Failed.", Snackbar.LENGTH_SHORT);
                //snackbar.show();

            }
        }) {
        };
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        return cal_ver;
    }

    private void deleteallevents() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.getContentResolver().delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + "= *", null);
    }
}
