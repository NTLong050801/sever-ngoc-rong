package com.girlkun.models.boss.list_boss.sieuthi;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.Random;
import com.girlkun.models.item.Item;
public class sexy extends Boss {

    public sexy() throws Exception {
        super(BossID.sexy, BossesData.sexy);
    }
   @Override
    public void reward(Player plKill) {
        
        ItemMap itemMap;
        itemMap = Util.ratiItem(zone,1954, 1, this.location.x, this.location.y, plKill.id);
        if (Util.isTrue(50, 100)) {
        itemMap.options.add(new Item.ItemOption(50,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(35,40)));
        itemMap.options.add(new Item.ItemOption(95,40));
        itemMap.options.add(new Item.ItemOption(96,40));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,3)));
        itemMap.options.add(new Item.ItemOption(30,0));
        } else {
        itemMap.options.add(new Item.ItemOption(50,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(35,39)));
        itemMap.options.add(new Item.ItemOption(30,0));
                                }
        int[] HoaBiNgan = new int[]{861};
        int randomNR = new Random().nextInt(HoaBiNgan.length);
        if (Util.isTrue(1, 5)) {
        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, HoaBiNgan[randomNR], 1, this.location.x, this.location.y, plKill.id));
        } else {    
        
        Service.gI().dropItemMap(this.zone, itemMap);
    }
    } 

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,900000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
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