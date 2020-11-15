package fr.radi3nt.uhc.api.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.uhc.api.events.UHCPlayerKillAnotherEvent;
import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.player.npc.NPC;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerKilledFinisherListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKilled(UHCPlayerKilledEvent e) {
        if (!e.isCancelled()) {
            UHCPlayer uhcPlayer = e.getKilled();
            UHCGame game = uhcPlayer.getGameData().getGame();
            Player player = e.getKilled().getPlayer();
            Location playerDeathLocation = player.getLocation();
            NPC npc = this.spawnCorpse(uhcPlayer, uhcPlayer.getPlayer().getLocation());

            //todo sound ENTITY_ELDER_GUARDIAN_AMBIENT


            if (player.getTotalExperience() != 0)
                player.getLocation().getWorld().spawn(playerDeathLocation.clone().add(0.5, 0.5, 0.5), ExperienceOrb.class).setExperience(player.getTotalExperience() / 17);

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    player.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : player.getInventory().getArmorContents()) {
                if (item != null) {
                    player.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : player.getInventory().getExtraContents()) {
                if (item != null) {
                    player.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : player.getInventory().getStorageContents()) {
                if (item != null) {
                    player.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }

            game.kill(uhcPlayer, e.getReason(), playerDeathLocation);

            if (game.getState() == GameState.PLAYING) {
                if (e.getKiller() != null) {
                    Bukkit.getPluginManager().callEvent(new UHCPlayerKillAnotherEvent(uhcPlayer.getGameData().getGame(), e.getKiller(), uhcPlayer));
                    uhcPlayer.getGameData().setKiller(e.getKiller()); //todo move this
                }
            }


            //uhcPlayer.getGameData().setCanBeRespawned(false);
            uhcPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            try {
                uhcPlayer.getPlayer().setFlying(true);
            } catch (IllegalArgumentException e1) {

            }
        }
    }

    private NPC spawnCorpse(UHCPlayer player, Location baseLoc) {
        Location npcLoc = baseLoc.clone();
        while (npcLoc.getBlock().getType() == Material.AIR && !npcLoc.getBlock().getType().isSolid()) {
            npcLoc.setY(npcLoc.getBlockY() - 1);
            if (npcLoc.getBlockY() <= -1) {
                npcLoc = baseLoc.clone();
                break;
            }
        }
        NPC npc = new NPC(player.getName(), npcLoc.add(0, 1, 0), UHCCore.getPlugin());
        for (UHCPlayer uhcPlayer : player.getGameData().getGame().getDeadAndAlivePlayers()) {
            if (uhcPlayer.isOnline())
                npc.addRecipient(uhcPlayer.getPlayer());
        }
        EntityPlayer playerNMS = ((CraftPlayer) player.getPlayer()).getHandle();
        if (playerNMS.getProfile() != null) {
            try {
                GameProfile profile = playerNMS.getProfile();
                Property property = profile.getProperties().get("textures").iterator().next();
                String texture = property.getValue();
                String signature = property.getSignature();
                npc.setSkin(texture, signature);
            } catch (Exception e1) {
                Logger.getGeneralLogger().logInConsole("§4§lCannot get skin for player: " + player.getPlayer().getUniqueId());
                Logger.getGeneralLogger().log(e1);
            }
        }
        npc.spawn(false, true);
        npc.setSleep(true, npc.getDirectionInversed(baseLoc), false);
        return npc;
    }

}
