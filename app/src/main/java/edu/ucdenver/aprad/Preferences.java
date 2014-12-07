package edu.ucdenver.aprad;

import edu.ucdenver.aprad.spectrum_analyzer.SpectrumAnalyzer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Preferences extends Activity {
  
  public static final String PREFS_NAME  = "SharedPreferences";
  public static final String FREQUENCY   = "FREQUENCY";
  public static final String LIVE_RENDER = "LIVE_RENDER";
  public static final String LOG_SCALING = "LOG_SCALING";
  
  private Spinner frequencySpinner;
  private Spinner liveRenderSpinner;
  private Spinner displayScalingSpinner;
  private Button savePreferencesButton;
  
  SharedPreferences sharedPreferences;
  
  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.preferences );
    
    sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
    
    
    frequencySpinner = (Spinner) findViewById( R.id.frequencySpinner );
    setFrequencySpinnerSelection();
    
    liveRenderSpinner = (Spinner) findViewById( R.id.liveRenderSpinner );
    setLiveRenderSpinner();
    
    displayScalingSpinner = (Spinner) findViewById( R.id.displayScalingSpinner );
    setDisplayScalingSpinner();
 
    
    savePreferencesButton = (Button) findViewById(R.id.savePreferencesButton);
    savePreferencesButton.setOnClickListener( 
    											new OnClickListener() 
    											{
    												@Override
    												public void onClick( View v )
    												{
    													saveSharedPreferences();
    													Intent intent = new Intent( v.getContext(), Aprad.class);
    													v.getContext().startActivity(intent);
    												}
    											}
    										);
  }

  
  private void setFrequencySpinnerSelection()
  {
    ArrayAdapter myAdap = (ArrayAdapter) frequencySpinner.getAdapter(); //cast to an ArrayAdapter

    int spinnerPosition = myAdap.getPosition( String.valueOf( (int) sharedPreferences.getFloat( FREQUENCY, 8000.0f ) ) + " Hz" );

    //set the default according to value
    frequencySpinner.setSelection(spinnerPosition);
  }
  
  
  
  private void setDisplayScalingSpinner() 
  {
    displayScalingSpinner.setSelection( (sharedPreferences.getBoolean( LOG_SCALING, true )) ? 0 : 1 );
  }

  
  
  private void setLiveRenderSpinner()
  {
    liveRenderSpinner.setSelection( (sharedPreferences.getBoolean( LIVE_RENDER, true )) ? 0 : 1 );
  }

  
  
  private void saveSharedPreferences() 
  {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putFloat( FREQUENCY, Float.parseFloat( frequencySpinner.getSelectedItem().toString().replaceAll("[^\\d.]", "") ));
    editor.putBoolean( LIVE_RENDER, (liveRenderSpinner.getSelectedItemPosition() == 0) );
    editor.putBoolean( LOG_SCALING, (displayScalingSpinner.getSelectedItemPosition() == 0) );
    editor.commit();
  }
}
