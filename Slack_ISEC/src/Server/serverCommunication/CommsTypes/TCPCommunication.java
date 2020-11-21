package Server.serverCommunication.CommsTypes;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPCommunication {
    private final static int SIZE = 256;
    private final static String ERROR_RECEIVE = "ERROR";
    private int serverPort;
    private boolean foundPort = false;
    private ServerSocket sS;
    private Socket s;
    private InputStream iS;
    private OutputStream oS;
    
    public TCPCommunication(int serverPort){
        this.serverPort = serverPort;
    }
    
    public int getServerPort(){
        return serverPort;
    }
    
    public void initializeTCP() throws IOException{
        while(!foundPort){
            try{
                sS = new ServerSocket(serverPort);
                foundPort = true;
            }catch(BindException ex){
                setServerPort(serverPort+1);
            }
        }
        
    }
    
    public void closeTCP() throws IOException{
        iS.close();
        oS.close();
        s.close();
    }
    
    public void acceptConnection() throws IOException{
        s = sS.accept();
        iS = s.getInputStream();
        oS = s.getOutputStream();
    }
    
    public void sendTCP(String msg) throws IOException{
        oS.write(msg.getBytes());
        oS.flush();
    }
    
    public String receiveTCP() throws IOException{
        byte[] bufStr = new byte[SIZE];
        int nBytes = iS.read(bufStr);
        String tempStr = new String(bufStr, 0, nBytes);
        return tempStr;
    }
    
    private void setServerPort(int serverPort){
        this.serverPort = serverPort;
    }
    
    //We probably have to develop a method that can send a serializable object
    //through a TCP connection.
}
