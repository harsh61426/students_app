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

public class h_JSONComplaintParser {

    Activity activity;
    private InputStream stream;
    //private ArrayList<h_Complaint> hComplaintArray;
    private h_WashingMachine h_wm;
    private h_WaterDispenser h_wd;
    private h_Washroom h_w;
    private h_PinRoom h_pr;
    private h_PinWing h_pw;
    private String[][] arr;


    private ArrayList<Complaint> hComplaintArray;

    public h_JSONComplaintParser(String string, Activity activity) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        hComplaintArray = new ArrayList<>();
        h_wm=new h_WashingMachine();
        h_wd=new h_WaterDispenser();
        h_w=new h_Washroom();
        h_pr=new h_PinRoom();
        h_pw=new h_PinWing();
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
            if (hComplaint.isCustom()) hComplaintArray.add(hComplaint);

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
            } else if (name.equals("image_url") && reader.peek() != JsonToken.NULL) {
                hComplaint.setImageUrl(reader.nextString());
            } else if (name.equals("custom")) {
                if (reader.nextString().equals("0")) {
                    hComplaint.setCustom(false);
                    if(hComplaint.getTitle().equals("Washing Machine")){
                        if(hComplaint.getDescription().equals("Power supply not proper")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[0][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[0][0],hComplaint.getProximity())?h_wm.getT1details()[0][0]:h_wm.getT1details()[0][0]+","+hComplaint.getProximity(),0,0);
                            h_wm.setT1details((h_wm.getT1details()[0][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[0][1])+1),0,1);
                        }
                        if(hComplaint.getDescription().equals("Check water error")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[1][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[1][0],hComplaint.getProximity())?h_wm.getT1details()[1][0]:h_wm.getT1details()[1][0]+","+hComplaint.getProximity(),1,0);
                            h_wm.setT1details((h_wm.getT1details()[1][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[1][1])+1),1,1);
                        }
                        if(hComplaint.getDescription().equals("Water outlet problem")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[2][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[2][0],hComplaint.getProximity())?h_wm.getT1details()[2][0]:h_wm.getT1details()[2][0]+","+hComplaint.getProximity(),2,0);
                            h_wm.setT1details((h_wm.getT1details()[2][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[2][1])+1),2,1);
                        }
                        if(hComplaint.getDescription().equals("Dryer not functional")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[3][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[3][0],hComplaint.getProximity())?h_wm.getT1details()[3][0]:h_wm.getT1details()[3][0]+","+hComplaint.getProximity(),3,0);
                            h_wm.setT1details((h_wm.getT1details()[3][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[3][1])+1),3,1);
                        }
                        if(hComplaint.getDescription().equals("Not even starting wash cycle")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[4][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[4][0],hComplaint.getProximity())?h_wm.getT1details()[4][0]:h_wm.getT1details()[4][0]+","+hComplaint.getProximity(),4,0);
                            h_wm.setT1details((h_wm.getT1details()[4][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[4][1])+1),4,1);
                        }
                        if(hComplaint.getDescription().equals("Others")&&!hComplaint.isResolved()){
                            h_wm.setComments(hComplaint.getComments());
                            h_wm.setImageUrl(hComplaint.getImageUrl());
                            h_wm.setT1details((h_wm.getT1details()[5][0].equals(""))?hComplaint.getProximity():isPresent(h_wm.getT1details()[5][0],hComplaint.getProximity())?h_wm.getT1details()[5][0]:h_wm.getT1details()[5][0]+","+hComplaint.getProximity(),5,0);
                            h_wm.setT1details((h_wm.getT1details()[5][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wm.getT1details()[5][1])+1),5,1);
                        }
                    } else if(hComplaint.getTitle().equals("Water Dispenser")){
                        if(hComplaint.getDescription().equals("Power supply not proper")&&!hComplaint.isResolved()){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.setT2details((h_wd.getT2details()[0][0].equals(""))?hComplaint.getProximity():isPresent(h_wd.getT2details()[0][0],hComplaint.getProximity())?h_wd.getT2details()[0][0]:h_wd.getT2details()[0][0]+","+hComplaint.getProximity(),0,0);
                            h_wd.setT2details((h_wd.getT2details()[0][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[0][1])+1),0,1);
                        }
                        if(hComplaint.getDescription().equals("Heating or cooling not working")&&!hComplaint.isResolved()){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.setT2details((h_wd.getT2details()[1][0].equals(""))?hComplaint.getProximity():isPresent(h_wd.getT2details()[1][0],hComplaint.getProximity())?h_wd.getT2details()[1][0]:h_wd.getT2details()[1][0]+","+hComplaint.getProximity(),1,0);
                            h_wd.setT2details((h_wd.getT2details()[1][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[1][1])+1),1,1);
                        }
                        if(hComplaint.getDescription().equals("Monkey drank from the dispenser")&&!hComplaint.isResolved()){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.setT2details((h_wd.getT2details()[2][0].equals(""))?hComplaint.getProximity():isPresent(h_wd.getT2details()[2][0],hComplaint.getProximity())?h_wd.getT2details()[2][0]:h_wd.getT2details()[2][0]+","+hComplaint.getProximity(),2,0);
                            h_wd.setT2details((h_wd.getT2details()[2][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[2][1])+1),2,1);
                        }
                        if(hComplaint.getDescription().equals("Algae is growing")&&!hComplaint.isResolved()){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.setT2details((h_wd.getT2details()[3][0].equals(""))?hComplaint.getProximity():isPresent(h_wd.getT2details()[3][0],hComplaint.getProximity())?h_wd.getT2details()[3][0]:h_wd.getT2details()[3][0]+","+hComplaint.getProximity(),3,0);
                            h_wd.setT2details((h_wd.getT2details()[3][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[3][1])+1),3,1);
                        }
                        if(hComplaint.getDescription().equals("Others")&&!hComplaint.isResolved()){
                            h_wd.setComments(hComplaint.getComments());
                            h_wd.setImageUrl(hComplaint.getImageUrl());
                            h_wd.setT2details((h_wd.getT2details()[4][0].equals(""))?hComplaint.getProximity():isPresent(h_wd.getT2details()[4][0],hComplaint.getProximity())?h_wd.getT2details()[4][0]:h_wd.getT2details()[4][0]+","+hComplaint.getProximity(),4,0);
                            h_wd.setT2details((h_wd.getT2details()[4][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_wd.getT2details()[4][1])+1),4,1);
                        }

                    } else if(hComplaint.getTitle().equals("Washroom")){
                        if(hComplaint.getDescription().equals("Power supply not proper")){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[0][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[0][0],hComplaint.getProximity())?h_w.getT3details()[0][0]:h_w.getT3details()[0][0]+","+hComplaint.getProximity(),0,0);
                            h_w.setT3details((h_w.getT3details()[0][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[0][1])+1),0,1);
                        }
                        if(hComplaint.getDescription().equals("Taps not properly working")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[1][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[1][0],hComplaint.getProximity())?h_w.getT3details()[1][0]:h_w.getT3details()[1][0]+","+hComplaint.getProximity(),1,0);
                            h_w.setT3details((h_w.getT3details()[1][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[1][1])+1),1,1);
                        }
                        if(hComplaint.getDescription().equals("Showers not properly working")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[2][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[2][0],hComplaint.getProximity())?h_w.getT3details()[2][0]:h_w.getT3details()[2][0]+","+hComplaint.getProximity(),2,0);
                            h_w.setT3details((h_w.getT3details()[2][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[2][1])+1),2,1);
                        }
                        if(hComplaint.getDescription().equals("Towel Hangers not present")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[3][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[3][0],hComplaint.getProximity())?h_w.getT3details()[3][0]:h_w.getT3details()[3][0]+","+hComplaint.getProximity(),3,0);
                            h_w.setT3details((h_w.getT3details()[3][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[3][1])+1),3,1);
                        }
                        if(hComplaint.getDescription().equals("Washroom doors not closing properly")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[4][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[4][0],hComplaint.getProximity())?h_w.getT3details()[4][0]:h_w.getT3details()[4][0]+","+hComplaint.getProximity(),4,0);
                            h_w.setT3details((h_w.getT3details()[4][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[4][1])+1),4,1);
                        }
                        if(hComplaint.getDescription().equals("Flush tanks not working")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[5][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[5][0],hComplaint.getProximity())?h_w.getT3details()[5][0]:h_w.getT3details()[5][0]+","+hComplaint.getProximity(),5,0);
                            h_w.setT3details((h_w.getT3details()[5][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[5][1])+1),5,1);
                        }
                        if(hComplaint.getDescription().equals("Pipes leaking")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[6][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[6][0],hComplaint.getProximity())?h_w.getT3details()[6][0]:h_w.getT3details()[6][0]+","+hComplaint.getProximity(),6,0);
                            h_w.setT3details((h_w.getT3details()[6][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[6][1])+1),6,1);
                        }
                        if(hComplaint.getDescription().equals("Others")&&!hComplaint.isResolved()){
                            h_w.setComments(hComplaint.getComments());
                            h_w.setImageUrl(hComplaint.getImageUrl());
                            h_w.setT3details((h_w.getT3details()[7][0].equals(""))?hComplaint.getProximity():isPresent(h_w.getT3details()[7][0],hComplaint.getProximity())?h_w.getT3details()[7][0]:h_w.getT3details()[7][0]+","+hComplaint.getProximity(),7,0);
                            h_w.setT3details((h_w.getT3details()[7][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_w.getT3details()[7][1])+1),7,1);
                        }
                    } else if(hComplaint.getTitle().equals("Problems in your room")){

                        if(hComplaint.getDescription().equals("Electrical work")&&!hComplaint.isResolved()){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.setT4details((h_pr.getT4details()[0][0].equals(""))?hComplaint.getProximity():isPresent(h_pr.getT4details()[0][0],hComplaint.getProximity())?h_pr.getT4details()[0][0]:h_pr.getT4details()[0][0]+","+hComplaint.getProximity(),0,0);
                            h_pr.setT4details((h_pr.getT4details()[0][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[0][1])+1),0,1);
                        }
                        if(hComplaint.getDescription().equals("Civil work")&&!hComplaint.isResolved()){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.setT4details((h_pr.getT4details()[1][0].equals(""))?hComplaint.getProximity():isPresent(h_pr.getT4details()[1][0],hComplaint.getProximity())?h_pr.getT4details()[1][0]:h_pr.getT4details()[1][0]+","+hComplaint.getProximity(),1,0);
                            h_pr.setT4details((h_pr.getT4details()[1][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[1][1])+1),1,1);
                        }
                        if(hComplaint.getDescription().equals("Furniture broken")&&!hComplaint.isResolved()){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.setT4details((h_pr.getT4details()[2][0].equals(""))?hComplaint.getProximity():isPresent(h_pr.getT4details()[2][0],hComplaint.getProximity())?h_pr.getT4details()[2][0]:h_pr.getT4details()[2][0]+","+hComplaint.getProximity(),2,0);
                            h_pr.setT4details((h_pr.getT4details()[2][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[2][1])+1),2,1);
                        }
                        if(hComplaint.getDescription().equals("Internet problem (LAN port repair)")&&!hComplaint.isResolved()){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.setT4details((h_pr.getT4details()[3][0].equals(""))?hComplaint.getProximity():isPresent(h_pr.getT4details()[3][0],hComplaint.getProximity())?h_pr.getT4details()[3][0]:h_pr.getT4details()[3][0]+","+hComplaint.getProximity(),3,0);
                            h_pr.setT4details((h_pr.getT4details()[3][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[3][1])+1),3,1);
                        }
                        if(hComplaint.getDescription().equals("Others")&&!hComplaint.isResolved()){
                            h_pr.setComments(hComplaint.getComments());
                            h_pr.setImageUrl(hComplaint.getImageUrl());
                            h_pr.setT4details((h_pr.getT4details()[4][0].equals(""))?hComplaint.getProximity():isPresent(h_pr.getT4details()[4][0],hComplaint.getProximity())?h_pr.getT4details()[4][0]:h_pr.getT4details()[4][0]+","+hComplaint.getProximity(),4,0);
                            h_pr.setT4details((h_pr.getT4details()[4][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pr.getT4details()[4][1])+1),4,1);
                        }

                    } else if(hComplaint.getTitle().equals("Problems in your wing")){

                        if(hComplaint.getDescription().equals("Electrical work")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[0][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[0][0],hComplaint.getProximity())?h_pw.getT5details()[0][0]:h_pw.getT5details()[0][0]+","+hComplaint.getProximity(),0,0);
                            h_pw.setT5details((h_pw.getT5details()[0][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[0][1])+1),0,1);
                        }
                        if(hComplaint.getDescription().equals("Civil work")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[1][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[1][0],hComplaint.getProximity())?h_pw.getT5details()[1][0]:h_pw.getT5details()[1][0]+","+hComplaint.getProximity(),1,0);
                            h_pw.setT5details((h_pw.getT5details()[1][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[1][1])+1),1,1);
                        }
                        if(hComplaint.getDescription().equals("Furniture broken")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[2][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[2][0],hComplaint.getProximity())?h_pw.getT5details()[2][0]:h_pw.getT5details()[2][0]+","+hComplaint.getProximity(),2,0);
                            h_pw.setT5details((h_pw.getT5details()[2][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[2][1])+1),2,1);
                        }
                        if(hComplaint.getDescription().equals("Internet problem")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[3][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[3][0],hComplaint.getProximity())?h_pw.getT5details()[3][0]:h_pw.getT5details()[3][0]+","+hComplaint.getProximity(),3,0);
                            h_pw.setT5details((h_pw.getT5details()[3][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[3][1])+1),3,1);
                        }
                        if(hComplaint.getDescription().equals("Wing not cleaned regularly")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[4][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[4][0],hComplaint.getProximity())?h_pw.getT5details()[4][0]:h_pw.getT5details()[4][0]+","+hComplaint.getProximity(),4,0);
                            h_pw.setT5details((h_pw.getT5details()[4][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[4][1])+1),4,1);
                        }
                        if(hComplaint.getDescription().equals("Does not have a dustbin")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[5][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[5][0],hComplaint.getProximity())?h_pw.getT5details()[5][0]:h_pw.getT5details()[5][0]+","+hComplaint.getProximity(),5,0);
                            h_pw.setT5details((h_pw.getT5details()[5][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[5][1])+1),5,1);
                        }
                        if(hComplaint.getDescription().equals("Cloth wires not proper")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[6][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[6][0],hComplaint.getProximity())?h_pw.getT5details()[6][0]:h_pw.getT5details()[6][0]+","+hComplaint.getProximity(),6,0);
                            h_pw.setT5details((h_pw.getT5details()[6][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[6][1])+1),6,1);
                        }
                        if(hComplaint.getDescription().equals("Others")&&!hComplaint.isResolved()){
                            h_pw.setComments(hComplaint.getComments());
                            h_pw.setImageUrl(hComplaint.getImageUrl());
                            h_pw.setT5details((h_pw.getT5details()[7][0].equals(""))?hComplaint.getProximity():isPresent(h_pw.getT5details()[7][0],hComplaint.getProximity())?h_pw.getT5details()[7][0]:h_pw.getT5details()[7][0]+","+hComplaint.getProximity(),7,0);
                            h_pw.setT5details((h_pw.getT5details()[7][1].equals(""))?"1":String.valueOf(Integer.parseInt(h_pw.getT5details()[7][1])+1),7,1);
                        }

                    }
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

    public h_PinRoom getH_pr() {
        return h_pr;
    }

    public h_PinWing getH_pw() {
        return h_pw;
    }

    public ArrayList<Complaint> gethComplaintArray() {
        return hComplaintArray;
    }

    public h_WashingMachine getH_wm() {
        //Log.d("dork",h_wm.getT1details()[0][0]);
        return h_wm;
    }

    public h_Washroom getH_w() {
        return h_w;
    }

    public h_WaterDispenser getH_wd() {
        return h_wd;
    }

    private boolean isPresent(String s,String p){
        for(int i=0;i<s.length()-p.length()+1;i++){
            if(s.substring(i,i+p.length()).equals(p)){
                return true;
            }
        }
        return false;
    }
}
