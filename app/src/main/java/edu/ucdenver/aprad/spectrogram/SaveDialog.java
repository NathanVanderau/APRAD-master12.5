package edu.ucdenver.aprad.spectrogram;

import edu.ucdenver.aprad.R.string;
import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import edu.ucdenver.aprad.spectrogram.Spectrogram;

public class SaveDialog extends DialogFragment 
{
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) 
  {
      // Use the Builder class for convenient dialog construction
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      
      builder.setMessage(string.save_dialog).setPositiveButton(string.save, new DialogInterface.OnClickListener() 
      		  
      		{
                 public void onClick(DialogInterface dialog, int id) 
                 {                	 
                	 Spectrogram.setSaveFlag();
                	 
                 }
      		}
      ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
             {
                 public void onClick(DialogInterface dialog, int id) {
                     // User cancelled the dialog
                 }
             }
      );
      // Create the AlertDialog object and return it
      return builder.create();
  }
}
