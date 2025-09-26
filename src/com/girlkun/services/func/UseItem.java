package com.girlkun.services.func;

import com.girlkun.services.MapService;
import com.girlkun.services.NpcService;
import com.girlkun.services.SkillService;
import com.girlkun.services.NgocRongNamecService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.ItemService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.RewardService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.item.Item;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.server.ServerNotify;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.girlkun.server.io.MySession;
import com.girlkun.services.EffectSkillService;
import com.girlkun.utils.Logger;
import lombok.var;

import java.util.Date;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;
    public static final int[][][] LIST_ITEM_CLOTHES = {
        // áo , quần , găng ,giày,rada
        //td -> nm -> xd
        {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//            System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 74:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFoot(pl, item.template.id);
                    break;

                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 21:
                    if (pl.newpet != null) {
                        ChangeMapService.gI().exitMap(pl.newpet);
                        pl.newpet.dispose();
                        pl.newpet = null;
                    }
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg);
                    Service.getInstance().point(pl);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    controllerCalltrb(pl, item);
                    break;
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 72: {
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                }
                default:
                    switch (item.template.id) {
                        case 457:
                            Input.gI().createFormUseGold(pl);
                            break;
                        case 992:
                            
                            if (pl.getSession().player.nPoint.power >= 280000000000L) {
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            break;
                                } else {
                                    Service.gI().sendThongBao(pl, "Vui lòng đạt 280 tỷ sức mạnh để sử dụng");
                                }
                            
                            
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;

                        case 1131:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1 && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "đệ tử ngươi phải có chiêu 2 3 chứ!");
                            }
                            break;
                        case 1979:
                            changeskill4(pl, item);
                            break;

                        case 1419: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2152, 2155));
                                linhThu.itemOptions.add(new ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                        }
                        break;

                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 5) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít thôi con");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                        case 1507:
                        case 1508:
                        case 1509:
                        case 1510:
                            useItemTime(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 1296: //cskb
                             maydoboss(pl);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1108: //đổi đệ tử
                            changePetBerus(pl, item);
                            break;
                        case 547: //đổi đệ tử
                            changePetPic(pl, item);
                        case 1991: //đổi đệ tử
                            changePetST(pl, item);
                        case 1106: //đổi đệ tử
                            changePetZx(pl, item);
                        case 548: //đổi đệ tử
                            changePetNK(pl, item);
                        case 568: //trứngggg
                            sudungTrungBuu(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 921://bông tai
                            pl.fusion.isBTC2 = item.template.id == 921;
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 2129: //bông tai c3
                            pl.fusion.isBTC3 = item.template.id == 2129;
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 2130: //bông tai c4
                            pl.fusion.isBTC4 = item.template.id == 2130;
                            UseItem.gI().usePorata4(pl);
                            break;
                        case 2131: //bông tai c4
                            pl.fusion.isBTC5 = item.template.id == 2131;
                            UseItem.gI().usePorata5(pl);
                            break;
                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;
                      
                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 2033://hop qua skh, item 2003 td
                        case 2034://hop qua skh, item 2004 nm
                        case 2035://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHK(pl, item);
                            break;
                        case 2036://hop qua skh, item 2003 td
                        case 2037://hop qua skh, item 2004 nm
                        case 2038://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHKVIP(pl, item);
                            break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 1120:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 569:
                            useItemQuaDua(pl, item);
                            break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 1496: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1491, 1495));
                                linhThu.itemOptions.add(new ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                        }
                        break;
                        case 1497: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1487, 1490));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 15)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 15)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 14)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 14)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 14)));
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                        }
                        break;
                        case 1498: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1500, 1505));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(25, 45)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(25, 45)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(25, 45)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(10, 25)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(10, 25)));
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                        }
                        break;
                        case 1499: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1948, 1963));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(25, 55)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(25, 55)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(25, 55)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(25, 39)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(25, 39)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(25, 39)));
                                    if (Util.isTrue(1, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(117, Util.nextInt(5, 15)));
                                    linhThu.itemOptions.add(new ItemOption(5, Util.nextInt(5, 15)));
                                    }
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                        }
                        break;
                        case 2020: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2100, 2107));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1,15)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 15)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1,15)));
                                    linhThu.itemOptions.add(new ItemOption(87, 1));
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                        }
                        break;
                        case 1719: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(1716, 1718));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 9)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 9)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 9)));
                                    
                                }
                                pl.LeHoi += 1;
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được " + linhThu.template.name);
                            }
                        }
                        break;
                        case 2021: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2110, 2114));
                                if (Util.isTrue(50, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(5, 25)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(5, 25)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 15)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(5, 25)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(5, 25)));
                                    linhThu.itemOptions.add(new ItemOption(87, 1));
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                        }
                        break;
                        case 1359:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse1 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 300);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1360:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse2 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 301);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1361:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse3 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 890);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1362:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle4 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse4 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 893);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1363:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle5 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse5 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, pl.gender + 100);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1420:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle14 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2);
                                pl.isTitleUse14 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 132);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 2 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1421:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle15 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse15 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 133);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1422:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle16 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1);
                                pl.isTitleUse16 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 134);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 1 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1423:
                            if (pl.lastTimeTitle1 == 0 && pl.lastTimeTitle2 == 0 && pl.lastTimeTitle3 == 0 && pl.lastTimeTitle4 == 0 && pl.lastTimeTitle5 == 0 && pl.lastTimeTitle14 == 0 && pl.lastTimeTitle15 == 0 && pl.lastTimeTitle16 == 0 && pl.lastTimeTitle17 == 0) {
                                pl.lastTimeTitle17 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse17 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 135);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 1 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 1 rồi !");
                            }
                            break;
                        case 1424:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle13 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse13 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 131);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 4 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1364:
                            if (pl.lastTimeTitle6 == 0 && pl.lastTimeTitle7 == 0) {
                                pl.lastTimeTitle6 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse6 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 109);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 2 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 2 rồi !");
                            }
                            break;
                        case 1365:
                            if (pl.lastTimeTitle6 == 0 && pl.lastTimeTitle7 == 0) {
                                pl.lastTimeTitle7 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse7 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 111);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 2 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 2 rồi !");
                            }
                            break;
                        case 1366:
                            if (pl.lastTimeTitle8 == 0 && pl.lastTimeTitle9 == 0 && pl.lastTimeTitle10 == 0) {
                                pl.lastTimeTitle8 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse8 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 108);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 3 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 3 rồi !");
                            }
                            break;
                        case 1367:
                            if (pl.lastTimeTitle8 == 0 && pl.lastTimeTitle9 == 0 && pl.lastTimeTitle10 == 0) {
                                pl.lastTimeTitle9 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse9 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 114);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 3 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 3 rồi !");
                            }
                            break;
                        case 1368:
                            if (pl.lastTimeTitle8 == 0 && pl.lastTimeTitle9 == 0 && pl.lastTimeTitle10 == 0) {
                                pl.lastTimeTitle10 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse10 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 115);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 3 !");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 3 rồi !");
                            }
                            break;
                        case 1369:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 1;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 123);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1370:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 2;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 124);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1371:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 3;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 125);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1372:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 4;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 126);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1376:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 5;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 127);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1377:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 6;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 128);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1378:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 7;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 129);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1379:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle11 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 999999);
                                pl.pointTop += 8;
                                pl.isTitleUse11 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 130);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1380:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle12 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse12 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 117);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 5 ngày danh hiệu loại 4!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1381:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle12 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 9995);
                                pl.isTitleUse12 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 121);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 1382:
                            if (pl.lastTimeTitle11 == 0 && pl.lastTimeTitle12 == 0 && pl.lastTimeTitle13 == 0) {
                                pl.lastTimeTitle12 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5);
                                pl.isTitleUse12 = true;
                                Service.gI().point(pl);
                                Service.gI().sendTitle(pl, 122);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Service.gI().sendThongBao(pl, "Bạn nhận danh hiệu loại 4 Vĩnh viễn!");
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đang sử dụng Danh Hiệu Loại 4 rồi !");
                            }
                            break;
                        case 2022: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2052, 2063));
                                if (Util.isTrue(30, 100)) {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(10, 30)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(10, 40)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(10, 40)));
                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
                                } else {
                                    linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(15, 30)));
                                    linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(15, 40)));
                                    linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(15, 40)));
                                    linhThu.itemOptions.add(new ItemOption(87, 1));
                                    if (Util.isTrue(1, 500)) {
                                    linhThu.itemOptions.add(new ItemOption(117, 15)); 
                                    ServerNotify.gI().notify(" Một đứa nào đó đã nhận được chúc phúc pet tăng Dame.");
                                    }
                                     if (Util.isTrue(1, 500)) {
                                    linhThu.itemOptions.add(new ItemOption(77, 25)); 
                                    ServerNotify.gI().notify(" Một đứa nào đó đã nhận được chúc phúc pet tăng HP.");
}
                                    if (Util.isTrue(1, 500)) {
                                    linhThu.itemOptions.add(new ItemOption(103, 25)); 
                                    ServerNotify.gI().notify(" Một đứa nào đó đã nhận được chúc phúc pet tăng Ki.");

                                    }
                                }
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }}
                            break;
                        
                        case 1425:
                            int time = 5000;
                            if (pl.zone.map.mapId == 176 && pl.location.x >= 611 && pl.location.y == 360) {
                                Service.gI().sendThongBaoOK(pl, "Đợi cá cắn câu");
                                ItemTimeService.gI().sendItemTime(pl, 21884, time / 1000);
                                try {
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                                ItemService.gI().OpenItem1361(pl, item);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Không thể câu cá ở đây");
                            }
                            break;
                        case 1426:
                            time = 5500;
                            if (pl.zone.map.mapId == 176 && pl.location.x >= 611 && pl.location.y == 360) {
                                Service.gI().sendThongBaoOK(pl, "Đợi cá cắn câu");
                                ItemTimeService.gI().sendItemTime(pl, 21885, time / 1000);
                                try {
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                                ItemService.gI().OpenItem1362(pl, item);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Không thể câu cá ở đây");
                            }
                            break;
                        case 2146:
                            useDieuGiay(pl, item);
                            break;
                        case 2147:
                            useDieuVai(pl, item);
                            break;    
                        
                        case 2148:
                            useRuongTim(pl, item);
                            break;
                        case 2149:
                            useRuongDo(pl, item);
                            break;    
                        case 1721:
                          if (pl.nPoint.hpg < 1350000) { pl.nPoint.hpg += 100 ;
                          Service.gI().sendThongBao(pl, "HP gốc tăng 100");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 1tr350 hpg rồi");
                          break;
                          }
                        case 1722:
                          if (pl.nPoint.hpg < 1350000) { pl.nPoint.hpg += 500 ;
                          Service.gI().sendThongBao(pl, "HP gốc tăng 500");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 1tr350 hpg rồi");
                          break;
                          }
                        case 1723:
                          if (Util.isTrue(8,10) && pl.nPoint.hpg < 1800000) { pl.nPoint.hpg += 2000 ;
                          Service.gI().sendThongBao(pl, "Đột phá thành công.");
                          }
                          else { 
                              if (pl.nPoint.hpg > 1020) {
                           pl.nPoint.hpg -= 1000;
                           Service.gI().sendThongBao(pl, "Phản phệ bị hao hụt 1000 HP gốc rồi.");
                           } else {
                          pl.nPoint.hpg = 20;
                          Service.gI().sendThongBao(pl, "Phế vật.");
                          }}
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          case 1724:
                          if (pl.nPoint.mpg < 1350000) { pl.nPoint.mpg += 100 ;
                          Service.gI().sendThongBao(pl, "MP gốc tăng 100");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 1tr350 mpg rồi");
                          break;
                          }
                        case 1725:
                          if (pl.nPoint.mpg < 1350000) { pl.nPoint.mpg += 500 ;
                          Service.gI().sendThongBao(pl, "MP gốc tăng 500");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 1tr350 mpg rồi");
                          break;
                          }
                        case 1726:
                          if (Util.isTrue(8,10) && pl.nPoint.mpg < 1800000) { pl.nPoint.mpg += 2000 ;
                          Service.gI().sendThongBao(pl, "Đột phá thành công.");
                          }
                          else { 
                              if (pl.nPoint.mpg > 1020) {
                           pl.nPoint.mpg -= 1000;
                           Service.gI().sendThongBao(pl, "Phản phệ bị hao hụt 1000 MP gốc rồi.");
                           } else {
                          pl.nPoint.mpg = 20;
                          Service.gI().sendThongBao(pl, "Phế vật.");
                          }}
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                        case 1727:
                            if (pl.nPoint.nghenghiep == 0) { 
                                Service.gI().sendThongBao(pl, "Đi học nghề đi.");
                                break;
                            }
                          if (pl.nPoint.nghenghiep < 58) { pl.nPoint.nghenghiep += 3 ;
                          Service.gI().sendThongBao(pl, "Tăng cấp nghề nghiệp thành công.");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max cấp bậc nghề rồi");
                          break;
                          }
                        case 1728:
                            if (pl.nPoint.nghenghiep == 0) { 
                                Service.gI().sendThongBao(pl, "Đi học nghề đi.");
                                break;
                            }
                          if (pl.nPoint.nghenghiep < 55) { pl.nPoint.nghenghiep += 6 ;
                          Service.gI().sendThongBao(pl, "Tăng cấp nghề nghiệp thành công.");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Nghề nghiệp cần dưới cấp 19.");
                          break;
                          }
                        case 1729:
                            if (pl.nPoint.nghenghiep == 0) { 
                                Service.gI().sendThongBao(pl, "Đi học nghề đi.");
                                break;
                            }
                            if (pl.nPoint.nghenghiep > 115) { 
                                Service.gI().sendThongBao(pl, "Max cấp độ nghề rồi .");
                                break;
                            }
                          if (Util.isTrue(2, 10)) {
                   pl.nPoint.nghenghiep += 9;
                   Service.gI().sendThongBao(pl, "Đột phá trình độ thành công.");
                   } else {
                   if (pl.nPoint.nghenghiep > 6) {
                   pl.nPoint.nghenghiep -= 6;
                   Service.gI().sendThongBao(pl, "Học ngu quá nên rớt 2 cấp liền.");
                   } else {
                   pl.nPoint.nghenghiep = 0;
                   Service.gI().sendThongBao(pl, "Học ngu quá nên bị đuổi khỏi nghề luôn.");
                 }
               } 
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          case 1730:
                          if (pl.nPoint.dameg < 62000) { pl.nPoint.dameg += 5 ;
                          Service.gI().sendThongBao(pl, "Dame gốc tăng 5");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 62k dame rồi");
                          break;
                          }
                        case 1731:
                          if (pl.nPoint.dameg < 62000) { pl.nPoint.dameg += 25 ;
                          Service.gI().sendThongBao(pl, "Dame gốc tăng 25");
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          }
                          else { Service.gI().sendThongBao(pl, "Max 62k dame rồi");
                          break;
                          }
                        case 1732:
                          if (Util.isTrue(8,10) && pl.nPoint.dameg < 90000) { pl.nPoint.dameg += 200 ;
                          Service.gI().sendThongBao(pl, "Đột phá thành công.");
                          }
                          else { 
                              if (pl.nPoint.dameg > 101) {
                           pl.nPoint.dameg -= 100;
                           Service.gI().sendThongBao(pl, "Phản phệ bị hao hụt 200 sức đánh rồi.");
                           } else {
                          pl.nPoint.dameg = 1;
                          Service.gI().sendThongBao(pl, "Phế vật.");
                          }}
                          InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                          InventoryServiceNew.gI().sendItemBags(pl);
                          Service.gI().point(pl);
                          break;
                          case 1226: 
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0)
                            { Service.gI().sendThongBao(pl, "Không đủ hành trang rồi ní.");
                            }
                            else {
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                int randomJr = Util.nextInt(1220,1225);
                            Item jiren = ItemService.gI().DoJiren((short) randomJr,pl.gender);
                            InventoryServiceNew.gI().addItemBag(pl, jiren);
                            icon[1] = jiren.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            Service.gI().sendThongBao(pl, "Chúc mừng ní đã nhận được " + jiren.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            }
                           break;
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = item.itemOptions.size();
            int ruby = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225, 16, 17, 18};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                ruby = Util.nextInt(1, 500) * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                ruby = Util.nextInt(1, 100) * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                ruby = Util.nextInt(1, 500) * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 6);
            }
            for (int i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (int i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 457);
                itemReward.quantity = Util.nextInt(1, 10);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(ruby) + " Hồng Ngọc", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addRuby(ruby);
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    public void changeskill4(Player player, Item item) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Mày làm gì có đệ ?");
        }
        if (player.pet.playerSkill.skills.get(3).skillId != -1 && player.pet.playerSkill.skills.get(2).skillId != -1) {
            player.pet.openSkill4();
            InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Đổi chiêu đệ thành công");
        } else {
            Service.gI().sendThongBao(player, "Đéo có chiêu thằng ngu");

        }
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetBerus(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetPic(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changePicPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetZx(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changezenoPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetST(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeSTPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetNK(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeNKPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void sudungTrungBuu(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeMabuPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {13, 14, 16, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{1000000, 2000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {16, 17, 18, 19, 20, 381, 382, 383, 384};
            int[][] gold = {{1, 1000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.ruby += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.ruby > Inventory.LIMIT_RUBY) {
                    pl.inventory.ruby = Inventory.LIMIT_RUBY;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useDieuGiay(Player pl, Item item) {
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        int sl = 0;
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int tile = Util.nextInt(0,1000);
            if (tile < 400)
            {int cskb = Util.nextInt(1099, 1102);
            Item it = ItemService.gI().createNewItem((short) cskb);
            sl = Util.nextInt(2,6);
            it.quantity = sl;
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được x" + sl + " " + it.template.name );
            icon[1] = it.template.iconID;
            } else if (tile < 561)
            {Item it = ItemService.gI().createNewItem( (short) 457);
             if (Util.isTrue(6, 10)){sl = 5;} else if (Util.isTrue(6,10)) {sl = 10;} else {sl = 50;}
             it.quantity = sl;
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được x" + sl + " " + it.template.name );
            icon[1] = it.template.iconID;}
            else if (tile < 711){
                Item it = ItemService.gI().createNewItem( (short) 16);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 811){
                Item it = ItemService.gI().createNewItem( (short) 15);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1"  + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 871){
                Item it = ItemService.gI().createNewItem( (short) 14);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 875){
                short idIT = 0;
                if (Util.isTrue(1, 3)) {idIT = 2148;} else if (Util.isTrue(1,2)){idIT = (short) (2039 + pl.gender);} else {idIT = (short) (2033 + pl.gender);}
                Item it = ItemService.gI().createNewItem(idIT);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + " " + it.template.name );
                ServerNotify.gI().notify(pl.name + " đã may mắn nhận được " + it.template.name);
            icon[1] = it.template.iconID;
            }
             else if (tile < 900){
                Item it = ItemService.gI().createNewItem( (short) 1004);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else {
                int randRuby = Util.nextInt(10000,100000);
                Service.gI().sendThongBao(pl, "Nhận được " + randRuby+ " ruby. " );
            pl.inventory.ruby += randRuby;
            icon[1] = 7743;
            }                     
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    
    private void useDieuVai(Player pl, Item item) {
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        int sl = 0;
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int tile = Util.nextInt(0,1000);
            if (tile < 400)
            {int nrtb = Util.nextInt(2091, 2093);
            Item it = ItemService.gI().createNewItem((short) nrtb);
            sl = Util.nextInt(1,3);
            it.quantity = sl;
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được x" + sl + " " + it.template.name );
            icon[1] = it.template.iconID;
            } else if (tile < 750)
            {Item it = ItemService.gI().createNewItem( (short) 457);
             if (Util.isTrue(6, 10)){sl = 20;} else if (Util.isTrue(9,10)) {sl = 100;} else {sl = 2000;}
             it.quantity = sl;
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được x" + sl + " " + it.template.name );
            if (sl == 2000) {
                ServerNotify.gI().notify(pl.name + " đã may mắn nhận được 2k thỏi vàng ");}
            icon[1] = it.template.iconID;}
            else if (tile < 800){
                Item it = ItemService.gI().createNewItem( (short) 2148);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 830){
                Item it = ItemService.gI().createNewItem( (short) 2149);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1"  + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 850){
                short id = 0;
                if (pl.gender ==0) {id = 0;} else if (pl.gender == 1) {id = 2;} else { id = 1;}
                Item it = ItemService.gI().createNewItem( (short) (1010 + id));
                it.itemOptions.add(new ItemOption(0, Util.nextInt(5000,10000)));
                it.itemOptions.add(new ItemOption(2, Util.nextInt(25,50)));
                it.itemOptions.add(new ItemOption(49, 30));
                it.itemOptions.add(new ItemOption(77, 40));
                it.itemOptions.add(new ItemOption(103, 40));
                if (Util.isTrue(1,2)) {
                it.itemOptions.add(new ItemOption(5, 30));
                it.itemOptions.add(new ItemOption(159, 4));} 
                else {
                it.itemOptions.add(new ItemOption(160, 70));
                it.itemOptions.add(new ItemOption(79, 40));
                }
                it.itemOptions.add(new ItemOption(87, 1));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1,15)));
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1"  + " " + it.template.name );
            icon[1] = it.template.iconID;
            }
            else if (tile < 900){
            int randIT = Util.nextInt(0,2);
            short ht = pl.gender;
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem((short) (2033 + (randIT*3) + ht), 1); 
            InventoryServiceNew.gI().addItemBag(pl, it);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1" + it.template.name  );
                ServerNotify.gI().notify(pl.name + " đã may mắn nhận được "+ it.template.name);
            icon[1] = it.template.iconID;
            }
            else if (tile < 902){
                Item it = ItemService.gI().createNewItem( (short) 2042);
                InventoryServiceNew.gI().addItemBag(pl, it);
                Service.gI().sendThongBao(pl, "Nhận được x1"  + " " + it.template.name );
                ServerNotify.gI().notify(pl.name + " đã may mắn nhận được thánh ngọc huyền bí. ");
            icon[1] = it.template.iconID;
            }   else {
                int randRuby = Util.nextInt(50000,1000000);
                Service.gI().sendThongBao(pl, "Nhận được " + randRuby+ " ruby. " );
                if (randRuby >8000000) {
                  ServerNotify.gI().notify(pl.name + " đã may mắn nhận được "+ randRuby/1000000 + " triệu Hồng ngọc.");  
                }
            pl.inventory.ruby += randRuby;
            icon[1] = 7743;
            }                     
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useRuongTim(Player pl, Item item) {
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        int sl = 300;
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 4) {
            if (Util.isTrue(2, 10))
            {short[] itemIds = {2115, 2116, 2117, 2118, 2119};
        for (short id : itemIds) {
            Item it = ItemService.gI().createNewItem(id);
            it.quantity = sl;
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được x" + sl + " " + it.template.name);  
        }
        icon[1] = 30003; 
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
    }  else {
                if (Util.isTrue(1,2)){
                                    if (Util.isTrue(70,100)){
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);
                                    icon[1] = Kiem.template.iconID;
                                    }
                                    else if (Util.isTrue(70,100)){
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);   
                                    icon[1] = Kiem.template.iconID;
                                    }
                                    else if (Util.isTrue(70,100)) {
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);    
                                    icon[1] = Kiem.template.iconID;
                                    } else  {
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);  
                                    icon[1] = Kiem.template.iconID;
                                    }} else if (Util.isTrue(995, 1000)){
                                    if (Util.isTrue(70,100)){
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);
                                    icon[1] = Kiem.template.iconID;
                                    }
                                    else if (Util.isTrue(70,100)){
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);   
                                    icon[1] = Kiem.template.iconID;
                                    }
                                    else if (Util.isTrue(70,100)) {
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);   
                                    icon[1] = Kiem.template.iconID;
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
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);
                                    icon[1] = Kiem.template.iconID;
                                    }
                                    } else {
                                        if (Util.isTrue(1, 2)) {
                                     Item Kiem = ItemService.gI().createNewItem((short) 2141, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(25,40)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(30,50)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(30,50)));
                                    Kiem.itemOptions.add(new Item.ItemOption(80,60));
                                    Kiem.itemOptions.add(new Item.ItemOption(81,60));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(108,30));
                                    Kiem.itemOptions.add(new Item.ItemOption(179,40));
                                    Kiem.itemOptions.add(new Item.ItemOption(117,30));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);   
                                    ServerNotify.gI().notify(pl.name + " đã may mắn nhận được " + Kiem.template.name);
                                    icon[1] = Kiem.template.iconID;
                                        }  else {
                                    Item Kiem = ItemService.gI().createNewItem((short) 2124, 1);  
                                    Kiem.itemOptions.add(new Item.ItemOption(49,Util.nextInt(25,40)));
                                    Kiem.itemOptions.add(new Item.ItemOption(77,Util.nextInt(30,50)));
                                    Kiem.itemOptions.add(new Item.ItemOption(103,Util.nextInt(30,50)));
                                    Kiem.itemOptions.add(new Item.ItemOption(95,25));
                                    Kiem.itemOptions.add(new Item.ItemOption(96,25));
                                    Kiem.itemOptions.add(new Item.ItemOption(48,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(22,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(23,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(97,1));
                                    Kiem.itemOptions.add(new Item.ItemOption(108,30));
                                    Kiem.itemOptions.add(new Item.ItemOption(179,40));
                                    Kiem.itemOptions.add(new Item.ItemOption(117,30));
                                    Kiem.itemOptions.add(new Item.ItemOption(41,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(42,0));
                                    Kiem.itemOptions.add(new Item.ItemOption(43,0));
                                    InventoryServiceNew.gI().addItemBag(pl, Kiem);
                                    InventoryServiceNew.gI().sendItemBags(pl);    
                                    Service.getInstance().sendThongBao(pl," đã nhận được " +Kiem.template.name);  
                                    ServerNotify.gI().notify(pl.name + " đã may mắn nhận được " + Kiem.template.name);
                                    icon[1] = Kiem.template.iconID;
                                        }
                                            }
                          CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);  
            }
         // trừ vp         
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    private void useRuongDo(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int randIT = Util.nextInt(0,3);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (Util.isTrue(7,10)) {
            Item it = ItemService.gI().createNewItem((short) (1721 + randIT*3), 1); 
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được " + it.template.name );
            icon[1] = it.template.iconID;
            } else if (Util.isTrue(4, 10)) {
            Item it = ItemService.gI().createNewItem((short) (1721 + randIT*3 +1), 1); 
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được " + it.template.name );
            icon[1] = it.template.iconID;    
            } else {
            Item it = ItemService.gI().createNewItem((short) (1721 + randIT*3+2), 1); 
            InventoryServiceNew.gI().addItemBag(pl, it);
            Service.gI().sendThongBao(pl, "Nhận được " + it.template.name );
            ServerNotify.gI().notify(pl.name + " đã may mắn nhận được " + it.template.name);
            icon[1] = it.template.iconID;    
            }
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);  
         // trừ vp         
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        } 
    }

    
    
    private void useItemQuaDua(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {2069, 2070, 2071, 2072, 2073};
            int[][] gold = {{10000000, 20000000}};
            int[][] ruby = {{10, 20}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyetSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ huyết siêu cấp");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhiSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ khí siêu cấp");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXenSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng giáp xên siêu cấp");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNoSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng cuồng nộ siêu cấp");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.getInstance().point(pl);
                break;
            case 385: //ẩn danh
                if (pl.itemTime.isUseAnDanhSC == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng ẩn danh đặc biệt");
                    return;
                }
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 1507: //bổ huyết
                if (pl.nPoint.power <= 60000000000L) {
                    Service.gI().sendThongBao(pl, "Sức mạnh bạn chưa đủ 60 tỉ!");
                    return;
                }
                pl.itemTime.lastTimex2 = System.currentTimeMillis();
                pl.itemTime.isUsex2 = true;
                break;
            case 1508: //bổ khí
                if (pl.nPoint.power <= 60000000000L) {
                    Service.gI().sendThongBao(pl, "Sức mạnh bạn chưa đủ 60 tỉ!");
                    return;
                }
                pl.itemTime.lastTimex5 = System.currentTimeMillis();
                pl.itemTime.isUsex5 = true;
                break;
            case 1509: //giáp xên
                if (pl.nPoint.power <= 60000000000L) {
                    Service.gI().sendThongBao(pl, "Sức mạnh bạn chưa đủ 60 tỉ!");
                    return;
                }
                pl.itemTime.lastTimex7 = System.currentTimeMillis();
                pl.itemTime.isUsex7 = true;
                break;
            case 1510: //cuồng nộ
                if (pl.nPoint.power <= 60000000000L) {
                    Service.gI().sendThongBao(pl, "Sức mạnh bạn chưa đủ 60 tỉ!");
                    return;
                }
                pl.itemTime.lastTimex10 = System.currentTimeMillis();
                pl.itemTime.isUsex10 = true;
                Service.getInstance().point(pl);
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1100: //bổ huyết
                if (pl.itemTime.isUseBoHuyet == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ huyết");
                    return;
                }
                pl.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyetSC = true;
                break;
            case 1101: //bổ khí
                if (pl.itemTime.isUseBoKhi == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ khí");
                    return;
                }
                pl.itemTime.lastTimeBoKhiSC = System.currentTimeMillis();
                pl.itemTime.isUseBoKhiSC = true;
                break;
            case 1102: //giáp xên
                if (pl.itemTime.isUseGiapXen == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng giáp xên");
                    return;
                }
                pl.itemTime.lastTimeGiapXenSC = System.currentTimeMillis();
                pl.itemTime.isUseGiapXenSC = true;
                break;
            case 1099: //cuồng nộ
                if (pl.itemTime.isUseCuongNo == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng cuồng nộ");
                    return;
                }
                pl.itemTime.lastTimeCuongNoSC = System.currentTimeMillis();
                pl.itemTime.isUseCuongNoSC = true;
                Service.getInstance().point(pl);
                break;
            case 1103: //ẩn danh
                if (pl.itemTime.isUseAnDanh == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng ẩn danh");
                    return;
                }
                pl.itemTime.lastTimeAnDanhSC = System.currentTimeMillis();
                pl.itemTime.isUseAnDanhSC = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    public boolean maydoboss(Player pl) {
        try {
            BossManager.gI().dobossmember(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private void controllerCalltrb(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONGTRB1 && tempId <= SummonDragon.NGOC_RONGTRB3) {
            switch (tempId) {
                case SummonDragon.NGOC_RONGTRB1:
                    SummonDragon.gI().openMenuSummonShenronTRB(pl, (byte) (tempId - 2090));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGONTRB,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 1 sao TRB ", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {

                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata5(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion5(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        Zone zoneChose = pl.mapCapsule.get(index);
        //Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapDiaNguc(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (index != 0 || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(Util.LeHaiGH(player.nPoint.hp + hpKiHoiPhuc));
            player.nPoint.setMp(Util.LeHaiGH(player.nPoint.mp + hpKiHoiPhuc));
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(Util.LeHaiGH(player.pet.nPoint.hp + hpKiHoiPhuc));
                player.pet.nPoint.setMp(Util.LeHaiGH(player.pet.nPoint.mp + hpKiHoiPhuc));
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHK(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }
    private void ItemDHKVIP(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }
}
