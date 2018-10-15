# EZbar

Zbar二维码扫码器,贼简单

<img src="/gif/img.gif" width="320px" />

### 如何使用
[ ![Download](https://api.bintray.com/packages/ci250454344/EZbar/EZbar/images/download.svg) ](https://bintray.com/ci250454344/EZbar/EZbar/_latestVersion)


在工程gralde中加入

      implementation 'com.ccx1:EZbar:1.0.3'


在project的gradle中的allprojects中加入


        allprojects {
            repositories {
                google()
                jcenter()
                maven {url 'https://dl.bintray.com/ci250454344/EZbar/'}
            }
        }


在布局中添加ScannerView


         <ezbar.ccx.com.ezbarlib.ScannerView
                android:id="@+id/cameraPreview"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

java代码

        mScannerView = (ScannerView) findViewById(R.id.cameraPreview);
        mScannerView.setOnParsingCompleteListener(new ParsingCompleteListener() {
            @Override
            public void onComplete(String text, String handingTime) {
                Toast.makeText(CameraTestActivity.this, "检测到扫描结果 : " + text + " , 消耗时间 ：" + handingTime + " 秒", Toast.LENGTH_SHORT).show();
            }
        });

onComplete是扫码完成的回调

提供了一个设置扫码框大小的方法

        mScannerView.setScanWidthAndHeight(200);


提供了一个设置扫码框颜色的方法

        mScannerView.setScanRectColor(Color.RED);

提供了一个扫码结束之后重置的方法，非常关键

        mScannerView.reset();

在退出activity的时候要进行

        @Override
        protected void onDestroy() {
            mScannerView.release();
            super.onDestroy();
        }


#### 打开闪光灯。

跟我写的zxing一样,true是打开，false是关闭

        mScannerView.openFlash(isOpen);

