package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.UserList;
import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LogIn extends AppCompatActivity {
    private List<UserList> usersList = new ArrayList<>();
    // private UserList userData;
    private Context context;
    private EditText email;
    private TextInputEditText password;
    private CardView logIn;
    private String message = "No user found...";
    private UserList userData;
    LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        context = (LogIn.this);
        inItView();
        //NetworkCalls.getUserData(context);

    }

    private void inItView() {
        usersList = BaseUtils.getUsers(context);
        email = findViewById(R.id.si_email);
        password = findViewById(R.id.si_pass);
        //password = findViewById(R.id.si_password);
        logIn = findViewById(R.id.si_signIn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidSignupForm()) {
                    //validateUser(email.getText().toString().trim(), password.getText().toString().trim());
                 /*   if (password.getText().toString().equals("pass@123")) {
                        startActivity(new Intent(LogIn.this, PasswordActivity.class)
                                .putExtra("type", "newUser"));
                    } else {*/
                        NetworkCalls.getUserData(LogIn.this,
                                email.getText().toString(),
                                password.getText().toString(), mViewModel, true);
                 //   }
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
        if (!email.getText().toString().isEmpty()) {
            if (!password.getText().toString().isEmpty()) {
                //  return true;
            } else {
                BaseUtils.showToast(context, "Enter Password");
                return false;
            }
        } else {
            BaseUtils.showToast(context, "Enter User Name");
            return false;
        }

        return true;
    }
    public  void showToast(Context context, String msg) {
        Toast toast
                = Toast.makeText(
                LogIn.this,
                " " + msg + " ",
                Toast.LENGTH_SHORT);
        // Getting the View
        View view = toast.getView();

        // Finding the textview in Toast view
        TextView text
                = (TextView)view
                .findViewById(
                        android.R.id.message);

        // Setting the Text Appearance
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.M) {
            text.setTextAppearance(
                    R.style.toastTextStyle);
        }

        // Showing the Toast Message
        toast.show();
    }
}