package in.ac.iitm.students.complaint_box.objects;

/**
 * Created by dell on 26-12-2017.
 *
 */

public class h_WashingMachine {


    private boolean resolved;
    private String uid;
    private int comments;
    private String imageUrl;
    private String[][] t1details=new String[6][2];

    /*public String getRoomNo() {
        return roomNo;
    }

    public void setMoreRooms(String moreRooms) {
        this.moreRooms = moreRooms;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getRollNo() {
        return rollNo;
    }



    public String getDate() {
        return date;
    }

    public String getHostel() {
        return hostel;
    }



    public String getMoreRooms() {
        return moreRooms;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }



    public void setDate(String date) {
        this.date = date;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

    public String[][] getT1details() {
        return t1details;
    }

    public void setT1details(String s,int i,int j) {
        this.t1details[i][j]=s;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isResolved() {
        return resolved;
    }

    public int getComments() {
        return comments;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }



}
