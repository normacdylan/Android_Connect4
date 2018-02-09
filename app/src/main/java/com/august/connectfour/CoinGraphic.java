package com.august.connectfour;

/**
 * Created by mrx on 2017-12-04.
 */

public class CoinGraphic {

    private float x, y, radius;
    private int color;

    public CoinGraphic(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public int getColor() {
        return color;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getRadius() {
        return radius;
    }

    public void copyCoin(CoinGraphic coin) {
        this.x = coin.getX();
        this.y = coin.getY();
        this.radius = coin.getRadius();
        this.color = coin.getColor();
    }

    public void updateX(float x) {
        this.x = x;
    }

    public void fall(float fall) {
        y += fall;
    }

}
