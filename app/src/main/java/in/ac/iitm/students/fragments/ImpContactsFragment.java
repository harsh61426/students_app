package in.ac.iitm.students.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.ImpContactsAdapter;

/**
 * Created by DELL on 12/21/2017.
 */

public class ImpContactsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_imp_contacts, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_contacts);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ImpContactsAdapter(getActivity()));
        return view;
    }
}
