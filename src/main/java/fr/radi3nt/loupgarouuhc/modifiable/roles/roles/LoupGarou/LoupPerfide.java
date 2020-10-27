package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.LoupGarou;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LoupPerfide extends LoupGarou {

    boolean day = false;

    public LoupPerfide(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("LoupPerfide", WinType.LOUP_GAROU, RoleType.LOUP_GAROU);
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
}
