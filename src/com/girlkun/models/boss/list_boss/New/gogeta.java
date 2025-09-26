package com.girlkun.models.boss.list_boss.New;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossType;
import com.girlkun.models.boss.BossesData;
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

public class gogeta extends Boss {

    private goku isgoku;
    protected boolean isReady;
    public boolean isFusion;
    protected long lastTimeFusion;
    private final int timeToFusion = 5000;
    protected long lastTimecanAttack;
    public boolean canAttack;

    public gogeta() throws Exception {
        super(BossType.gogeta, BossesData.gogeta);
        this.isgoku = null;
        this.isReady = false;
        this.isFusion = false;
    }

    

    public void createSmallBoss() {
        try {
            this.isgoku = new goku(this, this.zone, (short) this.location.x, (short) this.location.y, BossesData.goku);
        } catch (Exception ex) {

        }
    }

    public void hoptheAdr() {
        if (this.isgoku != null && this.isgoku.typePk == ConstPlayer.NON_PK && this.isgoku.isReady
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
        this.name = "Gogeta Siêu Saiyan 4";
        this.nPoint.mpg = 11_12_2005;
        this.nPoint.dameg = 100_100;
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)] + 200_000_000;
        this.nPoint.calPoint();
        super.active(); 
        this.initSkill();
        this.resetBase();
    }

    public void fusion(boolean porata) {
        ChangeMapService.gI().exitMap(this.isgoku);
        this.isgoku = null;
        this.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
        fusionEffect(this.fusion.typeFusion);
        this.location.y = this.zone.map.yPhysicInTop(this.location.x, this.location.y);
        ChangeMapService.gI().changeMap(this, zone,this.location.x, this.location.y);
        Service.getInstance().Send_Caitrang(this);
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void reward(Player plKill) {
        int[] NRs = new int[]{1099,1100,1101,1102,381,382};
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, Util.useItem3(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
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
            if (damage >= this.nPoint.hp && !this.isReady && this.isgoku != null) {
                this.changeToTypeNonPK();
                this.nPoint.hp = 1;
                this.isReady = true;
                this.effectSkill.removeSkillEffectWhenDie();
                if (this.isgoku.isReady) {
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
        if (this.isgoku == null && Util.canDoWithTime(lastTimecanAttack, timeToFusion * 2) && !this.canAttack) {
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
