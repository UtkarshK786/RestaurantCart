package com.example.restaurantCart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Home extends AppCompatActivity {

    TextView t11,t12,t21,t22,t31,t32,tv;
    Button b1,b2,b3;
    int items,chnce=0;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        t11=findViewById(R.id.textView2);
        t21=findViewById(R.id.textView3);
        t31=findViewById(R.id.textView4);
        t12=findViewById(R.id.textView5);
        t22=findViewById(R.id.textView6);
        t32=findViewById(R.id.textView7);
        b1=findViewById(R.id.button);
        b2=findViewById(R.id.button5);
        b3=findViewById(R.id.button4);
        firebaseAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference("users");
//        tv.setText("12");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_menu,menu);
        MenuItem item = menu.findItem(R.id.cart);
//        MenuItemCompat.setActionView(item, R.layout.resource);
//        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);
//        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
//        tv.setText(String.valueOf(items));
        return true;
    }
public void buttonClicked(final View view){
         items++;
    Toast.makeText(this, "item added, click on cart icon to continue", Toast.LENGTH_LONG).show();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flg=0;
                String itemz;
                if(view==b1)
                    itemz="item1";
                else if(view==b2)
                    itemz="item2";
                else itemz="item3";
//                Toast.makeText(Home.this, itemz, Toast.LENGTH_SHORT).show();
               for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                   Log.i("okay",dataSnapshot1.getValue().toString());
                   for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                       if(dataSnapshot2.getValue().equals(firebaseAuth.getCurrentUser().getEmail())){
                          flg=1;
//                           Log.i("hehe","yaha tak pohoch raha hai");
                       }
                       if(flg==1&&dataSnapshot2.getKey().equals(itemz)){
                           int cnt=Integer.parseInt(dataSnapshot2.getValue().toString());
                           Log.i("cnt",String.valueOf(cnt));
                           dbRef.child(dataSnapshot1.getKey()).child(itemz).setValue(cnt+1);
                           flg=0;
                       }
                       Log.i("okay2",dataSnapshot2.getKey());
                      if(dataSnapshot2.getKey().equals("items"))
                          dbRef.child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).setValue(items);

                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//    Toast.makeText(this, String.valueOf(items), Toast.LENGTH_SHORT).show();

}
public void cartzz(View view){
    startActivity(new Intent(getApplicationContext(),Cart.class));
}
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart: startActivity(new Intent(getApplicationContext(),Cart.class));
                            return true;
            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                Toast.makeText(this, "You are now logged out", Toast.LENGTH_SHORT).show();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
