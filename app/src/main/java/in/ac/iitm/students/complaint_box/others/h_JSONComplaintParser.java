package in.ac.iitm.students.complaint_box.others;

import android.app.Activity;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import in.ac.iitm.students.complaint_box.objects.h_Complaint;
import in.ac.iitm.students.complaint_box.objects.h_PinRoom;
import in.ac.iitm.students.complaint_box.objects.h_PinWing;
import in.ac.iitm.students.complaint_box.objects.h_WashingMachine;
import in.ac.iitm.students.complaint_box.objects.h_Washroom;
import in.ac.iitm.students.complaint_box.objects.h_WaterDispenser;


/**
 * Created by DELL on 7/7/2017.
 */

public class h_JSONComplaintParser {

    Activity activity;
    private InputStream stream;
    private ArrayList<h_Complaint> hComplaintArray;
    private h_WashingMachine h_wm=new h_WashingMachine();
    private h_WaterDispenser h_wd=new h_WaterDispenser();
    private h_Washroom h_w=new h_Washroom();
    private h_PinRoom h_pr=new h_PinRoom();
    private h_PinWing h_pw=new h_PinWing();
    private String[][] arr;



    public h_JSONComplaintParser(String string, Activity activity) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        hComplaintArray = new ArrayList<>();
        this.activity = activity;
    }

    public void pleasePleaseParseMyData() throws IOException {

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.setLenient(true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //Log.d("Read complaints array", reader+"");
            readComplaintsArray(reader);
        } finally {

            reader.close();

        }

    }

    public void readComplaintsArray(JsonReader reader) throws IOException {
        //ArrayList<h_Complaint> hComplaints = new ArrayList<>();
        //Log.e("message",reader+"");
        //while ( reader.peek() == JsonToken.STRING) reader.nextString();
        reader.beginArray();

        while (reader.hasNext()) {

            hComplaintArray.add(readComplaint(reader));
            if(!(hComplaintArray.get(hComplaintArray.size()-1).isCustom()))
                hComplaintArray.remove(hComplaintArray.size()-1);
        }
        reader.endArray();
        //return hComplaints;

    }


    public h_Complaint readComplaint(JsonReader reader) throws IOException,NullPointerException {

        h_Complaint hComplaint = new h_Complaint();
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
                    if(hComplaint.getTitle().equals("Washing Machine")){
                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[0][0]=(h_wm.getT1details()[0][0]==null)?hComplaint.getProximity():h_wm.getT1details()[0][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[0][1]=(h_wm.getT1details()[0][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[0][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Check water error")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[1][0]=(h_wm.getT1details()[1][0]==null)?hComplaint.getProximity():h_wm.getT1details()[1][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[1][1]=(h_wm.getT1details()[1][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[1][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Water outlet problem")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[2][0]=(h_wm.getT1details()[2][0]==null)?hComplaint.getProximity():h_wm.getT1details()[2][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[2][1]=(h_wm.getT1details()[2][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[2][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Dryer not functional")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[3][0]=(h_wm.getT1details()[3][0]==null)?hComplaint.getProximity():h_wm.getT1details()[3][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[3][1]=(h_wm.getT1details()[3][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[3][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Not even starting wash cycle")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[4][0]=(h_wm.getT1details()[4][0]==null)?hComplaint.getProximity():h_wm.getT1details()[4][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[4][1]=(h_wm.getT1details()[4][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[4][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Others")){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.getT1details()[5][0]=(h_wm.getT1details()[5][0]==null)?hComplaint.getProximity():h_wm.getT1details()[5][0]+","+hComplaint.getProximity();
                            h_wm.getT1details()[5][1]=(h_wm.getT1details()[5][1]==null)?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[5][1])+1);
                        }
                    }
                    else if(hComplaint.getTitle().equals("Water Dispenser")){
                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.getT2details()[0][0]=(h_wd.getT2details()[0][0]==null)?hComplaint.getProximity():h_wd.getT2details()[0][0]+","+hComplaint.getProximity();
                            h_wd.getT2details()[0][1]=(h_wd.getT2details()[0][1]==null)?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[0][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Heating or cooling not working")){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.getT2details()[1][0]=(h_wd.getT2details()[1][0]==null)?hComplaint.getProximity():h_wd.getT2details()[1][0]+","+hComplaint.getProximity();
                            h_wd.getT2details()[1][1]=(h_wd.getT2details()[1][1]==null)?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[1][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Monkey drank from the dispenser")){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.getT2details()[2][0]=(h_wd.getT2details()[2][0]==null)?hComplaint.getProximity():h_wd.getT2details()[2][0]+","+hComplaint.getProximity();
                            h_wd.getT2details()[2][1]=(h_wd.getT2details()[2][1]==null)?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[2][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Algae is growing")){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.getT2details()[3][0]=(h_wd.getT2details()[3][0]==null)?hComplaint.getProximity():h_wd.getT2details()[3][0]+","+hComplaint.getProximity();
                            h_wd.getT2details()[3][1]=(h_wd.getT2details()[3][1]==null)?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[3][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Others")){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.getT2details()[4][0]=(h_wd.getT2details()[4][0]==null)?hComplaint.getProximity():h_wd.getT2details()[4][0]+","+hComplaint.getProximity();
                            h_wd.getT2details()[4][1]=(h_wd.getT2details()[4][1]==null)?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[4][1])+1);
                        }

                    }
                    else if(hComplaint.getTitle().equals("Washroom")){
                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[0][0]=(h_w.getT3details()[0][0]==null)?hComplaint.getProximity():h_w.getT3details()[0][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[0][1]=(h_w.getT3details()[0][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[0][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Taps not properly working")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[1][0]=(h_w.getT3details()[1][0]==null)?hComplaint.getProximity():h_w.getT3details()[1][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[1][1]=(h_w.getT3details()[1][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[1][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Showers not properly working")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[2][0]=(h_w.getT3details()[2][0]==null)?hComplaint.getProximity():h_w.getT3details()[2][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[2][1]=(h_w.getT3details()[2][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[2][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Towel Hangers not present")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[3][0]=(h_w.getT3details()[3][0]==null)?hComplaint.getProximity():h_w.getT3details()[3][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[3][1]=(h_w.getT3details()[3][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[3][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Washroom doors not closing properly")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[4][0]=(h_w.getT3details()[4][0]==null)?hComplaint.getProximity():h_w.getT3details()[4][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[4][1]=(h_w.getT3details()[4][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[4][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Flush tanks not working")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[5][0]=(h_w.getT3details()[5][0]==null)?hComplaint.getProximity():h_w.getT3details()[5][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[5][1]=(h_w.getT3details()[5][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[5][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Pipes leaking")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[6][0]=(h_w.getT3details()[6][0]==null)?hComplaint.getProximity():h_w.getT3details()[6][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[6][1]=(h_w.getT3details()[6][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[6][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Others")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.getT3details()[7][0]=(h_w.getT3details()[7][0]==null)?hComplaint.getProximity():h_w.getT3details()[7][0]+","+hComplaint.getProximity();
                            h_w.getT3details()[7][1]=(h_w.getT3details()[7][1]==null)?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[7][1])+1);
                        }
                    }
                    else if(hComplaint.getTitle().equals("Problems in your room")){

                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.getT4details()[0][0]=(h_pr.getT4details()[0][0]==null)?hComplaint.getProximity():h_pr.getT4details()[0][0]+","+hComplaint.getProximity();
                            h_pr.getT4details()[0][1]=(h_pr.getT4details()[0][1]==null)?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[0][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Heating or cooling not working")){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.getT4details()[1][0]=(h_pr.getT4details()[1][0]==null)?hComplaint.getProximity():h_pr.getT4details()[1][0]+","+hComplaint.getProximity();
                            h_pr.getT4details()[1][1]=(h_pr.getT4details()[1][1]==null)?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[1][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Monkey drank from the dispenser")){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.getT4details()[2][0]=(h_pr.getT4details()[2][0]==null)?hComplaint.getProximity():h_pr.getT4details()[2][0]+","+hComplaint.getProximity();
                            h_pr.getT4details()[2][1]=(h_pr.getT4details()[2][1]==null)?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[2][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Algae is growing")){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.getT4details()[3][0]=(h_pr.getT4details()[3][0]==null)?hComplaint.getProximity():h_pr.getT4details()[3][0]+","+hComplaint.getProximity();
                            h_pr.getT4details()[3][1]=(h_pr.getT4details()[3][1]==null)?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[3][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Others")){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.getT4details()[4][0]=(h_pr.getT4details()[4][0]==null)?hComplaint.getProximity():h_pr.getT4details()[4][0]+","+hComplaint.getProximity();
                            h_pr.getT4details()[4][1]=(h_pr.getT4details()[4][1]==null)?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[4][1])+1);
                        }

                    }
                    else if(hComplaint.getTitle().equals("Problems in your wing")){

                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[0][0]=(h_pw.getT5details()[0][0]==null)?hComplaint.getProximity():h_pw.getT5details()[0][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[0][1]=(h_pw.getT5details()[0][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[0][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Taps not properly working")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[1][0]=(h_pw.getT5details()[1][0]==null)?hComplaint.getProximity():h_pw.getT5details()[1][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[1][1]=(h_pw.getT5details()[1][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[1][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Showers not properly working")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[2][0]=(h_pw.getT5details()[2][0]==null)?hComplaint.getProximity():h_pw.getT5details()[2][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[2][1]=(h_pw.getT5details()[2][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[2][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Towel Hangers not present")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[3][0]=(h_pw.getT5details()[3][0]==null)?hComplaint.getProximity():h_pw.getT5details()[3][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[3][1]=(h_pw.getT5details()[3][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[3][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Washroom doors not closing properly")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[4][0]=(h_pw.getT5details()[4][0]==null)?hComplaint.getProximity():h_pw.getT5details()[4][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[4][1]=(h_pw.getT5details()[4][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[4][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Flush tanks not working")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[5][0]=(h_pw.getT5details()[5][0]==null)?hComplaint.getProximity():h_pw.getT5details()[5][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[5][1]=(h_pw.getT5details()[5][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[5][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Pipes leaking")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[6][0]=(h_pw.getT5details()[6][0]==null)?hComplaint.getProximity():h_pw.getT5details()[6][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[6][1]=(h_pw.getT5details()[6][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[6][1])+1);
                        }
                        if(hComplaint.getDescription().equals("Others")){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.getT5details()[7][0]=(h_pw.getT5details()[7][0]==null)?hComplaint.getProximity():h_pw.getT5details()[7][0]+","+hComplaint.getProximity();
                            h_pw.getT5details()[7][1]=(h_pw.getT5details()[7][1]==null)?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[7][1])+1);
                        }

                    }
                }
                else if(reader.nextString().equals("1")){
                    hComplaint.setCustom(true);
                }

            } else if (name.equals("error")) {
                //Log.e("error message",""+reader);
                reader.nextString();
                reader.endObject();

                return h_Complaint.getErrorComplaintObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return hComplaint;
    }

    public h_PinRoom getH_pr() {
        return h_pr;
    }

    public h_PinWing getH_pw() {
        return h_pw;
    }

    public ArrayList<h_Complaint> gethComplaintArray() {
        return hComplaintArray;
    }

    public h_WashingMachine getH_wm() {
        return h_wm;
    }

    public h_Washroom getH_w() {
        return h_w;
    }

    public h_WaterDispenser getH_wd() {
        return h_wd;
    }
}
