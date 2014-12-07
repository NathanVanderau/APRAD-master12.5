package edu.ucdenver.aprad.spectrum_analyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import edu.ucdenver.aprad.OpenSavedData;
import edu.ucdenver.aprad.Preferences;
import edu.ucdenver.aprad.R;
import edu.ucdenver.aprad.spectrogram.Spectrogram;
import edu.ucdenver.aprad.tools.AudioRecordListener;
import edu.ucdenver.aprad.tools.AudioRecorder;
import edu.ucdenver.aprad.tools.FFT;
import edu.ucdenver.aprad.tools.AudioRecorderListenerManipulate;

/**
 * SpectrumAnalyzer main class which extends Activity
 * @author Dan Rolls
 * @author Michael Dewar
 */
public class SpectrumAnalyzer extends Activity  
{
	
	private SpectrumAnalyzerView spectrumVisualizer;	
	private SurfaceHolder surfaceHolder;
	
	private AudioRecorder signalCapture;


	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spectrum_analyzer);
		
		// get the Surface view which is used to draw the spectrum.
		this.spectrumVisualizer = (SpectrumAnalyzerView) findViewById(R.id.surfaceView);
		this.surfaceHolder = this.spectrumVisualizer.getHolder();

	}
	
	/**
	 * Initialize AudioRecorder and register the AudioRecorder listener
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		updatePreferences();
		this.signalCapture = new AudioRecorder( AudioRecorderListenerManipulate.frequency, 
												AudioRecorderListenerManipulate.NUM_SAMPLES,
												spectrumVisualizer);
		//AudioRecorderListenerManipulate.registerFFTAvailListener(this);
	}
	
	private void updatePreferences(){
	  SharedPreferences sharedPreferences = getSharedPreferences( Preferences.PREFS_NAME, 0 );
	  AudioRecorderListenerManipulate.frequency = sharedPreferences.getFloat( Preferences.FREQUENCY, 8000.0f );
	}

  /**
	 * End the signal capture and unregister listener
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		this.signalCapture.end();
		//AudioRecorderListenerManipulate.unregisterFFTAvailListener(this);
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
    switch ( item.getItemId() ){
    case R.id.settings:
      Intent settingsIntent = new Intent( SpectrumAnalyzer.this, Preferences.class );
      settingsIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
      this.startActivity(settingsIntent);
      return true;
    case R.id.open_file:
      Intent openSavedDataIntent = new Intent( SpectrumAnalyzer.this, OpenSavedData.class );
      this.startActivity(openSavedDataIntent);
      return true;
    default:
        return super.onOptionsItemSelected(item);
    }
  }
	

	
}

