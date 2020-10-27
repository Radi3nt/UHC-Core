package fr.radi3nt.loupgarouuhc.modifiable.roles.roles.Villagers;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.roles.Role;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleIdentity;
import fr.radi3nt.loupgarouuhc.modifiable.roles.RoleType;
import fr.radi3nt.loupgarouuhc.modifiable.roles.WinType;
import org.bukkit.Location;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Sorciere extends Role {

    public boolean hasrespwaned = false;

    public Sorciere(LGGame game) {
        super(game);
    }

    public static RoleIdentity getStaticRoleIdentity() {
        return new RoleIdentity("Sorciere", WinType.VILLAGE, RoleType.VILLAGER);
    }

    @Override
    public RoleIdentity getRoleIdentity() {
        return getStaticRoleIdentity();
    }

    @Override
    public void OnNight(LGGame game, LGPlayer lgp) {

    }

    @Override
    public void OnDay(LGGame game, LGPlayer lgp) {

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
        Potion pohealth1 = new Potion(PotionType.INSTANT_HEAL,1, true);
        Potion poreg1 = new Potion(PotionType.REGEN,1, true);
        Potion podam1 = new Potion(PotionType.INSTANT_DAMAGE,1, true);

        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), pohealth1.toItemStack(3));
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), poreg1.toItemStack(1));
        lgp.getPlayer().getWorld().dropItem(lgp.getPlayer().getLocation(), podam1.toItemStack(3));
    }
}
