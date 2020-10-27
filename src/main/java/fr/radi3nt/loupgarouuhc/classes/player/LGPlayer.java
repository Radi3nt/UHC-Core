package fr.radi3nt.loupgarouuhc.classes.player;

import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.stats.Stats;
import fr.radi3nt.loupgarouuhc.utilis.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class LGPlayer {

	private static final HashMap<UUID, LGPlayer> cachedPlayers = new HashMap<UUID, LGPlayer>();
	private final UUID uuid;
	private Stats stats;
	private Languages language;
	private Player player;
	private Chat chat;
	private PlayerGameData gameData = new PlayerGameData(null);

	public static LGPlayer thePlayer(Player player) {
		LGPlayer lgp = cachedPlayers.get(player.getUniqueId());
		if (lgp == null) {
			lgp = new LGPlayer(player);
			cachedPlayers.put(player.getUniqueId(), lgp);
		}
		lgp.player = player;
		return lgp;
	}

	public static LGPlayer thePlayer(UUID uuid) {
		LGPlayer lgp = cachedPlayers.get(uuid);
		if (lgp == null) {
			lgp = new LGPlayer(uuid);
			cachedPlayers.put(uuid, lgp);
		}
		return lgp;
	}

	private LGPlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		stats = new Stats();

		for (Languages languages : Languages.getLanguages()) {
			if (languages.getId().equals(Languages.DEFAULTID)) {
				language = languages;
			}
		}
	}

	private LGPlayer(UUID uuid) {
		this.player = null;
		this.uuid = uuid;
		stats = new Stats();

		for (Languages languages : Languages.getLanguages()) {
			if (languages.getId().equals(Languages.DEFAULTID)) {
				language = languages;
			}
		}
	}

	public static LGPlayer removePlayer(Player player) {
		return cachedPlayers.remove(player.getUniqueId());//.remove();
	}


	public void sendMessage(String msg) {
		if (this.player != null)
			player.sendMessage(msg);
	}

	public void sendTitle(String s, String s1, int i, int i1, int i2) {
		this.player.sendTitle(s, s1, i, i1, i2);
	}

	public void saveStats() {
		Config config = Config.createConfig(LoupGarouUHC.getPlugin().getDataFolder() + "/players", getName() + ".yml");


		config.getConfiguration().set("Stats" + ".games", this.getStats().getGameNumber());
		config.getConfiguration().set("Stats" + ".wins", this.getStats().getWinnedGames());
		config.getConfiguration().set("Stats" + ".kills", this.getStats().getKills());
		config.getConfiguration().set("Stats" + ".points", this.getStats().getPoints());
		config.saveConfig();

		Config config1 = Config.createConfig(LoupGarouUHC.getPlugin().getDataFolder() + "", "players.yml");


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
		Config config = Config.createConfig(LoupGarouUHC.getPlugin().getDataFolder() + "/players", player.getName() + ".yml");

		Stats stats = new Stats();
		stats.setGameNumber(config.getConfiguration().getInt("Stats" + ".games"));
		stats.setWinnedGames(config.getConfiguration().getInt("Stats" + ".wins"));
		stats.setKills(config.getConfiguration().getInt("Stats" + ".kills"));
		stats.setPoints(config.getConfiguration().getInt("Stats" + ".points"));
		setStats(stats);
	}

	public void saveLang() {
		Config config = Config.createConfig(LoupGarouUHC.getPlugin().getDataFolder() + "/players", getName() + ".yml");
		config.getConfiguration().set("Lang", language.getId());
		config.saveConfig();
	}

	public void loadSavedLang() {
		Config config = Config.createConfig(LoupGarouUHC.getPlugin().getDataFolder() + "/players", getName() + ".yml");

		String id = config.getConfiguration().getString("Lang");

		if (id == null) {
			config.getConfiguration().set("Lang", "fr");
			config.saveConfig();
			id = "fr";
		}

		for (Languages value : Languages.getLanguages()) {
			if (id.equals(value.getId()))
				language = value;
		}
		if (language.getId().equals(Languages.DEFAULTID)) {
			for (Languages value : Languages.getLanguages()) {
				if (id.equals("fr"))
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

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

	public boolean isInGame() {
		return gameData.getGame() != null;
	}

	public boolean isLinkedToPlayer() {
		return player != null;
	}

	public PlayerGameData getGameData() {
		return gameData;
	}

	public void setGameData(PlayerGameData gameData) {
		this.gameData = gameData;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + getName() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof LGPlayer && ((LGPlayer) obj).getUuid() == this.getUuid();
	}

}
