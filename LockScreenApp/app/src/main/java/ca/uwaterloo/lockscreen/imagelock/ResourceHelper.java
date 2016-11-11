package ca.uwaterloo.lockscreen.imagelock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.lockscreen.R;

/**
 * Created by kasal on 08.11.2016.
 */

public class ResourceHelper {

    public static List<ImagePrototype> createPrototypes(Resources res, Activity act){
        List<ImagePrototype> prototypes = new ArrayList<>();

        ImagePrototype prot = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res, R.drawable.pizza,R.drawable.pizza_obf)
                },
                ImagePrototype.DROP_AREA.NONE,
                R.drawable.pizza);
        ImagePrototype prot2 = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res,R.drawable.skyline,R.drawable.skyline_obf)
                },
                ImagePrototype.DROP_AREA.NONE,
                R.drawable.skyline);

        prototypes.add(prot);
        prototypes.add(prot2);
        prototypes.add(prot);
        prototypes.add(prot2);


        prototypes = ApplyPreferences(prototypes, act);

        return prototypes;
    }

    private static List<ImagePrototype> ApplyPreferences(List<ImagePrototype> prototypes, Activity act) {
        SharedPreferences pref = act.getSharedPreferences("ca.uwaterloo.lockscreen", Context.MODE_PRIVATE);

        for (ImagePrototype img : prototypes) {
            img.unlockArea = toDROP_AREA(pref.getInt(Integer.toString(img.Id), toInt(ImagePrototype.DROP_AREA.NONE)));
        }

        return prototypes;
    }


    public static void SavePreferences(List<ImagePrototype> prototypes, Activity act) {
        SharedPreferences pref = act.getSharedPreferences("ca.uwaterloo.lockscreen", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = pref.edit();

        for (ImagePrototype img : prototypes) {
            ed.putInt(Integer.toString(img.Id), toInt(img.unlockArea));
        }

        ed.apply();
    }

    public static int toInt(ImagePrototype.DROP_AREA area){
        if(area == ImagePrototype.DROP_AREA.NONE)return 0;
        if(area == ImagePrototype.DROP_AREA.LEFT)return 1;
        if(area == ImagePrototype.DROP_AREA.RIGHT)return 2;
        if(area == ImagePrototype.DROP_AREA.TOP)return 3;
        if(area == ImagePrototype.DROP_AREA.BOTTOM)return 4;
        return 0;
    }

    public static ImagePrototype.DROP_AREA toDROP_AREA(int i){
        if(i == 0)return ImagePrototype.DROP_AREA.NONE;
        if(i == 1)return ImagePrototype.DROP_AREA.LEFT;
        if(i == 2)return ImagePrototype.DROP_AREA.RIGHT;
        if(i == 3)return ImagePrototype.DROP_AREA.TOP;
        if(i == 4)return ImagePrototype.DROP_AREA.BOTTOM;
        return ImagePrototype.DROP_AREA.NONE;
    }

}
