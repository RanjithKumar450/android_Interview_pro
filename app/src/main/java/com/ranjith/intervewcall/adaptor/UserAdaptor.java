package com.ranjith.intervewcall.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ranjith.intervewcall.EmployeeDetailsActivity;
import com.ranjith.intervewcall.R;
import com.ranjith.intervewcall.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.RecyclerHolder> {

    List<UserModel> models;
    Context context;

    public UserAdaptor(List<UserModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdaptor.RecyclerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,null,false);

        return new UserAdaptor.RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdaptor.RecyclerHolder holder, int position) {
//        holder.useName.setText(models.get(position).getName());
        holder.emailTxt.setText(models.get(position).getEmail().toLowerCase());
        holder.useName.setText(ChangeToCamelCase(models.get(position).getName()));


//        ChangeToCamelCase(models.get(position).getName());
        holder.emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_id = models.get(position).getEmail().toString().toLowerCase();
                String[] emails = email_id.split(",");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,emails);
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(intent,"Choose An Email"));
            }
        });

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmployeeDetailsActivity.class);
                intent.putExtra("EMP_ID",models.get(position).getId().toString());

                context.startActivity(intent);
//                Toast.makeText(context, "id :" + models.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String ChangeToCamelCase(String name) {
        String[] words = name.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return (models!=null)?models.size() :0;
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        public TextView useName;
        public TextView emailTxt;
        public LinearLayout main_layout;
        public RecyclerHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            useName = (TextView) itemView.findViewById(R.id.useName);
            main_layout = (LinearLayout) itemView.findViewById(R.id.main_layout);
        }
    }
}
