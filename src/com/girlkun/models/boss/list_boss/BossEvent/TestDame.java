package com.girlkun.models.boss.list_boss.BossEvent;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author DVũ HYến
 */
public class TestDame extends Boss {
    // Biến để chống spam thông báo
    private long lastNotifyTime = 0;
    private double lastDamageNotified = 0;
    private static final long NOTIFY_COOLDOWN = 15000; // 5 giây cooldown
    private static final double DAMAGE_INCREASE_RATIO = 1.2; // Cần tăng 20% sát thương so với lần trước
    
    public TestDame() throws Exception {
        super(BossID.TestDame, BossesData.TestDame);
    }
     
    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            
            // Hiển thị chat bình thường (không cần chống spam)
            this.chat(plAtt.name + " đã đánh một chiêu " + 
                plAtt.playerSkill.skillSelect.template.name + " với sát thương " + 
                Util.numberToMoney(damage));

            // Kiểm tra điều kiện thông báo server với chống spam
            if (plAtt.isPl() && damage >= 500_000_000) {
                long currentTime = System.currentTimeMillis();
                
                // Chỉ thông báo nếu: hết cooldown VÀ sát thương tăng đáng kể
                if ((currentTime - lastNotifyTime >= NOTIFY_COOLDOWN) && 
                    (damage > lastDamageNotified * DAMAGE_INCREASE_RATIO)) {
                    
                    ServerNotify.gI().notify(plAtt.name + " đã đánh một chiêu " + 
                        plAtt.playerSkill.skillSelect.template.name + " với sát thương " + 
                        Util.numberToMoney(damage) + " mọi người đều ngưỡng mộ");
                    
                    // Cập nhật thời gian và sát thương lần cuối
                    lastNotifyTime = currentTime;
                    lastDamageNotified = damage;
                }
            }

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