package in.ac.iitm.students.complaint_box.objects;

/**
 * Created by dell on 25-12-2017.
 */

public class h_DescriptionwithFreq extends h_Description {

    private int freq;

    public h_DescriptionwithFreq(String a,String b,int freq){
        this.freq=freq;
        this.setDescription(a);
        this.setProximity(b);

    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}
