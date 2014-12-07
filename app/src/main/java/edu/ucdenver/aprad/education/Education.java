package edu.ucdenver.aprad.education;

import edu.ucdenver.aprad.R;
import edu.ucdenver.aprad.education.RawSignal;
import edu.ucdenver.aprad.info.About;
import edu.ucdenver.aprad.info.Help;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The Education feature is used to allow Physicist and Electrical Engineers to either real saved
 * wave data, or sythesize wave data adjustable in the areas of frequency, amplitude and phase
 * shift. The ability to study and display multiple waves at once, and the interaction between them
 * is desired. This will allow the Physicists and Electrical Engineers to study the resultant waves.
 * <br><br>
 *
 * @author Nathan Vanderau
 * @created 21 Nov 2014
 * @modified_by
 * @modified_date
 */

public class Education extends Activity {

    public static final String SHARED_PREFERENCES = "SharedPreferences";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education);
        getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

/*        Button synthesizeWaveDataButton = (Button) findViewById(R.id.synthesizeWaveDataButton);
        synthesizeWaveDataButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                Intent intent = new Intent( v.getContext(), SimulateWaveData.class);
                v.getContext().startActivity(intent);
            }
        });
*/
/*        Button useSavedDataButton = (Button) findViewById(R.id.useSavedDataButton);
        useSavedDataButton.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                Intent intent = new Intent( v.getContext(), UseSavedData.class);
                v.getContext().startActivity(intent);
            }
        });
*/
        Button viewRawSignalButton = (Button) findViewById(R.id.viewRawSignalButton);
        viewRawSignalButton.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v ){
                Intent intent = new Intent( v.getContext(), RawSignal.class);
                v.getContext().startActivity(intent);
            }
        });

        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                Intent intent = new Intent( v.getContext(), Help.class);
                v.getContext().startActivity(intent);
            }
        });

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ){
                Intent intent = new Intent( v.getContext(), About.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}
