package com.example.openimagefile;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button button;

    static final Integer WRITE_EXST = 1;
    static final Integer READ_EXST = 2;

    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".myfileprovider";
    static final Integer PERMISSION_REQUEST_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        // Check Permission.
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);


        button.setOnClickListener(v -> {
            // Check Permission.
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);

            // Save the image from mipmap folder to ImageOpenExample folder.
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image);
            Log.e("bitmap", bitmap.toString());

            Utility.storeImage(bitmap, this);

            String extStorageDirectory = getExternalFilesDir("/ImageOpen").toString();
            //String dataPath = extStorageDirectory + "/SampleImage.jpg";

            File currentfile = new File(extStorageDirectory);
            Log.e("TAGG", "File list size " + currentfile.listFiles().length);

            Uri outputUri = FileProvider.getUriForFile(this, AUTHORITY, currentfile.listFiles()[1]);
            provide(outputUri);
            /*Intent viewFile = new Intent(Intent.ACTION_VIEW);
            viewFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            viewFile.setDataAndType(outputUri, "image/*");
            startActivity(viewFile);*/

        });
    }

    public void provide(Uri outputUri) {
        //String content = "Hello FileProvider! ".concat(String.valueOf(System.currentTimeMillis()));
        //File file = new File(getFilesDir(), UUID.randomUUID().toString().concat(".txt"));

        //Uri uri = FileProvider.getUriForFile(this, AUTHORITY, file);

        Intent intent = new Intent().setClassName("com.demo.filereceiver", "com.demo.filereceiver.MainActivity");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String apkPackage = "com.demo.filereceiver";
        Intent LaunchIntent = new Intent(Intent.ACTION_SEND);
        LaunchIntent.putExtra("Sending", "Test Data");
        //LaunchIntent.setAction(Intent.ACTION_SEND);
        //LaunchIntent.setType("image/*");
        LaunchIntent.setType("application/zip");
        LaunchIntent.putExtra(Intent.EXTRA_STREAM, outputUri);
        LaunchIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        LaunchIntent.setPackage(apkPackage);
        ClipData clipData = new ClipData(new ClipDescription("Meshes", new String[]{ClipDescription.MIMETYPE_TEXT_URILIST}), new ClipData.Item(outputUri));
        LaunchIntent.setClipData(clipData);
        startActivity(Intent.createChooser(LaunchIntent, "share file with"));

        //ClipData clipData = new ClipData(new ClipDescription("Meshes", new String[]{ClipDescription.MIMETYPE_TEXT_URILIST}), new ClipData.Item(outputUri));
        //intent.setClipData(clipData);
        //startActivity(intent);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ID) {

            if (grantResults.length > 1
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.e("permission", "Permission not granted");

            }
        }
    }

}
