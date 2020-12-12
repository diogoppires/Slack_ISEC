package Server.serverCommunication.CommsTypes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPCommunication {

    private final static int SIZE = 256;
    private final static String ERROR_RECEIVE = "ERROR";
    private int serverPort;
    private boolean foundPort = false;
    private ServerSocket sS;
    private Socket s;
    private InputStream iS;
    private OutputStream oS;

    public TCPCommunication(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        //return serverPort;
        return sS.getLocalPort();
    }

    public void initializeTCP() throws IOException {
        while (!foundPort) {
            try {
                sS = new ServerSocket(serverPort);
                foundPort = true;
            } catch (BindException ex) {
                setServerPort(serverPort + 1);
            }
        }

    }

    public void closeTCP() throws IOException {
        if (iS != null && oS != null && s != null) {
            iS.close();
            oS.close();
            s.close();
        }
        sS.close();
    }

    public void acceptConnection() throws IOException {
        s = sS.accept();
        iS = s.getInputStream();
        oS = s.getOutputStream();
    }

    public void sendTCP(String msg) throws IOException {
        oS.write(msg.getBytes());
        oS.flush();
    }

    public void sendTCP(byte[] msg) throws IOException {
        oS.write(msg);
        oS.flush();
    }

    public String receiveTCP() throws IOException {
        byte[] bufStr = new byte[SIZE];
        int nBytes = iS.read(bufStr);
        String tempStr = new String(bufStr, 0, nBytes);
        return tempStr;
    }

    public void receiveTcpFile(FileOutputStream fo, int MAX_DATA) {
        byte[] buffStr = new byte[MAX_DATA];
        try {
            while (iS.read(buffStr) != -1) {
                fo.write(buffStr);
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    //We probably have to develop a method that can send a serializable object
    //through a TCP connection.
    public Socket getSocketClient() {
        return s;
    }

    public void sendFile(FileInputStream fileIS, int MAX_DATA) {
        try {
            byte[] buffStr = new byte[MAX_DATA];
            while (fileIS.available() != 0) {
                buffStr = fileIS.readNBytes(MAX_DATA);
                sendTCP(buffStr);

            }
        } catch (IOException ex) {
            Logger.getLogger(TCPCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
