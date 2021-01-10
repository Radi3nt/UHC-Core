package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.exeptions.common.NoArgsException;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.exeptions.common.NoUHCPlayerException;
import fr.radi3nt.uhc.api.game.GameState;
import fr.radi3nt.uhc.api.game.GameType;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.game.UHCGameImpl;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.WorldMap;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class SpectateCommand implements CommandArg {

    @Override
    public void onCommand(CommandUtilis utilis) throws NoArgsException, NoPermissionException, NoUHCPlayerException {
        requirePermission(utilis.getSender(), "uhc.spectate", "");
        if (utilis.requireMinArgs(1)) {
            UHCPlayer player = UHCPlayer.thePlayer((Player) utilis.getSender());
            for (UHCGame game : UHCCore.getGameQueue()) {
                if (game.getUHCPlayerInThisGame(utilis.getArgs()[0])!=null) {
                    game.spectate(player);
                    player.getPlayer().teleport(UHCPlayer.thePlayer(utilis.getArgs()[0]).getPlayerStats().getLastLocation());
                    player.getPlayer().setGameMode(GameMode.SPECTATOR);
                    try {
                        player.sendMessage(String.format(player.getLanguage().getMessage("commands.spectate"), utilis.getArgs()[0]));
                    } catch (CannotFindMessageException e) {
                        UHCCore.handleCannotFindMessageException(e, player);
                    }
                    return;
                }
            }
            throw new NoUHCPlayerException();
        }
    }

    public static void mapmap(UHCPlayer player, String url) {
        String imageurl = url.toLowerCase();

        MapView map = Bukkit.getServer().createMap(Bukkit.getServer().getWorlds().get(0));

        for(MapRenderer render : map.getRenderers()) {
            map.removeRenderer(render);
        }

        map.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView map, MapCanvas canvas, Player player) {
                if (MinecraftServer.currentTick % 60 == 0) {
                    /*
                    Webcam webcam = Webcam.getDefault();
                    webcam.open();
                    BufferedImage photo = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
                    photo.getGraphics().drawImage(webcam.getImage(), 0, 0, null);

                    canvas.drawImage(0, 0, webcam.getImage());

                     */
                }

            }

        });

        ItemStack item = new ItemStack(Material.MAP,1, map.getId());

        player.getPlayerStats().refresh();
        player.getPlayerStats().getInventory().addItem(item);
        player.getPlayerStats().update();
    }

    @Override
    public List<String> tabComplete(CommandUtilis utilis) {
        HashSet<String> set = new HashSet<>();
        for (UHCPlayer player : UHCCore.getPlayers()) {
            set.add(player.getName());
        }
        return new ArrayList<>(set);
    }
}
