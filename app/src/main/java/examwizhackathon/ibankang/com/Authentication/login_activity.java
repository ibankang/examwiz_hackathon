package examwizhackathon.ibankang.com.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import examwizhackathon.ibankang.com.MainActivity;
import examwizhackathon.ibankang.com.R;

public class login_activity extends AppCompatActivity {

    String guid = null;
    public static final String Guid_KEY = "guid";
    TextInputLayout emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView signup_txt, forgot_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailEditText = findViewById(R.id.email_edit_txt);
        passwordEditText = findViewById(R.id.password_edit_txt);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);

        loginBtn.setOnClickListener((v) -> loginUser());

        signup_txt = findViewById(R.id.signup_txt);
        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_activity.this, signup_activity.class));
            }
        });

        forgot_txt = findViewById(R.id.forgot_txt);
        forgot_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_activity.this, forgot_password_activity.class));
            }
        });
    }
    void loginUser(){
        String email  = emailEditText.getEditText().getText().toString();
        String password  = passwordEditText.getEditText().getText().toString();


        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }

    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login is success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to mainactivity
                        startActivity(new Intent(login_activity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(login_activity.this, "Email not verified, Please verify your email.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    //login failed
                    Toast.makeText(login_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }
}