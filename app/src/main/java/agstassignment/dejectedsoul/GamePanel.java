package agstassignment.dejectedsoul;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
    The Game Main Panel for handler the stage.
    The Class draw and update the player,  monster and their bullet or obstacle on the surface view.
    The Bullet will be handled by Thread "Fire" with Headler.
    The character and enemies HP recovery, MP recovery, and Fire will handled by this class/
    The Main Game Screen will be handled by "DrawingThread".

    Stage will auto add when player kill all enemies in the current stage.
    When new stage is started, the initializeEnemy() will be call and impory the new enemy data to SpawnedEnemy (Array: Enemy[])
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public StagesContainer stagesContainer;
    private LoadingTask loadingTask;
    //Enemy[] SpawnedEnemy;
    private List<Enemy> SpawnedEnemy = new ArrayList<Enemy>();
    private int stage, attackTime;
    public GameBackStageUI GameScreen;
    private DrawingThread drawingThread;
    private boolean isReady, BlueFire_overheat;
    boolean bulletReady, isLose = false;
    MediaPlayer NormalBGM, NormalBossBGM, BossChallengeBGM;


    int panelWidth, panelHeight;
    Character character;
    //Sprite Controller;
    Bitmap backGround;
    //char ControlDirection;
    //double ControllerValue;
    Random rand;
    boolean isEscape;


    public GameManager GM;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        drawingThread = new DrawingThread(getHolder(),this);
        setFocusable(true);
        rand = new Random();
        GM = new GameManager();
        GM.mContext = context;
    }


    protected void initGame() {
        // Initialize the game for starting the new challenge.
        // The Game Element will be initialize when loading task processing and update after the loading task

        if(loadingTask == null || loadingTask.getStatus().equals(AsyncTask.Status.FINISHED)){
            GameScreen.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GameScreen.loadingBar.setVisibility(View.VISIBLE);
                    GameScreen.loadingpercentage.setVisibility(View.VISIBLE);
                    GameScreen.loadingBar.setProgress(0);
                    GameScreen.loadingpercentage.setText("");
                }
            });
        loadingTask = new LoadingTask(3, GameScreen.loadingBar, GameScreen.loadingpercentage,doBgRunnable, doInProgress,doOnPostTask);
        loadingTask.execute();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isReady = true;
            //Controller.handleActionDown((int)event.getX(),(int)event.getY());
            character.handleActionDown((int)event.getX(),(int)event.getY());
            if(GameScreen.isAutoShooting && !BlueFire_overheat){
                bulletReady = true;
                character.isBulletReady = true;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(character.isTouched()){
                if(event.getX() > 0 && character.getX() > 0) {
                    character.setX(event.getX());
                }
                else if(character.getX() < 0)
                    character.setX(character.getX() + character.getSpriteWidth() / 12);
                if(event.getX() > 0 && character.getY() > 0) {
                    character.setY(event.getY());
                }
                else if(character.getY() < 0)
                    character.setY(character.getY() + character.getSpriteHeight() / 12);
                character.handleBounce(0, 0, getWidth(), getHeight());
            }
            // Movement Controller for the character
            // Controller Value for control the acceleration of the movement,
            //The closer to the left, the higher the speed of moving to the left, The closer to the right, the higher the speed of moving to the right
            /*
            if(Controller.isTouched()) {
                isReady = true;
                Controller.setX((int)event.getX());
                if(!GameScreen.isAutoShooting && event.getY() < Controller.getY() - Controller.getSpriteHeight() / 2 && !BlueFire_overheat){
                    bulletReady = true;
                    character.isBulletReady = true;
                }
                else if(!GameScreen.isAutoShooting) {
                    bulletReady = false;
                    character.isBulletReady = false;
                }
                if(Controller.getX() > getWidth() / 2) {
                    ControlDirection = 'r';

                }
                else if(Controller.getX() < getWidth() / 2) {
                    ControlDirection = 'l';
                }
                ControllerValue = ((Controller.getX() - getWidth()/2)/(getWidth()/2)/100)*100;
            }
             */
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(character.getFireTimer() != null && character.getFireTask() != null) {
                character.isBulletReady = false;
            }
            //Controller.setTouched(false);
            character.setTouched(false);
            bulletReady = false;
            //Controller.setX(getWidth()/2);
            //ControlDirection = 'n';
        }

        return true;
    }

    protected void update() {
        //Log.e("StageEnemySize", "" + GameScreen.stagesContainer.getStages().get(stage).getEnemies().size());
        if (isReady) {
            if (!GameScreen.isPause) {
                if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.BLUEFIRE && character.getCurrentMp() == character.getMaxMp()) {
                    BlueFire_overheat = false;
                    character.isBulletReady = true;
                }
                // Shoot the bullet.
                if(character.getEquippedBullet().getBulletType() != BulletEquipment.BulletType.DARK) {
                    if (character.getCurrentHp() > 0 && character.getCurrentHp() < character.getMaxHp()) {
                        if (character.hpRecoveryTask == null) {
                            character.hpRecoveryTask = new RecoveryTask(GameScreen, character, RecoveryTask.CharacterType.Player, RecoveryTask.RecoveryType.HP);
                            character.hpRecoveryTask.execute();
                        }
                        if (!character.hpRecoveryTask.isRunning) {
                            character.hpRecoveryTask = null;
                        }
                    }
                }
                //(Controller.isTouched() && bulletReady) || (Controller.isTouched() && GameScreen.isAutoShooting)
                if ((character.isTouched() && bulletReady)) {
                    if (character.getEquippedBullet().getBulletType() != BulletEquipment.BulletType.BLUEFIRE) {
                        if (character.getCurrentMp() > 0) {
                            if (character.getFireTimer() == null && character.getFireTask() == null) {
                                isReady = true;
                                character.setFireTask(new FireTask(GameScreen, character, FireTask.CharacterType.Player, BitmapFactory.decodeResource(getResources(), character.getEquippedBullet().bulletBitmapID), 8, 8, 5));
                                character.getFireTask().execute();
                                //GameScreen.addStageReward(1, GameBackStageUI.StageValueType.EXP);
                            }
                        }
                    } else if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.BLUEFIRE) {
                        if (!BlueFire_overheat) {
                            if (character.getCurrentMp() > 0) {
                                if (character.getFireTimer() == null && character.getFireTask() == null) {
                                    isReady = true;
                                    character.setFireTask(new FireTask(GameScreen, character, FireTask.CharacterType.Player, BitmapFactory.decodeResource(getResources(), character.getEquippedBullet().bulletBitmapID), 8, 8, 5));
                                    character.getFireTask().execute();
                                    //GameScreen.addStageReward(1, GameBackStageUI.StageValueType.EXP);
                                }
                            }
                        }else character.isBulletReady = false;
                    }
                } if (character.getEquippedBullet().getBulletType() != BulletEquipment.BulletType.BLUEFIRE) {
                        if (character.getCurrentMp() < character.getMaxMp()) {
                            if (GameScreen.isAutoShooting && character.getCurrentMp() >= character.getMaxMp() / 5) {
                                bulletReady = true;
                            }

                            if (character.mpRecoveryTask == null) {
                                character.mpRecoveryTask = new RecoveryTask(GameScreen, character, RecoveryTask.CharacterType.Player, RecoveryTask.RecoveryType.MP);
                                character.mpRecoveryTask.execute();
                            }
                            if (!character.mpRecoveryTask.isRunning) {
                                character.mpRecoveryTask = null;
                            }
                        }
                    } else if (character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.BLUEFIRE) {
                        if (character.getCurrentMp() == 0) {
                            BlueFire_overheat = true;
                        }
                        if (BlueFire_overheat) {
                            if (character.mpRecoveryTask == null) {
                                character.mpRecoveryTask = new RecoveryTask(GameScreen, character, RecoveryTask.CharacterType.Player, RecoveryTask.RecoveryType.MP);
                                character.mpRecoveryTask.execute();
                            }
                            if (!character.mpRecoveryTask.isRunning) {
                                character.mpRecoveryTask = null;
                            }
                        }
                    }
                    /*
                    switch (ControlDirection) {
                        case 'r':
                            character.setHSpeed(character.getMoveSpeed() * ControllerValue);
                            character.handleBounce(0, 0, getWidth(), getHeight());
                            break;
                        case 'l':
                            character.setHSpeed(character.getMoveSpeed() * ControllerValue);
                            character.handleBounce(0, 0, getWidth(), getHeight());
                            break;
                        default:
                            character.setHSpeed(0);
                            character.handleBounce(0, 0, getWidth(), getHeight());
                            break;
                    }
                    */
                character.handleBounce(0, 0, getWidth(), getHeight());
                    character.update();

                    for (int e = 0; e < SpawnedEnemy.size(); e++) {
                        if ((SpawnedEnemy.get(e).getCurrentHp() >= 0 && SpawnedEnemy.get(e).getCurrentHp() < SpawnedEnemy.get(e).getMaxHp()) ||(SpawnedEnemy.get(e).isUnDead && SpawnedEnemy.get(e).getCurrentHp() <= 0)) {
                            if(SpawnedEnemy.get(e).isUnDead != true || (SpawnedEnemy.get(e).isUnDead && SpawnedEnemy.get(e).isDead)) {
                                if (SpawnedEnemy.get(e).hpRecoveryTask == null) {
                                    SpawnedEnemy.get(e).hpRecoveryTask = new RecoveryTask(GameScreen, SpawnedEnemy.get(e), RecoveryTask.CharacterType.Enemy, RecoveryTask.RecoveryType.HP);
                                    SpawnedEnemy.get(e).hpRecoveryTask.execute();
                                    Log.e("Undead Recovery", SpawnedEnemy.get(e).getName() + ": " + SpawnedEnemy.get(e).getHpRecoveryValue() +  " / " + SpawnedEnemy.get(e).getHpRecoverySpeed() / 1000 + "s");
                                }
                                if (!SpawnedEnemy.get(e).hpRecoveryTask.isRunning) {
                                    SpawnedEnemy.get(e).hpRecoveryTask = null;
                                }
                            }
                        }

                        if (!SpawnedEnemy.get(e).isDead) {
                            if (SpawnedEnemy.get(e).getCurrentMp() > 0 && SpawnedEnemy.get(e).isBulletReady) {
                                if (SpawnedEnemy.get(e).getFireTimer() == null && SpawnedEnemy.get(e).getFireTask() == null) {
                                    SpawnedEnemy.get(e).setFireTask(new FireTask(GameScreen, SpawnedEnemy.get(e), FireTask.CharacterType.Enemy, BitmapFactory.decodeResource(getResources(), SpawnedEnemy.get(e).getEquippedBullet().bulletBitmapID), 8, 8, 5));
                                    SpawnedEnemy.get(e).getFireTask().execute();
                                    //GameScreen.addStageReward(1, GameBackStageUI.StageValueType.EXP);
                                }
                            } else if(SpawnedEnemy.get(e).getCurrentMp() <= 0)
                                SpawnedEnemy.get(e).setBulletReady(false);

                            if(SpawnedEnemy.get(e).getCurrentMp() >= SpawnedEnemy.get(e).getMaxMp())
                                SpawnedEnemy.get(e).setBulletReady(true);

                            if (!SpawnedEnemy.get(e).isBulletReady()) {
                                if (SpawnedEnemy.get(e).mpRecoveryTask == null) {
                                    SpawnedEnemy.get(e).mpRecoveryTask = new RecoveryTask(GameScreen, SpawnedEnemy.get(e), RecoveryTask.CharacterType.Enemy, RecoveryTask.RecoveryType.MP);
                                    SpawnedEnemy.get(e).mpRecoveryTask.execute();
                                }
                                if (!SpawnedEnemy.get(e).mpRecoveryTask.isRunning) {
                                    SpawnedEnemy.get(e).mpRecoveryTask = null;
                                }
                            }
                        }
                    }

                    // Checking the collision detection between player and the enemy's bullets.
                    for (int e = 0; e < SpawnedEnemy.size(); e++) {

                        if ((character.collideWith(SpawnedEnemy.get(e)) && SpawnedEnemy.get(e).collideTime <= 1) && !SpawnedEnemy.get(e).isDead) {
                            final int beforeValueHP = Math.round((float) character.getCurrentHp() / (float) character.getMaxHp() * 100);
                            character.getHurt(SpawnedEnemy.get(e).getRandomDamage());
                            GameScreen.runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             GameScreen.updateCharacterStatus(beforeValueHP, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                                                         }
                                                     });
                            //Log.e("After Get Hurt","" +  character.getCurrentHp());
                        }

                        for (int i = 0; i < SpawnedEnemy.get(e).getFiredBullets().size(); i++) {
                            if (SpawnedEnemy.get(e).getFiredBullets().get(i) != null) {
                                SpawnedEnemy.get(e).getFiredBullets().get(i).setBounce(true);
                            }

                            if (character.collideWith(SpawnedEnemy.get(e).getFiredBullets().get(i)) && !SpawnedEnemy.get(e).isDead) {
                                //this.surfaceDestroyed(this.getHolder());
                                //Handle the action when character collide with obstacle.
                                final int beforeValueHP = Math.round((float) character.getCurrentHp() / (float) character.getMaxHp() * 100);
                                character.getHurt(SpawnedEnemy.get(e).getRandomDamage());
                                GameScreen.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        GameScreen.updateCharacterStatus(beforeValueHP, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                                    }});
                                //Log.e("After Get Hurt","" +  character.getCurrentHp());
                                SpawnedEnemy.get(e).getFiredBullets().remove(SpawnedEnemy.get(e).getFiredBullets().get(i));
                            }
                            if (SpawnedEnemy.get(e).getFiredBullets().get(i).getY() >= getHeight()) {
                                SpawnedEnemy.get(e).getFiredBullets().remove(SpawnedEnemy.get(e).getFiredBullets().get(i));
                                //Log.e("BulletSize", "" + character.getFiredBullets().size());
                            } else SpawnedEnemy.get(e).getFiredBullets().get(i).update();
                        }
                    }


                    // Updating the player's bullet.
                    for (int i = 0; i < character.getFiredBullets().size(); i++) {
                        if (character.getFiredBullets().get(i) != null) {
                            character.getFiredBullets().get(i).setBounce(true);
                            //Log.e("Length", "" + SpawnedEnemy.size());
                            for (int e = 0; e < SpawnedEnemy.size(); e++) {
                                if (SpawnedEnemy.get(e).collideWith(character.getFiredBullets().get(i)) && !SpawnedEnemy.get(e).isDead) {
                                    character.getFiredBullets().remove(character.getFiredBullets().get(i));
                                    final Enemy getHurtEnemy = SpawnedEnemy.get(e);
                                    final int beforeValueHP = Math.round((float) getHurtEnemy.getCurrentHp() / (float) getHurtEnemy.getMaxHp() * 100);
                                    GameScreen.sp.play(GameScreen.soundIds[0], .5f, .5f, 1, 0, 1.0f);
                                    //Log.e("Value", "" + beforeValueHP);
                                    if(character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.FOREST){
                                        if(rand.nextDouble() >= 0.5) {
                                            getHurtEnemy.getHurt((GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv / 4));
                                        } else if(rand.nextDouble() <= 0.5){
                                            if(rand.nextDouble() <= 0.5){
                                                Log.e("Multi Value", "" + (1.09 + (0.1 * GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv)));
                                                Log.e("Damage", "" + (int)((double)character.getRandomDamage() * (1.09 + (0.01 * GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv))));
                                                getHurtEnemy.getHurt((int)((double)character.getRandomDamage() * (1.09 + (0.01 * GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv))));
                                            }
                                            else  getHurtEnemy.getHurt((int)((double)character.getRandomDamage() * (0.75)));
                                        }

                                    } else if(character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.DARK) {
                                        //Log.e("Vampire:", "" + getHurtEnemy.getCurrentHp() * 0.01);
                                        final int beforePlayerValueHP = Math.round((float) character.getCurrentHp() / (float) character.getMaxHp() * 100);
                                        if(getHurtEnemy.getCurrentHp() * 0.01 > 1) {
                                            character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Plus, (int) (double) (getHurtEnemy.getCurrentHp() * 0.01));
                                        } else character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Plus, 1);
                                        getHurtEnemy.getHurt((int) ((double) character.getRandomDamage() * (0.5 + (character.getCurrentHp() * 100 / character.getMaxHp()) / 100) + getHurtEnemy.getCurrentHp() * 0.01));
                                        GameScreen.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GameScreen.updateCharacterStatus(beforePlayerValueHP, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                                            }});
                                    } else if(character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.FIREBALL){
                                        int bonusDamage = 0;
                                        attackTime++;
                                        if(character.getEquippedBullet().lv <= 11){
                                            if(attackTime >= (21 - character.getEquippedBullet().lv)){
                                                bonusDamage = (attackTime / (21 - character.getEquippedBullet().lv));
                                            }
                                        } else {
                                            if(attackTime >= 10){
                                                bonusDamage = (attackTime / 10);
                                            }
                                        }
                                        if(bonusDamage >= (character.getEquippedBullet().lv * 2)){
                                            bonusDamage = (character.getEquippedBullet().lv * 2);
                                        }
                                        getHurtEnemy.getHurt(character.getRandomDamage() + bonusDamage);
                                        //Log.e("Attack Time", "" + attackTime);
                                        //Log.e("Bonus Dam", "" + bonusDamage);
                                    } else
                                        getHurtEnemy.getHurt(character.getRandomDamage());
                                    GameScreen.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            GameScreen.updateCharacterStatus(beforeValueHP, GameBackStageUI.UpdateStatusType.EnemyHP, getHurtEnemy);
                                        }});
                                    if(rand.nextDouble() <= 0.15 && character.getEquippedBullet().getBulletType() == BulletEquipment.BulletType.FOREST){
                                        Log.e("Poison Start", "Poising Success");
                                        if(SpawnedEnemy.get(e).getPoisonTask() == null){
                                            SpawnedEnemy.get(e).setPoisonTask(new PoisonTask(GameScreen,SpawnedEnemy.get(e), PoisonTask.CharacterType.Enemy, PoisonTask.PoisonType.HP,((int)(double)( 1 + GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv / 2))));
                                            SpawnedEnemy.get(e).getPoisonTask().execute();
                                        }
                                        else if(SpawnedEnemy.get(e).getPoisonTask() != null && SpawnedEnemy.get(e).getPoisonTask().isRunning){
                                            if(SpawnedEnemy.get(e).getPoisonTask().Value < ((int)(double)(1 + GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv / 2)) * 5)
                                            SpawnedEnemy.get(e).getPoisonTask().addValue(((int)(double)(1 + GameScreen.bulletEquipmentContainer.bulletEquipments[2].lv / 2)));
                                            Log.e("Poison Start", "Stack Poising");
                                        }
                                    }
                                    if(SpawnedEnemy.get(e).getPoisonTask() != null){
                                        if (!SpawnedEnemy.get(e).getPoisonTask().isRunning) {
                                            SpawnedEnemy.get(e).setPoisonTask(null);
                                        }
                                    }

                                }
                            }

                            if (character.getFiredBullets().get(i).getY() <= 0) {
                                character.getFiredBullets().remove(character.getFiredBullets().get(i));
                                //Log.e("BulletSize", "" + character.getFiredBullets().size());
                            } else character.getFiredBullets().get(i).update();
                        }
                    }
                    //for(Bullet bullet : character.getFiredBullets()){

                    //}

                    for (int e = 0; e < SpawnedEnemy.size(); e++) {
                        //Log.e("Check Collilde with character" + e,"" + character.collideWith(SpawnedEnemy.get(e)) + " / " +SpawnedEnemy.get(e).getCollideWithCharacter());
                        SpawnedEnemy.get(e).setBounce(true);
                        SpawnedEnemy.get(e).handleBounce(0, 0, getWidth(), getHeight());
                        if(!SpawnedEnemy.get(e).isUnDead || (SpawnedEnemy.get(e).isUnDead && !SpawnedEnemy.get(e).isDead))
                            SpawnedEnemy.get(e).update();
                        //Log.e("SpawnedEnemy Current HP", "" + e + " / " + SpawnedEnemy.get(e).getCurrentHp());
                        if(SpawnedEnemy.get(e).getCurrentHp() <= 0) {
                            //Log.e("SpawnedEnemy Current HP", "" + e + " / " + SpawnedEnemy.get(e).getCurrentHp());
                            SpawnedEnemy.get(e).isDead = true;
                            if(SpawnedEnemy.get(e).getPoisonTask() != null) {
                                if (SpawnedEnemy.get(e).getPoisonTask().isRunning)
                                    SpawnedEnemy.get(e).getPoisonTask().isRunning = false;
                            }
                            if(SpawnedEnemy.get(e).isUnDead){
                                SpawnedEnemy.get(e).setY(GameScreen.gameMainScreenContainer.getHeight() / 12);
                            }
                        }
                    }
                }
            }
            // Check the character is / not dead.
        if (character.isDead() && !isLose) {
            isLose = true;
            returnToMenu();
        }
        }

    protected void render(Canvas canvas) {
        if(!GameScreen.isPause) {
            //Controller.setY((float)(getHeight()/1.05));
            // fills the canvas with white color
            GM.mCanvas = canvas;
            canvas.drawColor(Color.WHITE);
            //Paint paint = new Paint();
            //paint.setColor(Color.BLACK);
            canvas.drawBitmap(backGround, 0, 0, null);

            //sprites[0].draw(canvas);

            //Controller.draw(canvas);

            for (int e = 0; e < SpawnedEnemy.size(); e++) {
                //Log.d("Enemy " + e, " Created");
                if ((!SpawnedEnemy.get(e).isDead && SpawnedEnemy.get(e) != null) || (SpawnedEnemy.get(e).isUnDead && SpawnedEnemy.get(e) != null)) {
                    SpawnedEnemy.get(e).draw(canvas);
                } else {
                    if (!SpawnedEnemy.get(e).isRewarded()) {
                        final Enemy DeadEnemy = SpawnedEnemy.get(e);
                        DeadEnemy.getFiredBullets().removeAll(DeadEnemy.getFiredBullets());
                        if (GameScreen.skillsContainer.skills[5].lv > 0) {
                            final int beforePlayerValueHP = Math.round((float) character.getCurrentHp() / (float) character.getMaxHp() * 100);
                            final int beforePlayerValueMP = Math.round((float) character.getCurrentMp() / (float) character.getMaxMp() * 100);
                            character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Plus, (int) GameScreen.skillsContainer.skills[5].totalValue);
                            character.adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Plus, (int) GameScreen.skillsContainer.skills[5].totalValue);
                            GameScreen.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GameScreen.updateCharacterStatus(beforePlayerValueHP, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                                    GameScreen.updateCharacterStatus(beforePlayerValueMP, GameBackStageUI.UpdateStatusType.CurrentMP, character);
                                }
                            });
                        }
                        DeadEnemy.setRewarded(true);
                        GameScreen.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GameScreen.addStageReward(DeadEnemy.ExpReward, GameBackStageUI.StageValueType.EXP);
                                GameScreen.addStageReward(DeadEnemy.MoneyReward, GameBackStageUI.StageValueType.MONEY);
                            }
                        });
                        stagesContainer.getStages().get(stage).getEnemies().remove(DeadEnemy);
                    }
                }
            }

            for (int e = 0; e < SpawnedEnemy.size(); e++) {
                for (int i = 0; i < SpawnedEnemy.get(e).getFiredBullets().size(); i++) {
                    if (SpawnedEnemy.get(e).getFiredBullets().get(i) != null) {
                        SpawnedEnemy.get(e).getFiredBullets().get(i).draw(canvas);
                    }
                }
            }

            //Draw the player's bullet on the surface view.
            for (int i = 0; i < character.getFiredBullets().size(); i++) {
                if (character.getFiredBullets().get(i) != null) {
                    character.getFiredBullets().get(i).draw(canvas);
                }
            }
            character.draw(canvas);

            // If stage clear, The stage will add one and reload the enemy for next stage.
            // If all stage clear, the stage view will close and return to menu, the total reward will show after return.
            if (stagesContainer.checkNull(stage)) {
                Log.e("Clear", "Touched");
                stage += 1;
                if ((stage + 1) > stagesContainer.maxStage) {
                    returnToMenu();
                }
                initializeEnemy();
            }
        }
    }

    //Program run at any change of the panel size
    public void onSizeChanged(int w, int h, int oldW, int oldH){
        panelHeight = h;
        panelWidth = w;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        GameScreen.isPause = false;
    }

    //Program run at creating the surface view
    public void surfaceCreated(SurfaceHolder holder){
        drawingThread.setRunning(true);
        if (drawingThread.getState() == Thread.State.NEW) {
            drawingThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        GameScreen.isPause = true;
        Runtime.getRuntime().gc();
    }

    //region The Method for handle the gameover screen.
    private void returnToMenu(){
        drawingThread.setRunning(false);
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Setting of Reward Screen
                new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        for (int i = 0; i < character.getFiredBullets().size(); i++) {
                            if (character.getFiredBullets().get(i) != null) {
                                character.getFiredBullets().remove(character.getFiredBullets().get(i));
                                //Log.e("BulletSize", "" + character.getFiredBullets().size());
                            }
                        }
                        //Log.e("Current Update","" +  character.getCurrentHp());
                    }

                    @Override
                    public void onFinish() {
                        //Log.e("Before Reset","" +  character.getCurrentHp());
                        if (character.getFireTimer() != null){
                        character.getFireTimer().cancel();
                        character.getFireTimer().purge();
                    }
                        character.setFireTimer(null);
                        character.setFireTask(null);
                        character.hpRecoveryTask = null;
                        character.mpRecoveryTask = null;
                        GameScreen.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GameScreen.gameMainScreenContainer.removeAllViews();
                                GameScreen.uibottomContainer.setVisibility(View.VISIBLE);
                                GameScreen.stageUI.setVisibility(View.GONE);
                                GameScreen.charStatusUI.setVisibility(View.GONE);
                                GameScreen.menuTopUI.setVisibility(View.VISIBLE);
                                GameScreen.showStageReward(stage+1);
                            }
                        });
                        SpawnedEnemy = null;
                        stagesContainer = null;
                        backGround.recycle();
                        //character.RecycleBitmap();
                        //Log.e("After Reset","" +  character.getCurrentHp());
                        cancel();
                        surfaceDestroyed(getHolder());

                        try {
                            NormalBGM.stop();
                            NormalBGM.release();
                        } catch (Exception e) {

                        }
                        try {
                            NormalBossBGM.stop();
                            NormalBossBGM.release();
                        } catch (Exception e) {

                        }
                        try {
                            BossChallengeBGM.stop();
                            BossChallengeBGM.release();
                        } catch (Exception e) {

                        }


                        GameScreen.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GameScreen.updateUIStatus(GameBackStageUI.StageValueType.MONEY);
                                GameScreen.updateCharacterStatus(0, GameBackStageUI.UpdateStatusType.CurrentHP,character);
                                GameScreen.updateCharacterStatus(0, GameBackStageUI.UpdateStatusType.CurrentMP,character);
                                GameScreen.mediaPlayer = MediaPlayer.create(GameScreen,R.raw.mainmenubgm);
                                GameScreen.mediaPlayer.start();
                                GameScreen.mediaPlayer.setLooping(true);
                                GameScreen.sp.release();
                            }});
                        Runtime.getRuntime().gc();
                    }
                }.start();
            }
        });
    }
    //endregion

    private void initializeEnemy(){
        // This method will be called by Initialize() [A method for initialization of whole GamePanel.] and clear the current stage.
        // Initialize all anemies data in current stage.
        // set position randomly when the game mode is ENDLESSCHALLENGE.
        // region Initialize the container class of stages.
        if(stagesContainer == null) {
            switch (GameScreen.gameMode) {
                case TIMEATTACK:

                    break;
                case BOSSCHALLENGE:
                    // Set Stage to the selected Boss Stage
                    stagesContainer = new StagesContainer(GameScreen, 1);
                    stagesContainer.setBossStage(GameScreen.bossChallengeType);
                    break;
                case ENDLESSCHALLENGE:
                    stagesContainer = new StagesContainer(GameScreen, 100);
                    break;
            }
        }

        // region Setting of the BGM
        if(!stagesContainer.getStages().get(stage).isBossStage() && GameScreen.gameMode == GameBackStageUI.GameMode.ENDLESSCHALLENGE){
            //NormalBGM.release();
            if(NormalBGM == null) {
                NormalBGM = new MediaPlayer();
                NormalBGM = MediaPlayer.create(GameScreen, R.raw.battlebgm1);
            }
            if(NormalBossBGM == null) {
                NormalBossBGM = new MediaPlayer();
                NormalBossBGM = MediaPlayer.create(GameScreen, R.raw.normalstagebossbgm);
            }
            if(BossChallengeBGM == null) {
                BossChallengeBGM = new MediaPlayer();
                BossChallengeBGM = MediaPlayer.create(GameScreen, R.raw.bossstagebgm);
            }
            NormalBGM.start();
            NormalBGM.setLooping(true);
            NormalBossBGM.start();
            NormalBossBGM.setLooping(true);
            BossChallengeBGM.start();
            BossChallengeBGM.setLooping(true);
            //Release and stop another MediaPlayer
            if(NormalBossBGM.isPlaying()) {
                NormalBossBGM.stop();
                NormalBossBGM.release();
                NormalBossBGM = null;
            }
            if(BossChallengeBGM.isPlaying()) {
                BossChallengeBGM.stop();
                BossChallengeBGM.release();
                BossChallengeBGM = null;
            }
        }else if(stagesContainer.getStages().get(stage).isBossStage() && GameScreen.gameMode == GameBackStageUI.GameMode.ENDLESSCHALLENGE){
            //NormalBossBGM.release();
            if(NormalBGM == null) {
                NormalBGM = new MediaPlayer();
                NormalBGM = MediaPlayer.create(GameScreen, R.raw.battlebgm1);
            }
            if(NormalBossBGM == null) {
                NormalBossBGM = new MediaPlayer();
                NormalBossBGM = MediaPlayer.create(GameScreen, R.raw.normalstagebossbgm);
            }
            if(BossChallengeBGM == null) {
                BossChallengeBGM = new MediaPlayer();
                BossChallengeBGM = MediaPlayer.create(GameScreen, R.raw.bossstagebgm);
            }
            NormalBGM.start();
            NormalBGM.setLooping(true);
            NormalBossBGM.start();
            NormalBossBGM.setLooping(true);
            BossChallengeBGM.start();
            BossChallengeBGM.setLooping(true);
            //Release and stop another MediaPlayer
            if(NormalBGM.isPlaying()) {
                NormalBGM.stop();
                NormalBGM.release();
                NormalBGM = null;
            }
            if(BossChallengeBGM.isPlaying()) {
                BossChallengeBGM.stop();
                BossChallengeBGM.release();
                BossChallengeBGM = null;
            }
        }else if(GameScreen.gameMode == GameBackStageUI.GameMode.BOSSCHALLENGE){
            //BossChallengeBGM.release();
            if(NormalBGM == null) {
                NormalBGM = new MediaPlayer();
                NormalBGM = MediaPlayer.create(GameScreen, R.raw.battlebgm1);
            }
            if(NormalBossBGM == null) {
                NormalBossBGM = new MediaPlayer();
                NormalBossBGM = MediaPlayer.create(GameScreen, R.raw.normalstagebossbgm);
            }
            if(BossChallengeBGM == null) {
                BossChallengeBGM = new MediaPlayer();
                BossChallengeBGM = MediaPlayer.create(GameScreen, R.raw.bossstagebgm);
            }
            NormalBGM.start();
            NormalBGM.setLooping(true);
            NormalBossBGM.start();
            NormalBossBGM.setLooping(true);
            BossChallengeBGM.start();
            BossChallengeBGM.setLooping(true);
            //Release and stop another MediaPlayer
            if(NormalBGM.isPlaying()) {
                NormalBGM.stop();
                NormalBGM.release();
                NormalBGM = null;
            }
            if(NormalBossBGM.isPlaying()) {
                NormalBossBGM.stop();
                NormalBossBGM.release();
                NormalBossBGM = null;
            }
        }
        // endregion

        character.getFiredBullets().removeAll(character.getFiredBullets()); // remove all launched bullets which shoot by player character.
        SpawnedEnemy = stagesContainer.getStages().get(stage).getEnemies();
        Random random = new Random();
        // endregion
        // region Enemies Initialization.

        //SpawnedEnemy = new Enemy[stagesContainer.getStages().get(stage).getEnemies().size()];

        for(int i = 0; i < SpawnedEnemy.size();i++){
            //Log.e("StageEnemy", "" + GameScreen.stagesContainer.getStages().get(stage).getEnemies().get(i));
            //if(SpawnedEnemy[i] == null) {
            if(GameScreen.gameMode != GameBackStageUI.GameMode.BOSSCHALLENGE) {
                SpawnedEnemy.get(i).setX( SpawnedEnemy.get(i).getSpriteWidth() / 2);
                SpawnedEnemy.get(i).setY( SpawnedEnemy.get(i).getSpriteHeight() / 2);
                if (! SpawnedEnemy.get(i).isBoss) {
                    // Normal Enemy Position Setting
                    SpawnedEnemy.get(i).setX(SpawnedEnemy.get(i).getSpriteWidth() / 2 + random.nextInt(getWidth() / 3));
                    SpawnedEnemy.get(i).setY(SpawnedEnemy.get(i).getSpriteHeight() / 2 + random.nextInt(getHeight() / 3));
                    SpawnedEnemy.get(i).setHSpeed(SpawnedEnemy.get(i).getMoveSpeed() * (0.1 + random.nextDouble()));
                } else {
                    // Boss Enemy Position Setting
                    SpawnedEnemy.get(i).setX(SpawnedEnemy.get(i).getSpriteWidth() / 2);
                    SpawnedEnemy.get(i).setY(SpawnedEnemy.get(i).getSpriteHeight() / 8);
                    SpawnedEnemy.get(i).setHSpeed(SpawnedEnemy.get(i).getMoveSpeed() * (0.1 + random.nextDouble()));
                }
                if (SpawnedEnemy.get(i).isUnDead && !SpawnedEnemy.get(i).isBoss) {
                    SpawnedEnemy.get(i).setX(SpawnedEnemy.get(i).getSpriteWidth() / 2 + random.nextInt(getWidth() / 3));
                    SpawnedEnemy.get(i).setY(SpawnedEnemy.get(i).getSpriteHeight() / 12);
                    SpawnedEnemy.get(i).setVSpeed(0 + SpawnedEnemy.get(i).getMoveSpeed() * (0.1 + random.nextDouble()));
                }
            }
            //Log.e("SpawenedEnemy" + i,  SpawnedEnemy.get(i) + " is Ready" + ", isUndead: " + SpawnedEnemy.get(i).isUnDead);
            Runtime.getRuntime().gc();
        }
        // endregion


        // region Set Stage Text for showing the current stage and update the enemy status UI.
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(GameScreen.gameMode == GameBackStageUI.GameMode.BOSSCHALLENGE){
                    GameScreen.CurrentStageText.setText("Challenge\n" +  SpawnedEnemy.get(0).getName());
                } else {
                    GameScreen.CurrentStageText.setText(GameScreen.endlessDifficulty.toString() + "\tSTAGE\n" + (stage + 1));
                }
            }
        });
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameScreen.updateCharacterStatus(0, GameBackStageUI.UpdateStatusType.EnemyHP, SpawnedEnemy.get(0));
                GameScreen.StageButtonLayout.setVisibility(VISIBLE);
            }});

        // endregion
    }


    private void Initialize(){
        // region reset stage elements and the character position when the new game start.
        stage = 0;
        attackTime = 0;
        isReady = false;
        GameScreen.isPause = false;
        character = GameScreen.character;
        character.GP = this;
        character.setX(getWidth()/2);
        character.setY(getHeight()/1.25);
        character.bulletData = new Bullet(BitmapFactory.decodeResource(getResources(), character.getEquippedBullet().bulletBitmapID), 8, 8, 5, 0, 0, 0, -1);
        // Initialize the Background.
        backGround = BitmapFactory.decodeResource(getResources(),R.drawable.desert);
        float scale = (float)backGround.getHeight()/(float)getHeight();
        int newWidth = Math.round(backGround.getWidth()/scale);
        int newHeight = Math.round(backGround.getHeight()/scale);
        backGround = Bitmap.createScaledBitmap(backGround, newWidth, newHeight, true);

        //Controller = new Sprite(BitmapFactory.decodeResource(getResources(),
        //        R.drawable.finger_icon),getWidth()/2,(float)(getHeight()/1.05));

        character.setBounce(true);
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameScreen.updateCharacterStatus(0, GameBackStageUI.UpdateStatusType.CurrentHP,character);
                GameScreen.updateCharacterStatus(0, GameBackStageUI.UpdateStatusType.CurrentMP,character);
            }
        });

        initializeEnemy(); // Call the method for initialize the current stage enemy;
        Log.d("Status" , "InitGame End.");
        Runtime.getRuntime().gc();
        // endregion
    }

    private void LoadingInProgress(){
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameScreen.gameMainScreenContainer.setVisibility(INVISIBLE);

            }
        });
    }

    private void InitializeFinish(){
        GameScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameScreen.gameMainScreenContainer.setVisibility(VISIBLE);

            }
        });
    }


    Runnable doBgRunnable = new Runnable() {
        @Override
        public void run() {
            Initialize();
        }
    };
    Runnable doInProgress = new Runnable() {
        @Override
        public void run() {
            LoadingInProgress();
        }
    };
    Runnable doOnPostTask = new Runnable() {
        @Override
        public void run() {
            InitializeFinish();
        }
    };

}