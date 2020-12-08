package club.nsuer.nsuer;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash_bar);

        FirebaseApp.initializeApp(this);


        if (Build.VERSION.SDK_INT >= 25) {


            ImageView v = (ImageView) this.findViewById(R.id.animatedLoadingBar);
            Drawable d = v.getDrawable();
            if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                avd.start();
            } else if (d instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                avd.start();
            }

        }


        final Intent intent = new Intent(this, MainActivity.class);


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {


                startActivity(intent);

                overridePendingTransition(0, R.animator.fade_out);

                finish();


            }
        }, 100);


    }
}