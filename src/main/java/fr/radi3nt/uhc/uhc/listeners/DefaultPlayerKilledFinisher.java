package fr.radi3nt.uhc.uhc.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.uhc.api.events.UHCPlayerKillAnotherEvent;
import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.Attribute;
import fr.radi3nt.uhc.api.player.PlayerState;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.player.npc.NPC;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DefaultPlayerKilledFinisher implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKilled(UHCPlayerKilledEvent e) {
        if (!e.isCancelled()) {
            UHCPlayer uhcPlayer = e.getKilled();
            UHCGame game = uhcPlayer.getGameData().getGame();


            uhcPlayer.getPlayerStats().refresh();
            Location playerDeathLocation = uhcPlayer.getPlayerStats().getLastLocation();
            this.spawnCorpse(uhcPlayer, playerDeathLocation);

            uhcPlayer.getGameData().setPlayerState(PlayerState.DEAD);

            if (game.getState() == GameState.PLAYING) {
                if (e.getKiller() != null) {
                    uhcPlayer.getGameInformation().putAttribute("killer", new Attribute<>(e.getKiller()));
                    Bukkit.getPluginManager().callEvent(new UHCPlayerKillAnotherEvent(uhcPlayer.getGameData().getGame(), e.getKiller(), uhcPlayer));
                }
            }

            //todo sound ENTITY_ELDER_GUARDIAN_AMBIENT

            for (UHCPlayer deadAndAlivePlayer : e.getGame().getSpectatorsAndAlivePlayers()) {
                if (deadAndAlivePlayer.isOnline())
                    deadAndAlivePlayer.getPlayer().playSound(playerDeathLocation, Sound.ENTITY_WITHER_SPAWN, SoundCategory.AMBIENT, 1, 1);
            }

            if (uhcPlayer.getPlayerStats().getXp() != 0)
                playerDeathLocation.getWorld().spawn(playerDeathLocation.clone().add(0.5, 0.5, 0.5), ExperienceOrb.class).setExperience(uhcPlayer.getPlayerStats().getXp() / 17);

            for (ItemStack item : uhcPlayer.getPlayerStats().getInventory().getContents()) {
                if (item != null) {
                    playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : uhcPlayer.getPlayerStats().getInventory().getArmorContents()) {
                if (item != null) {
                    playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : uhcPlayer.getPlayerStats().getInventory().getExtraContents()) {
                if (item != null) {
                    playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            for (ItemStack item : uhcPlayer.getPlayerStats().getInventory().getStorageContents()) {
                if (item != null) {
                    playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                    item.setAmount(0);
                }
            }
            uhcPlayer.getPlayerStats().update();

            game.kill(uhcPlayer, e.getReason(), playerDeathLocation);


            //uhcPlayer.getGameData().setCanBeRespawned(false);
            //uhcPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            if (e.getKilled().isOnline())
            try {
                uhcPlayer.getPlayer().setFlying(true);
            } catch (IllegalArgumentException e1) {

            }
        }
    }

    private NPC spawnCorpse(UHCPlayer player, Location baseLoc) {
        Location npcLoc = baseLoc.getBlock().getLocation().clone().add(0, -1, 0);
        while (npcLoc.getBlock().getType() == Material.LONG_GRASS || npcLoc.getBlock().getType() == Material.AIR || !npcLoc.getBlock().getType().isSolid()) {
            npcLoc.setY(npcLoc.getBlockY() - 1);
            if (npcLoc.getBlockY() <= -1) {
                npcLoc = baseLoc.getBlock().getLocation().clone().add(0, -1, 0);
                break;
            }
        }
        NPC npc = new NPC(player.getName(), npcLoc.add(0, 1, 0), UHCCore.getPlugin());
        for (UHCPlayer uhcPlayer : player.getGameData().getGame().getSpectatorsAndAlivePlayers()) {
            if (uhcPlayer.isOnline())
                npc.addRecipient(uhcPlayer.getPlayer());
        }
        if (player.isOnline()) {
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
        }
        npc.spawn(false, true);
        npc.setSleep(true, npc.getDirectionInversed(baseLoc), false);
        return npc;
    }

}
