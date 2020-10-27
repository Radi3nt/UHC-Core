package fr.radi3nt.loupgarouuhc.commands;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.FinalHeal;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
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
                return null;
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
                return null;
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
        FinalHeal finalHeal = new FinalHeal(new LGGame(LoupGarouUHC.parameters));
        finalHeal.activate();
        finalHeal.onCommand(commandSender, command, "uhc", strings);
    }

}
