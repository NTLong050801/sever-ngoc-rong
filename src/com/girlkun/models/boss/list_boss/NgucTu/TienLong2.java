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
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 *@Stole By Arriety
 */
public class TienLong2 extends Boss {

    public TienLong2() throws Exception {
        super(BossID.ThienLong2, BossesData.ThienLong2);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap itemMap;
        itemMap = Util.ratiItem(zone,1704, 1, this.location.x, this.location.y, plKill.id);
        if (Util.isTrue(50, 100)) {
        itemMap.options.add(new Item.ItemOption(50,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(81,Util.nextInt(10,30)));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,3)));
        itemMap.options.add(new Item.ItemOption(30,0));
        } else {
        itemMap.options.add(new Item.ItemOption(50,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(81,Util.nextInt(10,30)));
        itemMap.options.add(new Item.ItemOption(3,Util.nextInt(10,30)));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,7)));
        itemMap.options.add(new Item.ItemOption(30,0));
                                }    
        plKill.LeHoi += 5;
        Service.gI().dropItemMap(this.zone, itemMap);
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