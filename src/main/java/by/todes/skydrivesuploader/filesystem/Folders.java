/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader.filesystem;

import by.todes.skydrivesuploader.PriorityDrirection;
import by.todes.skydrivesuploader.dropbox.DBSettings;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author l_miron
 */
public class Folders {
    private Path pathFromG;
    
    public static void main(String[] args){
        Folders folders=new Folders();
        folders.getListOfFiles("D:\\Dropbox\\");

        
    }
    
    public List<File> getListOfFiles(String path){
        return getListOfFiles(Paths.get(path));
    }
    
    public List<File> getListOfFiles(Path pathFrom) {
        List<File> files=new ArrayList<>();
        File folder = pathFrom.toFile();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {                
                getListOfFiles(Paths.get(fileEntry.getAbsolutePath()));
            } else {
                try {
                    Path pathFr=Paths.get(fileEntry.getAbsolutePath());
//                    Path subPath=pathFrom.subpath(pathFromG.getNameCount()-1, 
//                                                            pathFrom.getNameCount());
//                    Path pathToCopy=pathTo.resolve(subPath);
//                    pathToCopy.toFile().mkdirs();
//                    System.out.println(pathFr);
                    files.add(pathFr.toFile());
                } catch (Exception ex) {
                    Logger.getLogger(Folders.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return files;
    }
}
