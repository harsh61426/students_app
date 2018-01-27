package in.ac.iitm.students.objects;

/**
 * Created by sam10795 on 11/1/18.
 */

public class ew_member {
    private String name;
    private String rollno;
    private String email;
    private String contact;
    private String title;

    public ew_member(String name, String rollno, String email, String contact, String title)
    {
        this.name = name;
        this.rollno = rollno;
        this.email = email;
        this.contact = contact;
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }
}
