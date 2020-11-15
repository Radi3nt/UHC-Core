/*
 * Copyright (c) 2020. Code made by Radi3nt. Do not decompile. All right reserved
 */

package fr.radi3nt.uhc.api.stats;

import fr.radi3nt.uhc.api.player.npc.NMSBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Hologram {

    private static final Class<?> CraftWorld = NMSBase.getOBCClass("CraftWorld"),
            World = NMSBase.getNMSClass("World"),
            EntityArmorStand = NMSBase.getNMSClass("EntityArmorStand"),
            PacketPlayOutSpawnEntityLiving = NMSBase.getNMSClass("PacketPlayOutSpawnEntityLiving"),
            PacketPlayOutEntityDestroy = NMSBase.getNMSClass("PacketPlayOutEntityDestroy"),
            PacketPlayOutEntityMetadata = NMSBase.getNMSClass("PacketPlayOutEntityMetadata"),
            PacketPlayOutEntityTeleport = NMSBase.getNMSClass("PacketPlayOutEntityTeleport"),
            Entity = NMSBase.getNMSClass("Entity"),
            DataWatcher = NMSBase.getNMSClass("DataWatcher"),
            EntityLiving = NMSBase.getNMSClass("EntityLiving");
    private static Constructor<?> EntityArmorStandConstructor = null,
            PacketPlayOutSpawnEntityLivingConstructor = null,
            PacketPlayOutEntityDestroyConstructor = null,
            PacketPlayOutEntityMetadataConstructor = null,
            PacketPlayOutEntityTeleportConstructor = null;
    private static Method setInvisible = null, setCustomNameVisible = null,
            setCustomName = null, getId = null, getDataWatcher = null,
            setLocation = null, setMarker = null;

    static {
        try {
            EntityArmorStandConstructor = EntityArmorStand.getConstructor(World, double.class, double.class, double.class);
            PacketPlayOutSpawnEntityLivingConstructor = PacketPlayOutSpawnEntityLiving.getConstructor(EntityLiving);
            PacketPlayOutEntityDestroyConstructor = PacketPlayOutEntityDestroy.getConstructor(int[].class);
            PacketPlayOutEntityMetadataConstructor = PacketPlayOutEntityMetadata.getConstructor(int.class, DataWatcher, boolean.class);
            PacketPlayOutEntityTeleportConstructor = PacketPlayOutEntityTeleport.getConstructor(Entity);
            setInvisible = EntityArmorStand.getMethod("setInvisible", boolean.class);
            setMarker = EntityArmorStand.getMethod("setMarker", boolean.class);
            setCustomNameVisible = EntityArmorStand.getMethod("setCustomNameVisible", boolean.class);
            setLocation = Entity.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            try {
                setCustomName = EntityArmorStand.getMethod("setCustomName", String.class);
            } catch (NoSuchMethodException x) {
                setCustomName = EntityArmorStand.getMethod("setCustomName", NMSBase.IChatBaseComponent.IChatBaseComponent);
            }
            getId = EntityArmorStand.getMethod("getId");
            getDataWatcher = Entity.getMethod("getDataWatcher");
        } catch (NoSuchMethodException ignored) {
        }
    }

    private final Object armorStand;
    private final int id;
    private final Object packetPlayOutSpawnEntityLiving;
    private final Object packetPlayOutEntityDestroy;
    private final Set<Player> viewers = new HashSet<>();
    private Location location;
    private String text;

    public Hologram(Location location, String text) {
        this.location = location;
        this.text = text;
        try {
            this.armorStand = EntityArmorStandConstructor.newInstance(NMSBase.getHandle(CraftWorld.cast(location.getWorld())), location.getX(), location.getY(), location.getZ());
            setInvisible.invoke(armorStand, true);
            setMarker.invoke(armorStand, true);
            setCustomNameVisible.invoke(armorStand, true);
            if (setCustomName.getParameterTypes()[0].equals(String.class)) {
                setCustomName.invoke(armorStand, text);
            } else {
                setCustomName.invoke(armorStand, NMSBase.IChatBaseComponent.of(text));
            }
            this.id = (int) getId.invoke(armorStand);
            this.packetPlayOutSpawnEntityLiving = PacketPlayOutSpawnEntityLivingConstructor.newInstance(armorStand);
            this.packetPlayOutEntityDestroy = PacketPlayOutEntityDestroyConstructor.newInstance(new int[]{id});
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("An error occurred while creating the hologram.", e);
        }
    }

    public void display(Player... players) {
        update();
        try {
            for (Player player : players) {
                if (player == null) return;
                if (viewers.add(player)) {
                    NMSBase.sendPacket(packetPlayOutSpawnEntityLiving, player);
                    updateMetadata(player);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        }
    }

    public void displayForAll() {
        update();
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (viewers.add(player)) {
                    NMSBase.sendPacket(packetPlayOutSpawnEntityLiving, player);
                    updateMetadata(player);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        }
    }

    public void hide(Player... players) {
        update();
        for (Player player : players) {
            if (viewers.remove(player)) {
                NMSBase.sendPacket(packetPlayOutEntityDestroy, player);
            }
        }
    }

    public void hideFromAll() {
        update();
        for (Player player : viewers) {
            NMSBase.sendPacket(packetPlayOutEntityDestroy, player);
        }
        viewers.clear();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        try {
            setLocation.invoke(armorStand, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            this.location = location;
            updateLocation();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        } finally {
            update();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        try {
            if (setCustomName.getParameterTypes()[0].equals(String.class)) {
                setCustomName.invoke(armorStand, text);
            } else {
                setCustomName.invoke(armorStand, NMSBase.IChatBaseComponent.of(text));
            }
            this.text = text;
            updateMetadata();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        } finally {
            update();
        }
    }

    private void updateMetadata() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object packet = PacketPlayOutEntityMetadataConstructor.newInstance(id, getDataWatcher.invoke(armorStand), true);
        for (Player player : viewers) {
            NMSBase.sendPacket(packet, player);
        }
    }

    private void updateMetadata(Player player) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        NMSBase.sendPacket(PacketPlayOutEntityMetadataConstructor.newInstance(id, getDataWatcher.invoke(armorStand), true), player);
    }

    private void updateLocation() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Player player : viewers) {
            NMSBase.sendPacket(PacketPlayOutEntityTeleportConstructor.newInstance(armorStand), player);
        }
    }

    private void update() {
        viewers.removeIf(Objects::isNull);
    }


    public Set<Player> getViewers() {
        return viewers;
    }
}