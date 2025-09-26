package com.girlkun.models.npc;

import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.services.ClanService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.NgocRongNamecService;
import com.girlkun.services.PetService;
import com.girlkun.services.FriendAndEnemyService;
import com.girlkun.services.TaskService;
import com.girlkun.services.OpenPowerService;
import com.girlkun.services.IntrinsicService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.ItemService;
import com.girlkun.network.io.Message;
import com.girlkun.models.mob.Mob;
import com.girlkun.services.*;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.map.bando.BanDoKhoBau;
import com.girlkun.models.map.bando.BanDoKhoBauService;
import com.girlkun.services.ChatGlobalService;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossType;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;

import java.util.HashMap;
import java.util.List;

import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.SummonDragon;
import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static com.girlkun.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static com.girlkun.services.func.SummonDragon.SHENRON_SAY;
import com.girlkun.models.player.Player;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.func.*;
import com.girlkun.services.func.Input;
import com.girlkun.services.func.LuckyRound;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import static com.girlkun.services.func.CombineServiceNew.*;
import com.arriety.kygui.ItemKyGui;
import com.arriety.kygui.ShopKyGuiService;
import com.arriety.kygui.ShopKyGuiManager;
import com.girlkun.event.Event;
import com.girlkun.event.EventDAO;
import com.girlkun.models.pariry.PariryServices;
import com.girlkun.models.pariry.pariryManager;
//import com.girlkun.models.player.Archivement;
import java.io.DataOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.io.IOException;

import java.util.logging.Level;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    public static int timebahatmit;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    // private static Npc npcminuong(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    //}
    private NpcFactory() {

    }

    public static Npc chanmenh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Hồn Hoàn"
                                + "\n\n|1|Đến Tinh Đấu đại sâm lâm săn Hồn Thú"
                                + "\n\n|5|Săn Hồn Thú Rôi Hấp Thu Hồn Hoàn"
                                + "\n|3|Có Thể Tăng Cấp Hồn Hoàn",
                                "Nâng cấp Hồn Hoàn", "Tinh Đấu đại sâm lâm");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 212, -1, 144);
                                    break; // qua lanh dia
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc cauca(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 176) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Cửa Hàng", "Về Lại Đảo Kame", "Từ chối");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đi Câu Cá", "Từ chối");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 176) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "CAUCA", true);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 5, -1, 795, 408);
                                    break;

                            }
                        } else if (this.mapId == 5) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 176, -1, 348, 240);
                                    break;
                            }
                        }
                    }
                }

            }
        };
    }

    public static Npc gapthu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 1234, "|0|- •⊹٭DragonBall Umi⊹• -\n" + "|2|MÁY GẮP TRỨNG\n" + "|1|GẮP X1 : 500 Hồng Ngoc\nGẮP X10 : 5k Hồng Ngoc\nGẮP X100 : 50k Hồng Ngoc\n" + "|7|LƯU Ý : Nếu hành trang đầy sẽ chuyển trứng về ruơng phụ\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 1234) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.ruby < 500) {
                                        Service.gI().sendThongBao(player, "không đủ 500 Hồng Ngoc");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                        return;
                                    }
                                    player.inventory.ruby -= 500;
                                    Service.gI().sendMoney(player);
                                    Item gapt = Util.petrandom(Util.nextInt(2020, 2022));
                                    if (Util.isTrue(40, 100)) {
                                        InventoryServiceNew.gI().addItemBag(player, gapt);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.createOtherMenu(player, 1234, "|2|Bạn vừa gắp được : " + gapt.template.name + "\nSố Hồng Ngọc Trừ : 500" + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    } else {
                                        this.createOtherMenu(player, 1234, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?" + "\nSố Hồng Ngọc Trừ : 500" + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    }
                                    break;
                                case 1:
                                    if (player.inventory.ruby < 5000) {
                                        Service.gI().sendThongBao(player, "không đủ 5000 Hồng Ngoc");
                                        return;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int hn = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            hn += 500;
                                            Thread.sleep(100);
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 100) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : " + (10 - timex10) + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Đóng");
                                                break;
                                            }
                                            player.inventory.ruby -= 500;
                                            Service.gI().sendMoney(player);
                                            Item gapx10 = Util.petrandom(Util.nextInt(2020, 2022));
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(40, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố hồng ngọc đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố hồng ngọc đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố hồng ngọc đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố hồng ngọc đã trừ : " + hn + "\n : ",
                                                            "Đóng");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (player.inventory.ruby < 50000) {
                                        Service.gI().sendThongBao(player, "không đủ 50000 Hồng Ngoc");
                                        return;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                        int timex100 = 100;
                                        int hn = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            hn += 500;
                                            Thread.sleep(100);
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 100) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : " + (10 - timex100) + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Đóng");
                                                break;
                                            }
                                            player.inventory.ruby -= 500;
                                            Service.gI().sendMoney(player);
                                            Item gapx100 = Util.petrandom(Util.nextInt(2020, 2022));
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố hồng ngọc đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố hồng ngọc đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố hồng ngọc đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố hồng ngọc đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                            + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp mấy thằng lồn k vào bang", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                            ItemTimeService.gI().removeTextDoanhTrai(pl);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "OK", "Từ Chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi DesTroy Gas cấp độ "
                                                + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
                                                + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau1(player, Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    private static Npc Yardrat(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 171) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Cải Trang Yardrat cần:\b|7|X9999 Bí Kiếp + 1 Tỷ vàng", "Quay Về", "Đổi \n Cải Trang Yardrat", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 171) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, -1);
                                    break;
                                case 1: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 590);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 9999) {
                                        this.npcChat(player, "Bạn không đủ 9999 Bí Kiếp");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 9999);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) (592 + player.gender));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(94, 40));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(33, 1));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Cải Trang Yardrat");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc meothantai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                        + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 1.000 Hồng ngọc\n Tối đa: 10.000.000 Hồng ngọc"
                        + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng", "Thể lệ", "Tham gia");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                    + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false) {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                                }
                            } else {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                }
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0 && pl.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                                case 1:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().TAI_taixiu(pl);
                                    }
                                    break;
                                case 2:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().XIU_taixiu(pl);
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                            + "\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size() + TaiXiu.gI().PlayersXiu.size()) + " người"
                                            + "\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai + pl.goldXiu) + " Hồng ngọc"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc meothantai1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "\b|8|Trò chơi Chẵn Lẻ đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Chẵn Lẻ! Đặt cược và dự đoán đúng"
                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                        + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 10 thỏi vàng\n Tối đa: 10.000 thỏi vàng"
                        + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng", "Thể lệ", "Tham gia");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Chẵn và Lẻ, bạn chỉ được chọn 1 nhà để tham gia"
                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Chẵn Lẻ"
                                    + "\n\nNếu Tổng số 3 con xí ngầu là 1,3,5,7,9,11,13,15,17 : Lẻ\nNếu Tổng số 3 con xí ngầu là 2,4,6,8,10,12,14,16,18"
                                    + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
                        } else if (select == 1) {
                            if (pariryManager.time > 0 && player.cuoc1 == 0 && player.cuoc == 0) {
                                createOtherMenu(player, 1, "\n|7|---NHÀ CÁI---"
                                        + "\n\n|6|Tổng nhà Chẵn: " + Util.format(pariryManager.gI().goldChan) + " thỏi vàng"
                                        + "\n|6|Tổng nhà Lẻ: " + Util.format(pariryManager.gI().goldLe) + " thỏi vàng"
                                        + "\n\n|4|Số Tiền Bạn Đã Đặt: " + (player.cuoc + player.cuoc1) + " thỏi vàng"
                                        + "\n Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.8)) + " thỏi vàng"
                                        + "\n\n|5|Thời gian còn lại: " + pariryManager.time + " giây", "Cập nhập", "Theo Chẵn", "Theo Lẻ", "Xem \nlịch sử\nbản thân", "Xem lịch sử", "Nhận\nphần thưởng", "Đóng");
                            } else if (pariryManager.time > 0 && (player.cuoc1 > 0 || player.cuoc > 0)) {
                                createOtherMenu(player, 1, "\n|7|---NHÀ CÁI---"
                                        + "\n\n|6|Tổng nhà Chẵn: " + Util.format(pariryManager.gI().goldChan) + " thỏi vàng"
                                        + "\n|6|Tổng nhà Lẻ: " + Util.format(pariryManager.gI().goldLe) + " thỏi vàng"
                                        + "\n\n|4|Số Tiền Bạn Đã Đặt: " + (player.cuoc + player.cuoc1) + " thỏi vàng"
                                        + "\n Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.8)) + " thỏi vàng"
                                        + "\n\n|5|Thời gian còn lại: " + pariryManager.time + " giây", "Cập nhập", "Đóng");
                            } else {
                                createOtherMenu(player, 1, "\n|7|---NHÀ CÁI---"
                                        + "\n\n|6|Tổng nhà Chẵn: " + Util.format(pariryManager.gI().goldChan) + " thỏi vàng"
                                        + "\n|6|Tổng nhà Lẻ: " + Util.format(pariryManager.gI().goldLe) + " thỏi vàng"
                                        + "\n\n|4|Số Tiền Bạn Đã Đặt: " + (player.cuoc + player.cuoc1) + " thỏi vàng"
                                        + "\n Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.8)) + " thỏi vàng"
                                        + "\n\n|5|Thời gian còn lại: " + pariryManager.time + " giây" + "\n\n|7|Hệ thống đang tính toán", "Cập nhập", "Đóng");
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (pariryManager.time > 0 && player.cuoc == 0 && player.cuoc1 == 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, 1, "\n|7|---NHÀ CÁI---"
                                            + "\n\n|6|Tổng nhà Chẵn: " + Util.format(pariryManager.gI().goldChan) + " thỏi vàng"
                                            + "\n|6|Tổng nhà Lẻ: " + Util.format(pariryManager.gI().goldLe) + " thỏi vàng"
                                            + "\n\n|4|Số Tiền Bạn Đã Đặt: " + (player.cuoc + player.cuoc1) + " thỏi vàng"
                                            + "\n Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.8)) + " thỏi vàng"
                                            + "\n\n|5|Thời gian còn lại: " + pariryManager.time + " giây", "Cập nhập", "Theo Chẵn", "Theo Lẻ", "Xem \nlịch sử\nbản thân", "Xem lịch sử", "Nhận\nphần thưởng", "Đóng");
                                    break;

                                case 1:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().CHAN(player);
                                    }
                                    break;
                                case 2:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().LE(player);
                                    }
                                    break;
                                case 3:
                                    Service.getInstance().sendThongBaoOK(player,
                                            PariryServices.gI().getHistoryPlayer(player));
                                    break;
                                case 4:
                                    Service.getInstance().sendThongBaoOK(player,
                                            PariryServices.gI().getHistory());
                                    break;
                                case 5:
                                    if (player.rubyWin <= 0) {
                                        Service.getInstance().sendThongBaoOK(player, "Có cái nịt mà nhận");
                                        break;
                                    }
                                    PariryServices.gI().rewardRuby(player);
                                    break;
                            }
                        } else if (pariryManager.time > 0 && (player.cuoc1 > 0 || player.cuoc > 0)) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(player, 1, "\n|7|---NHÀ CÁI---"
                                            + "\n\n|6|Tổng nhà Chẵn: " + Util.format(pariryManager.gI().goldChan) + " thỏi vàng"
                                            + "\n|6|Tổng nhà Lẻ: " + Util.format(pariryManager.gI().goldLe) + " thỏi vàng"
                                            + "\n\n|4|Số Tiền Bạn Đã Đặt: " + (player.cuoc + player.cuoc1) + " thỏi vàng"
                                            + "\n Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.8)) + " thỏi vàng"
                                            + "\n\n|5|Thời gian còn lại: " + pariryManager.time + " giây", "Cập nhập", "Đóng");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 80) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm đuôi khỉ và thỏi vàng\n Bật cờ giết quái khỉ vào những giờ chẵn sẽ rơi ra đuôi khỉ",
                                            "Khỉ\ncấp 2",
                                            "Khỉ\ncấp 3",
                                            "Khỉ\ncấp 4",
                                            "Khỉ\ncấp 5",
                                            "Khỉ\ncấp 6",
                                            "Khỉ\ncấp 7",
                                            "Khỉ\ncấp 8",
                                            "Từ chối");
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1111);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1111);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1111) && soLuong >= 20 && soLuongg >= 1 && soLuonggg >= 3) {
                                            CombineServiceNew.gI().khilv2(player, 1136);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 20);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 3);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 1 với 20 đuôi khỉ với 3 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 1: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1136);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1136 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1136 + i) && soLuong >= 30 && soLuongg >= 1 && soLuonggg >= 5) {
                                            CombineServiceNew.gI().khilv3(player, 1137 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 5);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 2 với 30 đuôi khỉ với 5 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 2: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1137 + i) && soLuong >= 40 && soLuongg >= 1 && soLuonggg >= 7) {
                                            CombineServiceNew.gI().khilv4(player, 1138 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 40);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 7);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 3 với 40 đuôi khỉ với 7 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 3: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1138 + i) && soLuong >= 50 && soLuongg >= 1 && soLuonggg >= 9) {
                                            CombineServiceNew.gI().khilv5(player, 1112 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 50);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 9);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 4 với 50 đuôi khỉ với 9 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 4: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1112);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1112 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1112 + i) && soLuong >= 60 && soLuongg >= 1 && soLuonggg >= 12) {
                                            CombineServiceNew.gI().khilv6(player, 1113 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 60);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 12);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 5 với 60 đuôi khỉ với 12 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 5: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1113);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1113 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1113 + i) && soLuong >= 70 && soLuongg >= 1 && soLuonggg >= 15) {
                                            CombineServiceNew.gI().khilv7(player, 1114 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 70);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 15);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 6 với 70 đuôi khỉ với 15 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 6: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 579);
                                    Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1114);
                                    Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    int soLuong = 0;
                                    int soLuongg = 0;
                                    int soLuonggg = 0;
                                    if (dns != null) {
                                        if (klv1 != null) {
                                            if (tv != null) {
                                                soLuong = dns.quantity;
                                                soLuongg = klv1.quantity;
                                                soLuonggg = tv.quantity;
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1114 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 1114 + i) && soLuong >= 99 && soLuongg >= 1 && soLuonggg >= 20) {
                                            CombineServiceNew.gI().khilv8(player, 1110 + i);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 99);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 20);
                                            this.npcChat(player, "Upgrade Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần cái trang khỉ cấp 7 với 99 đuôi khỉ với 20 thỏi vàng");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 500 tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossByType(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 500_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 500 tr vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()}, (int) player.nPoint.dame,
                                                new long[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn bố giúp gì nào?", "Kho báu dưới biển", "Giải tán bang hội", "Lãnh địa Bang Hội", "Chuyển Sinh");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.banDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                + player.clan.banDoKhoBau.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu \nCác con cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Không có bang đòi đi cái cc à ");
                                }
                                break;
                            case 1:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
//                                        
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Đồng ý!", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 100000000000L) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "Mày chưa đủ 100 tỏi sức mạnh để vào");
                                }
                                break; // qua lanh dia
                            case 3: //shop bùa
                                createOtherMenu(player, ConstNpc.MENU_CS,
                                        "Bạn có muốn chuyển sinh không? \n sau khi chuyển sinh sẽ chỉ còn 2k sức mạnh nhưng sẽ được tăng chỉ số và được nhận danh hiệu thiên tử (tăng 25% tnsm) ",
                                        "Đồng ý", "Từ chối");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CS) {
                        switch (select) {
                            case 0:
                                //Service.gI().sendThongBaoOK(player,"Bú");
                                if (player.getSession().player.nPoint.power < 300000000000L) {
                                    Service.gI().sendThongBaoOK(player, "Cần 300 Tỉ Sức Mạnh!");
                                } else if (player.getSession().player.nPoint.hpg < 600000) {
                                    Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (600000 - player.nPoint.hpg) + " HP Nữa");
                                } else if (player.getSession().player.nPoint.mpg < 600000) {
                                    Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (600000 - player.nPoint.mpg) + " KI Nữa");
                                } else if (player.getSession().player.nPoint.dameg < 32000) {
                                    Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (32000 - player.nPoint.dameg) + " SD Nữa");
                                } else if (player.getSession().player.inventory.ruby < 20000) {
                                    Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (20000 - player.inventory.ruby) + " Hồng Ngọc");
                                } else if (player.getSession().player.nPoint.dameg == 32000) {
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 33000;
                                        player.nPoint.hpg = 650000;
                                        player.nPoint.mpg = 650000;
                                        player.CapCS = 1;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 33000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 34000;
                                        player.nPoint.hpg = 700000;
                                        player.nPoint.mpg = 700000;
                                        player.CapCS = 2;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 34000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 35000;
                                        player.nPoint.hpg = 750000;
                                        player.nPoint.mpg = 750000;
                                        player.CapCS = 3;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 35000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 36000;
                                        player.nPoint.hpg = 800000;
                                        player.nPoint.mpg = 800000;
                                        player.CapCS = 4;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 36000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 37000;
                                        player.nPoint.hpg = 850000;
                                        player.nPoint.mpg = 850000;
                                        player.CapCS = 5;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 37000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 38000;
                                        player.nPoint.hpg = 900000;
                                        player.nPoint.mpg = 900000;
                                        player.CapCS = 6;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 38000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 39000;
                                        player.nPoint.hpg = 950000;
                                        player.nPoint.mpg = 950000;
                                        player.CapCS = 7;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.getSession().player.nPoint.dameg == 39000) {
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(100, 100)) {
                                        player.nPoint.power = 2000;
                                        player.inventory.ruby -= 20000;
                                        player.nPoint.dameg = 40000;
                                        player.nPoint.hpg = 1000000;
                                        player.nPoint.mpg = 1000000;
                                        player.CapCS = 8;
                                        player.itemTime.isUsex2 = false;
                                        player.itemTime.isUsex5 = false;
                                        player.itemTime.isUsex7 = false;
                                        player.itemTime.isUsex10 = false;
                                        Service.gI().sendThongBaoOK(player, "Chuyển Sinh Thành Công! \n Vui Lòng out ra vào lại");
                                    }
                                } else if (player.nPoint.dameg == 40000) {
                                    Service.gI().sendThongBaoOK(player, "Đã Chuyển Sinh Mốc Tối Đã!!!");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

   public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                 "|3|Ngọc Rồng KABUTO Xin Chào Cậu\n|7|Mở thành viên chỉ với 10K\n Mở thành viên trên wed thưởng :\n40 Thỏi Vàng Và 10.000 hồng ngọc \n|2|Hiện tại cậu đang có:"+player.getSession().coinBar + "Đ"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Đổi Gold", "Đổi Hồng Ngọc", "Nhận ngọc xanh", "GiftCode");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                                   
                            case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                              case 1:
                                    Input.gI().createFormQDN(player);
                                    break;      
                            case 2:
                                 if (player.inventory.gem == 2000000) {
                                    this.npcChat(player, "Bú ít thôi con");
                                    break;
                                }
                                player.inventory.gem = 2000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 2M ngọc xanh");
                                break;      
                            
//                            case 4:
//                                if (!player.getSession().actived) {
//                                    if (player.getSession().coinBar >= 10000) {
//                                        player.getSession().actived = true;
//                                        if (PlayerDAO.subcoinBar(player, 10000)) ;
//                                            Item tv = ItemService.gI().createNewItem((short)457,40);
//                                            player.inventory.ruby += 10000;
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            InventoryServiceNew.gI().addItemBag(player, tv);
//                                            Service.getInstance().sendMoney(player);
//                                            Service.gI().sendThongBao(player, "|7|Kích hoạt thành công, bạn nhận được thêm 40 Thỏi Vàng và 10k Hồng Ngọc");
//                                    } else {
//                                        this.npcChat(player, "Có cái dái tiền mà mở thành viên...!");
//                                    }
//                                } else {
//                                    this.npcChat(player, "Mày đã mở mõm thành viên rồi!");

                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;

                        }
                    } 
                }

            }

        };
    }                                

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi mày, tao chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Điện của tao sẽ đưa chúng mày đi chơi gái chỉ trong 3 giây. MÀY muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu BÍM");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {
                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố bababa", "Ok em zai", "Có cc");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda của bố sử dụng công nghệ mới nhất, "
                                        + "có thể đưa các con đi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_29_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossByType(BossType.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết mẹ rồi...");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_29_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossByType(BossType.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết mẹ rồi...");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_29_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossByType(BossType.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết mẹ rồi...");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_29_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                        break;
                                    }
                                case 2:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_18_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                        break;
                                    }
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc genshin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, cửa hàng genshin?",
                            "Cửa Hàng", "Đến Teyvat");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "GENSHIN", false);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 202, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc lehoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if ( player.nPoint.nghenghiep == 0)
                    {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, người muốn làm gì?",
                            "Cửa Hàng 1", "Cửa Hàng 2");
                    } else {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, người muốn làm gì?",
                            "Cửa Hàng 1", "Cửa Hàng 2","Mở slot đồ đặc biệt","Mở slot đồ cho đệ tử");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: //shop
                                ShopServiceNew.gI().opendShop(player, "LeHoi", false);
                                break;
                            case 1: //shop
                                ShopServiceNew.gI().opendShop(player, "LeHoi1", false);
                                break;
                            case 2:
                                if (player.inventory.itemsBody.size() >= 13) {
        Service.getInstance().sendThongBao(player, "Bạn đã mở tối đa số ô đồ cơ thể.");
        return;
    }  
        player.inventory.itemsBody.add(ItemService.gI().createItemNull());
        Service.getInstance().sendThongBao(player, "Đã mở thêm 1 ô đồ cơ thể (" + player.inventory.itemsBody.size() + "/13)");
        break;
                            case 3:
                                if (player.pet.inventory.itemsBody.size() >= 13) {
        Service.getInstance().sendThongBao(player, "Bạn đã mở tối đa số ô đồ cơ thể.");
        return;
    }  
        player.pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        Service.getInstance().sendThongBao(player, "Đã mở thêm 1 ô đồ cơ thể (" + player.pet.inventory.itemsBody.size() + "/13)");
        break;
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, người muốn làm gì?",
                            "Cửa Hàng", "Hỗ Trợ", "Shop Tiền Tệ");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY", false);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "TIEN_TE", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc obito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ngươi muốn làm gì?",
                            "Cửa Hàng", "Cửa Hàng \n Giảm Giá", "Rút Tiền");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "OBITO", false);
                                    break;
                                case 1:
                                    if (player.checkPGG() == true) {
                                        ShopServiceNew.gI().opendShop(player, "PGG", true);
                                    } else {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi không có phiếu giảm giá", "Đóng");
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormQDTV(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc fa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "lewlew đồ fa không có ny",
                                "Cửa Hàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 109) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "FA", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ngokhong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "lewlew đồ fa không có ny",
                            "Cửa Hàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 122) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "NK", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vip(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "lewlew đồ fa không có ny",
                            "Cửa Hàng", "Vip");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                String checkvnd;
                int thoivang;
                int capvip;
                int cachvip;
                int thieudiem;
                if (player.getSession().tongnap >= 0 && player.getSession().tongnap < 50000) {
                    checkvnd = "Cày Cuốc";
                    thoivang = 2;
                    capvip = 0;
                } else if (player.getSession().tongnap >= 50000 && player.getSession().tongnap <= 150000) {
                    checkvnd = "Đại Gia";
                    thoivang = 5;
                    capvip = 1;
                } else {
                    checkvnd = "Thần Thoại";
                    thoivang = 10;
                    capvip = 2;
                }
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: //shop
                                ShopServiceNew.gI().opendShop(player, "VIP", false);
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.BASE_MENU1, "Cố Gắng Có Làm Mới Có Ăn Con, đừng lo lắng cho ta"
                                        + "\n|7| Cấp Bậc được tính như sau:"
                                        + "\n|1| Tổng Nạp < 50.000đ = Cày Cuốc "
                                        + "\n|4|(Nhận 2 Hộp Quà/Ngày)"
                                        + "\n|1| Tổng Nạp >= 50.000đ và <= 150.000đ = Đại Gia "
                                        + "\n|4|(Nhận 5 Hộp Quà/Ngày)"
                                        + "\n|1|Tổng Nạp > 150.000đ = Thần Thoại "
                                        + "\n|4|(Nhận 10 Hộp Quà/Ngày)"
                                        + "\n|7|Cấp Bậc hiện tại của bạn là : " + checkvnd
                                        + "\n|3|Tổng Nạp : " + Util.format(player.getSession().tongnap) + "đ",
                                        "Nhận danh hiệu \nCày Cuốc (5d)", "Nhận danh hiệu \nĐại Gia (vv)", "Nhận danh hiệu \nThần Thoại (vv)", "Điểm danh \nhằng ngày", "Đóng");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU1) {
                        switch (select) {
                            case 0:
                                if (capvip < 0) {
                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện!");
                                    return;
                                }
                                if (player.nhanqua >= 1) {
                                    Service.getInstance().sendThongBao(player, "bạn đã nhận quà này rồi!");
                                    return;
                                }
                                Item tvv = ItemService.gI().createNewItem((short) 1380, 1);
                                tvv.itemOptions.add(new Item.ItemOption(50, 5));
                                tvv.itemOptions.add(new Item.ItemOption(77, 5));
                                tvv.itemOptions.add(new Item.ItemOption(103, 5));
                                tvv.itemOptions.add(new Item.ItemOption(101, 5));
                                tvv.itemOptions.add(new Item.ItemOption(30, 0));
                                InventoryServiceNew.gI().addItemBag(player, tvv);
                                InventoryServiceNew.gI().sendItemBags(player);
                                player.nhanqua = 1;
                                Service.gI().sendThongBao(player, "Bạn Nhận Được " + tvv.template.name);
                                break;
                            case 1:
                                if (capvip < 1) {
                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện!");
                                    return;
                                }
                                if (player.nhanqua >= 2) {
                                    Service.getInstance().sendThongBao(player, "bạn đã nhận quà này rồi!");
                                    return;
                                }
                                Item dh = ItemService.gI().createNewItem((short) 1381, 1);
                                dh.itemOptions.add(new Item.ItemOption(50, 10));
                                dh.itemOptions.add(new Item.ItemOption(77, 10));
                                dh.itemOptions.add(new Item.ItemOption(103, 10));
                                dh.itemOptions.add(new Item.ItemOption(101, 10));
                                dh.itemOptions.add(new Item.ItemOption(30, 0));
                                InventoryServiceNew.gI().addItemBag(player, dh);
                                InventoryServiceNew.gI().sendItemBags(player);
                                player.nhanqua = 2;
                                Service.gI().sendThongBao(player, "Bạn Nhận Được " + dh.template.name);
                                break;
                            case 2:
                                if (capvip < 2) {
                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện!");
                                    return;
                                }
                                if (player.nhanqua >= 3) {
                                    Service.getInstance().sendThongBao(player, "bạn đã nhận quà này rồi!");
                                    return;
                                }
                                Item dh1 = ItemService.gI().createNewItem((short) 1382, 1);
                                dh1.itemOptions.add(new Item.ItemOption(50, 15));
                                dh1.itemOptions.add(new Item.ItemOption(77, 15));
                                dh1.itemOptions.add(new Item.ItemOption(103, 15));
                                dh1.itemOptions.add(new Item.ItemOption(101, 15));
                                dh1.itemOptions.add(new Item.ItemOption(30, 0));
                                InventoryServiceNew.gI().addItemBag(player, dh1);
                                InventoryServiceNew.gI().sendItemBags(player);
                                player.nhanqua = 3;
                                Service.gI().sendThongBao(player, "Bạn Nhận Được " + dh1.template.name);
                                break;
                            case 3:
                                if (player.diemdanh == 0) {
                                    int thoivang1;
                                    if (player.getSession().tongnap >= 0 && player.getSession().tongnap < 50000) {
                                        thoivang1 = 2;
                                    } else if (player.getSession().tongnap >= 50000 && player.getSession().tongnap <= 150000) {
                                        thoivang1 = 5;
                                    } else {
                                        thoivang1 = 10;
                                    }
                                    Item thoivang2 = ItemService.gI().createNewItem((short) 1499, thoivang1);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang2);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemdanh = System.currentTimeMillis();
                                    Service.getInstance().sendThongBao(player, "|7|Bạn vừa nhận được " + thoivang1 + " " + thoivang2.template.name);
                                } else {
                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Mày tìm bố có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa trang bị", "Nâng Cấp SKH",
                                "Nâng Cấp SKH Vip","SKH trang bị Hoàng Kim ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DAP_SET_KICH_HOAT);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.SET_KICH_HOAT_HOANG_KIM);
                                    break;    
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.DAP_SET_KICH_HOAT:
                                case CombineServiceNew.SET_KICH_HOAT_HOANG_KIM:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;
                                    }
                                    CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc baHatMit2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Mày tìm bố có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng");
                                    break;
                                case 1://nâng cấp vật phẩm
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2://nhập ngọc rồng
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NHAP_NGOC_RONG:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc tosukaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Mày tìm bố có việc gì?", "Bông tai\nPorata Cấp 2", "Bông tai\nPorata Cấp 3", "Bông tai\nPorata Cấp 4", "Bông tai\nPorata Cấp 5");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_BONG_TAI_2,
                                            "Ngươi muốn làm gì?",
                                            "Nâng Cấp", "Mở Chỉ Số");
                                    break;
                                case 1: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_BONG_TAI_3,
                                            "Ngươi muốn làm gì?",
                                            "Nâng Cấp", "Mở Chỉ Số");
                                    break;
                                case 2: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_BONG_TAI_4,
                                            "Ngươi muốn làm gì?",
                                            "Nâng Cấp", "Mở Chỉ Số");
                                    break;
                                case 3: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_BONG_TAI_5,
                                            "Ngươi muốn làm gì?",
                                            "Nâng Cấp", "Mở Chỉ Số");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BONG_TAI_2) {
                            switch (select) {
                                case 0: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 1: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BONG_TAI_3) {
                            switch (select) {
                                case 0: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI3);
                                    break;
                                case 1: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI3);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BONG_TAI_4) {
                            switch (select) {
                                case 0: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI4);
                                    break;
                                case 1: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI4);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BONG_TAI_5) {
                            switch (select) {
                                case 0: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI5);
                                    break;
                                case 1: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI5);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NANG_CAP_BONG_TAI3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI3:
                                case CombineServiceNew.NANG_CAP_BONG_TAI4:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI4:
                                case CombineServiceNew.NANG_CAP_BONG_TAI5:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI5:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "Ngũ Hành chảo x2 Tnsm\nHỗ trợ cho Ae Từ\b|1|10Ty trở lên", "OK", "Từ chối");
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Mày Có Muốn Về Với Mẹ Không?", "OK", "Từ chối");

                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power <= 10000000000L) {
                                    Service.gI().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 123, -1, 174);
                            }
                            if (mapId == 123) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                            }
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_22_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Bạn Chưa Hoàn Thành Nhiệm Vụ");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_22_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Bạn Chưa Hoàn Thành Nhiệm Vụ");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 80000000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nHồng Ngọc",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(
                                                    player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_RUBY);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con đường \n rắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMap(player, 143, -1, 1065, 336);
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 60000000000L) {
                                        //                Service.gI().sendMoney(player);
                                        ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    } else {
                                        this.npcChat(player, "Bạn chưa đủ 60 tỷ để vào");
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc dai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Zeno đang nổi điên ngươi có dám ngăn ngài ấy lại không ?",
                                "Đồng ý", "Từ chối");
                    } else if (this.mapId == 177) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 177, -1, 456);
                                    break;
                            }
                        }
                    } else if (this.mapId == 177) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 456);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc tele(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 212) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 216) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 202) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đến khe nứt không gian không?",
                                "Đồng ý", "Từ chối");
                    } else if (this.mapId == 211) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến \n Ngoại Môn", "Quay về \n đảo", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 212) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                                    break;
                            }
                        }
                    } else if (this.mapId == 216) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                            }
                        }
                    } else if (this.mapId == 202) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                            }
                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                            }
                        }
                    } else if (this.mapId == 211) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 171, -1, 456);
                            }
                            if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 456);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc nami(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đến đảo SkyPiea không?",
                                "Cửa Hàng", "Đồng ý", "Từ chối");
                    } else if (this.mapId == 217) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "NAMI", false);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, 456);
                                    break;
                            }
                        }
                    } else if (this.mapId == 217) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc dn(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn xuống địa ngục không ?",
                                "Cửa Hàng", "Đồng ý", "Từ chối");
                    } else if (this.mapId == 204) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "DN", false);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 204, -1, 456);
                                    break;
                            }
                        }
                    } else if (this.mapId == 204) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 456);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc tan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Săn Quỷ", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 211) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 216, -1, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc fu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:

                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc champa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Vùng Đất Hủy Diệt", "Từ chối");
                    } else if (this.mapId == 146) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_29_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 295);
                                        break;
                                    }
                            }
                        }
                    } else if (this.mapId == 146) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null && player.clanMember.getNumDateFromJoinTimeToToday() >= 2 && !player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getMinLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + " Phút" + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 2 || (player.clan.doanhTrai != null && player.clanMember.getNumDateFromJoinTimeToToday() < 2)) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 2 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }

                    if (!player.clan.doanhTrai_haveGone) {
                        player.clan.doanhTrai_haveGone = (new java.sql.Date(player.clan.doanhTrai_lastTimeOpen)).getDay() == (new java.sql.Date(System.currentTimeMillis())).getDay();
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                if (player.clan.doanhTrai != null && TimeUtil.getMinLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000) == 0) {
                                    Service.getInstance().sendThongBao(player, "Hết 30p gòi, đợi mai đê !!!!");
                                } else if (player.clan.doanhTrai == null) {
                                    try {
                                        DoanhTraiService.gI().joinDoanhTrai(player);
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else if (player.clan.doanhTrai != null && TimeUtil.getMinLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000) > 0) {
                                    ChangeMapService.gI().changeMapInYard(player, 53, -1, 60);
                                    ItemTimeService.gI().sendTextDoanhTrai(player);

                                }
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Ngươi muốn mở giới hạn sức mạnh cho ai?",
                        "Cho\n sư phụ", "Cho\n đệ tử", "từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Nâng giới hạn sức mạnh sư phụ đến "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "giới hạn sức mạnh sư phụ đã đạt mức tối đa",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Nâng giới hạn sức mạnh đệ tử đến "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "giới hạn sức mạnh đệ tử đã đạt mức tối đa",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Hết vàng rồi thằng óc chó "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Hết vàng rồi thằng óc chó "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    }
                }
            }
        };
    }
public static boolean isThoSan(int nghe) {
    return (nghe - 1) % 3 == 0;
}
public static boolean isPhuPhep(int nghe) {
    return (nghe - 2) % 3 == 0;
}
public static boolean isThoRen(int nghe) {
    return nghe % 3 == 0 && nghe != 0;
}
     public static Npc thoNghe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                        if (isThoSan(player.nPoint.nghenghiep)) {
                            int capBacThoSan = (player.nPoint.nghenghiep +2) / 3;
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào Thợ Săn cấp "+ capBacThoSan + ", bạn muốn làm gì?\n Đổi ngọc cần x3333mdv + x999 con ruồi",
                                "Nâng cấp\n trang bị", "Đổi ngọc \n thợ săn","Chọn lại nghề", "Thoát");
                        } else if (isThoRen(player.nPoint.nghenghiep)) {
                             int capBacThoRen = player.nPoint.nghenghiep  / 3;
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào Thợ Rèn cấp " + capBacThoRen +", bạn cần gì?",
                                "Rèn trang bị đặc biệt", "Rèn quặng.\n Cần 3333 mdv +\nx999 sao 4 cánh","Chọn lại nghề", "Thoát");
                        } else if (isPhuPhep(player.nPoint.nghenghiep)) {
                            int capBacPhuPhep = (player.nPoint.nghenghiep +1) / 3;
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào Pháp Sư cấp " +capBacPhuPhep+", pháp sư muốn làm gì?:\n Đổi trang bị cần x9999 mdv và x999 \n sao 4 cánh, túi trà, linh dịch, ruồi và thỏ",
                                "Đổi trang bị đặc biệt.", "Phù phép pháp sư","Chọn lại nghề", "Thoát");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Thanh niên muốn học nghề gì?\n Cần 8000 thỏi vàng để học nghề.",
                                "Thợ rèn", "Pháp sư", "Thợ săn","Thôi chuồn");
                        }
            }
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (this.mapId == 10) {
                    if (player.iDMark.isBaseMenu()) {
                        int nghe = player.nPoint.nghenghiep;
                        if (isThoSan(nghe)) {
                            switch (select) {
                                case 0:
                                     CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THO_SAN);
                                    break;
                                case 1:
                                    Item mdv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 225);
                                    Item sao = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116);
                                    int sl = (player.nPoint.nghenghiep+2)/3 + Util.nextInt(0, 2);
                                    if (mdv.quantity >= 1111 && sao.quantity >= 10 && mdv != null && sao != null) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mdv, 3333); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, sao, 999); 
                                    Item PhoiRen = ItemService.gI().createNewItem((short) 2125, sl);
                                    PhoiRen.itemOptions.add(new Item.ItemOption(73, 1));
                                    InventoryServiceNew.gI().addItemBag(player, PhoiRen);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi được " + sl  + " ngọc thợ săn.");
                                    }
                                    else {
                                    Service.getInstance().sendThongBao(player, "Cần x3333 mảnh đá vụn và x999 con ruồi.");
                                    }
                                    break;
                                case 2:
                                int capBacThoSan = (player.nPoint.nghenghiep +2) / 3;
                                this.createOtherMenu(player, 1001,
                                "Chào Thợ Săn cấp " +capBacThoSan+".\n Học lại nghề sẽ reset cấp bậc về 1.\n Vẫn muốn chọn chứ:",
                                "Học lại nghề","Thôi");
                                     break;   
                            }
                        } else if (isThoRen(nghe)) {
                            switch (select) {
                                case 0:
                                     CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.THO_REN);
                                    break;
                                case 1:
                                    Item mdv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 225);
                                    Item sao = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2118);
                                    int sl = player.nPoint.nghenghiep/3 + Util.nextInt(0, 2);
                                    if (mdv.quantity >= 1111 && sao.quantity >= 10 && mdv != null && sao != null) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mdv, 3333); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, sao, 999); 
                                    Item PhoiRen = ItemService.gI().createNewItem((short) 2128, sl);
                                    PhoiRen.itemOptions.add(new Item.ItemOption(73, 1));
                                    InventoryServiceNew.gI().addItemBag(player, PhoiRen);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Rèn được " + sl  + " quặng");
                                    }
                                    else {
                                    Service.getInstance().sendThongBao(player, "Cần x3333 mảnh đá vụn và x999 sao 4 cánh.");
                                    }
                                    break;
                                case 2:
                                    int capBacThoRen = player.nPoint.nghenghiep / 3;
                                this.createOtherMenu(player, 1001,
                                "Chào Thợ Rèn cấp " +capBacThoRen+".\n Học lại nghề sẽ reset cấp bậc về 1.\n Vẫn muốn chọn chứ:",
                                "Học lại nghề","Thôi");
                                     break;   
                            }
                        } else if (isPhuPhep(nghe)) {
                            switch (select) {
                                case 0:
                                    Item mdv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 225);
                                    Item sao = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2118);
                                    Item tra = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2119);
                                    Item nuoc = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2117);
                                    Item ruoi = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116);
                                    Item tho = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115);
                                    if (mdv.quantity >= 9999 && sao.quantity >= 999 && tra.quantity >= 999 && nuoc.quantity >= 999 && ruoi.quantity >= 999 && tho.quantity >= 999 ) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mdv, 9999); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, sao, 999); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tra, 999); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, nuoc, 999); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ruoi, 999); 
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tho, 999);
                                    if (Util.isTrue(1,2)){
                                    if (Util.isTrue(90,100)){
                                    Item Kiem = ItemService.gI().createNewItem((short) 2120, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(1,5)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(0,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(50,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(5,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);
                                    }
                                    else if (Util.isTrue(90,100)){
                                    Item Kiem = ItemService.gI().createNewItem((short) 2121, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(0,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(50,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(5,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);   
                                    }
                                    else if (Util.isTrue(90,100)) {
                                    Item Kiem = ItemService.gI().createNewItem((short) 2122, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(95,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(96,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(0,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(50,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(5,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);       
                                    } else {
                                    Item Kiem = ItemService.gI().createNewItem((short) 2123, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(20,30)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(20,30)));
                                    Kiem.itemOptions.add(new Item.ItemOption(95,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(96,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(0,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(50,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(5,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);  
                                    }} else {
                                    if (Util.isTrue(90,100)){
                                    Item Kiem = ItemService.gI().createNewItem((short) 2137, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(1,5)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);
                                    }
                                    else if (Util.isTrue(90,100)){
                                    Item Kiem = ItemService.gI().createNewItem((short) 2138, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(5,10)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);   
                                    }
                                    else if (Util.isTrue(90,100)) {
                                    Item Kiem = ItemService.gI().createNewItem((short) 2139, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(10,15)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(80,Util.nextInt(10,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(81,Util.nextInt(10,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);       
                                    } else {
                                    Item Kiem = ItemService.gI().createNewItem((short) 2140, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(15,20)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(20,30)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(20,30)));
                                    Kiem.itemOptions.add(new Item.ItemOption(80,Util.nextInt(20,40)));
                                    Kiem.itemOptions.add(new Item.ItemOption(81,Util.nextInt(20,40)));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(player, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    Service.getInstance().sendThongBao(player,"Đổi thành công " +Kiem.template.name);     
                                    }
                                    }
                                    }
                                    else {
                                    Service.getInstance().sendThongBao(player, "Thiếu đồ.");
                                    }
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAP_SU);
                                    break;
                                case 2: 
                                     int capBacPhuPhep = (player.nPoint.nghenghiep +1) / 3;
                                     this.createOtherMenu(player, 1001,
                                "Chào Pháp Sư cấp " +capBacPhuPhep+".\n Học lại nghề sẽ reset cấp bậc về 1.\n Vẫn muốn chọn chứ:",
                                "Học lại nghề","Thôi");
                                     break;
                            }
                        } else  {
                            Item tv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                            switch (select) {
                                case 0:
                                   if (tv.quantity >= 8000){
                                   player.nPoint.nghenghiep = 3;
                                   InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 8000);
                                   Service.getInstance().sendThongBao(player, "Học nghề thợ rèn thành công");    
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Hết thỏi vàng rồi cu.\n " + "Thiếu"
                                            + (8000- tv.quantity ) + "thỏi vàng");
                                }
                                    break;
                                case 1:  
                                   if (tv.quantity >= 8000){
                                   player.nPoint.nghenghiep = 2;
                                   InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 8000);
                                   Service.getInstance().sendThongBao(player, "Học nghề thợ rèn thành công");    
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Hết thỏi vàng rồi cu.\n " + "Thiếu"
                                            + (5000- tv.quantity ) + "thỏi vàng");
                                }
                                    break;
                                case 2:
                                   if (tv.quantity >= 8000){
                                   player.nPoint.nghenghiep = 1;
                                   InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 8000);
                                   Service.getInstance().sendThongBao(player, "Học nghề thợ rèn thành công");    
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Hết thỏi vàng rồi cu.\n " + "Thiếu"
                                            + (8000- tv.quantity ) + "thỏi vàng");
                                }
                                    break;
                            }
                        }
                    }
                    else if (player.iDMark.getIndexMenu() == 1001) {
            if (select == 0) {
            player.nPoint.nghenghiep = 0;
            Service.getInstance().sendThongBao(player, "Đã bỏ nghề thành công.");
    }
}
                else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.THO_SAN:
                                case CombineServiceNew.THO_REN:
                                case CombineServiceNew.PHAP_SU:  
                              CombineServiceNew.gI().startCombine(player);
                    }
                             
                }
                }
                
            }
        }
    };
}
     
    public static Npc TK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, ta có thể giúp gì được cho ngươi?",
                                "Đến hành tinh xanh", "Cửa hàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 160) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_35_0) {
                                        Service.gI().sendThongBao(player, "Hãy làm nhiệm vụ trước");
                                        return;
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 876);
                                        break;
                                    }
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "TK", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Naruto(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, ta có thể giúp gì được cho ngươi?",
                                "Cửa hàng danh hiệu", "Từ Chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "Hokage", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc HK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 174) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Chế Tạo trang bị Hoàng Kim", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 174) {
                        if (select == 0) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_HK);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }

    public static Npc HD(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 174) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Mày tìm bố có việc gì?", "Khảm trang bị Hoàng Kim", "Tẩy khảm trang bị Hoàng Kim");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 174) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.Kham);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TKham);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.Kham:
                                case CombineServiceNew.TKham:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Vào đại chiến ngọc rồng đen, "
                                        + "Có mún vô không?",
                                        "Hướng dẫn\nthêm", "Đồng ý", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Người phải có bang!");
                                    return;
                                }
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù Phép", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SHOP đồ thiên sứ\n|6| Mang đủ 5 món đồ Hủy diệt. Ta sẽ bán đồ thiên sứ cho ngươi",
                            "Cửa hàng thiên sứ", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                if (player.setClothes.setDHD == 5) {
                                    ShopServiceNew.gI().opendShop(player, "THIEN_SU", true);
                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu mặc 5 món đồ Hủy diệt", "Đóng");
                                }
                                break;
                            }
                    }
                }
            }
        };
    }

    public static Npc TD(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SHOP đồ hoàng kim\n|6| Mang đủ 5 món đồ hoàng kim. Ta sẽ bán đồ vip cho ngươi",
                            "Cửa hàng hoàng kim", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                if (player.setClothes.setDHK == 5) {
                                    ShopServiceNew.gI().opendShop(player, "HOANGKIM", true);
                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu mặc 5 món đồ hoàng kim", "Đóng");
                                }
                                break;
                            }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu65(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Chế Tạo trang bị thiên sứ", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        if (select == 0) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|SHOP đồ Hủy diệt\n|6| Mang đủ 5 món đồ Thần linh và đem 99 Thức ăn đến cho ta. Ta sẽ bán đồ Hủy diệt cho ngươi",
                            "Cửa hàng Hủy diệt", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                if (player.check99ThucAnHuyDiet() == true) {
                                    if (player.setClothes.setDTL == 5) {
                                        ShopServiceNew.gI().opendShop(player, "BILL", true);
                                    } else {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu mặc 5 món Thần linh", "Đóng");
                                    }
                                } else {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi chưa đủ 99 thức ăn", "Đóng");
                                }
                                break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, cậu muốn tôi giúp gì?", "Nhiệm vụ\nhàng ngày", "Nhiệm vụ\nthành tích", "Từ chối");
                    }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    player.achievement.Show();
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 19) {
                        this.createOtherMenu(player, 0, "Ngươi đã trên 80Ty có muốn săn đệ uub không?", "Có", "Đéo");
                    }
                    if (mapId == 201) {
                        this.createOtherMenu(player, 0, "Mày Có Muốn Về Với Mẹ Không?", "OK", "Từ chối");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 19) {
                                if (player.nPoint.power < 79999999999L) {
                                    Service.gI().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 201, -1, 174);
                            }
                            if (mapId == 201) {
                                ChangeMapService.gI().changeMapInYard(player, 19, -1, 469);
                            }

                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Từ chối");

                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 1 thỏi vàng\bGiá trị ký gửi 1-100k thỏi vàng hoặc 1-1tr hồng ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            if (!pl.getSession().actived) {
                                Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                            } else {
                                ShopKyGuiService.gI().openShopKyGui(pl);
                            }
                            break;

                    }
                }
            }
        };
    }

    private static Npc events(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (!canOpenNpc(player)) return;
            EventDAO.loadEventFromSk();
            Event event = EventDAO.getCurrentEvent();
            if (event == null) {
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hiện tại không có sự kiện nào đang diễn ra.", "Đóng");
                return;
            }
            this.createOtherMenu(player, ConstNpc.BASE_MENU, 
                "Sự kiện " + event.getName() + " đang diễn ra!\nBạn muốn làm gì?", 
                "Đổi item\n sự kiện",
                "Hướng\ndẫn", 
                "Đóng");
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (!canOpenNpc(player)) return;

            Event event = EventDAO.getCurrentEvent();
            Item thoivang = ItemService.gI().createNewItem((short) 457);
            Item ItemSK1 = ItemService.gI().createNewItem((short) event.getReward_1());
            Item ItemSK2 = ItemService.gI().createNewItem((short) event.getReward_2());
            if (event == null) {
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hiện tại không có sự kiện nào!", "Đóng");
                System.out.println("Event hiện tại: null " );
                return;           
            }
            if (player.iDMark.isBaseMenu()) {
            
                switch (select) {
                    case 0: // "Đổi vật phẩm"
                        if (event.npcId == 3100) {
                        this.createOtherMenu(player, event.getNpcId(), 
                            "Bạn cần đủ 33 vật phẩm sự kiện mỗi loại và 10 thỏi vàng để đổi " + ItemSK1.template.name +
                                    ".\n Bạn cần đủ 101 vật phẩm sự kiện mỗi loại và x4 tờ 10k vnd để đổi " + ItemSK2.template.name , 
                            "Đổi \n" + ItemSK1.template.name, 
                            "Đổi \n" + ItemSK2.template.name, 
                            "Từ chối");}
                        
                        break;

                case 1: // Hướng dẫn 
                    if (event.npcId == 3100)
                    {NpcService.gI().createTutorial(player, this.avartar, ConstNpc.SU_KIEN_HE);}
            }
        } else if (player.iDMark.getIndexMenu()== 3100) {
              List<Integer> requiredItemIds = EventDAO.getItemDropList(); // [2142, 2143, 2144, 2145]
            switch (select){
                case 0:     
int sl1 = 33;
// Kiểm tra đủ số lượng trước
for (int itemId : requiredItemIds) {
    Item item = InventoryServiceNew.gI().findItemBag(player, itemId);
    if (item == null || item.quantity < sl1) {
        Service.gI().sendThongBao(player, "Bạn cần đủ mỗi vật phẩm x" + sl1 + " để đổi.");
        return;
    }
}
Item item457 = InventoryServiceNew.gI().findItemBag(player, 457);
if (item457 == null || item457.quantity < 9) {
    Service.gI().sendThongBao(player, "Bạn cần 10 thỏi vàng.");
    return;
}
if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
    Service.gI().sendThongBao(player, "Túi đồ của bạn đã đầy!");
    return;
}
// Trừ vật phẩm
    requiredItemIds.stream().map((itemId) -> InventoryServiceNew.gI().findItemBag(player, itemId)).filter((item) -> (item != null)).forEachOrdered((item) -> {
    InventoryServiceNew.gI().subQuantityItemsBag(player,item, sl1);
            });
    InventoryServiceNew.gI().subQuantityItemsBag(player, item457, 10);
// Cập nhật túi đồ và gửi phần thưởng
InventoryServiceNew.gI().sendItemBags(player);
InventoryServiceNew.gI().addItemBag(player, ItemSK1);
InventoryServiceNew.gI().sendItemBags(player);
Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemSK1.template.name + "!");
    break;
                 case 1:        
int sl2 = 101;
// Kiểm tra đủ số lượng trước
for (int itemId : requiredItemIds) {
    Item item = InventoryServiceNew.gI().findItemBag(player, itemId);
    if (item == null || item.quantity < sl2) {
        Service.gI().sendThongBao(player, "Bạn cần đủ mỗi vật phẩm x" + sl2 + " để đổi.");
        return;
    }
}
Item tien = InventoryServiceNew.gI().findItemBag(player, 1004);
if (tien == null || tien.quantity < 3) {
    Service.gI().sendThongBao(player, "Công làm diều là 4 tờ 10k nhé.");
    return;
}

if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
    Service.gI().sendThongBao(player, "Túi đồ của bạn đã đầy!");
    return;
}
// Trừ vật phẩm
    requiredItemIds.stream().map((itemId) -> InventoryServiceNew.gI().findItemBag(player, itemId)).filter((item) -> (item != null)).forEachOrdered((item) -> {
    InventoryServiceNew.gI().subQuantityItemsBag(player,item, sl2);
            });
    InventoryServiceNew.gI().subQuantityItemsBag(player, tien, 4);
// Cập nhật túi đồ và gửi phần thưởng
InventoryServiceNew.gI().sendItemBags(player);
InventoryServiceNew.gI().addItemBag(player, ItemSK2);
InventoryServiceNew.gI().sendItemBags(player);
Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemSK2.template.name + "!");
    break;
            }
        }
    }
};}
    
    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHAN_MENH:
                    return chanmenh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHAMPA:
                    return champa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LeHoi:
                    return lehoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OBITO:
                    return obito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGO_KHONG:
                    return ngokhong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FA:
                    return fa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VIP:
                    return vip(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GENSHIN:
                    return genshin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT2:
                    return baHatMit2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TELE:
                    return tele(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAI:
                    return dai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NAMI:
                    return nami(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DN:
                    return dn(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAN:
                    return tan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FU:
                    return fu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MAI:
                    return mai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TK:
                    return TK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.naruto:
                    return Naruto(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HK:
                    return HK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HD:
                    return HD(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TD:
                    return TD(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGHE_NGHIEP:
                    return thoNghe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HAHA:
                    return gapthu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CAUCA:
                    return cauca(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MEO_THAN_TAI:
                    return meothantai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MEO_THAN_TAI1:
                    return meothantai1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return npcThienSu65(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Yardrat:
                    return Yardrat(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO:
                    return tosukaio(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.EVENT:
                    return events(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: //                        if (player.getSession().actived) 
                    {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.gI().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONTRB://TRB
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONTRB:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronTRB(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2033:
                    case ConstNpc.MENU_OPTION_USE_ITEM2034:
                    case ConstNpc.MENU_OPTION_USE_ITEM2035:
                        try {
                        ItemService.gI().OpenDHK(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2036:
                    case ConstNpc.MENU_OPTION_USE_ITEM2037:
                    case ConstNpc.MENU_OPTION_USE_ITEM2038:
                        try {
                        ItemService.gI().OpenDHKVIP(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.ruby >= 50 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.ruby -= 50;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;

                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                int gender1 = player.gender;
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player, gender1);
                                }
                                break;
                            case 1:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 2:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 3:
                                Input.gI().buffvnd(player);
                                break;
                            case 4:
                                Input.gI().createFormBuffItemVip(player);
                                break;
                        }
                        break;

                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.TTMEM:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TTMEM, 28711,
                                        "|7|Thông tin Sư Phụ " + player.name
                                        + "\n|8|Sức Mạnh: " + Util.numberToMoney(player.nPoint.power)
                                        + "\n|4|Hp: " + Util.getFormatNumber(player.nPoint.hp) + "/" + Util.getFormatNumber(player.nPoint.hpMax)
                                        + "\n|4|Ki: " + Util.getFormatNumber(player.nPoint.mp) + "/" + Util.getFormatNumber(player.nPoint.mpMax)
                                        + "\n|4|Sức đánh: " + Util.getFormatNumber(player.nPoint.dame),
                                        new String[]{"Thông Tin\n Sư Phụ", "Thông Tin\n Đệ Tử", "Đóng"},
                                        player);
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TTMEM, 28711,
                                        "|7|Thông tin đệ tử " + player.pet.name
                                        + "\n|8|Sức Mạnh: " + Util.numberToMoney(player.pet.nPoint.power)
                                        + "\n|4|Hp: " + Util.getFormatNumber(player.pet.nPoint.hp) + "/" + Util.getFormatNumber(player.pet.nPoint.hpMax)
                                        + "\n|4|Ki: " + Util.getFormatNumber(player.pet.nPoint.mp) + "/" + Util.getFormatNumber(player.pet.nPoint.mpMax)
                                        + "\n|4|Sức đánh: " + Util.getFormatNumber(player.pet.nPoint.dame),
                                        new String[]{"Thông Tin\n Sư Phụ", "Thông Tin\n Đệ Tử", "Đóng"},
                                        player);
                                break;
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
