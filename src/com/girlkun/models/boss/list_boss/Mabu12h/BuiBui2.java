package com.girlkun.models.boss.list_boss.Mabu12h;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.item.Item;
import java.util.Random;

public class BuiBui2 extends Boss {

    public BuiBui2() throws Exception {
        super(BossID.BUI_BUI_2, BossesData.BUI_BUI_2);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length -1);

        if (Util.isTrue(10, 100)) {
            if (Util.isTrue(10, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 562, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone,new ItemMap (Util.RaitiDoc12(zone, Manager.itemDC12[randomc12], 1, this.location.x, this.location.y, plKill.id)));
            return;
        }
        else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        
            if (Util.isTrue(20, 100)) {  
           
switch (Util.nextInt(0, 12)) {
    case 0:
 ItemMap itemMap;
            itemMap = Util.ratiItem(zone, 555, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
            int rand = Util.nextInt(1, 100);
if (rand <= 30) {
    itemMap.options.add(new Item.ItemOption(221, 1));
    itemMap.options.add(new Item.ItemOption(213, 1));
} else if (rand <= 60) {
    itemMap.options.add(new Item.ItemOption(224, 1));
    itemMap.options.add(new Item.ItemOption(214, 1));
} else if (rand <= 90) {
     itemMap.options.add(new Item.ItemOption(139, 1));
    itemMap.options.add(new Item.ItemOption(127, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(141, 1));
    itemMap.options.add(new Item.ItemOption(129, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(230, 1));
    itemMap.options.add(new Item.ItemOption(229, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(140, 1));
    itemMap.options.add(new Item.ItemOption(128, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));         
}}
            Service.getInstance().dropItemMap(this.zone, itemMap);
 break;
  case 1:
            itemMap = Util.ratiItem(zone, 556, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
            int rand = Util.nextInt(1, 100);
if (rand <= 30) {
    itemMap.options.add(new Item.ItemOption(221, 1));
    itemMap.options.add(new Item.ItemOption(213, 1));
} else if (rand <= 60) {
    itemMap.options.add(new Item.ItemOption(224, 1));
    itemMap.options.add(new Item.ItemOption(214, 1));
} else if (rand <= 90) {
     itemMap.options.add(new Item.ItemOption(139, 1));
    itemMap.options.add(new Item.ItemOption(127, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(141, 1));
    itemMap.options.add(new Item.ItemOption(129, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(230, 1));
    itemMap.options.add(new Item.ItemOption(229, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(140, 1));
    itemMap.options.add(new Item.ItemOption(128, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
                        Service.getInstance().dropItemMap(this.zone, itemMap);
break;

case 2:
            itemMap = Util.ratiItem(zone, 562, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
                        Service.getInstance().dropItemMap(this.zone, itemMap);
break;

case 3:

            itemMap = Util.ratiItem(zone, 563, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
            int rand = Util.nextInt(1, 100);
if (rand <= 30) {
    itemMap.options.add(new Item.ItemOption(221, 1));
    itemMap.options.add(new Item.ItemOption(213, 1));
} else if (rand <= 60) {
    itemMap.options.add(new Item.ItemOption(224, 1));
    itemMap.options.add(new Item.ItemOption(214, 1));
} else if (rand <= 90) {
     itemMap.options.add(new Item.ItemOption(139, 1));
    itemMap.options.add(new Item.ItemOption(127, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(141, 1));
    itemMap.options.add(new Item.ItemOption(129, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(230, 1));
    itemMap.options.add(new Item.ItemOption(229, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(140, 1));
    itemMap.options.add(new Item.ItemOption(128, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 4:
            itemMap = Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(33, 100)){
                ///rada td
            int rand = Util.nextInt(1, 100);
if (rand <= 30) {
    itemMap.options.add(new Item.ItemOption(221, 1));
    itemMap.options.add(new Item.ItemOption(213, 1));
} else if (rand <= 60) {
    itemMap.options.add(new Item.ItemOption(224, 1));
    itemMap.options.add(new Item.ItemOption(214, 1));
} else if (rand <= 90) {
     itemMap.options.add(new Item.ItemOption(139, 1));
    itemMap.options.add(new Item.ItemOption(127, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(141, 1));
    itemMap.options.add(new Item.ItemOption(129, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(230, 1));
    itemMap.options.add(new Item.ItemOption(229, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(140, 1));
    itemMap.options.add(new Item.ItemOption(128, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
}}
            else if   (Util.isTrue(50, 100)){
                ///rada nm
            int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
           else { 
                ///rada xd
            int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(223, 1));
    itemMap.options.add(new Item.ItemOption(218, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(222, 1));
    itemMap.options.add(new Item.ItemOption(217, 1));
}  else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(136, 1));
    itemMap.options.add(new Item.ItemOption(133, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(137, 1));
    itemMap.options.add(new Item.ItemOption(134, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(138, 1));
    itemMap.options.add(new Item.ItemOption(135, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 5:

            itemMap = Util.ratiItem(zone, 557, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 6:

            itemMap = Util.ratiItem(zone, 558, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));

            }}            
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 7:
 
            itemMap = Util.ratiItem(zone, 564, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
             Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 8:

            itemMap = Util.ratiItem(zone, 558, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(219, 1));
    itemMap.options.add(new Item.ItemOption(216, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(220, 1));
    itemMap.options.add(new Item.ItemOption(215, 1));
} else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(143, 1));
    itemMap.options.add(new Item.ItemOption(131, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(144, 1));
    itemMap.options.add(new Item.ItemOption(132, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(142, 1));
    itemMap.options.add(new Item.ItemOption(130, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 9:
            itemMap = Util.ratiItem(zone, 559, 1, this.location.x, this.location.y, plKill.id);
           itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(223, 1));
    itemMap.options.add(new Item.ItemOption(218, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(222, 1));
    itemMap.options.add(new Item.ItemOption(217, 1));
}  else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(136, 1));
    itemMap.options.add(new Item.ItemOption(133, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(137, 1));
    itemMap.options.add(new Item.ItemOption(134, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(138, 1));
    itemMap.options.add(new Item.ItemOption(135, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 10:

            itemMap = Util.ratiItem(zone, 560, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(223, 1));
    itemMap.options.add(new Item.ItemOption(218, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(222, 1));
    itemMap.options.add(new Item.ItemOption(217, 1));
}  else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(136, 1));
    itemMap.options.add(new Item.ItemOption(133, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(137, 1));
    itemMap.options.add(new Item.ItemOption(134, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(138, 1));
    itemMap.options.add(new Item.ItemOption(135, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 11:
          
            itemMap = Util.ratiItem(zone, 566, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(223, 1));
    itemMap.options.add(new Item.ItemOption(218, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(222, 1));
    itemMap.options.add(new Item.ItemOption(217, 1));
}  else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(136, 1));
    itemMap.options.add(new Item.ItemOption(133, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(137, 1));
    itemMap.options.add(new Item.ItemOption(134, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(138, 1));
    itemMap.options.add(new Item.ItemOption(135, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
             Service.getInstance().dropItemMap(this.zone, itemMap);
break;
case 12:
            itemMap = Util.ratiItem(zone, 567, 1, this.location.x, this.location.y, plKill.id);
            itemMap.options.add(new Item.ItemOption(49, Util.nextInt(1, 5)));
              itemMap.options.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
              itemMap.options.add(new Item.ItemOption(103, Util.nextInt(5, 10)));    
            if   (Util.isTrue(1, 20)){
             int rand = Util.nextInt(1, 100);
if (rand <= 45) {
    itemMap.options.add(new Item.ItemOption(223, 1));
    itemMap.options.add(new Item.ItemOption(218, 1));
} else if (rand <= 90) {
    itemMap.options.add(new Item.ItemOption(222, 1));
    itemMap.options.add(new Item.ItemOption(217, 1));
}  else if (rand <= 93) {
   itemMap.options.add(new Item.ItemOption(136, 1));
    itemMap.options.add(new Item.ItemOption(133, 1));
} else if (rand <= 96) {
   itemMap.options.add(new Item.ItemOption(137, 1));
    itemMap.options.add(new Item.ItemOption(134, 1));
} else if (rand <= 99) {
   itemMap.options.add(new Item.ItemOption(138, 1));
    itemMap.options.add(new Item.ItemOption(135, 1));
} else {
  itemMap.options.add(new Item.ItemOption(228, 1));
    itemMap.options.add(new Item.ItemOption(226, 1));
            itemMap.options.add(new Item.ItemOption(3, 15));
            }}
            Service.getInstance().dropItemMap(this.zone, itemMap);
break;
    }}
           
            
        plKill.fightMabu.changePoint((byte) 40);
    }  public float injured(Player plAtt, float damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
               damage = damage/2;
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