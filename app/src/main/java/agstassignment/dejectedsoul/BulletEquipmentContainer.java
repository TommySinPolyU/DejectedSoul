package agstassignment.dejectedsoul;

import android.util.Log;

import static agstassignment.dejectedsoul.BulletEquipment.BulletType.*;

public class BulletEquipmentContainer {
    BulletEquipment[] bulletEquipments;

    public BulletEquipmentContainer(){
        bulletEquipments = new BulletEquipment[4];
        /* Index 0 = FireBall
                    Index 1 = BlueFire
                    Index 2 = Forest
                    Index 3 = Dark
                */
        for(int i = 0; i< bulletEquipments.length; i++){
            bulletEquipments[i] = new BulletEquipment();
        }
        bulletEquipments[0].InitailizeBulletEquipment(
                FIREBALL,R.drawable.bullet_normal,
                1.0f,3,7,21);
        bulletEquipments[1].InitailizeBulletEquipment(
                BLUEFIRE,R.drawable.bullet_lightblue,
                1.0f,0,1,32);
        bulletEquipments[2].InitailizeBulletEquipment(
                FOREST,R.drawable.bullet_lightgreen,
                1.0f,2,6,24);
        bulletEquipments[3].InitailizeBulletEquipment(
                DARK, R.drawable.bullet_black,
                1.0f,0,9,26);
    }

    public BulletEquipment[] getBulletEquipments() {
        return bulletEquipments;
    }

    public void setEquippedBullet(Character character,BulletEquipment.BulletType bulletType){
        /* Index 0 = FireBall
                    Index 1 = BlueFire
                    Index 2 = Forest
                    Index 3 = Dark
                */

        for(int i = 0; i < bulletEquipments.length; i++){
            bulletEquipments[i].calculateTotalValue();
        }

        Log.e("BulletSpeed", ""+ character.getBulletSpeed());
        Log.e("MaxMP", ""+ character.getMaxMp());
        Log.e("MPRecoveryValue", ""+ character.getMpRecoveryValue());
        Log.e("AtkSpeed", ""+ character.getAtkSpeed());
        Log.e("MinDam", ""+ character.getMinDamage());
        Log.e("MaxDam", ""+ character.getMaxdamage());

        // Remove the current bullet skill effects.
        if(character.getEquippedBullet() != null) {
            switch (character.getEquippedBullet().bulletType) {
                case FIREBALL:
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Plus,40);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[0].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[0].totalMaxATK);
                    Log.e("BulletSpeed", ""+ character.getBulletSpeed());
                    Log.e("MaxMP", ""+ character.getMaxMp());
                    Log.e("MPRecoveryValue", ""+ character.getMpRecoveryValue());
                    Log.e("AtkSpeed", ""+ character.getAtkSpeed());
                    Log.e("MinDam", ""+ character.getMinDamage());
                    Log.e("MaxDam", ""+ character.getMaxdamage());
                    break;
                case BLUEFIRE:
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Minus,(17 + bulletEquipments[1].lv * 3));
                    character.adjustValueInt(Character.AdjustType.MPRecoveryValue, Character.CalculationMethod.Minus,(6 + bulletEquipments[1].lv));
                    character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Multiply, 1.14 + 0.01 * bulletEquipments[1].lv);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[1].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[1].totalMaxATK);
                    Log.e("BulletSpeed", ""+ character.getBulletSpeed());
                    Log.e("MaxMP", ""+ character.getMaxMp());
                    Log.e("MPRecoveryValue", ""+ character.getMpRecoveryValue());
                    Log.e("AtkSpeed", ""+ character.getAtkSpeed());
                    Log.e("MinDam", ""+ character.getMinDamage());
                    Log.e("MaxDam", ""+ character.getMaxdamage());
                    break;
                case FOREST:
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[2].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[2].totalMaxATK);
                    break;
                case DARK:
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[3].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[3].totalMaxATK);
                    break;
                case NULL:

                    break;
            }
        }
        // Add the equip bullet skill effects.
        /* Index 0 = FireBall
                    Index 1 = BlueFire
                    Index 2 = Forest
                    Index 3 = Dark
                */
        switch (bulletType){
            case FIREBALL:
                character.equippedBullet = bulletEquipments[0];
                character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[0].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Minus,40);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMaxATK);
                Log.e("BulletSpeed", ""+ character.getBulletSpeed());
                Log.e("MaxMP", ""+ character.getMaxMp());
                Log.e("MPRecoveryValue", ""+ character.getMpRecoveryValue());
                Log.e("AtkSpeed", ""+ character.getAtkSpeed());
                Log.e("MinDam", ""+ character.getMinDamage());
                Log.e("MaxDam", ""+ character.getMaxdamage());
                break;
            case BLUEFIRE:
                character.equippedBullet = bulletEquipments[1];
                character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Plus,(17 + bulletEquipments[1].lv * 3));
                character.adjustValueInt(Character.AdjustType.MPRecoveryValue, Character.CalculationMethod.Plus,(6 + bulletEquipments[1].lv));
                character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[1].totalBulletSpeed);
                character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Division, 1.14 + 0.01 * bulletEquipments[1].lv);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMaxATK);
                Log.e("BulletSpeed", ""+ character.getBulletSpeed());
                Log.e("MaxMP", ""+ character.getMaxMp());
                Log.e("MPRecoveryValue", ""+ character.getMpRecoveryValue());
                Log.e("AtkSpeed", ""+ character.getAtkSpeed());
                Log.e("MinDam", ""+ character.getMinDamage());
                Log.e("MaxDam", ""+ character.getMaxdamage());
                break;
            case FOREST:
                character.equippedBullet = bulletEquipments[2];
                character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[2].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMaxATK);
                break;
            case DARK:
                character.equippedBullet = bulletEquipments[3];
                character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[3].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMaxATK);
                break;
            case NULL:

                break;
        }
    }

    public void enhanceBullet(Character character,BulletEquipment.BulletType bulletType){
        if(character.getEquippedBullet() != null) {
        if(character.getEquippedBullet().getBulletType() == bulletType) {
            switch (character.getEquippedBullet().bulletType) {
                case FIREBALL:
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Plus, 40);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus, bulletEquipments[0].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus, bulletEquipments[0].totalMaxATK);
                    break;
                case BLUEFIRE:
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Minus, (17 + bulletEquipments[1].lv * 3));
                    character.adjustValueInt(Character.AdjustType.MPRecoveryValue, Character.CalculationMethod.Minus, (6 + bulletEquipments[1].lv));
                    character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Multiply, 1.14 + 0.01 * bulletEquipments[1].lv);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus, bulletEquipments[1].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus, bulletEquipments[1].totalMaxATK);
                    break;
                case FOREST:
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus, bulletEquipments[2].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus, bulletEquipments[2].totalMaxATK);
                    break;
                case DARK:
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus, bulletEquipments[3].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus, bulletEquipments[3].totalMaxATK);
                    break;
                case NULL:

                    break;
            }
        }
        }

        switch (bulletType){
            case FIREBALL:
                bulletEquipments[0].lv++;
                break;
            case BLUEFIRE:
                bulletEquipments[1].lv++;
                break;
            case FOREST:
                bulletEquipments[2].lv++;
                break;
            case DARK:
                bulletEquipments[3].lv++;
                break;
            case NULL:
                break;
        }

        for(int i = 0; i < bulletEquipments.length; i++){
            bulletEquipments[i].calculateTotalValue();
        }
        Log.e("Get Type", "" + character.getEquippedBullet().getBulletType());
        if(character.getEquippedBullet().getBulletType() == bulletType){
            switch (bulletType){
                case FIREBALL:
                    character.equippedBullet = bulletEquipments[0];
                    character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[0].totalBulletSpeed);
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Minus,40);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMaxATK);
                    break;
                case BLUEFIRE:
                    Log.e("BulletSpeed", ""+ bulletEquipments[1].totalBulletSpeed);
                    character.equippedBullet = bulletEquipments[1];
                    character.adjustValueInt(Character.AdjustType.MaxMP, Character.CalculationMethod.Plus,(17 + bulletEquipments[1].lv * 3));
                    character.adjustValueInt(Character.AdjustType.MPRecoveryValue, Character.CalculationMethod.Plus,(6 + bulletEquipments[1].lv));
                    character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[1].totalBulletSpeed);
                    character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Division, 1.14 + 0.01 * bulletEquipments[1].lv);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMaxATK);
                    break;
                case FOREST:
                    character.equippedBullet = bulletEquipments[2];
                    character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[2].totalBulletSpeed);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMaxATK);
                    break;
                case DARK:
                    character.equippedBullet = bulletEquipments[3];
                    character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[3].totalBulletSpeed);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMaxATK);
                    break;
                case NULL:
                    break;
            }
        }
    }
}

