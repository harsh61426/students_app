package in.ac.iitm.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
            holder.acco = (TextView)convertView.findViewById(R.id.acco);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<students.size()) {
            Student s = students.get(position);
            holder.rollno.setText(s.getRollno());
            holder.name.setText(s.getName());
            if(s.getRoom().isEmpty())
            {
                holder.acco.setText(s.getHostel().toUpperCase());
            }
            else
            {
                holder.acco.setText(s.getRoom()+", "+s.getHostel().toUpperCase());
            }
        }
        else
        {
            holder.name.setText("N/A");
            holder.rollno.setText("N/A");
            holder.acco.setText("N/A");
        }
        return convertView;
    }

    static class ViewHolder
    {
        TextView rollno;
        TextView name;
        TextView acco;
    }

}
