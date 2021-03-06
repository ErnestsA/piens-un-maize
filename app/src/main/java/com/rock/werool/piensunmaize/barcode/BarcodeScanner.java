package com.rock.werool.piensunmaize.barcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.rock.werool.piensunmaize.R;

import java.io.IOException;

public class BarcodeScanner extends AppCompatActivity {
    CameraSource cameraSource;
    final int cameraPermissionCode = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case cameraPermissionCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    this.recreate();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(BarcodeScanner.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BarcodeScanner.this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BarcodeScanner.this,
                        new String[]{Manifest.permission.CAMERA},
                        cameraPermissionCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {


            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
            final SurfaceView cameraView = (SurfaceView) findViewById(R.id.camera_view);
            final TextView barcodeInfo = (TextView) findViewById(R.id.code_info);
            CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1600, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(30.0f);
            cameraSource = builder.build();


            final ImageView flash = (ImageView)findViewById(R.id.flash);

            flash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flashOnButton();
                    if (flashmode) {
                        flash.setImageResource(R.drawable.flash_outline_yellow);
                    } else {
                        flash.setImageResource(R.drawable.flash_outline_grey);
                    }
                }
            });

            cameraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                                            //empty
                }
            });

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        cameraSource.start(cameraView.getHolder());   //ignore
                        /*
                        camera = getCamera(cameraSource);
                        Camera.Parameters param = camera.getParameters();
                        param.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                        camera.setParameters(param);
                        */
                    } catch (IOException e) {
                        Log.e("CAMERA SOURCE", e.getMessage());
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                    if (barcodes.size() != 0) {
                        BarcodeAction barAction = new BarcodeAction();
                        //barAction.executeActionFromBarcode(barcodes.valueAt(0).displayValue);
                        barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                            public void run() {
                                barcodeInfo.setText(    // Update the TextView
                                        barcodes.valueAt(0).displayValue
                                );
                            }
                        });
                    }
                }
            });
        }
    }
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
    private Camera camera = null;
    boolean flashmode=false;
    private void flashOnButton() {
        camera=getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode?Camera.Parameters.FLASH_MODE_TORCH :Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
