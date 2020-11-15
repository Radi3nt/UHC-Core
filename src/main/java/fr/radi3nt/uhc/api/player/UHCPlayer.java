package fr.radi3nt.uhc.api.player;

import fr.radi3nt.uhc.api.chats.Chat;
import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.stats.Stats;
import fr.radi3nt.uhc.api.utilis.Config;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class UHCPlayer {

	private static final HashMap<UUID, UHCPlayer> cachedPlayers = new HashMap<>();
	private final UUID uuid;
	private Stats stats;
	private Language language;
	private Player player;
	private final PlayerStats playerStats;
	private Chat chat;
	private PlayerGameData gameData = new NullPlayerGameData();

	private UHCPlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		stats = new Stats();
		playerStats = new PlayerStats(player.getUniqueId());
		playerStats.refresh();

		for (Language languages : Language.getLanguages()) {
			if (languages.getId().equals(Language.DEFAULTID)) {
				language = languages;
			}
		}
	}

	private UHCPlayer(UUID uuid) {
		this.player = null;
		this.uuid = uuid;
		stats = new Stats();
		playerStats = new PlayerStats(uuid);
		playerStats.refresh();

		for (Language languages : Language.getLanguages()) {
			if (languages.getId().equals(Language.DEFAULTID)) {
				language = languages;
			}
		}
	}

	public static UHCPlayer thePlayer(Player player) {
		UHCPlayer lgp = cachedPlayers.get(player.getUniqueId());
		if (lgp == null) {
			lgp = new UHCPlayer(player);
			cachedPlayers.put(player.getUniqueId(), lgp);
		}
		lgp.player = player;
		return lgp;
	}

	public static UHCPlayer thePlayer(UUID uuid) {
		UHCPlayer lgp = cachedPlayers.get(uuid);
		if (lgp == null) {
			lgp = new UHCPlayer(uuid);
			cachedPlayers.put(uuid, lgp);
		}
		return lgp;
	}

	public static UHCPlayer thePlayer(String name) {
		try {
			return thePlayer(Bukkit.getOfflinePlayer(name).getUniqueId());
		} catch (NullPointerException e) {
			return null;
		}
	}

	public static UHCPlayer removePlayer(Player player) {
		UHCPlayer.thePlayer(player).playerStats.delete();
		return cachedPlayers.remove(player.getUniqueId());
	}


	public void sendMessage(String msg) {
		if (isOnline() && player.isOnline())
			player.sendMessage(msg);
		else
			playerStats.addMessages(msg);
	}

	public void sendIdMessage(String id) {
		try {
			sendMessage(language.getMessage(id));
		} catch (CannotFindMessageException e) {
			sendMessage(Language.NO_MESSAGE);
		}
	}

	public void sendTitle(String s, String s1, int i, int i1, int i2) {
		this.player.sendTitle(s, s1, i, i1, i2);
	}

	public void saveStats() {
		Config config = Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/players", getName() + ".yml");


		config.getConfiguration().set("Stats" + ".games", this.getStats().getGameNumber());
		config.getConfiguration().set("Stats" + ".wins", this.getStats().getWinnedGames());
		config.getConfiguration().set("Stats" + ".kills", this.getStats().getKills());
		config.getConfiguration().set("Stats" + ".points", this.getStats().getPoints());
		config.saveConfig();

		Config config1 = Config.createConfig(UHCCore.getPlugin().getDataFolder() + "", "players.yml");


		ArrayList<String> arrayList = new ArrayList<>();
		try {
			arrayList = (ArrayList<String>) config1.getConfiguration().getStringList("Players");
		} catch (NullPointerException e) {

		}
		if (!arrayList.contains(getName())) {
			arrayList.add(getName());
		}
		config1.getConfiguration().set("Players", arrayList);

		config1.saveConfig();
	}

	public void loadStats() {
		Config config = Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/players", player.getName() + ".yml");

		Stats stats = new Stats();
		stats.setGameNumber(config.getConfiguration().getInt("Stats" + ".games"));
		stats.setWinnedGames(config.getConfiguration().getInt("Stats" + ".wins"));
		stats.setKills(config.getConfiguration().getInt("Stats" + ".kills"));
		stats.setPoints(config.getConfiguration().getInt("Stats" + ".points"));
		setStats(stats);
	}

	public void saveLang() {
		Config config = Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/players", getName() + ".yml");
		config.getConfiguration().set("Lang", language.getId());
		config.saveConfig();
	}

	public void loadSavedLang() {
		Config config = Config.createConfig(UHCCore.getPlugin().getDataFolder() + "/players", getName() + ".yml");
		String id = config.getConfiguration().getString("Lang");

		if (id == null || id.equals(Language.DEFAULTID)) {
			config.getConfiguration().set("Lang", UHCCore.DEFAULT_LANG_ID);
			config.saveConfig();
			id = UHCCore.DEFAULT_LANG_ID;
		}

		for (Language value : Language.getLanguages()) {
			if (id.equals(value.getId()))
				language = value;
		}

		if (language.getId().equals(Language.DEFAULTID)) {
			for (Language value : Language.getLanguages()) {
				if (value.getId().equals(UHCCore.DEFAULT_LANG_ID))
					language = value;
			}
		}
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public void remove() {
		this.player = null;
	}

	public String getName() {
		return player != null ? player.getName() : Bukkit.getOfflinePlayer(uuid).getName();
	}

	public Player getPlayer() {
		return player;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isInGame() {
		return gameData.getGame() != null;
	}

	public boolean isOnline() {
		return player != null && player.isOnline();
	}

	public PlayerGameData getGameData() {
		return gameData;
	}

	public void setGameData(PlayerGameData gameData) {
		this.gameData = gameData;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + getName() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof UHCPlayer && ((UHCPlayer) obj).getUuid() == this.getUuid();
	}

}
