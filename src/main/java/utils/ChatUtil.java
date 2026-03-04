package utils;
import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;


public class ChatUtil{

    public static ActionInterface ACTION;
    public ChatUtil(){
    }
    public String battlecry(){
        return("THE CATS OF THE VOID WILL CLAIM ALL");
    }

    public String version(){
        String version = VersionUtil.getVersion();
        return "running version "+version;
    }

    private void sendAll(String message){
        ACTION.sendChat(message, ActionChat.Channel.BROADCAST);    }
}
