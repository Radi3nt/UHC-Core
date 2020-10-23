package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Timber extends Scenario {

    static int[] dx = {0, 0, -1, 1};
    static int[] dz = {-1, 1, 0, 0};
    static Material[] axes = {Material.DIAMOND_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.STONE_AXE,
            Material.WOOD_AXE};
    static Material[] logs = {Material.LOG, Material.LOG_2};
    static Material[] leaves = {Material.LEAVES, Material.LEAVES_2};

    private boolean axeOnly = false;
    private int time = 3;

    public Timber(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "Timber";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.LOG);
    }

    @ScenarioEvent
    public void event(BlockBreakEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                World w = e.getBlock().getWorld();
                Location loc = e.getBlock().getLocation();
                Block block = e.getBlock();
                boolean okay = false;

                /*
                 * Leaves Checker
                 */
                for (int dy = 1; !okay && dy < 9; dy++) {
                    for (int k = 0; k < leaves.length; k++) {
                        for (int i = 0; !okay && i < dx.length; i++) {
                            okay |= (w.getBlockAt(loc.getBlockX() + dx[i], loc.getBlockY() + dy, loc.getBlockZ() + dz[i])
                                    .getType() == leaves[k]); // must have leaves somewhere nearby
                        }
                    }
                }
                if (!okay)
                    return;

                /*
                 * Log Checker
                 */
                okay = false;
                for (int j = 0; j < logs.length; j++) {
                    if (block.getType() == logs[j]) {
                        okay = true;
                    }
                }
                if (!okay) {
                    return; // Must be some type of log
                }

                /*
                 * Axe Checker
                 */
                okay = false;
                ItemStack axe = null;
                if (axeOnly) {
                    PlayerInventory inv = player.getPlayer().getInventory();
                    for (int i = 0; !okay && i < axes.length; i++) {
                        if (inv.getItemInMainHand().getType().equals(axes[i])) {
                            okay = true;
                            axe = inv.getItemInMainHand();
                        }
                    }
                    if (!okay) {
                        return; // must destroy with axe
                    }
                }

                if (axe != null)
                    fell(w, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), time, axe);
                else
                    fell(w, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), time);

            }
        }
    }

    private void fell(World w, int x, int y, int z, long time) {
        Bukkit.getScheduler().runTaskLater(LoupGarouUHC.getPlugin(), () -> {
            boolean air = false;
            if (w.getBlockAt(x, y, z).getType() == Material.AIR) {
                air = true;
            }
            if (!w.getBlockAt(x, y, z).breakNaturally() && !air) {
                return;
            } else if (!air) {
                w.playSound(new Location(w, x, y, z), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1, 1.5f);
            }
            // Checks blocks on current y plane
            for (int j = 0; j < logs.length; j++) {
                for (int i = 0; i < dx.length; i++) {
                    if (w.getBlockAt(x + dx[i], y, z + dz[i]).getType() == logs[j]) {
                        fell(w, x + dx[i], y, z + dz[i], time);
                    }
                }

                // Checks block above
                if (w.getBlockAt(x, y + 1, z).getType() == logs[j]) {
                    fell(w, x, y + 1, z, time);
                }
                // Checks block below
                if (w.getBlockAt(x, y - 1, z).getType() == logs[j]) {
                    fell(w, x, y - 1, z, time);
                }
            }
        }, time);
    }

    private void fell(World w, int x, int y, int z, long time, ItemStack axe) {
        Bukkit.getScheduler().runTaskLater(LoupGarouUHC.getPlugin(), () -> {
            boolean air = false;
            if (w.getBlockAt(x, y, z).getType() == Material.AIR) {
                air = true;
            }
            if (!w.getBlockAt(x, y, z).breakNaturally() && !air) {
                return;
            } else {
                if (axe != null) {
                    if (axe.getDurability() < axe.getType().getMaxDurability()) {
                        w.playSound(new Location(w, x, y, z), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1, 1.5f);
                    } else {
                        return;
                    }
                    if (!air) {
                        axe.setDurability((short) (axe.getDurability() + 1));
                    }
                } else {
                    return;
                }
            }
            // Checks blocks on current y plane
            for (int j = 0; j < logs.length; j++) {
                for (int i = 0; i < dx.length; i++) {
                    if (w.getBlockAt(x + dx[i], y, z + dz[i]).getType() == logs[j]) {
                        fell(w, x + dx[i], y, z + dz[i], time, axe);
                    }
                }

                // Checks block above
                if (w.getBlockAt(x, y + 1, z).getType() == logs[j]) {
                    fell(w, x, y + 1, z, time, axe);
                }
                // Checks block below
                if (w.getBlockAt(x, y - 1, z).getType() == logs[j]) {
                    fell(w, x, y - 1, z, time, axe);
                }
            }
        }, time);
    }

    @ScenarioGetter(name = "Axe only")
    public boolean isAxeOnly() {
        return axeOnly;
    }

    @ScenarioSetter(name = "Axe only")
    public void setAxeOnly(boolean axeOnly) {
        this.axeOnly = axeOnly;
    }

    @ScenarioGetter(name = "Time")
    public int getTime() {
        return time;
    }

    @ScenarioSetter(name = "Time")
    public void setTime(int time) {
        this.time = time;
    }
}
