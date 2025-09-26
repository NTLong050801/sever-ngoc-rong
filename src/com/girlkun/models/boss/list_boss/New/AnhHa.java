/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.New;
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
/**
 *
 * @author DVũ HYến
 */
public class AnhHa extends Boss {

    public AnhHa() throws Exception {
        super(BossID.AnhHa, BossesData.AnhHa);
    }

    @Override
    public void reward(Player plKill) {
        
        if (Util.isTrue(100, 100)) {
            for (int i = 0; i < Util.nextInt(5, 15); i++) {
             ItemMap it = new ItemMap(this.zone, 861, 30000 , this.location.x - i*10 , this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            Service.getInstance().dropItemMap(this.zone, it);
        }}
        if (Util.isTrue(100, 100)) {
             for (int i = 0; i < Util.nextInt(10, 35); i++) {
             ItemMap it = new ItemMap(this.zone, 457, Util.nextInt(20, 50) , this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y -24), -1);
            Service.getInstance().dropItemMap(this.zone, it);
        }}
        if (Util.isTrue(10, 100)) {
            for (int i = 0; i < Util.nextInt(10, 25); i++) {
             ItemMap it = new ItemMap(this.zone, 457, Util.nextInt(100, 500) , this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            Service.getInstance().dropItemMap(this.zone, it);
        }}
        int[] itemDoHKs = new int[]{2033,2034,2035,2036,2037,2038};
        int[] itemThanhNgocs = new int[]{2039,2040,2041};
        int randomDoHK = new Random().nextInt(itemDoHKs.length);
        int randomThanhNgoc = new Random().nextInt(itemThanhNgocs.length);
            if (Util.isTrue(100, 100)) {
                for (int i = 0; i < Util.nextInt(3, 7); i++) {
                Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 2033, 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
                Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 2034, 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
                Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 2035, 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
                return;
            }}
            if (Util.isTrue(100, 100)) {
                for (int i = 0; i < Util.nextInt(2, 5); i++) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, itemDoHKs[randomDoHK], 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
            }}
            if (Util.isTrue(10, 100)) {
                for (int i = 0; i < Util.nextInt(5, 10); i++) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, itemDoHKs[randomDoHK], 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
            }}
            if (Util.isTrue(100, 100)) {
                for (int i = 0; i < Util.nextInt(2, 4); i++) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, itemThanhNgocs[randomThanhNgoc], 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
            }}
            if (Util.isTrue(10, 100)) {
                for (int i = 0; i < Util.nextInt(4, 10); i++) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, 2042, 1, this.location.x - i*15, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 12), -1));
            }}
          
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
            damage = this.nPoint.subDameInjureWithDeff(damage/6);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
               damage = damage/16;
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
    }    
