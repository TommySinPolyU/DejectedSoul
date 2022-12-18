package agstassignment.dejectedsoul;

public class BulletEquipment {
    public enum BulletType{
        FIREBALL,
        BLUEFIRE,
        FOREST,
        DARK,
        NULL;

        public static int toInt(BulletType bulletType){
            return bulletType.ordinal();
        }
    }

    BulletType bulletType;
    int lv, baseMinATK, baseMaxATK, baseBulletSpeed;
    int totalMinATK, totalMaxATK, totalBulletSpeed;
    int bulletBitmapID;
    float scaleValue;

    public int getBulletBitmapID() {
        return bulletBitmapID;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getTotalMinATK() {
        return totalMinATK;
    }

    public int getTotalMaxATK() {
        return totalMaxATK;
    }

    public int getTotalBulletSpeed() {
        return totalBulletSpeed;
    }

    public float getScaleValue() {
        return scaleValue;
    }

    public int getLv() {
        return lv;
    }

    public void InitailizeBulletEquipment(BulletType bulletType, int bulletBitmapID, float scaleValue , int baseMinATK, int baseMaxATK, int baseBulletSpeed){
        this.bulletType = bulletType;
        this.lv = 1;
        this.bulletBitmapID = bulletBitmapID;
        this.scaleValue = scaleValue;
        this.baseMinATK = baseMinATK;
        this.baseMaxATK = baseMaxATK;
        this.baseBulletSpeed= baseBulletSpeed;
    }


    public void calculateTotalValue(){
        totalMinATK = baseMinATK + lv / 2;
        totalMaxATK = baseMaxATK + lv;
        totalBulletSpeed = baseBulletSpeed + lv / 10;
    }

}
