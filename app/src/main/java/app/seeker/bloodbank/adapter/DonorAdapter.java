package app.seeker.bloodbank.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import app.seeker.bloodbank.R;
import app.seeker.bloodbank.activity.DonorDetails;
import app.seeker.bloodbank.activity.MainActivity;
import app.seeker.bloodbank.model.DonorModel;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyviewHolder> {
    private Context context;
    List<DonorModel> donorModelList;
    RecyclerView recyclerView;
    DonorAdapter adapter;
    Activity activity;

    int counter = 0;

    public DonorAdapter(Context context, List<DonorModel> donorModelList, RecyclerView recyclerView, DonorAdapter adapter, Activity activity) {
        this.context = context;
        this.donorModelList = donorModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_donor_layout, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder myviewHolder, final int i) {
        final DonorModel donorModel = donorModelList.get(i);
        myviewHolder.nameTV.setText(donorModel.getName());
        myviewHolder.bgTV.setText(donorModel.getBloodGroup());
        myviewHolder.cityTV.setText(donorModel.getCity());
        myviewHolder.donateDateTV.setText(donorModel.getDonateDate());


        myviewHolder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter++;

                if (counter % 2 == 1) {

                    myviewHolder.additionalLAYOUT.setVisibility(View.VISIBLE);

                } else if (counter % 2 == 0) {

                    myviewHolder.additionalLAYOUT.setVisibility(View.GONE);
                }

            }
        });

        myviewHolder.fullChildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                counter++;

                if (counter % 2 == 1) {

                    myviewHolder.additionalLAYOUT.setVisibility(View.VISIBLE);

                } else if (counter % 2 == 0) {

                    myviewHolder.additionalLAYOUT.setVisibility(View.GONE);
                }

                return false;
            }
        });
        myviewHolder.callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(i);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + donorModel.getPhoneNumber()));
                context.startActivity(intent);
            }
        });
        myviewHolder.viewDetailsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(i);
                Intent intent = new Intent(context, DonorDetails.class);
                intent.putExtra("name", donorModel.getName());
                intent.putExtra("phone", donorModel.getPhoneNumber());
                intent.putExtra("email", donorModel.getEmail());
                intent.putExtra("bloodGroup", donorModel.getBloodGroup());
                intent.putExtra("city", donorModel.getCity());
                intent.putExtra("donatdate",donorModel.getDonateDate());
                context.startActivity(intent);

            }
        });
        myviewHolder.copyPhoneLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(i);
                copyPhone(donorModel.getPhoneNumber());

            }
        });
        myviewHolder.sendSmsLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(i);
                sendSms(donorModel.getPhoneNumber());

            }
        });
        myviewHolder.sendEmailLAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonorModel donorModel = donorModelList.get(i);

                if (!donorModel.getEmail().equals("Not Found")) {

                    sendEmail(donorModel.getEmail());

                } else {

                    Toast.makeText(context, "No Email Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void copyPhone(String phoneNumber) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("phone", phoneNumber);

        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
        }


    }

    private void sendSms(String phoneNumber) {

        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        context.startActivity(intent);

    }

    private void sendEmail(String email) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type email here");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }



   /* private boolean checkCallPermission() {


        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    request);


            return false;

        } else {

            return true;
        }*/


    @Override
    public int getItemCount() {
        return donorModelList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        public TextView nameTV, bgTV, cityTV, donateDateTV;
        public LinearLayout fullChildCV;
        public ImageView callIV;
        public LinearLayout additionalLAYOUT, viewDetailsLAYOUT, copyPhoneLAYOUT, sendSmsLAYOUT, sendEmailLAYOUT;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            bgTV = itemView.findViewById(R.id.bg_TV);
            cityTV = itemView.findViewById(R.id.city_TV);
            callIV = itemView.findViewById(R.id.call_IV);
            donateDateTV = itemView.findViewById(R.id.donateDateTV);

            fullChildCV = itemView.findViewById(R.id.fullChildCV);
            additionalLAYOUT = itemView.findViewById(R.id.additional_LAYOUT);
            viewDetailsLAYOUT = itemView.findViewById(R.id.view_details_LAYOUT);
            copyPhoneLAYOUT = itemView.findViewById(R.id.copy_phone_LAYOUT);
            sendSmsLAYOUT = itemView.findViewById(R.id.send_sms_LAYOUT);
            sendEmailLAYOUT = itemView.findViewById(R.id.send_email_LAYOUT);
        }
    }


}
