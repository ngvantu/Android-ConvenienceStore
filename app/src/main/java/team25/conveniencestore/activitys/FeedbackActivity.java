package team25.conveniencestore.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import team25.conveniencestore.R;

public class FeedbackActivity extends Activity {

    private static final int RC_SIGN_IN = 2;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvName;
    private ImageButton btnOpenGmail;
    private ImageButton btnSendFeedback;
    private FirebaseUser user;

    void CallLogin()
    {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.logo_login)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    void CallSignOut()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Signed out!", Toast.LENGTH_SHORT).show();
                        tvName.setText("");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        user = FirebaseAuth.getInstance().getCurrentUser();

        tvName = findViewById(R.id.tvName);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);

        if(user != null) {
            String sender = user.getEmail();
            if (sender == null)
                sender = user.getPhoneNumber();
            tvName.setText(sender);
        }

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        btnOpenGmail = findViewById(R.id.btnGmail);
        btnOpenGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmail();
            }
        });
    }

    public void openEmail() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:ConvinientSupport@gmail.com"));
            if(title.isEmpty())
                title = "Thư góp ý";
            if(content.isEmpty())
                content = "Nội dung góp ý/báo lỗi";
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
                    .putExtra(Intent.EXTRA_TEXT, content);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Can't find mail app in your device", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Login successed!", Toast.LENGTH_SHORT).show();
                user = FirebaseAuth.getInstance().getCurrentUser();
                String sender = user.getEmail();
                if(sender == null)
                    sender = user.getPhoneNumber();
                tvName.setText(sender);

            } else {
                Toast.makeText(getApplicationContext(), "Some thing wrong happened!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendFeedback() {
        if (user == null) {
           CallLogin();
        }
        else {
            String sender = tvName.getText().toString();
            String uid = user.getUid();
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
                myRef.child("uid").setValue(uid);
                myRef.child("sender").setValue(sender);
                myRef.child("tittle").setValue(title);
                myRef.child("content").setValue(content);
                AlertDialog.Builder myBuider = new AlertDialog.Builder(this);
                myBuider.setTitle("Cảm ơn")
                        .setMessage("Cảm ơn góp ý phản hồi của bạn")
                        .setPositiveButton("OK", null)
                        .show();

                etTitle.setText("");
                etContent.setText("");
                CallSignOut();
            } catch (Exception e) {
            }
        }
    }
}
