package in.ac.iitm.students.objects;

/**
 * Created by SAM10795 on 14-06-2017.
 */

public class Course {
    private char slot;
    private String course_id;
    private long days;
    //
    private int flag;
    //-1=FN
    //0=Nil
    //1=AN
    //private int flag2[]={0,0,0,0,0};

    public char getSlot() {
        return slot;
    }

    public long getDays() {
        return days;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public void setSlot(char slot) {
        this.slot = slot;
    }

    public int getFlag1(){
        return flag;
    }

    public void setFlag1(int j){
        this.flag=j;
    }

}
