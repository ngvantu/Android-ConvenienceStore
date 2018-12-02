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
import team25.conveniencestore.SqlProvider.FavoritePlaces;
import team25.conveniencestore.models.GooglePlace;

public class FavoriteStoresAdapter extends RecyclerView.Adapter<FavoriteStoresAdapter.ViewHolder> {

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView address;
        TextView star;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dlg_store_name);
            address = itemView.findViewById(R.id.dlg_store_adress);
            star = itemView.findViewById(R.id.dlg_store_star);
            ratingBar = itemView.findViewById(R.id.dlg_ratingbar);
        }
    }

    // constuctor
    private List<FavoritePlaces> favoriteStores;

    public FavoriteStoresAdapter(List<FavoritePlaces> favoriteStores) {
        this.favoriteStores = favoriteStores;
    }

    @Override
    public FavoriteStoresAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_result_store_row, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteStoresAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(favoriteStores.get(i).name);
        viewHolder.address.setText(favoriteStores.get(i).vicinity);
        viewHolder.star.setText(String.valueOf(favoriteStores.get(i).rating));
        viewHolder.ratingBar.setRating(Float.valueOf(String.valueOf(favoriteStores.get(i).rating)));
        LayerDrawable stars = (LayerDrawable) viewHolder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return favoriteStores.size();
    }
}
