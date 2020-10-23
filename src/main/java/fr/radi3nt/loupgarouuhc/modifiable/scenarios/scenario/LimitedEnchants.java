package fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.player.LGPlayer;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LimitedEnchants extends Scenario {

    private final List<Enchantment> disabledEnchantement = new ArrayList<>();
    private final Map<Enchantment, Integer> limitedEnchants = new HashMap();

    public LimitedEnchants(LGGame game) {
        super(game);
    }

    public static String getName() {
        return "LimitedEnchants";
    }

    public static ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        itemStack.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        return itemStack;
    }

    @ScenarioEvent
    public void event(PrepareItemEnchantEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getEnchanter());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                for (int i = 0; i < e.getOffers().length; i++) {
                    EnchantmentOffer offer = e.getOffers()[i];
                    if (disabledEnchantement.contains(offer.getEnchantment())) {
                        e.getOffers()[i] = null;
                    }
                }
            }
        }
    }

    @ScenarioEvent
    public void event(EnchantItemEvent e) {
        LGPlayer player = LGPlayer.thePlayer(e.getEnchanter());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                for (Enchantment enchantment : disabledEnchantement) {
                    if (e.getEnchantsToAdd().containsKey(enchantment)) {
                        e.setCancelled(true);
                        return;
                    }
                }
                ItemStack current = e.getItem();
                if (current == null) {
                    return;
                }
                if (current.getEnchantments().isEmpty()) {
                    if (current.getType().equals(Material.ENCHANTED_BOOK)) {
                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) current.getItemMeta();
                        if (meta != null) {
                            current = (this.checkEnchant(meta.getStoredEnchants(), e.getEnchanter(), current));
                        }
                    }
                } else {
                    current = (this.checkEnchant(current.getEnchantments(), e.getEnchanter(), current));
                }
            }
        }
    }

    @ScenarioEvent
    public void onPrepareAnvilEvent(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.ANVIL)) {
            return;
        }
        if (event.getSlot() != 2) {
            return;
        }
        ItemStack current = event.getCurrentItem();
        if (current == null) {
            return;
        }
        if (current.getEnchantments().isEmpty()) {
            if (current.getType().equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) current.getItemMeta();
                if (meta != null) {
                    event.setCurrentItem(this.checkEnchant(meta.getStoredEnchants(), (Player) event.getWhoClicked(), current));
                }
            }
        } else {
            event.setCurrentItem(this.checkEnchant(current.getEnchantments(), (Player) event.getWhoClicked(), current));
        }
    }

    private ItemStack checkEnchant(Map<Enchantment, Integer> enchant, Player player, ItemStack item) {
        Map<Enchantment, Integer> tempEnchant = new HashMap<Enchantment, Integer>();
        ItemStack result = new ItemStack(item);
        for (Enchantment e : enchant.keySet()) {
            result.removeEnchantment(e);
            if (limitedEnchants.containsKey(e)) {
                tempEnchant.put(e, Math.min(enchant.get(e), limitedEnchants.get(e)));
            } else {
                tempEnchant.put(e, enchant.get(e));
            }
        }

        if (!result.getType().equals(Material.ENCHANTED_BOOK) && !result.getType().equals(Material.BOOK)) {
            result.addUnsafeEnchantments(tempEnchant);
        } else if (!tempEnchant.isEmpty()) {
            result = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
            if (meta != null) {
                for (Enchantment e2 : tempEnchant.keySet()) {
                    meta.addStoredEnchant(e2, tempEnchant.get(e2), false);
                }
                result.setItemMeta(meta);
            }
        } else {
            result = new ItemStack(Material.BOOK);
        }
        return result;
    }

}
