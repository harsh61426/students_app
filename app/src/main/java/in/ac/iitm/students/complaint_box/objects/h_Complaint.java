package in.ac.iitm.students.complaint_box.objects;

import java.io.Serializable;

/**
 * Created by harisanker on 22/6/17.
 */

public class h_Complaint implements Serializable {
    private String name;
    private String rollNo;
    private String roomNo;
    private String title;
    private String proximity;
    private String description;
    private String tag;
    private int upvotes;
    private int downvotes;
    private String date;
    private boolean resolved;
    private String uid;
    private int comments;
    private String hostel;
    private String moreRooms;

    public static h_Complaint getErrorComplaintObject() {
        h_Complaint hComplaint = new h_Complaint();
        hComplaint.setName("Institute MobOps");
        hComplaint.setUpvotes(42);
        hComplaint.setDownvotes(0);
        hComplaint.setComments(0);
        hComplaint.setDate("00-00-0000");
        hComplaint.setResolved(true);
        hComplaint.setHostel("IIT Madras");
        hComplaint.setTag("#instimobops");
        hComplaint.setTitle("Error getting complaints!");
        hComplaint.setDescription("This could be due to:\nNo internet :/\nno complaints :)\nbut not server error ;)");
        return hComplaint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo(){return rollNo;}

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHostel() {
        return hostel;
    }

    private void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getMoreRooms() { return moreRooms;}

    public void setMoreRooms(String moreRooms){this.moreRooms = moreRooms;}
}
