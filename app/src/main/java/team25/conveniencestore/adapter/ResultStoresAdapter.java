package team25.conveniencestore.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.models.GooglePlace;

public class ResultStoresAdapter extends RecyclerView.Adapter<ResultStoresAdapter.ViewHolder> {

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView address;
        TextView star;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.dlg_store_name);
            address = (TextView) itemView.findViewById(R.id.dlg_store_adress);
            star = (TextView) itemView.findViewById(R.id.dlg_store_star);
            ratingBar = (RatingBar) itemView.findViewById(R.id.dlg_ratingbar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    // constuctor
    private List<GooglePlace> resultStores;
    private OnItemClickListener onItemClickListener;

    public ResultStoresAdapter(OnItemClickListener onItemClickListener, List<GooglePlace> resultStores) {
        this.onItemClickListener = onItemClickListener;
        this.resultStores = resultStores;
    }

    @Override
    public ResultStoresAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_result_store_row, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultStoresAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(resultStores.get(i).getName());
        viewHolder.address.setText(resultStores.get(i).getVicinity());
        viewHolder.star.setText(String.valueOf(resultStores.get(i).getRating()));
        viewHolder.ratingBar.setRating(Float.valueOf(String.valueOf(resultStores.get(i).getRating())));
        LayerDrawable stars = (LayerDrawable) viewHolder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return resultStores.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
