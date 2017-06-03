/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.todes.skydrivesuploader.dropbox;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author l_miron
 */
public class DBUtils {
    //refactor
    static String resolvePath(DBSettings dbSettings, Path from) {
        Path subPath = from.subpath(Paths.get(dbSettings.getPcFolder()).getNameCount(),
                from.getNameCount());
        System.out.println(subPath.toString());
        return "/"+subPath.toString().replace("\\", "/");
    }
}
