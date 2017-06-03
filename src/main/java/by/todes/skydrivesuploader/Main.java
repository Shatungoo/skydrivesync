/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader;

import by.todes.skydrivesuploader.dropbox.DBSettings;
import by.todes.skydrivesuploader.dropbox.DropboxListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 *
 * @author l_miron
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
                DBSettings dBSettings=new DBSettings();
        dBSettings.setDbFolder("");
        dBSettings.setPcFolder("D:\\Dropbox\\");
        dBSettings.setPriority(PriorityDrirection.DISK);
        // Change this to match the environment you want to watch.
        Settings.instance();

        FileAlterationObserver fao = new FileAlterationObserver(dBSettings.getPcFolder());
        fao.addListener(new DropboxListener(dBSettings));

        final FileAlterationMonitor monitor = new FileAlterationMonitor();
        monitor.addObserver(fao);
        monitor.start();
        System.out.println("Starting monitor. CTRL+C to stop.");

//        DBold.instance();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Stopping monitor.");
                monitor.stop();
            } catch (Exception ignored) {
            }
        }));
    }
    
    public void compareList(){
        
    }

}
