package ca.uwaterloo.lockscreen.imagelock;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by kasal on 08.11.2016.
 */


/**
 * Created by kasal on 08.11.2016.
 */

public class LockSetup implements ImageProvider {

    private List<ImagePrototype> prototypes;
    private int currentIndex;

    public LockSetup(List<ImagePrototype> prototypes) {
        this.prototypes = prototypes;
        restartSequence();
    }

    private void restartSequence() {
        currentIndex = 0;
    }

    public Bitmap getCurrentImage() {

        return getCurrentPrototype().getImage(false);
    }

    public Bitmap getNextImage() {
        ImagePrototype next = getNextPrototype();
        if (next == null) {
            return null;
        } else {
            return next.getImage(false);
        }
    }

    public void dropAreaSelected(ImagePrototype.DROP_AREA dropArea) throws ca.uwaterloo.lockscreen.imagelock.ImageUnlocker.ImageUnlockFailedException {
        if (dropArea == ImagePrototype.DROP_AREA.NONE) {
            return;
        }
        prototypes.get(currentIndex).unlockArea = dropArea;
        currentIndex++;
    }

    private ImagePrototype getCurrentPrototype() {
        return prototypes.get(currentIndex);
    }

    private ImagePrototype getNextPrototype() {
        if (currentIndex + 1 < prototypes.size()) {
            return prototypes.get(currentIndex + 1);
        } else {
            return null;
        }
    }

    public boolean isFinished() {
        return currentIndex == prototypes.size();
    }

}
