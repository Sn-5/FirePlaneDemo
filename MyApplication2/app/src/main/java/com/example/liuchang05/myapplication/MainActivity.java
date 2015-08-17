package com.example.liuchang05.myapplication;


import android.graphics.Color;
import android.os.Bundle;

import java.io.InputStream;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.Window;
import android.view.WindowManager;
;


public class MainActivity extends Activity {

    PlaneView mPlaneView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ȫ����ʾ����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ��ȡ��Ļ���
        Display display = getWindowManager().getDefaultDisplay();

        // ��ʾ�Զ������ϷView
        mPlaneView = new PlaneView(this, display.getWidth(), display.getHeight());
        setContentView(mPlaneView);
    }
    public boolean onTouchEvent(MotionEvent event) {
        // ��ô���������
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            // ������Ļʱ��
            case MotionEvent.ACTION_DOWN:
                mPlaneView.UpdateTouchEvent(x, y,true);
                break;
            // �������ƶ�ʱ��
            case MotionEvent.ACTION_MOVE:
                mPlaneView.UpdateTouchEvent(x, y,true);
                break;
            // ��ֹ����ʱ��
            case MotionEvent.ACTION_UP:
                mPlaneView.UpdateTouchEvent(x, y,false);
                break;
        }
        return false;
    }

    public class PlaneView extends SurfaceView implements Callback, Runnable {
        /**
         * ��Ļ�Ŀ��*
         */
        private int mScreenWidth = 0;
        private int mScreenHeight = 0;

        Paint mPaint = null;



        //�ӵ����������
        final static int BULLET_COUNT = 10;


        //ÿ��1500���뷢��һ���ӵ�
        final static int FIRE_TIME = 1300;



        /**
         * ��Ϸ���߳�*
         */
        private Thread mThread = null;
        /**
         * �߳�ѭ����־*
         */
        private boolean mIsRunning = false;

        private SurfaceHolder mSurfaceHolder = null;
        private Canvas mCanvas = null;

        private Context mContext = null;


        Plane mPlane =null;

        //�ɻ�ͼƬ�ĸ߶�
        private int mPlaneHeight = 0;
        //�ɻ�ͼƬ�Ŀ��
        private int mPlaneWidth = 0;


        /**
         * �ӵ���*
         */
        Bullet mBullet[] = null;
        Bitmap mBitbullet[] = null;

        //�ӵ�ͼƬ�߶�
        private int mBulletBitHeight = 0;
        //�ӵ�ͼƬ���
        private int mBulletBitWidth = 0;


        /**
         * ��ʼ�������ӵ�ID����*
         */
        public int mSendId = 0;

        /**
         * ��һ���ӵ������ʱ��*
         */
        public Long mSendTime = 0L;


        /**
         * ��ָ����Ļ����������*
         */
        public int mTouchPosX = 0;
        public int mTouchPosY = 0;


        /**
         * ��־��ָ����Ļ������*
         */
        public boolean mTouching = false;


        public PlaneView(Context context, int screenWidth, int screenHeight) {
            super(context);
            mContext = context;

            mPaint = new Paint();
            mPaint.setColor(Color.WHITE);

            mScreenWidth = screenWidth;
            mScreenHeight = screenHeight;

            //��ȡmSurfaceHolder
            mSurfaceHolder = getHolder();
            mSurfaceHolder.addCallback(this);
            setFocusable(true);

            init();

        }

        //��ʼ����Ϸ��Դ
        private void init() {

            //������������
            mPlane = new Plane(ReadBitmap(mContext, R.drawable.plane));

            mPlaneHeight = mPlane.getPlaneBitHeight();
            mPlaneWidth = mPlane.getPlaneBitWidth();

            //��ʼ����������
            mPlane.init(mScreenWidth/2, mScreenHeight - mPlaneHeight);


            //�����ӵ�����
            mBullet = new Bullet[BULLET_COUNT];
            mBitbullet = new Bitmap[BULLET_COUNT];

            for (int i = 0; i < BULLET_COUNT; i++) {
                mBitbullet[i] = ReadBitmap(mContext, R.drawable.bullet);

                mBulletBitHeight = mBitbullet[0].getHeight();
                mBulletBitWidth = mBitbullet[0].getWidth();
            }

            for (int i = 0; i < BULLET_COUNT; i++) {
                mBullet[i] = new Bullet(mBitbullet[i]);
            }

            mSendTime = System.currentTimeMillis();



        }

        //��ʼ������
        public void InitDraw() {
            /**���Ʒɻ�����**/
            mPlane.DrawPlane(mCanvas, mPaint);

            /**�����ӵ�����*/
            for (int i = 0; i < BULLET_COUNT; i++) {
                mBullet[i].DrawBullet(mCanvas, mPaint);
            }

        }

        //���»�ͼ(ֻ�Ǹ�������)
        public void UpdateDraw()
        {
            /** ��ָ������Ļ���·ɻ����� **/
            if (mTouching)
            {
                mPlane.UpdatePlane(mTouchPosX, mTouchPosY);
            }
            /** �����ӵ����� **/
            for (int i = 0; i < BULLET_COUNT; i++) {
                /** �ӵ����������¸�ֵ**/
                mBullet[i].UpdateBullet();

            }


            /**����ʱ���ʼ��Ϊ������ӵ�**/
            if (mSendId < BULLET_COUNT) {
                long now = System.currentTimeMillis();
                if (now - mSendTime >= FIRE_TIME) {
                    // mBullet[mSendId].init(mPlane.mPlanePosX - BULLET_LEFT_OFFSET, mPlane.mPlanePosY - BULLET_UP_OFFSET);
                    mBullet[mSendId].init(mPlane.mPlanePosX + mPlaneWidth/2 - mBulletBitWidth/2 , mPlane.mPlanePosY - mBulletBitHeight);

                    mSendTime = now;
                    mSendId++;
                }
            } else {
                mSendId = 0;
            }
        }


        protected void Draw() {

            InitDraw();

            UpdateDraw();


        }

        public void UpdateTouchEvent(int x, int y, boolean touching) {
            // �������ⰴť���²��Ų�ͬ����Ч
            mTouching = touching;
            mTouchPosX = x - mPlaneWidth/2;
            mTouchPosY = y - mPlaneHeight/2;
        }

        public Bitmap ReadBitmap(Context context, int resId) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            // ��ȡ��ԴͼƬ
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, opt);
        }



        @Override
        public void run() {
            while (mIsRunning) {
                //����������̰߳�ȫ��
                synchronized (mSurfaceHolder) {
                    /**�õ���ǰ���� Ȼ������**/
                    mCanvas = mSurfaceHolder.lockCanvas();
                    mCanvas.drawColor(Color.BLACK);
                    Draw();
                    /**���ƽ����������ʾ����Ļ��**/
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                                   int arg3) {
            // surfaceView�Ĵ�С�����ı��ʱ��

        }

        @Override
        public void surfaceCreated(SurfaceHolder arg0) {
            /**������Ϸ���߳�**/
            mIsRunning = true;
            mThread = new Thread(this);
            mThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            // surfaceView���ٵ�ʱ��
            mIsRunning = false;
        }

    }
}


















