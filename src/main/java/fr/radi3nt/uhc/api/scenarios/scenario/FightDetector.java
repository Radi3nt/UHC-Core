package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class FightDetector extends Scenario {

    private final HashMap<UUID, Long> players = new HashMap<>();
    private final int delay = 15;

    public FightDetector(UHCGame game) {
        super(game);
    }


    public static ScenarioData getData() { return new ScenarioData("FightDetector").setItemStack(new ItemStack(Material.IRON_AXE)).setDescription("Detect fight for spectators");}



        @EventHandler
    public void event(EntityDamageByEntityEvent e) {
        if (isActive()) {
            if (e.getEntity() instanceof Player) {
                if (e.getDamager() instanceof Player) {
                    if (game.getPvP().isPvp()) {
                        if (players.getOrDefault(e.getDamager().getUniqueId(), System.currentTimeMillis()) / 1000 + delay <= System.currentTimeMillis() / 1000)
                            for (UHCPlayer spectator : game.getSpectators()) {
                                sendMessage((Player) e.getEntity(), (Player) e.getDamager(), spectator.getPlayer());
                            }
                        players.put(e.getEntity().getUniqueId(), System.currentTimeMillis());
                        players.put(e.getDamager().getUniqueId(), System.currentTimeMillis());
                    }
                } else if (e.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) e.getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        if (game.getPvP().isPvp()) {
                            UHCPlayer shooter = UHCPlayer.thePlayer((Player) projectile.getShooter());
                            if (shooter.getGameData().getGame() == game) {
                                if (players.getOrDefault(e.getDamager().getUniqueId(), System.currentTimeMillis()) / 1000 + delay < System.currentTimeMillis() / 1000)
                                    for (UHCPlayer spectator : game.getSpectators()) {
                                        sendMessage((Player) e.getEntity(), shooter.getPlayer(), spectator.getPlayer());
                                    }
                                players.put(e.getEntity().getUniqueId(), System.currentTimeMillis());
                                players.put(e.getDamager().getUniqueId(), System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendMessage(Player damager, Player damaged, Player toSend) {
        if (damager != null && damaged != null && toSend != null) {
            //toSend.sendMessage(LoupGarouUHC.getPrefix() + ChatColor.DARK_AQUA + damager + ChatColor.AQUA + " is now fighting " + ChatColor.DARK_AQUA + damaged + " ");
            TextComponent tc = new TextComponent();
            tc.setText(UHCCore.getPrefix() + ChatColor.DARK_AQUA + damager + ChatColor.AQUA + " is now fighting " + ChatColor.DARK_AQUA + damaged + " ");
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg spectate " + damager.getName()));

            TextComponent tc2 = new TextComponent();
            tc2.setText(ChatColor.DARK_GREEN + "[" + ChatColor.BOLD + "CLICK HERE" + ChatColor.DARK_GREEN + "]");
            tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "click here to tp to " + ChatColor.DARK_GREEN + damager.getName()).create()));
            tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg spectate " + damager.getName()));

            tc.addExtra(tc2);

            toSend.spigot().sendMessage(tc);
        }
    }

}
