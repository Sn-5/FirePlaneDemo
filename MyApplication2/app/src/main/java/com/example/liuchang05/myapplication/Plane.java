package com.example.liuchang05.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by CHAMPION on 2015/8/16.
 */
public class Plane {

    //�ɻ��ƶ��ٶ�
    static final int PLANE_SPEED = 10000;


    //�ɻ�����Ļ�е�����
    public int mPlanePosX = 0;
    public int mPlanePosY = 0;

    private Bitmap pBitmap = null;


    public Plane(Bitmap planeBitmap)
    {
        pBitmap = planeBitmap;
    }

    //�õ��ɻ�ͼƬ�ĸ߶�
    public int getPlaneBitHeight()
    {
        return pBitmap.getHeight();
    }

    //�õ��ɻ�ͼƬ�Ŀ��
    public int getPlaneBitWidth()
    {
        return pBitmap.getWidth();
    }

    //��ʼ���ɻ�����
    public void init(int x, int y)
    {
        mPlanePosX = x;
        mPlanePosY = y;
    }

    //���Ʒɻ�
    public void DrawPlane(Canvas canvas, Paint paint)
    {
        canvas.drawBitmap(pBitmap, mPlanePosX, mPlanePosY, paint);
    }

    //���·ɻ�λ��
    public void UpdatePlane(int x, int y)
    {
/*
        if (mPlanePosX < x) {
            mPlanePosX += PLANE_SPEED;
        } else {
            mPlanePosX -= PLANE_SPEED;
        }
        if (mPlanePosY < y) {
            mPlanePosY += PLANE_SPEED;
        } else {
            mPlanePosY -= PLANE_SPEED;
        }
*/
        if (Math.abs(mPlanePosX - x) <= PLANE_SPEED) {
            mPlanePosX = x;
        }
        if (Math.abs(mPlanePosY - y) <= PLANE_SPEED) {
            mPlanePosY = y;
        }
    }


}
