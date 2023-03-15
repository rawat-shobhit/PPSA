package com.smit.ppsa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.AttendeceResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!BaseUtils.isSameDay(this)){
            BaseUtils.putUserAttendance(this,false);
            BaseUtils.setDay(this);
        }

        hideSystemUI();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (BaseUtils.getUserLogIn(Splash.this)) {
                    getAttendence(Splash.this,curFormater.format(currentTime));
                } else {
                    intent = new Intent(Splash.this, LogIn.class);
                    startActivity(intent);
                    Splash.this.finish();
                }


            }
        }, 3000);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    private void getAttendence(Context context, String date){
        if (!BaseUtils.isNetworkAvailable(context)) {
            if (BaseUtils.isAttendanceDone(Splash.this)){
                startActivity(new Intent(Splash.this, MainActivity.class));
            }else{
                startActivity(new Intent(Splash.this, WorkerForm.class));
            }
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_t_attend&w=Year(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[2]+"%3C%3CAND%3E%3EMonth(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[1]+"%3C%3CAND%3E%3EDay(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[0]+"%3C%3CAND%3E%3En_user_id%3C%3CEQUALTO%3E%3E"+BaseUtils.getUserInfo(Splash.this).getId();
        ApiClient.getClient().getAttendence(url).enqueue(new Callback<AttendeceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AttendeceResponse> call, Response<AttendeceResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){

                        BaseUtils.putUserAttendance(context,true);

                       startActivity(new Intent(Splash.this, MainActivity.class));
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                        //  dateone.setText(response.body().getUserData().get(0).getdCdat());
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                        //     attendencetype .setText(response.body().getMessage());

                    }else{
                        startActivity(new Intent(Splash.this, WorkerForm.class));

                    }
                }else{
                    startActivity(new Intent(Splash.this, WorkerForm.class));
                }
                Splash.this.finish();
            }

            @Override
            public void onFailure(Call<AttendeceResponse> call, Throwable t) {

            }
        });
    }
}