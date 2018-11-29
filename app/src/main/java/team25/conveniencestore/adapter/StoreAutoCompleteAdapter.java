package team25.conveniencestore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.models.StoreItemAutoComplete;

public class StoreAutoCompleteAdapter extends ArrayAdapter<StoreItemAutoComplete> {

    private List<StoreItemAutoComplete> storeListFull;

    public StoreAutoCompleteAdapter(@NonNull Context context) {
        super(context, 0);
        filterStoreList();
    }

    private void filterStoreList() {
        storeListFull = new ArrayList<>();
        storeListFull.add(new StoreItemAutoComplete("Tất cả", Color.TRANSPARENT));
        storeListFull.add(new StoreItemAutoComplete("B's Mart", R.drawable.logo_store_bsmart));
        storeListFull.add(new StoreItemAutoComplete("Circle K", R.drawable.logo_store_circlek));
        storeListFull.add(new StoreItemAutoComplete("Family Mart", R.drawable.logo_store_familymart));
        storeListFull.add(new StoreItemAutoComplete("Ministop", R.drawable.logo_store_ministop));
        storeListFull.add(new StoreItemAutoComplete("Vinmart+", R.drawable.logo_store_vinmart));
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return storeFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_store_autocomplete_row, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.autocomplete_store_name);
        ImageView imageViewLogo = convertView.findViewById(R.id.autocomplete_store_logo);

        StoreItemAutoComplete storeItem = getItem(position);

        if (storeItem != null) {
            textViewName.setText(storeItem.getStoreName());
            imageViewLogo.setImageResource(storeItem.getLogoImage());
        }

        return convertView;
    }

    private Filter storeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<StoreItemAutoComplete> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(storeListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(StoreItemAutoComplete item : storeListFull){
                    if (item.getStoreName().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                addAll((List) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((StoreItemAutoComplete) resultValue).getStoreName();
        }
    };
}
