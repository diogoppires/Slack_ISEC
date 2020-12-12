package Server.Utils;

import java.io.FileOutputStream;

public class ReceiveFiles {
    private String fileName;
    private FileOutputStream oS;
    private int serverId;
    private int lastPos;

    public ReceiveFiles(String fileName, int serverId) {
        this.fileName = fileName;
        this.serverId = serverId;
//        oS = new FileOutputStream("C://temp//teste.png");
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileOutputStream getoS() {
        return oS;
    }

    public void setoS(FileOutputStream oS) {
        this.oS = oS;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }
}
