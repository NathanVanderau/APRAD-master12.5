package edu.ucdenver.aprad;

import edu.ucdenver.aprad.R;
import edu.ucdenver.aprad.education.Education;
import edu.ucdenver.aprad.education.TimeDomain;
import edu.ucdenver.aprad.info.About;
import edu.ucdenver.aprad.info.Help;
import edu.ucdenver.aprad.spectrogram.Spectrogram;
import edu.ucdenver.aprad.spectrum_analyzer.SpectrumAnalyzer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Aprad extends Activity {

  Button spectrumAnalyzerButton;
  Button spectrogramButton;
  Button timeDomainButton;
  Button educationButton;
  Button helpButton;
  Button aboutButton;
  
  ActionBar actionBar;
  
  public static final String PREFS_NAME = "SharedPreferences";
  SharedPreferences sharedPreferences;
  
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    
    spectrumAnalyzerButton = (Button) findViewById(R.id.spectrumAnalyzerButton);
    spectrumAnalyzerButton.setOnClickListener( 
    											new OnClickListener() 
    											{
    												@Override
    												public void onClick( View v )
    												{
    													Intent intent = new Intent( v.getContext(), SpectrumAnalyzer.class);
    													v.getContext().startActivity(intent);
    												}
    											}
    										 );
    
    spectrogramButton = (Button) findViewById(R.id.spectrogramButton);
    spectrogramButton.setOnClickListener( 
    											new OnClickListener() 
    											{
    												@Override
    												public void onClick( View v )
    												{
    													Intent intent = new Intent( v.getContext(), Spectrogram.class);
    													v.getContext().startActivity(intent);
    												}
    											}
    									);

    timeDomainButton = (Button) findViewById(R.id.timeDomainButton);
    timeDomainButton.setOnClickListener(
          new OnClickListener()
          {
              @Override
              public void onClick( View v )
              {
                  Intent intent = new Intent( v.getContext(), TimeDomain.class);
                  v.getContext().startActivity(intent);
              }
          }
      );

    educationButton = (Button) findViewById(R.id.educationButton);
    educationButton.setOnClickListener( 
    									new OnClickListener() 
    									{
    										@Override
    										public void onClick( View v )
    										{
    											Intent intent = new Intent( v.getContext(), Education.class);
    											v.getContext().startActivity(intent);
    										}
    									}
    								  );
    
    helpButton = (Button) findViewById(R.id.helpButton);
    helpButton.setOnClickListener( 
    									new OnClickListener() 
    									{
    										@Override
    										public void onClick( View v )
    										{
    											Intent intent = new Intent( v.getContext(), Help.class);
    											v.getContext().startActivity(intent);
    										}
    									}
    							 );
    
    aboutButton = (Button) findViewById(R.id.aboutButton);
    aboutButton.setOnClickListener( 
    									new OnClickListener() 
    									{
    										@Override
    										public void onClick( View v )
    										{
    											Intent intent = new Intent( v.getContext(), About.class);
    											v.getContext().startActivity(intent);
    										}
    									}
    							  );
    
  }
  
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.aprad, menu);
      return true;
  }

  
  
  @Override
  public boolean onOptionsItemSelected( MenuItem item ){
    // Handle item selection
    switch ( item.getItemId() )
    {
    	case R.id.settings:
    		Intent settingsIntent = new Intent( Aprad.this, Preferences.class );
    		settingsIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
    		this.startActivity(settingsIntent);
    		return true;
    	case R.id.open_file:
    		Intent openSavedDataIntent = new Intent( Aprad.this, OpenSavedData.class );
    		this.startActivity(openSavedDataIntent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    }
  }

  
}
