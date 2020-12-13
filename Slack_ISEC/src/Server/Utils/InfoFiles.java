package Server.Utils;

public class InfoFiles {
    private String fileName;
    private int fileId;
    private int serverId;
    private long fileSize;

    public InfoFiles(String fileName, int fileId, long fileSize, int serverId) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.serverId = serverId;
    }
}