package examwizhackathon.ibankang.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import examwizhackathon.ibankang.com.Authentication.login_activity;
import examwizhackathon.ibankang.com.Authentication.splash_screen_activity;
import examwizhackathon.ibankang.com.SubAdmin.subadmin_exam_activity;
import examwizhackathon.ibankang.com.adminUi.AdminCalendarFragment;
import examwizhackathon.ibankang.com.adminUi.AdminHomeFragment;
import examwizhackathon.ibankang.com.adminUi.AdminSearchFragment;
import examwizhackathon.ibankang.com.adminUi.subadmin_activity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String account_type = "user", username, email;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    AppCompatSpinner spinner;
    String selectedYear = "1st Year";
    boolean userSelect = false; // Flag to track user selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new AdminHomeFragment());


        // Get reference to the spinner
        spinner = findViewById(R.id.year_spinner);
        spinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_search_24));

        // Define an array of options

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the spinner's adapter
                // Check if the selection is user-initiated
                if (userSelect) {
                    // Get the selected item from the spinner's adapter
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    selectedYear = selectedItem;

                    // Do something with the selected item
                    // For example, you can display it in a Toast
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                } else {
                    // If the selection is not user-initiated, set the flag to true
                    userSelect = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected (optional)
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new AdminHomeFragment());
                } else if (item.getItemId() == R.id.search) {
                    replaceFragment(new AdminSearchFragment());
                } else if (item.getItemId() == R.id.calander) {
                    replaceFragment(new AdminCalendarFragment());
                }
                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menu_btn = (ImageView) findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //
                    NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);

                    ((ImageView)findViewById(R.id.user_photo_img)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, profile_activity.class));
                        }
                    });
                    TextView usernameTextView = headerView.findViewById(R.id.user_name_txt);
                    TextView emailTextView = headerView.findViewById(R.id.user_email_id_txt);
                    usernameTextView.setText(username);
                    emailTextView.setText(email);

                    // Open drawer

                    drawerLayout.openDrawer(GravityCompat.START);
//                    NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
                    if (account_type.equals("admin")){
                        navigationView.getMenu().findItem(R.id.nav_subamin).setVisible(true);
                    }

                    if (account_type.equals("subadmin")){
                        navigationView.getMenu().findItem(R.id.nav_subamin_exam).setVisible(true);
                    }

                    Toast.makeText(MainActivity.this, account_type, Toast.LENGTH_SHORT).show();
                    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            int id = item.getItemId();
                            if (id == R.id.nav_home) {
                                startActivity(new Intent(MainActivity.this, splash_screen_activity.class));
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                            else if (id == R.id.nav_subamin) {

                                startActivity(new Intent(MainActivity.this, subadmin_activity.class));
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }

                            else if (id == R.id.nav_subamin_exam) {

                                startActivity(new Intent(MainActivity.this, subadmin_exam_activity.class));
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }

                            else if (id == R.id.nav_logout) {
                                logoutUser();
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        }


                    });
                }

            }
        });


        setUserInfoInDrawer();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void logoutUser() {
        // Log out the user using Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Open the login activity
        openLoginActivity();
    }

    // Method to open the login activity
    private void openLoginActivity() {
        Log.d("MainActivity", "Logging out and opening LoginActivity");
        Intent intent = new Intent(this, login_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // finish();
    }

    private void setUserInfoInDrawer() {


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
                            // Retrieve user data
                            username = document.getString("name");
                            email = document.getString("email");
                            account_type = document.getString("account_type");

                            // Set user data in drawer header

                            updateUIWithUserData();
                        }
                    } else {
                        Log.d("MainActivity", "Document retrieval failed:", task.getException());
                    }
                }
            });
        }
    }

    private void updateUIWithUserData() {
        // Update account type UI
        TextView account_type_txt = findViewById(R.id.account_type_txt);
        LinearLayout year_dropdown_layout = findViewById(R.id.year_layout);
        LinearLayout account_type_layout = findViewById(R.id.account_type_layout);

        if (account_type.equals("admin")) {
            account_type_txt.setText("Admin");
            year_dropdown_layout.setVisibility(View.GONE);
        } else if (account_type.equals("subadmin")) {
            account_type_txt.setText("Subadmin");
            year_dropdown_layout.setVisibility(View.GONE);
        } else if (account_type.equals("user")) {
            account_type_layout.setVisibility(View.GONE);
            year_dropdown_layout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        // Show the correct account type in Toast
        Toast.makeText(MainActivity.this, account_type, Toast.LENGTH_SHORT).show();
    }
}