package in.ac.iitm.students.complaint_box.objects;

/**
 * Created by dell on 25-12-2017.
 */

public class h_Description {

    private String description;
    private String proximity;

    public h_Description(String description,String proximity){
        this.description=description;
        this.proximity=proximity;
    }
    public h_Description(){

    }


    public String getProximity() {
        return proximity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setProximity(String proximity) {
        this.proximity = proximity;
    }
}
