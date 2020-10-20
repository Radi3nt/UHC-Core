package fr.radi3nt.loupgarouuhc.classes.lang.translations;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.Message;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.PlaceHolder;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.RoleAspect;
import fr.radi3nt.loupgarouuhc.classes.message.Logger;
import fr.radi3nt.loupgarouuhc.utilis.Config;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Reader {


    public void loadLanguage(File file) {
        Config config = Config.createConfig(file.getParent(), file.getName());
        String name;
        String id;
        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<RoleAspect> roleAspects = new ArrayList<>();
        ArrayList<PlaceHolder> placeHolders = new ArrayList<>();

        name = config.getConfiguration().getString("Language.name");
        id = config.getConfiguration().getString("Language.id");

        if (!id.equals(Languages.DEFAULTID)) {
            for (String roleid : config.getConfiguration().getConfigurationSection("Roles").getKeys(false)) {
                String roleName = config.getConfiguration().getString("Roles." + roleid + ".name");
                String roleShortDesc = config.getConfiguration().getString("Roles." + roleid + ".short desc");
                String roleDesc = config.getConfiguration().getString("Roles." + roleid + ".desc");

                RoleAspect roleAspect = new RoleAspect(roleid, roleName, roleShortDesc, roleDesc);
                roleAspects.add(roleAspect);
            }

            for (String messageId : config.getConfiguration().getConfigurationSection("Messages").getKeys(false)) {
                Message message = new Message(messageId, config.getConfiguration().getString("Messages." + messageId));
                messages.add(message);
            }

            for (String messageId : config.getConfiguration().getConfigurationSection("PlaceHolders").getKeys(false)) {
                PlaceHolder placeHolder = new PlaceHolder(messageId, config.getConfiguration().getString("PlaceHolders." + messageId));
                placeHolders.add(placeHolder);
            }

            new Languages(name, id, roleAspects, messages, placeHolders);
            Logger.getLogger().logWhenDebug(ChatColor.DARK_RED + "[Language] Loading new language: " + file.getName(), LoupGarouUHC.console);
        }
    }

    public boolean loadAllLanguage() {
    /*
        try {

     */


        URI uri = null;
        try {
            uri = LoupGarouUHC.class.getResource("/langs").toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Map<String, String> env = new HashMap<>();
        try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
            //Path path = zipfs.getPath("/images/icons16");
            for (Path path : zipfs.getRootDirectories()) {
                for (Path path1 : Files.list(path.resolve("/langs")).collect(Collectors.toList())) {
                    try {
                        File directoryPath = new File("plugins/LoupGarouUHC/langs/" + path1.getFileName().toString());
                        Files.copy(path1, directoryPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

                /*
                for (File file : filesList1) {
                    System.out.println("1");
                    boolean found = false;
                    if (filesList!=null) {
                        for (File file1 : filesList) {
                            if (file1.getName().equals(file.getName()))
                                found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("Creating new file");
                        File newFile = new File(directoryPath.getAbsolutePath() + file.getName());
                        if (!newFile.exists()) {
                            try {
                                newFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            System.out.println("Copiying");
                            Files.copy(file.toPath(), directoryPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            */

        File directoryPath = new File("plugins/LoupGarouUHC/langs");
        File[] filesList = directoryPath.listFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (!file.isHidden() && file.getName().contains(".yml")) {
                    loadLanguage(file);
                } else {
                    Logger.getLogger().logWhenDebug(ChatColor.DARK_RED + "[Language] Skipping loading of: " + file.getName(), LoupGarouUHC.console);
                }
            }
        }
        return true;
            /*
        } catch (Exception e) {
            return false;
        }

             */
    }
}
