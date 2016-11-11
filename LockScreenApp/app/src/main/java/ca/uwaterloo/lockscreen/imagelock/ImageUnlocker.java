package ca.uwaterloo.lockscreen.imagelock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ca.uwaterloo.lockscreen.R;
import ca.uwaterloo.lockscreen.utils.RandomHelper;

/**
 * Created by kasal on 08.11.2016.
 */

public class ImageUnlocker implements ImageProvider{

    public static boolean obfuscate = false;

    private static final int SEQUENCE_SIZE = 3;
    private boolean sequenceCorrect;

    private List<ImagePrototype> prototypes;
    private List<Integer> currentSequence;

    int currentSequenceIndex;
    private boolean unlocked;

    public ImageUnlocker(List<ImagePrototype> prototypes){
        this.prototypes = prototypes;
        restartSequence();
    }

    private void restartSequence() {
        sequenceCorrect = true;
        unlocked = false;
        currentSequenceIndex = 0;
        currentSequence = generateRandomSequence();
    }

    private List<Integer> generateRandomSequence() {
        List<Integer> sequence = new ArrayList<Integer>();
        for(int i = 0; i < SEQUENCE_SIZE; i++){
            int r;
            do {
                r = RandomHelper.randInt(0, prototypes.size());
            }while(sequence.contains(r));
            sequence.add(r);
        }
        return sequence;
    }

    public Bitmap getCurrentImage() {

        ImagePrototype img = getCurrentPrototype();
        if(img == null){
            return null;
        }
        return img.getImage(obfuscate);
    }

    public Bitmap getNextImage() {
        ImagePrototype next = getNextPrototype();
        if(next == null){
            return null;
        }else {
            return next.getImage(obfuscate);
        }
    }

    public void dropAreaSelected(ImagePrototype.DROP_AREA dropArea) throws ImageUnlockFailedException{
        if(dropArea == ImagePrototype.DROP_AREA.NONE){
            return;
        }
        if(!isCorrectDropArea(dropArea)){
            sequenceCorrect = false;
        }
        currentSequenceIndex++;
        if(!checkSequenceFinished()) {
            getCurrentPrototype().shuffleImages();
        }
    }

    private boolean checkSequenceFinished() throws ImageUnlockFailedException{
        if(currentSequenceIndex == currentSequence.size()){
            if(!sequenceCorrect){
                restartSequence();
                throw new ImageUnlockFailedException();
            }else{
                unlocked = true;
            }
        }
        return unlocked;
    }

    private boolean isCorrectDropArea(ImagePrototype.DROP_AREA dropArea) {
        ImagePrototype prot = getCurrentPrototype();
        return prot.isCorrectDropArea(dropArea);
    }

    private ImagePrototype getCurrentPrototype() {
        if(currentSequenceIndex >= currentSequence.size()){
            return null;
        }
        return prototypes.get( currentSequence.get(currentSequenceIndex) );
    }
    private ImagePrototype getNextPrototype() {
        if(currentSequenceIndex + 1 < currentSequence.size()) {
            return prototypes.get(currentSequence.get(currentSequenceIndex + 1));
        }else{
            return null;
        }
    }

    public boolean isFinished() {
        return unlocked;
    }


    public void dispose(){
        for (ImagePrototype img: prototypes) {
            img.dispose();
        }
    }

    public class ImageUnlockFailedException extends Throwable {


    }
}
