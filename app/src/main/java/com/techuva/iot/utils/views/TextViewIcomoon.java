package com.techuva.iot.utils.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.io.File;

@SuppressLint("AppCompatCustomView")
public class TextViewIcomoon extends TextView {

    private Context context;
    public TextViewIcomoon(Context context) {
        super(context);
        this.context = context;
        createView();
    }


    public TextViewIcomoon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView(){
        setGravity(Gravity.CENTER);
            loadfont();
    }




    private void loadfont () {
       // File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_IOT_Files");
        File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        File file = new File(Dir,  "/icomoon.ttf");

        if(file.exists() && file.getPath().length()>0){
         /*  Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),
                   "fonts/icomoon.ttf");*/
          Typeface typeFace = Typeface.createFromFile(new File(file.getPath()));
          setTypeface(typeFace);
        }
        else {
           // Toast.makeText(context, "No Font", Toast.LENGTH_SHORT).show();
        }
    }
    // Download the custom font from the URL
}
