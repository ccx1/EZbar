package ezbar.ccx.com.ezbarlib;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class ScannerView extends FrameLayout implements Camera.PreviewCallback, Camera.AutoFocusCallback, CameraManager.onStatusChangeListener {
    private Handler      mMainHanlder;
    private Context      mContext;
    private ImageScanner scanner;
    private boolean      isPreview;

    static {
        System.loadLibrary("iconv");
    }

    private CameraManager           mCameraManager;
    private ParsingCompleteListener parsingCompleteListener;
    private ViewfinderView          mViewfinderView;

    public ScannerView(@NonNull Context context) {
        this(context, null);
    }

    public ScannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mMainHanlder = new Handler();
        init();
    }

    private void init() {
        mCameraManager = new CameraManager(this);
        CameraPreview cameraPreview = new CameraPreview(mContext, mCameraManager, this, this);
        this.addView(cameraPreview);
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        mViewfinderView = new ViewfinderView(mContext, null);
        this.addView(mViewfinderView);
        mCameraManager.setOnStatusChangeListener(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size       size       = parameters.getPreviewSize();
        Image             barcode    = new Image(size.width, size.height, "Y800");
        barcode.setData(data);
        long start  = System.currentTimeMillis();
        int  result = scanner.scanImage(barcode);
        if (result != 0) {
            mCameraManager.stop();
            SymbolSet syms = scanner.getResults();
            // 扫码结果页
            for (Symbol sym : syms) {
                long end = System.currentTimeMillis();
                if (parsingCompleteListener != null) {
                    parsingCompleteListener.onComplete(sym.getData(), (end - start + 0f) / 1000 + "");
                }
            }
        }
    }

    public void setOnParsingCompleteListener(ParsingCompleteListener parsingCompleteListener) {
        this.parsingCompleteListener = parsingCompleteListener;
    }

    public void openFlash(boolean isOpen) {
        mCameraManager.openFlash(isOpen);
    }

    public void release() {
        mCameraManager.releaseCamera();
    }

    public void reset() {
        mCameraManager.reset();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        mMainHanlder.postDelayed(doAutoFocus, 1000);
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (isPreview) {
                mCameraManager.autoFocus();
            }
        }
    };

    @Override
    public void onChange(int status) {
        isPreview = status != 0;
    }


    public void setScanWidthAndHeight(int tailor) {
        mViewfinderView.setScanWidthAndHeight(tailor);
    }


    public void setScanRectColor(int scanRectColor) {
        mViewfinderView.setScanRectColor(scanRectColor);
    }

}
