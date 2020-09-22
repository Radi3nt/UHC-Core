package fr.radi3nt.loupgarouuhc.classes.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static fr.radi3nt.loupgarouuhc.classes.npc.NMSBase.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.*;

import static fr.radi3nt.loupgarouuhc.classes.npc.NMSBase.serializeString;

public class NPC {

	private int entityId;
	private Location location;
	private GameProfile gameprofile;

	private Integer specStand;

	private List<Player> recipients;
	private Recipient recipient_type = Recipient.ALL;

	private String display_name;
	private String tablist_name;
	private DataWatcher dataWatcher;
	private DataWatcherObject<Byte> object_entity_state;
	private DataWatcherObject<String> object_customName;
	private DataWatcherObject<Boolean> object_isSilent;
	private DataWatcherObject<Boolean> object_hasGravity;
	private DataWatcherObject<Boolean> object_isCustomNameVisible;

	private static final Map<BlockFace, BlockFace[]> nextFaceOrder = new EnumMap<>(BlockFace.class);

	static {
		nextFaceOrder.put(BlockFace.NORTH, new BlockFace[] { BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH });
		nextFaceOrder.put(BlockFace.EAST, new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST });
		nextFaceOrder.put(BlockFace.SOUTH, new BlockFace[] { BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH });
		nextFaceOrder.put(BlockFace.WEST, new BlockFace[] { BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST });
	}

	private boolean isDestroyed;
	private Plugin plugin;
	
	
	/*
	 *  initialize all the npc default settings
	 */
	public NPC(String name, Location location, Plugin plugin) {
		this.plugin = plugin;
		this.display_name = name;
		this.tablist_name = name;
		this.recipient_type = Recipient.ALL;
		this.recipients = new ArrayList<Player>();
		this.entityId = (int) Math.ceil(Math.random() * 1000);
		this.gameprofile = new GameProfile(UUID.randomUUID(), display_name);
		this.location = location;
		this.dataWatcher = new DataWatcher(null);
		this.dataWatcher.register(object_entity_state = new DataWatcherObject<>(0, DataWatcherRegistry.a), (byte) 0);
		this.dataWatcher.register(new DataWatcherObject<>(1, DataWatcherRegistry.b), 300);
		this.dataWatcher.register(object_customName = new DataWatcherObject<>(2, DataWatcherRegistry.d), "");
		this.dataWatcher.register(object_isCustomNameVisible = new DataWatcherObject<>(3, DataWatcherRegistry.h),false);
		this.dataWatcher.register(object_isSilent = new DataWatcherObject<>(4, DataWatcherRegistry.h), false);
		this.dataWatcher.register(object_hasGravity = new DataWatcherObject<>(5, DataWatcherRegistry.h), false);
		this.dataWatcher.register(new DataWatcherObject<>(6, DataWatcherRegistry.a), (byte) 0);
		this.dataWatcher.register(new DataWatcherObject<>(7, DataWatcherRegistry.c), 20.0F);
		this.dataWatcher.register(new DataWatcherObject<>(8, DataWatcherRegistry.b), 0);
		this.dataWatcher.register(new DataWatcherObject<>(9, DataWatcherRegistry.h), false);
		this.dataWatcher.register(new DataWatcherObject<>(10, DataWatcherRegistry.b), 0);
		this.dataWatcher.register(new DataWatcherObject<>(11, DataWatcherRegistry.c), 0.0F);
		this.dataWatcher.register(new DataWatcherObject<>(12, DataWatcherRegistry.b), 20);
		this.dataWatcher.register(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 127);
		this.dataWatcher.register(new DataWatcherObject<>(14, DataWatcherRegistry.a), (byte) 1);
		this.dataWatcher.register(new DataWatcherObject<>(15, DataWatcherRegistry.n), new NBTTagCompound());
		this.dataWatcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.n), new NBTTagCompound());
	}

	
	/*
	 *  view all the packet receivers
	 */
	public List<Player> getRecipients() {
		return this.recipients;
	}
	
	
	/*
	 *  get the signature and skin from the gameprofile
	 */
	public Property getSkin() {
		if (this.gameprofile.getProperties().isEmpty())
			return null;
		return (Property) this.gameprofile.getProperties().get("textures").toArray()[0];
	}
	
	/*
	 * get npc id
	 */
	public int getEntityId() {
		return entityId;
	}

	
	/*
	 * get npc location
	 */
	public Location getLocation() {
		return location;
	}


	/*
	 * get npc gameprofile
	 */
	public GameProfile getGameprofile() {
		return gameprofile;
	}

	/*
	 * get all the packet receivers
	 */
	public Recipient getRecipient_type() {
		return recipient_type;
	}


	/*
	 * get npc displayname above head
	 */
	public String getDisplay_name() {
		return display_name;
	}

	/*
	 * get npc displayname in tablist
	 */
	public String getTablist_name() {
		return tablist_name;
	}

	/*
	 * check if npc is deleted
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

	/*
	 * get plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}


	/*
	 *  add Player so it receives the update packets
	 */
	public void addRecipient(Player p) {
		this.recipients.add(p);
	}
	
	
	/*
	 *  Remove Player that receives the update packets
	 */
	public void removeRecipient(Player p) {
		this.recipients.remove(p);
	}
	

	/*
	 *  toggle if the packet receivers are all the players online or a specicif group.
	 *  If set to ALL (online) then the set/get recipient will have no effect
	 */
	public void setRecipientType(Recipient recipient_type) {
		this.recipient_type = recipient_type;
	}

	
	/*
	 *  spawn the npc, this should be the last function after init.
	 */
	public void spawn(boolean tablist, boolean fix_head) {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		this.setField(packet, "a", this.entityId);
		this.setField(packet, "b", this.gameprofile.getId());
		this.setField(packet, "c", location.getX());
		this.setField(packet, "d", location.getY());
		this.setField(packet, "e", location.getZ());
		this.setField(packet, "f", fix_head ? (byte) ((int) location.getYaw() * 256.0F / 360.0F) : 0);
		this.setField(packet, "g", fix_head ? (byte) ((int) location.getPitch() * 256.0F / 360.0F) : 0);
		this.setField(packet, "h", this.dataWatcher);
		this.addToTabList();
		this.sendPacket(packet);
		this.isDestroyed = false;

		
		//Delay is required to commit the tablist changes
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!tablist) removeFromTabList();
			}
		}.runTaskLater(this.plugin, 5);

	}
	

	/*
	 *  set the name above the player
	 */
	public void setDisplayNameAboveHead(String name) throws IOException {
		if(name.length() > 16) throw new IOException("Name cannot be longer than 16 chatacters.");
		this.display_name = name;
		this.reloadNpc();
	}
	
	
	/*
	 *  set the name above the player and tablist.
	 */
	public void setDisplayName(String name) throws IOException {
		this.setDisplayNameAboveHead(name);
		this.setTablistName(name);
	}
	

	/*
	 *  set custom name in tablist
	 */
	public void setTablistName(String name) {
		this.tablist_name = name;
		this.updateToTabList();
	}
	
	
	/*
	 *  respawn the npc and refresh all comitted changes
	 */
	public void reloadNpc() {
		this.updateProfile();
		if(!this.isDestroyed) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.entityId });
			this.sendPacket(packet);
			this.spawn(true, true);
		}	
	}


	/*
	 *  Update/Refresh the gameprofile that contains UUID, Name, Skin.
	 */
	private void updateProfile() {
		Property skin = this.getSkin();
		this.gameprofile = new GameProfile(this.gameprofile.getId(), this.display_name);
		if (skin != null)
			this.setSkin(skin.getValue(), skin.getSignature());
	}
	
	
	/*
	 *  set the texture and signature in the gameprofile, to submit it you must reload player.
	 */
	public void setSkin(String texture, String signature) {
		this.gameprofile.getProperties().put("textures", new Property("textures", texture, signature));
	}


	
	/*
	 *  remove npc from the recipient's tablist
	 */
	public void removeFromTabList() {
		/*
		Object packet = null;
		try {
			packet = NMSBase.getNMSClass("PacketPlayOutPlayerInfo").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameprofile, 0,
				EnumGamemode.NOT_SET, CraftChatMessage.fromString(tablist_name)[0]);
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getField(packet, "b");
		players.add(data);
		this.setField(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
		this.setField(packet, "b", players);
		this.sendPacket(packet);

		 */

		try {
			Class<?> cIChatBaseComponent = getNMSClass("IChatBaseComponent");
			Class<?> cPacketPlayOutPlayerInfo = getNMSClass("PacketPlayOutPlayerInfo");
			Class<?> cPlayerInfoData = getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
			//Class<?> cEnumGamemode = getNMSClass("WorldSettings$EnumGamemode");
			Class<?> cEnumGamemode = getNMSClass("EnumGamemode");
			Object pPacketPlayOutInfo = cPacketPlayOutPlayerInfo.getConstructor().newInstance();
			Field fa = pPacketPlayOutInfo.getClass().getDeclaredField("a");
			fa.setAccessible(true);
			fa.set(pPacketPlayOutInfo, getEnumConstant(getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), "REMOVE_PLAYER"));
			Object oPlayerInfoData = cPlayerInfoData.getConstructor(cPacketPlayOutPlayerInfo, GameProfile.class, int.class, cEnumGamemode, cIChatBaseComponent)
					//.newInstance(pPacketPlayOutInfo, gameprofile, 1, getEnumConstant(getNMSClass("WorldSettings$EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
					.newInstance(pPacketPlayOutInfo, gameprofile, 0, getEnumConstant(getNMSClass("EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
			Field b = pPacketPlayOutInfo.getClass().getDeclaredField("b");
			b.setAccessible(true);
			@SuppressWarnings("unchecked")
			ArrayList<Object> array = (ArrayList<Object>) b.get(pPacketPlayOutInfo);
			array.add(oPlayerInfoData);
			b.set(pPacketPlayOutInfo, array);
			NMSBase.sendPacket(pPacketPlayOutInfo);
		} catch (Exception e1) {

		}
	}

	
	
	/*
	 *  update npc to the recipient's tablist
	 */
	public void updateToTabList() {
		/*
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameprofile, 0,
				EnumGamemode.NOT_SET, CraftChatMessage.fromString(tablist_name)[0]);
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getField(packet, "b");
		players.add(data);
		this.setField(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
		this.setField(packet, "b", players);
		this.sendPacket(packet);

		 */
		try {
			Class<?> cIChatBaseComponent = getNMSClass("IChatBaseComponent");
			Class<?> cPacketPlayOutPlayerInfo = getNMSClass("PacketPlayOutPlayerInfo");
			Class<?> cPlayerInfoData = getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
			//Class<?> cEnumGamemode = getNMSClass("WorldSettings$EnumGamemode");
			Class<?> cEnumGamemode = getNMSClass("EnumGamemode");
			Object pPacketPlayOutInfo = cPacketPlayOutPlayerInfo.getConstructor().newInstance();
			Field fa = pPacketPlayOutInfo.getClass().getDeclaredField("a");
			fa.setAccessible(true);
			fa.set(pPacketPlayOutInfo, getEnumConstant(getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), "UPDATE_DISPLAY_NAME"));
			Object oPlayerInfoData = cPlayerInfoData.getConstructor(cPacketPlayOutPlayerInfo, GameProfile.class, int.class, cEnumGamemode, cIChatBaseComponent)
					//.newInstance(pPacketPlayOutInfo, gameprofile, 1, getEnumConstant(getNMSClass("WorldSettings$EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
					.newInstance(pPacketPlayOutInfo, gameprofile, 0, getEnumConstant(getNMSClass("EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
			Field b = pPacketPlayOutInfo.getClass().getDeclaredField("b");
			b.setAccessible(true);
			@SuppressWarnings("unchecked")
			ArrayList<Object> array = (ArrayList<Object>) b.get(pPacketPlayOutInfo);
			array.add(oPlayerInfoData);
			b.set(pPacketPlayOutInfo, array);
			NMSBase.sendPacket(pPacketPlayOutInfo);
		} catch (Exception e1) {

		}
	}

	
	/*
	 *  add npc from the recipient's tablist
	 */
	public void addToTabList() {
		/*
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameprofile, 0,
				EnumGamemode.NOT_SET, CraftChatMessage.fromString(tablist_name)[0]);
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getField(packet, "b");
		players.add(data);
		this.setField(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
		this.setField(packet, "b", players);
		this.sendPacket(packet);

		 */
		try {
			Class<?> cIChatBaseComponent = getNMSClass("IChatBaseComponent");
			Class<?> cPacketPlayOutPlayerInfo = getNMSClass("PacketPlayOutPlayerInfo");
			Class<?> cPlayerInfoData = getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
			//Class<?> cEnumGamemode = getNMSClass("WorldSettings$EnumGamemode");
			Class<?> cEnumGamemode = getNMSClass("EnumGamemode");
			Object pPacketPlayOutInfo = cPacketPlayOutPlayerInfo.getConstructor().newInstance();
			Field fa = pPacketPlayOutInfo.getClass().getDeclaredField("a");
			fa.setAccessible(true);
			fa.set(pPacketPlayOutInfo, getEnumConstant(getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), "ADD_PLAYER"));
			Object oPlayerInfoData = cPlayerInfoData.getConstructor(cPacketPlayOutPlayerInfo, GameProfile.class, int.class, cEnumGamemode, cIChatBaseComponent)
					//.newInstance(pPacketPlayOutInfo, gameprofile, 1, getEnumConstant(getNMSClass("WorldSettings$EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
					.newInstance(pPacketPlayOutInfo, gameprofile, 0, getEnumConstant(getNMSClass("EnumGamemode"), "NOT_SET"), serializeString(gameprofile.getName()));
			Field b = pPacketPlayOutInfo.getClass().getDeclaredField("b");
			b.setAccessible(true);
			@SuppressWarnings("unchecked")
			ArrayList<Object> array = (ArrayList<Object>) b.get(pPacketPlayOutInfo);
			array.add(oPlayerInfoData);
			b.set(pPacketPlayOutInfo, array);
			NMSBase.sendPacket(pPacketPlayOutInfo);
		} catch (Exception e1) {

		}

	}

	
	/*
	 *  put items in inventory, see https://www.google.nl/search?q=bukkit+inventory+slots&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjr9v_FxvjaAhUFMuwKHQi7ALUQ_AUICigB&biw=1920&bih=974#imgrc=QUECAbUohgZxbM:
	 *  for more info
	 */
	public void setEquipment(EnumItemSlot slot, ItemStack item) {
		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
		this.setField(packet, "a", this.entityId);
		this.setField(packet, "b", slot);
		this.setField(packet, "c", item);
		this.sendPacket(packet);
	}

	
	/*
	 * set player action, such as shift, onfire, ect recommending to use 'public void setAction(Action action)'.
	 */
	public void setAction(byte action) {
		this.setMetaData(action);
	}
	

	/*
	 * set player action, such as shift, onfire, ect.
	 */
	public void setAction(Action action) {
		this.setMetaData(action.build());
	}

	
	/*
	 * make sure the npc is near a bed or on it.
	 */
	public void setSleep(boolean state, byte direction, boolean head) {
		if (state) {
			Location bed = location.clone();
			bed.setY(1);

			PacketPlayOutBed packet = new PacketPlayOutBed();
			this.setField(packet, "a", this.entityId);
			this.setField(packet, "b", new BlockPosition(bed.getX(), bed.getY(), bed.getZ()));

			for (Player p : (this.recipient_type == Recipient.ALL ? Bukkit.getOnlinePlayers() : this.recipients)) {
				((CraftPlayer) p).sendBlockChange(bed, Material.getMaterial("BED_BLOCK"), direction);
				//((CraftPlayer) p).sendBlockChange(bed.add(0, 0, 1), Material.getMaterial("BED_BLOCK"), direction);
			}

			this.sendPacket(packet);
			//this.teleport(this.location.clone().add(0, 0.075, 0), false);

			if (head) {
				this.teleport(this.location.clone().add(0, 0.2, 0), false);
			} else {
				Location location1 = this.location.clone().add(0, 0.2, 0);
				location1.setPitch(25);
				location1.setYaw(new SecureRandom().nextInt(30)-15);
				this.teleport(location1, false);
			}

		} else {
			this.setAnimation(NPCAnimation.LEAVE_BED);
			this.teleport(this.location.clone().subtract(0, 0.2, 0), true);
		}
	}
	
	
	/*
	 * delete the npc from the server.
	 */
	public void destroy() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.entityId });
		if (specStand!=null) {
			PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(new int[]{this.specStand});
			this.sendPacket(packet1);
		}
		this.removeFromTabList();
		this.sendPacket(packet);
		this.isDestroyed = true;
	}

	
	/*
	 * set npc status such as die or hurt. I recommend to use method 'public void setStatus(NPCStatus status)'
	 */	
	public void setStatus(byte status) {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
		this.setField(packet, "a", this.entityId);
		this.setField(packet, "b", status);
		this.sendPacket(packet);
	}
	
	
	/*
	 * set npc status such as die or hurt.
	 */
	public void setStatus(NPCStatus status) {
		this.setStatus((byte) status.getId());
	}

	
	/*
	 * set npc effect such as Night Vision or something else.
	 */
	public void setEffect(MobEffect effect) {
		this.sendPacket(new PacketPlayOutEntityEffect(this.entityId, effect));
	}

	
	/*
	 * set npc animation such as Swing arm ect. I recommend using method 'public void setAnimation(NPCAnimation animation)'
	 */
	public void setAnimation(byte animation) {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		this.setField(packet, "a", this.entityId);
		this.setField(packet, "b", animation);
		this.sendPacket(packet);
	}

	
	/*
	 * set npc animation such as Swing arm ect.
	 */
	public void setAnimation(NPCAnimation animation) {
		this.setAnimation((byte) animation.getId());
	}

	
	/*
	 * teleport npc to different location
	 */
	public void teleport(Location location, Boolean onGround) {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		this.setField(packet, "a", this.entityId);
		this.setField(packet, "b", location.getX());
		this.setField(packet, "c", location.getY());
		this.setField(packet, "d", location.getZ());
		this.setField(packet, "e", (byte) location.getYaw());
		this.setField(packet, "f", (byte) location.getPitch());
		this.setField(packet, "g", onGround);
		this.sendPacket(packet);
		this.rotateHead(location.getPitch(), location.getYaw());
		this.location = location;
	}

	
	/*
	 * rotate npc head to pitch and yaw.
	 */
	public void rotateHead(float pitch, float yaw) {
		PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.entityId, getFixRotation(yaw), (byte) pitch, true);
		PacketPlayOutEntityHeadRotation packet_1 = new PacketPlayOutEntityHeadRotation();
		this.setField(packet_1, "a", this.entityId);
		this.setField(packet_1, "b", getFixRotation(yaw));
		this.sendPacket(packet);
		this.sendPacket(packet_1);
	}

	
	
	
	/*
	 * These methods below are not usefull.
	 */
	
	private <T> void setDataWatcherObject(DataWatcherObject<T> datawatcherobject, Object t0) {
		try {
			Method m = this.dataWatcher.getClass().getDeclaredMethod("registerObject", DataWatcherObject.class,
					Object.class);
			m.setAccessible(true);
			m.invoke(this.dataWatcher, datawatcherobject, t0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setMetaData(byte data) {
		this.setDataWatcherObject(this.object_entity_state, data);
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.entityId, this.dataWatcher, true);
		sendPacket(packet);
	}
	
	private byte getFixRotation(float yawpitch) {
		return (byte) ((int) (location.getYaw() * 256.0F / 360.0F));
	}

	private byte getFixRotationYaw(float yawpitch) {
		return (byte) ((int) (yawpitch * 256.0F / 360.0F));
	}

	public byte getDirection(Location location) {
		org.bukkit.block.Block block = location.getBlock().getRelative(BlockFace.UP);
		BlockFace facing = getFacingDirection(location.getYaw());
		if(block.getRelative(facing).getType().isSolid()) {
			BlockFace[] order = nextFaceOrder.get(facing);
			for(BlockFace face : order) {
				if(block.getRelative(face).getType().isSolid()) continue;
				return getDirection(face);
			}
		}
		return getDirection(facing);
	}

	public byte getDirectionInversed(Location location) {
		org.bukkit.block.Block block = location.getBlock().getRelative(BlockFace.UP);
		BlockFace facing = getFacingDirectionInversed(location.getYaw());
		if(block.getRelative(facing).getType().isSolid()) {
			BlockFace[] order = nextFaceOrder.get(facing);
			for(BlockFace face : order) {
				if(block.getRelative(face).getType().isSolid()) continue;
				return getDirection(face);
			}
		}
		return getDirection(facing);
	}

	private BlockFace getFacingDirection(float yaw) {
		if(yaw < 0.0F) yaw += 180.0F;
		if(yaw >= 45.0F && yaw <= 135.0F) return BlockFace.EAST;
		if(yaw >= 135.0F && yaw <= 225.0F) return BlockFace.SOUTH;
		if(yaw >= 225.0F && yaw <= 315.0F) return BlockFace.WEST;
		return BlockFace.NORTH;
	}

	private BlockFace getFacingDirectionInversed(float yaw) {
		if(yaw < 0.0F) yaw += 180.0F;
		if(yaw >= 45.0F && yaw <= 135.0F) return BlockFace.WEST;
		if(yaw >= 135.0F && yaw <= 225.0F) return BlockFace.NORTH;
		if(yaw >= 225.0F && yaw <= 315.0F) return BlockFace.EAST;
		return BlockFace.SOUTH;
	}
/*
	private int getFacingDirection(BlockFace blockFace) {
		if(yaw < 0.0F) yaw += 180.0F;
		if(yaw >= 45.0F && yaw <= 135.0F) return BlockFace.EAST;
		if(yaw >= 135.0F && yaw <= 225.0F) return BlockFace.SOUTH;
		if(yaw >= 225.0F && yaw <= 315.0F) return BlockFace.WEST;
		return BlockFace.NORTH;
		switch(blockFace) {
			case EAST:
				return (byte)1;
			case SOUTH:
				return (byte)2;
			case WEST:
				return (byte)3;
			default:
				return (byte)0;
		}
	}

 */

	private byte getDirection(BlockFace blockFace) {
		switch(blockFace) {
			case EAST:
				return (byte)1;
			case SOUTH:
				return (byte)2;
			case WEST:
				return (byte)3;
			default:
				return (byte)0;
		}
	}

	private byte getDirectionInversed(BlockFace blockFace) {
		switch(blockFace) {
			case EAST:
				return (byte)3;
			case SOUTH:
				return (byte)0;
			case WEST:
				return (byte)1;
			default:
				return (byte)2;
		}
	}

	private BlockFace getBlockFace(byte direction) {
		switch(direction) {
			case 1:
				return BlockFace.EAST;
			case 2:
				return BlockFace.SOUTH;
			case 3:
				return BlockFace.WEST;
			default:
				return BlockFace.NORTH;
		}
	}

	private Object getField(Object obj, String field_name) {
		try {
			Field field = obj.getClass().getDeclaredField(field_name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setField(Object obj, String field_name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(field_name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	private void sendPacket(Packet<?> packet) {
		for (Player p : (this.recipient_type == Recipient.ALL ? Bukkit.getOnlinePlayers() : this.recipients)) {
			this.sendPacket(packet, p);
		}

	}

	private void sendPacket(Object packet, Player player) {
		Method sendPacket = null;
		try {
			sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sendPacket.invoke(getConnection(player), packet);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private void sendPacket(Object packet) {
		for (Player p : (this.recipient_type == Recipient.ALL ? Bukkit.getOnlinePlayers() : this.recipients)) {
			this.sendPacket(packet, p);
		}

	}

	public void spectate(Player player, boolean state) {
		Class<?> packetClass = null;
		try {
			packetClass = getNMSClass("PacketPlayOutCamera");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object camera = null;
		try {
			camera = packetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (state) {
			setField(camera, "a", this.entityId);
		} else {
			setField(camera, "a", player.getEntityId());
		}
		sendPacket(camera, player);
		player.getLocation().setYaw(location.getYaw());
		player.getLocation().setPitch(location.getPitch());
	}

	public void spectateFix(Player player, float yaw, boolean state) {

		/*

		WORKING CODE !!!!!!!!!!

		WorldServer s = ((CraftWorld)location.getWorld()).getHandle();
		EntityArmorStand stand = new EntityArmorStand(s);

		stand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0);
		stand.setCustomName("I'm a Armorstand!");
		stand.setCustomNameVisible(true);
		stand.setNoGravity(true);
		stand.setInvisible(false);


		PacketPlayOutSpawnEntityLiving spawnP = new PacketPlayOutSpawnEntityLiving(stand);

		sendPacket(spawnP, player);



		if(yaw < 0.0F) yaw += 180.0F;
		if(yaw >= 45.0F && yaw <= 135.0F) return BlockFace.WEST;
		if(yaw >= 135.0F && yaw <= 225.0F) return BlockFace.NORTH;
		if(yaw >= 225.0F && yaw <= 315.0F) return BlockFace.EAST;
		return BlockFace.SOUTH;
		 */

		Location location1 = location.clone();
		location.getYaw();

		double shiftX = -0.1;
		double shiftY = -1.1;

		float yawW = 0;
		if(yaw < 0.0F) yaw += 180.0F;
		if(yaw >= 45.0F && yaw <= 135.0F) {
			yawW = 90;
			location1.add(shiftX, 0, 0);
		} else if(yaw >= 135.0F && yaw <= 225.0F) {
			yawW = (90 + 90);
			location1.add(0, 0, shiftX);
		} else if(yaw >= 225.0F && yaw <= 315.0F) {
			yawW = (180 + 90);
			location1.add(-shiftX, 0, 0);
		} else {
			location1.add(0, 0, -shiftX);
		}

		if (specStand==null) {
			WorldServer s = ((CraftWorld) location.getWorld()).getHandle();
			EntityArmorStand stand = new EntityArmorStand(s);
			specStand= stand.getBukkitEntity().getEntityId();
			//specStand.setLocation(location1.getX(), location1.getY()+shiftY, location1.getZ(), 0, 0);
			//specStand.setCustomName("I'm a Armorstand!");
			stand.setCustomNameVisible(false);
			stand.setNoGravity(true);
			stand.setInvisible(true);


			PacketPlayOutSpawnEntityLiving spawnP = new PacketPlayOutSpawnEntityLiving(stand);

			sendPacket(spawnP, player);
		}


		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		this.setField(packet, "a", specStand);
		this.setField(packet, "b", location1.getX());
		this.setField(packet, "c", location1.getY()+shiftY);
		this.setField(packet, "d", location1.getZ());
		this.setField(packet, "e", (byte) 0);
		this.setField(packet, "f", (byte) 0);
		this.setField(packet, "g", false);
		this.sendPacket(packet, player);

		PacketPlayOutEntity.PacketPlayOutEntityLook packet1 = new PacketPlayOutEntity.PacketPlayOutEntityLook(specStand, getFixRotationYaw(yawW), (byte) 0, true);
		PacketPlayOutEntityHeadRotation packet_1 = new PacketPlayOutEntityHeadRotation();
		this.setField(packet_1, "a", specStand);
		this.setField(packet_1, "b", getFixRotationYaw(yawW));
		this.sendPacket(packet1, player);
		this.sendPacket(packet_1, player);

		Class<?> packetClass = null;
		try {
			packetClass = getNMSClass("PacketPlayOutCamera");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object camera = null;
		try {
			camera = packetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (state) {
			setField(camera, "a", specStand);
		} else {
			setField(camera, "a", player.getEntityId());
		}
		sendPacket(camera, player);
		//player.getLocation().setYaw(location.getYaw());
		//player.getLocation().setPitch(location.getPitch());
	}
	


	enum Recipient {
		ALL, LISTED_RECIPIENTS;
	}

	enum NPCAnimation {

		SWING_MAIN_HAND(0), 
		TAKE_DAMAGE(1), 
		LEAVE_BED(2), 
		SWING_OFFHAND(3), 
		CRITICAL_EFFECT(4), 
		MAGIC_CRITICAL_EFFECT(5);

		private int id;

		private NPCAnimation(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

	}

	enum NPCStatus {

		HURT(2), DIE(3);

		private int id;

		private NPCStatus(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public static class Action {

		private boolean on_fire, crouched, sprinting, invisible, glowing, flying_elytra;
		private byte result = 0;

		public Action(boolean on_fire, boolean crouched, boolean sprinting, boolean invisible, boolean glowing,
				boolean flying_elytra) {
			this.on_fire = on_fire;
			this.crouched = crouched;
			this.sprinting = sprinting;
			this.invisible = invisible;
			this.glowing = glowing;
			this.flying_elytra = flying_elytra;
		}

		public Action() {
		}

		public boolean isOn_fire() {
			return on_fire;
		}

		public Action setOn_fire(boolean on_fire) {
			this.on_fire = on_fire;
			return this;
		}

		public boolean isCrouched() {
			return crouched;
		}

		public Action setCrouched(boolean crouched) {
			this.crouched = crouched;
			return this;
		}

		public boolean isSprinting() {
			return sprinting;
		}

		public Action setSprinting(boolean sprinting) {
			this.sprinting = sprinting;
			return this;
		}

		public boolean isInvisible() {
			return invisible;
		}

		public Action setInvisible(boolean invisible) {
			this.invisible = invisible;
			return this;
		}

		public boolean isGlowing() {
			return glowing;
		}

		public Action setGlowing(boolean glowing) {
			this.glowing = glowing;
			return this;
		}

		public boolean isFlying_elytra() {
			return flying_elytra;
		}

		public Action setFlying_elytra(boolean flying_elytra) {
			this.flying_elytra = flying_elytra;
			return this;
		}

		public byte build() {
			result = 0;
			result = add(this.on_fire, (byte) 0x01);
			result = add(this.crouched, (byte) 0x02);
			result = add(this.sprinting, (byte) 0x08);
			result = add(this.invisible, (byte) 0x20);
			result = add(this.glowing, (byte) 0x40);
			result = add(this.flying_elytra, (byte) 0x80);
			return result;
		}

		private byte add(boolean condition, byte amount) {
			return (byte) (result += (condition ? amount : 0x00));
		}
	}
}