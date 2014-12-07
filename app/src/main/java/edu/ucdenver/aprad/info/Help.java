package edu.ucdenver.aprad.info;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.ucdenver.aprad.R;

/**
 * Displays information about how to use the program.
 * 
 * @author Robbie Perlstein
 * @author Dan Rolls
 * @author Michael Dewar
 * @author Kun Li
 * @author Nathan Vaderau
 */
public class Help extends Activity {
	
	private ScrollView scrollView;
	private TextView textView;
	
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.help);
    //scrollView = (ScrollView)findViewById(R.id.scrollViewer);
    //textView = (TextView)findViewById(R.id.helpContent);
    
  }
  
  /*
  private void scrollToBottom()
  {
	  scrollView.post(new Runnable()
      { 
          public void run()
          { 
        	  scrollView.smoothScrollTo(0, textView.getBottom());
          } 
      });
  }
  */
}