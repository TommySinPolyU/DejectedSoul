package agstassignment.dejectedsoul;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Character {
    GameBackStageUI gameScreen;
    public boolean isPlayer = true, isBulletReady, isUnDead, isBoss, isFinalBoss;
    BulletEquipment equippedBullet;
    private String name;
    public int getHurtTime = 0;
    public RecoveryTask hpRecoveryTask ,mpRecoveryTask;
    public colorChange colorChangeTask;
    private int Lv, Exp, ExpRequirement;
    private List<Bullet> firedBullets = new ArrayList<Bullet>();
    private List<Integer> gotDamages = new ArrayList<Integer>();
    Bullet bulletData;
    Timer coilldewithOtherTimer;


    private FireTask fireTask;
    private Timer FireTimer, PoisonTimer;
    private PoisonTask poisonTask;
    boolean isDead, isGetHurt;
    public GamePanel GP;
    public enum CalculationMethod{
        Plus,
        Minus,
        Multiply,
        Division,
        Equals
    }
    public enum AdjustType{
        MaxHP,
        CurrentHP,
        MaxMP,
        CurrentMP,
        MinDam,
        MaxDam,
        AtkSpeed,
        BulletSpeed,
        MoveSpeed,
        HPRecoveryValue,
        MPRecoveryValue,
        HPRecoverySpeed,
        MPRecoverySpeed
    }
    /*
    Sprite Elements
     */
    private double x, y;
    private int spriteWidth, spriteHeight;
    private Bitmap d_bitmap[], l_bitmap[], r_bitmap[], u_bitmap[];
    public Bitmap singleBitmap;
    public Paint paint;
    private boolean dragable;
    private boolean touched;
    private boolean bounce;
    private boolean animated;
    private int curFrame;
    private int curFrameTick;
    private int noOfFrame;
    private int noOfTickPerFrame;
    private char direction; // d = down(default), l = left, r = right, u = up
    // Sprite Element Declaration End

    /*
    Character Attributes
     */
    private int baseMaxHP, baseMaxMP, baseHPRecoveryValue, baseMPRecoveryValue;
    private int CurrentHp, MaxHp, CurrentMp, MaxMp, hpRecoveryValue, mpRecoveryValue;
    private int MinDamage,Maxdamage;
    private double atkSpeed, bulletSpeed, hpRecoverySpeed, mpRecoverySpeed;
    private double vSpeed, hSpeed;
    private double MoveSpeed, baseMoveSpeed;
    //  Character Attributes Declaration End

    public Character(Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame){
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
        if(height == 1 && noOfFrame == 1) {
            singleBitmap = Bitmap.createBitmap(bitmap, 0, 0, spriteWidth, spriteHeight);
        }
        else {
            for (int i = 0; i < noOfFrame; i++) {
                d_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight);
                l_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 1 * spriteHeight, spriteWidth, spriteHeight);
                r_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight);
                u_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 3 * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        this.isDead = false;
        //bitmap.recycle();
    }

    public Character(Character character, int lv, int maxHp, int maxMp, int minDamage, int maxDamage , int hpRecoveryValue, int mpRecoveryValue, double atkSpeed, double moveSpeed, double bulletSpeed, double hpRecoverySpeed, double mpRecoverySpeed){
        this.baseMaxHP = maxHp;
        this.baseMaxMP = maxMp;
        this.baseHPRecoveryValue = hpRecoveryValue;
        this.baseMPRecoveryValue = mpRecoveryValue;
        this.baseMoveSpeed = moveSpeed;
        this.Lv = lv;
        this.CurrentHp = maxHp;
        this.CurrentMp = maxMp;
        this.MinDamage = minDamage;
        this.Maxdamage = maxDamage;
        this.atkSpeed = atkSpeed;
        this.bulletSpeed = bulletSpeed;
        this.mpRecoverySpeed = mpRecoverySpeed;
        this.hpRecoverySpeed = hpRecoverySpeed;
        this.d_bitmap = character.d_bitmap;
        this.l_bitmap = character.l_bitmap;
        this.r_bitmap = character.r_bitmap;
        this.u_bitmap = character.u_bitmap;
        this.spriteWidth = character.spriteWidth;
        this.spriteHeight = character.spriteHeight;
        this.MoveSpeed = this.baseMoveSpeed;
        this.MaxHp = this.baseMaxHP;
        this.MaxMp = this.baseMaxMP;
        this.hpRecoveryValue = baseHPRecoveryValue;
        this.mpRecoveryValue = baseMPRecoveryValue;
        this.curFrame = 0;
        this.curFrameTick = 0;
        this.noOfFrame = character.noOfFrame;
        this.noOfTickPerFrame = character.noOfTickPerFrame;
        if(character.noOfFrame == 1) {
            singleBitmap = Bitmap.createBitmap(character.singleBitmap, 0, 0, spriteWidth, spriteHeight);
        }
        this.isDead = false;

    }

    public Character(Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame, double x, double y,int lv, int maxHp, int maxMp, int minDamage, int maxDamage , int hpRecoveryValue, int mpRecoveryValue, double atkSpeed, double moveSpeed, double bulletSpeed, double hpRecoverySpeed, double mpRecoverySpeed) {
        this.baseMaxHP = maxHp;
        this.baseMaxMP = maxMp;
        this.baseHPRecoveryValue = hpRecoveryValue;
        this.baseMPRecoveryValue = mpRecoveryValue;
        this.baseMoveSpeed = moveSpeed;
        this.MaxHp = maxHp;
        this.MaxMp = maxMp;
        this.Lv = lv;
        this.CurrentHp = maxHp;
        this.CurrentMp = maxMp;
        this.MinDamage = minDamage;
        this.Maxdamage = maxDamage;
        this.atkSpeed = atkSpeed;
        this.bulletSpeed = bulletSpeed;
        this.MoveSpeed = moveSpeed;
        this.mpRecoverySpeed = mpRecoverySpeed;
        this.mpRecoveryValue = mpRecoveryValue;
        this.hpRecoverySpeed = hpRecoverySpeed;
        this.hpRecoveryValue = hpRecoveryValue;
        this.MoveSpeed = this.baseMoveSpeed;
        this.MaxHp = this.baseMaxHP;
        this.MaxMp = this.baseMaxMP;
        this.hpRecoveryValue = baseHPRecoveryValue;
        this.mpRecoveryValue = baseMPRecoveryValue;
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
        this.direction = 'd';
        if(height == 1 && noOfFrame == 1){
            singleBitmap = Bitmap.createBitmap(bitmap,0,0,spriteWidth,spriteHeight);
        }else {
            for (int i = 0; i < noOfFrame; i++) {
                d_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight);
                l_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 1 * spriteHeight, spriteWidth, spriteHeight);
                r_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight);
                u_bitmap[i] = Bitmap.createBitmap(bitmap, i * spriteWidth, 3 * spriteHeight, spriteWidth, spriteHeight);
            }
        }
        this.x = x;
        this.y = y;
        this.vSpeed = 0;
        this.hSpeed = 0;
        this.bounce = false;
        this.animated = true;
        this.dragable = false;
        paint = new Paint();
        this.isDead = false;
        //bitmap.recycle();
    }

    // Clear all bitmap to release the memory;
    public void RecycleBitmap(){
        for(int i = 0; i < d_bitmap.length;i++){
            d_bitmap[i].recycle();
        }
        for(int i = 0; i < l_bitmap.length;i++){
            l_bitmap[i].recycle();
        }
        for(int i = 0; i < u_bitmap.length;i++){
            u_bitmap[i].recycle();
        }
        for(int i = 0; i < r_bitmap.length;i++){
            r_bitmap[i].recycle();
        }
    }

    // Getter Setting

    public BulletEquipment getEquippedBullet() {
        if(equippedBullet != null)
            return equippedBullet;
        else
            return null;
    }

    public List<Bullet> getFiredBullets(){return firedBullets;};
    public int getMinDamage(){return  MinDamage;}
    public int getMaxdamage(){return Maxdamage;}
    public int getCurrentHp(){
        return CurrentHp;
    }
    public int getHpRecoveryValue() {return hpRecoveryValue;}
    public int getMpRecoveryValue() {return  mpRecoveryValue;}

    public Timer getPoisonTimer() {
        return PoisonTimer;
    }

    public PoisonTask getPoisonTask() {
        return poisonTask;
    }

    public Timer getFireTimer(){return FireTimer;}
    public FireTask getFireTask(){return fireTask;}

    public int getMaxHp(){
        return MaxHp;
    }

    public int getCurrentMp(){
        return CurrentMp;
    }

    public int getMaxMp(){
        return MaxMp;
    }

    public double getAtkSpeed(){
        return atkSpeed;
    }

    public double getBulletSpeed(){
        return bulletSpeed;
    }
    public double getMoveSpeed(){return MoveSpeed;}
    public double getMpRecoverySpeed(){return mpRecoverySpeed;}
    public double getHpRecoverySpeed(){return hpRecoverySpeed;}
    public boolean isDead(){
        return isDead;
    }

    public int getRandomDamage(){
        Random rand = new Random();
        int randNum;
        randNum = rand.nextInt((this.Maxdamage - this.MinDamage) + 1 ) + (this.MinDamage);
        return randNum;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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

    public char getDirection() {
        return direction;
    }

    public List<Integer> getGotDamages(){
        return gotDamages;
    }

    public int getLv(){return Lv;}
    public int getExp(){return Exp;}
    public int getExpRequirement(){return ExpRequirement;}

    public String getName() {
        return name;
    }

    public void setLv(int lv){
        this.Lv = lv;
    }

    public void setExpRequirement(int constantValue, double lvCoefficient){
        if(getLv() > 0 && getLv() < 10)
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * lvCoefficient * constantValue)));
        else if(getLv() >= 10 && getLv() < 25)
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * (lvCoefficient * 2) * constantValue)));
        else if(getLv() >= 25 && getLv() < 40)
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * (lvCoefficient * 3) * constantValue)));
        else if(getLv() >= 40 && getLv() < 55)
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * (lvCoefficient * 4 ) * constantValue)));
        else if(getLv() >= 55 && getLv() < 70)
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * (lvCoefficient * 5 ) * constantValue)));
        else
            this.ExpRequirement =  Math.round((float)(constantValue + ((this.Lv - 1) * (lvCoefficient * 6) * constantValue)));
    }

    // Set the equirment of bullet and adjust the character value to the equipment value.

    // Getter Setting End...

    // Setter Setting
    public void adjustValueInt(AdjustType adjustType,CalculationMethod calculationMethod, int adjustValue){
        int Value = 0;
        switch (adjustType){
            case MaxHP:
                Value = this.MaxHp;
                break;
            case CurrentHP:
                Value = this.CurrentHp;
                break;
            case MaxMP:
                Value = this.MaxMp;
                Log.e("Before Value" , "" + Value);
                break;
            case CurrentMP:
                Value = this.CurrentMp;
                break;
            case MinDam:
                Value = this.MinDamage;
                break;
            case MaxDam:
                Value = this.Maxdamage;
                break;
            case HPRecoveryValue:
                Value = this.hpRecoveryValue;
                break;
            case MPRecoveryValue:
                Value = this.mpRecoveryValue;
                break;
        }
        //Log.e("Before Value" , "" + Value);
        switch (calculationMethod){
            case Plus:
                Value += adjustValue;
                break;
            case Minus:
                Value -= adjustValue;
                break;
            case Multiply:
                Value *= adjustValue;
                break;
            case Division:
                Value /= adjustValue;
                break;
            case Equals:
                Value = adjustValue;
                break;
        }
        switch (adjustType){
            case MaxHP:
                this.MaxHp = Value;
                switch (calculationMethod){
                    case Plus:
                        this.CurrentHp += Value;
                        if(this.CurrentHp > this.MaxHp)
                            this.CurrentHp = this.MaxHp;
                        break;
                    case Minus:
                        this.CurrentHp -= Value;
                        if(this.CurrentHp > this.MaxHp)
                            this.CurrentHp = this.MaxHp;
                        break;
                }
                break;
            case CurrentHP:
                this.CurrentHp = Value;
                if(this.CurrentHp > this.MaxHp)
                    this.CurrentHp = MaxHp;
                if (this.CurrentHp < 0 ){
                    this.CurrentHp = 0;
                }
                break;
            case MaxMP:
                this.MaxMp = Value;
                Log.e("After Value" , "" + Value);
                switch (calculationMethod){
                    case Plus:
                        this.CurrentMp += Value;
                        if(this.CurrentMp > this.MaxMp)
                            this.CurrentMp = this.MaxMp;
                        break;
                    case Minus:
                        this.CurrentMp -= Value;
                        if(this.CurrentMp > this.MaxMp)
                            this.CurrentMp = this.MaxMp;
                        break;
                }
                break;
            case CurrentMP:
                this.CurrentMp = Value;
                if(this.CurrentMp > this.MaxMp)
                    this.CurrentMp = MaxMp;
                if (this.CurrentMp < 0 ){
                    this.CurrentMp = 0;
                }
                break;
            case MinDam:
                this.MinDamage = Value;
                break;
            case MaxDam:
                this.Maxdamage = Value;
                break;
            case HPRecoveryValue:
                this.hpRecoveryValue = Value;
                break;
            case MPRecoveryValue:
                this.mpRecoveryValue = Value;
                break;
        }
        //Log.e("After Value" , "" + Value);
    }
    public void adjustValueDouble(AdjustType adjustType,CalculationMethod calculationMethod, double adjustValue){
        double Value = 0.0;
        switch (adjustType){
            case AtkSpeed:
                Value = this.atkSpeed;
                break;
            case BulletSpeed:
                Value = this.bulletSpeed;
                break;
            case MoveSpeed:
                Value = this.MoveSpeed;
                break;
            case HPRecoverySpeed:
                Value = this.hpRecoverySpeed;
                break;
            case MPRecoverySpeed:
                Value = this.mpRecoverySpeed;
                break;
        }
        //Log.e("Before Value" , "" + Value);
        switch (calculationMethod){
            case Plus:
                Value += adjustValue;
                break;
            case Minus:
                Value -= adjustValue;
                break;
            case Multiply:
                Value *= adjustValue;
                break;
            case Division:
                Value /= adjustValue;
                break;
            case Equals:
                Value = adjustValue;
                break;
        }
        switch (adjustType){
            case AtkSpeed:
                this.atkSpeed = Value;
                break;
            case BulletSpeed:
                this.bulletSpeed = Value;
                break;
            case MoveSpeed:
                this.MoveSpeed = Value;
                break;
            case HPRecoverySpeed:
                this.hpRecoverySpeed = Value;
                break;
            case MPRecoverySpeed:
                this.mpRecoverySpeed = Value;
                break;
        }
        //Log.e("After Value" , "" + Value);
    }

    public void getHurt(int Value){
            colorChangeTask = new colorChange();

        gotDamages.add(Value);
        //Log.e("Enemy HP", "" + getCurrentHp());
        this.CurrentHp -= Value;
        if(this.CurrentHp <= 0){
            isDead = true;
        }
    }

    public void addExp(int Value){
        int beforeValue = this.getExp();
        Log.e("Before Exp", "" + beforeValue);
            this.Exp += Value;
        Log.e("After Exp", "" + this.Exp);
        gameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.Exp,this);
                while (this.Exp >= this.ExpRequirement) {
                    Log.e("Level Up Before Exp", "" + beforeValue);
                    this.Lv += 1;
                    gameScreen.skillsContainer.learnSkill(Skill.SkillType.SOULEATER);
                    this.Exp -= this.ExpRequirement;
                    Log.e("Level Up After Exp", "" + this.Exp);
                    gameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.Exp,this);
                    gameScreen.skillsContainer.learnSkill(Skill.SkillType.SOULEATER);
                    gameScreen.skillsContainer.skillPoint += 3;
                }
    }

    public void setExp(int Value){
        this.Exp = Value;
    }

    public void setFireTask(FireTask fireTask){
        this.fireTask = fireTask;
    }

    public void setFireTimer(Timer fireTimer){
        this.FireTimer = fireTimer;
    }


    public void setPoisonTask(PoisonTask poisonTask) {
        this.poisonTask = poisonTask;
    }

    public void setPoisonTimer(Timer poisonTimer) {
        PoisonTimer = poisonTimer;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
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

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public void setBounce(boolean bounce) {
        this.bounce = bounce;
    }

    public void setAniamated(boolean animated) {
        this.animated = animated;
    }

    public void setName(String name){
        this.name = name;
    }

    // Setter Setting End...

    // Public Method Setting
    public void move(int move_x, int move_y) {
        this.x += move_x;
        this.y += move_y;
    }
    public void handleBounce(int left, int top, int right, int bottom) {
        if(bounce) {
            if(x < left + spriteWidth/2 || x > right - spriteWidth/2) {
                if(x < left + spriteWidth/2){
                    this.x += spriteWidth/12;
                }
                else if(x > right - spriteWidth/2){
                    this.x -= spriteWidth/12;
                }
                hSpeed *= 0;
            }
            if(y < top + spriteHeight/2 || y > bottom - spriteHeight/2) {
                if(y < top + spriteHeight/2){
                    this.y += spriteHeight/12;
                }
                else if(y > bottom - spriteHeight/2){
                    this.y -= spriteHeight/12;
                }
                vSpeed *= 0;
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
        switch(direction) {
            case 'd': bitmap = d_bitmap; break;
            case 'l': bitmap = l_bitmap; break;
            case 'r': bitmap = r_bitmap; break;
            case 'u': bitmap = u_bitmap; break;
        }

        if(isGetHurt){
            paint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(GP.GM.mContext,R.color.colorRed), PorterDuff.Mode.SRC_IN));
        }else
            paint.setColorFilter(null);

        if(hSpeed == 0 && vSpeed == 0){
            canvas.drawBitmap(bitmap[curFrame],(int)(x - spriteWidth/2),(int)( y - spriteHeight / 2), paint);
        }

        canvas.drawBitmap(bitmap[nextFrame()],(int)(x - spriteWidth/2),(int)( y - spriteHeight / 2), paint);
    }
    public void update() {
        if(!touched) {
            x += hSpeed;
            y += vSpeed;
        }
        direction = 'u';
        /*
        if(Math.abs(hSpeed) < Math.abs(vSpeed)) {
            if (vSpeed > 0)
                direction = 'd';
            else if(vSpeed < 0)
                direction = 'u';
        }
        else if(hSpeed > 0)
            direction = 'r';
        else if (hSpeed < 0) direction = 'l';
        else direction = 'u';
        */

    }

    //Collision Detection With Other Character
    public boolean collideWith(Character other) {
        if((Math.abs(this.x - other.x) < this.spriteWidth/2 + other.spriteWidth/2) &&
                (Math.abs(this.y - other.y) < this.spriteHeight/2 + other.spriteHeight/2))
            return true;
        return false;
    }

    public boolean collideWith(Bullet other) {
        if(isDead){
            return false;
        }
        Rect BulletRect = new Rect((int)(other.x + other.getSpriteWidth()*2.5f), other.y + other.getSpriteHeight(), (int)(other.x + other.getSpriteWidth() * 4.5), (int)(other.y + other.getSpriteHeight() * 2.5f));
        Rect CharRect = new Rect((int)(x - getSpriteWidth() * 0.4),(int)(y - getSpriteHeight() * 0.4), (int)(x + getSpriteWidth() * 0.4), (int)(y + getSpriteHeight() * 0.45));

        if(CharRect.intersect(BulletRect)){
            //Log.e("Bullet Rect Value", ":Left: " + BulletRect.left + ", Top: " + BulletRect.top + ", Right: " + BulletRect.right + ", Bottom: " + BulletRect.bottom);
            //Log.e("Chat Rect Value", ":Left: " + CharRect.left + ", Top: " + CharRect.top + ", Right: " + CharRect.right + ", Bottom " + CharRect.bottom);
            return true;
        }

        return false;
        /*
        if(((Math.abs(this.x - other.x) <= this.spriteWidth + other.spriteWidth) &&
                (Math.abs(this.y - other.y) <= this.spriteHeight + other.spriteHeight)))  {
            Log.e("Bullet Touch", "True");
            return true;
        }
        return false;
        */
    }

    public boolean collideWith(final Enemy other) {
        Rect CharRect = new Rect((int)(x - getSpriteWidth() * 0.4),(int)(y - getSpriteHeight() * 0.45), (int)(x + getSpriteWidth() * 0.4), (int)(y + getSpriteHeight() * 0.5));
        Rect EnemyRect = new Rect((int)(other.getX() - other.getSpriteWidth() * 0.4),(int)(other.getY() - other.getSpriteHeight() * 0.45), (int)(other.getX() + other.getSpriteWidth() * 0.4), (int)(other.getY() + other.getSpriteHeight() * 0.5));
        if(CharRect.intersect(EnemyRect)){
            final TimerTask gethurtSpeed = new TimerTask() {
                @Override
                public void run() {
                    if(other.collideTime > 1) {
                        other.setCollideWithCharacter(false);
                        other.collideTime = 0;
                        coilldewithOtherTimer.cancel();
                        coilldewithOtherTimer.purge();
                        coilldewithOtherTimer = null;
                    }
                }
            };
            //Log.e("Bullet Rect Value", ":Left: " + BulletRect.left + ", Top: " + BulletRect.top + ", Right: " + BulletRect.right + ", Bottom: " + BulletRect.bottom);
            //Log.e("Chat Rect Value", ":Left: " + CharRect.left + ", Top: " + CharRect.top + ", Right: " + CharRect.right + ", Bottom " + CharRect.bottom);
            other.setCollideWithCharacter(true);
            other.collideTime++;

            if(coilldewithOtherTimer == null){
                coilldewithOtherTimer = new Timer();
                coilldewithOtherTimer.schedule(gethurtSpeed,(long)other.getAtkSpeed());
            }
            CharRect = null;
            EnemyRect = null;
            return true;
        }else{
            CharRect = null;
            EnemyRect = null;
            return false;
        }

    }


    public void handleActionDown(int eventX, int eventY){
        if (eventX >= (x - spriteWidth/2) &&
                (eventX <= (x + spriteWidth/2))) {
            if (eventY >= (y - spriteHeight/2) &&
                    (eventY <= (y + spriteHeight/2))){
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }
    }

    public void fire(Bitmap bitmap, int noOfFrame, int height, int noOfTickPerFrame){
        Bullet shootingBullet = new Bullet(0,0,0,-1);
        shootingBullet.setBitmap(bulletData);
        shootingBullet.setDamage(getRandomDamage());
        shootingBullet.setBounce(true);
        shootingBullet.setX((int)getX() - getSpriteWidth()); shootingBullet.setY((int)getY() - shootingBullet.getSpriteHeight());
        shootingBullet.setVSpeed(-getBulletSpeed());
        firedBullets.add(shootingBullet);
        if(isPlayer && gameScreen != null){
            gameScreen.sp.play(gameScreen.soundIds[1], .5f, .5f, 1, 0, 1.0f);
        }
        adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Minus, 1);
        //bullet.launch(direction); //this should be calculated by which direction the player is facing.
    }

    private class colorChange {
        CountDownTimer Timer;
        final HandlerThread handlerThread = new HandlerThread("ColorChange");
        public colorChange(){
            if (handlerThread.getState() == Thread.State.NEW)
                handlerThread.start();
            run();
        }
        public void run(){
            final Handler handler = new Handler(handlerThread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Timer = new CountDownTimer(500, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            isGetHurt = true;
                        }

                        @Override
                        public void onFinish() {
                            {
                                colorChangeTask = null;
                                getHurtTime = 0;
                                handlerThread.quit();
                                handlerThread.interrupt();
                                getGotDamages().remove(0);
                                isGetHurt = false;
                                cancel();
                            }
                        }
                    }.start();
                }
            });
        }
    }

    public void calculateTotalValue(SkillsContainer skillsContainer){
        this.adjustValueInt(AdjustType.MaxHP,CalculationMethod.Equals,(int)(skillsContainer.skills[0].totalValue + this.baseMaxHP));
        this.adjustValueInt(AdjustType.MaxMP,CalculationMethod.Equals,(int)(skillsContainer.skills[1].totalValue  + this.baseMaxMP));
        this.adjustValueInt(AdjustType.HPRecoveryValue,CalculationMethod.Equals,(int)(skillsContainer.skills[2].totalValue + this.baseHPRecoveryValue));
        this.adjustValueInt(AdjustType.MPRecoveryValue,CalculationMethod.Equals,(int)(skillsContainer.skills[3].totalValue + this.baseMPRecoveryValue));
        this.adjustValueDouble(AdjustType.MoveSpeed,CalculationMethod.Equals,(skillsContainer.skills[4].totalValue  + this.baseMoveSpeed));
    }
}
