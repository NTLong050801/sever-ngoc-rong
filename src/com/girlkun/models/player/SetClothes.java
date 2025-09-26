package com.girlkun.models.player;

import com.girlkun.models.item.Item;

public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;
    public byte sieuvip;
    public byte sucdanh;
    public byte yamcha;
    public byte santa;

    public byte nail;
    public byte kami;

    public byte radic;
    public byte broly;
    public byte setJr;

    public byte setDHD;
    public byte setDHK;
    public byte setDTS;
    public byte setDTL;
     public byte setMSK;
    public boolean godClothes;

    public void setup() {
        setDefault();
        setupSKT();
        setupDTS();
        setupDHD();
        setupDTL();
        setupDHK();
        setupNewSKH();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            broly++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 213:
                        case 221:
                            isActSet = true;
                            yamcha++;
                            break;
                        case 214:
                        case 224:
                            isActSet = true;
                            santa++;
                            break;
                        case 215:
                        case 220:
                            isActSet = true;
                            nail++;
                            break;
                        case 216:
                        case 219:
                            isActSet = true;
                            kami++;
                            break;
                        case 218:
                        case 223:
                            isActSet = true;
                            nappa++;
                            break;
                        case 217:
                        case 222:
                            isActSet = true;
                            radic++;
                            break;
                        case 225:
                        case 227:
                            isActSet = true;
                            sucdanh++;
                            break;
                        case 226:
                        case 228:
                            isActSet = true;
                            sieuvip++;
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setupDHK() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 200) {
                                setDHK++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDTS() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 120) {
                                setDTS++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDHD() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDTL() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 15) {
                                setDTL++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }
    
    //new set 
    private void setupNewSKH() {
    for (int i = 0; i < 6; i++) {
        Item item = this.player.inventory.itemsBody.get(i);
          if (item == null || !item.isNotNullItem()) continue;// bỏ qua nếu slot trống
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 234) {
                setJr++;
                break; // chỉ đếm 1 lần với mỗi item
            }
        }
    }
}

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.santa = 0;
        this.yamcha = 0;
        this.radic = 0;
        this.broly = 0;
        this.nail = 0;
        this.kami = 0;
        this.sieuvip= 0;
        this.sucdanh = 0;
        this.setDHD = 0;
        this.setDHK = 0;
        this.setDTS = 0;
        this.setDTL = 0;
        this.setJr = 0;
        this.godClothes = false;
    }

    public void dispose() {
        this.player = null;
    }
}
