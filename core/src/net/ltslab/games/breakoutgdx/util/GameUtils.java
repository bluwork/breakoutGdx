/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.util;

public class GameUtils {
    public static void print(String tag, Object... objects) {
        String toPrint = "";
        toPrint += tag + " *** ";
        for(Object o : objects) {
            toPrint += " " + o.toString();
        }
        System.out.println(toPrint);
    }


}
