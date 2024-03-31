package examwizhackathon.ibankang.com.SubAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.Utils;

public class subadmin_create_exam_activity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView,autoCompletetxt_category;

    EditText exam_title, exam_date, exam_instruction, exam_start_time, exam_end_time;
    String selected_live_time = "null", selected_exam_category = "MST-1", guid= null;
    private FirebaseFirestore db;
    Button create_exam_btn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    String year_uid = null;
    private static final int REQUEST_CODE_CSV = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subadmin_create_exam_activity);

        year_uid = getIntent().getStringExtra("year_uid");

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user ID
        guid = firebaseUser.getUid();

        exam_title = findViewById(R.id.exam_title);
        exam_date = findViewById(R.id.exam_date);
        exam_start_time = findViewById(R.id.exam_start_time);
        exam_end_time = findViewById(R.id.exam_end_time);
        exam_instruction = findViewById(R.id.exam_instruction_txt);

        progressBar = findViewById(R.id.progress_bar);
        create_exam_btn = findViewById(R.id.create_exam_btn);

        //exam live time
        autoCompleteTextView = findViewById(R.id.exam_live_time_dropdown);

        // Data to be inserted into the dropdown
        String[] live_time = {"5 mints", "10 mints", "15 mints", "20 mints"};

        // Creating adapter to set data to the AutoCompleteTextView
        ArrayAdapter<String> adapter_live_time = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, live_time);

        // Setting the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter_live_time);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_live_time = (String) parent.getItemAtPosition(position);
                // Call method to save selected department to Firebase
            }
        });

        //exam category
        autoCompletetxt_category = findViewById(R.id.exam_category_dropdown);

        // Data to be inserted into the dropdown
        String[] exam_category = {"MST-1", "MST-2", "Final Exam"};

        // Creating adapter to set data to the AutoCompleteTextView
        ArrayAdapter<String> adapter_exam_category = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, exam_category);

        // Setting the adapter to the AutoCompleteTextView
        autoCompletetxt_category.setAdapter(adapter_exam_category);

        autoCompletetxt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_exam_category = (String) parent.getItemAtPosition(position);
                // Call method to save selected department to Firebase
            }
        });


        create_exam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                create_exam();
                changeInProgress(true);

            }
        });


    }

    public boolean create_exam() {

        String exam_uid= Utils.randomcode(16);
        if (exam_title.getText().toString().length()<1) {
            exam_title.setError("Field is required");
            return false;
        }
        if (exam_date.getText().toString().length()<1) {
            exam_date.setError("Field is required");
            return false;
        }
        if (exam_start_time.getText().toString().length()<1) {
            exam_start_time.setError("Field is required");
            return false;
        }
        if (exam_end_time.getText().toString().length()<1) {
            exam_end_time.setError("Field is required");
            return false;
        }
        if (exam_instruction.getText().toString().length()<1) {
            exam_instruction.setError("Field is required");
            return false;
        }

        else {
            Map<String, Object> mapdata = new HashMap<>();
            mapdata.put("exam_uid", exam_uid);
            mapdata.put("status", true);
            mapdata.put("year_uid", year_uid);
            mapdata.put("exam_title", exam_title.getText().toString());
            mapdata.put("exam_start", exam_start_time.getText().toString());
            mapdata.put("exam_date", exam_date.getText().toString());
            mapdata.put("exam_end", exam_end_time.getText().toString());
            mapdata.put("exam_category", selected_exam_category);
            mapdata.put("exam_instructions", exam_instruction.getText().toString());
            mapdata.put("seating_plan_live", selected_live_time);
            mapdata.put("date_time", FieldValue.serverTimestamp());

            FirebaseFirestore.getInstance().collection("exam").document(exam_uid).set(mapdata).addOnCompleteListener(task -> {
                Toast.makeText(subadmin_create_exam_activity.this, "Successfully created an exam", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        return false;
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            create_exam_btn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            create_exam_btn.setVisibility(View.VISIBLE);
        }
    }
}