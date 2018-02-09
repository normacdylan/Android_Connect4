package com.august.connectfour;

/**
 * Created by mrx on 2017-12-04.
 */

public class BoardGraphic {

    private float left, top, right, bottom, xMargin, yMargin;
    private float[] holesX, holesY;

    public BoardGraphic(float left, float top, float right, float bottom, int rows, int columns) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        yMargin = (bottom-top)/(rows*2);
        xMargin = (right-left)/(columns*2);
        holesX = new float[columns];
        holesY = new float[rows];
        setHoles(rows, columns);
    }

    public float getYMargin() {
        return yMargin;
    }

    public float getXMargin() {
        return xMargin;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    private void setHoles(int rows, int columns) {
        float x = left+xMargin;
        float y = top+yMargin;

        for (int i=0;i<rows;i++) {
            holesY[i] = y;
            y += yMargin*2;
        }

        for (int j=0;j<columns;j++) {
            holesX[j] = x;
            x += xMargin*2;
        }
    }

    public float[] getHolesX() {return holesX;}
    public float[] getHolesY() {return holesY;}


}
