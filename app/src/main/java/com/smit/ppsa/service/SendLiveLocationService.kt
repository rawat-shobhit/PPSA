package com.smit.ppsa.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.R
import com.smit.ppsa.Response.AddDocResponse
import com.smit.ppsa.executor.AppExecutors
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*


class SendLiveLocationService : Service() {
    private val handler = Handler()
    var timer = Timer()
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    var latitude: Double? = null
    var longitude: Double? = null
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LocationService"
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Running in Background")
                .setPriority(Notification.PRIORITY_DEFAULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                AppExecutors.instance?.networkIO()?.execute {
                    val lat = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        DecimalFormat("##.######").format(latitude)
                    )
                    val lng = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        DecimalFormat("##.######").format(longitude)
                    )

                    val n_staff_info = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        BaseUtils.getUserInfo(this).getnUserLevel()
                    )
                    val n_user_id = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        BaseUtils.getUserInfo(this).getId()
                    )
                    val n_sanc = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        BaseUtils.getUserInfo(this).n_staff_sanc
                    )
                    ApiClient.getClient().postLoc(lat, lng, n_sanc, n_staff_info, n_user_id).enqueue(object : retrofit2.Callback<AddDocResponse>{
                        override fun onResponse(
                            call: Call<AddDocResponse>,
                            response: Response<AddDocResponse>
                        ) {

                        }

                        override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
                        }
                    })

                }
                //                  lat=30.977006;
//                  lng=76.537880;
                //saveAddress();
            } else {
                // Toast.makeText(HomeActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }.addOnFailureListener {
            //  Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                //getLocation()

            }
        }, 0, 15*60*1000)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
        timer.cancel()
        val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nManager.cancel(1)

    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }


}