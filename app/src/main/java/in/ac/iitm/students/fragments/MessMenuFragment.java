package in.ac.iitm.students.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import in.ac.iitm.students.R;

public class MessMenuFragment extends Fragment {

    private static final String ARG_MENU_TITLE = "menu";

    private String menutitle;


    public MessMenuFragment() {
        // Required empty public constructor
    }

    public static MessMenuFragment newInstance(String menutitle) {
        MessMenuFragment fragment = new MessMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MENU_TITLE, menutitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menutitle = getArguments().getString(ARG_MENU_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mess_menu, container, false);
        ListView listView = (ListView)view.findViewById(R.id.list_menu);
        return view;
    }

}
