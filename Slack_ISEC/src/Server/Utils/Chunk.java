package Server.Utils;

import java.io.Serializable;

public class Chunk implements Serializable {
    private String fileName;
    private String destination;
    private int serverId;
    private int pos;
    private byte[] chunk;

    private boolean end;



    public Chunk(String fileName, String destination, int serverId, int pos, byte[] chunk, boolean end) {
        this.fileName = fileName;
        this.destination = destination;
        this.serverId = serverId;
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
}
