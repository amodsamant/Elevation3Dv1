package com.example.android.opengl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Elevation3Dv1 extends Activity {

    private GLSurfaceView mGLView;
    static String elevation1="";
    
    static float triangleCoords[] = {
        // in counterclockwise order:
        -0.3f, -0.111004243f, 0.0f,  
		-0.2f, -0.0004243f, 0.0f, 
		-0.1f, 0.422008459f, 0.0f, 
		0.0f,  0.522008459f, 0.0f,
		0.1f, 0.411004243f, 0.0f,  
		0.2f, 0.211004243f, 0.0f,
		0.3f, 0.311004243f, 0.0f,
		0.4f, 0.311004243f, 0.0f   
		
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
        
        //ElevationUtils.getElevationData();
                
        FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
        View.inflate(this, R.layout.table_info, rootLayout);
        Typeface myTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "Roboto-Light.ttf");//Roboto-Light.ttf  RobotoCondensed-Light.ttf
        TextView textViewStat1 = (TextView)findViewById(R.id.textViewStat1);
        textViewStat1.setTypeface(myTypeface);
        textViewStat1.setText(elevation1);
        
        TextView textViewStat2 = (TextView)findViewById(R.id.textViewStat2);
        textViewStat2.setTypeface(myTypeface);
        
        TextView textViewStat3 = (TextView)findViewById(R.id.textViewStat3);
        textViewStat3.setTypeface(myTypeface);
        
        TextView textViewStat4 = (TextView)findViewById(R.id.textViewStat4);
        textViewStat4.setTypeface(myTypeface);
  
      
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}