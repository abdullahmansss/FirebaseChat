package softagi.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import softagi.firebase.Models.UserModel;

public class MainActivity extends AppCompatActivity
{
    EditText email_field,username_field,password_field,confirm_password_field,mobile_field,address_field;
    Button register;
    LinearLayout linearLayout;

    String email,username,password,confirmpassword,mobile,address;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_field = findViewById(R.id.email_field);
        username_field = findViewById(R.id.username_field);
        password_field = findViewById(R.id.password_field);
        confirm_password_field = findViewById(R.id.confirm_password_field);
        mobile_field = findViewById(R.id.mobile_field);
        address_field = findViewById(R.id.address_field);
        register = findViewById(R.id.register_btn);
        linearLayout = findViewById(R.id.register_layout);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = email_field.getText().toString();
                username = username_field.getText().toString();
                password = password_field.getText().toString();
                confirmpassword = confirm_password_field.getText().toString();
                mobile = mobile_field.getText().toString();
                address = address_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "enter your email", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    email_field.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(username))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "enter your name", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    username_field.requestFocus();

                    return;
                }

                if (password.length() < 6)
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "password is too short", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    password_field.requestFocus();

                    return;
                }

                if (!confirmpassword.equals(password))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "password not matching", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    confirm_password_field.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(mobile))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "enter your mobile number", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    mobile_field.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(address))
                {
                    Snackbar snackbar = Snackbar.make(linearLayout, "enter your address", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    address_field.requestFocus();

                    return;
                }

                createUser(email,password,username,mobile,address);
            }
        });
    }

    private void createUser(final String email, String password, final String username, final String mobile, final String address)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            String user_id = task.getResult().getUser().getUid();

                            adduser(email,username,mobile,address,user_id);
                        } else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void adduser(String email, String username, String mobile, String address, String user_id)
    {
        UserModel userModel = new UserModel(email,username,mobile,address);

        databaseReference.child("Users").child(user_id).setValue(userModel);

        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void already(View view)
    {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}
