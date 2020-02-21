package example.com.mytestnoti;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import example.com.mytestnoti.Service.MySingleton;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText edtTitle;
    EditText edtMessage,edtBody;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1SVpxNc:APA91bErBFFqyubkpcduoVtCWCJdnaE51EPE1Us5k_OHTPQfqcqOuw427GaTdX1McQEfsWnU9ZE34Z_C7evP6lPWMhDRFnHKNdIyusUAQsIymfyiZEtyfsX22xxkc-Vtn_FwU55BJgjQ";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String NOTIFICATION_BODY;
    String NOTIFICATION_IMAGE = "https://tinyjpg.com/images/social/website.jpg";
    String TOPIC;
    Button btnShowMsg;

    String TOKEN = "ef33hxyHtFE:APA91bH_c3gqoA1oLGMLLdegG6-9w7BL1BEzfgXLoGahsWyfMwNIJQ0fZQXadZmY7F8eIxuyq4ihR4lrg15PygRRzbn7ufylbi0oOX9EhPylcLY6LNHIsRYWICOjsSLwv3KVZiarBDuC";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTitle = findViewById(R.id.editText);
        /*edtMessage = findViewById(R.id.editText2);*/
        edtBody = findViewById(R.id.editText2);
        Button btnSend = findViewById(R.id.button);
        btnShowMsg = findViewById(R.id.btnShowMsg);
        imageView = findViewById(R.id.imageView);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = edtTitle.getText().toString();
                NOTIFICATION_BODY = edtBody.getText().toString();

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    //notifcationBody.put("message", NOTIFICATION_MESSAGE);
                    notifcationBody.put("body", NOTIFICATION_BODY);
                    notifcationBody.put("image", NOTIFICATION_IMAGE);


                    //notification.put("to", TOPIC); // send to TOPIC

                    notification.put("to", TOKEN); // send to Token
                    notification.put("data", notifcationBody);

                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);

                //Test******
                // save data to firebase
                //Toast.makeText(MainActivity.this, "MSG : " + notification, Toast.LENGTH_SHORT).show();

            }
        });


        btnShowMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Glide.with(getApplicationContext())
                .load(NOTIFICATION_IMAGE)
                .into(imageView);

    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: " + response.toString());
                //edtTitle.setText("");
                //edtMessage.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onErrorResponse: Didn't work");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
