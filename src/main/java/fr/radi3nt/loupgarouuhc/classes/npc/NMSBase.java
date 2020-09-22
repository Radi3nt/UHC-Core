package fr.radi3nt.loupgarouuhc.classes.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.*;

public class NMSBase {

    static public Class<?> getNMSClass(String clazz) throws Exception {
        return Class.forName("net.minecraft.server." + getVersion() + "." + clazz);
    }

    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);
        return version;
    }

    public static Class<?> getClass(String name, boolean asArray) {
        try {
            if (asArray) return Array.newInstance(Class.forName(name), 0).getClass();
            else return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
        Constructor<?> c = clazz.getConstructor(args);
        c.setAccessible(true);
        return c;
    }

    public static Class<?> getCraftBukkitClass(String name) {
        return getClass(getCraftBukkitPrefix() + name, false);
    }

    public static String getCraftBukkitPrefix() {
        return "org.bukkit.craftbukkit." + getVersion() + ".";
    }

    public static Object getEnumConstant(Class<?> enumClass, String name) throws Exception {
        if (!enumClass.isEnum())
            return null;
        for (Object o : enumClass.getEnumConstants())
            if (name.equals(invokeMethod(o, "name", new Class[0])))
                return o;
        return null;
    }

    public static Method getMethod(Class<?> clazz, String mname) throws Exception {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(mname);
        } catch (Exception e) {
            try {
                m = clazz.getMethod(mname);
            } catch (Exception ex) {
                for (Method me : clazz.getDeclaredMethods()) {
                    if (me.getName().equalsIgnoreCase(mname))
                        m = me;
                    break;
                }
                if (m == null)
                    for (Method me : clazz.getMethods()) {
                        if (me.getName().equalsIgnoreCase(mname))
                            m = me;
                        break;
                    }
            }
        }
        m.setAccessible(true);
        return m;
    }

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) throws Exception {
        return getConstructor(clazz, args).newInstance(initargs);
    }

    public static Object invokeMethod(Object obj, String method, Object[] initargs) throws Exception {
        return getMethod(obj.getClass(), method).invoke(obj, initargs);
    }

    public static Object serializeString(String s) {
        try {
            Class<?> chatSerelizer = getCraftBukkitClass("util.CraftChatMessage");

            Method mSerelize = chatSerelizer.getMethod("fromString", String.class);

            return ((Object[]) mSerelize.invoke(null, s))[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static void sendPacket(Object packet) throws Exception {
        Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket.invoke(getConnection(player), packet);
        }
    }


}
