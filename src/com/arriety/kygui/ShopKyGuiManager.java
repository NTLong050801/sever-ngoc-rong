package com.arriety.kygui;

import com.girlkun.database.GirlkunDB;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONValue;

public class ShopKyGuiManager {

    private static ShopKyGuiManager instance;

    public static ShopKyGuiManager gI() {
        if (instance == null) {
            instance = new ShopKyGuiManager();
            }
        return instance;
    }

    public long lastTimeUpdate;

    public String[] tabName = {"Áo Quần", "Găng Giày", "Phụ Kiện", "Linh tinh", ""};

    public List<ItemKyGui> listItem = new ArrayList<>();

    public void save() {
        try (Connection con = GirlkunDB.getConnection();) {
            Statement s = con.createStatement();
            s.execute("TRUNCATE shop_ky_gui");
            for (ItemKyGui it : this.listItem) {
                if (it != null) {
                    s.execute(String.format("INSERT INTO `shop_ky_gui`(`id`, `player_id`, `tab`, `item_id`,`gold`, `ruby`, `quantity`, `itemOption`, `isUpTop`, `isBuy`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                            it.id, it.player_sell, it.tab, it.itemId, it.goldSell, it.rubySell, it.quantity, JSONValue.toJSONString(it.options).equals("null") ? "[]" : JSONValue.toJSONString(it.options), it.isUpTop, it.isBuy ? 1 : 0));
                }
            }
        } catch (Exception e) {
        }
    }
}
