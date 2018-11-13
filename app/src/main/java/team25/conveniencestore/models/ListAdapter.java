package team25.conveniencestore.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import team25.conveniencestore.R;

public class ListAdapter extends ArrayAdapter<Store> {

    public ListAdapter(Context context, int resource, List<Store> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.store_list_line, null);
        }
        Store p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView name = (TextView) view.findViewById(R.id.store_name);
            name.setText(p.name);

            TextView goal = (TextView) view.findViewById(R.id.store_goal);
            goal.setText(String.valueOf( p.star));
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
            ratingBar.setRating(p.star);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

            TextView adrress = (TextView) view.findViewById(R.id.store_adress);
            adrress.setText(p.Address);

        }
        return view;
    }

}
