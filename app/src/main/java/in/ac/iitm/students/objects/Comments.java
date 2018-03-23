package in.ac.iitm.students.objects;

import in.ac.iitm.students.complaint_box.objects.CommentObj;

/**
 * Created by sam10795 on 11/2/18.
 */

public class Comments {

    private String name, date, rollNo;
    private String roomNo,commentStr, error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentStr() {
        return commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
