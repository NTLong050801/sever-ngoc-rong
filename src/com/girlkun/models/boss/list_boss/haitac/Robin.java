package com.girlkun.models.boss.list_boss.haitac;

import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

import java.util.Random;


public class Robin extends Boss {

    public Robin() throws Exception {
        super(BossID.Robin, BossesData.Robin);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 1287, 50, this.location.x, this.location.y, plKill.id));
        }
        if (Util.isTrue(100, 100)) {
        int randHN = Util.nextInt(50000, 300000);
        plKill.inventory.ruby += randHN;
        Service.gI().sendMoney(plKill);
        Service.getInstance().sendThongBao(plKill, "Bạn đã nhận được " + Util.numberToMoney(randHN) + " Hồng Ngọc");   }
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 400000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//    
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}






















