package com.girlkun.models.boss.list_boss.doanh_trai;;
import com.girlkun.consts.ConstMob;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossData;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 * @author BTH sieu cap vippr0
 */
public class TrungUyTrang extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public TrungUyTrang(Zone zone , int dame, long hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "Trung úy trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((10000 + dame)), //dame
                new long[]{((200000000 + hp))}, //hp
                new int[]{59}, //map join
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
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        if (Util.isTrue(BossManager.ratioReward, 100)) {
            if (Util.isTrue(50, 100)) {
             Service.gI().dropItemMap(this.zone,new ItemMap(zone, 861, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }
@Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!piercing) {
            boolean haveBulon = false;  //true: diết bulong mới pem dc false: k cần bulong
            for (Mob mob : this.zone.mobs) {
                if (mob.tempId == ConstMob.BULON && !mob.isDie()) {
                    haveBulon = true;
                    break;
                }
            }
            if (haveBulon) {
                damage = 1;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}





