package edu.ucdenver.aprad.education;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import edu.ucdenver.aprad.R;

/*****************************
 **
 ** @author Nathan Vanderau
 ** @created 21 Nov 2014
 ** @modified_by
 ** @modified_date
 **
 ** From an online tutorial that can be found at:
 ** http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/
 **
 *****************************/

public class FileChooser extends ListActivity
{
    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }

    //Fils in File info
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Option>dir = new ArrayList<Option>();
        List<Option>fls = new ArrayList<Option>();
        try
        {
            for(File ff: dirs)
            {
                if(ff.isDirectory())
                    dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                else
                {
                    fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                }
            }
        }
        catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory",f.getParent()));
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.open_file, dir);
        this.setListAdapter(adapter);
    }

    //Handles when users click on files and folders.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Option option = adapter.getItem(position);
        if(option.getData().equalsIgnoreCase("folder")||option.getData().equalsIgnoreCase("parent directory")){
            currentDir = new File(option.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(option);
        }
    }
    private void onFileClick(Option option)
    {
        Toast.makeText(this, "File Clicked: "+option.getName(), Toast.LENGTH_SHORT).show();
    }
}
