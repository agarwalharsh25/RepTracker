package com.reptracker.android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.reptracker.android.databinding.ActivityCaptureSignatureBinding;
import com.reptracker.android.utilities.DrawingView;
import com.reptracker.android.utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptureSignatureActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    private static final String TAG = CaptureSignatureActivity.class.getSimpleName();

    private ActivityCaptureSignatureBinding binding;

    private Utility utility = new Utility();

    private DrawingView mDrawLayout;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCaptureSignatureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDrawLayout = binding.drawingView;

        mDrawLayout.setVisibility(View.VISIBLE);
        mDrawLayout.setDrawingCacheEnabled(true);
        mDrawLayout.setEnabled(true);
        mDrawLayout.invalidate();
        mDrawLayout.setBackgroundColor(Color.WHITE);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("signatureCaptured", false);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        binding.clearSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawLayout.clear();
                mDrawLayout.invalidate();
            }
        });

        binding.submitSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();

            }
        });

        surfaceView = binding.cameraPreview;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }

    void capturePicture() {
        if (camera != null) {
            Log.d(TAG, "capture picture");
            camera.takePicture(null, null, this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
                try {
                    camera = Camera.open( camIdx );
                    camera.setDisplayOrientation(90);
                    try {
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("signatureCaptured", false);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        String pictureFileName = "RepTracker_pic_" + System.currentTimeMillis() + ".png";
        String pictureAbsolutePath = utility.createDirectoryAndSaveFile(data, "Pictures", pictureFileName);

        Bitmap bitmap = Bitmap.createBitmap(mDrawLayout.getWidth(), mDrawLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawLayout.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String signatureFileName = "RepTracker_sig_" + System.currentTimeMillis() + ".png";
        String signatureAbsolutePath = utility.createDirectoryAndSaveFile(byteArray, "Signatures", signatureFileName);

        Intent i = new Intent();
        i.putExtra("signatureCaptured", true);
        i.putExtra("signaturePath", signatureAbsolutePath);
        i.putExtra("picturePath", pictureAbsolutePath);
        setResult(RESULT_OK, i);
        finish();

    }

}