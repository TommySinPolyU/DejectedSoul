package agstassignment.dejectedsoul;

import static agstassignment.dejectedsoul.BulletEquipment.BulletType.BLUEFIRE;
import static agstassignment.dejectedsoul.BulletEquipment.BulletType.DARK;
import static agstassignment.dejectedsoul.BulletEquipment.BulletType.FIREBALL;
import static agstassignment.dejectedsoul.BulletEquipment.BulletType.FOREST;

public class EnemyBulletEquipmentContainer {
    BulletEquipment[] bulletEquipments;
    int index;

    public EnemyBulletEquipmentContainer(){
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
                1.0f,2,8,12);
        bulletEquipments[1].InitailizeBulletEquipment(
                BLUEFIRE,R.drawable.bullet_lightblue,
                1.0f,0,1,18);
        bulletEquipments[2].InitailizeBulletEquipment(
                FOREST,R.drawable.bullet_lightgreen,
                1.0f,1,4,13);
        bulletEquipments[3].InitailizeBulletEquipment(
                DARK, R.drawable.bullet_black,
                1.0f,0,12,14);
    }

    public BulletEquipment[] getBulletEquipments() {
        return bulletEquipments;
    }

    public void setEquippedBullet(Character character, BulletEquipment bulletEquipment){
        /* Index 0 = FireBall
                    Index 1 = BlueFire
                    Index 2 = Forest
                    Index 3 = Dark
                */
        for(int i = 0; i < bulletEquipments.length; i++){
            bulletEquipments[i].calculateTotalValue();
        }
        // Remove the current bullet skill effects.
        if(character.getEquippedBullet() != null) {
            switch (character.getEquippedBullet().bulletType) {
                case FIREBALL:
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[0].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[0].totalMaxATK);
                    break;
                case BLUEFIRE:
                    character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Multiply, 1.2);
                    character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Division, 1.2);
                    character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Minus,bulletEquipments[1].totalMinATK);
                    character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Minus,bulletEquipments[1].totalMaxATK);
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
        switch (bulletEquipment.getBulletType()){
            case FIREBALL:
                character.equippedBullet = bulletEquipments[0];
                character.adjustValueInt(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[0].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[0].totalMaxATK);
                break;
            case BLUEFIRE:
                character.equippedBullet = bulletEquipments[1];
                character.adjustValueInt(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[1].totalBulletSpeed);
                character.adjustValueDouble(Character.AdjustType.AtkSpeed, Character.CalculationMethod.Division, 1.2);
                character.adjustValueDouble(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Multiply, 1.2);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[1].totalMaxATK);
                break;
            case FOREST:
                character.equippedBullet = bulletEquipments[2];
                character.adjustValueInt(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[2].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[2].totalMaxATK);
                break;
            case DARK:
                character.equippedBullet = bulletEquipments[3];
                character.adjustValueInt(Character.AdjustType.BulletSpeed, Character.CalculationMethod.Equals, bulletEquipments[3].totalBulletSpeed);
                character.adjustValueInt(Character.AdjustType.MinDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMinATK);
                character.adjustValueInt(Character.AdjustType.MaxDam, Character.CalculationMethod.Plus,bulletEquipments[3].totalMaxATK);
                break;
            case NULL:
                break;
        }
    }


}
