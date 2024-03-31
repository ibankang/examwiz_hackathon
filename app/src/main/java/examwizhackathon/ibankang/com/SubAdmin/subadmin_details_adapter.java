package examwizhackathon.ibankang.com.SubAdmin;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import examwizhackathon.ibankang.com.R;
import examwizhackathon.ibankang.com.view_model;

public class subadmin_details_adapter extends RecyclerView.Adapter<subadmin_details_adapter.viewholder> {

    ArrayList<view_model> dataholder;
    Context context;

    public subadmin_details_adapter(ArrayList<view_model> dataholder, Context context){
        this.dataholder = dataholder;
        this.context = context;
    }
    @NonNull
    @Override
    public subadmin_details_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_subadmin_list, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subadmin_details_adapter.viewholder holder, int position) {
        final view_model temp = dataholder.get(position);
        holder.subadmin_name_txt.setText(temp.getText2());
        if (temp.getText1().length()>20) {
            Picasso.get().load(temp.getText1()).into(holder.subadmin_profile_img);
        }
        holder.subadmin_email_txt.setText(temp.getText3());
//        holder.subadmin_create_date.setText(temp.getText4());

//        holder.layout_cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, plant_details_activity.class);
//                intent.putExtra("reg_uid", temp.getText10());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                context.startActivity(intent);
//
//            }
//        });

        holder.more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> mapdata = new HashMap<>();
                mapdata.put("account_type", "user");
                mapdata.put("admin_uid", "null");
                mapdata.put("subaccount_time", FieldValue.serverTimestamp());

                CollectionReference accountRef = FirebaseFirestore.getInstance().collection("account");
                accountRef.document(temp.getText10()).update(mapdata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Subadmin removed successfully", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dataholder.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    }, 1000);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CardView layout_cardview;

        TextView subadmin_name_txt, subadmin_email_txt, subadmin_create_date;
        ImageView subadmin_profile_img, more_img;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            layout_cardview = itemView.findViewById(R.id.layout_cardview);
            subadmin_profile_img = itemView.findViewById(R.id.profileImage);
            subadmin_name_txt = itemView.findViewById(R.id.subadmin_name_txt);
            subadmin_email_txt = itemView.findViewById(R.id.subadmin_email_txt);
//            subadmin_create_date = itemView.findViewById(R.id.subadmin_create_date);
            more_img = itemView.findViewById(R.id.more_img);
        }
    }
}
