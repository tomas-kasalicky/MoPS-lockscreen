package ca.uwaterloo.lockscreen;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

import ca.uwaterloo.lockscreen.imagelock.ImagePrototype;
import ca.uwaterloo.lockscreen.imagelock.ResourceHelper;
import ca.uwaterloo.lockscreen.utils.RandomHelper;

public class BrowseActivity extends Activity {

    private List<ImagePrototype> prototypes;

    private int currentProt = 0;
    private int currentInst = 0;
    private int count = 0;
    private boolean obfuscate;
    private boolean sample;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        sample = getIntent().getBooleanExtra("sample",false);

        prototypes = ResourceHelper.createPrototypes(getResources(), this);

        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        View obfButton = findViewById(R.id.btn_obf);
        obfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchObfuscation();
            }
        });

        if(sample){
            obfButton.setVisibility(View.INVISIBLE);
        }

        img = (ImageView)findViewById(R.id.browse_img);

        viewImage();
    }


    private void viewImage() {
        if(obfuscate || sample){
            img.setImageResource(prototypes.get(currentProt).ObfuscatedId[currentInst]);
        }else {
            img.setImageResource(prototypes.get(currentProt).Id[currentInst]);
        }
    }

    private void next(){
        if(sample){
            count++;
            if(count >= 10){
                finish();
                return;
            }

            currentProt = RandomHelper.randInt(0,prototypes.size());
            currentInst = RandomHelper.randInt(0,Array.getLength(prototypes.get(currentProt).Id));
        }else {
            currentInst++;
            if (currentInst >= Array.getLength(prototypes.get(currentProt).Id)) {
                currentInst = 0;
                currentProt++;

            }
            if (currentProt >= prototypes.size()) {
                currentProt = prototypes.size() - 1;
                finish();
                return;
            }
        }
        viewImage();
    }

    private void previous(){
        currentInst--;
        if(currentInst < 0){
            currentInst = Array.getLength(prototypes.get(currentProt).Id) - 1;
            currentProt--;
            if(currentProt < 0){
                currentProt = 0;
            }
        }
        viewImage();
    }

    private void switchObfuscation(){
        obfuscate = !obfuscate;
        viewImage();
    }

}
