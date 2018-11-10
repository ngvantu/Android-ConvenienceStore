package team25.conveniencestore;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends Activity {

    private EditText etTitle;
    private EditText etContent;
    private ImageButton btnOpenGmail;
    private ImageButton btnSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        btnSendFeedback = (ImageButton) findViewById(R.id.btnSendFeedback);
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        btnOpenGmail =(ImageButton) findViewById(R.id.btnGmail);
        btnOpenGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGmail();
            }
        });
    }

    public void openGmail() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setType("plain/text")
                    .setData(Uri.parse("tienandehit@gmail.com"))
                    .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            if(title.isEmpty())
                title = "Thư góp ý";
            if(content.isEmpty())
                content = "Nội dung góp ý/báo lỗi";
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
                    .putExtra(Intent.EXTRA_TEXT, content);

            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Can't find google mail app in your device", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendFeedback() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message").push();
        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter title feedback!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter content feedback!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            myRef.child("tittle").setValue(title);
            myRef.child("content").setValue(content);
            Toast.makeText(getApplicationContext(), "Đã gửi. Cảm ơn góp ý phản hồi của bạn", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
        }
    }
}
