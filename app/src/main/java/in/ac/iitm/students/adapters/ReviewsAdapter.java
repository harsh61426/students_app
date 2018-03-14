package in.ac.iitm.students.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import in.ac.iitm.students.R;

/**
 * Created by dell on 03-03-2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    int count;
    String[] names;
    String[] reviewGrading;
    String[] reviewXp;
    Float[] stars = {4.5f, 2.5f, 3.5f, 4.0f, 3.0f, 5.0f, 3.5f};

    public void setNames(String[] names) {
        this.names = names;
    }

    public void setReviewGrading(String[] reviewGrading) {
        this.reviewGrading = reviewGrading;
    }

    public void setReviewXp(String[] reviewXp) {
        this.reviewXp = reviewXp;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public View view;
    public TextView name, grading, xp;
    public RatingBar ratingBar;


    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.tv_name);
            grading = (TextView) itemView.findViewById(R.id.event_prof_details);
            xp = (TextView) itemView.findViewById(R.id.event_content);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        name.setText(names[position]);
        grading.setText(reviewGrading[position]);
        xp.setText(reviewXp[position]);
        ratingBar.setRating(stars[position]);

    }

    @Override
    public int getItemCount() {
        return count;
    }
}
