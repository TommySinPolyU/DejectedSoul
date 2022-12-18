package agstassignment.dejectedsoul;

import java.util.ArrayList;
import java.util.List;

public class Stage {
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private int difficulty, stageId;
    private boolean isClear, isBossStage;

    public Stage(int stageId, int difficulty, boolean isBossStage){
        this.difficulty = difficulty;
        this.stageId = stageId;
        this.isBossStage = isBossStage;
    }

    public boolean isBossStage() {
        return isBossStage;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getStageId() {
        return stageId;
    }

    public void addEnemy(Enemy enemy){
        this.enemies.add(enemy);
    }

    public List<Enemy> getEnemies(){
        return enemies;
    }

    public void removeAllEnemies(){
        enemies.removeAll(enemies);
    }

    public void removeEnemy(int index){
        enemies.remove(index);
    }

}
