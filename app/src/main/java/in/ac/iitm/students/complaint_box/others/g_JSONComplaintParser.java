package in.ac.iitm.students.complaint_box.others;

import android.app.Activity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.objects.Complaint;

/**
 * Created by DELL on 7/7/2017.
 */

public class g_JSONComplaintParser {

    Activity activity;
    private InputStream stream;
    private ArrayList<Complaint> gComplaintArray;

    public g_JSONComplaintParser(String string, Activity activity) {
        this.stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        this.gComplaintArray = new ArrayList<>();
        this.activity = activity;
    }

    public ArrayList<Complaint> pleasePleaseParseMyData() throws IOException {

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.setLenient(true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Log.d("Read complaints array", reader + "");
            return readComplaintsArray(reader);
        } finally {
            reader.close();
        }

    }

    public ArrayList<Complaint> readComplaintsArray(JsonReader reader) throws IOException {
        ArrayList<Complaint> gComplaints = new ArrayList<>();
        Log.e("message", reader + "");
        reader.beginArray();

        while (reader.hasNext()) {
            gComplaints.add(readComplaint(reader));
        }
        reader.endArray();

        return gComplaints;
    }

    public Complaint readComplaint(JsonReader reader) throws IOException {

        Complaint hComplaint = new Complaint();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.d("bleek", name);
            if (name.equals("name")) {
                hComplaint.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                hComplaint.setRollNo(reader.nextString());
            } else if (name.equals("hostel")) {
                hComplaint.setHostel(reader.nextString());
            } else if (name.equals("title")) {
                hComplaint.setTitle(reader.nextString());
            } else if (name.equals("description")) {
                hComplaint.setDescription(reader.nextString());
            } else if (name.equals("upvotes")) {
                hComplaint.setUpvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("downvotes")) {
                hComplaint.setDownvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("uuid")) {
                hComplaint.setUid(reader.nextString());
            } else if (name.equals("datetime")) {
                hComplaint.setDate(reader.nextString());
            } else if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                hComplaint.setTag(reader.nextString());
            } else if (name.equals("comments")) {
                hComplaint.setComments(Integer.parseInt(reader.nextString()));
            } else if (name.equals("image_url") && reader.peek() != JsonToken.NULL) {
                hComplaint.setImageUrl(reader.nextString());
            } else if (name.equals("error")) {
                //Log.e("error message",""+reader);
                reader.nextString();
                reader.endObject();

                return Complaint.getErrorComplaintObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d("bleek", "radio silence");

        return hComplaint;
    }

}