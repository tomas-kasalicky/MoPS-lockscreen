package ca.uwaterloo.lockscreen.imagelock;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.lockscreen.R;

/**
 * Created by kasal on 08.11.2016.
 */

public class ResourceHelper {

    public static List<ImagePrototype> createPrototypes(Resources res){
        List<ImagePrototype> prototypes = new ArrayList<>();
        ImagePrototype prot = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res, R.drawable.pizza,R.drawable.pizza_obf)
                },
                ImagePrototype.DROP_AREA.BOTTOM);
        ImagePrototype prot2 = new ImagePrototype(
                new ObfuscatedImagePair[]{
                        new ObfuscatedImagePair(res,R.drawable.skyline,R.drawable.skyline_obf)
                },
                ImagePrototype.DROP_AREA.TOP);

        prototypes.add(prot);
        prototypes.add(prot2);
        prototypes.add(prot);
        prototypes.add(prot2);

        return prototypes;
    }

}
