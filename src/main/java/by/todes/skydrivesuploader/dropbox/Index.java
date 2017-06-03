///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package by.todes.skydrivesuploader.dropbox;
//
///**
// *
// * @author l_miron
// */
//public class Index {
//
//    public static void main(String[] args) {
////        Folders folders = new Folders();
////        System.out.println("Begin");
////        List<File> listOfFiles = folders.getListOfFiles("D:\\Dropbox\\todes\\KMZtoGPXConverter");
////        System.out.println("go to dropbox");
////        List<FileMetadata> dbFiles = DBold.instance().getFileList("/todes/KMZtoGPXConverter");
////        System.out.println("let's compare it");
////        System.out.println("list listoffiles: " + listOfFiles.size());
////        System.out.println("list dbFiles: " + dbFiles.size());
////        List<File> copyListOfFiles=new ArrayList<>(listOfFiles);
////        List<FileMetadata> copyDbFiles=new ArrayList<>(dbFiles);
////        long l=System.currentTimeMillis();
////        for (File file : copyListOfFiles) {
////            String systemPath  = DBUtils.resolvePath(file.toPath());
////            try {
////                for (FileMetadata dbFile : copyDbFiles) {
////                    try {
////                        if ((systemPath.equalsIgnoreCase(dbFile.getPathLower()))
////                                ) {
////                            if (dbFile.getSize()!=file.length()){
//////                                System.out.println("file are differ "+file.getName() );
////                                if (Settings.downloadPriority==PriorityDrirection.SKYDRIVE){
////                                    listOfFiles.remove(file);
////                                }else if (Settings.downloadPriority==PriorityDrirection.DISK){
////                                    dbFiles.remove(dbFile);
////                                }else if (Settings.downloadPriority==PriorityDrirection.NOTHING){
////                                    listOfFiles.remove(file);
////                                    dbFiles.remove(dbFile);
////                                    System.out.println("problem with file:"+file.getName());
////                                }
////                            }else{
////                                listOfFiles.remove(file);
////                                dbFiles.remove(dbFile);
////                            }
////
////                        }
////                    } catch (Exception e) {
////                        Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, e);
////                    }
////                }
////            } catch (Exception e) {
////                Logger.getLogger(DBold.class.getName()).log(Level.SEVERE, null, e);
////            }
////        }
////        System.out.println("time:"+(System.currentTimeMillis()-l));
////
////        for (File file : listOfFiles) {
//////            System.out.println(" uplopad:"+file.toPath());
////            try {
////                DBold.instance().upload(file.toPath());
////            } catch (Exception ex) {
////                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
////            }
////        }
////        for (FileMetadata dbFile : dbFiles) {
////            //need refactoring
//////            DBold.instance().download(dbFile.getPathLower(),
//////                    Paths.get(DropboxSettings.dropboxFolder.resolve(dbFile.getName()).toString()));
////        }
////        System.out.println("files to upload: " + listOfFiles.size());
////        System.out.println("files to download: " + dbFiles.size());
//    }
//
//}
