package com.sparksplugin.gateauthenticator.model;

public class Tokens {
    private String sender;
    private String username;
    private boolean verified;
    private boolean expired;
    private String id;
    private String ztime;
    private String zdate;
    private String supername;
    private String mtype;
    private String receiver;
    private String reason;
    private boolean approved;
    public Tokens(String sender, String username, boolean verified, String ztime, String zdate, String supername,String mtype,String id,boolean expired) {
        this.sender = sender;
        this.username = username;
        this.verified = verified;
        this.ztime = ztime;
        this.zdate = zdate;
        this.supername = supername;
        this.mtype = mtype;
        this.id = id;
        this.expired = expired;
    }
    public Tokens() {

    }
    public boolean isApproved(){return approved;}
    public String getReason(){return reason;}
    public boolean isExpired()
    {
        return expired;
    }
    public String getReceiver()
    {
        return receiver;
    }

    public String getId()
    {
        return id;
    }
    public String getmType()
    {
        return mtype;
    }
    public String getSupername()
    {
        return supername;
    }
    public String getSender()
    {
        return sender;
    }
    public String getUsername()
    {
        return username;
    }
    public String getZtime()
    {
        return ztime;
    }
    public String getZdate()
    {
        return zdate;
    }
    public boolean isVerified()
    {
        return verified;
    }

}
