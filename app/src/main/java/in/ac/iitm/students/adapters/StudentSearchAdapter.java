package in.ac.iitm.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Student;

/**
 * Created by sam10795 on 3/1/18.
 */

public class StudentSearchAdapter extends ArrayAdapter {
    private ArrayList<Student> students;
    private Context context;

    public StudentSearchAdapter(ArrayList<Student> students, Context context)
    {
        super(context, R.layout.item_search_result,students);
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_result,parent,false);
            holder = new ViewHolder();
            holder.rollno = (TextView)convertView.findViewById(R.id.roll);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<students.size()) {
            holder.rollno.setText(students.get(position).getRollno());
            holder.name.setText(students.get(position).getName());
        }
        else
        {
            holder.name.setText("N/A");
            holder.rollno.setText("N/A");
        }
        return convertView;
    }

    static class ViewHolder
    {
        TextView rollno;
        TextView name;
    }

}
