package examwizhackathon.ibankang.com.SubAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.view_model;

public class subadmin_exam_schedule_details_adapter extends RecyclerView.Adapter<subadmin_exam_schedule_details_adapter.viewholder> {

    ArrayList<view_model> dataholder;
    Context context;

    public subadmin_exam_schedule_details_adapter(ArrayList<view_model> dataholder, Context context){
        this.dataholder = dataholder;
        this.context = context;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exam_schedule_list, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {
        final view_model temp = dataholder.get(position);
        holder.exam_room_no_txt.setText(temp.getText1());
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("exam");
        collectionReference
                .whereEqualTo("exam_uid",temp.getText10()).limit(1)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        for (QueryDocumentSnapshot s : task.getResult()){


                            holder.exam_title_txt.setText(s.getString("exam_title"));//1
//                                    s.getString("exam_category"),//2
//                                    s.getString("exam_date"),//3
//                                    s.getString("exam_start"),//4
//                                    //String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),//4
//                                    s.getString("exam_end"),//5
//                                    s.getString("seating_plan_live"),//6
//                                    "",//7
//                                    "",//8
//                                    "",//9
//                                    s.getString("exam_uid"));//10


                        }

                    }else
                    {

                    }
                });
//        holder.exam_category_txt.setText(temp.getText2());
//        //3
//        holder.exam_start_txt.setText(temp.getText4());
//        holder.exam_end_txt.setText(temp.getText5());
//        holder.exam_live_txt.setText(temp.getText6());
//        holder.subadmin_create_date.setText(temp.getText4());

//        holder.add_new_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, subadmin_upload_exam_schedule_activity.class);
//                intent.putExtra("exam_uid", temp.getText10());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                context.startActivity(intent);
//
//            }
//        });

//        holder.exam_delete_btn_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 FirebaseFirestore.getInstance().collection("schedule").whereEqualTo("exam_uid", temp.getText10()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                     @Override
//                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                         if (task.isSuccessful() && task.getResult().size()>0) {
//                             for (QueryDocumentSnapshot s:task.getResult()) {
//                                 CollectionReference accountRef = FirebaseFirestore.getInstance().collection("schedule");
//                                 accountRef.document(s.getId()).delete();
//
//                             }
//                             CollectionReference accountRef = FirebaseFirestore.getInstance().collection("exam");
//                             accountRef.document(temp.getText10()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                 @Override
//                                 public void onComplete(@NonNull Task<Void> task) {
//                                     Toast.makeText(context, "Exam Delete successfully", Toast.LENGTH_SHORT).show();
//                                     new Handler().postDelayed(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             dataholder.remove(position);
//                                             notifyItemRemoved(position);
//                                         }
//                                     }, 1000);
//                                 }
//                             });
//                         }
//                     }
//                 });
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        LinearLayout add_new_layout;
        TextView  exam_room_no_txt, exam_title_txt, exam_category_txt, exam_start_txt, exam_end_txt;
        ImageView exam_delete_btn_img;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            add_new_layout = itemView.findViewById(R.id.add_new_layout);
            // exam_date_txt = itemView.findViewById(R.id.exam_date_txt);
            exam_title_txt = itemView.findViewById(R.id.exam_title_txt);
//             exam_day_txt = itemView.findViewById(R.id.exam_day_txt);
            exam_room_no_txt = itemView.findViewById(R.id.exam_room_no_txt);
//            exam_category_txt = itemView.findViewById(R.id.exam_category_txt);
//            exam_live_txt = itemView.findViewById(R.id.exam_live_time_txt);
//            exam_start_txt = itemView.findViewById(R.id.exam_start_txt);
//            exam_end_txt = itemView.findViewById(R.id.exam_end_txt);
//            exam_delete_btn_img = itemView.findViewById(R.id._exam_delete_btn_img);
        }
    }
}
