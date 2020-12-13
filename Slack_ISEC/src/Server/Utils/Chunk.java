package Server.Utils;

import java.io.Serializable;

public class Chunk implements Serializable {
    private String fileName;
    private String userSender;
    private String destination;

    private int serverId;
    private int fileId;
    private int pos;
    private byte[] chunk;

    private boolean end;

    public Chunk(String fileName,
                 String userSender,
                 String destination,
                 int fileId,
                 int serverId,
                 int pos,
                 byte[] chunk,
                 boolean end) {
        this.fileName = fileName;
        this.userSender = userSender;
        this.destination = destination;
        this.serverId = serverId;
        this.fileId = fileId;
        this.pos = pos;
        this.chunk = chunk;
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDestination(){
        return destination;
    }

    public int getServerId() {
        return serverId;
    }

    public int getPos() {
        return pos;
    }

    public byte[] getChunk() {
        return chunk;
    }

    public String getSender() {
        return userSender;
    }

    public int getFileId() {
        return fileId;
    }
}
