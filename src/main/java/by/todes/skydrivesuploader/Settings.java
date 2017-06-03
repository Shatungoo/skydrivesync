/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 *
 * @author l_miron
 */


public class Settings implements Serializable {
    private static Settings INSTANCE;

    static final long serialVersionUID = -7588980448693010399L;

    public String ACCESS_TOKEN;
    public boolean useProxy=false;
    String host;
    int port;
    public PriorityDrirection downloadPriority=PriorityDrirection.DISK;

    private Settings() {

    }

    public Proxy getProxy(){
        return new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(host,port));
    }


    void save(){
        try(FileOutputStream fos = new FileOutputStream("settins.st");
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void load(){
        ObjectInputStream oin;
        try (FileInputStream fis = new FileInputStream("settins.st")) {
            oin = new ObjectInputStream(fis);
            INSTANCE = (Settings) oin.readObject();
            System.out.println(INSTANCE.ACCESS_TOKEN);

        } catch (ClassNotFoundException|IOException e) {
            e.printStackTrace();
        }

    }

    public static Settings instance() {
        if (INSTANCE == null) {
//            INSTANCE = new Settings();
//            INSTANCE.save();
            load();
//
        }
        return INSTANCE;
    }
    
}
