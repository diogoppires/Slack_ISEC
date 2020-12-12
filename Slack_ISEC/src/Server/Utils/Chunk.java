package Server.Utils;

public class Chunk {
    private String fileName;
    private int serverId;
    private int pos;
    private byte[] chunk;

    public Chunk(String fileName, int serverId, int pos, byte[] chunk) {
        this.fileName = fileName;
        this.serverId = serverId;
        this.pos = pos;
        this.chunk = chunk;
    }


}
