package com.girlkun.models.boss.list_boss.android;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossType;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;
import java.io.IOException;
import java.util.Random;

public class SuperAndroid17 extends Boss {

    private Pic adr17;
    protected boolean isReady;
    public boolean isFusion;
    protected long lastTimeFusion;
    private final int timeToFusion = 5000;
    protected long lastTimecanAttack;
    public boolean canAttack;

    public SuperAndroid17() throws Exception {
        super(BossType.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
        this.adr17 = null;
        this.isReady = false;
        this.isFusion = false;
    }

    

    public void createSmallBoss() {
        try {
            this.adr17 = new Pic(this, this.zone, (short) this.location.x, (short) this.location.y, BossesData.PIC_TA_AC);
        } catch (Exception ex) {

        }
    }

    public void hoptheAdr() {
        if (this.adr17 != null && this.adr17.typePk == ConstPlayer.NON_PK && this.adr17.isReady
                && this.typePk == ConstPlayer.NON_PK && this.isReady && !this.isFusion) {
            if (Util.canDoWithTime(lastTimeFusion, this.timeToFusion)) {
                this.isFusion = true;
                setBaseFusion();
                fusion(false);
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        }
    }

    private void setBaseFusion() {
        BossData data = this.getData()[this.getCurrentLevel()];
        this.name = "Super Android 17";
        this.nPoint.mpg = 11_12_2005;
        this.nPoint.dameg = 100_100;
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)] + 200_000_000;
        this.nPoint.calPoint();
        super.active(); 
        this.initSkill();
        this.resetBase();
    }

    public void fusion(boolean porata) {
        ChangeMapService.gI().exitMap(this.adr17);
        this.adr17 = null;
        this.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
        fusionEffect(this.fusion.typeFusion);
        this.location.y = this.zone.map.yPhysicInTop(this.location.x, this.location.y);
        ChangeMapService.gI().changeMap(this, zone,this.location.x, this.location.y);
        Service.getInstance().Send_Caitrang(this);
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void reward(Player plKill) {
        int[] NRs = new int[]{1099,1100,1101,1102};
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
            }
        ItemMap itemMap;
        if (Util.isTrue(50, 100)) {
            itemMap = Util.ratiItem(zone, 1957, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(50, Util.nextInt(30, 40)));
            itemMap.options.add(new Item.ItemOption(77, Util.nextInt(30, 40)));
            itemMap.options.add(new Item.ItemOption(103, Util.nextInt(30, 40)));
            itemMap.options.add(new Item.ItemOption(14, 20));
            itemMap.options.add(new Item.ItemOption(4, 20));
            itemMap.options.add(new Item.ItemOption(93, Util.nextInt(1, 7)));
            itemMap.options.add(new Item.ItemOption(30, 1));
            Service.getInstance().dropItemMap(this.zone, itemMap);
            }
    }
    
    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) this.id);
            Service.gI().sendMessAllPlayerInMap(this, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/4);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/4;
            }
            if (damage >= this.nPoint.hp && !this.isReady && this.adr17 != null) {
                this.changeToTypeNonPK();
                this.nPoint.hp = 1;
                this.isReady = true;
                this.effectSkill.removeSkillEffectWhenDie();
                if (this.adr17.isReady) {
                    this.lastTimeFusion = System.currentTimeMillis();
                    this.lastTimecanAttack = System.currentTimeMillis();
                }
                return 0;
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
        hoptheAdr();
        if (this.typePk == ConstPlayer.NON_PK && !isReady) {
            this.changeToTypePK();
            return;
        }
        if (this.adr17 == null && Util.canDoWithTime(lastTimecanAttack, timeToFusion * 2) && !this.canAttack) {
            this.changeToTypePK();
            this.canAttack = true;
            return;
        }
        this.attack();
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        this.createSmallBoss();
        st = System.currentTimeMillis();
    }
    private long st;

}
