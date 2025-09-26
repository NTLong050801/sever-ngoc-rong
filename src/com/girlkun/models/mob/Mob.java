package com.girlkun.models.mob;


import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstMob;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import java.util.List;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.event.Event;
import com.girlkun.event.EventDAO;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Location;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.player.SetClothes;
import com.girlkun.models.reward.ItemMobReward;
import com.girlkun.models.reward.MobReward;
import com.girlkun.network.io.Message;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.*;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        if ( level <= 10){
        mob.point.dame = 10000;
        mob.point.maxHp = 1000000;
         }
        if ( level > 10 && level <= 50 ){
        mob.point.dame = 1000000;
        mob.point.maxHp = 500000000;
         }
        if ( level > 50 && level < 100 ){
        mob.point.dame = 2500000;
        mob.point.maxHp = 1500000000;
         }
        if ( level >= 100 ){
        mob.point.dame = 5000000;
        mob.point.maxHp = 2000000000;
         }
    }
    
public static void initMopbKhiGas(Mob mob, int level) {
        if ( level <= 10){
        mob.point.dame = 10000;
        mob.point.maxHp = 1000000;
         }
        if ( level > 10 && level <= 50 ){
        mob.point.dame = 1000000;
        mob.point.maxHp = 500000000;
         }
        if ( level > 50 && level < 100 ){
        mob.point.dame = 2500000;
        mob.point.maxHp = 1500000000;
         }
        if ( level >= 100 ){
        mob.point.dame = 5000000;
        mob.point.maxHp = 2000000000;
         }
    }
    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt((mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }
public boolean isSieuQuai() {
        return this.lvMob > 0;
    }
    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            this.point.hp -= damage;
            if (this.isDie()) {
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
            } else {
                this.sendMobStillAliveAffterAttacked(this, damage, plAtt != null ? plAtt.nPoint.isCrit : false, plAtt);
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124){
            tiemNang *= 1.5;
        }
        if (pl.zone.map.mapId >= 53 && pl.zone.map.mapId <= 62){
            tiemNang *= 2;
        }
        if (pl.zone.map.mapId >= 135 && pl.zone.map.mapId <= 138){
            tiemNang *= 2.5;
        }
        if (pl.zone.map.mapId >= 147 && pl.zone.map.mapId <= 152){
            tiemNang *= 2.5;
        }
        return tiemNang;
    }

    public void update() {
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }

        if (this.isDie()&& !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    break;
                    case ConstMap.MAP_KHI_GAS:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
            }
        }
        effectSkill.update();
        attackPlayer();
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
//                MobService.gI().mobAttackPlayer(this, pl);
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
     private void mobAttackPlayer(Player player) {
        int dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        int dame = (int) player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet &&!player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt((int) player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    private void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet&&!player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 861 && item.itemTemplate.id != 457) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 861 && item.itemTemplate.id != 457) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

       private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        if (mobReward == null) {
            return list;
        }final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);
        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        byte randomVp1 = (byte) new Random().nextInt(Manager.manhts.length);
        byte randomVp2 = (byte) new Random().nextInt(Manager.manhts1.length);
        byte randomVp0 = (byte) new Random().nextInt(Manager.manhts2.length);
        byte randomVp4 = (byte) new Random().nextInt(Manager.manhts3.length);
        byte randomVp3 = (byte) new Random().nextInt(Manager.thucan.length);
        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        
        //nghe nghiep
        
        if (player.nPoint.nghenghiep != 0 && Util.isTrue(1, 300)) {
            int sl = (player.nPoint.nghenghiep -1)/3 +2;
    int[] itemIds = {2115, 2116, 2117, 2118, 2119};
int randomIndex = Util.nextInt(0, itemIds.length -1);
int randomItemId = itemIds[randomIndex];
    list.add(new ItemMap(zone, randomItemId, sl, x, player.location.y, player.id));
}
        if (player.nPoint.isAccJr() && Util.isTrue(1, 500) && (this.zone.map.mapId == 0 || this.zone.map.mapId == 7 || this.zone.map.mapId == 14)) {
    list.add(new ItemMap(zone, 1226, 1, x, player.location.y, player.id));
}  
         //event
   Event event = EventDAO.getCurrentEvent();
if (event != null && this.zone.map.mapId > 84) {
    List<Integer> eventItemIds = EventDAO.getItemDropList();
    eventItemIds.stream().filter((itemId) -> (Util.isTrue(1,100))).map((itemId) -> new ItemMap(this.zone, itemId, 1, x, yEnd, player.id)).forEachOrdered((eventItem) -> {
    // 50% xác suất rơi item
                list.add(eventItem);
            });
}
        
        if (player.itemTime.isUseMayDo && Util.isTrue(10, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        }
        if (player.cFlag>=1 && Util.isTrue(100, 100) && this.zone.map.mapId>=79 && this.zone.map.mapId<=83 && hour!=1 && hour!=3 && hour!=5 && hour!=7 && hour!=11 && hour!=13 && hour!=15 && hour!=17 && hour!=19 && hour!=21 && hour!=23) {    //up bí kíp
            list.add(new ItemMap(zone, 579, 1, x, player.location.y, player.id));// cai nay sua sau nha
        }        if (Util.isTrue(20, 100) && this.tempId != 0) {
            list.add(new ItemMap(zone, 225, 1, x, player.location.y, player.id));// cai nay sua sau nha
        }
        if (Util.isTrue(1, 80) && this.tempId != 0) {
            list.add(new ItemMap(zone, 1720, Util.nextInt(1, 8), x, player.location.y, player.id));// cai nay sua sau nha
        }
        
         if (Util.isTrue(1, 500) && player.nPoint.power >= 5000000000L) {
             list.add(new ItemMap(zone, 457, Util.nextInt(5, 40), x, player.location.y, player.id));// cai nay sua sau nha
        }
             if (Util.isTrue(1, 1000) && Util.isTrue(1,20)){
            list.add(new ItemMap(zone, 1004, 1, x, player.location.y, player.id));// cai nay sua sau nha
         }
        if (Util.isTrue(5, 100) && player.setClothes.setDTL == 5 && MapService.gI().isMapCold(this.zone.map)) {
            list.add(new ItemMap(zone, Manager.thucan[randomVp3], 1, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(1, 600) && player.setClothes.setDTS == 5 && this.zone.map.mapId == 174) {
            list.add(new ItemMap(zone, Manager.manhts1[randomVp2], 1, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(1, 1000) && player.setClothes.setDTS == 5 && this.zone.map.mapId == 174) {
            list.add(new ItemMap(zone, Manager.manhts2[randomVp0], 1, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(1, 2000) && player.setClothes.setDHK == 5 && this.zone.map.mapId == 174) {
            list.add(new ItemMap(zone, Manager.manhts3[randomVp4], 1, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(10, 100) && this.zone.map.mapId == 155 && player.setClothes.setDHD == 5) {
            list.add(new ItemMap(zone, Manager.manhts[randomVp1], 1, this.location.x, this.location.y, player.id));
        }if (this.tempId>0 && this.zone.map.mapId>=122 && this.zone.map.mapId<=124){
        if (Util.isTrue(5, 100)) {    //mvc2
            list.add(new ItemMap(zone, 542, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=122 && this.zone.map.mapId<=124){
        if (Util.isTrue(10, 100)) {    //mvc2
            list.add(new ItemMap(zone, 543, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA2){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2030, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA3){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2030, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA4){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2030, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA5){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2030, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA2){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2134, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA3){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2135, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159 && player.fusion.typeFusion==ConstPlayer.HOP_THE_PORATA4){
        if (Util.isTrue(50, 100)) {    //hop the btc2 ms farm dc btc3
            list.add(new ItemMap(zone, 2136, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159){
        if (Util.isTrue(30, 100)) {    //mvc2
            list.add(new ItemMap(zone, 933, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159){
        if (Util.isTrue(30, 100)) {    //mhbt
            list.add(new ItemMap(zone, 934, 1, x, player.location.y, player.id));}}
        if (this.tempId>0 && this.zone.map.mapId>=156 && this.zone.map.mapId<=159){
        if (Util.isTrue(10, 100)) {    //đá xanh lam
            list.add(new ItemMap(zone, 935, 1, x, player.location.y, player.id));}}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (0));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(213, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(221,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (0));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(214, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(224,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (6));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 30));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(213, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(221,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (6));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 30));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(214, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(224,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (21));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 4));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(213, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(221,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (21));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 4));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(214, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(224,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (27));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,10));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(213, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(221,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (27));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,10));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(214, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(224,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(213, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(221,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 0) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(214, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(224,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(216, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(219,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(215, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(220,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (7));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 20));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(216, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(219,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (7));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 20));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(215, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(220,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (22));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 3));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(216, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(219,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (22));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 3));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(215, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(220,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (28));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,15));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(216, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(219,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (28));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,15));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(215, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(220,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(216, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(219,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 1) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(215, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(220,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(217, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(222,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(47, 2));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(218, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(223,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (8));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 20));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(217, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(222,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (8));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(6, 20));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(218, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(223,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (23));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 3));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(217, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(222,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (23));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(0, 3));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(218 ,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(223,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (29));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,15));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(217, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(222,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (29));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(7,15));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(218, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(223,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(217, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(222,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if ((this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 3 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9 || this.zone.map.mapId == 11 || this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 17) && player.gender == 2) {
            if(Util.isTrue(1, 500)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (12));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(14,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(218, 1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(223,1));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(30,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
         if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Quanthanlinh = ItemService.gI().createNewItem((short) (556));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(22, Util.nextInt(55,65)));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(21, 15));
                Quanthanlinh.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinh.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (560));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45,55)));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(21, 15));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhxd.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Quanthanlinhnm = ItemService.gI().createNewItem((short) (558));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(22, Util.nextInt(50,60)));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(21, 15));
                Quanthanlinhnm.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhnm.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Aothanlinh = ItemService.gI().createNewItem((short) (555));
                Aothanlinh.itemOptions.add(new Item.ItemOption(47, Util.nextInt(500,600)));
                Aothanlinh.itemOptions.add(new Item.ItemOption(21, 15));
                Aothanlinh.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Aothanlinh.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Aothanlinhxd = ItemService.gI().createNewItem((short) (559));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(600,700)));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(21, 15));
                Aothanlinhxd.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Aothanlinhxd.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Aothanlinhnm = ItemService.gI().createNewItem((short) (557));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(47, Util.nextInt(400,550)));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(21, 15));
                Aothanlinhnm.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Aothanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Aothanlinhnm.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Gangthanlinh = ItemService.gI().createNewItem((short) (562));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6000,7000)));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(21, 15));
                Gangthanlinh.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Gangthanlinh.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Gangthanlinhxd = ItemService.gI().createNewItem((short) (566));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6500,7500)));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(21, 15));
                Gangthanlinhxd.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Gangthanlinhxd.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Gangthanlinhnm = ItemService.gI().createNewItem((short) (564));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(0, Util.nextInt(5500,6500)));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(21, 15));
                Gangthanlinhnm.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Gangthanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Gangthanlinhnm.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Giaythanlinh = ItemService.gI().createNewItem((short) (563));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(23, Util.nextInt(50,60)));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(21, 15));
                Giaythanlinh.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Giaythanlinh.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Giaythanlinhxd = ItemService.gI().createNewItem((short) (567));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(55,65)));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(21, 15));
                Giaythanlinhxd.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Giaythanlinhxd.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 10)){
                Item Giaythanlinhnm = ItemService.gI().createNewItem((short) (565));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(23, Util.nextInt(65,75)));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(21, 15));
                Giaythanlinhnm.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Giaythanlinhnm);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Giaythanlinhnm.template.name);
                            }}
        if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if(Util.isTrue(1, 1000) && Util.isTrue(1, 30)){
                Item Nhanthanlinh = ItemService.gI().createNewItem((short) (561));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(14, Util.nextInt(13,16)));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(21, 15));
                Nhanthanlinh.itemOptions.add(new Item.ItemOption(87,1));
                InventoryServiceNew.gI().addItemBag(player, Nhanthanlinh);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Nhanthanlinh.template.name);
                            }}
        if (this.zone.map.mapId >= 202 && this.zone.map.mapId <= 203) {
            if(Util.isTrue(10, 100)){
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (2152));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(30, 1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhxd.template.name);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level > 50 && player.clan.banDoKhoBau.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x -10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x+40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 457, 1 , this.location.x +50, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 135 && this.zone.map.mapId <= 138 && player.clan.banDoKhoBau.level >= 108) {
            if(Util.isTrue(1, 200)){
            int[] idThienSus = new int[]{1048, 1051, 1054, 1057, 1060, 1049, 1052, 1055, 1058, 1061, 1050, 1053, 1056, 1059, 1062};
            int randomidThienSu = new Random().nextInt(idThienSus.length);
            int idThienSu = idThienSus[randomidThienSu];

              ItemMap itemMap;
              itemMap = Util.ratiItem(zone,(short) (idThienSu), 1, this.location.x, this.location.y, player.id);
              if (idThienSu == 1048 || idThienSu == 1049 || idThienSu == 1050) {
              itemMap.options.add(new Item.ItemOption(47, Util.nextInt(8000, 10000)));}
              else if (idThienSu == 1051 || idThienSu == 1052 || idThienSu == 1053) {
              itemMap.options.add(new Item.ItemOption(22, Util.nextInt(80, 110)));}
              else if (idThienSu == 1054 || idThienSu == 1055 || idThienSu == 1056) {
              itemMap.options.add(new Item.ItemOption(0, Util.nextInt(16000, 19000)));}
              else if (idThienSu == 1057 || idThienSu == 1058 || idThienSu == 1059) {
              itemMap.options.add(new Item.ItemOption(23, Util.nextInt(80, 110)));}
              else {
              itemMap.options.add(new Item.ItemOption(14, Util.nextInt(18, 26)));}
              
              itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
              itemMap.options.add(new Item.ItemOption(21, 180));
              itemMap.options.add(new Item.ItemOption(208, 1));
               if(Util.isTrue(1, 5)){
              itemMap.options.add(new Item.ItemOption(227, 1));
              itemMap.options.add(new Item.ItemOption(225, 1));
               }
                Service.gI().dropItemMap(this.zone, itemMap);
         }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -50, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level > 50 && player.clan.khiGas.level < 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +50, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -100, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -90, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -80, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -70, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -60, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -50, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x -10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +10, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +20, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +30, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x+40, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +50, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +60, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +70, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +80, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +90, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
            if(Util.isTrue(100, 100)){
                ItemMap it = new ItemMap(this.zone, 861, 10000 , this.location.x +100, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.getInstance().dropItemMap(this.zone, it);
                            }}
        if (this.zone.map.mapId >= 85 && this.zone.map.mapId <= 91) {
            if(Util.isTrue(1, 1000)){
                if(Util.isTrue(1, 50)){
                int[] itemDoHKs = new int[]{2033,2034,2035,2036,2037,2038};
                int randomDoHK = new Random().nextInt(itemDoHKs.length);
                Item DoHK = ItemService.gI().createNewItem((short) (itemDoHKs[randomDoHK]));
                InventoryServiceNew.gI().addItemBag(player, DoHK);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+DoHK.template.name);
                            }}}
        if (this.zone.map.mapId >= 85 && this.zone.map.mapId <= 91) {
            if(Util.isTrue(1, 1000)){
                if(Util.isTrue(1, 30)){
                int[] itemDaSKHs = new int[]{2039,2040,2041};
                int randomDaSKH = new Random().nextInt(itemDaSKHs.length);
                Item DaSKH = ItemService.gI().createNewItem((short) (itemDaSKHs[randomDaSKH]));
                InventoryServiceNew.gI().addItemBag(player, DaSKH);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+DaSKH.template.name);
                            }}}
        if (this.zone.map.mapId >= 85 && this.zone.map.mapId <= 91) {
            if(Util.isTrue(1, 1000)){
                if(Util.isTrue(1, 100)){
                Item DaSKHV = ItemService.gI().createNewItem((short) (2042));
                InventoryServiceNew.gI().addItemBag(player, DaSKHV);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+DaSKHV.template.name);
                            }}}
        if (this.zone.map.mapId >= 85 && this.zone.map.mapId <= 91) {
            if(Util.isTrue(1, 1000)){
                 if(Util.isTrue(1, 10)){
                int[] itemNROVIPs = new int[]{2093,2092,2091};
                int randomNROVIP = new Random().nextInt(itemNROVIPs.length);
                Item NROVIP = ItemService.gI().createNewItem((short) (itemNROVIPs[randomNROVIP]));
                InventoryServiceNew.gI().addItemBag(player, NROVIP);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+NROVIP.template.name);
                            }}}
        if (this.zone.map.mapId >= 171 && this.zone.map.mapId <= 173) {
            if(Util.isTrue(50, 100)){
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (590));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(30, 1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhxd.template.name);
                            }}
       if (this.zone.map.mapId >= 147 && this.zone.map.mapId <= 152 && player.clan.khiGas.level >= 100) {
         if(Util.isTrue(1, 300)){
            int[] idThienSus = new int[]{1048, 1051, 1054, 1057, 1060, 1049, 1052, 1055, 1058, 1061, 1050, 1053, 1056, 1059, 1062};
            int randomidThienSu = new Random().nextInt(idThienSus.length);
            int idThienSu = idThienSus[randomidThienSu];

              ItemMap itemMap;
              itemMap = Util.ratiItem(zone,(short) (idThienSu), 1, this.location.x, this.location.y, player.id);
              if (idThienSu == 1048 || idThienSu == 1049 || idThienSu == 1050) {
              itemMap.options.add(new Item.ItemOption(47, Util.nextInt(8000, 10000)));}
              else if (idThienSu == 1051 || idThienSu == 1052 || idThienSu == 1053) {
              itemMap.options.add(new Item.ItemOption(22, Util.nextInt(80, 110)));}
              else if (idThienSu == 1054 || idThienSu == 1055 || idThienSu == 1056) {
              itemMap.options.add(new Item.ItemOption(0, Util.nextInt(16000, 19000)));}
              else if (idThienSu == 1057 || idThienSu == 1058 || idThienSu == 1059) {
              itemMap.options.add(new Item.ItemOption(23, Util.nextInt(80, 110)));}
              else {
              itemMap.options.add(new Item.ItemOption(14, Util.nextInt(18, 26)));}
              
              itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
              itemMap.options.add(new Item.ItemOption(21, 180));
              itemMap.options.add(new Item.ItemOption(208, 1));
               if(Util.isTrue(1, 5)){
              itemMap.options.add(new Item.ItemOption(227, 1));
              itemMap.options.add(new Item.ItemOption(225, 1));
               }
                Service.gI().dropItemMap(this.zone, itemMap);
         }}
        if (this.zone.map.mapId >= 204 && this.zone.map.mapId <= 210) {
            if(Util.isTrue(10, 100)){
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (2044));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(30, 1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhxd.template.name);
                            }}
        if (this.tempId == 0) {
                player.achievement.plusCount(7);
            }if (this.tempId == 7 || this.tempId == 8 || this.tempId == 9 || this.tempId == 10 || this.tempId == 11 || this.tempId == 12) {
                player.achievement.plusCount(6);
            }
        if (this.zone.map.mapId >= 212 && this.zone.map.mapId <= 215) {
            if(Util.isTrue(10, 100)){
                Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (1318));
                Quanthanlinhxd.itemOptions.add(new Item.ItemOption(30, 1));
                InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được "+Quanthanlinhxd.template.name);
                            }}
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
            case ConstMob.CABIRA:
            case ConstMob.TOBI:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_33_0) {
                    itemMap = new ItemMap(this.zone, 993, 1, this.location.x, this.location.y, player.id);
                }
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_33_1) {
                    itemMap = new ItemMap(this.zone, 456, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

     public void sendMobStillAliveAffterAttacked(Mob mob, int dameHit, boolean crit, Player player) {
        Message msg;
        try {
            if (player != null) {
                player.checkHutMau();
                msg = new Message(-9);
                msg.writer().writeByte(mob.id);
                msg.writer().writeInt(mob.point.gethp());
                msg.writer().writeInt(dameHit);
                msg.writer().writeBoolean(crit); // chí mạng
                msg.writer().writeByte(player.checkHutMau() ? 37 : -1);
                Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
                msg.cleanup();
            }
         } catch (Exception e) {
        }
    }
    
      public void sendMobToCaiBinh(Player player, Mob mob, int timeSocola) {
        Message message = null;
        try {
            message = new Message(-112);
            message.writer().writeByte(1);
            message.writer().writeByte(mob.id); //mob id
            message.writer().writeShort(11175); //icon socola
            Service.getInstance().sendMessAllPlayerInMap(player, message);
            message.cleanup();
            mob.effectSkill.setCaiBinhChua(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) {           
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void sendPlayerToCaiBinh(Player player, int time) {
        if (player.effectSkill != null) {
            player.effectSkill.isCaiBinhChua = true;
            player.effectSkill.timeCaiBinhChua = time;
            player.effectSkill.lastTimeCaiBinhChua = System.currentTimeMillis();
            Service.getInstance().Send_Caitrang(player);
        }
    }
}
