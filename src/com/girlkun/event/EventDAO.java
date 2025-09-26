package com.girlkun.event;

import static java.time.ZonedDateTime.now;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDAO {
    private static int sk = 1;
    private static Event currentEvent;

    public static void loadEventFromSk() {
        if (sk == 1) {
            currentEvent = new Event(1, "hè 2025","Sự kiện có nhiều phần quà thú vị.","2142,2143,2144,2145",2146,2147,3100);
        }
        
        else {
            currentEvent = null; // hoặc tạo các event khác cho sk khác
        }
    }

    public static Event getCurrentEvent() {
        return currentEvent;
    }

    public static List<Integer> getItemDropList() {
        List<Integer> list = new ArrayList<>();
        if (currentEvent == null || currentEvent.getListItem() == null) return list;
        String[] parts = currentEvent.getListItem().split(",");
        for (String part : parts) {
            try {
                list.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }
}
