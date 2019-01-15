package team25.conveniencestore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team25.conveniencestore.R;
import team25.conveniencestore.models.Route;
import team25.conveniencestore.models.Step;

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView content;
        TextView distance;
        TextView duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.direction_content);
            distance = (TextView) itemView.findViewById(R.id.direction_distance);
            duration = (TextView) itemView.findViewById(R.id.direction_duration);
        }
    }

    // constructor
    private List<Step> steps;

    public DirectionAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_direction_row, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.content.setText(steps.get(i).instructions);
        viewHolder.distance.setText(steps.get(i).distance.text);
        viewHolder.duration.setText(steps.get(i).duration.text);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
