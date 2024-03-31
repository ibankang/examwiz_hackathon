package examwizhackathon.ibankang.com.SubAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import examwizhackathon.ibankang.com.R;

public class subadmin_exam_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subadmin_exam_activity);
        CardView yr_1st_box=findViewById(R.id.yr_1st_box);
        yr_1st_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(subadmin_exam_activity.this, subadmin_year_exam_activity.class);
                intent.putExtra("year_uid","yr_1st_box");
                startActivity(intent);
            }
        });
    }
}