package agstassignment.dejectedsoul;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

//The AsyncTask for shooting the bullet.
public class FireTask extends AsyncTask<String,Double,String> {
    public enum CharacterType {
        Player,
        Enemy
    }

    CharacterType characterType;
    //final HandlerThread handlerThread = new HandlerThread("Fire");
    public GameBackStageUI GameScreen;
    Character FTcharacter;
    Bitmap bulletBitmap;
    int bulletnoOfFrame, bulletSpriteHeight, bulletnoOfTick;
    public boolean isRunning;
    float scaleTo;

    public FireTask(GameBackStageUI GameScreen, Character character, CharacterType characterType, Bitmap bulletBitmap, int bulletnoOfframe, int bulletspriteheight, int bulletnoOftick) {
        this.GameScreen = GameScreen;
        this.FTcharacter = character;
        this.bulletBitmap = bulletBitmap;
        this.bulletnoOfFrame = bulletnoOfframe;
        this.bulletSpriteHeight = bulletspriteheight;
        this.bulletnoOfTick = bulletnoOftick;
        //this.scaleTo = ScaleTo;
        this.characterType = characterType;
    }
    @Override
    protected String doInBackground(String... values) {
        //final  Handler fireHandler = new Handler(handlerThread.getLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Log.e("Fire Timer", "Set");
                isRunning = true;
                FTcharacter.setFireTimer(new Timer());
                FTcharacter.getFireTimer().schedule(shootTimerTask, (long) FTcharacter.getAtkSpeed(), (long) FTcharacter.getAtkSpeed());
            }
        };
        runnable.run();
        //fireHandler.post(runnable);
        return "Done";
    }

    @Override
    protected void onProgressUpdate(Double... values) {

    }

    @Override
    protected void onPostExecute(String result) {

    }

    TimerTask shootTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (FTcharacter.getFireTimer() != null && FTcharacter.getFireTask() != null) {
                if (FTcharacter.getCurrentMp() > 0 && FTcharacter.isBulletReady) {
                    if(!FTcharacter.isDead && !GameScreen.isPause) {
                        shootBullet();
                    }else {
                        FTcharacter.getFireTimer().cancel();
                        FTcharacter.getFireTimer().purge();
                        FTcharacter.setFireTask(null);
                        FTcharacter.setFireTimer(null);
                    }
                }
            }
        }
    };


        private void shootBullet() {
            if (!isRunning) {
                FTcharacter.getFireTimer().cancel();
                FTcharacter.getFireTimer().purge();
                FTcharacter.setFireTask(null);
                FTcharacter.setFireTimer(null);
            }
            //Bitmap ScaleBitmap = Bitmap.createScaledBitmap(bulletBitmap, Math.round(bulletBitmap.getWidth()), Math.round(bulletBitmap.getHeight()), false);
            FTcharacter.fire(bulletBitmap, bulletnoOfFrame, bulletSpriteHeight, bulletnoOfTick); // call the character fire method to shoot the bullet and add the bullet in array.
            bulletBitmap.recycle(); // recycle the bitmap to release the memory.
            if (FTcharacter.getMaxMp() > 0) {
                final int beforeValue = FTcharacter.getCurrentMp();
                //Log.e("MP: " , "" + FTcharacter.getCurrentMp() + " / " + FTcharacter.getMaxMp() );
                if (characterType == CharacterType.Player) {
                    GameScreen.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.CurrentMP, FTcharacter);
                        }
                    });
                }
            }
        }
    }


