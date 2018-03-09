package in.ac.iitm.students.objects;

/**
 * Created by sam10795 on 9/3/18.
 */

public class MessMenu {

    private String breakfast;
    private String lunch;
    private String dinner;
    private float rate_breakfast;
    private float rate_lunch;
    private float rate_dinner;
    private int num_b,num_l,num_d;

    public MessMenu(String breakfast, String lunch, String dinner,
                    float rate_breakfast, float rate_lunch, float rate_dinner,
                    int num_b, int num_d, int num_l)
    {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.num_b = num_b;
        this.num_l = num_l;
        this.num_d =num_d;
        this.rate_breakfast = rate_breakfast;
        this.rate_lunch = rate_lunch;
        this.rate_dinner = rate_dinner;
    }

    public float getRate_breakfast() {
        return rate_breakfast;
    }

    public float getRate_dinner() {
        return rate_dinner;
    }

    public float getRate_lunch() {
        return rate_lunch;
    }

    public int getNum_b() {
        return num_b;
    }

    public int getNum_d() {
        return num_d;
    }

    public int getNum_l() {
        return num_l;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getDinner() {
        return dinner;
    }

    public String getLunch() {
        return lunch;
    }
}
