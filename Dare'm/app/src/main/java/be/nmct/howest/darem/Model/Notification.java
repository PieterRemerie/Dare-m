package be.nmct.howest.darem.Model;

/**
 * Created by Piete_000 on 15/12/2017.
 */

public class Notification {
    public String friendsId;
    public String message;

    public Notification(){

    }
    public Notification(String friendsId, String message){
        this.friendsId = friendsId;
        this.message = message;
    }
}
