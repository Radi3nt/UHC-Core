package fr.radi3nt.uhc.uhc.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.radi3nt.uhc.api.events.UHCPlayerKillAnotherEvent;
import fr.radi3nt.uhc.api.events.UHCPlayerKilledEvent;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.game.reasons.ReasonSlainByPlayer;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.player.Attribute;
import fr.radi3nt.uhc.api.player.PlayerState;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.player.npc.NPC;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultPlayerKilledFinisher implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKilled(UHCPlayerKilledEvent e) {
        if (!e.isProceeded()) {
            e.setProceeded(true);
            UHCPlayer uhcPlayer = e.getKilled();
            UHCGame game = uhcPlayer.getGameData().getGame();

            uhcPlayer.sendTitle(ChatColor.RED + "You died", ChatColor.GRAY + "You can only spectate the game", 5, 5 * 20, 5);

            uhcPlayer.getPlayerStats().refresh();
            Location playerDeathLocation = uhcPlayer.getPlayerStats().getLastLocation();
            spawnCorpse(uhcPlayer, playerDeathLocation);

            uhcPlayer.getGameData().setPlayerState(PlayerState.DEAD);

            if (game.getState() == GameState.PLAYING) {
                if (e.getReason() instanceof ReasonSlainByPlayer) {
                    UHCPlayer killer = ((ReasonSlainByPlayer) e.getReason()).getKiller();
                    uhcPlayer.getGameInformation().putAttribute("killer", new Attribute<>(killer));
                    Bukkit.getPluginManager().callEvent(new UHCPlayerKillAnotherEvent(uhcPlayer.getGameData().getGame(), killer, uhcPlayer));
                }
            }

            //todo sound ENTITY_ELDER_GUARDIAN_AMBIENT

            for (UHCPlayer deadAndAlivePlayer : e.getGame().getSpectatorsAndAlivePlayers()) {
                if (deadAndAlivePlayer.isOnline())
                    deadAndAlivePlayer.getPlayer().playSound(playerDeathLocation, Sound.ENTITY_WITHER_SPAWN, SoundCategory.AMBIENT, 1, 1);
            }

            lootPlayerDrops(uhcPlayer);

            game.kill(uhcPlayer, e.getReason(), playerDeathLocation);
            uhcPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            if (e.getKilled().isOnline())
            try {
                uhcPlayer.getPlayer().setFlying(true);
            } catch (IllegalArgumentException e1) {

            }
        }
    }

    private void spawnCorpse(UHCPlayer player, Location baseLoc) {
        UHCGame game = player.getGameData().getGame();
        Location npcLoc = baseLoc.clone();
        npcLoc.setY(baseLoc.getBlockY());
        while (npcLoc.getBlock().getType() == Material.LONG_GRASS || npcLoc.getBlock().getType() == Material.AIR || !npcLoc.getBlock().getType().isSolid()) {
            npcLoc.setY(npcLoc.getBlockY() - 1);
            if (npcLoc.getBlockY() <= -1) {
                npcLoc = baseLoc.clone().add(0, -1, 0);
                break;
            }
        }
        NPC dead = new NPC(player.getName(), npcLoc.clone().add(0, 1, 0), UHCCore.getPlugin());
        NPC npc = new NPC(player.getName(), npcLoc.clone().add(0, 1, 0), UHCCore.getPlugin());
        dead.spawn(false, true);
        npc.spawn(false, true);
        Location newLoc = dead.getLocation().clone();
        newLoc.setYaw(dead.getLocation().getYaw()+90);
        dead.teleport(newLoc, false);
        if (player.isOnline()) {
            EntityPlayer playerNMS = ((CraftPlayer) player.getPlayer()).getHandle();
            if (playerNMS.getProfile() != null) {
                try {
                    GameProfile profile = playerNMS.getProfile();
                    Property property = profile.getProperties().get("textures").iterator().next();
                    String texture = property.getValue();
                    String signature = property.getSignature();
                    npc.setSkin(texture, signature);
                    dead.setSkin(texture, signature);
                } catch (Exception e1) {
                    Logger.getGeneralLogger().logInConsole("§4§lCannot get skin for player: " + player.getPlayer().getUniqueId());
                    Logger.getGeneralLogger().log(e1);
                }
            }
            ItemStack boots = player.getPlayerStats().getInventory().getBoots();
            ItemStack legs = player.getPlayerStats().getInventory().getLeggings();
            ItemStack chest = player.getPlayerStats().getInventory().getChestplate();
            ItemStack head = player.getPlayerStats().getInventory().getHelmet();
            ItemStack hand = player.getPlayerStats().getInventory().getItemInMainHand();
            if (boots!=null) {
                npc.setEquipment(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(boots.clone()).cloneItemStack());
                dead.setEquipment(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(boots.clone()).cloneItemStack());
            }
            if (legs!=null) {
                npc.setEquipment(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(legs.clone()).cloneItemStack());
                dead.setEquipment(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(legs.clone()).cloneItemStack());
            }
            if (chest!=null) {
                npc.setEquipment(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(chest.clone()).cloneItemStack());
                dead.setEquipment(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(chest.clone()).cloneItemStack());
            }
            if (head!=null) {
                npc.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(head.clone()).cloneItemStack());
                dead.setEquipment(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(head.clone()).cloneItemStack());
            }
            if (hand!=null) {
                npc.setEquipment(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(hand.clone()).cloneItemStack());
                dead.setEquipment(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(hand.clone()).cloneItemStack());
            }
        }
        dead.reloadNpc();
        npc.reloadNpc();
        for (UHCPlayer uhcPlayer : player.getGameData().getGame().getSpectatorsAndAlivePlayers()) {
            if (uhcPlayer.isOnline()) {
                dead.addRecipient(uhcPlayer.getPlayer());
            }
        }
        dead.setStatus((byte) 3);
        npc.setSleep(true, npc.getDirectionInversed(baseLoc), false);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i==15) {
                    for (UHCPlayer uhcPlayer : game.getSpectatorsAndAlivePlayers()) {
                        if (uhcPlayer.isOnline()) {
                            npc.addRecipient(uhcPlayer.getPlayer());
                        }
                    }
                }
                if (i==20) {
                    //dead.destroy();
                    this.cancel();
                }
                i++;
            }
        }.runTaskTimer(UHCCore.getPlugin(), 0L, 0L);
    }

    private void lootPlayerDrops(UHCPlayer player) {
        player.getPlayerStats().refresh();
        Location playerDeathLocation = player.getPlayerStats().getLastLocation();

        if (player.getPlayerStats().getXp() != 0)
            playerDeathLocation.getWorld().spawn(playerDeathLocation.clone().add(0.5, 0.5, 0.5), ExperienceOrb.class).setExperience(player.getPlayerStats().getXp() / 17);

        for (ItemStack item : player.getPlayerStats().getInventory().getContents()) {
            if (item != null) {
                playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                item.setAmount(0);
            }
        }
        for (ItemStack item : player.getPlayerStats().getInventory().getArmorContents()) {
            if (item != null) {
                playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                item.setAmount(0);
            }
        }
        for (ItemStack item : player.getPlayerStats().getInventory().getExtraContents()) {
            if (item != null) {
                playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                item.setAmount(0);
            }
        }
        for (ItemStack item : player.getPlayerStats().getInventory().getStorageContents()) {
            if (item != null) {
                playerDeathLocation.getWorld().dropItem(playerDeathLocation, item.clone());
                item.setAmount(0);
            }
        }
        player.getPlayerStats().update();
    }

}
