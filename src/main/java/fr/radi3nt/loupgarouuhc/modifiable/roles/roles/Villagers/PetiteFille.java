package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PetiteFille extends Role {

    boolean day = false;

    public PetiteFille(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("PetiteFille", WinType.VILLAGE, RoleType.VILLAGER);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {
        day = false;
        new BukkitRunnable() {

            boolean hasBeenInvisible = false;

            @Override
            public void run() {
                if (lgp.getPlayer() != null) {
                    boolean nullItemStack = true;
                    for (ItemStack armorContent : lgp.getPlayer().getInventory().getArmorContents()) {
                        if (armorContent != null) {
                            nullItemStack = false;
                        }
                    }

                    if (nullItemStack && !hasBeenInvisible) {
                        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 60 * 20, 1, true, false), true);
                        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 60 * 20, 8, true, false), true);
                        hasBeenInvisible = true;
                    } else if (!nullItemStack && hasBeenInvisible) {
                        lgp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        lgp.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
                        cancel();
                    }
                }
                if (day || lgp.getGameData().getGame() != game) {
                    cancel();
                }
            }

        }.runTaskTimer(LoupGarouUHC.getPlugin(), 0L, 1L);
    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {
        day = true;
        lgp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        lgp.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }

    @Override
    public void OnNewEpisode(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().setMaxHealth(20F);
    }

    @Override
    public void OnKillSomeone(LGGame game, LGPlayer killer, LGPlayer killed) {

    }

    @Override
    public void OnKilled(LGGame game, LGPlayer killed, LGPlayer killer, Location location) {

    }

    @Override
    public void OnDiscoverRole(LGGame game, LGPlayer lgp) {
        lgp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1, true, false), true);
    }

}


