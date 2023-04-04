package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.AllUserResponse;
import com.smit.ppsa.Response.UserList;
import com.smit.ppsa.Response.userpassword.UserPasswordResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {
    private TextInputEditText oldpassword, newpassword, renewpassword;
    private TextInputLayout oldContainer;
    private Context context;
    private TextView passwordTitle;
    private CardView submit;
    private TextView oldPasswordTitle;
    LoginViewModel mViewModel;
    private static ApiClient.APIInterface apiInterface;
    private static UserList allUser;

    private ImageView backBtn;

    private GlobalProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        context = (PasswordActivity.this);
        progressDialog = new GlobalProgressDialog(context);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        Log.d("ioiu", "onCreate: " + BaseUtils.getUserInfo(this).getId());


        backBtn = findViewById(R.id.backbtn);
        oldPasswordTitle = findViewById(R.id.oldpasstt);
        oldpassword = findViewById(R.id.si_oldpass);
        newpassword = findViewById(R.id.si_newpass);
        renewpassword = findViewById(R.id.si_pass);
        oldContainer = findViewById(R.id.oldpassLayout);
        passwordTitle = findViewById(R.id.pastt);
        submit = findViewById(R.id.si_submit);

        //getPassword();


        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("newUser")) {
                oldContainer.setVisibility(View.GONE);
                oldPasswordTitle.setVisibility(View.GONE);
                passwordTitle.setText("This is the first time you are entering , please change your password here");
            } else {
                oldContainer.setVisibility(View.VISIBLE);
                oldPasswordTitle.setVisibility(View.VISIBLE);
                passwordTitle.setText("Please change your password here");
                getUserData();
            }
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidPasswordForm()) {
                    String password = new commons().md5(newpassword.getText().toString());
                    String passwordOld = new commons().md5(oldpassword.getText().toString());

                    mViewModel.submitPassword(Integer.valueOf(BaseUtils.getUserInfo(PasswordActivity.this).getId()),
                            password,
                            passwordOld,
                            null,
                            PasswordActivity.this, getIntent().getStringExtra("type"));
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    public void getUserData() {

        apiInterface = ApiClient.getClient();

        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(PasswordActivity.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }

        apiInterface.getAllUsers("c_MOBILE=" + BaseUtils.getUserName(PasswordActivity.this)).enqueue(new Callback<AllUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllUserResponse> call, @NotNull Response<AllUserResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        allUser = response.body().getUserData().get(0);

                        Log.d("All User Info",allUser.getN_staff_sanc());

                        BaseUtils.showToast(PasswordActivity.this,allUser.getN_staff_sanc());
                        BaseUtils.saveUserInfo(context, allUser);


                    }
                } else {
                    BaseUtils.showToast(context, "user not found");
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<AllUserResponse> call, @NotNull Throwable t) {
                BaseUtils.showToast(context, "user not found");

                progressDialog.hideProgressBar();
            }
        });
    }


    private void getPassword() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_typ_id<<EQUALTO>>" + getIntent().getStringExtra("hf_type_id");

        Log.d("kopopi", "getPythologyLab: " + url);
        ApiClient.getClient().getUserPassword(url).enqueue(new Callback<UserPasswordResponse>() {
            @Override
            public void onResponse(Call<UserPasswordResponse> call, Response<UserPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        // parentDataPythology = response.body().getUser_data();


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<UserPasswordResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });

    }

    private boolean isValidPasswordForm() {

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("newUser")) {

                if (!newpassword.getText().toString().isEmpty()) {
                    if (!renewpassword.getText().toString().isEmpty()) {

                        if (newpassword.getText().toString().equals(renewpassword.getText().toString())) {

                        } else {
                            BaseUtils.showToast(context, "Re entered password and new Password should match");
                            return false;
                        }
                    } else {
                        BaseUtils.showToast(context, "Re enter new Password");
                        return false;
                    }

                } else {
                    BaseUtils.showToast(context, "Enter new Password");
                    return false;
                }

            } else {
                if (!oldpassword.getText().toString().isEmpty()) {
                    if (!newpassword.getText().toString().isEmpty()) {
                        if (!renewpassword.getText().toString().isEmpty()) {

                            if (oldpassword.getText().toString().equals(newpassword.getText().toString())) {
                                BaseUtils.showToast(context, "Old and new Password should not match");
                                return false;
                            } else {
                                if (!newpassword.getText().toString().equals(renewpassword.getText().toString())) {

                                    BaseUtils.showToast(context, "Re entered password and new Password should match");
                                    return false;
                                }
                            }
                        } else {
                            BaseUtils.showToast(context, "Re enter new Password");
                            return false;
                        }

                    } else {
                        BaseUtils.showToast(context, "Enter new Password");
                        return false;
                    }
                } else {
                    BaseUtils.showToast(context, "Enter old password");
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }
}