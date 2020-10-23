package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.npc.NMSBase;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AutoBreak extends Scenario {

    public static final int MODE_BLACKLIST = 0;
    public static final int MODE_WHITELIST = 1;

    private int mode = MODE_BLACKLIST;
    private int time = 1 * 60 * 20;
    private List<Material> list = new ArrayList<>();

    public AutoBreak(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "AutoBreak";
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.COBBLESTONE);
    }

    @ScenarioEvent
    public void event(BlockPlaceEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getPlayer());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                if (mode == 0) {
                    if (list.contains(e.getBlockPlaced().getType()))
                        return;
                } else {
                    if (!list.contains(e.getBlockPlaced().getType()))
                        return;
                }

                new BukkitRunnable() {
                    final int id = (int) Math.ceil(Math.random() * 1000);
                    int i = 0;

                    @Override
                    public void run() {
                        if (i == 10) {
                            Object packet = null;
                            try {
                                packet = NMSBase.getNMSClass("PacketPlayOutBlockBreakAnimation").getConstructor(int.class, NMSBase.getNMSClass("BlockPosition"), int.class).newInstance(id, NMSBase.getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), -1);
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException instantiationException) {
                                instantiationException.printStackTrace();
                            }
                            //PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, new BlockPosition(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), -1);
                            NMSBase.sendPacket(packet);
                            e.getBlockPlaced().breakNaturally();
                            cancel();
                        } else {
                            Object packet = null;
                            try {
                                packet = NMSBase.getNMSClass("PacketPlayOutBlockBreakAnimation").getConstructor(int.class, NMSBase.getNMSClass("BlockPosition"), int.class).newInstance(id, NMSBase.getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), i);
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException instantiationException) {
                                instantiationException.printStackTrace();
                            }
                            //PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, new BlockPosition(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), i);
                            NMSBase.sendPacket(packet);
                        }
                        if (e.getBlock().getType() == Material.AIR) {
                            Object packet = null;
                            try {
                                packet = NMSBase.getNMSClass("PacketPlayOutBlockBreakAnimation").getConstructor(int.class, NMSBase.getNMSClass("BlockPosition"), int.class).newInstance(id, NMSBase.getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), -1);
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException instantiationException) {
                                instantiationException.printStackTrace();
                            }
                            //PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(id, new BlockPosition(e.getBlockPlaced().getX(), e.getBlockPlaced().getY(), e.getBlockPlaced().getZ()), -1);
                            NMSBase.sendPacket(packet);
                            cancel();
                        }
                        i++;
                    }
                }.runTaskTimer(LoupGarouUHC.getPlugin(), time / 10, time / 10);
            }
        }
    }

    @ScenarioGetter(name = "Mode")
    public int getMode() {
        return mode;
    }

    @ScenarioSetter(name = "Mode")
    public void setMode(int mode) {
        this.mode = mode;
    }

    @ScenarioGetter(name = "Time")
    public int getTime() {
        return time;
    }

    @ScenarioSetter(name = "Time")
    public void setTime(int time) {
        this.time = time;
    }

    @ScenarioGetter(name = "List")
    public List<Material> getList() {
        return list;
    }

    @ScenarioSetter(name = "List")
    public void setList(List<Material> list) {
        this.list = list;
    }
}
