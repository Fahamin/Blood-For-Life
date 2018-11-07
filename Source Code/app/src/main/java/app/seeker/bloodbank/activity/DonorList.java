package app.seeker.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.adapter.DonorAdapter;
import app.seeker.bloodbank.model.DonorModel;
import app.seeker.bloodbank.model.User;
import app.seeker.bloodbank.utils.UserDetails;

public class DonorList extends AppCompatActivity {
    TextView messageTV, helpTV;
    RecyclerView recyclerView;
    String city, country, bloodGroup, donatedate;

    DatabaseReference reference;
    List<DonorModel> donorModelList;
    DonorAdapter adapter;
    ProgressDialog progressDialog;
    private DatabaseReference databaseReference, databaseReferenceall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        bloodGroup = intent.getStringExtra("bloodGroup");
        donatedate = intent.getStringExtra("donatdate");
        init();


    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data. Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        messageTV = findViewById(R.id.messageTV);
        helpTV = findViewById(R.id.helpTV);
        recyclerView = findViewById(R.id.recyclerView);


        helpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DonorList.this, DonorInput.class));
            }
        });

        donorModelList = new ArrayList<>();

        donorModelList.clear();
        recyclerView.removeAllViewsInLayout();


        databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + city.toLowerCase() + "/" + bloodGroup);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                donorModelList.clear();
                for (DataSnapshot donor : dataSnapshot.getChildren()) {
                    DonorModel model = donor.getValue(DonorModel.class);
                    donorModelList.add(model);
                }

                if (donorModelList.size() > 0) {
                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Congrats !!! " + donorModelList.size() + " (" + bloodGroup + ") donor found in your area ");
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    adapter = new DonorAdapter(DonorList.this, donorModelList, recyclerView, adapter, DonorList.this);
                    RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(DonorList.this, 1);
                    recyclerView.setLayoutManager(layoutManagerBeforeMeal);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } else {
                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Sorry, No " + bloodGroup + " donor found in your area. " + " \nShowing " + bloodGroup + " donor for all over in " + country);
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    progressDialog.dismiss();

                    executeForNotFound();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void executeForNotFound() {

        databaseReferenceall = FirebaseDatabase.getInstance().getReference("UserDetails/Donor/" +
                country + "/" + "All" + "/" + bloodGroup);

        databaseReferenceall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donorModelList.clear();

                for (DataSnapshot donor : dataSnapshot.getChildren()) {

                    DonorModel donorModel = donor.getValue(DonorModel.class);
                    donorModelList.add(donorModel);
                }


                if (donorModelList.size() > 0) {

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                    adapter = new DonorAdapter(DonorList.this, donorModelList, recyclerView, adapter, DonorList.this);
                    RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(DonorList.this, 1);
                    recyclerView.setLayoutManager(layoutManagerBeforeMeal);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    progressDialog.dismiss();

                } else {

                    messageTV.setVisibility(View.VISIBLE);
                    messageTV.setText("Sorry, we have no " + bloodGroup + " donor available in our database");
                    messageTV.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                startActivity(new Intent(DonorList.this, profile.class));
                break;
            case R.id.tremsCondition:
                startActivity(new Intent(DonorList.this, Terms_condition.class));
                break;
            case R.id.exit:
                System.exit(1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
