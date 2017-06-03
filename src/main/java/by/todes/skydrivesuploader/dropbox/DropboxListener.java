/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader.dropbox;

import com.dropbox.core.DbxException;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shatun
 */
public class DropboxListener implements FileAlterationListener {

    final Logger LOGGER =Logger.getLogger(DropboxListener.class.getName());

    private DBSettings dBSettings;

    public DropboxListener(DBSettings dBSettings){
        this.dBSettings=dBSettings;
    }

    @Override
    public void onStart(FileAlterationObserver fao) {
//        String direction="";
//        switch (dBSettings.getPriority()){
//            case DISK:
//                direction="<--";
//                break;
//            case SKYDRIVE:
//                direction="-->";
//                break;
//            case NOTHING:
//                break;
//        }
//        LOGGER.info("monitor "+dBSettings.getPcFolder()+
//                direction+ dBSettings.getDbFolder()+" started");
    }

    @Override
    public void onDirectoryCreate(File file) {
        if (file.canRead()){
            DBold.instance().createFolder(DBUtils.resolvePath(dBSettings,file.toPath()));
        }
    }

    @Override
    public void onDirectoryChange(File file) {
        
    }

    @Override
    public void onDirectoryDelete(File file) {
        if (file.canRead()){
            DBold.instance().delete(DBUtils.resolvePath(dBSettings,file.toPath()));
        }
    }

@Override
    public void onFileCreate(final File file) {
        if (file.canRead()){
            try {
                DBold.instance().upload(file.getAbsolutePath(),
                        DBUtils.resolvePath(dBSettings,file.toPath()));
            } catch (DbxException ex) {
                Logger.getLogger(DropboxListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void onFileChange(final File file) {
        LOGGER.info(file.getAbsoluteFile() + " was modified.");
        if (file.canRead()) {
            try {
                DBold.instance().upload(file.getAbsolutePath(),
                        DBUtils.resolvePath(dBSettings,file.toPath()));
            } catch (DbxException ex) {
                Logger.getLogger(DropboxListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void onFileDelete(final File file) {
        DBold.instance().delete(DBUtils.resolvePath(dBSettings,file.toPath()));
    }

    @Override
    public void onStop(FileAlterationObserver fao) {
        
    }
    
}
