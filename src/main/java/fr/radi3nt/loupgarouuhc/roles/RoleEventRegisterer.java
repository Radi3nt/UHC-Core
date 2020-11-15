package fr.radi3nt.loupgarouuhc.roles;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class RoleEventRegisterer {

    private static Map<Role, RoleEventRegisterer> registererMap = new HashMap<>();

    private Plugin plugin;
    private Role role;

    private RoleEventRegisterer(Plugin plugin, Role role) {
        this.role = role;
        this.plugin=plugin;
    }

    public static RoleEventRegisterer theRegisterer(Plugin plugin, Role role) {
        return registererMap.getOrDefault(role, new RoleEventRegisterer(plugin, role));
    }

    public void register() {
        if (!registererMap.containsKey(role)) {
            Bukkit.getPluginManager().registerEvents(role, plugin);
            registererMap.put(role, this);
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(role);
        registererMap.remove(role);
    }

    public static void unregisterAll() {
        for (Map.Entry<Role, RoleEventRegisterer> roleRoleEventRegistererEntry : registererMap.entrySet()) {
            roleRoleEventRegistererEntry.getValue().unregister();
        }
    }
}
