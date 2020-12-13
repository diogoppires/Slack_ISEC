package Server.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReceiveFiles {
    private static int MAX_DATA = 5000;
    private FileOutputStream foS;
    private String fileName;
    private final String destination;

    private final int serverIdFile;   //Server where the file is from.
    private int serverId;       //the actual server ID.

    private String directory;
    private ArrayList<Chunk> receivedChunks;

    public ReceiveFiles(String fileName, int serverId, int serverIdFile, String destination) {
        this.fileName = fileName;
        this.serverIdFile = serverIdFile;
        this.serverId = serverId;
        this.destination = destination;
    }

    public void createDirectory(){
        StringBuilder directoryName = new StringBuilder();
        if (destination.equals("profile")){
            directoryName.append("C:\\").append(serverIdFile).append("\\profileImages");
        }
        else{
            directoryName.append("C:\\").append(serverIdFile);
        }
        File localDirectory = new File(directoryName.toString());
        System.out.println(localDirectory.toPath());
        if (!localDirectory.exists()) {
            try {
                Files.createDirectories(localDirectory.toPath());
            } catch (IOException e) {
                System.out.println("[ServerL - New File]Error creating directory.");
            }
        }
        directory = directoryName.toString();
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
        if(rCk.getChunk().length == MAX_DATA){          //If the length is equal to MAX_DATA, it means it didn't end yet.
            receivedChunks.add(rCk);
        }else if(rCk.getChunk().length < MAX_DATA){     //But if the length is smaller than MAX_DATA, it means it is over.
            receivedChunks.add(rCk);
            File localFilePath = getFilePath();
            try {
                System.out.println(localFilePath.getCanonicalPath());
                foS = new FileOutputStream(localFilePath.getCanonicalPath());
                for(Chunk c : receivedChunks){
                    foS.write(c.getChunk());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getFilePath() {
        File localFilePath = new File(directory + File.separator + fileName);
        if (localFilePath.exists()) {
            System.err.println("[SpreadFile] -> O Ficheiro já existe.");
            StringTokenizer nameTokenizer = new StringTokenizer(fileName, ".");
            String name = nameTokenizer.nextToken();
            String extension = nameTokenizer.nextToken();
            int i = 0;
            while (localFilePath.exists()) {
                String aux = name + "(" + ++i + ")";
                localFilePath = new File(directory + File.separator + aux + "." + extension);
            }
        }
        return localFilePath;
    }
}
