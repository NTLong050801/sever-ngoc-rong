    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.BossEvent;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 * @author DVũ HYến
 */
public class ThangBe extends Boss {
    public ThangBe() throws Exception {
        super(BossID.ThangBe , BossesData.ThangBe , BossesData.ThangBe1, BossesData.ThangBe2);
    }
     @Override
    public void reward(Player plKill) {
        int bossX = this.location.x;
    int bossY = this.location.y;

    // Rơi thỏi vàng (457), mỗi lần 5 thỏi
    int totalGold = Util.nextInt(20, 100);
    int goldDropTimes = totalGold / 5;

    for (int i = 0; i < goldDropTimes; i++) {
        int offsetX = Util.nextInt(-300, 300);
        int offsetY = Util.nextInt(-10, 10);
        int dropX = bossX + offsetX;
        int dropY = bossY + offsetY;

        Service.gI().dropItemMap(this.zone,
            Util.useItem4(zone, 457, 5, dropX, dropY, plKill.id));
    }

    // 40% rơi item 2146, mỗi lần 1 cái, tổng số ngẫu nhiên 1-10
    if (Util.isTrue(10, 100)) {
        int amount2146 = Util.nextInt(1, 10);
        for (int i = 0; i < amount2146; i++) {
            int offsetX = Util.nextInt(-200, 200);
            int offsetY = Util.nextInt(-10, 10);
            Service.gI().dropItemMap(this.zone,
                Util.useItem4(zone, 2146, 1, bossX + offsetX, bossY + offsetY, -1));
        }
    }

    // 5% rơi item 2147, mỗi lần 1 cái, tổng số ngẫu nhiên 1-3
    if (Util.isTrue(2, 100)) {
        int amount2147 = Util.nextInt(1, 3);
        for (int i = 0; i < amount2147; i++) {
            int offsetX = Util.nextInt(-100, 100);
            int offsetY = Util.nextInt(-10, 10);
            Service.gI().dropItemMap(this.zone,
                Util.useItem4(zone, 2147, 1, bossX + offsetX, bossY + offsetY, -1));
        }
    }

    // Rơi item đặc biệt hoặc ngẫu nhiên từ danh sách cũ
    int[] itemDos = new int[]{1099, 1100, 1101, 1102};
    int randomDo = new Random().nextInt(itemDos.length);
    if (Util.isTrue(10, 100)) {
        Service.gI().dropItemMap(this.zone,
            Util.useItem4(zone, Util.nextInt(1507,1510), 1, bossX, bossY, plKill.id));
    } else {
        Service.gI().dropItemMap(this.zone,
            Util.useItem4(zone, itemDos[randomDo], 2, bossX + 15, bossY+ 5, -1));
    }
}
 @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Đố a bắt được em.");
                return 0;
            }
                        damage = 10;
            if (plAtt.setClothes != null && plAtt.setClothes.setDHK == 5) { damage = 20;}
            if (this.effectSkill != null && this.effectSkill.isShielding) {
                this.chat("Em đang có khiên đây nè!");
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
}
