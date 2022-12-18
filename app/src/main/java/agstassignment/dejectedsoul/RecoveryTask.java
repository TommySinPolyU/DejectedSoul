package agstassignment.dejectedsoul;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

// A AsyncTask for handle the character recovery of the MP.
public class RecoveryTask extends AsyncTask<String,Double,String> {
    public enum RecoveryType{
        HP,
        MP
    }
    public enum CharacterType{
        Player,
        Enemy
    }
    CharacterType characterType;
    //final HandlerThread handlerThread = new HandlerThread("Recovery");
    //final Handler RecoveryHandler;
    Timer timer;
    Character character;
    GameBackStageUI GameScreen; // Get the UI layout for update the status of character.
    RecoveryType recoveryType;
    boolean isRunning;

    public RecoveryTask(GameBackStageUI GameScreen, Character character, CharacterType characterType, RecoveryType recoveryType) {
        //Log.e("Recovery" + recoveryType.toString(), " Created!");
        this.GameScreen = GameScreen;
        this.character = character;
        this.recoveryType = recoveryType;
        this.characterType = characterType;
        isRunning = true;
       // if (handlerThread.getState() == Thread.State.NEW && !handlerThread.isAlive())
       //     handlerThread.start();
       // RecoveryHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected String doInBackground(String... values) {
        if(timer == null) {
            timer = new Timer();
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    if (recoveryType == RecoveryType.MP) {
                        timer.schedule(recoveryTimerTask, (long) character.getMpRecoverySpeed(), (long) character.getMpRecoverySpeed());
                    } else if (recoveryType == RecoveryType.HP) {
                        timer.schedule(recoveryTimerTask, (long) character.getHpRecoverySpeed(), (long) character.getHpRecoverySpeed());
                    }
            }
        };
        if (timer != null) {
            runnable.run();
        }
        return "Done";
    }

    @Override
    protected void onProgressUpdate(Double... values) {

    }

    @Override
    protected void onPostExecute(String result) {

    }

    TimerTask recoveryTimerTask = new TimerTask() {
        @Override
        public void run() {
            if((!character.isDead || (character.isDead && character.isUnDead)) && !GameScreen.isPause)
                recovery();
            else {
                timer.purge();
                timer.cancel();
                timer = null;
                character.hpRecoveryTask = null;
                character.mpRecoveryTask = null;
            }
        }
    };

    private void recovery() {
        if (recoveryType == RecoveryType.MP) {
            final int beforeValue = Math.round((float) character.getCurrentMp() / (float) character.getMaxMp() * 100);
            character.adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Plus, character.getMpRecoveryValue());
            //Log.e("MP: " , "" + character.getCurrentMp() + " / " + character.getMaxMp() );
            if (characterType == CharacterType.Player) {
                GameScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.CurrentMP, character);
                    }
                });
            }
            if(character.getCurrentMp() >= character.getMaxMp()){
                timer.cancel();
                timer.purge();
                isRunning = false;
            }
        } else if(recoveryType == RecoveryType.HP){
            final int beforeValue = Math.round((float)character.getCurrentHp() / (float)character.getMaxHp()*100);
            character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Plus, character.getHpRecoveryValue());
            if(characterType == CharacterType.Player) {
                GameScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                    }
                });
            }
            //Log.e("Current HP", character.getName() + ": " + character.getCurrentHp());
            if(character.getCurrentHp() >= character.getMaxHp()){
                if(character.isUnDead)
                    character.isDead = false;
                timer.cancel();
                timer.purge();
                isRunning = false;
            }
        }
    }
}