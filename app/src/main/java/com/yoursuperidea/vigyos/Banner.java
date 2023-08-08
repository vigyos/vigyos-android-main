package com.yoursuperidea.vigyos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;



import java.util.ArrayList;

public class Banner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        ArrayList<SlideModel> img = new ArrayList<>();

        img.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
//        img.add(new SlideModel("", "Elephants and tigers may become extinct.",ScaleTypes.CENTER_CROP));
//        img.add(new SlideModel("", "And people do that.", ScaleTypes.CENTER_CROP));


        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(img);
    }
}