package com.example.contactmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactmanagerapp.adapter.ContactAdapter;
import com.example.contactmanagerapp.db.DatabaseHelper;
import com.example.contactmanagerapp.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // NO ROOM Database Project
    // Using SQLITE



    // Variables
    private ContactAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList  = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("To Do List App");


        // RecyclerVIew
        recyclerView = findViewById(R.id.recycler_view_contacts);
        db = new DatabaseHelper(this);

        // Contacts List
        contactArrayList.addAll(db.getAllContacts());

        contactsAdapter = new ContactAdapter(this, contactArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }
        });
    }

    public void addAndEditContacts(final boolean isUpdated,final Contact contact,final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contacts,null);

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alerDialogBuilder.setView(view);


        TextView contactTitle = view.findViewById(R.id.new_Contact_id);
        final EditText newContact = view.findViewById(R.id.name);
//        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add New Contact" : "Edit Contact");


        if (isUpdated && contact != null){
            newContact.setText(contact.getName());
//            contactEmail.setText(contact.getEmail());
        }

        alerDialogBuilder.setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isUpdated){
                                    DeleteContact(contact, position);
                                }else{
                                    dialogInterface.cancel();
                                }
                            }
                        }
                );

        final AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();

                    return;
                }else{
                    alertDialog.dismiss();
                }

                if (isUpdated && contact != null){
                    UpdateContact(newContact.getText().toString(),position);

                }else{
                    CreateContact(newContact.getText().toString());

                }

            }
        });

    }

    private void DeleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();


    }


    private void UpdateContact(String name, int position){
        Contact contact = contactArrayList.get(position);

        contact.setName(name);
     //   contact.setEmail(email);

        db.updateContact(contact);

        contactArrayList.set(position, contact);
        contactsAdapter.notifyDataSetChanged();


    }


    private void CreateContact(String name){

        long id = db.insertData(name);
        Contact contact = db.getContact(id);

        if (contact != null){
            contactArrayList.add(0, contact);
            contactsAdapter.notifyDataSetChanged();
         //   Toast.makeText(this, "id: "+id, Toast.LENGTH_SHORT).show();
        }

    }


    // Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}