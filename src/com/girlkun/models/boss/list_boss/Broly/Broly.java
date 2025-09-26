package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.bdkb.TrungUyXanhLo;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;


public class Broly extends Boss {

    public Broly() throws Exception {
        super(BossID.BROLY , BossesData.BROLY_1,BossesData.BROLY_2, BossesData.BROLY_3);
    }
     @Override
    public void reward(Player plKill) {
        if (this.nPoint.hpMax>1000000){
            
        BossData Super = new BossData(
            "Super Broly "+Util.nextInt(100),
            this.gender,
            new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            Util.nextInt(100000,200000), //dame
            new long[]{Util.nextInt(1000000,16077777)}, //hp
            new int[]{6}, //map join
            new int[][]{
                    {Skill.ANTOMIC,7,100},{Skill.MASENKO,7,100},
                {Skill.KAMEJOKO,7,100},
                    {Skill.TAI_TAO_NANG_LUONG,5,10000}},
            new String[]{"|-2|SuperBroly"}, //text chat 1
            new String[]{"|-1|Ọc Ọc"}, //text chat 2
            new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
            60
            );

            try {
                new Super(Util.createIdBossClone((int) this.id), Super, this.zone);
            } catch (Exception e) {
                e.printStackTrace();
            }}}
    @Override
    public void active() {
        super.active();
        this.nPoint.dame = this.nPoint.hpMax/10;
    }
    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (this.nPoint.hpMax < 16000000){
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = this.nPoint.hpMax/10;
                 if (damage > nPoint.mpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = this.nPoint.hpMax/10; 
                 if (damage > nPoint.tlNeDon) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = this.nPoint.hpMax/10; 
            }
            if (damage > this.nPoint.hpMax/10) {
                damage = this.nPoint.hpMax/10;
            }}
            if (this.nPoint.hpMax < 16007777){
                    this.nPoint.hpMax+=this.nPoint.hpMax/300;}
            if(this.nPoint.hpMax > 16007777){
                    this.nPoint.hpMax=16007777;}
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        }
    
    
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
}




















