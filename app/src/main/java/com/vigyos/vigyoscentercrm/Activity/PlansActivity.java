package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.vigyos.vigyoscentercrm.R;

public class PlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        TextView textView = findViewById(R.id.rotatedTextView);

        // Set the text you want to display
        textView.setText("Hello, Rotated Text!");

        // Create a RotateAnimation
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Set the duration of the animation
        rotateAnimation.setDuration(0);

        // Apply the animation to the TextView
        textView.startAnimation(rotateAnimation);
    }
}