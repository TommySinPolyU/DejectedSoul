package agstassignment.dejectedsoul;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Bullet {

    Bullet[] bulletData;
    public int x, y;
    public int spriteWidth, spriteHeight;
    private Bitmap d_bitmap[], l_bitmap[], r_bitmap[], u_bitmap[],tl_bitmap[], tr_bitmap[],dl_bitmap[], dr_bitmap[];
    private Paint paint;
    private boolean dragable;
    private boolean touched;
    private boolean bounce;
    private boolean animated;
    private double vSpeed, hSpeed;
    private int curFrame;
    private int curFrameTick;
    private int noOfFrame;
    private int noOfTickPerFrame;
    private String direction; // d = down(default), l = left, r = right, u = up
    private Random rand;
    int Damage;
    double speed;
    public boolean isDrawed;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public double getHSpeed() {
        return hSpeed;
    }

    public double getVSpeed() {
        return vSpeed;
    }

    public boolean getDragable() {
        return dragable;
    }

    public boolean getBounce() {
        return bounce;
    }

    public boolean getAnimated() {
        return animated;
    }

    public boolean isTouched() {
        return touched;
    }

    public String getDirection() {
        return direction;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHSpeed(double hSpeed) {
        this.hSpeed = hSpeed;
    }

    public void setVSpeed(double vSpeed) {
        this.vSpeed = vSpeed;
    }

    public void setDragable(boolean dragable) {
        this.dragable = dragable;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setBounce(boolean bounce) {
        this.bounce = bounce;
    }

    public void setAniamated(boolean animated) {
        this.animated = animated;
    }

    //  create the bullet data for enemy and player's character bullet equipment.
    public Bullet(GameBackStageUI backStageUI){
        bulletData= new Bullet[4];
        bulletData[0] = new Bullet(BitmapFactory.decodeResource(backStageUI.getResources(),R.drawable.bullet_normal),8,8,5,0,0,0,0);
        bulletData[1] = new Bullet(BitmapFactory.decodeResource(backStageUI.getResources(),R.drawable.bullet_lightblue),8,8,5,0,0,0,0);
        bulletData[2] = new Bullet(BitmapFactory.decodeResource(backStageUI.getResources(),R.drawable.bullet_lightgreen),8,8,5,0,0,0,0);
        bulletData[3] = new Bullet(BitmapFactory.decodeResource(backStageUI.getResources(),R.drawable.bullet_black),8,8,5,0,0,0,0);
    }

    public Bullet(int x, int y, int Hspeed, int Vspeed){
        this.x = x;
        this.y = y;
        this.vSpeed = Vspeed;
        this.hSpeed = Hspeed;
        this.bounce = false;
        this.animated = true;
        this.dragable = false;
    }

    public Bullet(Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame, int x, int y, int Hspeed, int Vspeed) {
        this.d_bitmap = new Bitmap[noOfFrame];
        this.l_bitmap = new Bitmap[noOfFrame];
        this.r_bitmap = new Bitmap[noOfFrame];
        this.u_bitmap = new Bitmap[noOfFrame];
        this.spriteWidth = bitmap.getWidth() / noOfFrame;
        this.spriteHeight = bitmap.getHeight() / height;
        this.curFrame = 0;
        this.curFrameTick = 0;
        this.noOfFrame = noOfFrame;
        this.noOfTickPerFrame = noOfTickPerFrame;
        this.direction = "d";
        for (int i = 0; i < noOfFrame; i++) {
            d_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 6 * spriteHeight, spriteWidth, spriteHeight);
            l_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight);
            r_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 4 * spriteHeight, spriteWidth, spriteHeight);
            u_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight);
        }
        this.x = x;
        this.y = y;
        this.vSpeed = Vspeed;
        this.hSpeed = Hspeed;
        this.bounce = false;
        this.animated = true;
        this.dragable = false;
        paint = new Paint();
        bitmap.recycle();
    }

    public void setBitmap(Bullet bulletData){
        this.spriteWidth = bulletData.spriteWidth / 8;
        this.spriteHeight = bulletData.spriteHeight / 8;
        this.d_bitmap = bulletData.d_bitmap;
        this.l_bitmap = bulletData.l_bitmap;
        this.r_bitmap = bulletData.r_bitmap;
        this.u_bitmap = bulletData.u_bitmap;
    }

    public void setDamage(int damage) {
        Damage = damage;
    }

    public void move(int move_x, int move_y) {
        this.x += move_x;
        this.y += move_y;
    }
    public void handleBounce(int left, int top, int right, int bottom) {
        if(bounce) {
            if(x < left + spriteWidth/8 || x > right - spriteWidth/8) {
                if(x < left + spriteWidth/8){
                    this.x += spriteWidth/12;
                }
                else if(x > right - spriteWidth/8){
                    this.x -= spriteWidth/12;
                }
                //hSpeed *= 0;
            }
            if(y < top + spriteHeight/2 || y > bottom - spriteHeight/2) {
                if(y < top + spriteHeight/2){
                    this.y += spriteHeight/12;
                }
                else if(y > bottom - spriteHeight/2){
                    this.y -= spriteHeight/12;
                }
                //vSpeed *= 0;
            }
        }
    }
    private int nextFrame() {
        if(++curFrameTick > noOfTickPerFrame){
            curFrameTick = 0;
            curFrame++;
        }
        return (curFrame >= noOfFrame) ? curFrame = 0:curFrame;
    }
    public void draw(Canvas canvas) {
        Bitmap bitmap[] = null;
        if(Math.abs(hSpeed) < Math.abs(vSpeed)) {
            if (vSpeed > 0)
                direction = "d";
            else if(vSpeed < 0)
                direction = "u";
        }
        else if(hSpeed > 0)
            direction = "r";
        else if (hSpeed < 0) direction = "l";
        else direction = "u";
        switch(direction) {
            case "d": bitmap = d_bitmap; break;
            case "l": bitmap = l_bitmap; break;
            case "r": bitmap = r_bitmap; break;
            case "u": bitmap = u_bitmap; break;
        }
        if(hSpeed == 0 && vSpeed == 0){
            canvas.drawBitmap(bitmap[curFrame],x - spriteWidth/2, y - spriteHeight / 2, paint);
        }
        else
            canvas.drawBitmap(bitmap[nextFrame()],x - spriteWidth/2, y - spriteHeight / 2, paint);
    }
    public void update() {
        if(!touched) {
            x += hSpeed;
            y += vSpeed;
        }

    }

}