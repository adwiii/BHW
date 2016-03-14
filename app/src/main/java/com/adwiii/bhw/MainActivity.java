package com.adwiii.bhw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText p1Name;
    EditText p2Name;
    Spinner diff;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Util.removeTitle(this);

        setContentView(R.layout.activity_main);

        p1Name = (EditText) findViewById(R.id.p1Name);
        p2Name = (EditText) findViewById(R.id.p2Name);

        diff = (Spinner) findViewById(R.id.diffSpinner);

        start = (Button) findViewById(R.id.startButton);

        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startButton) {
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra(GameActivity.P1_NAME, p1Name.getText().toString());
            i.putExtra(GameActivity.P2_NAME, p2Name.getText().toString());
            i.putExtra(GameActivity.DIFF, diff.getSelectedItemPosition());
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
