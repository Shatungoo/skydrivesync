///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package by.todes.skydrivesuploader;
//
//
//import java.nio.file.*;
//import java.nio.file.attribute.*;
//import java.io.*;
//import java.util.*;
//
///**
// *
// * @author l_miron
// */
//public class FolderListener {
// 
//    private final WatchService watcher;
//    private final Map<WatchKey,Path> keys;
//    private final boolean recursive;
//    private boolean trace = false;
//    private final List<IUpload> uploaders=new ArrayList<>();
// 
//    @SuppressWarnings("unchecked")
//    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
//        return (WatchEvent<T>)event;
//    }
// 
//    /**
//     * Register the given directory with the WatchService
//     */
//    private void register(Path dir) throws IOException {
//        WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
//                StandardWatchEventKinds.ENTRY_DELETE, 
//                StandardWatchEventKinds.ENTRY_MODIFY);
//        if (trace) {
//            Path prev = keys.get(key);
//            if (prev == null) {
//                System.out.format("register: %s\n", dir);
//            } else {
//                if (!dir.equals(prev)) {
//                    System.out.format("update: %s -> %s\n", prev, dir);
//                }
//            }
//        }
//        keys.put(key, dir);
//    }
// 
//    /**
//     * Register the given directory, and all its sub-directories, with the
//     * WatchService.
//     */
//    private void registerAll(final Path start) throws IOException {
//        // register directory and sub-directories
//        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
//                                                throws IOException{
//                register(dir);
//                return FileVisitResult.CONTINUE;
//            }
//        });
//    }
// 
//    /**
//     * Creates a WatchService and registers the given directory
//     */
//    FolderListener(Path dir, boolean recursive,IUpload... upload) throws IOException {
//        this.watcher = FileSystems.getDefault().newWatchService();
//        this.keys = new HashMap<WatchKey,Path>();
//        this.recursive = recursive;
//        List qqq=(List<IUpload>)Arrays.asList(upload);
//        this.uploaders.addAll(qqq );
//        if (recursive) {
//            System.out.format("Scanning %s ...\n", dir);
//            registerAll(dir);
//            System.out.println("Done.");
//        } else {
//            register(dir);
//        }
// 
//        // enable trace after initial registration
//        this.trace = true;
//    }
// 
//    /**
//     * Process all events for keys queued to the watcher
//     */
//    void processEvents() {
//        for (;;) {
// 
//            // wait for key to be signalled
//            WatchKey key;
//            try {
//                key = watcher.take();
//            } catch (InterruptedException x) {
//                return;
//            }
// 
//            Path dir = keys.get(key);
//            if (dir == null) {
//                System.err.println("WatchKey not recognized!!");
//                continue;
//            }
// 
//            for (WatchEvent<?> event: key.pollEvents()) {
//                WatchEvent.Kind kind = event.kind();
// 
//                // TBD - provide example of how OVERFLOW event is handled
//                if (kind == StandardWatchEventKinds.OVERFLOW) {
//                    continue;
//                }
// 
//                // Context for directory entry event is the file name of entry
//                WatchEvent<Path> ev = cast(event);
//                Path name = ev.context();
//                Path child = dir.resolve(name);
// 
//                // print out event
////                System.out.format("%s: %s\n", event.kind().name(), child);
// 
//                // if directory is created, and watching recursively, then
//                // register it and its sub-directories
//                if (recursive && (kind == StandardWatchEventKinds.ENTRY_CREATE)) {
//                    try {
//                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
//                            registerAll(child);
//                        }
//                        for(IUpload upload:uploaders){
//                            upload.onCreate(child);
//                        }
//                    } catch (IOException x) {
//                        // ignore to keep sample readbale
//                    }
//                }
//                if (kind == StandardWatchEventKinds.ENTRY_DELETE){
//                    for(IUpload upload:uploaders){
//                        upload.onDelete(child);
//                    }
//                }
//                if (kind == StandardWatchEventKinds.ENTRY_MODIFY){
//                    for(IUpload upload:uploaders){
//                        upload.onModify(child);
//                    }
//                }
//            }
// 
//            // reset key and remove from set if directory no longer accessible
//            boolean valid = key.reset();
//            if (!valid) {
//                keys.remove(key);
// 
//                // all directories are inaccessible
//                if (keys.isEmpty()) {
//                    break;
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        boolean recursive = true;
//        // register directory and process its events
////        Path dir = Paths.get("D://dropbox");
////        new FolderListener(dir, true,new IUpload(){
////            @Override
////            public void upload(Path from) {
//////                System.out.println("upload: "+from);
////            }
////
////            @Override
////            public void onCreate(Path from) {
//////                System.out.println("onCreate: "+from);
////                upload(from);
////            }
////
////            @Override
////            public void onDelete(Path from) {
//////                System.out.println("onDelete: "+from);
////                upload(from);
////            }
////
////            @Override
////            public void onModify(Path from) {
//////                System.out.println("onModify: "+from);
////                upload(from);
////            }
////        }
////        ).processEvents();
//    }
//}