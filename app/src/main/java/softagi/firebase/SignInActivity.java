package softagi.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity
{
    EditText email_field,password_field;
    Button register;
    LinearLayout linearLayout;

    String email,password;
    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        register = findViewById(R.id.register_btn);
        linearLayout = findViewById(R.id.register_layout);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = email_field.getText().toString();
                password = password_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "enter your email", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    email_field.requestFocus();

                    return;
                }

                if (password.length() < 6)
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "password is too short", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    password_field.requestFocus();

                    return;
                }

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("please wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                signIn(email,password);
            }
        });
    }

    private void signIn(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();

                            String user_id = task.getResult().getUser().getUid();

                            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                            intent.putExtra("uId", user_id);
                            startActivity(intent);
                            finish();
                        } else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
