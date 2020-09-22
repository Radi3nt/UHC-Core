package fr.radi3nt.loupgarouuhc.listeners;

import net.minecraft.server.v1_12_R1.ItemPickaxe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

public class OnCraftEvent {

    @EventHandler
    public void OnCraftEvent (CraftItemEvent e) {
        if (CraftItemStack.asNMSCopy(e.getCurrentItem()).getItem() instanceof ItemPickaxe) {
            //e.getCurrentItem().addEnchantment(Enchantment.DIG_SPEED, 1);
        }
    }

}
