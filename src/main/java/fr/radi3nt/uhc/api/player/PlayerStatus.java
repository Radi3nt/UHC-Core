package fr.radi3nt.uhc.api.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerStatus {

    private static final List<PlayerStatus> players = new ArrayList<>();

    private final UUID uuid;
    private final HashMap<PotionEffect, Long> activePotionsEffect = new HashMap<>();
    private final ArrayList<String> messages = new ArrayList<>();
    private PlayerInventory inventory;
    private Location lastLocation;
    private GameMode gameMode;
    private double health = 20;
    private double maxHealth = 20;
    private int food = 20;
    private int xp = 0;

    public PlayerStatus(UUID uuid) {
        this.uuid = uuid;
    }

    public static void updateAll() {
        for (PlayerStatus player : players) {
            player.update();
        }
    }

    /**
     * Update data -> player
     */
    public void update() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            updateGameMode();
            updateMaxHealth();
            updateHealth();
            updateFood();
            updateXp();
            applyPotionEffect();
            updateInventory();
            updateMessages();
        }
    }

    /**
     * Update: player -> data
     */
    public void refresh() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            this.setFood(player.getFoodLevel());
            this.setMaxHealth(player.getMaxHealth());
            this.setHealth(player.getHealth());
            this.setXp(player.getTotalExperience());
            this.setInventory(player.getInventory());
            this.setLastLocation(player.getLocation().clone());
            this.setGameMode(player.getGameMode());
            activePotionsEffect.clear();
            for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
                addPotionEffect(activePotionEffect);
            }
        }
    }

    public void delete() {
        players.remove(this);
    }

    public void reset() {
        health = 20;
        food = 20;
        xp = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public void updateInventory() {
        if (isOnline()) {
            Player player = getPlayer();
            player.getInventory().setContents(inventory.getContents());
            player.getInventory().setArmorContents(inventory.getArmorContents());
            player.getInventory().setExtraContents(inventory.getExtraContents());
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void updateHealth() {
        if (isOnline())
            getPlayer().setHealth(Math.min(health, getMaxHealth()));
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void updateFood() {
        if (isOnline())
            getPlayer().setFoodLevel(food);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void updateXp() {
        if (isOnline())
            getPlayer().setTotalExperience(xp);
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        ArrayList<PotionEffect> remove = new ArrayList<>();
        for (Map.Entry<PotionEffect, Long> potionEffectLongEntry : activePotionsEffect.entrySet()) {
            if (potionEffectLongEntry.getKey().getType() == potionEffect.getType()) {
                remove.add(potionEffectLongEntry.getKey());
            }
        }
        for (PotionEffect effect : remove) {
            activePotionsEffect.remove(effect);
        }
        remove.clear();
        activePotionsEffect.put(potionEffect, System.currentTimeMillis());
    }

    public void removePotionEffect(PotionEffectType potionEffect) {
        ArrayList<PotionEffect> remove = new ArrayList<>();
        for (PotionEffect potionEffectLongEntry : activePotionsEffect.keySet()) {
            if (potionEffectLongEntry.getType().equals(potionEffect)) {
                remove.add(potionEffectLongEntry);
            }
        }
        for (PotionEffect effect : remove) {
            activePotionsEffect.put(effect, 0L);
        }
    }

    public void clearPotionEffect() {
        for (PotionEffect potionEffect : activePotionsEffect.keySet()) {
            removePotionEffect(potionEffect.getType());
        }
    }

    public void applyPotionEffect() {
        if (isOnline()) {
            ArrayList<PotionEffect> remove = new ArrayList<>();
            for (Map.Entry<PotionEffect, Long> potion : activePotionsEffect.entrySet()) {

                PotionEffect effect = potion.getKey();
                long duration = (effect.getDuration() - ((System.currentTimeMillis() - potion.getValue()) / 1000) * 20);
                if (duration <= 0 || effect.getDuration() <= 0) {
                    remove.add(effect);
                    getPlayer().removePotionEffect(effect.getType());
                } else {
                    if (effect.getDuration() > 9999 * 20)
                        duration = 99999999 * 20;

                    PotionEffect potionEffect = new PotionEffect(potion.getKey().getType(), (int) duration, effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
                    getPlayer().addPotionEffect(potionEffect);
                }
            }
            for (PotionEffect potionEffect : remove) {
                activePotionsEffect.remove(potionEffect);
            }
            remove.clear();
        }
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void updateLocation() {
        if (isOnline())
            getPlayer().teleport(lastLocation);
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void updateMaxHealth() {
        if (isOnline())
            getPlayer().setMaxHealth(maxHealth);
    }

    public void addMessages(String message) {
        messages.add(message);
    }

    public void updateMessages() {
        if (isOnline()) {
            for (String message : messages) {
                getPlayer().sendMessage(message);
            }
            messages.clear();
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void updateGameMode() {
        if (isOnline()) {
            getPlayer().setGameMode(gameMode);
        }
    }
}
