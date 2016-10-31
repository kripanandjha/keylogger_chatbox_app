package com.example.kripanand.keylogger_chatbox;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vinuth on 23/10/16.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserID(){
        return getString("Name");
    }

    public String getBody(){
        return getString("Body");
    }

    public void setUserID(String userID){
        put("Name", userID);
    }

    public void setBody(String body){
        put("Body",body);
    }
}
