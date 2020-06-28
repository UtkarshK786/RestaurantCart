package com.example.restaurantCart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantCart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {

    TextView t11,t12,t21,t22,t31,t32,tv,t13,t23,t33,tot;
    Button btn;
    CardView c1,c2,c3;
    int items,chnce=0,total_price=0;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        t11=findViewById(R.id.textView2);
        t21=findViewById(R.id.textView3);
        t31=findViewById(R.id.textView4);
        t12=findViewById(R.id.textView5);
        t22=findViewById(R.id.textView6);
        t32=findViewById(R.id.textView7);
        tot=findViewById(R.id.textView8);
        t13=findViewById(R.id.textView9);
        t23=findViewById(R.id.textView10);
        t33=findViewById(R.id.textView11);
        c1=findViewById(R.id.cardView);
        c2=findViewById(R.id.cardView2);
        c3=findViewById(R.id.cardView3);
        btn=findViewById(R.id.button2);
        firebaseAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference("users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flg=0,total=0;
                String itemz;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.i("okay",dataSnapshot1.getValue().toString());
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        if(dataSnapshot2.getValue().equals(firebaseAuth.getCurrentUser().getEmail())){
                            flg=1;
                        }
                        if(flg==1&&dataSnapshot2.getKey().equals("item1")){
                           int qty=0;
                           qty=Integer.parseInt(dataSnapshot2.getValue().toString());
                           if(qty>0) {
                               c1.setVisibility(View.VISIBLE);
                               t12.setText("QTY:" + qty);
                               t13.setText("Amount:" + (qty * 120)+"Rs.");
                               total += (qty * 120);
                           }
                        }
                        if(flg==1&&dataSnapshot2.getKey().equals("item2")){
                            int qty=0;
                            qty=Integer.parseInt(dataSnapshot2.getValue().toString());
                            if(qty>0) {
                                c2.setVisibility(View.VISIBLE);
                                t22.setText("QTY:" + qty);
                                t23.setText("Amount:" + (qty * 95)+"Rs.");
                                total += (qty * 95);
                            }
                        }
                        if(flg==1&&dataSnapshot2.getKey().equals("item3")){
                            int qty=0;
                            qty=Integer.parseInt(dataSnapshot2.getValue().toString());
                            if(qty>0) {
                                c3.setVisibility(View.VISIBLE);
                                t32.setText("QTY:" + qty);
                                t33.setText("Amount:" + (qty * 230)+"Rs.");
                                total += (qty * 230);
                            }
                        }
                        Log.i("okay2",dataSnapshot2.getKey());
                        if(dataSnapshot2.getKey().equals("items"))
                            dbRef.child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).setValue(items);
                     if(total>0) {
                         tot.setText("Total amount:" + total + "Rs.");
                         btn.setVisibility(View.VISIBLE);
                     }
                    else{
                        tot.setText("Cart is empty");
                        btn.setVisibility(View.GONE);
                     }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void shoppe(View view){
        Toast.makeText(this, "Thank you for shopping with us!", Toast.LENGTH_LONG).show();
    }
}
