package fr.radi3nt.uhc.api.scenarios.scenario;

import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    private final List<UHCPlayer> bypassedPlayer = new ArrayList<>();

    public LimitedEnchants(UHCGame game) {
        super(game);
        disabledEnchantement.add(Enchantment.ARROW_FIRE);
        disabledEnchantement.add(Enchantment.FIRE_ASPECT);
        limitedEnchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        limitedEnchants.put(Enchantment.DAMAGE_ALL, 4);

        //todo add auto assassin
    }

    public static ScenarioData getData() {
        return new ScenarioData("LimitedEnchantment").setItemStack(getItem()).setDescription("Forbid and limits enchentements");
    }

    public static ItemStack getItem() { //todo message
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        itemStack.addEnchantment(Enchantment.FIRE_ASPECT, 1);
        return itemStack;
    }

    @EventHandler
    public void event(PrepareItemEnchantEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getEnchanter());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                for (int i = 0; i < e.getOffers().length; i++) {
                    EnchantmentOffer offer = e.getOffers()[i];
                    if (offer!=null)
                    if (disabledEnchantement.contains(offer.getEnchantment())) {
                        e.getOffers()[i] = null;
                    }
                }
            }
        }
    }

    @EventHandler
    public void event(InventoryClickEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer((Player) e.getWhoClicked());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                for (int i = 0; i < player.getPlayer().getInventory().getStorageContents().length; i++) {
                    ItemStack storageContent = player.getPlayer().getInventory().getStorageContents()[i];
                    if (storageContent != null) {
                        if (storageContent.getEnchantments().isEmpty()) {
                            if (storageContent.getType().equals(Material.ENCHANTED_BOOK)) {
                                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) storageContent.getItemMeta();
                                if (meta != null) {
                                    player.getPlayer().getInventory().setItem(i, this.checkEnchant(meta.getStoredEnchants(), (Player) e.getWhoClicked(), storageContent));
                                }
                            }
                        } else {
                            player.getPlayer().getInventory().setItem(i, this.checkEnchant(storageContent.getEnchantments(), (Player) e.getWhoClicked(), storageContent));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void event(EnchantItemEvent e) {
        UHCPlayer player = UHCPlayer.thePlayer(e.getEnchanter());
        if (player.getGameData().getGame() == game) {
            if (isActive()) {
                ItemStack current = e.getItem();
                if (current == null) {
                    return;
                }
                for (Enchantment enchantment : e.getItem().getItemMeta().getEnchants().keySet()) {
                    e.getItem().removeEnchantment(enchantment);
                }
                e.getItem().getItemMeta().getEnchants().putAll((this.checkEnchant(e.getEnchantsToAdd(), e.getEnchanter(), current)).getEnchantments());

                /*
                if (current.getEnchantments().isEmpty()) {
                    if (current.getType().equals(Material.ENCHANTED_BOOK)) {
                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) current.getItemMeta();
                        if (meta != null) {
                            e.getItem().getItemMeta().getEnchants().clear();
                            e.getItem().getItemMeta().getEnchants().putAll((this.checkEnchant(e.getEnchantsToAdd(), e.getEnchanter(), current)).getEnchantments());
                        }
                    }
                } else {
                    e.getItem().getItemMeta().getEnchants().clear();
                    e.getItem().getItemMeta().getEnchants().putAll((this.checkEnchant(e.getEnchantsToAdd(), e.getEnchanter(), current)).getEnchantments());
                }

                 */
            }
        }
    }

    @EventHandler
    public void onPrepareAnvilEvent(InventoryClickEvent event) {
        UHCPlayer player = UHCPlayer.thePlayer((Player) event.getWhoClicked());
        if (player.getGameData().getGame() == game) {

            if (isActive()) {
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
        }
    }

    private ItemStack checkEnchant(Map<Enchantment, Integer> enchant, Player player, ItemStack item) {
        Map<Enchantment, Integer> tempEnchant = new HashMap<>();
        ItemStack result = new ItemStack(item);
        UHCPlayer lgp = UHCPlayer.thePlayer(player);
        if (lgp.isPlaying() && !bypassedPlayer.contains(lgp)) {
            for (Enchantment e : enchant.keySet()) {
                result.removeEnchantment(e);
                if (disabledEnchantement.contains(e)) {
                    continue;
                }

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
        }
        return result;
    }

    public List<UHCPlayer> getBypassedPlayer() {
        return bypassedPlayer;
    }
}
