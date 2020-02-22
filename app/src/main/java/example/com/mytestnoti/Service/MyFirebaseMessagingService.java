package example.com.mytestnoti.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import example.com.mytestnoti.Constance;
import example.com.mytestnoti.Main2Activity;
import example.com.mytestnoti.MainActivity;
import example.com.mytestnoti.R;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);

        // Link ไป Web
        /*final Intent intent = new Intent(this, MainActivity.class);*/

        // Link ไป Main2Activity + ส่งข้อมูลใน Notification
        final Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("title", remoteMessage.getData().get("title"));
        intent.putExtra("body", remoteMessage.getData().get("body"));
        intent.putExtra("token", remoteMessage.getData().get("token"));
        intent.putExtra("image", remoteMessage.getData().get("image"));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        //test*****
        //intent.getStringExtra("key");

        // Link ไป Web
        //Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sirivatana.co.th/"));

        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        String imageUri = remoteMessage.getData().get("image");
        Bitmap bitmap = getBitmapfromUrl(imageUri);

        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background).setLargeIcon(bitmap).setContentTitle(remoteMessage.getData().get("title")).setContentText(remoteMessage.getData().get("body")).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap)).setAutoCancel(true)
                //.setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        Log.d("check", pendingIntent.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Create Notification
        notificationManager.notify(notificationID, notificationBuilder.build());

    }

    private Bitmap getBitmapfromUrl(String imageUri) {

        try {
            URL url = new URL(imageUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devicee notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d("token", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("token", token);
        /*final String SUBSCRIBE_TO = "userABC";

        String tokens = FirebaseInstanceId.getInstance().getToken();

        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);*/
        Log.i(TAG, "onTokenRefresh completed with token: " + token);


        SharedPreferences sharedPref = getSharedPreferences(Constance.MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constance.TOKEN, token);
        editor.commit();

        Log.d("token", "send token: " + token);
    }

}
