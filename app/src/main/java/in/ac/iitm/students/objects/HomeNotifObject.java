package in.ac.iitm.students.objects;

/**
 * Created by rohithram on 26/7/17.
 */

public class HomeNotifObject {

    public String detail;
    public String id;
    public String image_url;
    public String Topic;
    public String link;
    public String title;
    public String createdat;

    public HomeNotifObject(String Topic) {
        this.Topic = Topic;
    }

}
