package examwizhackathon.ibankang.com.SubAdmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import examwizhackathon.ibankang.com.R;

public class subadmin_upload_exam_schedule_activity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final String TAG = "UploadCSVActivity";

    //    RecyclerView recyclerView;
//    SharedPreferences sharedpreferences;
//    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    //    TextView textView;
//    private static final int GALLERY_REQUEST_CODE = 1;
    public String guid = null, exam_uid = null;
    //    private int columnindex = 0;
//    private static final int REQUEST_CODE_CSV = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subadmin_upload_exam_schedule_activity);

        exam_uid = getIntent().getStringExtra("exam_uid");

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user ID
        guid = firebaseUser.getUid();
//        // Check if permission is granted
//        if (checkPermission()) {
//            // Permission is already granted
//            openFilePicker();
//        } else {
//            // Permission is not granted, request permission
//            requestPermission();
//        }

        // Call file picker when activity starts
        openFilePicker();


//        ((ImageView)findViewById(R.id.upload_schedule_btn_img)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //new FetchCSVTask(textView, columnindex).execute(exam_uid);
//                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, FILE_SELECT_CODE);
//            }
//        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a CSV file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        readCSVFile(inputStream);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading CSV file", e);
                }
            }
        }
    }

    private void readCSVFile(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // Read the first line to get column names
            String headerLine = reader.readLine();
            if (headerLine != null) {
                // Parse column names
                String[] columns = headerLine.split(",");
                while ((headerLine = reader.readLine()) != null) {
                    // Parse CSV data and save to Firebase Firestore
                    saveToFirestore(parseCSV(columns, headerLine));

//                    Map<String, Object> mapdata = new HashMap<>();
//                    mapdata.put("exam_uid",exam_uid);
//                    mapdata.put("status", true);
//                    mapdata.put(String.valueOf(columns), String.valueOf(headerLine));
//                    mapdata.put("date_time", FieldValue.serverTimestamp());
//                    FirebaseFirestore.getInstance().collection("exam").document(exam_uid).set(mapdata);

                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file", e);
        }
    }

    private HashMap<String, String> parseCSV(String[] columns, String line) {
        // Parse CSV line and return a HashMap
        HashMap<String, String> data = new HashMap<>();
        String[] values = line.split(",");
        for (int i = 0; i < columns.length && i < values.length; i++) {
            // Use column names as keys
            data.put(columns[i], values[i]);
            data.put("exam_uid",exam_uid);
            data.put("date_time", String.valueOf(new Date()));
        }
        return data;
    }

    private void saveToFirestore(HashMap<String, String> data) {
        // Save data to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("schedule")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(subadmin_upload_exam_schedule_activity.this, "CSV data uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding document", e);
                    Toast.makeText(subadmin_upload_exam_schedule_activity.this, "Failed to upload CSV data", Toast.LENGTH_SHORT).show();
                });
    }

//    private boolean checkPermission() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.},
//                REQUEST_PERMISSION_CODE);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, open file picker
//                openFilePicker();
//            } else {
//                // Permission denied, show a message or handle it accordingly
//                Toast.makeText(this, "Permission denied. Cannot proceed.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//@Override
//protected void onResume() {
//    super.onResume();
//    Dexter.withContext(this).withPermissions(
//            Manifest.permission.INTERNET,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//
//    ).withListener(new MultiplePermissionsListener()
//    {
//        @Override
//        public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//
//        }
//        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken)
//        {
//            permissionToken.continuePermissionRequest();
//        }
//    }).check();
//}
}