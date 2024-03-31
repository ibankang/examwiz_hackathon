package examwizhackathon.ibankang.com.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import examwizhackathon.ibankang.com.R;

public class signup_activity extends AppCompatActivity {

    String guid = null;
    public static final String Guid_KEY = "guid";
    TextInputLayout emailEditText, passwordEditText, confirmPasswordEditText, name_textinput, roll_textinput, teacher_id_textinput, reg_textinput, phone_textinput, uni_name_textinput, dept_name_textinput, designation_textinput, passout_textinput;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView login_txt;

    String authority = "student", selectedDepartment = "Computer Science Engineering";


    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);


        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email_edit_txt);
        passwordEditText = findViewById(R.id.password_edit_txt);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_txt);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        name_textinput = findViewById(R.id.name_textinput);
        roll_textinput = findViewById(R.id.roll_textinput);
        teacher_id_textinput = findViewById(R.id.teacher_id_textinput);
        reg_textinput = findViewById(R.id.reg_textinput);
        phone_textinput = findViewById(R.id.phone_textinput);
        uni_name_textinput = findViewById(R.id.uni_name_textinput);
        dept_name_textinput = findViewById(R.id.dept_name_textinput);
        designation_textinput = findViewById(R.id.designation_textinput);
        passout_textinput = findViewById(R.id.passout_textinput);
//        city_name_textinput = findViewById(R.id.city_name_textinput);
//        state_textinput = findViewById(R.id.state_textinput);
//        country_textinput = findViewById(R.id.country_textinput);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.deptDropDown);


        // Data to be inserted into the dropdown
        String[] departments = {"Computer Science Engineering", "Mechanical Engineering", "Electronics and Communication Engineering", "Electronics and Computer Engineering"};

        // Creating adapter to set data to the AutoCompleteTextView
        ArrayAdapter<String> adapter_dept = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, departments);

        // Setting the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter_dept);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = (String) parent.getItemAtPosition(position);
                // Call method to save selected department to Firebase
            }
        });


        login_txt = findViewById(R.id.login_txt);
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup_activity.this, login_activity.class));
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        /// loginBtnTextView.setOnClickListener(v-> finish());
    }

    void createAccount() {
        String email = emailEditText.getEditText().getText().toString();

        String password = passwordEditText.getEditText().getText().toString();

        String confirmPassword = confirmPasswordEditText.getEditText().getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated) {
            return;
        }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signup_activity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            //creating acc is done
                            guid = firebaseAuth.getUid();

                            signup();


                        } else {
                            //failure
                            Toast.makeText(signup_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email, String password, String confirmPassword) {
        //validate the data that are input by user.

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }
        return true;
    }


    public boolean signup() {
        if (name_textinput.getEditText().getText().toString().length()<1) {
            name_textinput.setError("Field is required");
            return false;
        }
        if (roll_textinput.getEditText().getText().toString().length()<1) {
            roll_textinput.setError("Field is required");
            return false;
        }
        if (reg_textinput.getEditText().getText().toString().length()<1) {
            reg_textinput.setError("Field is required");
            return false;
        }
        if (phone_textinput.getEditText().getText().toString().length()<1) {
            phone_textinput.setError("Field is required");
            return false;
        }
        if (uni_name_textinput.getEditText().getText().toString().length()<1) {
            uni_name_textinput.setError("Field is required");
            return false;
        }
        if (passout_textinput.getEditText().getText().toString().length()<1) {
            passout_textinput.setError("Field is required");
            return false;
        }
//        if (city_name_textinput.getEditText().getText().toString().length()<1) {
//            city_name_textinput.setError("Field is required");
//            return false;
//        }
//        if (state_textinput.getEditText().getText().toString().length()<1) {
//            state_textinput.setError("Field is required");
//            return false;
//        }
//        if (country_textinput.getEditText().getText().toString().length()<1) {
//            country_textinput.setError("Field is required");
//            return false;
//        }
        else {
            Map<String, Object> mapdata = new HashMap<>();
            mapdata.put("guid", guid);
            mapdata.put("authority", authority);
            mapdata.put("account_type", "user");
            mapdata.put("admin_uid","null");
            mapdata.put("subaccount_time", FieldValue.serverTimestamp());
            mapdata.put("photo", "null");
            mapdata.put("status", true);
            mapdata.put("name", name_textinput.getEditText().getText().toString());
            mapdata.put("roll_no", roll_textinput.getEditText().getText().toString());
            mapdata.put("teacher_id", teacher_id_textinput.getEditText().getText().toString());
            mapdata.put("designation", designation_textinput.getEditText().getText().toString());
            mapdata.put("reg_no", reg_textinput.getEditText().getText().toString());
            mapdata.put("phone", phone_textinput.getEditText().getText().toString());
            mapdata.put("university", uni_name_textinput.getEditText().getText().toString());
            mapdata.put("department", selectedDepartment);
            mapdata.put("passout_year", passout_textinput.getEditText().getText().toString());
//            mapdata.put("city", city_name_textinput.getEditText().getText().toString());
//            mapdata.put("state", state_textinput.getEditText().getText().toString());
//            mapdata.put("country", country_textinput.getEditText().getText().toString());
            mapdata.put("email", emailEditText.getEditText().getText().toString());
            mapdata.put("date_time", FieldValue.serverTimestamp());

            FirebaseFirestore.getInstance().collection("account").document(guid).set(mapdata).addOnCompleteListener(task -> {
                Toast.makeText(signup_activity.this, "Successfully create account,Check email to verify", Toast.LENGTH_SHORT).show();
                firebaseAuth.getCurrentUser().sendEmailVerification();
                firebaseAuth.signOut();
                finish();
            });
        }
        return false;
    }
}