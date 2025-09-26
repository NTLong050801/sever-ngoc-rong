package com.girlkun.models.boss;

import com.girlkun.consts.ConstPlayer;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.CHAT_E;
import static com.girlkun.models.boss.BossStatus.CHAT_S;
import static com.girlkun.models.boss.BossStatus.DIE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.LEAVE_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import static com.girlkun.models.boss.BossStatus.REST;
import static com.girlkun.models.boss.BossStatus.WAIT;
import com.girlkun.models.boss.iboss.IBossNew;
import com.girlkun.models.boss.iboss.IBossOutfit;
import com.girlkun.models.boss.list_boss.android.SuperAndroid17;
import com.girlkun.models.boss.list_boss.New.gogeta;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.Objects;
import lombok.Data;

@Data

public class Boss extends Player implements IBossNew, IBossOutfit {

    public int currentLevel = 0;
    public BossData[] data;

    public BossStatus bossStatus;
    public Zone lastZone;
    private long lastTimeRest;
    private int secondsRest;
    private int secondsNotify;
    private long lastTimeChatS;
    private int timeChatS;
    private byte indexChatS;
    private long lastTimeChatE;
    private int timeChatE;
    private byte indexChatE;
    private long lastTimeChatM;
    private int timeChatM;
    private long lastTimeNotify;
    private int typeBoss;
    public long lastTimeTargetPlayer;
    public int timeTargetPlayer;
    private long timeToRestart;
    public Player playerTarget;
    public Player lockPlayerTarget;
    public Boss parentBoss;
    public Boss[][] bossAppearTogether;
    public Zone zoneFinal = null;
    protected Boss bossInstance;

    public Boss(int type, BossData... data) throws Exception {
        initializeBoss(type, data);
    }

    private void initializeBoss(int type, BossData... data) throws Exception {
        this.id = type;
        this.isBoss = true;

        if (data == null || data.length == 0) {
            throw new Exception("Dữ liệu boss không hợp lệ");
        }
        this.typeBoss = type;
        this.data = data;
        this.secondsRest = data[0].getSecondsRest();
        this.secondsNotify = data[0].getSecondsNotify();
        this.bossStatus = BossStatus.REST;
        BossManager.gI().addBoss(this);
        bossInstance = this;
        createBossAppearTogether();
    }

    private void createBossAppearTogether() {
         this.bossAppearTogether = new Boss[this.data.length][];
        for (int i = 0; i < this.bossAppearTogether.length; i++) {
            if (this.data[i].getBossesAppearTogether() != null) {
                this.bossAppearTogether[i] = new Boss[this.data[i].getBossesAppearTogether().length];
                for (int j = 0; j < this.data[i].getBossesAppearTogether().length; j++) {
                    Boss boss = BossManager.gI().createBoss(this.data[i].getBossesAppearTogether()[j]);
                    if (boss != null) {
                        boss.parentBoss = this;
                        this.bossAppearTogether[i][j] = boss;
                    }
                }
            }
        }
    }

    @Override
    public void initBase() {
        BossData dataBoss = this.data[this.currentLevel];
        this.name = String.format(dataBoss.getName(), Util.nextInt(0, 100));
        this.gender = dataBoss.getGender();
        this.nPoint.mpg = 11_12_2005;
        this.nPoint.dameg = dataBoss.getDame();
        this.nPoint.hpg = dataBoss.getHp()[Util.nextInt(0, dataBoss.getHp().length - 1)];
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    protected void initSkill() {
        for (Skill skill : this.playerSkill.skills) {
            skill.dispose();
        }
        this.playerSkill.skills.clear();
        this.playerSkill.skillSelect = null;
        int[][] skillTemp = data[this.currentLevel].getSkillTemp();
        for (int[] skillTemp1 : skillTemp) {
            Skill skill = SkillUtil.createSkill(skillTemp1[0], skillTemp1[1]);
            if (skillTemp1.length == 3) {
                skill.coolDown = skillTemp1[2];
            }
            this.playerSkill.skills.add(skill);
        }
    }

    protected void resetBase() {
        this.lastTimeChatS = 0;
        this.lastTimeChatE = 0;
        this.timeChatS = 0;
        this.timeChatE = 0;
        this.indexChatS = 0;
        this.indexChatE = 0;
    }

    //.outfit.
    @Override
    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        if (this.typeBoss == BossID.SUPER_ANDROID_17) {
            if (((SuperAndroid17) this).isFusion) {
                return 636;
            }
        }
        if (this.typeBoss == BossID.gogeta) {
            if (((gogeta) this).isFusion) {
                return 1440;
            }
        }
        return this.data[this.currentLevel].getOutfit()[0];
    }

    @Override
    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        if (this.typeBoss == BossID.SUPER_ANDROID_17) {
            if (((SuperAndroid17) this).isFusion) {
                return 637;
            }
        }
        if (this.typeBoss == BossID.gogeta) {
            if (((gogeta) this).isFusion) {
                return 1441;
            }
        }
        return this.data[this.currentLevel].getOutfit()[1];
    }

    @Override
    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        if (this.typeBoss == BossID.SUPER_ANDROID_17) {
            if (((SuperAndroid17) this).isFusion) {
                return 638;
            }
        }
        if (this.typeBoss == BossID.gogeta) {
            if (((gogeta) this).isFusion) {
                return 1442;
            }
        }
        return this.data[this.currentLevel].getOutfit()[2];
    }

    @Override
    public short getFlagBag() {
        return this.data[this.currentLevel].getOutfit()[3];
    }

    @Override
    public byte getAura() {
        return (byte) this.data[this.currentLevel].getOutfit()[4];
    }

    @Override
    public byte getEffFront() {
        return (byte) this.data[this.currentLevel].getOutfit()[5];
    }

//    public Zone getMapJoin() {
//        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
//        Zone map = MapService.gI().getMapWithRandZone(mapId);
//        return map;
//    }
    public Zone getMapJoin() {
        int[] mapJoinArray = this.data[this.currentLevel].getMapJoin();
        if (mapJoinArray != null && mapJoinArray.length > 0) {
            int mapId = mapJoinArray[Util.nextInt(0, mapJoinArray.length - 1)];
            Zone map = MapService.gI().getMapWithRandZone(mapId);
            return map;
        } else {
            return null;
        }
    }

    @Override
    public void changeStatus(BossStatus status) {
        this.bossStatus = status;
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarget != null && (this.playerTarget.isDie() || !this.zone.equals(this.playerTarget.zone))) {
            this.playerTarget = null;
        }
        if (this.playerTarget == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarget = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarget != null && this.playerTarget.effectSkin != null && this.playerTarget.effectSkin.isVoHinh) {
            this.playerTarget = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        if (this.playerTarget == this.pet) {
            this.playerTarget = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        return this.playerTarget;
    }

    @Override
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }

    @Override
    public void changeToTypeNonPK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
    }

    public void bossNotify() {
        if (this.secondsNotify == 0) {
            return;
        }
        if (this != null && !this.isDie() && this.zone != null && Util.canDoWithTime(this.lastTimeNotify, this.secondsNotify * 1000)) {
            this.lastTimeNotify = System.currentTimeMillis();
            if (timeToRestart == -1 && data[0].getBossesAppearTogether() != null && data[0].getBossesAppearTogether().length > 1) {
                for (Boss boss : bossAppearTogether[0]) {
                    if (boss != null && boss.zone != null) {
                        ServerNotify.gI().notify("BOSS " + boss.name + " vừa xuất hiện tại " + boss.zone.map.mapName);
                    }
                }
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            } else if (this.data[0].getBossesAppearTogether() == null) {
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        }
    }

    @Override
    public void updateBoss() {
        super.update();
        bossNotify();
        this.nPoint.mp = this.nPoint.mpg;
        if (this.effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.bossStatus) {
            case REST:
                this.rest();
                break;
            case RESPAWN:
                this.respawn();
                this.changeStatus(BossStatus.JOIN_MAP);
                break;
            case JOIN_MAP:
                this.joinMap();
                this.changeStatus(BossStatus.CHAT_S);
                System.out.println(this.name + " Xuất hiện ở " + this.zone.map.mapName + ", Khu: " + this.zone.zoneId);
                break;
            case CHAT_S:
                if (chatS()) {
                    this.doneChatS();
                    this.lastTimeChatM = System.currentTimeMillis();
                    this.timeChatM = 5000;
                    this.changeStatus(BossStatus.ACTIVE);
                }
                break;
            case ACTIVE:
                this.chatM();
                if (this.effectSkill.isCharging && !Util.isTrue(1, 20) || this.effectSkill.useTroi) {
                    return;
                }
                this.active();
                break;
            case WAIT:
                this.SpawnCombat();
                break;
            case DIE:
                this.changeStatus(BossStatus.CHAT_E);
                break;
            case CHAT_E:
                if (chatE()) {
                    this.doneChatE();
                    this.changeStatus(BossStatus.LEAVE_MAP);
                }
                break;
            case LEAVE_MAP:
                this.leaveMap();
                break;
        }
    }

    //loop
    @Override
    public void rest() {
//        int nextLevel = this.currentLevel + 1;
//        if (nextLevel >= this.data.length) {
//            nextLevel = 0;
//        }
        if (this.data[0].getTypeAppear() == TypeAppear.DEFAULT_APPEAR) {
            if (Util.canDoWithTime(lastTimeRest, secondsRest * 1000)) {
                changeStatus(BossStatus.RESPAWN);
            }
        }
    }

    @Override
    public void respawn() {
        this.initBase();
        this.changeToTypeNonPK();
    }

    @Override
    public void joinMap() {
if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.getInstance().sendFlagBag(this);
            this.notifyJoinMap();
        }
    }

    public void joinMapByZone(Player player) {
        if (player.zone != null) {
            this.zone = player.zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }

    }

    public void joinMapByZone(Zone zone) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    public void joinMapByZoneWithXY(Zone zone, short x, short y) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMap(this, zone, x, y);
        }
    }

    protected void notifyJoinMap() {
        if (this.id >= -22 && this.id <= -20) {
            return;
        }
        if (this.typeBoss == BossID.PIC) {
            return;
        }
        if (this.zone.map.mapId == 140
                || MapService.gI().isMapMaBu(this.zone.map.mapId)
                || MapService.gI().isMapBlackBallWar(this.zone.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(this.zone.map.mapId)
                || MapService.gI().isMapDoanhTrai(this.zone.map.mapId)) {
            return;
        }
        ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        this.lastTimeNotify = System.currentTimeMillis();
    }

    @Override
    public boolean chatS() {
        if (Util.canDoWithTime(lastTimeChatS, timeChatS)) {
            if (this.indexChatS == this.data[this.currentLevel].getTextS().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextS()[this.indexChatS];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatS = System.currentTimeMillis();
            this.timeChatS = textChat.length() * 100;
            if (this.timeChatS > 2000) {
                this.timeChatS = 2000;
            }
            this.indexChatS++;
        }
        return false;
    }

    @Override
    public void doneChatS() {

    }

    @Override
    public void chatM() {
        if (this.typePk == ConstPlayer.NON_PK) {
            return;
        }
        if (this.data[this.currentLevel].getTextM().length == 0) {
            return;
        }
        if (!Util.canDoWithTime(this.lastTimeChatM, this.timeChatM)) {
            return;
        }
        String textChat = this.data[this.currentLevel].getTextM()[Util.nextInt(0, this.data[this.currentLevel].getTextM().length - 1)];
        int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
        textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
        this.chat(prefix, textChat);
        this.lastTimeChatM = System.currentTimeMillis();
        this.timeChatM = Util.nextInt(3000, 10000);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

    protected long lastTimeAttack;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public void checkPlayerDie(Player player) {
        if (player.isDie()) {
            this.chat("Chừa nha con!!!");
        }
    }


    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name);
            Logger.log(Logger.CYAN,plKill.name + " vừa tiêu diệt được " + this.name + "\n");
        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public boolean chatE() {
        if (Util.canDoWithTime(lastTimeChatE, timeChatE)) {
            if (this.indexChatE == this.data[this.currentLevel].getTextE().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextE()[this.indexChatE];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatE = System.currentTimeMillis();
            this.timeChatE = textChat.length() * 100;
            if (this.timeChatE > 2000) {
                this.timeChatE = 2000;
            }
            this.indexChatE++;
        }
        return false;
    }

    @Override
    public void doneChatE() {
    }

    @Override
    public void leaveMap() {
        if (this.currentLevel < this.data.length - 1) {
            this.lastZone = this.zone;
            this.currentLevel++;
            this.changeStatus(BossStatus.RESPAWN);
        } else {
            ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
            ChangeMapService.gI().exitMap(this);
            this.lastZone = null;
            this.lastTimeRest = System.currentTimeMillis();
            if (this.parentBoss == null) {
                BossManager.gI().setTimeSpawnBoss.put(typeBoss, System.currentTimeMillis() + (secondsRest * 1000));
            }
            bossInstance = null;
            BossManager.gI().removeBoss(this);
        }
        this.wakeupAnotherBossWhenDisappear();
    }

    //end loop
    @Override
    public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
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
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public void chat(String text) {
        Service.getInstance().chat(this, text);
    }

    protected boolean chat(int prefix, String textChat) {
        if (prefix == -1) {
            this.chat(textChat);
        } else if (prefix == -2) {
            if (this.zone == null) {
                return false;
            }
            Player plMap = this.zone.getRandomPlayerInMap();
            if (plMap != null && !plMap.isDie() && Util.getDistance(this, plMap) <= 600) {
                Service.getInstance().chat(plMap, textChat);
            } else {
                return false;
            }
        } else if (prefix == -3) {
            if (this.parentBoss != null && !this.parentBoss.isDie()) {
                this.parentBoss.chat(textChat);
            }
        } else if (prefix >= 0) {
            if (this.bossAppearTogether != null && this.bossAppearTogether[this.currentLevel] != null) {
                Boss boss = this.bossAppearTogether[this.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            } else if (this.parentBoss != null && this.parentBoss.bossAppearTogether != null
                    && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                Boss boss = this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            }
        }
        return true;
    }

    @Override
    public void wakeupAnotherBossWhenAppear() {

        if (!MapService.gI().isMapMaBu(this.zone.map.mapId) && MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
            System.out.println("BOSS " + this.name + " : " + this.zone.map.mapName + " khu vực " + this.zone.zoneId + "(" + this.zone.map.mapId + ")");
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            int nextLevelBoss = boss.currentLevel + 1;
            if (nextLevelBoss >= boss.data.length) {
                nextLevelBoss = 0;
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.CALL_BY_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.APPEAR_WITH_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
                boss.changeStatus(BossStatus.RESPAWN);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentLevel, data, bossStatus, lastZone);
    }

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        Logger.log(Logger.GREEN,"Boss " + this.name + " đã chết" + "\n");
    }

    @Override
    public void SpawnCombat() {
        System.out.println("ahihi");
    }

}
