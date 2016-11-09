package ca.uwaterloo.lockscreen.imagelock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kasal on 08.11.2016.
 */


public class ObfuscatedImagePair{
    public final Bitmap clearImage;
    public final Bitmap obfuscatedImage;

    public ObfuscatedImagePair(Resources res, int clearId, int obfuscatedId){
        clearImage = BitmapFactory.decodeResource(res, clearId);
        obfuscatedImage = BitmapFactory.decodeResource(res, obfuscatedId);
    }
}