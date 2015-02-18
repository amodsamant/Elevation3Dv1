package com.example.android.opengl.utils;


import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask{
public AsyncResponse delegate=null;

   protected void onPostExecute(String result) {
      delegate.processFinish(result);
   }

@Override
protected Object doInBackground(Object... params) {
	// TODO Auto-generated method stub
	return null;
}
}