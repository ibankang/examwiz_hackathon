package examwizhackathon.ibankang.com.Authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import examwizhackathon.ibankang.com.R;

public class forgot_password_activity extends AppCompatActivity {

    TextView login_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        login_txt = findViewById(R.id.login_txt);
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgot_password_activity.this,login_activity.class));
            }
        });

        ((Button)findViewById(R.id.forgot_password_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpasswordInFirebase();
            }
        });

    }

    void forgotpasswordInFirebase() {
        TextInputLayout emile = (TextInputLayout) findViewById(R.id.email_edit_txt);
        if (emile.getEditText().getText().length() > 5) {
            Toast.makeText(this, "Sending..", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(emile.getEditText().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgot_password_activity.this, "Send Successful", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                                startActivity(new Intent(forgot_password_activity.this,login_activity.class));
                                finish();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Enter email id ?", Toast.LENGTH_SHORT).show();
        }
    }
}