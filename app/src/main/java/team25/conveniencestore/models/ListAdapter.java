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

public class ListAdapter extends ArrayAdapter<GooglePlace> {

    public ListAdapter(Context context, int resource, List<GooglePlace> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.custom_result_store_row, null);
        }
        GooglePlace p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView name = view.findViewById(R.id.dlg_store_name);
            name.setText(p.getName());

            TextView star = view.findViewById(R.id.dlg_store_star);
            star.setText(String.valueOf(p.getRating()));
            RatingBar ratingBar = view.findViewById(R.id.dlg_ratingbar);
            ratingBar.setRating(Float.parseFloat(star.getText().toString()));
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

            TextView adrress = view.findViewById(R.id.dlg_store_adress);
            adrress.setText(p.getVicinity());
        }
        return view;
    }

}
