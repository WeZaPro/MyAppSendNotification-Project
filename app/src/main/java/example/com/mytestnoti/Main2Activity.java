package example.com.mytestnoti;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class Main2Activity extends AppCompatActivity {

    TextView tvGetValueFormNotifyTitle;
    TextView tvGetValueFormNotifyBody;
    ImageView mvGetValueFormNotifyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvGetValueFormNotifyTitle = findViewById(R.id.tvGetValueFormNotifyTitle);
        tvGetValueFormNotifyBody = findViewById(R.id.tvGetValueFormNotifyBody);
        mvGetValueFormNotifyImage = findViewById(R.id.mvGetValueFormNotifyImage);

        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        String image = getIntent().getStringExtra("image");


        tvGetValueFormNotifyTitle.setText(title);
        tvGetValueFormNotifyBody.setText(body);

        Glide.with(getApplicationContext())
                .load(image)
                .into(mvGetValueFormNotifyImage);
    }
}
