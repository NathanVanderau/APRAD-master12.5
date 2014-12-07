package edu.ucdenver.aprad.spectrogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;

public class SpectrogramFileIO extends Activity 
{
  public static final Integer SIGNAL_COUNT  = 720;
  public static final Integer SIGNAL_LENGTH = 256;
  public static String filename = "test_file.txt";
  public String line;
  
  public void writeFile( double[][] signals )
  {  
     //String.valueOf( System.currentTimeMillis() ) + ".aprad";
    String data = new String();
    for( int i = 0; i < SIGNAL_COUNT; ++ i )
    {
      for( int j = 0; j < signals[i].length; ++ j )
      {
        data += String.valueOf( signals[i][j] );
        	if( j != signals[i].length - 1 )
        	{
        		data += ", ";
        	} else {
        		data += "\n";
        	}
      }
    }
    
    	try {
    		FileOutputStream output = openFileOutput( filename, Context.MODE_PRIVATE );
    		output.write(data.getBytes());
    		output.close();
    		} catch (Exception e) {
    		e.printStackTrace();
    		}
  }
  
  
  public String readStringFile() throws IOException
  {
	  String line = new String();	 
	  
	  try {
  			FileInputStream input = openFileInput( filename );
  			
  			int length = input.available();   
  	         byte [] buffer = new byte[length];   
  	        input.read(buffer); 
  	      line = EncodingUtils.getString(buffer, "UTF-8");
  			//inputScanner.useLocale(Locale.US);
 

  		} catch ( FileNotFoundException e ) {
  			e.printStackTrace();
  		}

	  return line;
	  
  }
  
  
  
  public double[][] readFile( String filename )
  {
    double[][] signals = new double[SIGNAL_COUNT][SIGNAL_LENGTH]; 
    
    try {
    		FileInputStream input = openFileInput( filename );
    		Scanner inputScanner = new Scanner(input);
    		inputScanner.useLocale(Locale.US);
    		int j = 0;
    		
    		while( inputScanner.hasNextLine() && j < SIGNAL_COUNT )
    		{
    	  
    			String line = inputScanner.nextLine();
    			Scanner lineScanner = new Scanner(line);
    			lineScanner.useDelimiter(",\\s*");
    			int i = 0;
        
    			while( lineScanner.hasNextDouble() && i < SIGNAL_LENGTH )
    			{
    				signals[i][j] = lineScanner.nextDouble();
    				++ i;
    			}
    			
    			++ j;
    		}
    	} catch ( FileNotFoundException e ) {
    		e.printStackTrace();
    	}

    return signals;
  }
  
  
}
