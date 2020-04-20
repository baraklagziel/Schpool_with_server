package com.example.loginschoolpool;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewAdapter> {

    private Context context;
    private TextView textViewTitle;
    private List<StudentMember>studentMemberList;
    private int studentMemberListSize = 0;

    public RecycleViewAdapter(Context context, List<StudentMember> studentMemberList) {
        this.context = context;
        this.studentMemberList = studentMemberList;
        this.studentMemberListSize = studentMemberList.size();
    }

    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_item_user,parent,false);
        return new MyViewAdapter(view,studentMemberListSize,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {

        holder.textView_member_name.setText(studentMemberList.get(position).getUsername().replaceAll("_"," "));
        Typeface typeface = ResourcesCompat.getFont(context,R.font.quicksand_medium);
        holder.textView_member_name.setTypeface(typeface);
        holder.textView_km.setText(studentMemberList.get(position).getKmFromMember());
        holder.textView_km.setTypeface(typeface);
        holder.imageView_member.setImageResource(studentMemberList.get(position).getThumbnail());

    }

    @Override
    public int getItemCount() {
        return studentMemberList.size();
    }

    public static class MyViewAdapter extends RecyclerView.ViewHolder{
        TextView textView_member_name;
        ImageView imageView_member;
        ImageView imageView_chat_icon;
        ImageView imageView_info_icon;
        TextView textView_title;
        TextView textView_km;



        public MyViewAdapter(@NonNull View itemView, int studentMemberListSize, Context context) {
            super(itemView);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            textView_member_name= (TextView)itemView.findViewById(R.id.textView_student_member_name);
            imageView_member= (ImageView)itemView.findViewById(R.id.imageView_student_member_pic);
            imageView_chat_icon= (ImageView)itemView.findViewById(R.id.chat_icon_cardview);
            textView_km= (TextView)itemView.findViewById(R.id.textView_km);

            imageView_chat_icon.setImageResource(R.drawable.chat_logo);
            imageView_info_icon= (ImageView)itemView.findViewById(R.id.info_icon_cardview);
            imageView_info_icon.setImageResource(R.drawable.info_logo);
        }
    }
}
