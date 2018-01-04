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

import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.objects.h_PinRoom;
import in.ac.iitm.students.complaint_box.objects.h_PinWing;
import in.ac.iitm.students.complaint_box.objects.h_WashingMachine;
import in.ac.iitm.students.complaint_box.objects.h_Washroom;
import in.ac.iitm.students.complaint_box.objects.h_WaterDispenser;

/**
 * Created by DELL on 7/7/2017.
 */

public class h_JSONMyComplaintsParser {

    Activity activity;
    private InputStream stream;
    //private ArrayList<h_Complaint> hComplaintArray;


    private ArrayList<Complaint> hComplaintArray;

    public h_JSONMyComplaintsParser(String string, Activity activity) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        hComplaintArray = new ArrayList<>();

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
        //ArrayList<h_Complaint> hComplaints = new ArrayList<>();
        //Log.e("message",reader+"");
        //while ( reader.peek() == JsonToken.STRING) reader.nextString();
        reader.beginArray();

        while (reader.hasNext()) {

            Complaint hComplaint = readComplaint(reader);
            hComplaintArray.add(hComplaint);

            //if(hComplaintArray.size()!=0 && !(hComplaintArray.get(hComplaintArray.size()-1).isCustom()))
            //    hComplaintArray.remove(hComplaintArray.size()-1);
        }
        reader.endArray();
        return hComplaintArray;

    }

    /**
     * IMP
     * Always keep custom as the last column in the table. The parsing assumes so.
     * IMP
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws NullPointerException
     */

    public Complaint readComplaint(JsonReader reader) throws IOException {

        Complaint hComplaint = new Complaint();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                hComplaint.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                hComplaint.setRollNo(reader.nextString());
            } else if (name.equals("roomno")) {
                hComplaint.setRoomNo(reader.nextString());
            } else if (name.equals("title")) {
                hComplaint.setTitle(reader.nextString());
            } else if (name.equals("proximity")) {
                hComplaint.setProximity(reader.nextString());
            } else if (name.equals("description")) {
                hComplaint.setDescription(reader.nextString());
            } else if (name.equals("upvotes")) {
                hComplaint.setUpvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("downvotes")) {
                hComplaint.setDownvotes(Integer.parseInt(reader.nextString()));
            } else if (name.equals("resolved")) {
                boolean resolved = false;
                if (reader.nextString().equals("1")) resolved = true;
                hComplaint.setResolved(resolved);
            } else if (name.equals("uuid")) {
                hComplaint.setUid(reader.nextString());
            } else if (name.equals("datetime")) {
                hComplaint.setDate(reader.nextString());
            } else if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                hComplaint.setTag(reader.nextString());
            } else if (name.equals("comments")) {
                hComplaint.setComments(Integer.parseInt(reader.nextString()));
            } else if (name.equals("more_rooms") && reader.peek() != JsonToken.NULL) {
                hComplaint.setMoreRooms(reader.nextString());
            } else if (name.equals("image_url") && reader.peek() != JsonToken.NULL) {
                hComplaint.setImageUrl(reader.nextString());
            } else if (name.equals("custom")) {
                if (reader.nextString().equals("0")) {
                    hComplaint.setCustom(false);

                } else {
                    hComplaint.setCustom(true);
                }

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
        return hComplaint;
    }


    public ArrayList<Complaint> gethComplaintArray() {
        return hComplaintArray;
    }


}
