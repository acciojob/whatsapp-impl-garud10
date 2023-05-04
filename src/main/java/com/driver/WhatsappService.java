package com.driver;

import java.util.ArrayList;
import java.util.List;

public class WhatsappService {
    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception{
        User user = new User(name, mobile);
        if(whatsappRepository.getUserForMobile(mobile).isEmpty()) {
            whatsappRepository.addUser(mobile, user);
            return "SUCCESS";
        }
        else {
            throw new RuntimeException("User already exists");
        }
    }


    public Group createGroup(List<User> users) {
        int noOfParticipants = users.size();
        String name;
        User admin;
        if(noOfParticipants < 2) {
            throw new RuntimeException("fkuhvf");
        }
        if(noOfParticipants == 2) {
            name = users.get(1).getName();
            admin = users.get(0);
        }
        else {
            int groupNo = whatsappRepository.getCustomerGroupCount();
            name = "Group " + (groupNo + 1);
            admin = users.get(0);
            whatsappRepository.addToCustomerGroupCount();
        }
        Group group = new Group(name,noOfParticipants);
        whatsappRepository.addGroup(group, users, admin);
        return group;
    }


    public int createMessage(String content) {
        int msgId = whatsappRepository.getMessageCount()+1;
        Message message = new Message(msgId, content);
        //update msg count
        whatsappRepository.addToMessageCount();
        //save msg in repo
        whatsappRepository.saveMessage(message);
        return msgId;
    }


    public int sendMessage(Message message, User sender, Group group) throws RuntimeException{
        Boolean groupPresesnt = whatsappRepository.findGroup(group);
        if(!groupPresesnt) {
            throw new RuntimeException("Group does not exist");
        }
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        List<User> users = whatsappRepository.getUsersInGroup(group);
        if(!users.contains(sender)){
            throw new RuntimeException("You are not allowed to send message");
        }
        //If the message is sent successfully, return the final number of messages in that group.
        whatsappRepository.sendMessageToGroup(group, sender, message);
        return whatsappRepository.getMessageCountInGroup(group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        Boolean groupPresesnt = whatsappRepository.findGroup(group);
        if(!groupPresesnt) {
            throw new RuntimeException("Group does not exist");
        }
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        User admin = whatsappRepository.getGroupAdmin(group);
        if(!admin.equals(approver)) {
            throw new RuntimeException("Approver does not have rights");
        }
        //Throw "User is not a participant" if the user is not a part of the group
        List<User> users = whatsappRepository.getUsersInGroup(group);
        if(!users.contains(user)){
            throw new RuntimeException("User is not a participant");
        }
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        whatsappRepository.updateAdminForGroup(group, user);
        return "SUCCESS";
    }
}
