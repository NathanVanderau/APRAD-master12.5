package edu.ucdenver.aprad.education;

import java.io.FileOutputStream;
import java.io.IOException;

import edu.ucdenver.aprad.OpenSavedData;
import edu.ucdenver.aprad.Preferences;
import edu.ucdenver.aprad.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.EditText;
import edu.ucdenver.aprad.spectrogram.Spectrogram;
import edu.ucdenver.aprad.spectrogram.SpectrogramFileIO;
import edu.ucdenver.aprad.spectrum_analyzer.SpectrumAnalyzer;
import edu.ucdenver.aprad.spectrum_analyzer.SpectrumAnalyzerView;
import edu.ucdenver.aprad.tools.AudioRecorder;
import edu.ucdenver.aprad.tools.AudioRecorderListenerManipulate;


// TODO Replace with actual education system logic
public class TimeDomain extends Activity {
	/*
	SpectrogramFileIO fileIO = new SpectrogramFileIO();
	//double[][] educational_samples = new double[SpectrogramFileIO.SIGNAL_COUNT][SpectrogramFileIO.SIGNAL_LENGTH];
	
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.education);
    
    String filename = "test_file.txt";
	  String string = "Hello world!";
	  FileOutputStream outputStream;

	  try {
	    outputStream = openFileOutput( filename, Context.MODE_PRIVATE );
	    outputStream.write( string.getBytes() );
	    outputStream.close();
	  } catch ( Exception e ){
	    e.printStackTrace();
	  }
    
    /*
    String text_content = new String();   
    try {
		 text_content = fileIO.readStringFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    EditText text2 = (EditText)findViewById(R.id.editText2); 
    text2.setText(text_content.toCharArray(), 0, text_content.length());
    */
	  
	 /*
    EditText text2 = (EditText)findViewById(R.id.editText2); 
    if(Spectrogram.saveFlag)
    	text2.setText("saved");
    else
    	text2.setText("unsaved");
    	*/

    /*
    educational_samples = fileIO.readFile(SpectrogramFileIO.filename);
    
    EditText text2 = (EditText)findViewById(R.id.editText2); 
    text2.setText(Double.toString(educational_samples[0][0]).toCharArray(), 0, 1);*/
    //}

    private TimeDomainView educationVisualizer;
    private SurfaceHolder surfaceHolder;

    private AudioRecorder signalCapture;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_domain);

        // get the Surface view which is used to draw the spectrum.
        this.educationVisualizer = (TimeDomainView) findViewById(R.id.educationSurfaceView);
        this.surfaceHolder = this.educationVisualizer.getHolder();

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
            educationVisualizer);
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
                Intent settingsIntent = new Intent( TimeDomain.this, Preferences.class );
                settingsIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                this.startActivity(settingsIntent);
                return true;
            case R.id.open_file:
                Intent openSavedDataIntent = new Intent( TimeDomain.this, OpenSavedData.class );
                this.startActivity(openSavedDataIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
