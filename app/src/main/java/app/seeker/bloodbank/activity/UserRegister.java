package app.seeker.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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

public class UserRegister extends AppCompatActivity {
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
        setContentView(R.layout.activity_user_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        inputEmail = findViewById(R.id.inputEmailEtdId);
        inputPassword = findViewById(R.id.inputPassEtdId);
        btnLogin = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                progressDialog.setMessage("Connecting with server . please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

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
                    inputPassword.setError(" Atleast 6 character requried");
                    inputPassword.requestFocus();
                    return;
                }

                progressDialog.setMessage("Connecting with server . please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                authintication();

            }
        });
    }

    private void authintication()

    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(UserRegister.this, "Register complied", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserRegister.this, DonorSearch.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

    }
}





