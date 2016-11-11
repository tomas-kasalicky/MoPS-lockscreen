package ca.uwaterloo.lockscreen.imagelock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Collection;

import ca.uwaterloo.lockscreen.R;
import ca.uwaterloo.lockscreen.utils.RandomHelper;

/**
 * Created by kasal on 08.11.2016.
 */

public class ImagePrototype {

    public final int Id;

    private final ObfuscatedImagePair[] imageData;
    private int currentImage;


    public Bitmap getImage(boolean obfuscated) {
        if(obfuscated){
            return imageData[currentImage].obfuscatedImage;
        }else{
            return imageData[currentImage].clearImage;
        }
    }

    public enum DROP_AREA {
        NONE, LEFT, RIGHT, TOP, BOTTOM;
    }

    public DROP_AREA unlockArea;

    public ImagePrototype(ObfuscatedImagePair[] imageData, DROP_AREA unlockArea, int id){
        this.unlockArea = unlockArea;
        this.imageData = imageData;
        this.Id = id;
        shuffleImages();
    }

    public boolean isCorrectDropArea(DROP_AREA dropArea) {
        return unlockArea == dropArea || unlockArea == DROP_AREA.NONE;
    }

    public void shuffleImages() {
        currentImage = RandomHelper.randInt(0, imageData.length);
    }

    public void dispose(){
        for (ObfuscatedImagePair img: imageData) {
            img.dispose();
        }
    }

}
