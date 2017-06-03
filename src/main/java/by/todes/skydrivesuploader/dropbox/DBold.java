/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader.dropbox;

import by.todes.skydrivesuploader.Settings;
import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.http.StandardHttpRequestor;
import com.dropbox.core.http.StandardHttpRequestor.Config;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author l_miron
 */
public final class DBold {

    DbxClientV2 client;

    static DBold INSTANCE;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
//        Dropbox dropbox = new Dropbox();
        DBold.instance().longpoll();//.doIt();
//        File file=new File("D:\\dropbox\\1.txt");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//        Dropbox.instance().upload(Paths.get("D:\\dropbox\\1.txt"));
    }

    private DBold() {
        try {
            authorize();
        } catch (DbxException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DBold instance() {
        if (INSTANCE == null) {
            INSTANCE = new DBold();
        }
        return INSTANCE;
    }

    public List<FileMetadata> getFileList(String path) {
        
        List<FileMetadata> fileList = new ArrayList<>();
        int i=0;
        try {
            ListFolderResult resultList = client.files().listFolderBuilder(path)
                    .withRecursive(true)
                    .start();
            
            while(true) {
                i++;
                for (Metadata metadata : resultList.getEntries()) {
                    if (metadata instanceof FileMetadata){
                        FileMetadata fileMetadata = (FileMetadata) metadata;
//                        System.out.println("fileList size:"+fileList.size());
                        try{
                            fileList.add(fileMetadata);
                        }catch(Exception e){
                            break;
                        }
                    }
                }
                if (!resultList.getHasMore()) {
                    break;
                }
                System.out.println("i:"+i);
                resultList=client.files().listFolderContinue(resultList.getCursor());
            }
        } catch (DbxException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("file:"+fileList.size()
                        +" i:"+i);
        return fileList;
    }



    synchronized void authorize() throws DbxException {
        Config.Builder conf = StandardHttpRequestor.Config.builder();
        if (Settings.instance().useProxy) {
            conf.withProxy(Settings.instance().getProxy());
        }
        HttpRequestor req = new StandardHttpRequestor(conf.build());
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US", req);
        client = new DbxClientV2(config, Settings.instance().ACCESS_TOKEN);
        System.out.println("Authorization success");

    }

    public void createFolder(String to) {
        try {
            FolderMetadata metadata = client.files().createFolder(to);
        } catch (DbxException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void upload(String from,String to) throws DbxException {

        System.out.println("upload:" + from.toString()
                + " to:" + "/" + from);
        ListFolderResult result = client.files().listFolder("");
        try (InputStream in = new FileInputStream(from)) {
            FileMetadata metadata = client.files().uploadBuilder(to)
//                    .withClientModified(new Date(from.toFile().lastModified()))
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(String to) {
        System.out.println("delete:" + to);
        try {
            Metadata metadata = client.files().delete(to);
        } catch (Exception ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void download(String from,Path to) {
        System.out.println("download:" + from
                 +" to"+to.toString());
        try {
            DbxDownloader<FileMetadata> metadata = client.files().download(from);
            FileOutputStream fos=new FileOutputStream(to.toFile());
            
             metadata.download(fos);
        } catch (DbxException | IOException ex) {
            Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public  void longpoll() throws DbxException  {
        long longpollTimeoutSecs = TimeUnit.MINUTES.toSeconds(2);

        // need 2 Dropbox clients for making calls:
        //
        // (1) One for longpoll requests, with its read timeout set longer than our polling timeout
        // (2) One for all other requests, with its read timeout set to the default, shorter timeout
        //
        String cursor = client.files()
            .listFolderGetLatestCursorBuilder("")
            .withIncludeDeleted(true)
            .withIncludeMediaInfo(false)
            .withRecursive(true)
            .start().getCursor();
        System.out.println("Longpolling for changes... press CTRL-C to exit.");
        while (true) {
            try {
                // will block for longpollTimeoutSecs or until a change is made in the folder
                ListFolderLongpollResult result = client.files()
                        .listFolderLongpoll(cursor, longpollTimeoutSecs);
                // we have changes, list them
                if (result.getChanges()) {
                    cursor = printChanges(client, cursor);
                }
                // we were asked to back off from our polling, wait the requested amount of seconds
                // before issuing another longpoll request.
                Long backoff = result.getBackoff();
                if (backoff != null) {
                    try {
                        System.out.printf("backing off for %d secs...\n", backoff.longValue());
                        Thread.sleep(TimeUnit.SECONDS.toMillis(backoff));
                    } catch (InterruptedException ex) {
                        System.exit(0);
                    }
                }
            } catch (DbxException ex) {
                Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static String printChanges(DbxClientV2 client, String cursor)
        throws DbxApiException, DbxException {

        while (true) {
            ListFolderResult result = client.files()
                .listFolderContinue(cursor);
            for (Metadata metadata : result.getEntries()) {
                String type;
                String details;
                if (metadata instanceof FileMetadata) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    type = "file";
                    details = "(rev=" + fileMetadata.getRev() + ")";
                } else if (metadata instanceof FolderMetadata) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    type = "folder";
                    details = folderMetadata.getSharingInfo() != null ? "(shared)" : "";
                } else if (metadata instanceof DeletedMetadata) {
                    type = "deleted";
                    details = "";
                } else {
                    throw new IllegalStateException("Unrecognized metadata type: " + metadata.getClass());
                }

                System.out.printf("\t%10s %24s \"%s\"\n", type, details, metadata.getPathLower());
            }
            // update cursor to fetch remaining results
            cursor = result.getCursor();

            if (!result.getHasMore()) {
                break;
            }
        }

        return cursor;
    }

}
