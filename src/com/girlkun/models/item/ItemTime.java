package com.girlkun.models.item;

import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.services.ItemTimeService;


public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 30;
    public static final byte TX = 33;
    public static final byte CL = 34;
    public static final byte BAN_DO_KHO_BAU = 31;
    public static final byte KHI_GASS= 2;
    
    public static final int TIME_ITEM = 600000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_EXP = 1800000;

    private Player player;

    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    
    public boolean isUseBoHuyetSC;
    public boolean isUseBoKhiSC;
    public boolean isUseGiapXenSC;
    public boolean isUseCuongNoSC;
    public boolean isUseAnDanhSC;
    
     public boolean isUsex2;
    public boolean isUsex5;
    public boolean isUsex7;
    public boolean isUsex10;
    
    public long lastTimex2;
    public long lastTimex5;
    public long lastTimex7;
    public long lastTimex10;
    
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;

    
    public long lastTimeBoHuyetSC;
    public long lastTimeBoKhiSC;
    public long lastTimeGiapXenSC;
    public long lastTimeCuongNoSC;
    public long lastTimeAnDanhSC;
    
    
    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    
    
    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;
    
    public boolean istrbsd;
    public boolean istrbhp;
    public boolean istrbki;
    public static final int TIME_TRB = 1800000;
    
    public long lastTimetrbsd;
    public long lastTimetrbhp;
    public long lastTimetrbki;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (isUsex2) {
            if (Util.canDoWithTime(lastTimex2, TIME_ITEM)) {
                isUsex2 = false;
                Service.gI().point(player);
            }
        }
        
        if (isUsex5) {
            if (Util.canDoWithTime(lastTimex5, TIME_EXP)) {
                isUsex5 = false;
                Service.gI().point(player);
            }
        }
       
        if (isUsex7) {
            if (Util.canDoWithTime(lastTimex7, TIME_EXP)) {
                isUsex7 = false;
            }
        }
        if (isUsex10) {
            if (Util.canDoWithTime(lastTimex10, TIME_EXP)) {
                isUsex10 = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
            }
        }
        
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }
       
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
       
         if (isUseBoHuyetSC) {
            if (Util.canDoWithTime(lastTimeBoHuyetSC, TIME_ITEM)) {
                isUseBoHuyetSC = false;
                Service.getInstance().point(player);
//                Service.getInstance().Send_Info_NV(this.player);
            }
        }
        if (isUseBoKhiSC) {
            if (Util.canDoWithTime(lastTimeBoKhiSC, TIME_ITEM)) {
                isUseBoKhiSC = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseGiapXenSC) {
            if (Util.canDoWithTime(lastTimeGiapXenSC, TIME_ITEM)) {
                isUseGiapXenSC = false;
            }
        }
        if (isUseCuongNoSC) {
            if (Util.canDoWithTime(lastTimeCuongNoSC, TIME_ITEM)) {
                isUseCuongNoSC = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseAnDanhSC) {
            if (Util.canDoWithTime(lastTimeAnDanhSC, TIME_ITEM)) {
                isUseAnDanhSC = false;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
                if (istrbsd) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_TRB)) {
                istrbsd = false;
                Service.gI().point(player);
            }
        }
         
        if (istrbhp) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_TRB)) {
                istrbhp = false;
                Service.gI().point(player);
            }
        }
        
        if (istrbki) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_TRB)) {
                istrbki = false;
                Service.gI().point(player);
            }
        }
    }
    
    public void dispose(){
        this.player = null;
    }
}
