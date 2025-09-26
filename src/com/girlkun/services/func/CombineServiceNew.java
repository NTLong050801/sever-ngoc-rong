package com.girlkun.services.func;

import com.girlkun.services.Service;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.RewardService;
import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.network.io.Message;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class CombineServiceNew {

    private static final int COST_DAP_DO_KICH_HOAT = 500000000;

    private static final int COST = 500000000;

    private static final int TIME_COMBINE = 1;

    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int SET_KICH_HOAT_HOANG_KIM = 310800;
    
    public static final int THO_REN = 310801;
    public static final int PHAP_SU = 310802;
    public static final int THO_SAN = 310803;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_CHAN_MENH = 523;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int NANG_CAP_BONG_TAI3 = 5101;
    public static final int MO_CHI_SO_BONG_TAI3 = 5109;
    public static final int NANG_CAP_BONG_TAI4 = 51100;
    public static final int MO_CHI_SO_BONG_TAI4 = 51009;
    public static final int NANG_CAP_BONG_TAI5 = 510001;
    public static final int MO_CHI_SO_BONG_TAI5 = 510009;
    public static final int Kham = 517;
    public static final int TKham = 518;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int DAP_SET_KICH_HOAT = 525;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int CHE_TAO_TRANG_BI_HK = 521;
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;
    private static final int RATIO_BONG_TAI3 = 40;
    private static final int RATIO_NANG_CAP3 = 35;
    private static final int RATIO_BONG_TAI4 = 35;
    private static final int RATIO_NANG_CAP4 = 30;
    private static final int RATIO_BONG_TAI5 = 10;
    private static final int RATIO_NANG_CAP5 = 10;
    private final Npc baHatMit2;
    private final Npc baHatMit;
    private final Npc chanmenh;
    private final Npc npsthiensu64;
    private final Npc npcthiensu65;
    private final Npc tosukaio;
    private final Npc thoNghe;
    private final Npc tk;
    private final Npc hd;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.chanmenh = NpcManager.getNpc(ConstNpc.CHAN_MENH);
        this.baHatMit2 = NpcManager.getNpc(ConstNpc.BA_HAT_MIT2);
        this.tosukaio = NpcManager.getNpc(ConstNpc.TO_SU_KAIO);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
        this.npcthiensu65 = NpcManager.getNpc(ConstNpc.VADOS);
        this.tk = NpcManager.getNpc(ConstNpc.HK);
        this.hd = NpcManager.getNpc(ConstNpc.HD);
        this.thoNghe = NpcManager.getNpc(ConstNpc.NGHE_NGHIEP);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1318) {
                            manhVo = item;
                        } else if (item.template.id >= 1351 && item.template.id <= 1358) {
                            bongTai = item;
                            star = item.template.id - 1351;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1358) {
                        this.chanmenh.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hồn Hoàn đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DaNangcap = getDaNangcapChanmenh(star);
                    player.combineNew.TileNangcap = getTiLeNangcapChanmenh(star);
                    if (bongTai != null && manhVo != null && (bongTai.template.id >= 1300 && bongTai.template.id < 1358)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        chanmenh.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.DaNangcap + " Đá Hoàng Kim");
                    } else {
                        this.chanmenh.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Hồn Hoàn và Đá Hoàng Kim", "Đóng");
                    }
                } else {
                    this.chanmenh.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Hồn Hoàn và Đá Hoàng Kim", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI3:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 2134) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI3;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2 và X99 Mảnh vỡ bông tai cấp 3", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2 và X99 Mảnh vỡ bông tai cấp 3", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI3:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2129) {
                            bongTai = item;
                        } else if (item.template.id == 2030) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP3;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 3, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI5:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2130) {
                            bongTai = item;
                        } else if (item.template.id == 2136) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI5;

                        String npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 4 và X99 Mảnh vỡ bông tai cấp 5", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 4 và X99 Mảnh vỡ bông tai cấp 5", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI5:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2131) {
                            bongTai = item;
                        } else if (item.template.id == 2030) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP5;

                        String npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 5, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 5, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                }
                break;
            case TKham:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isDHK()) {
                            bongTai = item;
                        } else if (item.template.id == 1199) {
                            manhHon = item;
                        } else if (item.template.id == 1198) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP5;

                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            hd.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            hd.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.hd.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 món hoàng kim, X99 Lọ thuốc tẩy và 1 Lọ thuốc tăng cấp", "Đóng");
                    }
                } else {
                    this.hd.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 món hoàng kim, X99 Lọ thuốc tẩy và 1 Lọ thuốc tăng cấp", "Đóng");
                }
                break;
            case Kham:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isDHK()) {
                            bongTai = item;
                        } else if (item.template.id == 1198) {
                            manhHon = item;
                        } else if (item.template.id == 1197) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP5;

                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            hd.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            hd.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.hd.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 món hoàng kim, X99 Lọ thuốc tăng cấp và 1 Đá tăng cấp ", "Đóng");
                    }
                } else {
                    this.hd.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 món hoàng kim, X99 Lọ thuốc tăng cấp và 1 Đá tăng cấp ", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI4:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2129) {
                            bongTai = item;
                        } else if (item.template.id == 2135) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI4;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3 và X99 Mảnh vỡ bông tai cấp 4", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 3 và X99 Mảnh vỡ bông tai cấp 4", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI4:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 2130) {
                            bongTai = item;
                        } else if (item.template.id == 2030) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP4;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 4, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 4, X99 Đá Ma Thuật và 1 Đá xanh lam", "Đóng");
                }
                break;

            case CHE_TAO_TRANG_BI_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Tuandz", "Yes");
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() < 1) {
                        this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).count() < 1) {
                        this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " + mTS.quantity + "/99\n";
                    if (daNC != null) {

                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npcthiensu65.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npcthiensu65.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;
            case CHE_TAO_TRANG_BI_HK:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Tuandz", "Yes");
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip1()).count() < 1) {
                        this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Thánh chỉ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS1() && item.quantity >= 99).count() < 1) {
                        this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Phiếu đồ Hoàng kim", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS1()) {
                                mTS = item;
                            } else if (item.isDaNangCap1()) {
                                daNC = item;
                            } else if (item.isDaMayMan1()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 25;
                    int tilenew = tilemacdinh;
                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS1).findFirst().get().typeNameManh() + " Hoàng kim "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip1).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " + mTS.quantity + "/99\n";
                    if (daNC != null) {

                        npcSay += "|2|Ngọc bội " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap1).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1186) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Lọ thuốc bí ẩn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan1).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1191) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1186) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.tk.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.tk.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;
            case DAP_SET_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
                    Item dhd = null, dtl = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 650 && item.template.id <= 662) {
                                dhd = item;
                            } else if (item.template.id >= 555 && item.template.id <= 567) {
                                dtl = item;
                            }
                        }
                    }
                    if (dhd != null) {
                        String npcSay = "|6|" + dhd.template.name + "\n";
                        for (Item.ItemOption io : dhd.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        if (dtl != null) {
                            npcSay += "|6|" + dtl.template.name + "\n";
                            for (Item.ItemOption io : dtl.itemOptions) {
                                npcSay += "|2|" + io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "Ngươi có muốn chuyển hóa thành\n";
                        npcSay += "|1|" + getNameItemC0(dhd.template.gender, dhd.template.type)
                                + " (ngẫu nhiên kích hoạt)\n|7|Tỉ lệ thành công " + (dtl != null ? "100%" : "40%") + "\n|2|Cần "
                                + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT) + " vàng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 1", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 2", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
                case SET_KICH_HOAT_HOANG_KIM:
    if (player.combineNew.itemsCombine.size() == 2) {
        Item trangBi = null;
        Item daSKH = null;

        for (Item item : player.combineNew.itemsCombine) {
            if (isTrangBiHoangKimzin(item)) {
                trangBi = item;
            } else if (isDaSKH(item)) {
                daSKH = item;
            }
        }

        // Lấy số lượng item có ID 457 trong rương đồ
        Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
        int soLuong = 0;
        if (tv != null) {
        soLuong = tv.quantity;}
        // Nếu đủ điều kiện để nâng cấp
        if (trangBi != null && daSKH != null) {
            if (soLuong >= 1000) {
                // Cho phép nâng cấp, hiển thị menu xác nhận
                String npcSay = "Bạn có muốn kích hoạt Hoàng Kim không?\n";
                npcSay += "|2|" + trangBi.template.name + "\n";
                for (Item.ItemOption io : trangBi.itemOptions) {
                    npcSay += io.getOptionString() + "\n";
                }
                npcSay += "|7|" + daSKH.template.name + "\n";
                npcSay += "|1|Cần 1000 thỏi vàng.";
                
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Từ chối");
            } else {
                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần 1000 thỏi vàng để nâng cấp", "Đóng");
            }

        } else if (trangBi == null && daSKH == null) {
            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 trang bị Hoàng Kim *zin* và 1 loại ngọc thánh để ép vào", "Đóng");
        } else if (trangBi != null && daSKH == null) {
            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 loại ngọc thánh để ép vào", "Đóng");
        } else {
            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 trang bị Hoàng Kim *zin* để ép vào", "Đóng");
        }

    } else {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            "Cần đúng 1 trang bị Hoàng Kim *zin* và 1 ngọc thánh", "Đóng");
    }
    break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc\n" + "1 lần", "Nâng cấp\n" + (player.combineNew.gemCombine * 10) + " ngọc\n" + "10 Lần", "Nâng cấp\n" + (player.combineNew.gemCombine * 100) + " ngọc\n" + "100 lần");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.type == 12) && (item.template.id != 14) && (item.template.id != 2091) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit2.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép");
                        } else {
                            this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit2.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC);
                            }
                        } else {
                            this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;

            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
        // tho ren
            
            case THO_REN:
                if (player.combineNew.itemsCombine.isEmpty()) {
                    this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho ta kiếm hoặc áo giáp, ta sẽ rèn cho nó xịn hơn.", "Đóng");
                    return;
                }
                    Item itemKiem = null;
                    Item itemAoGiap = null;
                    Item itemDaRen = null;
                    Item itemThoMM = null;
                if (player.combineNew.itemsCombine.size() == 3) {
                    for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 & item.template.id > 2119) {
                itemKiem = item;
            } else if (item.template.id <= 2141 & item.template.id > 2136) {
                itemAoGiap = item;
            } else if (item.template.id == 2128) 
            {itemDaRen = item;}
            else if (item.template.id == 2115) 
            {itemThoMM = item;} 
        } 
            if (itemDaRen != null && itemThoMM != null &&(itemAoGiap != null || itemKiem != null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    "Con có muốn nâng cấp đồ này không?", "Rèn đồ\nCần 1000 thỏi vàng."
                    )
                    ;}
            else if (itemDaRen != null && itemThoMM != null &&(itemAoGiap == null && itemKiem == null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trang bị đặc biệt.", "Đóng");
}
            else if (itemDaRen == null || itemThoMM == null &&(itemAoGiap != null || itemKiem != null))
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu nâng cấp.", "Đóng");
}
                } else if (player.combineNew.itemsCombine.size() > 3)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần trang bị đặc biệt\n thỏ may mắn\n và đá rèn.", "Đóng");
}
                else if (player.combineNew.itemsCombine.size() < 3)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu rồi.", "Đóng");
}
       break;
       case THO_SAN:
                if (player.combineNew.itemsCombine.isEmpty()) {
                    this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho ta kiếm hoặc áo giáp, ta sẽ rèn cho nó xịn hơn.", "Đóng");
                    return;
                }
                    Item itemKiem1 = null;
                    Item itemAoGiap1 = null;
                    Item itemNTS = null;
                    Item itemTuiTra = null;
                if (player.combineNew.itemsCombine.size() == 3) {
                    for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 & item.template.id > 2119) {
                itemKiem1 = item;
            } else if (item.template.id <= 2141 & item.template.id > 2136) {
                itemAoGiap1 = item;
            } else if (item.template.id == 2125) 
            {itemNTS = item;}
            else if (item.template.id == 2119) 
            {itemTuiTra = item;} 
        } 
            if (itemNTS != null && itemTuiTra != null &&(itemAoGiap1 != null || itemKiem1 != null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    "Con có muốn nâng cấp đồ này không?", "Mài dũa\nCần 1000 thỏi vàng."
                    )
                    ;}
            else if (itemNTS != null && itemTuiTra != null &&(itemAoGiap1 == null && itemKiem1 == null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trang bị đặc biệt.", "Đóng");
}
            else if (itemNTS == null || itemTuiTra == null &&(itemAoGiap1 != null || itemKiem1 != null))
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu nâng cấp.", "Đóng");
}
                } else if (player.combineNew.itemsCombine.size() > 3)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần trang bị đặc biệt\n ngọc thợ săn\n và túi trà.", "Đóng");
}
                else if (player.combineNew.itemsCombine.size() < 3)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu rồi.", "Đóng");
}
       break;
        case PHAP_SU:
                if (player.combineNew.itemsCombine.isEmpty()) {
                    this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho ta kiếm hoặc áo giáp, ta sẽ rèn cho nó xịn hơn.", "Đóng");
                    return;
                }
                    Item itemKiem2 = null;
                    Item itemAoGiap2 = null;
                    Item itemLinhDich = null;
                if (player.combineNew.itemsCombine.size() == 2) {
                    for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 & item.template.id > 2119) {
                itemKiem2 = item;
            } else if (item.template.id <= 2141 & item.template.id > 2136) {
                itemAoGiap2 = item;
            } else if (item.template.id == 2117) 
            {itemLinhDich = item;}
        } 
            if (itemLinhDich != null &&(itemAoGiap2 != null || itemKiem2 != null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    "Con có muốn nâng cấp đồ này không?", "Mài dũa\nCần 1000 thỏi vàng."
                    )
                    ;}
            else if ( itemLinhDich != null &&(itemAoGiap2 == null && itemKiem2 == null))
            {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trang bị đặc biệt.", "Đóng");
}
            else if ( itemLinhDich == null &&(itemAoGiap2 != null || itemKiem2 != null))
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu nâng cấp.", "Đóng");
}
                } else if (player.combineNew.itemsCombine.size() > 2)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần trang bị đặc biệt\n Linh dịch và túi trà.", "Đóng");
}
                else if (player.combineNew.itemsCombine.size() < 2)
                {this.thoNghe.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu nguyên liệu rồi.", "Đóng");
}
       break;
        }
        
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case SET_KICH_HOAT_HOANG_KIM:
                kichHoatHoangKim(player);
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;

            case DAP_SET_KICH_HOAT:
                dapDoKichHoat(player);
                break;

            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break;
            case CHE_TAO_TRANG_BI_HK:
                openCreateItemHK(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
            case NANG_CAP_BONG_TAI3:
                nangCapBongTai3(player);
                break;
            case MO_CHI_SO_BONG_TAI3:
                moChiSoBongTai3(player);
                break;
            case NANG_CAP_BONG_TAI4:
                nangCapBongTai4(player);
                break;
            case NANG_CAP_CHAN_MENH:
                nangCapChanMenh(player);
                break;
            case MO_CHI_SO_BONG_TAI4:
                moChiSoBongTai4(player);
                break;
            case NANG_CAP_BONG_TAI5:
                nangCapBongTai5(player);
                break;
            case MO_CHI_SO_BONG_TAI5:
                moChiSoBongTai5(player);
                break;
            case Kham:
                Kham(player);
                break;
            case TKham:
                TKham(player);
                break;
            case THO_REN:
                ThoRen(player);
                break;
            case THO_SAN:
                ThoSan(player);
                break;
            case PHAP_SU:
                PhapSu(player);
                break;
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    public void khilv2(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 18));//sd
        item.itemOptions.add(new ItemOption(77, 18));//hp
        item.itemOptions.add(new ItemOption(103, 18));//ki
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv3(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 20));//sd
        item.itemOptions.add(new ItemOption(77, 20));//hp
        item.itemOptions.add(new ItemOption(103, 20));//ki
        item.itemOptions.add(new ItemOption(14, 15));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv4(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 22));//sd
        item.itemOptions.add(new ItemOption(77, 22));//hp
        item.itemOptions.add(new ItemOption(103, 22));//ki
        item.itemOptions.add(new ItemOption(14, 20));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv5(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 25));//sd
        item.itemOptions.add(new ItemOption(77, 25));//hp
        item.itemOptions.add(new ItemOption(103, 25));//ki
        item.itemOptions.add(new ItemOption(14, 25));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv6(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 30));//sd
        item.itemOptions.add(new ItemOption(77, 30));//hp
        item.itemOptions.add(new ItemOption(103, 30));//ki
        item.itemOptions.add(new ItemOption(14, 30));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv7(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 35));//sd
        item.itemOptions.add(new ItemOption(77, 35));//hp
        item.itemOptions.add(new ItemOption(103, 35));//ki
        item.itemOptions.add(new ItemOption(14, 35));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void khilv8(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(50, 40));//sd
        item.itemOptions.add(new ItemOption(77, 40));//hp
        item.itemOptions.add(new ItemOption(103, 40));//ki
        item.itemOptions.add(new ItemOption(14, 40));
        if(Util.isTrue(1, 100)){
        item.itemOptions.add(new ItemOption(5, 10));//sdcm
        item.itemOptions.add(new ItemOption(94, 50));
        item.itemOptions.add(new ItemOption(19, 20));
        item.itemOptions.add(new ItemOption(117, 10));
        item.itemOptions.add(new ItemOption(77, 20));
        item.itemOptions.add(new ItemOption(103, 20));
        item.itemOptions.add(new ItemOption(230, 1));
        ServerNotify.gI().notify(player.name + " đã nhận được chúc phúc của thần khỉ.");
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isDaNangCap()) {
                    daNC = item;
                } else if (item.isDaMayMan()) {
                    daMM = item;
                } else if (item.isCongThucVip()) {
                    CtVip = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 500000000;

            int tilemacdinh = 35;
            int tileLucky = 20;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }

            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                if (daNC != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                }
                if (daMM != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).findFirst().get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            if (mTS != null && daMM != null && daNC != null && CtVip != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            } else if (CtVip != null && mTS != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
            } else if (CtVip != null && mTS != null && daNC != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
            } else if (CtVip != null && mTS != null && daMM != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            }

            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openCreateItemHK(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip1()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Thánh chỉ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS1() && item.quantity >= 99).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Phiếu đồ Hoàng kim");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS1 = null, daNC1 = null, daMM1 = null, CtVip1 = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS1()) {
                    mTS1 = item;
                } else if (item.isDaNangCap1()) {
                    daNC1 = item;
                } else if (item.isDaMayMan1()) {
                    daMM1 = item;
                } else if (item.isCongThucVip1()) {
                    CtVip1 = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 500000000;

            int tilemacdinh = 25;
            int tileLucky = 20;
            if (daNC1 != null) {
                tilemacdinh += (daNC1.template.id - 1186) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM1 != null) {
                tileLucky += tileLucky * (daMM1.template.id - 1191) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }

            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip1()).findFirst().get();
                if (daNC1 != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap1()).findFirst().get();
                }
                if (daMM1 != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan1()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS1() && item.quantity >= 99).findFirst().get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = {{2069, 2070, 2071, 2072, 2073}, {2074, 2075, 2076, 2077, 2078}, {2079, 2080, 2081, 2082, 2083}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoHoangKim(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh1()], itemCtVip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            if (mTS1 != null && daMM1 != null && daNC1 != null && CtVip1 != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS1, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM1, 1);
            } else if (CtVip1 != null && mTS1 != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS1, 99);
            } else if (CtVip1 != null && mTS1 != null && daNC1 != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS1, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC1, 1);
            } else if (CtVip1 != null && mTS1 != null && daMM1 != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS1, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM1, 1);
            }

            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item chanmenh = null;
            Item dahoangkim = null;
            int capbac = 0;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1318) {
                    dahoangkim = item;
                } else if (item.template.id >= 1351 && item.template.id < 1359) {
                    chanmenh = item;
                    capbac = item.template.id - 1350;
                }
            }
            int soluongda = player.combineNew.DaNangcap;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda) {
                if (chanmenh != null && (chanmenh.template.id >= 1351 && chanmenh.template.id < 1359)) {
                    if (Util.isTrue(player.combineNew.TileNangcap, 100)) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                        chanmenh.itemOptions.clear();
                        chanmenh.itemOptions.add(new Item.ItemOption(50, (3 + capbac * 5 )));
                        chanmenh.itemOptions.add(new Item.ItemOption(77, (3 + capbac * 5 )));
                        chanmenh.itemOptions.add(new Item.ItemOption(103, (3 + capbac * 5 )));
                        if (capbac == 8 && Util.isTrue(1,20)){
                        chanmenh.itemOptions.add(new Item.ItemOption(117, 10));
                        chanmenh.itemOptions.add(new Item.ItemOption(77, 20));
                        chanmenh.itemOptions.add(new Item.ItemOption(103, 20));
                        chanmenh.itemOptions.add(new Item.ItemOption(5, 15));
                        
                        chanmenh.itemOptions.add(new Item.ItemOption(230, 1));
                        ServerNotify.gI().notify(player.name + " đã nhận được chúc phúc của thánh thần.");
                        }
                        chanmenh.itemOptions.add(new Item.ItemOption(72, capbac + 1));
                        sendEffectSuccessCombine(player);
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.gI().sendThongBao(player, "Không đủ Đá Hoàng Kim để thực hiện");
            }
        }
    }

    public void openSKHVIP(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip1[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 1)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip1[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = Util.ratiTS(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 40;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(921);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai3(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 2134) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(2129);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 3));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai4(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2129) {
                    bongTai = item;
                } else if (item.template.id == 2135) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(2130);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 4));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai5(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2130) {
                    bongTai = item;
                } else if (item.template.id == 2136) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(2131);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 5));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongtai = item;
                } else if (item.template.id == 934) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 10)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 10)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(1, 10)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(1, 10)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 10)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(1, 10)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(1, 10)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2130) {
                    bongtai = item;
                } else if (item.template.id == 2030) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 4));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 20)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 20)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 20)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(15, 20)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(15, 20)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 20)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(15, 20)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(15, 20)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai5(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2131) {
                    bongtai = item;
                } else if (item.template.id == 2030) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 5));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(20, 25)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(20, 25)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(20, 25)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(20, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(20, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void TKham(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isDHK()) {
                    bongtai = item;
                } else if (item.template.id == 1199) {
                    honbongtai = item;
                } else if (item.template.id == 1198) {
                    daxanhlam = item;
                }
            } if (bongtai != null && !bongtai.isSKH()) {
                this.hd.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Trang bị chưa khảm", "Đóng");
                return;
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(50, 100)) {
                    ItemOption option_127 = new ItemOption();
                    ItemOption option_128 = new ItemOption();
                    ItemOption option_129 = new ItemOption();
                    ItemOption option_130 = new ItemOption();
                    ItemOption option_131 = new ItemOption();
                    ItemOption option_132 = new ItemOption();
                    ItemOption option_133 = new ItemOption();
                    ItemOption option_134 = new ItemOption();
                    ItemOption option_135 = new ItemOption();
                    ItemOption option_136 = new ItemOption();
                    ItemOption option_137 = new ItemOption();
                    ItemOption option_138 = new ItemOption();
                    ItemOption option_139 = new ItemOption();
                    ItemOption option_140 = new ItemOption();
                    ItemOption option_141 = new ItemOption();
                    ItemOption option_142 = new ItemOption();
                    ItemOption option_143 = new ItemOption();
                    ItemOption option_144 = new ItemOption();
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (ItemOption itopt : bongtai.itemOptions) {
                        if (itopt.optionTemplate.id == 47
                                || itopt.optionTemplate.id == 6
                                || itopt.optionTemplate.id == 0
                                || itopt.optionTemplate.id == 7
                                || itopt.optionTemplate.id == 14
                                || itopt.optionTemplate.id == 22
                                || itopt.optionTemplate.id == 23) {
                            option = itopt;
                        } else if (itopt.optionTemplate.id == 27
                                || itopt.optionTemplate.id == 28) {
                            option2 = itopt;
                        }
                        if (itopt.optionTemplate.id == 127) {
                            option_127 = itopt;
                        }
                        if (itopt.optionTemplate.id == 128) {
                            option_128 = itopt;
                        }
                        if (itopt.optionTemplate.id == 129) {
                            option_129 = itopt;
                        }
                        if (itopt.optionTemplate.id == 130) {
                            option_130 = itopt;
                        }
                        if (itopt.optionTemplate.id == 131) {
                            option_131 = itopt;
                        }
                        if (itopt.optionTemplate.id == 132) {
                            option_132 = itopt;
                        }
                        if (itopt.optionTemplate.id == 133) {
                            option_133 = itopt;
                        }
                        if (itopt.optionTemplate.id == 134) {
                            option_134 = itopt;
                        }
                        if (itopt.optionTemplate.id == 135) {
                            option_135 = itopt;
                        }
                        if (itopt.optionTemplate.id == 136) {
                            option_136 = itopt;
                        }
                        if (itopt.optionTemplate.id == 137) {
                            option_137 = itopt;
                        }
                        if (itopt.optionTemplate.id == 138) {
                            option_138 = itopt;
                        }
                        if (itopt.optionTemplate.id == 139) {
                            option_139 = itopt;
                        }
                        if (itopt.optionTemplate.id == 140) {
                            option_140 = itopt;
                        }
                        if (itopt.optionTemplate.id == 141) {
                            option_141 = itopt;
                        }
                        if (itopt.optionTemplate.id == 142) {
                            option_142 = itopt;
                        }
                        if (itopt.optionTemplate.id == 143) {
                            option_143 = itopt;
                        }
                        if (itopt.optionTemplate.id == 144) {
                            option_144 = itopt;
                        }
                    }
                    if (option_127 != null) {
                        bongtai.itemOptions.remove(option_127);
                        bongtai.itemOptions.remove(option_139);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_128 != null) {
                        bongtai.itemOptions.remove(option_128);
                        bongtai.itemOptions.remove(option_140);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_129 != null) {
                        bongtai.itemOptions.remove(option_129);
                        bongtai.itemOptions.remove(option_141);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_130 != null) {
                        bongtai.itemOptions.remove(option_130);
                        bongtai.itemOptions.remove(option_142);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_131 != null) {
                        bongtai.itemOptions.remove(option_131);
                        bongtai.itemOptions.remove(option_143);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_132 != null) {
                        bongtai.itemOptions.remove(option_132);
                        bongtai.itemOptions.remove(option_144);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_133 != null) {
                        bongtai.itemOptions.remove(option_133);
                        bongtai.itemOptions.remove(option_136);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_134 != null) {
                        bongtai.itemOptions.remove(option_134);
                        bongtai.itemOptions.remove(option_137);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    if (option_135 != null) {
                        bongtai.itemOptions.remove(option_135);
                        bongtai.itemOptions.remove(option_138);
                        option.param -= (option.param * 10 / 200);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 200);
                        }
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void Kham(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isDHK()) {
                    bongtai = item;
                } else if (item.template.id == 1198) {
                    honbongtai = item;
                } else if (item.template.id == 1197) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && bongtai.isSKH()) {
                this.chanmenh.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Trang bị đã khảm", "Đóng");
                return;
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(50, 100)) {
                    int skhId = ItemService.gI().randomSKHId(bongtai.template.gender);
                    bongtai.itemOptions.add(new Item.ItemOption(skhId, 1));
                    bongtai.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2129) {
                    bongtai = item;
                } else if (item.template.id == 2030) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 3));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 15)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 15)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 15)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(10, 15)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(10, 15)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 15)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(10, 15)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(10, 15)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

private void kichHoatHoangKim(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        Item trangBi = null;
        Item daSKH = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (isTrangBiHoangKimzin(item)) {
                trangBi = item;
            } else if (isDaSKH(item)) {
                daSKH = item;
            }
        }
         
        Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
        int soLuong = (tv != null) ? tv.quantity : 0;

        if (trangBi != null && daSKH != null && soLuong >= 1000) {            
            // Khai báo biến optionDaSKH
            int optionDaSKH = -1;  // Khởi tạo giá trị mặc định
            
            // Lấy option từ đá SKH
            if (daSKH.template.id == 2039) {
                int randTD = Util.nextInt(1, 100);
                if (randTD <= 33) {
                    optionDaSKH = 141;
                } else if (randTD <= 66) {
                    optionDaSKH = 140;
                } else {
                    optionDaSKH = 139;
                }
            } else if (daSKH.template.id == 2040) {
                int randNM = Util.nextInt(1, 100);
                if (randNM <= 33) {
                    optionDaSKH = 142;
                } else if (randNM <= 66) {
                    optionDaSKH = 143;
                } else {
                    optionDaSKH = 144;
                }
            } else if (daSKH.template.id == 2041) {
                int randXD = Util.nextInt(1, 100);
                if (randXD <= 33) {
                    optionDaSKH = 136;
                } else if (randXD <= 66) {
                    optionDaSKH = 137;
                } else {
                    optionDaSKH = 138;
                }
            }
            else if (daSKH.template.id == 2042) {
                int randTT = Util.nextInt(1, 100);
                if (randTT <= 77) {
                    optionDaSKH = 227;
                } else {
                    optionDaSKH = 228;
                }
            }
            // Kiểm tra nếu có optionDaSKH hợp lệ
            if (optionDaSKH != -1) {
                System.out.println("Option SKH: " + optionDaSKH);
                trangBi.itemOptions.add(new Item.ItemOption(optionDaSKH, 1));
                trangBi.itemOptions.add(new Item.ItemOption(230, 1));
            // Trừ nguyên liệu
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            InventoryServiceNew.gI().subQuantityItemsBag(player, daSKH, 1);
                // Hiệu ứng + thông báo
                sendEffectSuccessCombine(player);
                Service.gI().sendThongBao(player, "Kích hoạt Hoàng Kim thành công!");
            } else {
                Service.gI().sendThongBao(player, "Không có option đá SKH hợp lệ!");
            }
        } else {
            Service.gI().sendThongBao(player, "Thiếu điều kiện để kích hoạt!");
        }

        // Cập nhật giao diện
        InventoryServiceNew.gI().sendItemBags(player);
        reOpenItemCombine(player);
    }
}
    private void phaLeHoaTrangBi(Player player) {
        boolean flag = false;
        int solandap = player.combineNew.quantities;
        while (player.combineNew.quantities > 0 && !player.combineNew.itemsCombine.isEmpty() && !flag) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                break;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                break;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    //float ratio = getRatioPhaLeHoa(star);
                    int epint = (int) getRatioPhaLeHoa(star);
                    flag = Util.isTrue(epint, 100);
                    if (flag) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        Service.getInstance().sendThongBao(player, "Lên cấp sau " + (solandap - player.combineNew.quantities + 1) + " lần đập");
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
            }
            player.combineNew.quantities -= 1;
        }
        if (!flag) {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.type == 12) && (item.template.id != 14) && (item.template.id != 2091) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
///nghe nghiep 
    private void ThoRen(Player player) {
                    Item itemKiem = null;
                    Item itemAoGiap = null;
                    Item itemDaRen = null;
                    Item itemThoMM = null;        
                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                    int soLuong = (tv != null) ? tv.quantity : 0;
                    if (soLuong <1000)  {
                   Service.getInstance().sendThongBao(player, "Không đủ 1k thỏi vàng." );
                    return;
                    }
                    int CapBac = player.nPoint.nghenghiep/3 ;
                    int param = 1000 + Util.nextInt(0,20)* CapBac*100;
                    int param1 = (20 + Util.nextInt(0,20)* CapBac)*650;
              for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 && item.template.id > 2119) {
                itemKiem = item;
            } else if (item.template.id <= 2141 && item.template.id > 2136) {
                itemAoGiap = item;
            } else if (item.template.id == 2128) 
            {itemDaRen = item;}
            else if (item.template.id == 2115) 
            {itemThoMM = item;} 
        } 

        if ((itemDaRen.quantity < 10 || itemThoMM.quantity < 1111) && (itemKiem != null || itemAoGiap != null))
        { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if (itemDaRen.quantity >= 10 && itemThoMM.quantity >= 1111 && (itemKiem == null && itemAoGiap == null))
             { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if(itemDaRen.quantity >= 10 && itemThoMM.quantity >= 1111 && itemKiem != null){
            Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            for (Item.ItemOption io : itemKiem.itemOptions) {
                    if (io.optionTemplate.id == 0) {
                     option =  io ;
                    }
                    else if (io.optionTemplate.id == 41) {
                        option2 = io;
                    }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param ;}
                        else {itemKiem.itemOptions.add(new Item.ItemOption(0, param));}
                        if (option2 != null) { option2.param = CapBac;}
                            else { itemKiem.itemOptions.add(new Item.ItemOption(41, CapBac));  }
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemDaRen, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemThoMM, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Rèn đồ thành công bởi thợ rèn bậc " + CapBac ); 
            if (Util.isTrue(1,50) && CapBac < 30) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành thợ rèn bậc "+ CapBac + 1 +" . Rèn đồ ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
              if (param > 30000) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " rèn thành công "+ itemKiem.template.name +" lên " + param + " chỉ số.");
        }  
                    } else {
           InventoryServiceNew.gI().subQuantityItemsBag(player, itemDaRen, 10);
           InventoryServiceNew.gI().subQuantityItemsBag(player, itemThoMM, 1111);
           InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
           sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng thợ bậc " + CapBac + ", đã đập xịt!");
            } 
            reOpenItemCombine(player);
    } 
        else if(itemDaRen.quantity >= 10 && itemThoMM.quantity >= 1111 && itemAoGiap != null){
             Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            for (Item.ItemOption io : itemAoGiap.itemOptions) {
                    if (io.optionTemplate.id == 48) {
                     option =  io ;
                    }
                    else if (io.optionTemplate.id == 41) {
                        option2 = io;
                    }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param1 ;}
                        else {itemAoGiap.itemOptions.add(new Item.ItemOption(48, param1));}
                        if (option2 != null) { option2.param = CapBac;}
                            else { itemAoGiap.itemOptions.add(new Item.ItemOption(41, CapBac));  }
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemDaRen, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemThoMM, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Rèn đồ thành công bởi thợ rèn bậc " + CapBac );                
                        if (param1 > 200000) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " rèn thành công "+ itemAoGiap.template.name +" lên " + param1/1000 + "K chỉ số.");
        } 
                    if (Util.isTrue(1,50) && CapBac < 30) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành thợ rèn bậc "+ CapBac + 1 +" . Rèn đồ ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
                    } else {
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemDaRen, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemThoMM, 1111);   
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
           sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng thợ bậc " + CapBac + ", đã đập xịt!");
            }  
            reOpenItemCombine(player);
        }

    }
    
    private void ThoSan(Player player) {
                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                    int soLuong = (tv != null) ? tv.quantity : 0;
                    if (soLuong <1000)  {
                   Service.getInstance().sendThongBao(player, "Không đủ 1k thỏi vàng." );
                    return;
                    }
                    Item itemKiem = null;
                    Item itemAoGiap = null;
                    Item itemNgoc = null;
                    Item itemTuiTra = null;              
                    int CapBac = (player.nPoint.nghenghiep+2)/3 ;
                    int param = 10 + (Util.nextInt(0,20)* CapBac)/4;
                    int param1 = (20 + Util.nextInt(0,20)* CapBac)*650/1000;
                    
              for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 && item.template.id > 2119) {
                itemKiem = item;
            } else if (item.template.id <= 2141 && item.template.id > 2136) {
                itemAoGiap = item;
            } else if (item.template.id == 2125) 
            {itemNgoc = item;}
            else if (item.template.id == 2119) 
            {itemTuiTra = item;} 
        } 
        if ((itemNgoc.quantity < 10 || itemTuiTra.quantity < 1111) && (itemKiem != null || itemAoGiap != null))
        { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if (itemNgoc.quantity >= 10 && itemTuiTra.quantity >= 1111 && (itemKiem == null && itemAoGiap == null))
             { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if(itemNgoc.quantity >= 10 && itemTuiTra.quantity >= 1111 && itemKiem != null){
            Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            for (Item.ItemOption io : itemKiem.itemOptions) {
                    if (io.optionTemplate.id == 50) {
                     option =  io ;
                    }
                    else if (io.optionTemplate.id == 42) {
                        option2 = io;
                    }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param ;}
                        else {itemKiem.itemOptions.add(new Item.ItemOption(50, param));}
                        if (option2 != null) { option2.param = CapBac;}
                            else { itemKiem.itemOptions.add(new Item.ItemOption(42, CapBac));  }
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemNgoc, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTuiTra, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Nâng cấp đồ thành công bởi thợ săn bậc " + CapBac ); 
            if (param > 80) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " nâng cấp thành công "+ itemKiem.template.name +" lên " + param + " chỉ số.");
        }   
            if (Util.isTrue(1,50) && CapBac < 30) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành thợ săn bậc "+ CapBac + 1 +" . Rèn đồ ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
                    } else {
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemNgoc, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTuiTra, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng thợ săn bậc " + CapBac + ", đã đập xịt!");
            }         
            reOpenItemCombine(player);
    } 
        else if(itemNgoc.quantity >= 10 && itemTuiTra.quantity >= 99 && itemAoGiap != null){
             Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            Item.ItemOption option3 = null;
            for (Item.ItemOption io : itemAoGiap.itemOptions) {
                 switch (io.optionTemplate.id) {
                     case 22:
                         option =  io ;
                         break;
                     case 23:
                         option2 = io;
                         break;
                     case 42:
                         option3 = io;
                         break;
                     default:
                         break;
                 }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param1 ;}
                        else {itemAoGiap.itemOptions.add(new Item.ItemOption(22, param1));}
                        if (option2 != null) { option2.param = param1;}
                            else { itemAoGiap.itemOptions.add(new Item.ItemOption(23, param1));}
                        if (option3 != null) { option3.param = CapBac;}
                            else { itemAoGiap.itemOptions.add(new Item.ItemOption(42, CapBac));}
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemNgoc, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTuiTra, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Nâng cấp thành công bởi thợ săn bậc " + CapBac ); 
            if (param1 > 100) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " nâng cấp thành công "+ itemAoGiap.template.name +" lên " + param1 + " chỉ số.");
        }   
                    if (Util.isTrue(1,1000) && CapBac < 20) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành thợ săn bậc "+ CapBac + 1 +" . Nâng cấp đồ ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
                    } else {
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemNgoc, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTuiTra, 1111);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng thợ săn " + CapBac + ", đã đập xịt!");
            }   
            reOpenItemCombine(player);
        }
    }
    
    private void PhapSu(Player player) {
                     Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                    int soLuong = (tv != null) ? tv.quantity : 0;
                    if (soLuong <1000)  {
                   Service.getInstance().sendThongBao(player, "Không đủ 1k thỏi vàng." );
                    return;
                    }
                    Item itemKiem = null;
                    Item itemAoGiap = null;
                    Item linhDich = null;             
                    int CapBac = (player.nPoint.nghenghiep+1)/3 ;
                    int param = 5 + Util.nextInt(0,20)* CapBac/4;
                    int param1 = (30 + Util.nextInt(0,20)* CapBac);
              for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id <= 2124 && item.template.id > 2119) {
                itemKiem = item;
            } else if (item.template.id <= 2141 && item.template.id > 2136) {
                itemAoGiap = item;
            } else if (item.template.id == 2117) 
            {linhDich = item;}
        } 
        if (linhDich.quantity < 3333 && (itemKiem != null || itemAoGiap != null))
        { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if (linhDich.quantity >= 3333 && (itemKiem == null && itemAoGiap == null))
             { Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu." );}
        else if(linhDich.quantity >= 3333 && itemKiem != null){
            Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            for (Item.ItemOption io : itemKiem.itemOptions) {
                    if (io.optionTemplate.id == 5) {
                     option =  io ;
                    }
                    else if (io.optionTemplate.id == 43) {
                        option2 = io;
                    }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param ;}
                        else {itemKiem.itemOptions.add(new Item.ItemOption(5, param));}
                        if (option2 != null) { option2.param = CapBac;}
                            else { itemKiem.itemOptions.add(new Item.ItemOption(43, CapBac));  }
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, linhDich, 3333);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Phù phép đồ thành công bởi pháp sư bậc " + CapBac ); 
            if (param > 70) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " phù phép thành công "+ itemKiem.template.name +" lên " + param + " chỉ số.");
        }   
            if (Util.isTrue(1,50) && CapBac < 30) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành pháp sư bậc "+ CapBac + 1 +" . Pháp thuật ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
                    } else {
                 InventoryServiceNew.gI().subQuantityItemsBag(player, linhDich, 3333);
                 InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
                sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng pháp sư bậc " + CapBac + ", đã đập xịt!");
            }         
            reOpenItemCombine(player);
    } 
        else if(linhDich.quantity >= 3333 && itemAoGiap != null){
             Item.ItemOption option = null;
            Item.ItemOption option2 = null;
            for (Item.ItemOption io : itemAoGiap.itemOptions) {
                 switch (io.optionTemplate.id) {
                     case 97:
                         option =  io ;
                         break;
                     case 43:
                         option2 = io;
                         break;
                     default:
                         break;
                 }
                }
            if (Util.isTrue(20+ CapBac*3, 200)) {
                        if (option != null) {option.param = param1 ;}
                        else {itemAoGiap.itemOptions.add(new Item.ItemOption(97, param1));}
                        if (option2 != null) { option2.param = CapBac;}
                            else { itemAoGiap.itemOptions.add(new Item.ItemOption(43, CapBac));}
                        sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, linhDich, 3333);
            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
            sendEffectSuccessCombine(player);
            Service.getInstance().sendThongBao(player, "Phù phép thành công bởi pháp sư bậc " + CapBac ); 
            if (param1 > 250) {
            ServerNotify.gI().notify("Chúc mừng " + player.name + " phù phép thành công "+ itemAoGiap.template.name +" lên " + param1 + " chỉ số.");
        }   
                    if (Util.isTrue(1,50) && CapBac < 30) {
          ServerNotify.gI().notify("Chúc mừng " + player.name + " đã trở thành pháp sư bậc "+ CapBac + 1 +" . Phép thuật ngày càng xịn. " ); 
          player.nPoint.nghenghiep += 3;
        }
                    } else {
                InventoryServiceNew.gI().subQuantityItemsBag(player, linhDich, 3333);
                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1000);
                sendEffectFailCombine(player);
            Service.getInstance().sendThongBao(player, "Thằng pháp sư " + CapBac + ", đã đập xịt!");
            }   
            reOpenItemCombine(player);
        }
    }
    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private int getDaNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 40;
            case 2:
                return 50;
            case 3:
                return 60;
            case 4:
                return 70;
            case 5:
                return 80;
            case 6:
                return 90;
            case 7:
                return 100;
        }
        return 0;
    }

    private float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 80f;
            case 1:
                return 60f;
            case 2:
                return 40f;
            case 3:
                return 30f;
            case 4:
                return 25f;
            case 5:
                return 20f;
            case 6:
                return 10f;
            case 7:
                return 5f;
        }
        return 0;
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 1000000;
            case 1:
                return 2500000;
            case 2:
                return 4000000;
            case 3:
                return 50000000;
            case 4:
                return 60000000;
            case 5:
                return 90000000;
            case 6:
                return 150000000;
            case 7:
                return 190000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 5f;
            case 6:
                return 2f;
            case 7:
                return 1f;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 40;
            case 2:
                return 50;
            case 3:
                return 60;
            case 4:
                return 70;
            case 5:
                return 80;
            case 6:
                return 90;
            case 7:
                return 120;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 20;
            case 1:
                return 50;
            case 2:
                return 70;
            case 3:
                return 100;
            case 4:
                return 130;
            case 5:
                return 160;
            case 6:
                return 180;
            case 7:
                return 220;
        }
        return 220;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100;
            case 1:
                return 70;
            case 2:
                return 50;
            case 3:
                return 40;
            case 4:
                return 30;
            case 5:
                return 20;
            case 6:
                return 10;
            case 7: // 7 sao
                return 5;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100000;
            case 1:
                return 700000;
            case 2:
                return 900000;
            case 3:
                return 3000000;
            case 4:
                return 9000000;
            case 5:
                return 50000000;
            case 6:
                return 200000000;
            case 7:
                return 500000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 5 && item.template.id <= 20) || ((item.template.id >= 2091 && item.template.id <= 2093)));
    }
    private boolean isDaSKH(Item item) {
        return item != null && (item.template.type == 100 );
    }
    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || (item.template.id >= 529 && item.template.id <= 531) || item.template.id == 1716) {
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean isTrangBiHoangKimzin(Item item) {
    if (item != null && item.isNotNullItem()) {
        // Nếu item là trang bị Hoàng Kim
        if (item.template.id >= 2069 && item.template.id <= 2083) {
            // Duyệt các option của item
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 230) {
                    return false; // Có option 230 thì không phải đồ zin
                }
            }
            return true; // Không có option 230 => là đồ zin
        }
    }
    return false;
}

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 2091:
                return 7;
            case 2092:
                return 7;
            case 2093:
                return 5;
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 2; // +2%né đòn
            default:
                return -1;
        }
    }
   
  
    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 2091:
                return 77;
            case 2092:
                return 103;
            case 2093:
                return 50;
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case SET_KICH_HOAT_HOANG_KIM:
                return "Ta sẽ phù phép\ncho trang bị hoàng kim của ngươi\ntrở thành 1 trang bị kích hoạt";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";

            case DAP_SET_KICH_HOAT:
                return "Ta sẽ giúp ngươi chuyển hóa\n1 món đồ hủy diệt\nthành 1 món đồ kích hoạt";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n đồ kích hoạt VIP!";
            case NANG_CAP_CHAN_MENH:
                return "Ta sẽ Nâng cấp\nHồn Hoàn của ngươi\ncao hơn một bậc";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case NANG_CAP_BONG_TAI3:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 3";
            case NANG_CAP_BONG_TAI4:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 4";
            case NANG_CAP_BONG_TAI5:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 5";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case MO_CHI_SO_BONG_TAI3:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 3 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case MO_CHI_SO_BONG_TAI4:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 4 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case MO_CHI_SO_BONG_TAI5:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 5 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case Kham:
                return "Ta sẽ phù phép\ncho đồ Hoàng Kim của ngươi\ncó 1 chỉ số kích hoạt";
            case TKham:
                return "Ta sẽ phù phép\ncho đồ Hoàng Kim của ngươi\nmất 1 chỉ số kích hoạt";    
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            case CHE_TAO_TRANG_BI_HK:
                return "Chế tạo\ntrang bị Hoàng Kim";
            case THO_REN:
                return "Cấp bậc càng cao\n đồ càng xịn.";
            case THO_SAN:
                return "Cấp bậc càng cao\n đồ càng xịn.";
            case PHAP_SU:
                return "Cấp bậc càng cao\n đồ càng xịn.";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case SET_KICH_HOAT_HOANG_KIM:
                return "Chọn trang bị hoàng kim \n(Áo, quần, găng, giày hoặc rađa) chưa ép skh\nChọn loại ngọc thánh\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_CHAN_MENH:
                return "Vào hành trang\nChọn Hồn Hoàn muốn nâng cấp\nChọn Đá Hoàng Kim\n"
                        + "Sau đó chọn 'Nâng cấp'\n\n"
                        + "Lưu ý: Khi Nâng cấp Thành công sẽ tăng 3% chỉ số của cấp trước đó";
            case DAP_SET_KICH_HOAT:
                return "Vào hành trang\nChọn món đồ hủy diệt tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\n"
                        + "(Có thể thêm 1 món đồ thần linh bất kỳ để tăng tỉ lệ)\nSau đó chọn 'Đập'";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 1 trang bị thiên sứ bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + " đồ SKH VIP sẽ cùng loại \n với đồ thiên sứ!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI3:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI3:
                return "Vào hành trang\nChọn bông tai Porata\nChọn đá ma thuật số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI4:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI4:
                return "Vào hành trang\nChọn bông tai Porata\nChọn đá ma thuật số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI5:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI5:
                return "Vào hành trang\nChọn bông tai Porata\nChọn đá ma thuật số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case Kham:
                return "Vào hành trang\nChọn đồ hoàng kim\nChọn Lọ thuốc tăng cấp số lượng 99 cái\nvà Đá tăng cấp để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case TKham:
                return "Vào hành trang\nChọn đồ hoàng kim\nChọn Lọ thuốc tẩy số lượng 99 cái\nvà Lọ thuốc tăng cấp\nSau đó chọn 'Nâng cấp'\nlưu ý:tẩy thành công mới bị trừ chỉ số";    
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\nMảnh trang bị tương ứng\n"
                        + "Số Lượng 99\n"
                        + "Có thể thêm\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI_HK:
                return "Cần 1 Thánh chỉ\nPhiếu trang bị tương ứng\n"
                        + "Số Lượng 99\n"
                        + "Có thể thêm\nNgọc bội (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Quặng đá quý hiếm (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
                case THO_REN:
                return "Nâng cấp kiếm tăng Dame.\n Nâng áo giáp tăng HP,KI\n Nguyên liệu\n đồ + 10 đá rèn + 1111 thỏ may mắn. ";
                case THO_SAN:
                return "Nâng cấp kiếm tăng %Dame.\n Nâng áo giáp tăng HP,KI\n Nguyên liệu\n đồ + 10 ngọc thợ săn + 1111 túi trà. ";
                case PHAP_SU:
                return "Nâng cấp kiếm tăng %SDCM.\n Nâng áo giáp tăng PST\n Nguyên liệu\n đồ + 3333 linh dịch. ";
            default:
                return "";
        }
    }
}
