package ca.uwaterloo.lockscreen.imagelock;

import android.graphics.Bitmap;


/**
 * Created by kasal on 08.11.2016.
 */

public interface ImageProvider {

    Bitmap getCurrentImage();
    Bitmap getNextImage();
    void dropAreaSelected(ImagePrototype.DROP_AREA dropArea) throws ImageUnlocker.ImageUnlockFailedException ;
    boolean isFinished();
}
