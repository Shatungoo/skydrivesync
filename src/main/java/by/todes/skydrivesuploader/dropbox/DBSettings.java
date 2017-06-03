/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader.dropbox;

import by.todes.skydrivesuploader.PriorityDrirection;

/**
 *
 * @author l_miron
 */
public class DBSettings {
    private String dbFolder;
    private String pcFolder;
    private PriorityDrirection priority;

    /**
     * @return the dbFolder
     */
    public String getDbFolder() {
        return dbFolder;
    }

    /**
     * @param dbFolder the dbFolder to set
     */
    public void setDbFolder(String dbFolder) {
        this.dbFolder = dbFolder;
    }

    /**
     * @return the pcFolder
     */
    public String getPcFolder() {
        return pcFolder;
    }

    /**
     * @param pcFolder the pcFolder to set
     */
    public void setPcFolder(String pcFolder) {
        this.pcFolder = pcFolder;
    }

    /**
     * @return the priority
     */
    public PriorityDrirection getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(PriorityDrirection priority) {
        this.priority = priority;
    }
}
