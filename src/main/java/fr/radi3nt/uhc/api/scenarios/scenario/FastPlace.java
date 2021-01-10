package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

public class FastPlace extends Scenario {


    private int TICKS_BETWEEN_TRIGGER = 4, DELAY = 0, RANGE = 5;

    public FastPlace(UHCGame game) {
        super(game);
    }

    public static ScenarioData getData() { return new ScenarioData("FastPlace").setItemStack(new ItemStack(Material.IRON_SPADE)).setDescription("Fast placing blocks");}


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) { //todo PlayerAnimationEvent
        UHCPlayer player = UHCPlayer.thePlayer(event.getPlayer());
        if (player.getGameData().getGame().equals(game))
            if (isActive()) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getPlayer().getTargetBlock(null, 100);
                    if (block.getState() instanceof InventoryHolder && !event.getPlayer().isSneaking()) {
                        InventoryHolder ih = (InventoryHolder) block.getState();
                        Inventory i = ih.getInventory();
                        event.getPlayer().openInventory(i);
                    } else {
                        fastPlace(event.getPlayer());
                    }
                }
            }
    }

    private void fastPlace(Player player) {
        AtomicInteger timeSinceTrigger = new AtomicInteger(), id = new AtomicInteger();

        id.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), () -> {

            if (timeSinceTrigger.get() >= TICKS_BETWEEN_TRIGGER) {
                Bukkit.getScheduler().cancelTask(id.get());
                return;
            }

            timeSinceTrigger.incrementAndGet();

            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getAmount() < 1 || !item.getType().isBlock())
                return;

            BlockIterator itererator = new BlockIterator(player, RANGE);
            Block currentBlock = itererator.next(), formerBlock = null;
            while (itererator.hasNext()) {
                formerBlock = currentBlock;
                currentBlock = itererator.next();
                if (!currentBlock.getType().isSolid())
                    continue;
                break;
            }

            if (currentBlock == null || currentBlock.getType() == Material.AIR)
                return;

            EnumDirection direction = EnumDirection.valueOf(formerBlock.getFace(currentBlock).name());

            EntityHuman entityHuman = null;
            try {
                entityHuman = (EntityHuman) player.getClass().getMethod("getHandle").invoke(player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
            BlockPosition position = new BlockPosition(formerBlock.getX(), formerBlock.getY(), formerBlock.getZ());


            CraftItemStack.asNMSCopy(item).placeItem(entityHuman, ((CraftWorld) formerBlock.getWorld()).getHandle(), position, EnumHand.MAIN_HAND, direction, 0, 0, 0);
        }, DELAY, DELAY));
    }

    @ScenarioGetter(name = "Ticks between trigger")
    public int getTICKS_BETWEEN_TRIGGER() {
        return TICKS_BETWEEN_TRIGGER;
    }

    @ScenarioSetter(name = "Ticks between trigger")
    public void setTICKS_BETWEEN_TRIGGER(int TICKS_BETWEEN_TRIGGER) {
        this.TICKS_BETWEEN_TRIGGER = TICKS_BETWEEN_TRIGGER;
    }

    @ScenarioGetter(name = "Delay")
    public int getDELAY() {
        return DELAY;
    }

    @ScenarioSetter(name = "Delay")
    public void setDELAY(int DELAY) {
        this.DELAY = DELAY;
    }

    @ScenarioGetter(name = "Range")
    public int getRANGE() {
        return RANGE;
    }

    @ScenarioSetter(name = "Range")
    public void setRANGE(int RANGE) {
        this.RANGE = RANGE;
    }
}
