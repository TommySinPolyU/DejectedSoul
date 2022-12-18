package agstassignment.dejectedsoul;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Enemy extends Character {
    private EnemyBulletEquipmentContainer enemyBulletEquipmentContainer = new EnemyBulletEquipmentContainer();
    private Random random = new Random();
    boolean isRewarded, isCollideWithCharacter = false;
    public int collideTime = 0;
    private double randomlyEventChance;
    int MoneyReward, ExpReward;


    public Enemy(String Name, Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame, boolean isBoss, boolean isUndead, boolean isFinalBoss){
        super(bitmap, noOfFrame, height, noOfTickPerFrame);
        this.isBoss = isBoss;
        this.isUnDead = isUndead;
        this.isFinalBoss = isFinalBoss;
        isPlayer = false;
        this.setName(Name);
    }

    public Enemy(Character enemy, double RandomEventChance, int lv, int maxHp, int maxMp, int minDamage, int maxDamage , int hpRecoveryValue, int mpRecoveryValue, double atkSpeed, double moveSpeed, double bulletSpeed, double hpRecoverySpeed, double mpRecoverySpeed, BulletEquipment bulletEquipment){
        super(enemy, lv, maxHp, maxMp, minDamage, maxDamage , hpRecoveryValue, mpRecoveryValue, atkSpeed, moveSpeed, bulletSpeed, hpRecoverySpeed, mpRecoverySpeed);
        isPlayer = false;
        isBoss = enemy.isBoss;
        isUnDead = enemy.isUnDead;
        isFinalBoss = enemy.isFinalBoss;
        if(!isBoss)
            setName(enemy.getName() + " Lv." + getLv());
        else setName(enemy.getName());
        enemyBulletEquipmentContainer.setEquippedBullet(this, bulletEquipment);
        this.randomlyEventChance = RandomEventChance;
    }

    public Enemy(Character enemy, double RandomEventChance, boolean isBoss,boolean isUnDead, boolean isFinalBoss, int x, int y, int Hspeed, int Vspeed, int lv, int maxHp, int maxMp, int minDamage, int maxDamage , int hpRecoveryValue, int mpRecoveryValue, double atkSpeed, double moveSpeed, double bulletSpeed, double hpRecoverySpeed, double mpRecoverySpeed, int moneyReward, int expReward, BulletEquipment bulletEquipment){
        super(enemy, lv, maxHp, maxMp, minDamage, maxDamage , hpRecoveryValue, mpRecoveryValue, atkSpeed, moveSpeed, bulletSpeed, hpRecoverySpeed, mpRecoverySpeed);
        isPlayer = false;
        this.isBoss = isBoss;
        this.isUnDead = isUnDead;
        this.isFinalBoss = isFinalBoss;
        this.setX(x);
        this.setY(y);
        this.setHSpeed(Hspeed);
        this.setVSpeed(Vspeed);
        if(!isBoss)
            setName(enemy.getName() + "\nLv." + getLv());
        else setName(enemy.getName());
        setMoneyReward(moneyReward);
        setExpReward(expReward);
        enemyBulletEquipmentContainer.setEquippedBullet(this, bulletEquipment);
        this.randomlyEventChance = RandomEventChance;
    }



    public int getMoneyReward(){return MoneyReward;}

    public int getExpReward(){return ExpReward;}

    public boolean getCollideWithCharacter(){return isCollideWithCharacter;}

    public boolean isBulletReady(){return isBulletReady;}

    public boolean isRewarded(){return isRewarded;}

    public void setBulletReady(boolean bool){this.isBulletReady = bool;}

    public void setRewarded(boolean bool){this.isRewarded = bool;}

    public void setMoneyReward(int Value){this.MoneyReward = Value;}

    public void setExpReward(int Value){this.ExpReward = Value;}

    public void setCollideWithCharacter(boolean bool){this.isCollideWithCharacter = bool;}

    @Override
    public void handleBounce(int left, int top, int right, int bottom) {
        if(getBounce()) {
            if(getX() < left + getSpriteWidth() / 6 || getX() > right - getSpriteWidth()/6) {
                if (random.nextDouble() >= randomlyEventChance) {
                    //Log.e("Add Speed", randomlyEventChance + " / " + getVSpeed() + " / " + getHurtTime);
                    if(getHSpeed() < 0){
                        //Log.e("Before", "" + getHSpeed());
                        setX(getX() + getMoveSpeed());
                        setHSpeed(getMoveSpeed() * ((0.75 + random.nextDouble()) * (1 + random.nextInt(1))));
                        //Log.e("After", "" + getHSpeed());
                    } else {
                        setX(getX() - getMoveSpeed());
                        setHSpeed(-(getMoveSpeed() * ((0.75 + random.nextDouble()) * (1 + random.nextInt(1)))));
                    }
                    randomlyEventChance = random.nextDouble();
                } else {
                    //Log.e("Before", "" + getHSpeed());
                    setHSpeed(getHSpeed() * -1);
                    //Log.e("After", "" + getHSpeed());
                    randomlyEventChance = random.nextDouble();
                }
            }
            if(getVSpeed() > 0 || getVSpeed() < 0) {
                if (getY() < top + getSpriteHeight() / 6 || getY() > bottom - getSpriteHeight() / 6) {
                    if (getY() < top + getSpriteHeight() / 6) {
                        setY(bottom - getSpriteHeight() / 2);
                    } else {
                        setY(0 + getSpriteHeight());
                    }
                    getFiredBullets().removeAll(getFiredBullets());
                    if (!isBoss) {
                        setVSpeed(0);
                    }
                    if (isUnDead && !isBoss) {
                        setVSpeed(0 + getMoveSpeed() * (0.1 + random.nextDouble()));
                    }
                }
            }
        }
    }

    @Override
    public boolean collideWith(Bullet other) {
        Rect BulletRect = new Rect((int)(other.x + other.getSpriteWidth()*2.5), other.y + other.getSpriteHeight(), (int)(other.x + other.getSpriteWidth() * 4.5), (int)(other.y + other.getSpriteHeight() * 2.5f));
        Rect CharRect = new Rect((int)(getX() - getSpriteWidth() * 0.4),(int)(getY() - getSpriteHeight() * 0.45), (int)(getX() + getSpriteWidth() * 0.4), (int)(getY() + getSpriteHeight() * 0.5));

        if(BulletRect.intersect(CharRect)){
            BulletRect = null;
            CharRect = null;
                return true;
        }else {
            BulletRect = null;
            CharRect = null;
            return false;
        }

        /*
        if(((Math.abs(this.x - other.x) <= this.spriteWidth + other.spriteWidth) &&
                (Math.abs(this.y - other.y) <= this.spriteHeight + other.spriteHeight)))  {
            Log.e("Bullet Touch", "True");
            return true;
        }
        return false;
        */
    }

    @Override
    public void fire(Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame){
        Bullet shootingBullet = new Bullet(0,0,0,-1);
        shootingBullet.setBitmap(bulletData);
        shootingBullet.setDamage(getRandomDamage());
        shootingBullet.setBounce(true);
        shootingBullet.setX((int)getX() - getSpriteWidth() / 4); shootingBullet.setY((int)getY() - shootingBullet.getSpriteHeight());
        shootingBullet.setVSpeed(getBulletSpeed());
        getFiredBullets().add(shootingBullet);
        adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Minus, 1);
        //bullet.launch(direction); //this should be calculated by which direction the player is facing.
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint1 = new Paint();
            canvas.drawBitmap(this.singleBitmap,(int)(getX() - getSpriteWidth()/2),(int)( getY() - getSpriteHeight() / 2), paint1);

        if(isGetHurt){
            paint1.setColor(Color.RED);
            paint1.setTextSize(getSpriteWidth()/4);
            canvas.drawText(getGotDamages().get(0).toString(), (float) getX(), (float) getY(), paint1);
            if(getHurtTime < 1) {
                getHurtTime++;

                if(!isBoss) {
                    if (random.nextDouble() >= randomlyEventChance) {
                        //Log.e("Add Speed", randomlyEventChance + " / " + getVSpeed() + " / " + getHurtTime);
                        setVSpeed(getVSpeed() + 1);
                        randomlyEventChance = random.nextDouble();
                    } else {
                        randomlyEventChance = random.nextDouble();
                    }
                }
            }
        }else {
            paint1.setColor(Color.WHITE);
        }
        if(getPoisonTask() != null) {
            if (getPoisonTask().Value > 0 && getPoisonTask().isRunning) {
                paint1.setColor(Color.GREEN);
                paint1.setTextSize(getSpriteWidth() / 8);
                canvas.drawText("Poison -" + getPoisonTask().Value, (float) getX(), (float) getY() - getSpriteHeight() / 4, paint1);
            }
        }
    }
}
