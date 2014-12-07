package edu.ucdenver.aprad.spectrogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.ucdenver.aprad.OpenSavedData;
import edu.ucdenver.aprad.Preferences;
import edu.ucdenver.aprad.R;
import edu.ucdenver.aprad.education.Education;
import edu.ucdenver.aprad.tools.AudioRecordListener;
import edu.ucdenver.aprad.tools.AudioRecorder;
import edu.ucdenver.aprad.tools.AudioRecorderListenerManipulate;
import edu.ucdenver.aprad.tools.FFT;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * The Spectrogram is used to map frequency and amplitude over time. This is
 * represented as frequency on the y axis, time on the x axis, amd amplitude
 * is the color of the point on the graph.
 * 
 * @author Dan Rolls
 * @author Michael Dewar
 * @author Robbie Perlstein
 */
public class Spectrogram extends Activity implements AudioRecordListener 
{
	public static boolean saveFlag = false;
	
	private SpectrogramView    	spectrogramView;
	private AudioRecorder      	signalCapture;
	private SpectrogramFileIO  	fileWriter;
	private FFT 				fft;
	
	private boolean            	stopped       = false;
		
	private double             	frequency     = 8000.0;
	private int                	buffer_size   = AudioRecorderListenerManipulate.NUM_SAMPLES;
	private boolean            	liveRender    = false;
	private boolean            	scaling       = true;
	
	private final int          	START_STOP_BUTTON_X_SCALAR  = 8;
	private final int          	START_STOP_BUTTON_Y_SCALAR  = 3;
	
	private short[][] 			rawSignals;
	private int 				signal_count;
	
	private boolean 			rendering = false;
	
	private long 				start_time;
	private long 				end_time;
	private boolean 			buttonPress = false;
	private boolean 			paused = false;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.spectrogram );
		
		this.spectrogramView = (SpectrogramView) findViewById( R.id.surfaceView );
		fileWriter = new SpectrogramFileIO();		
		fft = new FFT( buffer_size );
	}

	/**
	 * Initialize AudioRecorder and register the AudioRecorder listener
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		updatePreferences();
		this.spectrogramView.setFrequency( frequency );
		this.spectrogramView.setLogScaling( scaling );
			if( !paused )
			{
				beginDataCollection();
			} 
			else 
			{
				//onDrawableSampleAvailable( null );
			}
		paused = false;
	}
	
	
	/**
	 * liveRender = sharedPreferences.getBoolean( Preferences.LIVE_RENDER, true );<br>
	 * frequency = sharedPreferences.getFloat( Preferences.FREQUENCY, 8000.0f );<br>
	 * scaling = sharedPreferences.getBoolean( Preferences.LOG_SCALING, true );
	 */
	 private void updatePreferences() 
	  {
	    SharedPreferences sharedPreferences = getSharedPreferences(Preferences.PREFS_NAME, 0);
	    liveRender = sharedPreferences.getBoolean( Preferences.LIVE_RENDER, true );
	    frequency = sharedPreferences.getFloat( Preferences.FREQUENCY, 8000.0f );
	    scaling = sharedPreferences.getBoolean( Preferences.LOG_SCALING, true );
	  }
	
	
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.aprad, menu);
    return true;
  }
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
	  switch ( item.getItemId() )
	  {
    	case R.id.settings:
    		stopDataCollection();
    		Intent settingsIntent = new Intent( Spectrogram.this, Preferences.class );
    		this.startActivity(settingsIntent);
    		return true;
    	case R.id.open_file:
    		stopDataCollection();
    		Intent openSavedDataIntent = new Intent( Spectrogram.this, OpenSavedData.class );
    		this.startActivity(openSavedDataIntent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
	  }
	}

	
   /**
	 * End the signal capture and unregister listener
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		stopped = true;
		stopDataCollection();
		paused  = true;
	}
	
	
	
	@Override
  public boolean onTouchEvent(MotionEvent event)
  {
	  if( !buttonPress )
	  {
	    buttonPress = true;
  	  	if( !rendering )
  	  	{
  	  		if( event.getAction() == MotionEvent.ACTION_DOWN )
  	  		{
  	  			float relativeTextSize = spectrogramView.getRelativeTopBarSize();
  	  			
      	  		if( event.getX() <= relativeTextSize * START_STOP_BUTTON_X_SCALAR &&
      	  			event.getY() <= relativeTextSize * START_STOP_BUTTON_Y_SCALAR )
      	  		{
      	  			toggleRecord();
      	  		}
  	  		} 
  	  	}
  	  buttonPress = false;
	  }
	  return true;
  }
  
 
	/**
	 * call this method after pressing the left top button
	 */
	private void toggleRecord() 
	{
		if( stopped )
		{
			beginDataCollection();
		} 
		else 
		{
			stopDataCollection();
			renderCollectedData();
		}
	}

	
	/**
	 * Set stopped =false<br>
	 * Initialize a new AudioRecorder instance--signalCapture<br>
	 * A new short[720]--rawSignals<br>
	 * Signal_count=0<br>
	 * Capture start_time in millis
	 */
	public void beginDataCollection()
	{
	  stopped = false;
	  start_time = System.currentTimeMillis();
	  this.signalCapture = new AudioRecorder(frequency,buffer_size, this );
	  //AudioRecorderListenerManipulate.registerFFTAvailListenerOnSpectrogram(this);
	  rawSignals = new short[SpectrogramView.MAX_SIGNALS_LENGTH][];
	  signal_count = 0;
	}
  
	
	/**
	 * Set stopped = true<br>
	 * SignalCapture.end()<br>
	 * Capture end_time in millis
	 */
	public void stopDataCollection()
	{
	  end_time = System.currentTimeMillis();
	  this.signalCapture.end();
	  stopped = true;
	  //AudioRecorderListenerManipulate.unregisterFFTAvailListener();
	}
	
	
	/**
	 * spectrogramView.undrawRecordingIndicator();<br>
	 * spectrogramView.drawButtonRendering();<br>
	 * drawSpectrogramRange();
	 */
	public void renderCollectedData()
	{
	  rendering = true;
	  if( !liveRender )
	  {
		  spectrogramView.undrawRecordingIndicator();
		  spectrogramView.drawButtonRendering();
		  drawSpectrogramRange();
	  }
	  rendering = false;
	}
	
	
  private void redrawData() {
    // TODO Auto-generated method stub
    
  }
  
  
  public static void setSaveFlag()
  {
	  saveFlag = true;
  }
  
	
	// TODO
	public void saveDataDialog()
	{
	  DialogFragment newFragment = new SaveDialog();
	  newFragment.show(getFragmentManager(), "Whatever?");
	  if( saveFlag )
	  {
	    saveData();
	    Spectrogram.saveFlag = false;
	  }
	  	  
	}
	
	// TODO
	public void saveData()
	{
		
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
	  File file;
	  try {
		  	//String fileName = Uri.parse(filename).getLastPathSegment();
		  	FileInputStream fio = new FileInputStream( filename );
		  	fio.toString();
   		  }
	  catch (IOException e) {
		  	// Error while creating file
	  	  }
	  */
	}
	

	
	/**
	 * Implements AudioRecorder listener. <br>
	 * Draws a new sample on the graph.
	 */
	@Override
	public void onDrawableSampleAvailable( short[] signal ) 
	{
		  loadSpectrogramGrid();
		  storeNextSignal( signal );
		  testLimitation();
		
		  // Render signal live
		  if( liveRender )
		  {
		    drawSpectrogram( signal_count - 1 );
		  // Record then render signal
		  } else if( recordingComplete() ){
			  renderCollectedData();
		  }
		  
		  
		  if( stopped )
		  {
			spectrogramView.drawTopDuration( getElapsedTime() );
			spectrogramView.drawButtonStart();
		    saveDataDialog();
		  }		 
		  
		
	}

	
	/**
	 * 
	 * @return end_time - start_time
	 */
	private long getElapsedTime() 
	{
		return end_time - start_time;
	}

  
	/**
	 * Tests to see :<br><br>
	 * if signal_count >= 720 ? <br>
	 * stopDataCollection(); -->signal_capture.end();
	 */
	public void testLimitation()
	{
		if( signal_count >= SpectrogramView.MAX_SIGNALS_LENGTH )
		{
			stopDataCollection();
		}
	}

  /**
   * Store raw signals in short[][] rawSignals<br><br>
   * rawSignals[signal_count] = signal;<br>
    	++ signal_count;
   * @param signal short[]
   */
  public void storeNextSignal( short[] signal )
  {
    if( signal == null )
    {
      return;
    }
    rawSignals[signal_count] = signal;
    signal_count++;
  }

  /**
   * Clear the graph, and redraw Axis<br><br>
   * if( signal_count == 0 )<br>
      spectrogramView.loadScaleValues();<br>
      spectrogramView.clearSprectrogramView();<br>
      spectrogramView.drawGrid();<br>
      spectrogramView.drawButtonStop();<br>
      spectrogramView.drawTopSampleRate();
   */
  public void loadSpectrogramGrid()
  {
    if( signal_count == 0 )
    {
      spectrogramView.loadScaleValues();
      spectrogramView.clearSprectrogramView();
      spectrogramView.drawGrid();
      spectrogramView.drawButtonStop();
      spectrogramView.drawTopSampleRate();
      if( !liveRender )
      {
        spectrogramView.drawRecordingIndicator();
      }
    }
  }

  /**
   * Draw the current(the last) signals size of 512 in one column.<br><br>
   * Call "spectrogramView.drawSpectrogram( double[] transformedSignals, ... ,offset )"<br>
   * That will call "spectrogramView.drawSpectrogramColumn()"
   * @param offset = signal_count-1, is the end location of collected signals
   */
  private void drawSpectrogram( int offset )
  {
    double[] signal = fft.calculateFFT( rawSignals[offset] );
    spectrogramView.drawSpectrogram(  signal, 
    								  frequency, 
    								  buffer_size, 
    								  signalCapture.getMaxFFTSample(),
    								  offset );
  }
  
  
  /**
   * Call spectrogramView.drawSpectrogramRange
   * ( double[][] transformedSignals, ... , signal_count, signal_count )
   */
  private void drawSpectrogramRange()
  {
    double[][] signals = calculateRawSignalsFFT();
    spectrogramView.drawSpectrogramRange( signals,
    									  frequency,
    									  buffer_size,
    									  signalCapture.getMaxFFTSample(),
    									  signal_count,
    									  signal_count );
  }

  /**
   * for i=0 to signal_count<br>
   * signals[i] = fft.calculateFFT( rawSignals[i] )
   * @return double[][] signals
   */
  private double[][] calculateRawSignalsFFT()
  {
    double[][] signals = new double[SpectrogramView.MAX_SIGNALS_LENGTH][];
    for( int i = 0; i < signal_count; ++ i )
    {
      signals[i] = fft.calculateFFT( rawSignals[i] );
    }
    return signals;
  }

  
  /**
   * 
   * @return whether signal_count = 720 ?
   */
  public boolean recordingComplete()
  {
    return (signal_count == SpectrogramView.MAX_SIGNALS_LENGTH);
  }

  
}

