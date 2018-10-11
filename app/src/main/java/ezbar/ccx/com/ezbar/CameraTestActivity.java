/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 * Created by lisah0 on 2012-02-24
 */
package ezbar.ccx.com.ezbar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ezbar.ccx.com.ezbarlib.ParsingCompleteListener;
import ezbar.ccx.com.ezbarlib.ScannerView;


public class CameraTestActivity extends Activity {


    private ScannerView mScannerView;
    private boolean isOpen = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mScannerView = (ScannerView) findViewById(R.id.cameraPreview);
        mScannerView.setOnParsingCompleteListener(new ParsingCompleteListener() {
            @Override
            public void onComplete(String text, String handingTime) {
                Toast.makeText(CameraTestActivity.this, "检测到扫描结果 : " + text + " , 消耗时间 ：" + handingTime + " 秒", Toast.LENGTH_SHORT).show();
            }
        });
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
