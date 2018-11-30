package team25.conveniencestore.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import team25.conveniencestore.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private JSONArray reviews;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtUser;
        public TextView txtComment;
        public ImageView imageView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtUser = (TextView) v.findViewById(R.id.username);
            txtComment = (TextView) v.findViewById(R.id.user_comment);
            imageView = (ImageView) v.findViewById(R.id.comment_userimg);
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

    public void add(int position, String item) {
//        reviews.put(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
//        values.remove(position);
        notifyItemRemoved(position);
    }

    public CommentAdapter(JSONArray reviews) {
        this.reviews = reviews;
    }


    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.comment_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        JSONObject comment = null;
        try {
            comment = reviews.getJSONObject(position);
            holder.txtUser.setText(comment.getString("author_name"));
//            holder.txtComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    remove(position);
//                }
//            });
            holder.txtComment.setText(comment.getString("text"));
            new DownloadImageTask(holder.imageView).execute(comment.getString("profile_photo_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviews.length();
    }
}


