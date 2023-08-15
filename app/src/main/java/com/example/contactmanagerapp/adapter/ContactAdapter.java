package com.example.contactmanagerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanagerapp.MainActivity;
import com.example.contactmanagerapp.R;
import com.example.contactmanagerapp.db.entity.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    //1- Variable
    private Context context;
    private ArrayList<Contact> contactlist;
    private MainActivity mainActivity;

    //2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
//        public TextView email;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
//            this.email = itemView.findViewById(R.id.email);
        }
    }

    public ContactAdapter(Context context,ArrayList<Contact> contacts,MainActivity mainActivity){
        this.context = context;
        this.contactlist= contacts;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.contact_list_item,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int positions) {
        final  Contact contact = contactlist.get(positions);

        holder.name.setText(contact.getName());
//        holder.email.setText(contact.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.addAndEditContacts(true,contact,positions);

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }
}
