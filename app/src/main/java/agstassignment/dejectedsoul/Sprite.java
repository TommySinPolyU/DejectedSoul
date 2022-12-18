package agstassignment.dejectedsoul;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
    private float x, y;
    private int spriteWidth, spriteHeight;
    private Bitmap bitmap;
    private int vSpeed, hSpeed;
    private boolean bounce, dragable,touched;
    private Paint paint;
    private Bitmap[] bitmaps[];

    public Sprite(){

    }

    // Constructor for create a sprite from a single image
    public Sprite(Bitmap bitmap, float x, float y) {
        this.bitmap= bitmap;
        this.spriteWidth= bitmap.getWidth();
        this.spriteHeight= bitmap.getHeight();
        this.x= x;
        this.y= y;
        this.vSpeed= 0;
        this.hSpeed= 0;
        this.bounce= false;
        this.dragable= false;
        paint = new Paint();
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getSpriteWidth() { return spriteWidth; }
    public int getSpriteHeight() { return spriteHeight; }
    public int getHSpeed() { return hSpeed; }
    public int getVSpeed() { return vSpeed; }
    public void setX(float x){ this.x= x; }
    public void setY(float y){ this.y= y; }
    public void setHSpeed(int hSpeed) { this.hSpeed= hSpeed; }
    public void setVSpeed(int vSpeed) { this.vSpeed= vSpeed; }

    public void update() {
        if(!touched) {
            x += hSpeed;
            y += vSpeed;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x-spriteWidth/2,
                y-spriteHeight/2, paint);
    }


    public void setTouched(boolean touched) { this.touched= touched; }
    public boolean isTouched() { return touched; }

    public void handleActionDown(int eventX, int eventY){
        if (eventX>= (x -spriteWidth/2) &&
                (eventX<= (x + spriteWidth/2))) {
            if (eventY>= (y -spriteHeight/2) &&
                    (eventY<= (y + spriteHeight/2))){
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    public void handleBounce(int left, int top, int right, int bottom) {
        if(bounce) {
            if(x < left + spriteWidth/2 || x >
                    right -spriteWidth/2) {
                //hSpeed*= -1;
            }
            if(y < top + spriteHeight/2 || y >
                    bottom -spriteHeight/2) {
                //vSpeed*= -1;
            }
        }
    }

    public boolean collideWith(Sprite other) {
        if((Math.abs(this.x-other.x) <
                this.spriteWidth/2 + other.spriteWidth/2)
                && (Math.abs(this.y-other.y) <
                this.spriteHeight/2 + other.spriteHeight/2))
            return true;
        return false;
    }


}
