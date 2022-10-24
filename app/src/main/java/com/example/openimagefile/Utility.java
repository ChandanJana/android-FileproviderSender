package com.example.openimagefile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class Utility {


    /**
     * Save image to ImageOpenExample folder.
     * @param image
     */
    static void storeImage(Bitmap image, Context context) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            Log.d("tab",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("tag", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("tag", "Error accessing file: " + e.getMessage());
        }
    }

    /**
     *  Get File directory.
     * @return File.
     */

    private static File getOutputMediaFile(Context context){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String extStorageDirectory = context.getExternalFilesDir("/ImageOpen").toString();
        //String dataPath = extStorageDirectory + "/ImageOpenExample";

        File mediaStorageDir = new File(extStorageDirectory);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdir();
        }

        // Create a media file name
//        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="SampleImage" +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

}
