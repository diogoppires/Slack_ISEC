/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.clientCommunication;

/**
 *
 * @author Rui
 */
public class ClientLog {
    StringBuilder msgLog;
    StringBuilder msg;
    
    public ClientLog(){

        msgLog = new StringBuilder();
        msg = new StringBuilder();
    }
    
    public void addMsg(String msg){
        msgLog.append(msg);
        msgLog.append("\n");
    }
    public void addMsgM(String msgs){
        msg.append(msgs);
        msg.append("\n");
    }
    
    public String getLog(){
        String strLog = msgLog.toString();
        msgLog.replace(0, msgLog.length(), "");
        return strLog;
    }
    public String getLogM(){
        String str = msg.toString();
        msg.replace(0, msg.length(), "");
        return str;
    }

}
