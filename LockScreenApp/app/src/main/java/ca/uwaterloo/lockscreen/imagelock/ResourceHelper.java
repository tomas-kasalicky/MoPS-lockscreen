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
                        new ObfuscatedImagePair(res, R.drawable.pizza,R.drawable.pizza_obf),
                        new ObfuscatedImagePair(res, R.drawable.pizza1,R.drawable.pizza1_obf),
                        new ObfuscatedImagePair(res, R.drawable.pizza2,R.drawable.pizza2_obf),
                        new ObfuscatedImagePair(res, R.drawable.pizza3,R.drawable.pizza3_obf),
                        new ObfuscatedImagePair(res, R.drawable.pizza4,R.drawable.pizza4_obf)
                },
                ImagePrototype.DROP_AREA.NONE,
                new int[]{
                        R.drawable.pizza,
                        R.drawable.pizza1,
                        R.drawable.pizza2,
                        R.drawable.pizza3,
                        R.drawable.pizza4
                },new int[]{
                R.drawable.pizza_obf,
                R.drawable.pizza1_obf,
                R.drawable.pizza2_obf,
                R.drawable.pizza3_obf,
                R.drawable.pizza4_obf
        });
        prototypes.add(prot);

        prot = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res, R.drawable.bmw,R.drawable.bmw_obf),
                        new ObfuscatedImagePair(res, R.drawable.bmw1,R.drawable.bmw1_obf),
                        new ObfuscatedImagePair(res, R.drawable.bmw2,R.drawable.bmw2_obf)
                },
                ImagePrototype.DROP_AREA.NONE,
                new int[]{
                        R.drawable.bmw,
                        R.drawable.bmw1,
                        R.drawable.bmw2
                },new int[]{
                R.drawable.bmw_obf,
                R.drawable.bmw1_obf,
                R.drawable.bmw2_obf
        });
        prototypes.add(prot);

        prot = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res, R.drawable.coffee,R.drawable.coffee_obf),
                        new ObfuscatedImagePair(res, R.drawable.coffee1,R.drawable.coffee1_obf),
                        new ObfuscatedImagePair(res, R.drawable.coffee2,R.drawable.coffee2_obf),
                        new ObfuscatedImagePair(res, R.drawable.coffee3,R.drawable.coffee3_obf),
                },
                ImagePrototype.DROP_AREA.NONE,
                new int[]{
                        R.drawable.coffee,
                        R.drawable.coffee1,
                        R.drawable.coffee2,
                        R.drawable.coffee3,
                },new int[]{
                R.drawable.coffee_obf,
                R.drawable.coffee1_obf,
                R.drawable.coffee2_obf,
                R.drawable.coffee3_obf,
        });
        prototypes.add(prot);

        prot = new ImagePrototype(
                new ObfuscatedImagePair[]{
                    new ObfuscatedImagePair(res, R.drawable.roses,R.drawable.roses_obf),
                    new ObfuscatedImagePair(res, R.drawable.roses1,R.drawable.roses1_obf),
                },
                ImagePrototype.DROP_AREA.NONE,
                new int[]{
                    R.drawable.roses,
                    R.drawable.roses1,
                },new int[]{
                    R.drawable.roses_obf,
                    R.drawable.roses1_obf,
            });
        prototypes.add(prot);

        prototypes = ApplyPreferences(prototypes, act);

        return prototypes;
    }

    private static List<ImagePrototype> ApplyPreferences(List<ImagePrototype> prototypes, Activity act) {
        SharedPreferences pref = act.getSharedPreferences("ca.uwaterloo.lockscreen", Context.MODE_PRIVATE);

        for (ImagePrototype img : prototypes) {
            img.unlockArea = toDROP_AREA(pref.getInt(Integer.toString(img.Id[0]), toInt(ImagePrototype.DROP_AREA.NONE)));
        }

        return prototypes;
    }


    public static void SavePreferences(List<ImagePrototype> prototypes, Activity act) {
        SharedPreferences pref = act.getSharedPreferences("ca.uwaterloo.lockscreen", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = pref.edit();

        for (ImagePrototype img : prototypes) {
            ed.putInt(Integer.toString(img.Id[0]), toInt(img.unlockArea));
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
