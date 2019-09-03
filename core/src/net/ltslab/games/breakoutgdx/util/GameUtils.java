/*
 * Copyright (c) 2019. BLoo
 */

package net.ltslab.games.breakoutgdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameUtils {
    public static void print(String tag, Object... objects) {
        String toPrint = "";
        toPrint += tag + " *** ";
        for(Object o : objects) {
            toPrint += " " + o.toString();
        }
        System.out.println(toPrint);
    }

    public static void writeImageFromBytes(byte[] pixels) {

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGB888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(new FileHandle(Gdx.files.getLocalStoragePath() + "mynoalpha.png"), pixmap);
        pixmap.dispose();
    }

    public static byte [] removeAlpha(byte[] pixels) {
        int noAlphaSize = pixels.length * 3 / 4;
        byte[] noAlpha = new byte[noAlphaSize];

        int alphaIndex = 0;
        for (int i = 0; i < pixels.length; i++) {

        if ((i + 1) % 4 == 0) {
            continue;
        }
        noAlpha[alphaIndex] = pixels[i];
        alphaIndex++;
        }
        return noAlpha;
    }

    public static byte[] takeSnapshot() {
        return ScreenUtils.getFrameBufferPixels(true);
    }

    public static byte[] takeNoAlphaSnapshot() {
        return removeAlpha(takeSnapshot());
    }
}
