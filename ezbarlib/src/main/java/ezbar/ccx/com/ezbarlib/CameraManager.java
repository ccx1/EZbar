package ezbar.ccx.com.ezbarlib;

import android.hardware.Camera;
import android.view.View;

public class CameraManager {

    private View    mScannerView;
    private Camera  mCamera;
    private boolean isScan;
    public static final int START = 1;
    public static final int STOP  = 0;


    public CameraManager(View view) {
        this.mScannerView = view;
    }

    /**
     * A safe way to get an instance of the Camera object.
     * @return Camera
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            if (mCamera != null) {
                return mCamera;
            }
            c = Camera.open();
            this.mCamera = c;
        } catch (Exception e) {
        }
        return c;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public void stop() {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        isScan = false;
        if (mOnStatusChangeListener != null) {
            mOnStatusChangeListener.onChange(STOP);
        }
    }

    public void start() {
        try {
            mCamera.startPreview();
            isScan = true;
            if (mOnStatusChangeListener != null) {
                mOnStatusChangeListener.onChange(START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        mCamera.setPreviewCallback((Camera.PreviewCallback) mScannerView);
        start();
        autoFocus();
    }

    public void autoFocus() {
        mCamera.autoFocus((Camera.AutoFocusCallback) mScannerView);
    }

    public void openFlash(boolean isOpen) {
        Camera.Parameters parameters = mCamera.getParameters();
        if (isOpen) {
            try {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } catch (Exception ignored) {
            }
        } else {
            try {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } catch (Exception ignored) {
            }

        }
    }

    private onStatusChangeListener mOnStatusChangeListener;

    public void setOnStatusChangeListener(onStatusChangeListener onStatusChangeListener) {
        mOnStatusChangeListener = onStatusChangeListener;
    }

    public interface onStatusChangeListener {
        void onChange(int status);
    }
}
