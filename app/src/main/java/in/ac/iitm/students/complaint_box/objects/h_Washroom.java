package in.ac.iitm.students.complaint_box.objects;

/**
 * Created by dell on 26-12-2017.
 *
 */

public class h_Washroom {
    private boolean resolved;
    private String uid;
    private int comments;
    private String imageUrl;
    private String[][] t3details= new String[8][2];

    public h_Washroom(){
        resolved=false;
        uid="";
        comments=0;
        imageUrl="";
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                t3details[i][j]="";
            }
        }
    }

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
    }*/

    public boolean isResolved() {
        return resolved;
    }

    public int getComments() {
        return comments;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String[][] getT3details() {
        return t3details;
    }

    public String getUid() {
        return uid;
    }

    public void setT3details(String s,int i,int j) {
        this.t3details[i][j]=s;
    }
    /*public String getDate() {
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

    public String getUid() {
        return uid;
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
    }
    */





}
