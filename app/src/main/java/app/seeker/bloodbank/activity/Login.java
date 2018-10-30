package app.seeker.bloodbank.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.seeker.bloodbank.R;

public class Login extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    FirebaseUser user;
    FirebaseAuth auth;
    String email, password;
    ProgressDialog progressDialog;
    private Button btnSignup, btnLogin;
    TextView btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        inputEmail = findViewById(R.id.inputEmailEtdId);
        inputPassword = findViewById(R.id.inputPassEtdId);
        btnLogin = findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        LOAD();
        progressDialog = new ProgressDialog(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Email");
                    inputEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Enter Valid Email");
                    inputEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Required password");
                    inputPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    inputPassword.setError("password check");
                    inputPassword.requestFocus();
                    return;
                }
                progressDialog.setMessage("Connecting with server . please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(Login.this, DonorSearch.class));

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Login Failed!! \n Try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));

            }
        });
    }

    public void RegisterBTN(View view) {

        AlertDialog aa = new AlertDialog.Builder(Login.this)
                .setTitle("Are you blood donor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Login.this, Registration.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Login.this, UserRegister.class));
                        finish();

                    }
                }).show();

    }

    private void LOAD() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, DonorSearch.class));
            finish();
        }
    }

}
