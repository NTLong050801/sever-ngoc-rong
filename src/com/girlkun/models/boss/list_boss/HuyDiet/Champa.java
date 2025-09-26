package com.girlkun.models.boss.list_boss.HuyDiet;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;


public class Champa extends Boss {

    public Champa() throws Exception {
        super(BossID.THAN_HUY_DIET_CHAMPA, BossesData.THAN_HUY_DIET_CHAMPA);
    }

    @Override
   public void reward(Player plKill) {
       int[] itemDos = new int[]{1108};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(100, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
         }
        if (Util.isTrue(2, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 1004, 1, this.location.x, this.location.y, plKill.id));
         }
    }

     @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
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
    public void active() {

        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
        }
}


