package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.chat.adapter.ChatAdapter;
import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ET_message)
    EditText ET_Message;
    @BindView(R.id.FloatingActionButton)
    android.support.design.widget.FloatingActionButton FloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);


        FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SaveData();

                ReturnData();

            }
        });
    }

    private void SaveData() {
//____________________________

        Message message = new Message(); // Class Message de include 2->Variable ( email ) & ( message )

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // value--> username El da5l 5laas w el tam ta5zenooo Fe class Message in Variable Name(email)
        message.setEmail(email); // Save class Message in Variable Name(email)


        String mes = ET_Message.getText().toString() ; // value--> message El atkaataabeet  5laas w el tam ta5zenhaaaaaaa Fe class Message in Variable Name(message)

        ET_Message.setText(""); // hnaaa ana bb3t Fe EditText El bakteb Fehaaa El message bab3t Text Faaaadyyyy فارغ because display the EditText فارغ بعد لما اكتب الرساله واضغط على الزرار علشان ارسلهااا

        message.setMessage(mes); // Save class Message in Variable Name(message)
//_______________________________________________________________________________________________________________________________________________________________________________________

// (For "Save" in Database(Firebase))
//__________________________________

// ---> El data de el mawgodaaa Fe Class Message in Variable_Name( email ) & Variable_Name( message ) W 3amalt Object mn Class Message W ba3atooo LL Firebase


        String key = FirebaseDatabase.getInstance().getReference() // hna ana bag3al el key el 4ayl el mail & message automatic (dynamic)
                // ya 3niii kol lma a3ml massage gdeeeda --> create new key ---> save el mail & message
                .child("Chat")
                .push()
                .getKey();


        FirebaseDatabase.getInstance().getReference() //getReference()---> method btrg3 reference mn Root "Chat" or refer Root "Chat"
                .child("Chat")
                .child(key)
                .setValue( message ) // Save Value of Object(message) in child --> (Root "Chat" ) --> in database( Firebase )
                // Object(message) include 2->Value ( "value"--> email ) & ( "value"--> message )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            //
                        }else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//_________________________________________________________________________________________________________________________________________________________________


    private void ReturnData() {
//______________________________


// (For "Return" data from Database(Firebase))
//__________________________________

// ---> El data de el kant mawgodaaa Fe Class Message in Variable_Name(email) & Variable_Name(message) W ba3atahaa Fe El Firebase By Object mn Class Message

        FirebaseDatabase.getInstance().getReference()
                .child("Chat")
                .addValueEventListener(new ValueEventListener() { // Return Value of Object(message) in child --> Root (Chat) --> in database
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<Message> messageList = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren())
                        {
                            Message message = child.getValue(Message.class);

                            messageList.add(message);
                        }

                        recyclerView(messageList); // Method el recyclerView

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


//_________________________________________________________________________________________________________________________________________________________________


    private void recyclerView(List<Message> messageList) {

        ChatAdapter adapter = new ChatAdapter(MainActivity.this , messageList );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, Login_Activity.class);
                startActivity(intent);
                finish();
        }


        return true;
    }
}


// (For "Save" in Database(Firebase))--------->Dummy
//___________________________________________________


//                FirebaseDatabase.getInstance().getReference()
//                        .child("messages")
//                        .child("3")
//                        .child("email")

//                        .setValue(message) // Save Value in child --> from Root --> in database

//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });



//_____________________________________________________________________________________________________________________________________________

// (For "Return" data from Database(Firebase))--------->Dummy
//____________________________________________________________

//                FirebaseDatabase.getInstance().getReference()
//                        .child("messages")
//                        .child("3")
//                        .child("email")
//                        .addValueEventListener(new ValueEventListener() { // Return Value in child --> Root --> in database
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String msg = dataSnapshot.getValue(String.class); // (String.class) no3 el data elrag3a mn 3amalet el save --> el 7aga el ana 5azentaah fe el save kant string
// yb2a lazm a2ol fe 3amalet el return no3 el daa el tam ta5zenhaa we el kan no3 el data (String.class)
//                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });


//________________________________________________________________________________________________________________________________________________

//________________________________________________________________________________________________________________________________________________________________


//    private void recyclerView() {
//
//        ChatAdapter adapter = new ChatAdapter(MainActivity.this ,getDummyObjectList());
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }

//__________________________________________________________________________________________________

//    private List<Message> getDummyObjectList() { // method btrag3 object mn El List<Meaasge> ya3nii no3 El list de Object Mn Message
//
//        List<Message> messages = new ArrayList<>(); // ba3ml list W ba3mal object mnhaaa (message).
//                                                    // el mafrod en list de bta7ml Objects mn Messages
//
//        Message m = new Message(); // tab3n el Object mn class Message bykooon mat5azen fe aw mawgod fe data 3la 7asb el Attributes el 3andoo
//                                    // tab3an ana lama bb3t Object mn el list el bta7ml magmo3a mn el Objects Mn class Message.
//                                    // w kol object bykon mawgod bda5l el Attributes bta3tooo data
//                                    // El data de el ana bast2blhaa fe el Adapter wa5od kol wa7da mn el data de wa3ml feha el ana 3ayzoooo .
//
//        m.setEmail("Yehia_hd");
//        m.setMessage("This is Message");
//
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);             // da kda el mafrod 3add el Object el bta7ml el data el hatzhr fe El recycler
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//        messages.add(m);
//
//        return messages;
//
//    }

//_____________________________________________________________________________________________________________________________________________________

