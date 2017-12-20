package be.nmct.howest.darem.Model;

/**
 * Created by Piete_000 on 19/12/2017.
 */

public class ChatBubble {
    private String content;
    private boolean myMessage;

    public ChatBubble(String content, boolean myMessage){
        this.content = content;
        this.myMessage = myMessage;
    }

    public String getContent(){
        return content;
    }

    public boolean myMessage(){
        return myMessage;
    }
}
