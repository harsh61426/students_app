package in.ac.iitm.students.objects;

/**
 * Created by sam10795 on 3/1/18.
 */

public class Student {
    private String rollno;
    private String name;
    private String hostel;
    private String room;
    private char gender;

    public char getGender() {
        return gender;
    }

    public String getHostel() {
        return hostel;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getRoom() {
        return room;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
