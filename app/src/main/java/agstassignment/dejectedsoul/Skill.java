package agstassignment.dejectedsoul;

public class Skill {
    public enum SkillType{
        HPUP,
        MPUP,
        HPRECOVERYUP,
        MPRECOVERYUP,
        MOVESPEEDUP,
        SOULEATER
    }
    public double totalValue;
    public int lv;
    public SkillType skillType;

    public Skill(SkillType skillType){
        this.skillType = skillType;
        lv = 0;
        calculateTotalValue(skillType);
    }

    public void calculateTotalValue(SkillType skillType){
        switch (skillType){
            case HPUP:
                totalValue = lv * 5;
                break;
            case MPUP:
                totalValue = lv * 3;
                break;
            case HPRECOVERYUP:
                totalValue = lv * 0.25;
                break;
            case MPRECOVERYUP:
                totalValue = lv * 0.25;
                break;
            case MOVESPEEDUP:
                totalValue = lv * 0.25;
                break;
            case SOULEATER:
                //if(lv %4 == 0 ) {
                totalValue = Math.floor(1 + lv * 0.25);
                //}
                break;
        }
    }
}
