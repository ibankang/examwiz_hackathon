package examwizhackathon.ibankang.com.SubAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.view_model;

public class subadmin_year_exam_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    TextView textView;
    String guid = null, exam_uid = null;

    String year_uid = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subadmin_year_exam_activity);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user ID
        guid = firebaseUser.getUid();

        year_uid = getIntent().getStringExtra("year_uid");

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);


        ((ImageView)findViewById(R.id.add_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(subadmin_year_exam_activity.this, subadmin_create_exam_activity.class);
                intent.putExtra("year_uid","yr_1st_box");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_subadmin_details();
    }

    void get_subadmin_details(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();
        subadmin_exam_details_adapter wAdapter = new subadmin_exam_details_adapter(dataholder, this);
        CollectionReference collectionReference = db.collection("exam");
        collectionReference
                .whereEqualTo("year_uid",year_uid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("exam_title"),//1
                                    s.getString("exam_category"),//2
                                    s.getString("exam_date"),//3
                                    s.getString("exam_start"),//4
                                    //String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),//4
                                    s.getString("exam_end"),//5
                                    s.getString("seating_plan_live"),//6
                                    "",//7
                                    "",//8
                                    "",//9
                                    s.getString("exam_uid"));//10

                            dataholder.add(obj);
                        }
                        recyclerView.setAdapter(wAdapter);
                    }else
                    {

                    }
                });
    }
}