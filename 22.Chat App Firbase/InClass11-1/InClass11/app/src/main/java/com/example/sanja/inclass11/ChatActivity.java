package com.example.sanja.inclass11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    TextView textViewName;

    ImageButton buttonSend;
    ImageButton buttonGallery;
     EditText editTextNewMesaage;

    ImageButton imageButtonLogout;

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef=mRootRef.child("message");

    ArrayList<Message> messageArrayList=new ArrayList<>();
    LinearLayout linearLayout;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();                      //points to the root node

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear);
        mAuth = FirebaseAuth.getInstance();
        imageButtonLogout=(ImageButton)findViewById(R.id.btnLogout);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutContainer);
        setTitle("Chat");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String name=firebaseUser.getDisplayName();
        Log.d("Display Namae",name);
        textViewName=(TextView)findViewById(R.id.txtViewUserName);
        textViewName.setText(name);


        editTextNewMesaage=(EditText)findViewById(R.id.editTextNewMessage);
        //editTextNewMesaage.setFocusable(false);
        buttonSend=(ImageButton) findViewById(R.id.btnSend);
        buttonGallery=(ImageButton)findViewById(R.id.btnGallery);

        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(ChatActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"User has been logged out",Toast.LENGTH_LONG).show();
                finish();


            }
        });


        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                messageArrayList=new ArrayList<Message>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Message message=postSnapshot.getValue(Message.class);
                    Log.d("Test:",message.toString());
                    messageArrayList.add(message);
                }

                for(int i=0;i<messageArrayList.size();i++)
                {
                    final Message message=messageArrayList.get(i);
                    if(message.getType()==0)
                    {
                        LayoutInflater inflator = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.message_layout,null);
                        linearLayout.addView(linearLayout1);

                        TextView txtViiewMessage = (TextView)linearLayout1.findViewById(R.id.txtMessage);
                        txtViiewMessage.setText(message.getMessage());
                        TextView txtViewName = (TextView)linearLayout1.findViewById(R.id.txtViewSenderName);
                        txtViewName.setText(message.getDispName());

                        TextView txtVieTime = (TextView)linearLayout1.findViewById(R.id.txtViewTIme);
                        PrettyTime prettyTime=new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong(message.getTime()))));
                        final ImageButton button=(ImageButton)linearLayout1.findViewById(R.id.imageButton);
                        button.setTag(message);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ImageButton button=(ImageButton)v;
                                final Message message1=(Message) button.getTag();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                builder.setTitle("Comment");

                                final EditText cityInput = new EditText(ChatActivity.this);
                                cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                cityInput.setHint("Enter your comment") ;
                                builder.setView(cityInput);

                                final LinearLayout ll = new LinearLayout(ChatActivity.this);
                                ll.setOrientation(LinearLayout.VERTICAL);
                                ll.addView(cityInput);
                                builder.setView(ll);

                                builder.setPositiveButton("Send", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String strCityInputString = cityInput.getText().toString();
                                        Message message2=new Message();
                                        int type=2;
                                        Date date=new Date();
                                        long epoch=date.getTime();
                                        String time=""+epoch;
                                        message2.setType(type);
                                        message2.setMessage(strCityInputString);
                                        message2.setTime(time);
                                        message2.setEmail(firebaseUser.getEmail());
                                        message2.setDispName(firebaseUser.getDisplayName());
                                        message1.messageArrayList.add(message2);
                                        mConditionRef.child(message1.getDispName()+message1.getTime()).setValue(message1);
                                        Log.d("check123",""+message1.toString());
                                        mConditionRef.child(message1.getDispName()+message1.getTime()).setValue(message1);
                                        Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_LONG).show();


                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        });
                        Log.d("Testing123",""+message.getMessageArrayList().size());
                        if(message.getMessageArrayList().size()>0)
                        {
                            for(int j=0;j<message.getMessageArrayList().size();j++)
                            {


                                Message comment=message.getMessageArrayList().get(j);
                                LayoutInflater inflator1 = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                                LinearLayout linearLayout2 = (LinearLayout) inflator1.inflate(R.layout.comment_layout,null);
                                linearLayout.addView(linearLayout2);

                                TextView viewComment=(TextView)linearLayout2.findViewById(R.id.txtViewComment);
                                viewComment.setText(comment.getMessage());

                                TextView viewName=(TextView)linearLayout2.findViewById(R.id.txtViewSenderName);
                                viewName.setText(comment.getDispName());

                                TextView viewTime=(TextView)linearLayout2.findViewById(R.id.textViewTimeComment);
                                PrettyTime prettyTime1=new PrettyTime();
                                viewTime.setText(prettyTime.format(new Date(Long.parseLong(comment.getTime()))));




                            }


                        }




                    }
                    else if(message.getType()==1)
                    {

                        LayoutInflater inflator = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout1 = (LinearLayout) inflator.inflate(R.layout.image_layout,null);
                        linearLayout.addView(linearLayout1);

                        ImageView imgView = (ImageView)linearLayout1.findViewById(R.id.imageView);
                        Picasso.with(ChatActivity.this).load(message.getMessage()).fit().centerCrop().into(imgView);

                        TextView txtViewName = (TextView)linearLayout1.findViewById(R.id.txtViewSenderName);
                        txtViewName.setText(message.getDispName());

                        TextView txtVieTime = (TextView)linearLayout1.findViewById(R.id.txtViewTime1);
                        PrettyTime prettyTime=new PrettyTime();
                        txtVieTime.setText(prettyTime.format(new Date(Long.parseLong(message.getTime()))));

                        ImageButton buttonDelete=(ImageButton)linearLayout1.findViewById(R.id.imageButton2);
                        buttonDelete.setTag(message);
                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageButton button=(ImageButton)v;
                                final Message Messa=(Message)button.getTag();
                                Log.d("Test",Messa.toString());
                                storageRef.child("images/"+Messa.getDispName()+Messa.getTime()+".JPEG").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Test","test");
                                        mConditionRef.child(Messa.getDispName()+Messa.getTime()).removeValue();
                                        Toast.makeText(ChatActivity.this,"Deleted the post",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                        final ImageButton button=(ImageButton)linearLayout1.findViewById(R.id.imageButton3);
                        button.setTag(message);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ImageButton button=(ImageButton)v;
                                final Message message1=(Message) button.getTag();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                builder.setTitle("Comment");

                                final EditText cityInput = new EditText(ChatActivity.this);
                                cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                cityInput.setHint("Enter your comment") ;
                                builder.setView(cityInput);

                                final LinearLayout ll = new LinearLayout(ChatActivity.this);
                                ll.setOrientation(LinearLayout.VERTICAL);
                                ll.addView(cityInput);
                                builder.setView(ll);

                                builder.setPositiveButton("Send", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String strCityInputString = cityInput.getText().toString();
                                        Message message2=new Message();
                                        int type=2;
                                        Date date=new Date();
                                        long epoch=date.getTime();
                                        String time=""+epoch;
                                        message2.setType(type);
                                        message2.setMessage(strCityInputString);
                                        message2.setTime(time);
                                        message2.setEmail(firebaseUser.getEmail());
                                        message2.setDispName(firebaseUser.getDisplayName());
                                        message1.messageArrayList.add(message2);
                                        mConditionRef.child(message1.getDispName()+message1.getTime()).setValue(message1);
                                        Log.d("check123",""+message1.toString());
                                        mConditionRef.child(message1.getDispName()+message1.getTime()).setValue(message1);
                                        Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_LONG).show();


                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        });

                        Log.d("Testing123",""+message.getMessageArrayList().size());
                        if(message.getMessageArrayList().size()>0)
                        {
                            for(int j=0;j<message.getMessageArrayList().size();j++)
                            {


                                Message comment=message.getMessageArrayList().get(j);
                                LayoutInflater inflator1 = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                                LinearLayout linearLayout2 = (LinearLayout) inflator1.inflate(R.layout.comment_layout,null);
                                linearLayout.addView(linearLayout2);

                                TextView viewComment=(TextView)linearLayout2.findViewById(R.id.txtViewComment);
                                viewComment.setText(comment.getMessage());

                                TextView viewName=(TextView)linearLayout2.findViewById(R.id.txtViewSenderName);
                                viewName.setText(comment.getDispName());

                                TextView viewTime=(TextView)linearLayout2.findViewById(R.id.textViewTimeComment);
                                PrettyTime prettyTime1=new PrettyTime();
                                viewTime.setText(prettyTime.format(new Date(Long.parseLong(comment.getTime()))));




                            }


                        }


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editTextNewMesaage.getText().toString();
                if(message.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"No Messages",Toast.LENGTH_LONG).show();
                }
                else
                {
                    sendMessage(message);
                }
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);

            }
        });
    }

    private void sendMessage(String message) {

        Message message1=new Message();
        int type=0;
        Date date=new Date();
        long epoch=date.getTime();

        String time=""+epoch;
        message1.setType(type);
        message1.setMessage(message);
        message1.setTime(time);
        message1.setEmail(firebaseUser.getEmail());
        message1.setDispName(firebaseUser.getDisplayName());
        mConditionRef.child(message1.getDispName()+time).setValue(message1);
        Log.d("check",""+message1.toString());



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 &&resultCode==RESULT_OK &&data!=null &&data.getData()!=null)
        {
            Uri uri=data.getData();
            try
            {
                final Message message1=new Message();
                int type=1;
                Date date=new Date();
                long epoch=date.getTime();

                final String time=""+epoch;
                message1.setType(type);
               // message1.setMessage(message);
                message1.setTime(time);
                message1.setEmail(firebaseUser.getEmail());
                message1.setDispName(firebaseUser.getDisplayName());

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                StorageReference imageRef=storageRef.child("images/"+ firebaseUser.getDisplayName()+time+".JPEG");
                UploadTask uploadTask = imageRef.putBytes(data1);
                uploadTask.addOnSuccessListener(ChatActivity.this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                        message1.setMessage(downloadUrl.toString());
                        mConditionRef.child(message1.getDispName()+time).setValue(message1);
                        Log.d("check",""+message1.toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });


            }catch (Exception e)
            {
                Log.d("Exception",e.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Click Logout Button",Toast.LENGTH_LONG).show();

    }
}
