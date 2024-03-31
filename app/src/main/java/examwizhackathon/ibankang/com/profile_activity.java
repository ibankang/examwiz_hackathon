package examwizhackathon.ibankang.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile_activity extends AppCompatActivity {

    String guid = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

//        ImageView edit_img = findViewById(R.id.edit_prifile_img);
//        edit_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        get_profile_data();

        // Remove this click listener setup
//        ((TextView)findViewById(R.id.edit_prifile_img)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(profile_activity.this, splash_screen_activity.class));
//            }
//        });




    }

    void get_profile_data(){


        // Get Firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            // Get user ID
            String userId = firebaseUser.getUid();

            // Get Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Get document reference
            DocumentReference docRef = db.collection("account").document(userId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                                if (document.getString("photo")!= null) {
//                                    ImageView user_img = findViewById(R.id.profileImage);
//                                    Picasso.get().load(document.getString("photo")).into(user_img);
//                                }
                            // Retrieve user data
                            String username = document.getString("name");
                            String email = document.getString("email");
                            String uni_name = document.getString("university");
                            String account_type = document.getString("account_type");
                            String dept = document.getString("department");
                            String rollno = document.getString("roll_no");
                            String regno = document.getString("reg_no");
                            String mobile = document.getString("phone");
                            String passout = document.getString("passout_year");

                            // Set user data in drawer header
                            TextView usernameTextView = findViewById(R.id.username_txt);
                            TextView emailTextView = findViewById(R.id.user_email);
                            TextView uni_name_txt = findViewById(R.id.uni_name_txt);
                            TextView account_type_txt = findViewById(R.id.account_type);
                            TextView dept_txt = findViewById(R.id.dept_name_txt);
                            TextView rollno_txt = findViewById(R.id.roll_txt);
                            TextView regno_txt = findViewById(R.id.reg_txt);
                            TextView mobile_txt = findViewById(R.id.user_phone);
                            TextView passout_txt = findViewById(R.id.passout_txt);

                            usernameTextView.setText(username);
                            emailTextView.setText(email);
                            uni_name_txt.setText(uni_name);
                            account_type_txt.setText(account_type);
                            dept_txt.setText(dept);
                            rollno_txt.setText(rollno);
                            regno_txt.setText(regno);
                            mobile_txt.setText(mobile);
                            passout_txt.setText(passout);

                        }
                    } else {
                        Log.d("MainActivity", "Document retrieval failed:", task.getException());
                    }
                }
            });
        }
    }

//    // Method to open the gallery
//    private void openGallery() {
//        Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    // Handle the result from the gallery intent
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//
//            // Update the profile picture in your app UI
//            ImageView profileImageView = findViewById(R.id.profileImage);
//            profileImageView.setImageURI(imageUri);
//
//            // Upload the new profile picture to Firebase Storage
//            uploadProfilePicture(imageUri);
//        }
//    }
//
//    // Method to upload profile picture to Firebase Storage
//    private void uploadProfilePicture(Uri imageUri) {
//        // Get reference to Firebase Storage
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + UUID.randomUUID().toString());
//
//        // Upload file to Firebase Storage
//        storageRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get the download URL of the uploaded file
//                        Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        downloadUriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri downloadUri) {
//                                // Update the profile picture URL in Firebase Firestore or Realtime Database
//                                updateProfilePictureUrl(downloadUri.toString());
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle any errors
//                        Toast.makeText(profile_activity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    // Method to update profile picture URL in Firebase Firestore or Realtime Database
//    private void updateProfilePictureUrl(String imageUrl) {
//        // Update the profile picture URL in your database
//        // For example, if using Firestore:
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference userRef = db.collection("account").document(guid); // Replace "user_id" with the actual user ID
//        userRef.update("profile_picture_url", imageUrl)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Profile picture URL updated successfully
//                        Toast.makeText(profile_activity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle any errors
//                        Toast.makeText(profile_activity.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}