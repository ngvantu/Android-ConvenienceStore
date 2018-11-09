package team25.conveniencestore;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends Activity {

    private EditText etTitle;
    private EditText etContent;
    private Button btnSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        btnSendFeedback = (Button) findViewById(R.id.btnSendFeedback);

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    public void sendFeedback(){
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter title feedback!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter content feedback!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Title: " + title + "\nContent: " + content, Toast.LENGTH_SHORT).show();
    }
}
