package team25.conveniencestore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import team25.conveniencestore.R;

public class UserCommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private JSONArray comments;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtUser;
        public TextView txtComment;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtUser = v.findViewById(R.id.username);
            txtComment = v.findViewById(R.id.user_comment);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
