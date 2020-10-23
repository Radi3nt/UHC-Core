// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.loupgarouuhc.event.listeners;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class SmallFeaturesListener implements Listener {

    @EventHandler
    private void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (LGPlayer.thePlayer(event.getPlayer()).isInGame())
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                event.setCancelled(true);
            } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void onDrinkMilk(final PlayerInteractEvent event) {
        final Action a = event.getAction();
        if (LGPlayer.thePlayer(event.getPlayer()).isInGame())
            if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.MILK_BUCKET) {
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void onCraft(final PrepareItemCraftEvent event) {
        final CraftingInventory inv = event.getInventory();
        final ItemStack AIR = new ItemStack(Material.AIR);
        if (inv.getResult() == null) {
            return;
        }
        if (new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1).isSimilar(inv.getResult())) {
            inv.setResult(AIR);
        }
    }

    @EventHandler
    public void onAppleEat(final PlayerItemConsumeEvent event) {
        if (LGPlayer.thePlayer(event.getPlayer()).isInGame())
            if (new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1).isSimilar(event.getItem())) {
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(LoupGarouUHC.getPlugin(), () -> event.getPlayer().getInventory().remove(event.getItem()));
            }
    }

    @EventHandler
    public void WeatherChangeEvent(final WeatherChangeEvent event) {
        event.setCancelled(true);
        event.getWorld().setWeatherDuration(0);
        event.getWorld().setThundering(false);
    }
}