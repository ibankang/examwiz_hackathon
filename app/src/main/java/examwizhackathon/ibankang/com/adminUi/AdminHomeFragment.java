package examwizhackathon.ibankang.com.adminUi;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.SubAdmin.subadmin_exam_details_adapter;
import examwizhackathon.ibankang.com.SubAdmin.subadmin_exam_schedule_details_adapter;
import examwizhackathon.ibankang.com.view_model;


public class AdminHomeFragment extends Fragment {

    String account_type = "user";
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    TextView textView;
    String guid = null, exam_uid = null;

    View view;
    String rollno = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user ID
        guid = firebaseUser.getUid();

        db = FirebaseFirestore.getInstance();

        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        // Retrieve year_uid from arguments
        if (getArguments() != null) {
            rollno = getArguments().getString("rollno");
        }

        get_exam_details();


        //slider
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider5, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        get_user_rollno();
    }

    void get_exam_details(){

        if (account_type.equals("admin") || account_type.equals("subadmin")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
            LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mlinearLayoutManager);
            ArrayList<view_model> dataholder = new ArrayList<>();
            subadmin_exam_details_adapter wAdapter = new subadmin_exam_details_adapter(dataholder, getContext());
            CollectionReference collectionReference = db.collection("exam");
            collectionReference
                    //.orderBy("date_time",Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            for (QueryDocumentSnapshot s : task.getResult()) {

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
                        } else {

                        }
                    });
        }

    }

    private void get_exam_details_user() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();
        subadmin_exam_schedule_details_adapter wAdapter = new subadmin_exam_schedule_details_adapter(dataholder, getContext());
        CollectionReference collectionReference = db.collection("schedule");
        collectionReference
                .whereEqualTo("rollno",rollno)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot s : task.getResult()) {

                            view_model obj = new view_model(
                                    s.getString("roomno"),//1
//                                    s.getString("exam_category"),//2
//                                    s.getString("exam_date"),//3
//                                    s.getString("exam_start"),//4
//                                    //String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),//4
//                                    s.getString("exam_end"),//5
//                                    s.getString("seating_plan_live"),//6
                                    "",//7
                                    "",//7
                                    "",//7
                                    "",//7
                                    "",//7
                                    "",//7
                                    "",//8
                                    "",//9
                                    s.getString("exam_uid"));//10

                            dataholder.add(obj);
                        }
                        recyclerView.setAdapter(wAdapter);
                    } else {

                    }
                });

    }

    private void get_user_rollno() {


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
                            rollno = document.getString("roll_no");
                            String name = document.getString("name");
                            Toast.makeText(getContext(), name+"_"+rollno, Toast.LENGTH_SHORT).show();

                            get_exam_details_user();
                        }
                    } else {
                        Log.d("MainActivity", "Document retrieval failed:", task.getException());
                    }
                }
            });
        }
    }

}