package agstassignment.dejectedsoul;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StagesContainer {
    // Stage Setting Class:
    // Create a stage by calling the "createStage" method: the method setting is createStage(stageNumber, Enemy[]);
    // All enemy attribute need to be set in this class.
    // Container element setting.
    // stage number start from 0.
    public enum BossStageType{
        DarkSlimeKing,
        ODIN,
        ForestSlimeKing,
        WaterSlimeKing,
        FireSlimeKing
    }

    EnemyBulletEquipmentContainer enemyBulletEquipmentContainer = new EnemyBulletEquipmentContainer();
    protected Random random = new Random();
    public GameBackStageUI mGameBackStageUI;
    Bullet bulletDataClass;
    private List<Stage> stages = new ArrayList<Stage>();
    public int maxStage;

    private Enemy[] enemiesList = new Enemy[17];
    int checkNum = 0;


    public StagesContainer(GameBackStageUI gameBackStageUI,int maxStage) {
        this.mGameBackStageUI = gameBackStageUI;
        bulletDataClass = new Bullet(mGameBackStageUI);
        this.maxStage = maxStage;
        // region A enemies list  contain the enemy type.
        // Add new Enemy data below.
        enemiesList[0] = new Enemy("Holy Slime",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimeholyi),1,1,1,
                false,false,false);

        enemiesList[1] = new Enemy("Holy Slime Elite",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimeholyii),1,1,1,
                false,true,false);

        enemiesList[2] = new Enemy("Holy Slime King",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimeholyiii),1,1,1,
                true,false,false);

        enemiesList[3] = new Enemy("Dark Slime",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimedarki),1,1,1,
                false,false,false);

        enemiesList[4] = new Enemy("Dark Slime Elite",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimedarkii),1,1,1,
                false,true,false);

        enemiesList[5] = new Enemy("Dark Slime King",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimedarkiii),1,1,1,
                true,false,false);

        enemiesList[6] = new Enemy("Fire Slime",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimefirei),1,1,1,
                false,false,false);

        enemiesList[7] = new Enemy("Fire Slime Elite",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimefireii),1,1,1,
                false,true,false);

        enemiesList[8] = new Enemy("Fire Slime King",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimefireiii),1,1,1,
                true,false,false);

        enemiesList[9] = new Enemy("Water Slime",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewateri),1,1,1,
                false,false,false);

        enemiesList[10] = new Enemy("Water Slime Elite",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewaterii),1,1,1,
                false,true,false);

        enemiesList[11] = new Enemy("Water Slime King",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewateriii),1,1,1,
                true,false,false);

        enemiesList[12] = new Enemy("Forest Slime",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewindi),1,1,1,
                false,false,false);

        enemiesList[13] = new Enemy("Forest Slime Elite",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewindii),1,1,1,
                false,true,false);

        enemiesList[14] = new Enemy("Forest Slime King",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_slimewindiii),1,1,1,
                true,false,false);

        enemiesList[15] = new Enemy("Odin",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_odin),1,1,1,
                true,false,true);

        enemiesList[16] = new Enemy("Odin",BitmapFactory.decodeResource(mGameBackStageUI.getResources(), R.drawable.enemy_odin_mirror),1,1,1,
                true,true,true);

        // endregion

        // region Create a null stage for initialize the enemies to the stage and adjust the difficulty of the stage.
        for(int i = 0; i < maxStage; i++){
            if(mGameBackStageUI.endlessDifficulty == GameBackStageUI.EndlessDifficulty.Easy) {
                if ((i + 1) % 10 == 0) {
                    createStage(i, 2, true);
                } else
                    createStage(i, 1, false);
            }
            else if (mGameBackStageUI.endlessDifficulty == GameBackStageUI.EndlessDifficulty.Normal){
                if((i+1) % 10 == 0){
                    createStage(i, 3, true);
                }else
                    createStage(i, 2, false);
                }
            else if (mGameBackStageUI.endlessDifficulty == GameBackStageUI.EndlessDifficulty.Hard) {
                if ((i + 1) % 10 == 0) {
                    createStage(i, 5, true);
                } else
                    createStage(i, 3, false);
            }
            else if (mGameBackStageUI.endlessDifficulty == GameBackStageUI.EndlessDifficulty.Hell) {
                if ((i + 1) % 10 == 0) {
                    createStage(i, 7, true);
                } else
                    createStage(i, 5, false);
            } else createStage(i,1,false);
        }
        // endregion

        // To Initialize all stage when the play mode is ENDLESS CHALLENGE
        for(int i = 0; i < maxStage; i++) {
            if ((i+1) % 10 != 0) {
                // Create a Normal Stage;'
                Log.e("Stage Status " + (i+1), "Normal");
                setStage(i, 1 + random.nextInt(stages.get(i).getDifficulty()));
            }
            else if ((i+1) % 10 == 0) {
                // Create a Boss Stage
                Log.e("Stage Status "+ (i+1), "Boss");
                setStage(i, 3 + random.nextInt(stages.get(i).getDifficulty()));
            }
        }

    }

    public void setBossStage(BossStageType bossStage){
        switch (bossStage){
            case DarkSlimeKing: // Bullet Lv Index = 3;
                //region DarkSlimeKing Stage Setting
                setStage(0, new Enemy[]{
                        new Enemy(enemiesList[5],random.nextDouble(), true, false, false,
                                mGameBackStageUI.gameMainScreenContainer.getWidth() / 2,mGameBackStageUI.gameMainScreenContainer.getHeight() / 8,0,0,
                                mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv + 10, 2000 + (200 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv), 3,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 5, (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 15,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 100, 1,
                                125, 0, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 20,
                                10000 - (20 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv), 250,
                                (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 100, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 100, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[4],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 4),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,12,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 500, 3,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 4, (7 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50, (7 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[4],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,12,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 500, 3,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 4, (7 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[3],random.nextDouble(), true,true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,6,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 250, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 10, (7 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,0,
                                1000, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 6, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[3],random.nextDouble(), true,true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,6,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 250, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 10, (4* mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,0,
                                1000, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 6, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[3],random.nextDouble(), true,true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,6,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 250, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 10, (4 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,0,
                                1000, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 6, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[3]),
                        new Enemy(enemiesList[3],random.nextDouble(), true,true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,6,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 250, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 10, (4 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 50,0,
                                1000, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[3].lv) + 6, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[3]),
                });
                break;
                //endregion
            case FireSlimeKing: // Bullet Lv Index = 0;
                //region FireSlimeKing Stage Setting
                setStage(0, new Enemy[]{
                        new Enemy(enemiesList[8],random.nextDouble(), true, false, false,
                                mGameBackStageUI.gameMainScreenContainer.getWidth() / 2,mGameBackStageUI.gameMainScreenContainer.getHeight() / 8,4,0,
                                mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv + 10, 1000 + (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv), 15,
                                (3 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 5, (6 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 15,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 100, 1,
                                125, 8, 30,
                                10000 - (20 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv), 250,
                                (250 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 500, (125 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 250, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[7],random.nextDouble(), true, true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 500, 1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,4,
                                750, 0, 24,
                                1000, 1000,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[7],random.nextDouble(), true, true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 500, 1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,4,
                                1000, 0, 26,
                                1000, 1000,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[7],random.nextDouble(), true, true, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 500, 1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,4,
                                1250, 0,  28,
                                1000, 1000,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[7],random.nextDouble(), true, true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 500, 1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,4,
                                1500, 0, 30,
                                1000, 1000,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[6],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,(int)((0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 6) * -1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 250, 0,
                                (3 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 10, (6 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,0,
                                1000, 0, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[6],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,(int)((0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 6),
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 250, 0,
                                (3 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 10, (6 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,0,
                                1000, 0, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[6],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,(int)((0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 6) * -1,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 250, 0,
                                (3 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 10, (6 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,0,
                                1000, 0, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                        new Enemy(enemiesList[6],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,(int)((0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 6),
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 3, (75 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 250, 0,
                                (3 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 10, (6 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 20,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 50,0,
                                1000, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[0].lv) + 6, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[0]),
                });
                break;
                //endregion
            case ForestSlimeKing: // Bullet Lv Index = 2;
                //region ForestSlimeKing Stage Setting
                setStage(0, new Enemy[]{
                        new Enemy(enemiesList[14],random.nextDouble(), true, false, false,
                                mGameBackStageUI.gameMainScreenContainer.getWidth() / 2,mGameBackStageUI.gameMainScreenContainer.getHeight() / 8,24,0,
                                mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv + 10, 2000 + (200 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 10,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (2 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 15,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 100, 1,
                                125, 16, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 20,
                                10000 - (20 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 250,
                                (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 100, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 100, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[13],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 6,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 500, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[13],random.nextDouble(), true, false,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 6,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 500, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[13],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 6,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 500, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[13],random.nextDouble(), true, false,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 6,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 500, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 6, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[12],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),(int)(mGameBackStageUI.gameMainScreenContainer.getHeight() / 3.8),12,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 250, 2,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 12, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[12],random.nextDouble(), true, false,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),(int)(mGameBackStageUI.gameMainScreenContainer.getHeight() / 2.6),12,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 250, 2,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 12, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[12],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),(int)(mGameBackStageUI.gameMainScreenContainer.getHeight() / 2),15,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 250, 2,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 15, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2]),
                        new Enemy(enemiesList[12],random.nextDouble(), true, false,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),(int)(mGameBackStageUI.gameMainScreenContainer.getHeight() / 1.6),15,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 5, (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 25, 2,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50,4,
                                400, 15, (0.1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 24,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[2].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[2])
                });
                break;
                //endregion
            case WaterSlimeKing: // Bullet Lv Index = 1;
                //region WaterSlimeKing Stage Setting
                setStage(0, new Enemy[]{
                        new Enemy(enemiesList[11],random.nextDouble(), true, false, false,
                                mGameBackStageUI.gameMainScreenContainer.getWidth() / 2,mGameBackStageUI.gameMainScreenContainer.getHeight() / 8,0,0,
                                mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv + 10, 2000 + (200 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv), 80,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 5, (2 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 15,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 100, 1,
                                125, 0, (0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 30,
                                10000 - (20 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv), 250,
                                (50 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 100, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 100, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[10],random.nextDouble(), true, false, false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 4),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 500, 20,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50,4,
                                200, 6, (0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 30,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[10],random.nextDouble(), true, false,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,0,0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 5, (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 500, 20,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 4, (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 8,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50,4,
                                200, 6, (0.05 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 30,
                                10000 - (100 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv), 1000,
                                (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50, (10 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[9],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,30,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 125, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (2* mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 6,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3,0,
                                1000, 30, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[9],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,15,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 125, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (2 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 6,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50,0,
                                1000, 30, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[9],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,15,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 125, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (2* mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 6,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50,0,
                                1000, 30, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[9],random.nextDouble(), true,true,false,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,0,30,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (25 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 100, 0,
                                (1 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 3, (2 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 6,
                                (5 * mGameBackStageUI.bulletEquipmentContainer.bulletEquipments[1].lv) + 50,0,
                                1000, 30, 0,
                                1000, 1000,
                                0, 0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                });
                break;
            //endregion
            case ODIN:
                //region ODIN Stage Setting
                setStage(0, new Enemy[]{
                        new Enemy(enemiesList[15],random.nextDouble(), true, false, true,
                                mGameBackStageUI.gameMainScreenContainer.getWidth() / 2,mGameBackStageUI.gameMainScreenContainer.getHeight() / 8,4,8,
                                mGameBackStageUI.character.getLv() + 10, 1000 + (200 * mGameBackStageUI.character.getLv()), 6,
                                (1 * mGameBackStageUI.character.getLv()) + 15, (2 * mGameBackStageUI.character.getLv()) + 30,
                                (5 * mGameBackStageUI.character.getLv()) + 50, 1,
                                500, 12, (0.25 * mGameBackStageUI.character.getLv()) + 20,
                                1500, 250,
                                (50 * mGameBackStageUI.character.getLv()) + 1000, (100 * mGameBackStageUI.character.getLv()) + 1000, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[16],random.nextDouble(), true, true, true,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.2),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,12,6,
                                (1 * mGameBackStageUI.character.getLv()) + 5, (100 * mGameBackStageUI.character.getLv()) + 500, 3,
                                (1 * mGameBackStageUI.character.getLv()) + 8, (2 * mGameBackStageUI.character.getLv()) + 15,
                                (1 * mGameBackStageUI.character.getLv()) + 50,1,
                                1000, 6, 18,
                                3000, 500,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[16],random.nextDouble(), true, true,true,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 1.6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 4,12,6,
                                (1 * mGameBackStageUI.character.getLv()) + 5, (100 * mGameBackStageUI.character.getLv()) + 500, 3,
                                (1 * mGameBackStageUI.character.getLv()) + 8, (2 * mGameBackStageUI.character.getLv()) + 15,
                                (1 * mGameBackStageUI.character.getLv()) + 50,1,
                                1000, 6, 18,
                                3000, 500,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[16],random.nextDouble(), true,true,true,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 2.5),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,6,12,
                                (1 * mGameBackStageUI.character.getLv()) + 5, (100 * mGameBackStageUI.character.getLv()) + 500, 3,
                                (1 * mGameBackStageUI.character.getLv()) + 8, (2 * mGameBackStageUI.character.getLv()) + 15,
                                (1 * mGameBackStageUI.character.getLv()) + 50,1,
                                1000, 6, 18,
                                3000, 500,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                        new Enemy(enemiesList[16],random.nextDouble(), true,true,true,
                                (int)(mGameBackStageUI.gameMainScreenContainer.getWidth() / 6),mGameBackStageUI.gameMainScreenContainer.getHeight() / 12,6,12,
                                (1 * mGameBackStageUI.character.getLv()) + 5, (100 * mGameBackStageUI.character.getLv()) + 500, 3,
                                (1 * mGameBackStageUI.character.getLv()) + 8, (2 * mGameBackStageUI.character.getLv()) + 15,
                                (1 * mGameBackStageUI.character.getLv()) + 50,1,
                                1000, 6, 18,
                                3000, 500,
                                0,0, enemyBulletEquipmentContainer.bulletEquipments[1]),
                });
                break;
            //endregion
        }
    }


    public void createStage(int stageNumber, int difficulty, boolean isBossStage){
        Stage stage = new Stage((stageNumber + 1),difficulty, isBossStage);
        stages.add(stageNumber,stage);
    }

    // Manual Setting Method.
    public void setStage(int stageNumber, Enemy[] enemies){
        stages.get(stageNumber).removeAllEnemies();
        for(int i = 0; i < enemies.length;i++) {
            stages.get(stageNumber).addEnemy(enemies[i]);
            enemies[i].bulletData = bulletDataClass.bulletData[BulletEquipment.BulletType.toInt(enemies[i].equippedBullet.bulletType)];
            if(enemies[i].isUnDead){
                enemies[i].adjustValueInt(Character.AdjustType.HPRecoveryValue, Character.CalculationMethod.Equals, enemies[i].getMaxHp() / 5);
                enemies[i].adjustValueDouble(Character.AdjustType.HPRecoverySpeed, Character.CalculationMethod.Equals, 1000);
                Log.e("Hprevery Value" , i + ": " + enemies[i].getHpRecoveryValue());
            }
            enemies[i].setBounce(true);
        }
    }

    // Auto Random setting.
    public void setStage(int stageNumber, int numberofEnemies){
        stages.get(stageNumber).removeAllEnemies();
        Enemy[] enemies = new Enemy[numberofEnemies];
        int stageNumberPlus = stageNumber + 1;
        // Initialize and import random data to each enemy.
        // check if current stage num % 10 not equal to 0, it will be a normal stage. otherwise, it will be a Boss stage.
        for(int i = 0; i < enemies.length;i++){
            createEnemy(enemies, stageNumber, i);
            if (stageNumberPlus % 10 != 0){
                do{
                    createEnemy(enemies, stageNumber, i);
                }while (enemies[i].isBoss || enemies[i].isUnDead || enemies[i].isFinalBoss);
            }
            if (stageNumberPlus % 10 == 0){
                do{
                    createEnemy(enemies, stageNumber, i);
                }while (!enemies[i].isBoss || (enemies[i].isBoss && enemies[i].isFinalBoss));
            }
        }
        // check if current stage is a Boss stage and ensure that there are one boss only on the stage.
        if (stageNumberPlus % 10 == 0) {
            for (int i = 0; i < enemies.length; i++) {
                Enemy firstEnemy = enemies[0];
                if(enemies[i] == firstEnemy){
                    continue;
                } else {
                    do{
                        createEnemy(enemies, stageNumber, i);
                    }while (enemies[i].isBoss || enemies[i].isFinalBoss);
                    Log.e("Enemy " + i, "isBoss: " + enemies[i].isBoss + ", isUndead: " + enemies[i].isUnDead);
                }
            }
        }

        // Set the following value to each enemy and add it to the array list.
        for(int i = 0; i < enemies.length;i++) {
            enemies[i].adjustValueInt(Character.AdjustType.MaxHP, Character.CalculationMethod.Equals,10 * enemies[i].getLv() * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())));
            enemies[i].adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Equals,enemies[i].getMaxHp());
            enemies[i].setMoneyReward(0 + (enemies[i].getLv() * 2) * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())));
            enemies[i].setExpReward(0 + enemies[i].getLv() * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())));
            stages.get(stageNumber).addEnemy(enemies[i]);
        }
        checkNum = 0;
    }

    public void createEnemy(Enemy[] enemies,int stageNumber,int loop){
        int randomBullet = random.nextInt(enemyBulletEquipmentContainer.bulletEquipments.length);
        enemies[loop] = new Enemy(enemiesList[random.nextInt(enemiesList.length)],random.nextDouble(),
                0 + ( 1 + stageNumber/2 + random.nextInt(stageNumber + 1)), 0,2 * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())),
                1 + ((stageNumber + 1) / 3) * (1 + random.nextInt(stages.get(0).getDifficulty()) / 2), 4 + ((stageNumber+1)) * (2 + random.nextInt(stages.get(stageNumber).getDifficulty())),
                0 + random.nextInt((stageNumber + 1)) * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())), 1 + random.nextInt((stageNumber + 1)) * (1 + random.nextInt(stages.get(stageNumber).getDifficulty())),
                200 + random.nextInt(1000), 6 + random.nextInt( 3 + (stageNumber + 1) / 6), 6 + random.nextInt(18),
                3000 + random.nextInt(7000), 500 + random.nextInt(500)
                , enemyBulletEquipmentContainer.bulletEquipments[randomBullet]);
        if(enemies[loop].isUnDead){
            enemies[loop].adjustValueInt(Character.AdjustType.HPRecoveryValue, Character.CalculationMethod.Equals, enemies[loop].getMaxHp() / 5);
            enemies[loop].adjustValueDouble(Character.AdjustType.HPRecoverySpeed, Character.CalculationMethod.Equals, 1000);
        }
        enemies[loop].bulletData = bulletDataClass.bulletData[randomBullet];
        enemies[loop].setBounce(true);
    }


    public List<Stage> getStages(){
        return stages;
    }

    public boolean checkNull(int stage){
        int stagesEnemiesMaxNum = stages.get(stage).getEnemies().size();
        int stagesEnemiesNum = stages.get(stage).getEnemies().size();
        //Log.e("Enemies Num", "" + stagesEnemiesNum);
        for(int i = 0; i < stagesEnemiesMaxNum; i++){
            //Log.e("Status " + i , "" + stages.get(stage).getEnemies().get(i).isUnDead);
            if(stages.get(stage).getEnemies().get(i).isUnDead)
                stagesEnemiesNum--;
        }
        //Log.e("Enemies Num", "" + stagesEnemiesNum);
        if(stagesEnemiesNum > 0)
            return false;
        else
            return true;
    }


}
