package Server.Utils;

public class InfoFiles {
    private String fileName;
    private int fileId;
    private int fileSize;
    private int serverId;

    public InfoFiles(String fileName, int fileId, int fileSize, int serverId) {
        this.fileName = fileName;
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.serverId = serverId;
    }
}
