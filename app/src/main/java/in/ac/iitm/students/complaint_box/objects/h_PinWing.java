package in.ac.iitm.students.complaint_box.objects;

/**
 * Created by dell on 26-12-2017.
 *
 */

public class h_PinWing {


    private boolean resolved;
    private String uid;
    private int comments;
    private String imageUrl;
    private String[][] t5details=new String[8][2];

    public h_PinWing(){
        resolved=false;
        uid="";
        comments=0;
        imageUrl="";
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                t5details[i][j]="";
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

    public String[][] getT5details() {
        return t5details;
    }

    public void setT5details(String s,int i,int j) {
        this.t5details[i][j]=s;
    }

    public int getComments() {
        return comments;
    }

    /*public String getDate() {
        return date;
    }

    public String getHostel() {
        return hostel;
    }*/

    public String getImageUrl() {
        return imageUrl;
    }

    /*public String getMoreRooms() {
        return moreRooms;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }*/

    public String getUid() {
        return uid;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    /*public void setDate(String date) {
        this.date = date;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /*public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

    public void setUid(String uid) {
        this.uid = uid;
    }

}
