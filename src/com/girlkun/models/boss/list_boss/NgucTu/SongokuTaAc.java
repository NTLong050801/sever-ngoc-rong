/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 *@Stole By Arriety
 */
public class SongokuTaAc extends Boss {

    public SongokuTaAc() throws Exception {
        super(BossID.SONGOKU_TA_AC, BossesData.SONGOKU_TA_AC);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        if (Util.isTrue(BossManager.ratioReward, 100)) {
            if (Util.isTrue(40, 100)) {
             Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
            } else {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
            }
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        if (Util.isTrue(1, 20)) {
        ItemMap itemMap;
        itemMap = Util.ratiItem(zone,1716, 1, this.location.x, this.location.y, plKill.id);
        if (Util.isTrue(70, 100)) {
        itemMap.options.add(new Item.ItemOption(9,300));
        itemMap.options.add(new Item.ItemOption(30,1));
        Service.gI().dropItemMap(this.zone, itemMap);
        } else {
        itemMap.options.add(new Item.ItemOption(77,30));
        itemMap.options.add(new Item.ItemOption(103,30));
        itemMap.options.add(new Item.ItemOption(9,300));
        itemMap.options.add(new Item.ItemOption(30,1));
        Service.gI().dropItemMap(this.zone, itemMap);
        }}
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
}