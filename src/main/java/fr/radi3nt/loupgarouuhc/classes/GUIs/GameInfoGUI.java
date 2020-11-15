package fr.radi3nt.loupgarouuhc.classes.GUIs;

import fr.radi3nt.uhc.api.lang.lang.Languages;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.loupgarouuhc.roles.Role;
import fr.radi3nt.loupgarouuhc.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.roles.RoleType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.lang.reflect.Constructor;
import java.util.*;

public class GameInfoGUI {

    public ItemStack createBook(Languages languages, RoleType roleType) {
        //create the book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        ArrayList<RoleIdentity> rolesSorts = new ArrayList<>();
        HashMap<String, Integer> roles = new HashMap<>();


        try {
            for (Map.Entry<RoleIdentity, Constructor<? extends Role>> role : Role.getRoleLinkByStringKey().entrySet())
                if (role.getKey().getRoleType()== roleType) {
                    rolesSorts.add(role.getKey());
                }
        } catch (Exception err) {
            Logger.getGeneralLogger().logInConsole("§4§lUne erreur est survenue lors de la sauvegarde des roles");
            Logger.getGeneralLogger().log(err);
        }

        ArrayList<String> nameOfRoles = new ArrayList<>();
        for (RoleIdentity roleSort : rolesSorts) {
            nameOfRoles.add(roleSort.getName(languages));
        }
        Collections.sort(nameOfRoles);

        Map<String, Integer> rolesNames = new TreeMap<>();
        int i=(rolesSorts.size()/14)+2;
        for (String nameOfRole : nameOfRoles) {
            rolesNames.put(nameOfRole, i);
            i++;
        }


//create a page
        ArrayList<BaseComponent[]> pages = new ArrayList<>();
        int numberOfLines = 1;
        ComponentBuilder component = new ComponentBuilder(ChatColor.GREEN + "" + ChatColor.BOLD +  "Role " + roleType.name() +"\n\n");
        for (Map.Entry<String, Integer> role : rolesNames.entrySet()) {
            if (numberOfLines==13) {
                pages.add(component.create());
                component=new ComponentBuilder(ChatColor.GOLD + role.getKey() + "\n").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "Plus d'infos").create())).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, String.valueOf(role.getValue())));
                numberOfLines=1;
            } else {
                component.append(ChatColor.GOLD + role.getKey() + "\n").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "Plus d'infos").create())).event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, String.valueOf(role.getValue())));
                numberOfLines++;
            }
        }

        pages.add(component.create());
        for (Map.Entry<String, Integer> stringIntegerEntry : rolesNames.entrySet()) {
            component=new ComponentBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + stringIntegerEntry.getKey());
            component.append("\n");
            component.append("\n");
            component.append(ChatColor.AQUA + "" + ChatColor.BOLD + "Description:\n");
            int number = 0;
            for (int i1 = 0; i1 < rolesSorts.size(); i1++) {
                RoleIdentity rolesSort = rolesSorts.get(i1);
                if (rolesSort.getName(languages).equals(stringIntegerEntry.getKey())) {
                    number=i1;
                }
            }
            component.color(net.md_5.bungee.api.ChatColor.BLUE);
            component.append(rolesSorts.get(number).getRoleDescription(languages));
            pages.add(component.create());
        }

        for (BaseComponent[] page : pages) {
            bookMeta.spigot().addPage(page);
        }

//set the title and author of this book
        bookMeta.setTitle(roleType.name() + " book");
        bookMeta.setAuthor("Radi3nt");

//update the ItemStack with this new meta
        book.setItemMeta(bookMeta);
        return book;
    }

}
