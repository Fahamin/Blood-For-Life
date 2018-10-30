package app.seeker.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.model.DonorModel;
import app.seeker.bloodbank.model.User;
import app.seeker.bloodbank.utils.UserDetails;

public class profile extends AppCompatActivity {

    TextView nameTV, bgTv, cityTV, emailTv, phoneTV, helpTv, donatdateTV;
    EditText nameET, phoneET, emailET, cityET, donatedateET;

    Button profileBtn;//callBTN, smsBTN, emailBTN;
    String name, city, bloodGroup, phone, email, donatedate, country;
    ImageView phone_copy_IV, email_copy_IV;

    FirebaseAuth auth;
    FirebaseUser user;

    LinearLayout searchLayout, profileLayout;

    LinearLayout editCardly, updateCardLy;

    DatabaseReference databaseReference, databaseReferenceall;
    Spinner bgsp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Toast.makeText(this, "" + UserDetails.USER_COUNTRY, Toast.LENGTH_SHORT).show();


        intView();


        //   final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //  country = getUserCountry(profile.this);
        //  city = cityET.getText().toString().trim();
        //   bloodGroup = getBloodGroup();
        //   searchLayout.setVisibility(View.GONE);
        //  profileLayout.setVisibility(View.VISIBLE);

            /*    databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                        country + "/" + city.toLowerCase() + "/" + bloodGroup);

                databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DonorModel donorModel = dataSnapshot.getValue(DonorModel.class);
                        nameTV.setText(donorModel.getName());
                        bgTv.setText(donorModel.getBloodGroup());
                        cityTV.setText(donorModel.getCity());
                        donatdateTV.setText(donorModel.getDonateDate());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
    }


    private void intView() {

        setTitle("Profile");

        nameTV = findViewById(R.id._nameTV);
        bgTv = findViewById(R.id.bg_TV);
        cityTV = findViewById(R.id.city_TV);
        emailTv = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        donatdateTV = findViewById(R.id.donateDateTV);
        //   cityET = findViewById(R.id.cityET);
        bgsp = findViewById(R.id.blood_group_SP);
        helpTv = findViewById(R.id.helpTV);


        updateCardLy = findViewById(R.id.updateCard);
        editCardly = findViewById(R.id.editCardView);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
        cityET = findViewById(R.id.cityET);
        donatedateET = findViewById(R.id.donateDateET);


        loadDataFromFB();


    }


    private void loadDataFromFB() {


        if (UserDetails.USER_COUNTRY == null || UserDetails.USER_CITY == null || UserDetails.USER_MOBILE == null || UserDetails.USER_BLOOD_GROUP == null)
            return;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserDetails").child("Donor").child(UserDetails.USER_COUNTRY).child(UserDetails.USER_CITY.toLowerCase()).child(UserDetails.USER_BLOOD_GROUP);


        reference.child(UserDetails.USER_MOBILE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    DonorModel donorModel = dataSnapshot.getValue(DonorModel.class);


                    nameTV.setText(donorModel.getName());
                    cityTV.setText(donorModel.getCity());
                    emailTv.setText(donorModel.getEmail());
                    phoneTV.setText(donorModel.getPhoneNumber());
                    bgTv.setText(donorModel.getBloodGroup());
                    donatdateTV.setText(donorModel.getDonateDate());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public String getBloodGroup() {

        String bg = "";

        switch (bgsp.getSelectedItemPosition()) {

            case 1: {

                bg = "A+";
                break;

            }
            case 2: {

                bg = "A-";
                break;

            }
            case 3: {

                bg = "B+";
                break;

            }
            case 4: {

                bg = "B-";
                break;

            }
            case 5: {

                bg = "AB+";
                break;

            }
            case 6: {

                bg = "AB-";
                break;

            }
            case 7: {

                bg = "O+";
                break;

            }
            case 8: {

                bg = "O-";
                break;
            }
        }
        return bg;
    }


    private String getUserCountry(Context context) {

        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simcontry = tm.getSimCountryIso();
            if (simcontry != null && simcontry.length() == 2) {
                Locale locale = new Locale("", simcontry);
                return locale.getDisplayCountry();
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) {
                    Locale locale = new Locale("", networkCountry);
                    return locale.getDisplayCountry();
                }
            }
        } catch (Exception e) {

        }

        return null;
    }

    public void updatData(View view) {


        String curCountry = UserDetails.USER_COUNTRY;
        String curCity = UserDetails.USER_CITY;
        String curBG = UserDetails.USER_BLOOD_GROUP;
        String curNumber = UserDetails.USER_MOBILE;

        // at first delete previous data
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UserDetails").child("Donor").child(curCountry).child(curCity).child(curBG);

        reference1.child(curNumber).removeValue();

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("UserDetails").child("Donor").child(curCountry).child("All").child(curBG);

        reference2.child(curNumber).removeValue();


        final String name, city, country, bggrop, phone, email, donate_date;
        name = nameET.getText().toString().trim();
        city = cityET.getText().toString().trim().toLowerCase();
        country = getUserCountry(profile.this);
        bggrop = getBloodGroup();
        phone = phoneET.getText().toString().trim();
        //email = emailET.getText().toString().trim();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();  // keep email id unique
        donate_date = donatedateET.getText().toString().trim();


        // now we have new reference
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("UserDetails").child("Donor").child(country).child(city).child(bggrop);
        final DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("UserDetails").child("Donor").child(country).child("All").child(bggrop);


        final DonorModel donorModel = new DonorModel(name, email, phone, city, country, bggrop, donate_date);


        // new user list
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserList");

        User user = new User(email.replace(".", "-"), country, city, phone, bggrop);


        try {

            reference3.child(phone).setValue(donorModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        reference4.child(phone).setValue(donorModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(profile.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });


        } catch (Exception e) {
            Log.i("all data", e.getMessage());
            Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
        }


        try {
            userRef.child(email.replace(".", "-")).setValue(user);

        } catch (Exception e) {
            Log.i("user data", e.getMessage());
        }


    }

    public void Edit(View view) {

        if (UserDetails.USER_COUNTRY == null) {
            updateCardLy.setVisibility(View.GONE);
            Toast.makeText(this, "Your profile data is not found ", Toast.LENGTH_SHORT).show();
            finish();

        }

        editCardly.setVisibility(View.GONE);
        updateCardLy.setVisibility(View.VISIBLE);
    }
}
