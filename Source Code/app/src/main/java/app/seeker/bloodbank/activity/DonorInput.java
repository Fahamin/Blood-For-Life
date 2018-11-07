package app.seeker.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.model.DonorModel;

public class DonorInput extends AppCompatActivity {

    EditText nameET, phoneET, emailET, cityET, dateET;
    String name, phone, email, city, bloodGroup, country, donateDate, Uid;
    TextView messageTV;
    Button inputBtn;
    Spinner bgsp;

    DatabaseReference reference;
    ProgressDialog progressDialog;
    private DatabaseReference databaseReference, databaseReferenceAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_input);


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intView();

    }

    private void intView() {

        nameET = findViewById(R.id.nameET);
        phoneET = findViewById(R.id.phoneET);
        emailET = findViewById(R.id.emailET);
        cityET = findViewById(R.id.cityET);
        messageTV = findViewById(R.id.messageTV);

        bgsp = findViewById(R.id.blood_group_SP);
        inputBtn = findViewById(R.id.inputBTN);
        dateET = findViewById(R.id.donateDateET);

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (checkValidity()) {
                    progressDialog = new ProgressDialog(DonorInput.this);
                    progressDialog.setMessage("Connecting with server . please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    name = nameET.getText().toString().trim();
                    phone = phoneET.getText().toString().trim();

                    if (emailET.getText().toString().trim().isEmpty()) {
                        email = "Not Found";
                    } else {
                        email = emailET.getText().toString().trim();
                    }
                    if (dateET.getText().toString().trim().isEmpty()) {
                        donateDate = "Unknown";
                    } else {
                        donateDate = dateET.getText().toString().trim();
                    }

                    city = cityET.getText().toString().trim();
                    bloodGroup = getBloodGroup();
                    country = getUserCountry(DonorInput.this);
                    dataInput();
                }


            }
        });

    }

    private void dataInput() {

        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);

        databaseReferenceAll = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + "All" + "/" + bloodGroup);

      /*  DonorModel model = new DonorModel();
        model.setUid("null");
        model.setName(name);
        model.setEmail(email);
        model.setPhoneNumber(phone);
        model.setCity(city);
        model.setCountry(country);
        model.setPssword("null");
        model.setBloodGroup(bloodGroup);
        model.setIsdonor(true);*/
        // Uid = reference.push().getKey();

        final DonorModel donorModel = new DonorModel(name, email, phone, city, country, bloodGroup,donateDate);

        databaseReferenceAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(phone)) {
                    phoneET.setError("This phone number is already registered");
                    progressDialog.dismiss();
                    messageTV.setVisibility(View.GONE);
                    return;
                } else {

                    databaseReference.child(phone).setValue(donorModel);
                    databaseReferenceAll.child(phone).setValue(donorModel);
                    progressDialog.dismiss();
                    Toast.makeText(DonorInput.this, "Successfully inserted data ", Toast.LENGTH_SHORT).show();

                    nameET.setText("");
                    phoneET.setText("");
                    cityET.setText("");
                    emailET.setText("");
                    bgsp.setSelection(0);
                    messageTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    private boolean checkValidity() {

        if (nameET.getText().toString().trim().isEmpty()) {
            nameET.setError("Donor's name is required");
            nameET.requestFocus();
            return false;
        }
        if (phoneET.getText().toString().trim().isEmpty()) {
            phoneET.setError("phons number is required");
            phoneET.requestFocus();
            return false;
        }
        if (cityET.getText().toString().trim().isEmpty()) {
            cityET.setError("city name is required");
            cityET.requestFocus();
            return false;
        }
        if (bgsp.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "please select your blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.shareApp:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Install now");
                String app_url = "";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.profileId:
                startActivity(new Intent(this,profile.class));
                break;
            case R.id.tremsCondition:
                startActivity(new Intent(DonorInput.this, Terms_condition.class));
                break;
            case R.id.exit:
                System.exit(1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
