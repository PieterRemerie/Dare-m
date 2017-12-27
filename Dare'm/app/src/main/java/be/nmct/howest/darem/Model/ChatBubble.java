package be.nmct.howest.darem.Model;

/**
 * Created by Piete_000 on 19/12/2017.
 */

public class ChatBubble {
    private String content;
    private String name;
    private Integer type;

    public ChatBubble(String content,String name, Integer type){
        this.content = content;
        this.name = name;
        this.type = type;
    }

    public String getContent(){
        return content;
    }
    public String getName(){
        return name;
    }

    public Integer getType(){
        return type;
    }
}
