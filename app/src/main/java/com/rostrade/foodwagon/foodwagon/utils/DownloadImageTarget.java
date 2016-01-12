package com.rostrade.foodwagon.foodwagon.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by frankie on 06.01.2016.
 */
public class DownloadImageTarget implements Target {

    public interface Callback {
        void onImageSaved(String filePath);
    }

    private String mFilePath;
    private Callback mCallback;

    public DownloadImageTarget(String filePath, Callback callback) {
        mFilePath = filePath;
        mCallback = callback;
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (bitmap != null) {
                    try {
                        FileOutputStream out = new FileOutputStream(mFilePath);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();

                        if (mCallback != null) {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onImageSaved(mFilePath);
                                }
                            };
                            mainHandler.post(runnable);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        // TODO: remove logging
        Log.d("DownloadImageTarget ", "downloading image");
    }
}
