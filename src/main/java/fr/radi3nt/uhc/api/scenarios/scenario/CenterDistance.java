package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.utilis.Maths;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CenterDistance extends Scenario {

    List<Integer> DistanceAnnounce = new ArrayList<>();

    public CenterDistance(UHCGame game) {
        super(game);
        DistanceAnnounce.add(0);
        DistanceAnnounce.add(100);
        DistanceAnnounce.add(250);
        DistanceAnnounce.add(500);
        DistanceAnnounce.add(750);
        DistanceAnnounce.add(1000);
        DistanceAnnounce.add(1250);
        DistanceAnnounce.add(1500);
        DistanceAnnounce.add(1750);
        DistanceAnnounce.add(2000);
    }

    public static ScenarioData getData() {
        return new ScenarioData("CenterDistance").setItemStack(new ItemStack(Material.COMPASS)).setDescription("Display your distance from 0 0");
    }

    @Override
    public void tick(GameTimer gameTimer, int tick) {
        super.tick(gameTimer, tick);
        if (gameTimer.getGame().equals(game)) {
            if (isActive()) {
                if (tick % 20 == 0)
                for (UHCPlayer lgp : gameTimer.getGame().getAlivePlayers()) {
                    int distance = (int) Maths.distanceIn2D(lgp.getPlayer().getLocation(), game.getParameters().getGameSpawn());

                    String disM = "";
                    try {
                        disM = getMessage(lgp.getLanguage(), "normal");
                    } catch (CannotFindMessageException e) {
                        UHCCore.handleCannotFindMessageException(e);
                    }
                    String disS = "NaN";
                    int maxNumber = 0;
                    Integer lastNumber = 0;

                    for (Integer integer : DistanceAnnounce) {
                        if (integer < distance) {
                            lastNumber = integer;
                        } else if (lastNumber != null) {
                            disS = disM.replace("%1%", String.valueOf(lastNumber)).replace("%2%", String.valueOf(integer));
                            lastNumber = null;
                        }
                        if (maxNumber < integer) {
                            maxNumber = integer;
                        }
                    }
                    if (distance == 0) {
                        //todo distance - message
                    }
                    if (disS.equals("NaN")) {
                        try {
                            disS = getMessage(lgp.getLanguage(), "normal").replace("%1%", String.valueOf(maxNumber));
                        } catch (CannotFindMessageException e) {
                            UHCCore.handleCannotFindMessageException(e);
                        }
                    }
                    lgp.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(disS));
                }
            }
        }
    }
}
