package com.example.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Response.UserList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LogIn extends AppCompatActivity{
    private List<UserList> usersList = new ArrayList<>();
    // private UserList userData;
    private Context context;
    private EditText email;
    private CardView logIn;
    private String message = "No user found...";
    private UserList userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        context=(LogIn.this);
        inItView();
        //NetworkCalls.getUserData(context);

    }

    private void inItView() {
        usersList = BaseUtils.getUsers(context);
        email = findViewById(R.id.si_email);
        //password = findViewById(R.id.si_password);
        logIn = findViewById(R.id.si_signIn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidSignupForm()) {
                    //validateUser(email.getText().toString().trim(), password.getText().toString().trim());
                    NetworkCalls.getUserData(LogIn.this,email.getText().toString());
                }

            }
        });
    }

    /*private void validateUser(String zUser, String zPassword) {
        usersList = BaseUtils.getUsers(context);
        boolean found = false;
        for(int i=0; i<usersList.size(); i++){
            if(checkValidUser(usersList.get(i))){
                found = true;
            }
        }

        if(found){
            BaseUtils.putUserLogIn(context, true);
            BaseUtils.savedUserData(context, userData);
            startActivity(new Intent(context, Manager.class));
        }else{
            BaseUtils.showToast(context, message);
        }

    }*/

   /* private boolean checkValidUser(UserList user) {
        String pswd = md5(password.getText().toString());
        message = "No user found...";

        if(email.getText().toString().toLowerCase().trim().equals(user.cMobile.toLowerCase().trim())){
            message = "Wrong password";
            if(pswd.toLowerCase().trim().equals(user.cPassword.toLowerCase().trim())){
                userData = user;
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
*/
    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isValidSignupForm() {
        if (email.getText().toString().isEmpty()) {
            BaseUtils.showToast(context, "Enter User Name");
            return false;
        }

        return true;
    }
}