package com.girlkun.event;

/**
 *
 * @author DVũ HYến
 */
public class Event {
    private int id; // Nếu có trường id tự tăng
    public String name;
    public String description;
    public String listItem;
    public int Reward_1;
    public int Reward_2;
    public int npcId;
   
    public Event() {}

public Event(int id, String name, String description, String listItem,int Reward,int Reward1, int npcId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.listItem = listItem;
    this.Reward_1 = Reward;
    this.Reward_2 = Reward1;
    this.npcId = npcId;
}

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getReward_1() { return Reward_1; }
    public void setReward_1(int Reward1) { this.Reward_1 = Reward_1; }
    
    public int getReward_2() { return Reward_2; }
    public void setReward_2(int Reward2) { this.Reward_2 = Reward_2; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getListItem() { return listItem; }
    public void setListItem(String listItem) { this.listItem = listItem; }

    public int getNpcId() { return npcId; }
    public void setNpcId(int npcId) { this.npcId = npcId; }
}