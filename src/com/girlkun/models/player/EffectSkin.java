package com.girlkun.models.player;

import com.girlkun.models.item.Item;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.ItemService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class EffectSkin {

    private static final String[] textOdo = new String[]{
        "Hôi quá", "Tránh ra đi thằng ở dơ", "Mùi gì kinh quá vậy?",
        "Kinh tởm quá", "Biến đi thằng ở dơ", "Kính ngài ở dơ"
    };
    private static final String[] textDraburaFrost = new String[]{ //Cải trang Drabura Frost
        "Ui lạnh quá..", "Đông cứng rồi", "Tránh xa ta ra",
    };
    private static final String[] textDrabura = new String[]{ //Cải trang Drabura
        "AAA Nặng Quá", "Chết tiệt", "Hóa đá rồi", "Tránh xa ta ra"
    };

    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }

    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimeXenHutHpKi;

    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;

    public boolean isVoHinh;

    public long lastTimeXHPKI;
    public int xHPKI;


    private long lastTimeHoaBang;  //Cải trang Dracula Frost
    private long lastTimeHoaDa; //Cải trang Dracula 


    public void update() {
        updateVoHinh();
        updateDraburaFrost();
        updateDrabura();
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
        }
        if (!this.player.isBoss && !this.player.isPet && !player.isNewPet) {
            updateTrainArmor();
        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.gI().point(player);
        }
    }

     private void updateDraburaFrost() {
        try {
            if (this.player.nPoint.isDraburaFrost == true) {
                if (Util.canDoWithTime(lastTimeHoaBang, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl)  && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaBang(pl, System.currentTimeMillis(), 5000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, (byte) EffectSkillService.BLIND_EFFECT);
                         Service.gI().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 11085, 5000 / 1000);
                    Service.gI().chat(player, "Ối Bạn Ơi , Hic !");
                    Service.gI().chat(pl, textDraburaFrost[Util.nextInt(0, textDraburaFrost.length - 1)]);
                    PlayerService.gI().sendInfoHpMpMoney(pl);
                    Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaBang = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    
    private void updateDrabura() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isDrabura == true) {
                if (Util.canDoWithTime(lastTimeHoaDa, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaDa(pl, System.currentTimeMillis(),5000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(),5000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                         Service.gI().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 4392, 5000 / 1000);
                    Service.gI().chat(player, "Bạn Bị Hoá Đá");
                        Service.gI().chat(pl, textDrabura[Util.nextInt(0, textDrabura.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaDa = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        int subHp = (int) (pl.nPoint.hpMax * param / 100);
                        int subMp = (int) (pl.nPoint.mpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = (int) (pl.nPoint.hp - 1);
                        }
                        if (subMp >= pl.nPoint.mp) {
                            subMp = (int) (pl.nPoint.mp - 1);
                        }
                        hpHut += subHp;
                        mpHut += subMp;
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        int subHp = (int) (pl.nPoint.hpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = (int) (pl.nPoint.hp - 1);
                        }
                        Service.gI().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }


    //giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().point(this.player);
        }
    }

    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
