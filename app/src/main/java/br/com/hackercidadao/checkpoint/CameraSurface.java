package br.com.hackercidadao.checkpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Leonardo on 25/07/2015.
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "Preview";

    SurfaceHolder mHolder;
    public Camera camera;

    CameraSurface(Context context) {
        super(context);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public static void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();

        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = 0;
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.

        camera = Camera.open(1);

        setCameraDisplayOrientation(1, camera);
        try {
            camera.setPreviewDisplay(holder);


            camera.setPreviewCallback(new Camera.PreviewCallback() {

                public void onPreviewFrame(byte[] data, Camera arg1) {
//                    FileOutputStream outStream = null;
//                    try {
//                        outStream = new FileOutputStream(String.format(Environment.getExternalStorageDirectory()+ "/%d.jpg", System.currentTimeMillis()));
//                        outStream.write(data);
//                        outStream.close();
//                        Log.d(TAG, "onPreviewFrame - wrote bytes: " + data.length);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                    }
                    CameraSurface.this.invalidate();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(this.getWidth(),this.getHeight());
        camera.setParameters(parameters);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        this.getHolder().removeCallback(this);
        camera.stopPreview();
        camera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = camera.getParameters();
        Log.d("HUE",parameters.getSupportedPreviewFormats().toString());
        //parameters.setPreviewSize(w, h);
        parameters.setPictureFormat(ImageFormat.JPEG);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint p = new Paint(Color.RED);
        Log.d(TAG, "draw");
        //canvas.drawText("PREVIEW", canvas.getWidth() / 2, canvas.getHeight() / 2, p);
    }
}

