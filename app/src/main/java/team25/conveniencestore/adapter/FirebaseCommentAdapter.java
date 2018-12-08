package team25.conveniencestore.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.io.InputStream;

import team25.conveniencestore.R;

public class FirebaseCommentAdapter extends RecyclerView.Adapter<FirebaseCommentAdapter.ViewHolder> {

    private DataSnapshot data;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtUser;
        public TextView txtComment;
        public ImageView imageView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtUser = v.findViewById(R.id.username);
            txtComment = v.findViewById(R.id.user_comment);
            imageView = v.findViewById(R.id.comment_userimg);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmp = null;

            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public FirebaseCommentAdapter(DataSnapshot data) {
        this.data = data;
    }

    @NonNull
    @Override
    public FirebaseCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.comment_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int i = 0;

        for (DataSnapshot child : data.getChildren()) {
            if(i == position) {
                holder.txtUser.setText((String) child.child("name").getValue());
                holder.txtComment.setText((String) child.child("comment").getValue());
                new DownloadImageTask(holder.imageView).execute((String) child.child("photo").getValue());
                break;
            }
            i++;
        }
    }

    @Override
    public int getItemCount() {
        return (int) data.getChildrenCount();
    }
}
