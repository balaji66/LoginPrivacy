package durga.balaji66.com.loginsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageViewSplash1, imageViewSplash2, imageViewSplash3, imageViewSplash4;
    Animation mAnimation1;
    Animation mAnimation2;
    Animation mAnimation3;
    Animation mAnimation4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        initObjects();
        anim1();
        anim2();
        anim3();
        anim4();
    }

    private void initViews() {
        imageViewSplash1 = (ImageView) findViewById(R.id.imageViewIcon1);
        imageViewSplash2 = (ImageView) findViewById(R.id.imageViewIcon2);
        imageViewSplash3 = (ImageView) findViewById(R.id.imageViewIcon3);
        imageViewSplash4 = (ImageView) findViewById(R.id.imageViewIcon4);

    }

    private void initObjects() {
        mAnimation1 = AnimationUtils.loadAnimation(this, R.anim.antirotate);
        mAnimation2 = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mAnimation3 = AnimationUtils.loadAnimation(this, R.anim.zoom3);
        mAnimation4 = AnimationUtils.loadAnimation(this, R.anim.zoom4);
    }

    private void anim1() {
        imageViewSplash1.startAnimation(mAnimation1);
        mAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewSplash2.setVisibility(View.VISIBLE);
                imageViewSplash2.startAnimation(mAnimation2);
                imageViewSplash1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void anim2() {
        mAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewSplash3.startAnimation(mAnimation3);
                imageViewSplash3.setVisibility(View.VISIBLE);
                imageViewSplash2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void anim3() {
        mAnimation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewSplash4.startAnimation(mAnimation4);
                imageViewSplash4.setVisibility(View.VISIBLE);
                imageViewSplash3.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void anim4() {
        mAnimation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewSplash1.setVisibility(View.VISIBLE);
                imageViewSplash2.setVisibility(View.VISIBLE);
                imageViewSplash3.setVisibility(View.VISIBLE);
                imageViewSplash4.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
