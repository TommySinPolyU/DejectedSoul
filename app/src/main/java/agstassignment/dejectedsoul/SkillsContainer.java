package agstassignment.dejectedsoul;

public class SkillsContainer {
    Skill[] skills;
    int skillPoint;

    public SkillsContainer(){
        skills = new Skill[6];
        skillPoint = 0;
        skills[0] = new Skill(Skill.SkillType.HPUP);
        skills[1] = new Skill(Skill.SkillType.MPUP);
        skills[2] = new Skill(Skill.SkillType.HPRECOVERYUP);
        skills[3] = new Skill(Skill.SkillType.MPRECOVERYUP);
        skills[4] = new Skill(Skill.SkillType.MOVESPEEDUP);
        skills[5] = new Skill(Skill.SkillType.SOULEATER);
    }

    public void learnSkill(Skill.SkillType skillType){
            switch (skillType){
                case HPUP:
                    skills[0].lv += 1;
                    break;
                case MPUP:
                    skills[1].lv += 1;
                    break;
                case HPRECOVERYUP:
                    skills[2].lv += 1;
                    break;
                case MPRECOVERYUP:
                    skills[3].lv += 1;
                    break;
                case MOVESPEEDUP:
                    skills[4].lv += 1;
                    break;
                case SOULEATER:
                    skills[5].lv += 1;
                    break;
        }
        for(int i = 0; i < skills.length; i++){
            skills[i].calculateTotalValue(skills[i].skillType);
        }
    }


}
