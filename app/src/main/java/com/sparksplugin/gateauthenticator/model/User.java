package com.sparksplugin.gateauthenticator.model;


public class User {

    private String UID;
    private String batch;
    private String branch;
    private String id;
    private String sem;
    private String supid;
    private String type;
    private String username;
    private boolean isdataset;
    private String sender;


    public String getSender(){return sender;}
    public String getSupid() {
        return supid;
    }
    public boolean isIsdataset()
    {
        return isdataset;
    }


    public String getUID() {
        return UID;
    }



    public String getBatch() {
        return batch;
    }


    public String getId() {
        return id;
    }



    public String getSem() {
        return sem;
    }



    public String getType() {
        return type;
    }



    public String getUsername() {
        return username;
    }



    public String getBranch() {
        return branch;
    }


}