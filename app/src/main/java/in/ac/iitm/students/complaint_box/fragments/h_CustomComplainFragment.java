package in.ac.iitm.students.complaint_box.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.ac.iitm.students.R;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

import static android.app.Activity.RESULT_OK;

public class h_CustomComplainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mUUID;


    private ArrayList<String> imageUrls;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public h_CustomComplainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment h_CustomComplainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static h_CustomComplainFragment newInstance(String param1, String param2) {
        h_CustomComplainFragment fragment = new h_CustomComplainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        imageUrls = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_fragment_custom_complaint, container, false);


        Button saveCustomCmplnt = (Button) view.findViewById(R.id.button_save);
        final TextInputLayout til_complaint_title = (TextInputLayout) view.findViewById(R.id.til_complaint_title);
        final TextInputLayout til_complaint_des = (TextInputLayout) view.findViewById(R.id.til_complaint_description);
        final TextInputLayout til_complaint_tag = (TextInputLayout) view.findViewById(R.id.til_complaint_tag);

        final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/addComplaint.php";
        final String hostel_url = "https://students.iitm.ac.in/studentsapp/studentlist/get_hostel.php";

        saveCustomCmplnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = til_complaint_title.getEditText().getText().toString();
                final String description = til_complaint_des.getEditText().getText().toString();
                final String tags = til_complaint_tag.getEditText().getText().toString();
                mUUID = UUID.randomUUID().toString();
                String imageUrl = "";
                if (imageUrls.size() != 0) {
                    imageUrl = imageUrls.get(0);
                }
                Log.e("WFVEVEESFCS", imageUrl);

                final String finalImageUrl = imageUrl;

                if(title != "" && description !="") {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("tagconvertstr", "["+response+"]");
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status.equals("1")) {
                                    getActivity().finish();
                                } else if (status.equals("0")) {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = sharedPref.edit();

                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            String moreRooms = Utils.getprefString(UtilStrings.ROOM, getActivity()) + ",";

                            params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, getActivity()));
                            params.put("NAME", Utils.getprefString(UtilStrings.NAME, getActivity()));
                            params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, getActivity()));
                            params.put("ROOM_NO", Utils.getprefString(UtilStrings.ROOM, getActivity()));

                            params.put("TITLE", title);
                            params.put("PROXIMITY", "");
                            params.put("DESCRIPTION", description);
                            params.put("UPVOTES", "0");
                            params.put("DOWNVOTES", "0");
                            params.put("RESOLVED", "0");
                            params.put("UUID", mUUID);
                            params.put("TAGS", tags);
                            params.put("DATETIME", date);
                            params.put("COMMENTS", "0");
                            params.put("IMAGEURL", finalImageUrl);
                            params.put("MORE_ROOMS", moreRooms);
                            params.put("CUSTOM", "1");
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                } else {
                    Log.d("test","type appropraite title and description");
                    Toast.makeText(getContext(), "type appropriate title and description", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(getContext(), HostelComplaintsActivity.class);
                //startActivity(intent);
            }
        });


        FloatingActionButton addPhoto = (FloatingActionButton) view.findViewById(R.id.fab_addImage);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog addImage = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View view = factory.inflate(R.layout.h_alert_addimage, null);
                FloatingActionButton ibt_camera = (FloatingActionButton) view.findViewById(R.id.ibt_camera);
                FloatingActionButton ibt_gallery = (FloatingActionButton) view.findViewById(R.id.ibt_gallery);

                addImage.setView(view);
                addImage.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        addImage.dismiss();
                    }
                });

                addImage.show();

                ibt_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        addImage.dismiss();
                    }
                });

                ibt_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        addImage.dismiss();
                    }
                });

            }
        });
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Log.i("YYYYYY", selectedImage.toString());
                    imageUrls.add(selectedImage.toString());
//                    imageview.setImageURI(selectedImage);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Log.i("XXXXX", selectedImage.toString());
                    imageUrls.add(selectedImage.toString());
//                    imageview.setImageURI(selectedImage);
                }
                break;
        }
    }
}
