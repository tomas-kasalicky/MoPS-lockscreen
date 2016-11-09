package ca.uwaterloo.lockscreen.utils;
import java.util.Random;

/**
 * Created by kasal on 08.11.2016.
 */

public class RandomHelper {
    private static Random rand = new Random();

    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt(max - min) + min;

        return randomNum;
    }
}
