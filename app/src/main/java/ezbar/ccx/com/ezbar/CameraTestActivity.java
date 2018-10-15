/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 * Created by lisah0 on 2012-02-24
 */
package ezbar.ccx.com.ezbar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ezbar.ccx.com.ezbarlib.ParsingCompleteListener;
import ezbar.ccx.com.ezbarlib.ScannerView;


public class CameraTestActivity extends Activity {


    private ScannerView mScannerView;
    private boolean isOpen = false;
    private ScannerView mScannerView1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        initScan();
    }

    private void initScan() {
        mScannerView = new ScannerView(this);
        ((ViewGroup) findViewById(R.id.cameraPreview)).addView(mScannerView);
        mScannerView.setOnParsingCompleteListener(new ParsingCompleteListener() {
            @Override
            public void onComplete(String text, String handingTime) {
                Toast.makeText(CameraTestActivity.this, "检测到扫描结果 : " + text + " , 消耗时间 ：" + handingTime + " 秒", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScan();
            }
        }
    }

    public void scan(View view) {
        mScannerView.reset();
    }


    @Override
    protected void onDestroy() {
        mScannerView.release();
        super.onDestroy();
    }

    public void open(View view) {
        isOpen = !isOpen;
        mScannerView.openFlash(isOpen);
    }
}
