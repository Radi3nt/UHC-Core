package fr.radi3nt.loupgarouuhc.classes.player;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.classes.chats.Chat;
import fr.radi3nt.loupgarouuhc.classes.config.Config;
import fr.radi3nt.loupgarouuhc.classes.stats.Stats;
import fr.radi3nt.loupgarouuhc.classes.roles.Role;
import fr.radi3nt.loupgarouuhc.classes.roles.RoleType;
import fr.radi3nt.loupgarouuhc.classes.roles.WinType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static fr.radi3nt.loupgarouuhc.LoupGarouUHC.*;


public class LGPlayer {
	private static HashMap<UUID, LGPlayer> cachedPlayers = new HashMap<UUID, LGPlayer>();


	public static LGPlayer thePlayer(Player player) {
		LGPlayer lgp = cachedPlayers.get(player.getUniqueId());
		if (lgp == null) {
			lgp = new LGPlayer(player);
			cachedPlayers.put(player.getUniqueId(), lgp);
		}
		lgp.player=player;
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


	private Stats stats;
	private Player player;
	private UUID uuid;
	private boolean dead;
	private LGGame game;
	private Role role;
	private boolean canVote;
	private Chat chat;
	private LGPlayer couple;
	private boolean canBeRespawned = false;
	private Integer diamondMined = 0;
	private LGPlayer killer;
	private Integer kills = 0;


	public LGPlayer getCouple() {
		return couple;
	}

	public void setCouple(LGPlayer player) {
		couple = player;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public Integer getDiamondMined() {
		return diamondMined;
	}

	public void setDiamondMined(Integer diamondMined) {
		this.diamondMined = diamondMined;
	}


	public boolean canBeRespawned() {
		return canBeRespawned;
	}

	public void setCanBeRespawned(boolean canBeRespawned) {
		this.canBeRespawned = canBeRespawned;
	}


	public boolean canVote() {
		return canVote;
	}

	public void setCanVote(boolean canVote) {
		this.canVote = canVote;
	}


	public LGPlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		stats = new Stats();
	}

	public LGPlayer(UUID uuid) {
		this.player=null;
		this.uuid = uuid;
		stats = new Stats();
	}

	public void sendMessage(String msg) {
		if (this.player != null)
			player.sendMessage(msg);
	}

	public boolean isDead() {
		return dead;
	}

	public LGGame getGame() {
		return game;
	}

	public void setGame(LGGame game) {
		this.game = game;
	}

	public void setDead(Boolean isdead) {
		dead = isdead;
	}

	public void remove() {
		this.player = null;
	}

	public String getName() {
		return player != null ? player.getName() : Bukkit.getOfflinePlayer(uuid).getName();
	}


	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public RoleType getRoleType() {
		return role.getRoleType();
	}

	public WinType getRoleWinType() {
		return role.getWinType();
	}

	@Override
	public String toString() {
		return super.toString() + " (" + getName() + ")";
	}

	public void sendTitle(String s, String s1, int i, int i1, int i2) {
		this.player.sendTitle(s, s1, i, i1, i2);
	}

	public Player getPlayer() {
		return player;
	}

	public static LGPlayer removePlayer(Player player) {
		return cachedPlayers.remove(player.getName());//.remove();
	}

	public LGPlayer getKiller() {
		return killer;
	}

	public void setKiller(LGPlayer killer) {
		this.killer = killer;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Integer getKills() {
		return kills;
	}

	public void setKills(Integer kills) {
		this.kills = kills;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void saveStats() {
		Config config = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "/players", getName()+".yml");


		config.getConfiguration().set(getName() + ".games", this.getStats().getGameNumber());
		config.getConfiguration().set(getName() + ".wins", this.getStats().getWinnedGames());
		config.getConfiguration().set(getName() + ".kills", this.getStats().getKills());
		config.getConfiguration().set(getName() + ".points", this.getStats().getPoints());
		config.saveConfig();

		Config config1 = fr.radi3nt.loupgarouuhc.classes.config.Config.createConfig(plugin.getDataFolder() + "", "players.yml");


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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof LGPlayer && ((LGPlayer) obj).getUuid() == this.getUuid();
	}
}
