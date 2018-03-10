package in.ac.iitm.students.objects;

/**
 * Created by sam10795 on 9/3/18.
 */

public class MessMenu {

    private String day;
    private String menutype;
    private String menu;
    private float rating;
    private int num;

    public MessMenu(String day, String menutype, String menu, float rating, int num)
    {
        this.day = day;
        this.menutype = menutype;
        this.menu = menu;
        this.rating = rating;
        this.num = num;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public float getRating() {
        return rating;
    }

    public int getNum() {
        return num;
    }

    public String getMenu() {
        return menu;
    }

    public String getMenutype() {
        return menutype;
    }

    public void setMenutype(String menutype) {
        this.menutype = menutype;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
