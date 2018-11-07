package app.seeker.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.allclass.BaseClass;
import app.seeker.bloodbank.model.DonorModel;
import app.seeker.bloodbank.model.User;
import app.seeker.bloodbank.utils.UserDetails;

public class Registration extends BaseClass {

    EditText nameET, phoneET, emailET, cityET, passET, confrimpassET, donatedateET;
    String name, email, Uid, password, donatedate;
    Button registerBtn;
    Spinner bgsp;
    ProgressDialog progressDialog;
    FirebaseAuth auth;


    /*private  final String  city;
    private  final String bloodGroup;
    private final String  country;*/
    //  public static final String phonekery =phone;


    private DatabaseReference databaseReference, databaseReferenceAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        intViiew();
        initVarible();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (checkValidity()) {
                    progressDialog.setMessage("Connecting with server . please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    loadData();

                    authintication();


                }
            }
        });

    }
    private void loadUserInfo() {

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().replace(".", "-");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserList");

        reference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);


                UserDetails.USER_COUNTRY = user.country;
                UserDetails.USER_CITY = user.city;
                UserDetails.USER_MOBILE = user.number;
                UserDetails.USER_BLOOD_GROUP = user.blood;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void loadData() {

        name = nameET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        password = passET.getText().toString().trim();
        phone = phoneET.getText().toString().trim();
        city = cityET.getText().toString().trim();
        bloodGroup = getBloodGroup();
        country = getUserCountry(Registration.this);


        if (donatedateET.getText().toString().trim().isEmpty()) {
            donatedate = "Unknown";
        } else {
            donatedate = donatedateET.getText().toString().trim();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);

        databaseReferenceAll = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + "All" + "/" + bloodGroup);

        final DonorModel model = new DonorModel(name, email, phone, city, country, bloodGroup, donatedate);


        databaseReferenceAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(phone)) {
                    phoneET.setError("This number is already register!!");
                    phoneET.requestFocus();
                    progressDialog.dismiss();
                    return;
                } else {
                    databaseReference.child(phone).setValue(model);
                    databaseReferenceAll.child(phone).setValue(model);

                    String u_id = email.replace(".", "-");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserList").child(u_id);

                    User user = new User(u_id, country, city, phone, bloodGroup);

                    reference.setValue(user);

                    loadUserInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void authintication() {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Register complied", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Registration.this, DonorSearch.class);
                  /* intent.putExtra("phon",phone);
                   intent.putExtra("city",city);
                   intent.putExtra("bloodgrup",bloodGroup);
                   intent.putExtra("country",country);*/


                    startActivity(intent);

                    //startActivity(new Intent(Registration.this, profile.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });



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

    private String getBloodGroup() {

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


    private void initVarible() {


        auth = FirebaseAuth.getInstance();
    }

    private void intViiew() {

        progressDialog = new ProgressDialog(Registration.this);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passworET);
        confrimpassET = findViewById(R.id.confrimpassworET);
        phoneET = findViewById(R.id.phoneET);
        cityET = findViewById(R.id.cityET);
        bgsp = findViewById(R.id.blood_group_SP);
        registerBtn = findViewById(R.id.inputBTN);
        donatedateET = findViewById(R.id.donateDateET);
    }

    private boolean checkValidity() {


        if (nameET.getText().toString().trim().isEmpty()) {
            nameET.setError("Name is Required");
            nameET.requestFocus();
            return false;
        }
        if (emailET.getText().toString().trim().isEmpty()) {
            emailET.setError("Enter valid Email");
            emailET.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString().trim()).matches()) {
            emailET.setError("Enter valid Email");
            emailET.requestFocus();
            return false;
        }

        if (passET.getText().toString().trim().isEmpty() || passET.getText().toString().trim().length() < 6) {
            passET.setError("password Required at least 6 character");
            passET.requestFocus();
            return false;
        }
        if (!passET.getText().toString().trim().equals(confrimpassET.getText().toString().trim())) {
            confrimpassET.setError("password not match");
            confrimpassET.requestFocus();
            return false;
        }

        if (phoneET.getText().toString().trim().isEmpty()) {
            phoneET.setError("Phone Number is Required");
            phoneET.requestFocus();
            return false;
        }

        if (cityET.getText().toString().trim().isEmpty()) {
            cityET.setError("Enter your City");
            cityET.requestFocus();
            return false;
        }

        if (bgsp.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "please select your blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}


