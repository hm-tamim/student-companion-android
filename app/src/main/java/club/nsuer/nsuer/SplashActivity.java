package club.nsuer.nsuer;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_bar);

        FirebaseApp.initializeApp(this);

        final Intent intent = new Intent(this, MainActivity.class);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
                overridePendingTransition(0, R.animator.fade_out);
                finish();
            }
        }, 1000);
    }
}