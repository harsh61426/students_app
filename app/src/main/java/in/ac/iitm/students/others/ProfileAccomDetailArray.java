package in.ac.iitm.students.others;

import java.util.ArrayList;

import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.objects.ProfileAccomDetails;

/**
 * Created by Sarath on 06/10/17.
 */
public class ProfileAccomDetailArray {

    public static ArrayList<ProfileAccomDetails> data=new ArrayList<ProfileAccomDetails>();

    public static ArrayList<ProfileAccomDetails> getAccomData(){



        return data;
    }

    public static void makeAccomCard(ProfileAccomDetails current){


        data.add(current);
        //ProfileAccomAdapter.adapterData=data;
        ProfileActivity.accomadapter.notifyDataSetChanged();

        //ProfileActivity.accomadapter.notifyItemInserted(data.indexOf(current));


    }
}
