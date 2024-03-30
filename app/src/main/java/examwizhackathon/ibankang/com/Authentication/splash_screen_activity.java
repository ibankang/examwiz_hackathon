package examwizhackathon.ibankang.com.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import examwizhackathon.ibankang.com.MainActivity;
import examwizhackathon.ibankang.com.R;

public class splash_screen_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser==null){
                    startActivity(new Intent(splash_screen_activity.this,login_activity.class));
                }else{
                    startActivity(new Intent(splash_screen_activity.this, MainActivity.class));
                }
                finish();
            }
        },3000);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}