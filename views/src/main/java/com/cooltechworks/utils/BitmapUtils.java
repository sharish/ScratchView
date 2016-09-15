package com.cooltechworks.utils;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * Created by sharish on 15/09/16.
 */
public class BitmapUtils {


    /**
     * Compares two bitmaps and gives the percentage of similarity
     *
     * @param bitmap1 input bitmap 1
     * @param bitmap2 input bitmap 2
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     *
     */
    public static float compareEquivalance(Bitmap bitmap1, Bitmap bitmap2) {

        if (bitmap1 == null || bitmap2 == null || bitmap1.getWidth() != bitmap2.getWidth() || bitmap1.getHeight() != bitmap2.getHeight()) {
            return 0f;
        }


        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        byte[] array1 = buffer1.array();
        byte[] array2 = buffer2.array();

        int len = array1.length; // array1 and array2 will be of some length.
        int count = 0;

        for(int i=0;i<len;i++) {
            if(array1[i] == array2[i]) {
                count++;
            }
        }

        return ((float)(count))/len;

    }
}
