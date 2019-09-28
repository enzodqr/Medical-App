package com.app.medical.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/* Show and manage the user's personal information
 *  Must show:
 *   Name
 *   Last name
 *   Phone number
 *   Blood type
 *   Address
 *   Emergency contact
 * */

public class Profile_Activity extends AppCompatActivity {

    /* Recycler, Model and Adapter variables */
    ArrayList<Profile_Model> profile_list;
    RecyclerView recyclerView;
    User_Model user;

    /* Firebase variables */

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DocumentReference documentReference;

    /* Layout elements variables */

    TextView profile_name_txt;
    ImageButton back_btn;


    /* onCreate method contains:
    *   the variables instances
    *   sets the recycler and adapter for the activity
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_name_txt = findViewById(R.id.profile_name);
        back_btn = findViewById(R.id.profile_back);

        profile_list = new ArrayList<>();
        recyclerView = findViewById(R.id.Profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.
                VERTICAL, false));

        get_user_data();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_to_menu();
            }
        });
    }

    /*
    *   Gets the user's data from the DB base on it's id and send it to the adapter
    * */
    public void get_user_data(){

        Log.d("Data user id", "Data: " + auth.getUid());

        documentReference = FirebaseFirestore.getInstance()
                .document(DB_Utilities.USERS+"/"+auth.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    user = documentSnapshot.toObject(User_Model.class);

                    profile_name_txt.setText(user.getName());

                    profile_list.add(new Profile_Model(DB_Utilities.TAG_ID, String.valueOf(user.getId())));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_AGE, String.valueOf(user.getAge())));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_GENDER, user.getGender()));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_PHONE, String.valueOf(user.getPhone())));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_EMERGENCY_CONTACT, String.valueOf(user.getEmergency_contact())));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_ADDRESS, user.getAddress()));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_BLOOD_TYPE, user.getBlood_type()));
                    profile_list.add(new Profile_Model(DB_Utilities.TAG_NATIONALITY, user.getNationality()));

                    Profile_Adapter adapter = new Profile_Adapter(profile_list);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    /*
     *   Back functionality of androids interface's back btn
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return_to_menu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    *   Return to menu function,
    *   handles the startActivity and the intent
    * */
    private void return_to_menu(){
        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
        finish();
    }
}