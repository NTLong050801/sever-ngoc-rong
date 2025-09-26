package com.girlkun.models.boss.list_boss.New;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import com.girlkun.services.PlayerService;
import java.util.Random;


public class zeno extends Boss {
    private long lastTimeHapThu;
    private int timeHapThu;
    public zeno() throws Exception {
        super(BossID.zeno, BossesData.zeno);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(10, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 1106, 1, this.location.x, this.location.y, plKill.id));
         }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 1004, 1, this.location.x, this.location.y, plKill.id));
         }
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;


    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        this.attack();
         super.active(); //To change body of generated methods, choose Tools | Templates.
         if (Util.canDoWithTime(st, 900000)) {
             this.changeStatus(BossStatus.LEAVE_MAP);
         }
    }
public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/5);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
               damage = damage/5;
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

    private void hapThu() {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
        this.chat(2, "Hắn ta mạnh quá,coi chừng " + pl.name + ",tên " + this.name + " hắn không giống như những kẻ thù trước đây");
        this.chat("Thật là yếu ớt " + pl.name);
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000);
    }
 
}