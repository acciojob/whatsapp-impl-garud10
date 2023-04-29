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
    private HashSet<String> userMobile;
    private HashSet<Message> messageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.messageMap = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        customGroupCount++;

        if(users.size() == 2){
            Group group = new Group(""+users.get(1), users.size());
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);
            return group;
        }
        else{
            Group group = new Group("Group "+customGroupCount, users.size());
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);
            return group;
        }

    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId,content);
        messageMap.add(message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(groupUserMap.containsKey(group) == false){
            throw new Exception("Group does not exist");
        }
        boolean flag = false;
        List<User> list = groupUserMap.get(group);
        for(User user : list){
            if(user == sender) flag = true;
        }
        if(flag == false){
            throw new Exception("You are not allowed to send message");
        }

        if(groupMessageMap.containsKey(group)) {
            List<Message> list2 = groupMessageMap.get(group);
            list2.add(message);
            groupMessageMap.put(group,list2);
        }
        else{
            List<Message> list2 = new ArrayList<>();
            list2.add(message);
            groupMessageMap.put(group,list2);
        }
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(groupUserMap.containsKey(group) == false){
            throw new Exception("Group does not exist");
        }
        User currentApprover = adminMap.get(group);
        if(currentApprover != approver){
            throw new Exception("Approver does not have rights");
        }

        if(groupUserMap.get(group).contains(user) == false){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
}
