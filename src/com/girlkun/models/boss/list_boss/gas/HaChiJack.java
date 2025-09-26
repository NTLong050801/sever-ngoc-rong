package com.girlkun.models.boss.list_boss.gas;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import com.girlkun.models.boss.list_boss.cell.SieuBoHung;
import com.girlkun.models.map.ItemMap;
import java.util.List;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstMob;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;

import java.util.List;

import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Location;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.reward.ItemMobReward;
import com.girlkun.models.reward.MobReward;
import com.girlkun.network.io.Message;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.*;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author BTH sieu cap vippr0
 */
public class HaChiJack extends Boss {

    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
    private int levell;

    public HaChiJack(Zone zone, int level, int dame, long hp) throws Exception {
        super(BossID.HACHIYACK, new BossData(
                "HaChiJack", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{639, 640, 641, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((1000 + dame) * level), //dame
                new long[]{((5000000 + hp) * level)}, //hp
                new int[]{148}, //map join
                (int[][]) Util.addArray(FULL_DEMON), //skill
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        int level = clan.khiGas.level;
        if ( level < 20){
            ItemMap itemMap;
                      itemMap = Util.ratiItem(zone,729, 1, this.location.x, this.location.y, plKill.id);
                      itemMap.options.add(new Item.ItemOption(50,Util.nextInt(20,22)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(20,22)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(20,22)));
        itemMap.options.add(new Item.ItemOption(14,1));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,7)));  
        itemMap.options.add(new Item.ItemOption(30,0));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        else if ( level <= 50 && level >= 20){
                     ItemMap itemMap;
                      itemMap = Util.ratiItem(zone,729, 1, this.location.x, this.location.y, plKill.id);
                      itemMap.options.add(new Item.ItemOption(50,Util.nextInt(22,25)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(22,25)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(22,25)));
        itemMap.options.add(new Item.ItemOption(14,2));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,7)));
        itemMap.options.add(new Item.ItemOption(30,0));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        else if ( level > 50 && level < 100){
                     ItemMap itemMap;
                      itemMap = Util.ratiItem(zone,729, 1, this.location.x, this.location.y, plKill.id);
                      itemMap.options.add(new Item.ItemOption(50,Util.nextInt(25,27)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(25,27)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(25,27)));
        itemMap.options.add(new Item.ItemOption(14,3));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,7)));
        itemMap.options.add(new Item.ItemOption(30,0));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        else if ( level <= 110 && level >= 100){
        ItemMap itemMap;
        itemMap = Util.ratiItem(zone,729, 1, this.location.x, this.location.y, plKill.id);
        itemMap.options.add(new Item.ItemOption(50,Util.nextInt(27,40)));
        itemMap.options.add(new Item.ItemOption(77,Util.nextInt(27,40)));
        itemMap.options.add(new Item.ItemOption(103,Util.nextInt(27,40)));
        itemMap.options.add(new Item.ItemOption(5,Util.nextInt(27,40)));
        itemMap.options.add(new Item.ItemOption(14,4));
        itemMap.options.add(new Item.ItemOption(93,Util.nextInt(1,7)));
        itemMap.options.add(new Item.ItemOption(30,0));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
    }
    @Override
    public void active() {
    super.active();
    }
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if ( levell <= 30000){
        if (Util.isTrue((levell/1000), 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 1000000);
            if (Util.isTrue(1, 100)) {
                this.chat(" Gà đánh mạnh lên");
                this.chat(" Gà đánh mạnh lên");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ngậm hành đi kkkkk");
                this.chat("Minh Phu  đẹp trai nhất SV");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("MinhPhu đẹp trai nhất SV");
            }
            damage = 0;
            }
        } else {
        if (Util.isTrue(40, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 1000000);
            if (Util.isTrue(1, 100)) {
                this.chat(" Gà đánh mạnh lên");
                this.chat(" Gà đánh mạnh lên");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ngậm hành đi kkkkk");
                this.chat("MinhPhu đẹp trai nhất SV");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("MinhPhu đẹp trai nhất SV");
            }
            damage = 0;
            }

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            if ( levell <= 30000){
            damage -= damage * (levell/1000)/100;
            } else {
            damage -= damage * 40/100;
            }
            return damage;
        } else {
            return 0;
        }
          
    }
     
  @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}







