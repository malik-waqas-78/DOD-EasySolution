package com.easysolutions.dod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Messaging extends AppCompatActivity {
    String cusName,cusNo,proNo,id;
    EditText text;
    DatabaseReference databaseReference;
    Calendar calendar;
    RecyclerView listView;
    MessageAdapter adapter;
    ArrayList<MsgDetails> mesgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent intent=getIntent();
        cusName=intent.getStringExtra("proName");
        cusNo=intent.getStringExtra("proNo");
        proNo=intent.getStringExtra("cusNo");
        id=intent.getStringExtra("idNo");
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(cusName);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        calendar=Calendar.getInstance();
        listView=findViewById(R.id.msg_listview);
        mesgs=new ArrayList<>();

        adapter=new MessageAdapter(this,mesgs);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        listView.scrollToPosition(mesgs.size()-1);
        getMessages();
    }

    private void getMessages() {
        databaseReference.child("Chat").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MsgDetails msg = dataSnapshot.getValue(MsgDetails.class);
                if (msg.getMsgBy().equals("cus")) {
                    msg.setViewType(0);
                } else {
                    msg.setViewType(1);
                }
                Log.d("92727", "msg: " + msg.getMsgText());
                Log.d("92727", "msg: " + msg.getMsgTime());
                Log.d("92727", "msg: " + msg.getMsgId());
                Log.d("92727", "msg: " + msg.getMsgBy());
                mesgs.add(msg);
                adapter.notifyItemInserted(mesgs.size() - 1);
                listView.scrollToPosition(mesgs.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(View view) {
        text=findViewById(R.id.msg_input);
        if(text.getText().toString().length()<=0){
            return;
        }
        String msg=text.getText().toString();
        int h,m,s;
        h=calendar.get(Calendar.HOUR_OF_DAY);
        m=calendar.get(Calendar.MINUTE);
        s=calendar.get(Calendar.SECOND);
        String patch = h>=12?" PM\n":" AM\n";
        h=(h>=12?h-12:h);
        String hh=(h<=9?"0"+h:""+h);
        String mm=(m<=9?"0"+m:""+m);
        String ss=(s<=9?"0"+s:""+s);
        int y,M,d;
        y=calendar.get(Calendar.YEAR);
        M=calendar.get(Calendar.MONTH);
        d=calendar.get(Calendar.DAY_OF_MONTH);
        String date=""+y+M+d;
        String Time=hh+":"+mm+":"+ss+patch+d+"/"+M+"/"+y;
        String msgid=System.currentTimeMillis()+date;
        MsgDetails msgDetails=new MsgDetails(msgid,"cus",msg,Time);
        databaseReference.child("Chat").child(id).child(msgid).setValue(msgDetails);
        text.setText("");
        listView.scrollToPosition(mesgs.size()-1);
    }
}
