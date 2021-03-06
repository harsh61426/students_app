package in.ac.iitm.students.complaint_box.objects;

import java.io.Serializable;

/**
 * Created by harisanker on 22/6/17.
 */

public class Complaint implements Serializable {
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
    private String hostel = "IIT Madras";
    private String imageUrl;
    private boolean custom;
    private String trending;

    public static Complaint getErrorComplaintObject() {
        Complaint hComplaint = new Complaint();
        hComplaint.setName("Institute MobOps");
        hComplaint.setRollNo("X");
        hComplaint.setUpvotes(42);
        hComplaint.setDownvotes(0);
        hComplaint.setComments(0);
        hComplaint.setDate("00-00-0000");
        hComplaint.setResolved(true);
        hComplaint.setCustom(true);
        hComplaint.setHostel("IIT Madras");
        hComplaint.setTag("#instimobops#gencomp");
        hComplaint.setTitle("Post insti-wide issues here!");
        hComplaint.setDescription("Your chance raise new issues, comment and vote on things which matter to you and the institute, has arrived. Get direct reply's from the Executive Wing members, only on General Complaints, Students App!");
        return hComplaint;
    }

    public static Complaint getHostelErrorComplaintObject() {
        Complaint hComplaint = new Complaint();
        hComplaint.setName("Institute MobOps");
        hComplaint.setRollNo("X");
        hComplaint.setUpvotes(42);
        hComplaint.setDownvotes(0);
        hComplaint.setComments(0);
        hComplaint.setDate("00-00-0000");
        hComplaint.setResolved(true);
        hComplaint.setCustom(true);
        hComplaint.setHostel("IIT Madras");
        hComplaint.setTag("#instimobops#hostelcomp");
        hComplaint.setTitle("Post hostel issues here!");
        hComplaint.setDescription("From your LAN port not working to the washing machine flooding the bathroom, this portal takes it all and organises it. Your complaints can be resolved by you and the hostel secretaries.");
        return hComplaint;
    }

    /*This could be due to:
    No internet :/
    no complaints :)
    but not server error ;)
    */

    public String getTrending() {
        return trending;
    }

    public void setTrending(String trending) {
        this.trending = trending;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getCustom() {
        return custom;
    }

    /*public void setCustom(Boolean custom) {
        this.custom = custom;
    }*/

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

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }


    /*public boolean isCustom() {
        return custom;
    }*/

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
