package fr.radi3nt.loupgarouuhc.commands;

import fr.radi3nt.uhc.api.utilis.AnnouncementUtils;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Logger;

public class TestCommand {

    @Before
    public void before() {
        Server server = new Server() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getVersion() {
                return "";
            }

            @Override
            public String getBukkitVersion() {
                return "";
            }

            @Override
            public Collection<? extends Player> getOnlinePlayers() {
                return null;
            }

            @Override
            public int getMaxPlayers() {
                return 0;
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public int getViewDistance() {
                return 0;
            }

            @Override
            public String getIp() {
                return "";
            }

            @Override
            public String getServerName() {
                return "";
            }

            @Override
            public String getServerId() {
                return "";
            }

            @Override
            public String getWorldType() {
                return "FLAT";
            }

            @Override
            public boolean getGenerateStructures() {
                return false;
            }

            @Override
            public boolean getAllowEnd() {
                return false;
            }

            @Override
            public boolean getAllowNether() {
                return false;
            }

            @Override
            public boolean hasWhitelist() {
                return false;
            }

            @Override
            public void setWhitelist(boolean b) {

            }

            @Override
            public Set<OfflinePlayer> getWhitelistedPlayers() {
                return null;
            }

            @Override
            public void reloadWhitelist() {

            }

            @Override
            public int broadcastMessage(String s) {
                System.out.println("Broadcast:" + s);
                return 0;
            }

            @Override
            public String getUpdateFolder() {
                return null;
            }

            @Override
            public File getUpdateFolderFile() {
                return null;
            }

            @Override
            public long getConnectionThrottle() {
                return 0;
            }

            @Override
            public int getTicksPerAnimalSpawns() {
                return 0;
            }

            @Override
            public int getTicksPerMonsterSpawns() {
                return 0;
            }

            @Override
            public Player getPlayer(String s) {
                return null;
            }

            @Override
            public Player getPlayerExact(String s) {
                return null;
            }

            @Override
            public List<Player> matchPlayer(String s) {
                return null;
            }

            @Override
            public Player getPlayer(UUID uuid) {
                return null;
            }

            @Override
            public PluginManager getPluginManager() {
                return null;
            }

            @Override
            public BukkitScheduler getScheduler() {
                return null;
            }

            @Override
            public ServicesManager getServicesManager() {
                return null;
            }

            @Override
            public List<World> getWorlds() {
                return new ArrayList<>();
            }

            @Override
            public World createWorld(WorldCreator worldCreator) {
                return null;
            }

            @Override
            public boolean unloadWorld(String s, boolean b) {
                return false;
            }

            @Override
            public boolean unloadWorld(World world, boolean b) {
                return false;
            }

            @Override
            public World getWorld(String s) {
                return null;
            }

            @Override
            public World getWorld(UUID uuid) {
                return null;
            }

            @Override
            public MapView getMap(short i) {
                return null;
            }

            @Override
            public MapView createMap(World world) {
                return null;
            }

            @Override
            public void reload() {

            }

            @Override
            public void reloadData() {

            }

            @Override
            public Logger getLogger() {
                return Logger.getGlobal();
            }

            @Override
            public PluginCommand getPluginCommand(String s) {
                return null;
            }

            @Override
            public void savePlayers() {

            }

            @Override
            public boolean dispatchCommand(CommandSender commandSender, String s) throws CommandException {
                return false;
            }

            @Override
            public boolean addRecipe(Recipe recipe) {
                return false;
            }

            @Override
            public List<Recipe> getRecipesFor(ItemStack itemStack) {
                return null;
            }

            @Override
            public Iterator<Recipe> recipeIterator() {
                return null;
            }

            @Override
            public void clearRecipes() {

            }

            @Override
            public void resetRecipes() {

            }

            @Override
            public Map<String, String[]> getCommandAliases() {
                return null;
            }

            @Override
            public int getSpawnRadius() {
                return 0;
            }

            @Override
            public void setSpawnRadius(int i) {

            }

            @Override
            public boolean getOnlineMode() {
                return false;
            }

            @Override
            public boolean getAllowFlight() {
                return false;
            }

            @Override
            public boolean isHardcore() {
                return false;
            }

            @Override
            public void shutdown() {

            }

            @Override
            public int broadcast(String s, String s1) {
                System.out.println("Broadcast: " + s + "/" + s1);
                return 0;
            }

            @Override
            public OfflinePlayer getOfflinePlayer(String s) {
                return new Player() {
                    @Override
                    public String getDisplayName() {
                        return null;
                    }

                    @Override
                    public void setDisplayName(String s) {

                    }

                    @Override
                    public String getPlayerListName() {
                        return null;
                    }

                    @Override
                    public void setPlayerListName(String s) {

                    }

                    @Override
                    public void setCompassTarget(Location location) {

                    }

                    @Override
                    public Location getCompassTarget() {
                        return null;
                    }

                    @Override
                    public InetSocketAddress getAddress() {
                        return null;
                    }

                    @Override
                    public void sendRawMessage(String s) {

                    }

                    @Override
                    public void kickPlayer(String s) {

                    }

                    @Override
                    public void chat(String s) {

                    }

                    @Override
                    public boolean performCommand(String s) {
                        return false;
                    }

                    @Override
                    public boolean isSneaking() {
                        return false;
                    }

                    @Override
                    public void setSneaking(boolean b) {

                    }

                    @Override
                    public boolean isSprinting() {
                        return false;
                    }

                    @Override
                    public void setSprinting(boolean b) {

                    }

                    @Override
                    public void saveData() {

                    }

                    @Override
                    public void loadData() {

                    }

                    @Override
                    public void setSleepingIgnored(boolean b) {

                    }

                    @Override
                    public boolean isSleepingIgnored() {
                        return false;
                    }

                    @Override
                    public void playNote(Location location, byte b, byte b1) {

                    }

                    @Override
                    public void playNote(Location location, Instrument instrument, Note note) {

                    }

                    @Override
                    public void playSound(Location location, Sound sound, float v, float v1) {

                    }

                    @Override
                    public void playSound(Location location, String s, float v, float v1) {

                    }

                    @Override
                    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {

                    }

                    @Override
                    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {

                    }

                    @Override
                    public void stopSound(Sound sound) {

                    }

                    @Override
                    public void stopSound(String s) {

                    }

                    @Override
                    public void stopSound(Sound sound, SoundCategory soundCategory) {

                    }

                    @Override
                    public void stopSound(String s, SoundCategory soundCategory) {

                    }

                    @Override
                    public void playEffect(Location location, Effect effect, int i) {

                    }

                    @Override
                    public <T> void playEffect(Location location, Effect effect, T t) {

                    }

                    @Override
                    public void sendBlockChange(Location location, Material material, byte b) {

                    }

                    @Override
                    public boolean sendChunkChange(Location location, int i, int i1, int i2, byte[] bytes) {
                        return false;
                    }

                    @Override
                    public void sendBlockChange(Location location, int i, byte b) {

                    }

                    @Override
                    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException {

                    }

                    @Override
                    public void sendMap(MapView mapView) {

                    }

                    @Override
                    public void updateInventory() {

                    }

                    @Override
                    public void awardAchievement(Achievement achievement) {

                    }

                    @Override
                    public void removeAchievement(Achievement achievement) {

                    }

                    @Override
                    public boolean hasAchievement(Achievement achievement) {
                        return false;
                    }

                    @Override
                    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {

                    }

                    @Override
                    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
                        return 0;
                    }

                    @Override
                    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

                    }

                    @Override
                    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
                        return 0;
                    }

                    @Override
                    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

                    }

                    @Override
                    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
                        return 0;
                    }

                    @Override
                    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {

                    }

                    @Override
                    public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {

                    }

                    @Override
                    public void setStatistic(Statistic statistic, EntityType entityType, int i) {

                    }

                    @Override
                    public void setPlayerTime(long l, boolean b) {

                    }

                    @Override
                    public long getPlayerTime() {
                        return 0;
                    }

                    @Override
                    public long getPlayerTimeOffset() {
                        return 0;
                    }

                    @Override
                    public boolean isPlayerTimeRelative() {
                        return false;
                    }

                    @Override
                    public void resetPlayerTime() {

                    }

                    @Override
                    public void setPlayerWeather(WeatherType weatherType) {

                    }

                    @Override
                    public WeatherType getPlayerWeather() {
                        return null;
                    }

                    @Override
                    public void resetPlayerWeather() {

                    }

                    @Override
                    public void giveExp(int i) {

                    }

                    @Override
                    public void giveExpLevels(int i) {

                    }

                    @Override
                    public float getExp() {
                        return 0;
                    }

                    @Override
                    public void setExp(float v) {

                    }

                    @Override
                    public int getLevel() {
                        return 0;
                    }

                    @Override
                    public void setLevel(int i) {

                    }

                    @Override
                    public int getTotalExperience() {
                        return 0;
                    }

                    @Override
                    public void setTotalExperience(int i) {

                    }

                    @Override
                    public float getExhaustion() {
                        return 0;
                    }

                    @Override
                    public void setExhaustion(float v) {

                    }

                    @Override
                    public float getSaturation() {
                        return 0;
                    }

                    @Override
                    public void setSaturation(float v) {

                    }

                    @Override
                    public int getFoodLevel() {
                        return 0;
                    }

                    @Override
                    public void setFoodLevel(int i) {

                    }

                    @Override
                    public Location getBedSpawnLocation() {
                        return null;
                    }

                    @Override
                    public void setBedSpawnLocation(Location location) {

                    }

                    @Override
                    public void setBedSpawnLocation(Location location, boolean b) {

                    }

                    @Override
                    public boolean getAllowFlight() {
                        return false;
                    }

                    @Override
                    public void setAllowFlight(boolean b) {

                    }

                    @Override
                    public void hidePlayer(Player player) {

                    }

                    @Override
                    public void hidePlayer(Plugin plugin, Player player) {

                    }

                    @Override
                    public void showPlayer(Player player) {

                    }

                    @Override
                    public void showPlayer(Plugin plugin, Player player) {

                    }

                    @Override
                    public boolean canSee(Player player) {
                        return false;
                    }

                    @Override
                    public boolean isFlying() {
                        return false;
                    }

                    @Override
                    public void setFlying(boolean b) {

                    }

                    @Override
                    public void setFlySpeed(float v) throws IllegalArgumentException {

                    }

                    @Override
                    public void setWalkSpeed(float v) throws IllegalArgumentException {

                    }

                    @Override
                    public float getFlySpeed() {
                        return 0;
                    }

                    @Override
                    public float getWalkSpeed() {
                        return 0;
                    }

                    @Override
                    public void setTexturePack(String s) {

                    }

                    @Override
                    public void setResourcePack(String s) {

                    }

                    @Override
                    public void setResourcePack(String s, byte[] bytes) {

                    }

                    @Override
                    public Scoreboard getScoreboard() {
                        return null;
                    }

                    @Override
                    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {

                    }

                    @Override
                    public boolean isHealthScaled() {
                        return false;
                    }

                    @Override
                    public void setHealthScaled(boolean b) {

                    }

                    @Override
                    public void setHealthScale(double v) throws IllegalArgumentException {

                    }

                    @Override
                    public double getHealthScale() {
                        return 0;
                    }

                    @Override
                    public Entity getSpectatorTarget() {
                        return null;
                    }

                    @Override
                    public void setSpectatorTarget(Entity entity) {

                    }

                    @Override
                    public void sendTitle(String s, String s1) {

                    }

                    @Override
                    public void sendTitle(String s, String s1, int i, int i1, int i2) {

                    }

                    @Override
                    public void resetTitle() {

                    }

                    @Override
                    public void spawnParticle(Particle particle, Location location, int i) {

                    }

                    @Override
                    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {

                    }

                    @Override
                    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {

                    }

                    @Override
                    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {

                    }

                    @Override
                    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {

                    }

                    @Override
                    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {

                    }

                    @Override
                    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {

                    }

                    @Override
                    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
                        return null;
                    }

                    @Override
                    public String getLocale() {
                        return null;
                    }

                    @Override
                    public Spigot spigot() {
                        return null;
                    }

                    @Override
                    public boolean isOnline() {
                        return false;
                    }

                    @Override
                    public boolean isBanned() {
                        return false;
                    }

                    @Override
                    public boolean isWhitelisted() {
                        return false;
                    }

                    @Override
                    public void setWhitelisted(boolean b) {

                    }

                    @Override
                    public Player getPlayer() {
                        return null;
                    }

                    @Override
                    public long getFirstPlayed() {
                        return 0;
                    }

                    @Override
                    public long getLastPlayed() {
                        return 0;
                    }

                    @Override
                    public boolean hasPlayedBefore() {
                        return false;
                    }

                    @Override
                    public Map<String, Object> serialize() {
                        return null;
                    }

                    @Override
                    public boolean isConversing() {
                        return false;
                    }

                    @Override
                    public void acceptConversationInput(String s) {

                    }

                    @Override
                    public boolean beginConversation(Conversation conversation) {
                        return false;
                    }

                    @Override
                    public void abandonConversation(Conversation conversation) {

                    }

                    @Override
                    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {

                    }

                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public PlayerInventory getInventory() {
                        return null;
                    }

                    @Override
                    public Inventory getEnderChest() {
                        return null;
                    }

                    @Override
                    public MainHand getMainHand() {
                        return null;
                    }

                    @Override
                    public boolean setWindowProperty(InventoryView.Property property, int i) {
                        return false;
                    }

                    @Override
                    public InventoryView getOpenInventory() {
                        return null;
                    }

                    @Override
                    public InventoryView openInventory(Inventory inventory) {
                        return null;
                    }

                    @Override
                    public InventoryView openWorkbench(Location location, boolean b) {
                        return null;
                    }

                    @Override
                    public InventoryView openEnchanting(Location location, boolean b) {
                        return null;
                    }

                    @Override
                    public void openInventory(InventoryView inventoryView) {

                    }

                    @Override
                    public InventoryView openMerchant(Villager villager, boolean b) {
                        return null;
                    }

                    @Override
                    public InventoryView openMerchant(Merchant merchant, boolean b) {
                        return null;
                    }

                    @Override
                    public void closeInventory() {

                    }

                    @Override
                    public ItemStack getItemInHand() {
                        return null;
                    }

                    @Override
                    public void setItemInHand(ItemStack itemStack) {

                    }

                    @Override
                    public ItemStack getItemOnCursor() {
                        return null;
                    }

                    @Override
                    public void setItemOnCursor(ItemStack itemStack) {

                    }

                    @Override
                    public boolean hasCooldown(Material material) {
                        return false;
                    }

                    @Override
                    public int getCooldown(Material material) {
                        return 0;
                    }

                    @Override
                    public void setCooldown(Material material, int i) {

                    }

                    @Override
                    public boolean isSleeping() {
                        return false;
                    }

                    @Override
                    public int getSleepTicks() {
                        return 0;
                    }

                    @Override
                    public GameMode getGameMode() {
                        return null;
                    }

                    @Override
                    public void setGameMode(GameMode gameMode) {

                    }

                    @Override
                    public boolean isBlocking() {
                        return false;
                    }

                    @Override
                    public boolean isHandRaised() {
                        return false;
                    }

                    @Override
                    public int getExpToLevel() {
                        return 0;
                    }

                    @Override
                    public Entity getShoulderEntityLeft() {
                        return null;
                    }

                    @Override
                    public void setShoulderEntityLeft(Entity entity) {

                    }

                    @Override
                    public Entity getShoulderEntityRight() {
                        return null;
                    }

                    @Override
                    public void setShoulderEntityRight(Entity entity) {

                    }

                    @Override
                    public double getEyeHeight() {
                        return 0;
                    }

                    @Override
                    public double getEyeHeight(boolean b) {
                        return 0;
                    }

                    @Override
                    public Location getEyeLocation() {
                        return null;
                    }

                    @Override
                    public List<Block> getLineOfSight(Set<Material> set, int i) {
                        return null;
                    }

                    @Override
                    public Block getTargetBlock(Set<Material> set, int i) {
                        return null;
                    }

                    @Override
                    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
                        return null;
                    }

                    @Override
                    public int getRemainingAir() {
                        return 0;
                    }

                    @Override
                    public void setRemainingAir(int i) {

                    }

                    @Override
                    public int getMaximumAir() {
                        return 0;
                    }

                    @Override
                    public void setMaximumAir(int i) {

                    }

                    @Override
                    public int getMaximumNoDamageTicks() {
                        return 0;
                    }

                    @Override
                    public void setMaximumNoDamageTicks(int i) {

                    }

                    @Override
                    public double getLastDamage() {
                        return 0;
                    }

                    @Override
                    public void setLastDamage(double v) {

                    }

                    @Override
                    public int getNoDamageTicks() {
                        return 0;
                    }

                    @Override
                    public void setNoDamageTicks(int i) {

                    }

                    @Override
                    public Player getKiller() {
                        return null;
                    }

                    @Override
                    public boolean addPotionEffect(PotionEffect potionEffect) {
                        return false;
                    }

                    @Override
                    public boolean addPotionEffect(PotionEffect potionEffect, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean addPotionEffects(Collection<PotionEffect> collection) {
                        return false;
                    }

                    @Override
                    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
                        return false;
                    }

                    @Override
                    public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
                        return null;
                    }

                    @Override
                    public void removePotionEffect(PotionEffectType potionEffectType) {

                    }

                    @Override
                    public Collection<PotionEffect> getActivePotionEffects() {
                        return null;
                    }

                    @Override
                    public boolean hasLineOfSight(Entity entity) {
                        return false;
                    }

                    @Override
                    public boolean getRemoveWhenFarAway() {
                        return false;
                    }

                    @Override
                    public void setRemoveWhenFarAway(boolean b) {

                    }

                    @Override
                    public EntityEquipment getEquipment() {
                        return null;
                    }

                    @Override
                    public void setCanPickupItems(boolean b) {

                    }

                    @Override
                    public boolean getCanPickupItems() {
                        return false;
                    }

                    @Override
                    public boolean isLeashed() {
                        return false;
                    }

                    @Override
                    public Entity getLeashHolder() throws IllegalStateException {
                        return null;
                    }

                    @Override
                    public boolean setLeashHolder(Entity entity) {
                        return false;
                    }

                    @Override
                    public boolean isGliding() {
                        return false;
                    }

                    @Override
                    public void setGliding(boolean b) {

                    }

                    @Override
                    public void setAI(boolean b) {

                    }

                    @Override
                    public boolean hasAI() {
                        return false;
                    }

                    @Override
                    public void setCollidable(boolean b) {

                    }

                    @Override
                    public boolean isCollidable() {
                        return false;
                    }

                    @Override
                    public AttributeInstance getAttribute(Attribute attribute) {
                        return null;
                    }

                    @Override
                    public void damage(double v) {

                    }

                    @Override
                    public void damage(double v, Entity entity) {

                    }

                    @Override
                    public double getHealth() {
                        return 0;
                    }

                    @Override
                    public void setHealth(double v) {

                    }

                    @Override
                    public double getMaxHealth() {
                        return 0;
                    }

                    @Override
                    public void setMaxHealth(double v) {

                    }

                    @Override
                    public void resetMaxHealth() {

                    }

                    @Override
                    public Location getLocation() {
                        return null;
                    }

                    @Override
                    public Location getLocation(Location location) {
                        return null;
                    }

                    @Override
                    public void setVelocity(Vector vector) {

                    }

                    @Override
                    public Vector getVelocity() {
                        return null;
                    }

                    @Override
                    public double getHeight() {
                        return 0;
                    }

                    @Override
                    public double getWidth() {
                        return 0;
                    }

                    @Override
                    public boolean isOnGround() {
                        return false;
                    }

                    @Override
                    public World getWorld() {
                        return null;
                    }

                    @Override
                    public boolean teleport(Location location) {
                        return false;
                    }

                    @Override
                    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
                        return false;
                    }

                    @Override
                    public boolean teleport(Entity entity) {
                        return false;
                    }

                    @Override
                    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
                        return false;
                    }

                    @Override
                    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
                        return null;
                    }

                    @Override
                    public int getEntityId() {
                        return 0;
                    }

                    @Override
                    public int getFireTicks() {
                        return 0;
                    }

                    @Override
                    public int getMaxFireTicks() {
                        return 0;
                    }

                    @Override
                    public void setFireTicks(int i) {

                    }

                    @Override
                    public void remove() {

                    }

                    @Override
                    public boolean isDead() {
                        return false;
                    }

                    @Override
                    public boolean isValid() {
                        return false;
                    }

                    @Override
                    public Server getServer() {
                        return null;
                    }

                    @Override
                    public Entity getPassenger() {
                        return null;
                    }

                    @Override
                    public boolean setPassenger(Entity entity) {
                        return false;
                    }

                    @Override
                    public List<Entity> getPassengers() {
                        return null;
                    }

                    @Override
                    public boolean addPassenger(Entity entity) {
                        return false;
                    }

                    @Override
                    public boolean removePassenger(Entity entity) {
                        return false;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean eject() {
                        return false;
                    }

                    @Override
                    public float getFallDistance() {
                        return 0;
                    }

                    @Override
                    public void setFallDistance(float v) {

                    }

                    @Override
                    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {

                    }

                    @Override
                    public EntityDamageEvent getLastDamageCause() {
                        return null;
                    }

                    @Override
                    public UUID getUniqueId() {
                        return null;
                    }

                    @Override
                    public int getTicksLived() {
                        return 0;
                    }

                    @Override
                    public void setTicksLived(int i) {

                    }

                    @Override
                    public void playEffect(EntityEffect entityEffect) {

                    }

                    @Override
                    public EntityType getType() {
                        return null;
                    }

                    @Override
                    public boolean isInsideVehicle() {
                        return false;
                    }

                    @Override
                    public boolean leaveVehicle() {
                        return false;
                    }

                    @Override
                    public Entity getVehicle() {
                        return null;
                    }

                    @Override
                    public void setCustomNameVisible(boolean b) {

                    }

                    @Override
                    public boolean isCustomNameVisible() {
                        return false;
                    }

                    @Override
                    public void setGlowing(boolean b) {

                    }

                    @Override
                    public boolean isGlowing() {
                        return false;
                    }

                    @Override
                    public void setInvulnerable(boolean b) {

                    }

                    @Override
                    public boolean isInvulnerable() {
                        return false;
                    }

                    @Override
                    public boolean isSilent() {
                        return false;
                    }

                    @Override
                    public void setSilent(boolean b) {

                    }

                    @Override
                    public boolean hasGravity() {
                        return false;
                    }

                    @Override
                    public void setGravity(boolean b) {

                    }

                    @Override
                    public int getPortalCooldown() {
                        return 0;
                    }

                    @Override
                    public void setPortalCooldown(int i) {

                    }

                    @Override
                    public Set<String> getScoreboardTags() {
                        return null;
                    }

                    @Override
                    public boolean addScoreboardTag(String s) {
                        return false;
                    }

                    @Override
                    public boolean removeScoreboardTag(String s) {
                        return false;
                    }

                    @Override
                    public PistonMoveReaction getPistonMoveReaction() {
                        return null;
                    }

                    @Override
                    public String getCustomName() {
                        return null;
                    }

                    @Override
                    public void setCustomName(String s) {

                    }

                    @Override
                    public void sendMessage(String s) {

                    }

                    @Override
                    public void sendMessage(String[] strings) {

                    }

                    @Override
                    public void setMetadata(String s, MetadataValue metadataValue) {

                    }

                    @Override
                    public List<MetadataValue> getMetadata(String s) {
                        return null;
                    }

                    @Override
                    public boolean hasMetadata(String s) {
                        return false;
                    }

                    @Override
                    public void removeMetadata(String s, Plugin plugin) {

                    }

                    @Override
                    public boolean isPermissionSet(String s) {
                        return false;
                    }

                    @Override
                    public boolean isPermissionSet(Permission permission) {
                        return false;
                    }

                    @Override
                    public boolean hasPermission(String s) {
                        return false;
                    }

                    @Override
                    public boolean hasPermission(Permission permission) {
                        return false;
                    }

                    @Override
                    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
                        return null;
                    }

                    @Override
                    public PermissionAttachment addAttachment(Plugin plugin) {
                        return null;
                    }

                    @Override
                    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
                        return null;
                    }

                    @Override
                    public PermissionAttachment addAttachment(Plugin plugin, int i) {
                        return null;
                    }

                    @Override
                    public void removeAttachment(PermissionAttachment permissionAttachment) {

                    }

                    @Override
                    public void recalculatePermissions() {

                    }

                    @Override
                    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
                        return null;
                    }

                    @Override
                    public boolean isOp() {
                        return false;
                    }

                    @Override
                    public void setOp(boolean b) {

                    }

                    @Override
                    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

                    }

                    @Override
                    public Set<String> getListeningPluginChannels() {
                        return null;
                    }

                    @Override
                    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass) {
                        return null;
                    }

                    @Override
                    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
                        return null;
                    }
                };
            }

            @Override
            public OfflinePlayer getOfflinePlayer(UUID uuid) {
                return null;
            }

            @Override
            public Set<String> getIPBans() {
                return null;
            }

            @Override
            public void banIP(String s) {

            }

            @Override
            public void unbanIP(String s) {

            }

            @Override
            public Set<OfflinePlayer> getBannedPlayers() {
                return null;
            }

            @Override
            public BanList getBanList(BanList.Type type) {
                return null;
            }

            @Override
            public Set<OfflinePlayer> getOperators() {
                return null;
            }

            @Override
            public GameMode getDefaultGameMode() {
                return null;
            }

            @Override
            public void setDefaultGameMode(GameMode gameMode) {

            }

            @Override
            public ConsoleCommandSender getConsoleSender() {
                return null;
            }

            @Override
            public File getWorldContainer() {
                return null;
            }

            @Override
            public OfflinePlayer[] getOfflinePlayers() {
                return new OfflinePlayer[0];
            }

            @Override
            public Messenger getMessenger() {
                return null;
            }

            @Override
            public HelpMap getHelpMap() {
                return null;
            }

            @Override
            public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
                return null;
            }

            @Override
            public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, String s) {
                return null;
            }

            @Override
            public Inventory createInventory(InventoryHolder inventoryHolder, int i) throws IllegalArgumentException {
                return null;
            }

            @Override
            public Inventory createInventory(InventoryHolder inventoryHolder, int i, String s) throws IllegalArgumentException {
                return null;
            }

            @Override
            public Merchant createMerchant(String s) {
                return null;
            }

            @Override
            public int getMonsterSpawnLimit() {
                return 0;
            }

            @Override
            public int getAnimalSpawnLimit() {
                return 0;
            }

            @Override
            public int getWaterAnimalSpawnLimit() {
                return 0;
            }

            @Override
            public int getAmbientSpawnLimit() {
                return 0;
            }

            @Override
            public boolean isPrimaryThread() {
                return false;
            }

            @Override
            public String getMotd() {
                return null;
            }

            @Override
            public String getShutdownMessage() {
                return null;
            }

            @Override
            public Warning.WarningState getWarningState() {
                return null;
            }

            @Override
            public ItemFactory getItemFactory() {
                return null;
            }

            @Override
            public ScoreboardManager getScoreboardManager() {
                return null;
            }

            @Override
            public CachedServerIcon getServerIcon() {
                return null;
            }

            @Override
            public CachedServerIcon loadServerIcon(File file) throws Exception {
                return null;
            }

            @Override
            public CachedServerIcon loadServerIcon(BufferedImage bufferedImage) throws Exception {
                return null;
            }

            @Override
            public int getIdleTimeout() {
                return 0;
            }

            @Override
            public void setIdleTimeout(int i) {

            }

            @Override
            public ChunkGenerator.ChunkData createChunkData(World world) {
                return null;
            }

            @Override
            public BossBar createBossBar(String s, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
                return null;
            }

            @Override
            public Entity getEntity(UUID uuid) {
                return null;
            }

            @Override
            public Advancement getAdvancement(NamespacedKey namespacedKey) {
                return null;
            }

            @Override
            public Iterator<Advancement> advancementIterator() {
                return null;
            }

            @Override
            public UnsafeValues getUnsafe() {
                return null;
            }

            @Override
            public Spigot spigot() {
                return new Spigot();
            }

            @Override
            public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

            }

            @Override
            public Set<String> getListeningPluginChannels() {
                return new HashSet<>();
            }
        };
        Bukkit.setServer(server);
    }

    @Test
    public void test() {
        CommandSender commandSender = new CommandSender() {
            @Override
            public void sendMessage(String s) {

            }

            @Override
            public void sendMessage(String[] strings) {

            }

            @Override
            public Server getServer() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Spigot spigot() {
                return null;
            }

            @Override
            public boolean isPermissionSet(String s) {
                return true;
            }

            @Override
            public boolean isPermissionSet(Permission permission) {
                return true;
            }

            @Override
            public boolean hasPermission(String s) {
                return true;
            }

            @Override
            public boolean hasPermission(Permission permission) {
                return true;
            }

            @Override
            public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
                return null;
            }

            @Override
            public PermissionAttachment addAttachment(Plugin plugin) {
                return null;
            }

            @Override
            public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
                return null;
            }

            @Override
            public PermissionAttachment addAttachment(Plugin plugin, int i) {
                return null;
            }

            @Override
            public void removeAttachment(PermissionAttachment permissionAttachment) {

            }

            @Override
            public void recalculatePermissions() {

            }

            @Override
            public Set<PermissionAttachmentInfo> getEffectivePermissions() {
                return null;
            }

            @Override
            public boolean isOp() {
                return true;
            }

            @Override
            public void setOp(boolean b) {

            }
        };
        org.bukkit.command.Command command = new Command("uhc", "yey", "ypia", new ArrayList<>()) {

            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                return false;
            }
        };
        String[] strings = new String[3];
        strings[0] = "fh";
        strings[1] = "activate";
        //FinalHeal finalHeal = new FinalHeal(new LGGame(new Parameters()));
        //finalHeal.activate();
        //finalHeal.onCommand(commandSender, command, "uhc", strings);

        //String date = LocalDateTime.of(2020, Month.NOVEMBER, 13, 10, 0, 0).toString();


        //System.out.println(LocalDateTime.parse(date).getHour());

        AnnouncementUtils.broadcastNewAnnouncements();
        //System.out.println(System.currentTimeMillis());
        //System.out.println((60 * 20 - ((System.currentTimeMillis() - 1603998723333L) / 1000) * 20));
    }

}
