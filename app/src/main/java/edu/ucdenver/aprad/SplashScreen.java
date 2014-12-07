package edu.ucdenver.aprad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
  private static int TIMER = 3000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash);

      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent intent = new Intent( SplashScreen.this, Aprad.class );
              startActivity( intent );

              // close this activity
              finish();
          }
      }, TIMER);
  }
}
