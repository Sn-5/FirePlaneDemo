package com.example.liuchang05.myapplication;

/**
 * Created by CHAMPION on 2015/8/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {

    /**�ӵ���Y���ٶ�**/
    static final int BULLET_STEP_Y = 35;


    /** �ӵ���XY���� **/
    public int m_posX = 0;
    public int m_posY = 0;

    //�ӵ�ͼƬ
    private Bitmap bBitmap = null;


    /**�Ƿ���»����ӵ�**/
    boolean mFacus = false;


    public Bullet(Bitmap bulletBitmap)
    {
        bBitmap = bulletBitmap;

    }

    /**��ʼ������**/
    public void init(int x, int y) {
        m_posX = x;
        m_posY = y;
        mFacus = true;
    }



    /**�����ӵ�**/
    public void DrawBullet(Canvas canvas, Paint paint) {
        if (mFacus) {
            canvas.drawBitmap(bBitmap,m_posX,m_posY,paint);
        }
    }

    /**�����ӵ��������**/
    public void UpdateBullet() {
        if (mFacus) {
            m_posY -= BULLET_STEP_Y;
        }

    }

}
