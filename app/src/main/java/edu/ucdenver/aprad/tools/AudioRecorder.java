package edu.ucdenver.aprad.tools;

import edu.ucdenver.aprad.education.Education;
import edu.ucdenver.aprad.education.TimeDomain;
import edu.ucdenver.aprad.education.TimeDomainView;
import edu.ucdenver.aprad.spectrogram.Spectrogram;
import edu.ucdenver.aprad.spectrogram.SpectrogramView;
import edu.ucdenver.aprad.spectrum_analyzer.SpectrumAnalyzerView;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
/**
 * AudioRecorder
 * Extends Thread to run the AudioRecorder in it's own thread
 * @author Dan Rolls
 * @author Michael Dewar
 */
public class AudioRecorder extends Thread 
{
  
	private AudioRecord  recorder;
	private static boolean notRealTimeFlag = false;
	private long lastSystemTime 	= 0;
	private long currentSystemTime 	= 0;
	//private AudioRecordListener audioListener;
	private SpectrumAnalyzerView spectrumVisualizer 	= null;
	private Spectrogram 	 	 spectrogram 			= null;
	private TimeDomainView educationVisualizer 	= null;
	protected AudioRecorderListenerManipulate manipulater = new AudioRecorderListenerManipulate();
	
	private int          minBufferSizer;
	private double       sampleRate;		
	private boolean      stopped;
	
	private int          numFFTSamples;
	private static final int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
	private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
	public  static int SAMPLING_TIME_INTERVAL =2500; 
	//private static AudioRecordListener audioListener;
	
	
	/**
	 * Constructor for AudioRecorder
	 * @param sampleRate Frequency of samples in Hz
	 * @param points The number of FFT samples to take
	 */
	public AudioRecorder(double sampleRate, int points, SpectrumAnalyzerView spectrumVisual)
	{
		this.sampleRate = sampleRate;
		this.numFFTSamples = points;
		this.spectrumVisualizer = spectrumVisual;
		start();
	}
	
	public AudioRecorder(double sampleRate, int points, Spectrogram spectrogram)
	{
		this.sampleRate = sampleRate;
		this.numFFTSamples = points;
		this.spectrogram = spectrogram;
		start();
	}
	
	public AudioRecorder(double sampleRate, int points, TimeDomainView educationVisualizer)
	{
		this.sampleRate = sampleRate;
		this.numFFTSamples = points;
		this.educationVisualizer = educationVisualizer;
		start();
	}
	
	/**
	 * To use Thread, run must be implemented
	 * Starts the AudioRecorder and collects data for transformation
	 */
	@Override
	public void run()
	{
		this.minBufferSizer = AudioRecord.getMinBufferSize( (int) this.sampleRate,
															CHANNEL_CONFIGURATION,AUDIO_ENCODING );
		
		this.recorder = new AudioRecord( AUDIO_SOURCE,(int)this.sampleRate, CHANNEL_CONFIGURATION, 
											AUDIO_ENCODING,this.minBufferSizer );
		
		if( this.recorder == null )
		{
			return;
		}
		
		
				try {
						this.recorder.startRecording();
					} catch( IllegalStateException e ){
							Log.e( "RecordingFailed", e.toString() );	
						}		
				
		short[] signalBuffer = null;
		int bufferReadResult = 0;
		
		/**
		 * Very important iteration, after this thread constructed, 
		 * it will run just in this loop
		 */
		if(spectrumVisualizer!=null)
			AudioRecorderListenerManipulate.registerFFTAvailListenerOnSpectrumAnalyzer( manipulater, spectrumVisualizer );
		else 
			if(spectrogram!=null)
				AudioRecorderListenerManipulate.registerFFTAvailListenerOnSpectrogram( manipulater, spectrogram );
			else
			{
				AudioRecorderListenerManipulate.registerFFTAvailListenerOnEducation( manipulater, educationVisualizer );
				AudioRecorder.notRealTimeFlag = true;
				
			}
		
			while( !stopped ){			
				signalBuffer = new short[numFFTSamples];   
				bufferReadResult = this.recorder.read( signalBuffer, 0, numFFTSamples );
				
				// Check to make sure we read something
				if(bufferReadResult > 0)
				{
					if(AudioRecorder.notRealTimeFlag)
					{
						currentSystemTime = System.currentTimeMillis();
						long timeInterval = currentSystemTime - lastSystemTime;
						
						if(timeInterval >= AudioRecorder.SAMPLING_TIME_INTERVAL)
						{
							AudioRecorderListenerManipulate.notifyListenerSignalRead(signalBuffer);						
							lastSystemTime = currentSystemTime;
						}
						
					}else {
						
						AudioRecorderListenerManipulate.notifyListenerSignalRead(signalBuffer);						
					}
					
				} else {
					Log.e( "AudioRecorder",
							"There was an error reading the audio device - ERROR: " 
									+ bufferReadResult );
				}
			}

			AudioRecorderListenerManipulate.unregisterFFTAvailListener( );		
			
		AudioRecorder.notRealTimeFlag = false;
		recorder.stop();
		recorder.release();
	}
	
	
	/**
	 * Used to get Max FFT sample value from FFT Class
	 * @return Max FFT Sample from the FFT Processing
	 */
	public int getMaxFFTSample()
	{
		return numFFTSamples;
	}
	
	
	/**
	 * Ends the thread
	 */
	public void end()
	{ 
		stopped = true;
	}
	
	
}
