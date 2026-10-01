package io.github.simuladorDeTrafego;

class Explosion {
    float x, y;
    long expireTime;
    public Explosion(float x, float y) {
        this.x = x;
        this.y = y;
        this.expireTime = System.currentTimeMillis() + 1000;
    }
}