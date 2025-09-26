package com.girlkun.models.boss.list_boss.android;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

public class Android14 extends Boss {

    public boolean callApk13;

    Android13 adr13;
    Android15 adr15;

    public Android14() throws Exception {
        super(BossID.ANDROID_14, BossesData.ANDROID_14);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{382, 383, 384};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
        this.adr13 = (Android13) this.getBossAppearTogether()[this.getCurrentLevel()][0];
        this.adr15 = (Android15) this.getBossAppearTogether()[this.getCurrentLevel()][1];
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK && !this.callApk13) {
            this.changeToTypePK();
        } else if (this.callApk13 && adr13.typePk == ConstPlayer.PK_ALL) {
            this.changeToTypePK();
        }
        this.attack();
    }

    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && this.nPoint.hp - damage <= this.nPoint.hpMax / 2) {
            this.callApk13();
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void callApk13() {
        if (this.getBossAppearTogether() == null || this.getBossAppearTogether()[this.getCurrentLevel()] == null) {
            return;
        }
        this.adr15.changeToTypeNonPK();
        this.changeToTypeNonPK();
        this.adr15.callApk13 = true;
        this.callApk13 = true;
        this.adr15.recoverHP();
        this.recoverHP();
        this.adr13.changeStatus(BossStatus.RESPAWN);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void doneChatS() {
        if (this.getBossAppearTogether() == null || this.getBossAppearTogether()[this.getCurrentLevel()] == null) {
            return;
        }
        for (Boss boss : this.getBossAppearTogether()[this.getCurrentLevel()]) {
            if (boss.id == BossID.ANDROID_15) {
                boss.changeToTypePK();
                break;
            }
        }
    }

}
