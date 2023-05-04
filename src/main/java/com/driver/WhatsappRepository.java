package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String,User> mobileUserMap;
    private HashMap<Integer,Message> messageMap;
    private int customGroupCount;
    private int messageCount;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.mobileUserMap = new HashMap<>();
        this.messageMap = new HashMap<>();
        this.customGroupCount = 0;
        this.messageCount = 0;
    }

    public void addUser(String mobile, User user) {
        mobileUserMap.put(mobile, user);
    }
    public Optional<User> getUserForMobile(String mobile) {
        if(mobileUserMap.containsKey(mobile)) {
            return Optional.of(mobileUserMap.get(mobile));
        }
        return Optional.empty();
    }

    public void addGroup(Group group, List<User> users, User admin) {
        groupUserMap.put(group,users);
        adminMap.put(group,admin);

    }
    public void addToCustomerGroupCount() {
        this.customGroupCount++;
    }

    public int getCustomerGroupCount() {
        return this.customGroupCount;
    }


    public int getMessageCount() {
        return this.messageCount;
    }

    public void addToMessageCount() {
        this.messageCount++;
    }

    public void saveMessage(Message message) {
        messageMap.put(message.getId(), message);
    }

    public Boolean findGroup(Group group) {
        if(groupUserMap.containsKey(group)) {
            return true;
        }
        return false;
    }

    public List<User> getUsersInGroup(Group group) {
        return groupUserMap.get(group);
    }

    public void sendMessageToGroup( Group group, User sender,Message message) throws RuntimeException{
        List<Message> msgs = new ArrayList<>();
        if(groupMessageMap.containsKey(group)) {
            msgs = groupMessageMap.get(group);
        }
        msgs.add(message);
        groupMessageMap.put(group, msgs);
        senderMap.put(message,sender);
        messageMap.put(message.getId(), message);
    }
    public int getMessageCountInGroup(Group group) {
        return groupMessageMap.get(group).size();
    }

    public User getGroupAdmin(Group group) {
        return adminMap.get(group);
    }

    public void updateAdminForGroup(Group group, User user) {
        adminMap.put(group, user);
    }



}
