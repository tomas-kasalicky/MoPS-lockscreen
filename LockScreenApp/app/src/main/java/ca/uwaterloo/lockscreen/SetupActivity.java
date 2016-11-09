package ca.uwaterloo.lockscreen;

import android.app.Activity;
import android.os.Bundle;

import ca.uwaterloo.lockscreen.control.SwipeImageView;
import ca.uwaterloo.lockscreen.imagelock.ImageUnlocker;
import ca.uwaterloo.lockscreen.imagelock.LockSetup;
import ca.uwaterloo.lockscreen.imagelock.ResourceHelper;

public class SetupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        SwipeImageView swipeView = (SwipeImageView) findViewById(R.id.swipeSetup);
        swipeView.setActivity(this);
        LockSetup unlocker = new LockSetup(ResourceHelper.createPrototypes(getResources()));
        swipeView.setImageProvider(unlocker);
    }
}
