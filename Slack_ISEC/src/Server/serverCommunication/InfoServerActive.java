package Server.serverCommunication;

public class InfoServerActive {
    private ServerInfoToClients sIC;
    private boolean ping;

    public InfoServerActive(ServerInfoToClients sIC) {
        this.sIC = sIC;
        ping = false;
    }

    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }
    
    public ServerInfoToClients getServerITC(){
        return sIC;
    }
   
}
