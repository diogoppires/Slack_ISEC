package Server.Utils;

public class Chunk {
    private static final int SIZE = 5000;
    private String fileName;
    private String destination;
    private int serverId;
    private int pos;
    private byte[] chunk;

    public Chunk(String fileName, String destination, int serverId, int pos, byte[] chunk) {
        this.fileName = fileName;
        this.destination = destination;
        this.serverId = serverId;
        this.pos = pos;
        this.chunk = chunk;
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
