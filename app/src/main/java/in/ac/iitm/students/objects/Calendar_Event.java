package in.ac.iitm.students.objects;

/**
 * Created by SAM10795 on 28-06-2017.
 */

public class Calendar_Event {

    public boolean eventStored = false; // this turns to true when we check for events for that day
    public String eventDisplay1 = "", eventDisplay2 = "";
    private int date;
    private int month;
    private String day = "doodle";
    private String details;
    private boolean holiday;
    private boolean remind;

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
