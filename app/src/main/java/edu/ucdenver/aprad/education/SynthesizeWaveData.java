package edu.ucdenver.aprad.education;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.ucdenver.aprad.R;

/**********************************************
 **
 ** Created by  * @author Nathan Vanderau
 ** @created 21 Nov 2014
 ** @modified_by
 ** @modified_date
 ** on 12/7/14.
 **
 **********************************************/

public class SynthesizeWaveData extends Activity
{
    public static final String SHARED_PREFERENCES = "SharedPreferences";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frequency_input);
        getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Button goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), SynthesizeWaveDataView.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}