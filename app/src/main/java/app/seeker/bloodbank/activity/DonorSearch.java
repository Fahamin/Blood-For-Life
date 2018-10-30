package app.seeker.bloodbank.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import app.seeker.bloodbank.R;

public class DonorSearch extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    LinearLayout bloodLAYOUT;
    TextView messageTV;
    RecyclerView recyclerView;
    EditText cityET;
    Button searchBTN;
    Spinner bgsp;
    String city, country, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();
        initVarible();
    }

    private void initView() {
        messageTV = (TextView) findViewById(R.id.messageTV);
        bloodLAYOUT = (LinearLayout) findViewById(R.id.blood_LAYOUT);
        cityET = (EditText) findViewById(R.id.cityET);
        bgsp = (Spinner) findViewById(R.id.blood_group_SP);
        searchBTN = (Button) findViewById(R.id.search_BTN);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }

    private void initVarible() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            messageTV.setVisibility(View.GONE);
            bloodLAYOUT.setVisibility(View.VISIBLE);
        } else {
            messageTV.setVisibility(View.VISIBLE);
            bloodLAYOUT.setVisibility(View.GONE);

            startActivity(new Intent(DonorSearch.this, Login.class));

        }

        messageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DonorSearch.this, Login.class));

            }
        });
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityET.getText().toString().trim().isEmpty()) {
                    cityET.setError("city name is required");
                    cityET.requestFocus();
                    return;
                }
                if (bgsp.getSelectedItemPosition() == 0) {
                    Toast.makeText(DonorSearch.this, "Please select a valid blood group", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkConnection()) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                    city = cityET.getText().toString();
                    country = getUserCountry(DonorSearch.this);
                    bloodGroup = getBloodGroup();

                    Intent intent = new Intent(DonorSearch.this, DonorList.class);
                    intent.putExtra("city", city);
                    intent.putExtra("bloodGroup", bloodGroup);
                    intent.putExtra("country", country);
                    startActivity(intent);
                } else {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    Toast.makeText(DonorSearch.this, "Please check your internet connection !!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean checkConnection() {


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfoWifi.isConnected();
        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfoMobile.isConnected();

        if (isWifiConn || isMobileConn) {
            return true;
        } else {
            return false;
        }
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

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available

                Locale loc = new Locale("", simCountry);
                return loc.getDisplayCountry();

            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available

                    Locale loc = new Locale("", networkCountry);
                    return loc.getDisplayCountry();
                }
            }
        } catch (Exception e) {

            Log.d("error", e.getMessage());
        }
        return null;
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
                //  Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Thanks for using my app")
                        .setMessage("Do you want to exit from here?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                System.exit(1);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();

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
                startActivity(new Intent(DonorSearch.this, profile.class));
                break;
            case R.id.tremsCondition:
                startActivity(new Intent(DonorSearch.this, Terms_condition.class));
                break;
            case R.id.exit:
                System.exit(1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
