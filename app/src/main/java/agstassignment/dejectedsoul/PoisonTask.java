package agstassignment.dejectedsoul;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

// A AsyncTask for handle the character recovery of the MP.
public class PoisonTask extends AsyncTask<String,Double,String> {
    public enum PoisonType{
        HP,
        MP
    }
    public enum CharacterType{
        Player,
        Enemy
    }
    CharacterType characterType;
    //final HandlerThread handlerThread = new HandlerThread("Recovery");
    int Time;
    Character character;
    GameBackStageUI GameScreen;
    PoisonType poisonType;
    int Value;
    boolean isRunning;
    //final Handler PoisonHandler;

    public PoisonTask(GameBackStageUI GameScreen, Character character, CharacterType characterType, PoisonType poisonType,int Value) {
        //Log.e("Recovery" + recoveryType.toString(), " Created!");
        this.GameScreen = GameScreen;
        this.character = character;
        this.poisonType = poisonType;
        this.characterType = characterType;
        this.Value = Value;
        isRunning = true;
        //if (handlerThread.getState() == Thread.State.NEW && !handlerThread.isAlive())
         //   handlerThread.start();
       // PoisonHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected String doInBackground(String... values) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                character.setPoisonTimer(new Timer());
                character.getPoisonTimer().schedule(poisonTimerTask,0,1000);
            }
        };
        runnable.run();
        return "Done";
    }

    @Override
    protected void onProgressUpdate(Double... values) {

    }

    @Override
    protected void onPostExecute(String result) {

    }

    public void setValue(int value) {
        Value = value;
    }

    public void addValue(int addValue){
        Value += addValue;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    TimerTask poisonTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(!character.isDead)
                poisoning();
            else {
                character.getPoisonTimer().cancel();
                character.getPoisonTimer().purge();
                character.setPoisonTimer(null);
            }
        }
    };

    private void poisoning() {
        isRunning = true;
        Time++;
        Log.e("Time" , "" + Time + ", Damage: " + Value);
        if (poisonType == PoisonType.MP) {
            final int beforeValue = Math.round((float) character.getCurrentMp() / (float) character.getMaxMp() * 100);
            character.adjustValueInt(Character.AdjustType.CurrentMP, Character.CalculationMethod.Minus, Value);
            //Log.e("MP: " , "" + character.getCurrentMp() + " / " + character.getMaxMp() );
            if (characterType == CharacterType.Player) {
                GameScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.CurrentMP, character);
                    }
                });
            }
        }
        else if(poisonType == PoisonType.HP){
            final int beforeValue = Math.round((float)character.getCurrentHp() / (float)character.getMaxHp()*100);
            character.adjustValueInt(Character.AdjustType.CurrentHP, Character.CalculationMethod.Minus, Value);
            if(characterType == CharacterType.Player) {
                GameScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.CurrentHP, character);
                    }
                });
            }
            else if(characterType == CharacterType.Enemy) {
                GameScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.updateCharacterStatus(beforeValue, GameBackStageUI.UpdateStatusType.EnemyHP, character);
                    }
                });
            }
        }

        if(Time >= 5 || character.getCurrentHp() <= 0){
            if(character.getPoisonTimer() != null) {
                character.getPoisonTimer().cancel();
                character.getPoisonTimer().purge();
            }
            character.setPoisonTimer(null);
            character.setPoisonTask(null);
            Log.e("Poison End" , "End Task.");
        }
    }
}