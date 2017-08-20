package in.ac.iitm.students.others;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created by DELL on 7/28/2017.
 */

public class InstiCalendar {
    public ArrayList<ArrayList<Calendar_Event>> cal_events;
    static long CalID=-1;
    private Context context;
    private String cal_ver = "0";

    public InstiCalendar(Context context) {
        this.context = context;
    }

    public static ArrayList<ArrayList<Calendar_Event>> readMonthObject(JsonReader reader, Context context, int mode) throws IOException {

        ArrayList<ArrayList<Calendar_Event>> cal_events = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("january")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 0, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("february")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 1, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("march")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 2, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("april")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 3, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("may")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 4, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("june")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 5, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("july")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 6, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("august")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 7, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("september")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 8, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("october")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 9, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("november")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 10, context, mode);
                cal_events.add(eventList);

            } else if (name.equals("december")) {
                ArrayList<Calendar_Event> eventList = readMonthArray(reader, 11, context, mode);
                cal_events.add(eventList);

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        return cal_events;

    }

    private static Calendar_Event readDayObject(JsonReader reader, int month, int i, Context context, int mode) throws IOException {

        Calendar_Event event = new Calendar_Event();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("date")) {
                event.setDate(Integer.parseInt(reader.nextString()));

            } else if (name.equals("day")) {
                event.setDay(reader.nextString());

            } else if (name.equals("details")) {
                event.setDetails(reader.nextString());

            } else if (name.equals("holiday")) {
                event.setHoliday(reader.nextString().equals("TRUE"));

            } else if (name.equals("remind")) {
                event.setRemind(reader.nextString().equals("TRUE"));

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        event.setMonth(month);
        //eventArrayList.add(event);


        if (mode == 0 && event.getDetails().length() > 0 && !exists(event, context)) {
            insertEvents(CalID, event, context);
        }
        return event;
    }

    private static ArrayList<Calendar_Event> readMonthArray(JsonReader reader, int month, Context context, int mode) throws IOException {

        ArrayList<Calendar_Event> eventList = new ArrayList<>();
        reader.beginArray();
        int i = 0;
        while (reader.hasNext()) {
            Calendar_Event event = readDayObject(reader, month, i, context, mode);
            eventList.add(event);
            i++;
        }
        reader.endArray();
        return eventList;
    }

    private static void sendJsonRequest(final Context context, final int mode) {


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
                        readMonthObject(reader, context, mode);
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
                Toast.makeText(context,"No Internet Access",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private static void insertEvents(long calId, Calendar_Event event, Context context) {

        Calendar cal = new GregorianCalendar(2017, event.getMonth() , event.getDate());    //Jan is 0
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
        values.put(CalendarContract.Events.CUSTOM_APP_PACKAGE, context.getPackageName());
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

            String[] proj = new String[]{CalendarContract.Events.TITLE};
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
        Toast.makeText(context,"IITM Calendar created",Toast.LENGTH_SHORT).show();
        return getCalendarId(acc, context);
    }

    public void fetchCalData(int mode) {


        String acc = "students.iitm";
        String disp = "IITM Calendar";
        String inter = "IITM Calendar";

        //CalID = Utils.getprefLong("CalID", context);


        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor calCursor =
                context.getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                if (displayName.equals("IITM Calendar")) {
                    CalID = id;
                    break;
                }
                CalID = -1;

            } while (calCursor.moveToNext());
        }

        // mode 0 for updating the calendar repo from the server
        // mode 1 for getting the event arrayList for populating the calendar recyclerView
        if ((mode == 0 && !getVersion().equalsIgnoreCase(Utils.getprefString("Cal_Ver", context))) || CalID==-1) {

            if (CalID == -1) {
                CalID = insertCalendar(acc, inter, disp, context);
                Log.i("CalID", CalID + "");
                //      Utils.saveprefLong("CalID", CalID, context);
            }
            /************************************************************************************/


            Toast.makeText(context, "Updating Calendar", Toast.LENGTH_SHORT).show();
            deleteallevents();
            sendJsonRequest(context, mode);
            Utils.saveprefString("Cal_Ver", getVersion(), context);
        } else {
            mode=1;
            sendJsonRequest(context, mode);
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


        ContentResolver cr = context.getContentResolver();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = cr
                .query(CalendarContract.Events.CONTENT_URI,
                        new String[]{CalendarContract.Events._ID,CalendarContract.Calendars._ID,CalendarContract.Events.CALENDAR_DISPLAY_NAME,CalendarContract.Events.CUSTOM_APP_PACKAGE},
                        null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            // it might be also better to check CALENDAR_ID here
            if(CalID==Integer.parseInt(cursor.getString(1)) || cursor.getString(2).equals("IITM Calendar")){
                Uri deleteUri = null;
                long eventID = Integer.parseInt(cursor.getString(0));
                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                int rows = context.getContentResolver().delete(deleteUri, null, null);
                //Log.i(DEBUG_TAG, "Rows deleted: " + rows);
            }
            cursor.moveToNext();
        }

    }
}
