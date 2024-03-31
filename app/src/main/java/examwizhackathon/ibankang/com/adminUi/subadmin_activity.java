package examwizhackathon.ibankang.com.adminUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.SubAdmin.subadmin_details_adapter;
import examwizhackathon.ibankang.com.view_model;

public class subadmin_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    String guid = null, subadmin;
    MaterialButton create_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subadmin_activity);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user ID
        guid = firebaseUser.getUid();

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        get_subadmin_details();

        create_btn = findViewById(R.id.create_account_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create_subadmin();
            }
        });

    }

    private void create_subadmin() {

        TextInputLayout subadmin_email_txt = findViewById(R.id.create_subadmin_textinput);

        // Check if an email is entered
        if (subadmin_email_txt.getEditText().getText().toString() != null) {
            String subadminEmail = subadmin_email_txt.getEditText().getText().toString().trim();
            db.collection("account").whereEqualTo("email", subadmin_email_txt.getEditText().getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot s : task.getResult()) {

                            // Update account type and other details
                            Map<String, Object> mapdata = new HashMap<>();
                            mapdata.put("account_type", "subadmin");
                            mapdata.put("admin_uid", guid);
                            mapdata.put("subaccount_time", FieldValue.serverTimestamp());

                            CollectionReference accountRef = db.collection("account");
                            accountRef.document(s.getString("guid")).update(mapdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(subadmin_activity.this, "Subadmin account created successfully", Toast.LENGTH_SHORT).show();
                                                get_subadmin_details();
                                                // Clear the email field after successful creation
                                                subadmin_email_txt.getEditText().setText("");
                                            } else {
                                                Toast.makeText(subadmin_activity.this, "Failed to create subadmin account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(subadmin_activity.this, "Please enter subadmin email", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            // Check if the email field is not empty


            // Remove the line below to prevent returning to the home screen
            // finish();
        }
    }


    void get_subadmin_details(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();
        subadmin_details_adapter wAdapter = new subadmin_details_adapter(dataholder, this);
        CollectionReference collectionReference = db.collection("account");
        collectionReference.whereEqualTo("admin_uid", guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("photo"),//1
                                    s.getString("name"),//2
                                    s.getString("email"),//3
                                    "",
                                    //String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),//4
                                    "",//5
                                    "",//6
                                    "",//7
                                    "",//8
                                    "",//9
                                    s.getString("guid"));//10

                            dataholder.add(obj);
                        }
                        recyclerView.setAdapter(wAdapter);
                    }else
                    {

                    }
                });
    }
}