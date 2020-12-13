package Server.Utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReceiveFiles implements Serializable {
    private static int MAX_DATA = 10000;
    private FileOutputStream foS;
    private String fileName;
    private final String destination;

    private final int serverIdFile;   //Server where the file is from.
    private int serverId;       //the actual server ID.


    public ReceiveFiles(String fileName, int serverId, int serverIdFile, String destination) {
        this.fileName = fileName;
        this.serverIdFile = serverIdFile;
        this.serverId = serverId;
        this.destination = destination;
    }


    //Getters
    public String getFileName() {
        return fileName;
    }

    public int getServerIdFile() {
        return serverIdFile;
    }

    public int getServerId() {
        return serverId;
    }


    public void addChunk(Chunk rCk){
        try {
            foS.write(rCk.getChunk());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildFile(){
        try {
            String directoryName = "";
            if (destination.equals("profile"))
                directoryName += "C:\\" + serverIdFile + "\\profileImages";
            else
                directoryName += "C:\\" + serverIdFile;
            File localdirectory = new File(directoryName);
            System.out.println(localdirectory.toPath());
            if (!localdirectory.exists()) {
                Files.createDirectories(localdirectory.toPath());
            }
            File localFilePath = new File(directoryName + File.separator + fileName);
            if (localFilePath.exists()) {
                System.err.println("[ThreadDownloadFromClient] -> O Ficheiro já existe.");
                StringTokenizer nameTokenizer = new StringTokenizer(fileName, ".");
                String name = nameTokenizer.nextToken();
                String extension = nameTokenizer.nextToken();
                int i = 0;
                while (localFilePath.exists()) {
                    String aux = name + "(" + ++i + ")";
                    localFilePath = new File(directoryName + File.separator + aux + "." + extension);
                }
            }
            System.out.println(localFilePath.getCanonicalPath());
            foS = new FileOutputStream(localFilePath.getCanonicalPath());
        }catch(IOException ex){
            ex.getStackTrace();
        }
    }
}
