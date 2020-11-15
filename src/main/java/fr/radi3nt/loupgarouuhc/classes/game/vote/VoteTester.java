package fr.radi3nt.loupgarouuhc.classes.game.vote;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.PlayerVote;
import fr.radi3nt.uhc.api.scenarios.scenario.vote.Vote;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class VoteTester {

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
                ArrayList<World> world = new ArrayList<>();
                world.add(new World() {
                    @Override
                    public Block getBlockAt(int i, int i1, int i2) {
                        return null;
                    }

                    @Override
                    public Block getBlockAt(Location location) {
                        return null;
                    }

                    @Override
                    public int getBlockTypeIdAt(int i, int i1, int i2) {
                        return 0;
                    }

                    @Override
                    public int getBlockTypeIdAt(Location location) {
                        return 0;
                    }

                    @Override
                    public int getHighestBlockYAt(int i, int i1) {
                        return 0;
                    }

                    @Override
                    public int getHighestBlockYAt(Location location) {
                        return 0;
                    }

                    @Override
                    public Block getHighestBlockAt(int i, int i1) {
                        return null;
                    }

                    @Override
                    public Block getHighestBlockAt(Location location) {
                        return null;
                    }

                    @Override
                    public Chunk getChunkAt(int i, int i1) {
                        return null;
                    }

                    @Override
                    public Chunk getChunkAt(Location location) {
                        return null;
                    }

                    @Override
                    public Chunk getChunkAt(Block block) {
                        return null;
                    }

                    @Override
                    public boolean isChunkLoaded(Chunk chunk) {
                        return false;
                    }

                    @Override
                    public Chunk[] getLoadedChunks() {
                        return new Chunk[0];
                    }

                    @Override
                    public void loadChunk(Chunk chunk) {

                    }

                    @Override
                    public boolean isChunkLoaded(int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean isChunkInUse(int i, int i1) {
                        return false;
                    }

                    @Override
                    public void loadChunk(int i, int i1) {

                    }

                    @Override
                    public boolean loadChunk(int i, int i1, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunk(Chunk chunk) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunk(int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunk(int i, int i1, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunk(int i, int i1, boolean b, boolean b1) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunkRequest(int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean unloadChunkRequest(int i, int i1, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean regenerateChunk(int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean refreshChunk(int i, int i1) {
                        return false;
                    }

                    @Override
                    public Item dropItem(Location location, ItemStack itemStack) {
                        return null;
                    }

                    @Override
                    public Item dropItemNaturally(Location location, ItemStack itemStack) {
                        return null;
                    }

                    @Override
                    public Arrow spawnArrow(Location location, Vector vector, float v, float v1) {
                        return null;
                    }

                    @Override
                    public <T extends Arrow> T spawnArrow(Location location, Vector vector, float v, float v1, Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public boolean generateTree(Location location, TreeType treeType) {
                        return false;
                    }

                    @Override
                    public boolean generateTree(Location location, TreeType treeType, BlockChangeDelegate blockChangeDelegate) {
                        return false;
                    }

                    @Override
                    public Entity spawnEntity(Location location, EntityType entityType) {
                        return null;
                    }

                    @Override
                    public LightningStrike strikeLightning(Location location) {
                        return null;
                    }

                    @Override
                    public LightningStrike strikeLightningEffect(Location location) {
                        return null;
                    }

                    @Override
                    public List<Entity> getEntities() {
                        return null;
                    }

                    @Override
                    public List<LivingEntity> getLivingEntities() {
                        return null;
                    }

                    @Override
                    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
                        return null;
                    }

                    @Override
                    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
                        return null;
                    }

                    @Override
                    public List<Player> getPlayers() {
                        return null;
                    }

                    @Override
                    public Collection<Entity> getNearbyEntities(Location location, double v, double v1, double v2) {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public UUID getUID() {
                        return null;
                    }

                    @Override
                    public Location getSpawnLocation() {
                        return null;
                    }

                    @Override
                    public boolean setSpawnLocation(Location location) {
                        return false;
                    }

                    @Override
                    public boolean setSpawnLocation(int i, int i1, int i2) {
                        return false;
                    }

                    @Override
                    public long getTime() {
                        return 0;
                    }

                    @Override
                    public void setTime(long l) {

                    }

                    @Override
                    public long getFullTime() {
                        return 0;
                    }

                    @Override
                    public void setFullTime(long l) {

                    }

                    @Override
                    public boolean hasStorm() {
                        return false;
                    }

                    @Override
                    public void setStorm(boolean b) {

                    }

                    @Override
                    public int getWeatherDuration() {
                        return 0;
                    }

                    @Override
                    public void setWeatherDuration(int i) {

                    }

                    @Override
                    public boolean isThundering() {
                        return false;
                    }

                    @Override
                    public void setThundering(boolean b) {

                    }

                    @Override
                    public int getThunderDuration() {
                        return 0;
                    }

                    @Override
                    public void setThunderDuration(int i) {

                    }

                    @Override
                    public boolean createExplosion(double v, double v1, double v2, float v3) {
                        return false;
                    }

                    @Override
                    public boolean createExplosion(double v, double v1, double v2, float v3, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1) {
                        return false;
                    }

                    @Override
                    public boolean createExplosion(Location location, float v) {
                        return false;
                    }

                    @Override
                    public boolean createExplosion(Location location, float v, boolean b) {
                        return false;
                    }

                    @Override
                    public Environment getEnvironment() {
                        return null;
                    }

                    @Override
                    public long getSeed() {
                        return 0;
                    }

                    @Override
                    public boolean getPVP() {
                        return false;
                    }

                    @Override
                    public void setPVP(boolean b) {

                    }

                    @Override
                    public ChunkGenerator getGenerator() {
                        return null;
                    }

                    @Override
                    public void save() {

                    }

                    @Override
                    public List<BlockPopulator> getPopulators() {
                        return null;
                    }

                    @Override
                    public <T extends Entity> T spawn(Location location, Class<T> aClass) throws IllegalArgumentException {
                        return null;
                    }

                    @Override
                    public <T extends Entity> T spawn(Location location, Class<T> aClass, Consumer<T> consumer) throws IllegalArgumentException {
                        return null;
                    }

                    @Override
                    public FallingBlock spawnFallingBlock(Location location, MaterialData materialData) throws IllegalArgumentException {
                        return null;
                    }

                    @Override
                    public FallingBlock spawnFallingBlock(Location location, Material material, byte b) throws IllegalArgumentException {
                        return null;
                    }

                    @Override
                    public FallingBlock spawnFallingBlock(Location location, int i, byte b) throws IllegalArgumentException {
                        return null;
                    }

                    @Override
                    public void playEffect(Location location, Effect effect, int i) {

                    }

                    @Override
                    public void playEffect(Location location, Effect effect, int i, int i1) {

                    }

                    @Override
                    public <T> void playEffect(Location location, Effect effect, T t) {

                    }

                    @Override
                    public <T> void playEffect(Location location, Effect effect, T t, int i) {

                    }

                    @Override
                    public ChunkSnapshot getEmptyChunkSnapshot(int i, int i1, boolean b, boolean b1) {
                        return null;
                    }

                    @Override
                    public void setSpawnFlags(boolean b, boolean b1) {

                    }

                    @Override
                    public boolean getAllowAnimals() {
                        return false;
                    }

                    @Override
                    public boolean getAllowMonsters() {
                        return false;
                    }

                    @Override
                    public Biome getBiome(int i, int i1) {
                        return null;
                    }

                    @Override
                    public void setBiome(int i, int i1, Biome biome) {

                    }

                    @Override
                    public double getTemperature(int i, int i1) {
                        return 0;
                    }

                    @Override
                    public double getHumidity(int i, int i1) {
                        return 0;
                    }

                    @Override
                    public int getMaxHeight() {
                        return 0;
                    }

                    @Override
                    public int getSeaLevel() {
                        return 0;
                    }

                    @Override
                    public boolean getKeepSpawnInMemory() {
                        return false;
                    }

                    @Override
                    public void setKeepSpawnInMemory(boolean b) {

                    }

                    @Override
                    public boolean isAutoSave() {
                        return false;
                    }

                    @Override
                    public void setAutoSave(boolean b) {

                    }

                    @Override
                    public void setDifficulty(Difficulty difficulty) {

                    }

                    @Override
                    public Difficulty getDifficulty() {
                        return null;
                    }

                    @Override
                    public File getWorldFolder() {
                        return null;
                    }

                    @Override
                    public WorldType getWorldType() {
                        return null;
                    }

                    @Override
                    public boolean canGenerateStructures() {
                        return false;
                    }

                    @Override
                    public long getTicksPerAnimalSpawns() {
                        return 0;
                    }

                    @Override
                    public void setTicksPerAnimalSpawns(int i) {

                    }

                    @Override
                    public long getTicksPerMonsterSpawns() {
                        return 0;
                    }

                    @Override
                    public void setTicksPerMonsterSpawns(int i) {

                    }

                    @Override
                    public int getMonsterSpawnLimit() {
                        return 0;
                    }

                    @Override
                    public void setMonsterSpawnLimit(int i) {

                    }

                    @Override
                    public int getAnimalSpawnLimit() {
                        return 0;
                    }

                    @Override
                    public void setAnimalSpawnLimit(int i) {

                    }

                    @Override
                    public int getWaterAnimalSpawnLimit() {
                        return 0;
                    }

                    @Override
                    public void setWaterAnimalSpawnLimit(int i) {

                    }

                    @Override
                    public int getAmbientSpawnLimit() {
                        return 0;
                    }

                    @Override
                    public void setAmbientSpawnLimit(int i) {

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
                    public String[] getGameRules() {
                        return new String[0];
                    }

                    @Override
                    public String getGameRuleValue(String s) {
                        return null;
                    }

                    @Override
                    public boolean setGameRuleValue(String s, String s1) {
                        return false;
                    }

                    @Override
                    public boolean isGameRule(String s) {
                        return false;
                    }

                    @Override
                    public WorldBorder getWorldBorder() {
                        return null;
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
                    public Spigot spigot() {
                        return null;
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
                    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

                    }

                    @Override
                    public Set<String> getListeningPluginChannels() {
                        return null;
                    }
                });
                return world;
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
                return new OfflinePlayer() {
                    @Override
                    public boolean isOnline() {
                        return false;
                    }

                    @Override
                    public String getName() {
                        return "Red";
                    }

                    @Override
                    public UUID getUniqueId() {
                        return new UUID(2, 2);
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
                        return true;
                    }

                    @Override
                    public Location getBedSpawnLocation() {
                        return null;
                    }

                    @Override
                    public Map<String, Object> serialize() {
                        return null;
                    }

                    @Override
                    public boolean isOp() {
                        return false;
                    }

                    @Override
                    public void setOp(boolean b) {

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
    public void voteTestNormal() {
        HashMap<Vote, Integer> integerHashMap = new HashMap<>();
        integerHashMap.put(new Vote(UHCPlayer.thePlayer(Bukkit.getOfflinePlayer("Red_white_200").getUniqueId()), UHCPlayer.thePlayer(Bukkit.getOfflinePlayer("Red_white_200").getUniqueId())), 1);
        integerHashMap.put(new Vote(UHCPlayer.thePlayer(Bukkit.getOfflinePlayer("Red_white_200").getUniqueId()), UHCPlayer.thePlayer(Bukkit.getOfflinePlayer("YiraSan").getUniqueId())), 2);

        LGGame game = new LGGame(null);
        new VoteManager(game, new PlayerVote(game)).proceedVote(integerHashMap);
    }
}
