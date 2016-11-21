package ca.uwaterloo.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.uwaterloo.lockscreen.imagelock.ImageUnlocker;

public class StartupActivity extends Activity {

    private Button lockButton;
    private Button lockButtonObf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        lockButton = (Button)findViewById(R.id.lockBtn);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock(false);
            }
        });

        lockButtonObf = (Button)findViewById(R.id.lockObfBtn);
        lockButtonObf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock(true);
            }
        });

        Button setupButton = (Button) findViewById(R.id.setupBtn);
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });

        Button browseButton = (Button) findViewById(R.id.browseBtn);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browse();
            }
        });

        Button sampleButton = (Button) findViewById(R.id.sampleBtn);
        sampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample();
            }
        });
    }

    private void setup() {
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

    private void browse() {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }


    private void sample() {
        Intent intent = new Intent(this, BrowseActivity.class);
        intent.putExtra("sample", true);
        startActivity(intent);
    }

    private void lock(boolean obfuscate) {
        ImageUnlocker.obfuscate = obfuscate;
        Intent intent = new Intent(this, LockScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
