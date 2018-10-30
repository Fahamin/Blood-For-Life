package app.seeker.bloodbank.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.model.DonorModel;

public class DonorDetails extends AppCompatActivity {

    TextView nameTV, bgTv, cityTV, emailTv, phoneTV, helpTv, donatdateTV;
    Button callBTN, smsBTN, emailBTN;
    String name, city, bloodGroup, phone, email, donatedate;
    ImageView phone_copy_IV, email_copy_IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        bloodGroup = intent.getStringExtra("bloodGroup");
        city = intent.getStringExtra("city");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        donatedate = intent.getStringExtra("donatdate");
        intView();
        setData();


        email_copy_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailTv.getText().toString().equals("Not Found")) {

                    copyItem(emailTv.getText().toString());

                } else {

                    Toast.makeText(DonorDetails.this, "No email found", Toast.LENGTH_SHORT).show();
                }

            }
        });


        phone_copy_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyItem(phoneTV.getText().toString());
            }
        });

        helpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DonorDetails.this, DonorInput.class));
            }
        });
    }


    private void copyItem(String s) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipe = ClipData.newPlainText("phone", s);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clipe);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        nameTV.setText(name);
        bgTv.setText(bloodGroup);
        phoneTV.setText(phone);
        emailTv.setText(email);
        cityTV.setText(city);
        donatdateTV.setText(donatedate);
    }

    private void intView() {

        nameTV = findViewById(R.id.nameTV);
        bgTv = findViewById(R.id.bg_TV);
        cityTV = findViewById(R.id.city_TV);
        emailTv = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        helpTv = findViewById(R.id.helpTV);
        donatdateTV = findViewById(R.id.donateDateTV);
        phone_copy_IV = findViewById(R.id.phoneCopyID);
        email_copy_IV = findViewById(R.id.emailCopyID);

    }

    public void CallNow(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneTV.getText().toString().trim()));
        startActivity(intent);

    }

    public void SmsNow(View view) {
        Uri uri = Uri.parse("smsto:" + phoneTV.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    public void EmailNow(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailTv.getText().toString().trim(), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type email here");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
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

                startActivity(new Intent(DonorDetails.this, profile.class));
                break;
            case R.id.tremsCondition:
                startActivity(new Intent(DonorDetails.this, Terms_condition.class));
                break;
            case R.id.exit:
                System.exit(1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
