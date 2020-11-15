package fr.radi3nt.uhc.api.player.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NMSBase {

    static public Class<?> getNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) {
        Constructor<?> c = null;
        try {
            c = clazz.getConstructor(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        c.setAccessible(true);
        return c;
    }

    public static Class<?> getCraftBukkitClass(String name) {
        return getClass(getCraftBukkitPrefix() + name, false);
    }

    public static String getCraftBukkitPrefix() {
        return "org.bukkit.craftbukkit." + getVersion() + ".";
    }

    public static Object getEnumConstant(Class<?> enumClass, String name) {
        if (!enumClass.isEnum())
            return null;
        for (Object o : enumClass.getEnumConstants())
            if (name.equals(invokeMethod(o, "name", new Class[0])))
                return o;
        return null;
    }

    public static Method getMethod(Class<?> clazz, String mname) {
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

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs) {
        try {
            return getConstructor(clazz, args).newInstance(initargs);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String method, Object[] initargs) {
        try {
            return getMethod(obj.getClass(), method).invoke(obj, initargs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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


    public static void sendPacket(Object packet) {
        Method sendPacket = null;
        try {
            sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                sendPacket.invoke(getConnection(player), packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendPacket(Object packet, Player player) {
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

    public static Class<?> getOBCClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("An error occurred while finding OBC class.", ex);
        }
    }


    public static Object getHandle(Object object) {
        try {
            return object.getClass().getMethod("getHandle").invoke(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class IChatBaseComponent {

        public static final Class<?> IChatBaseComponent = getNMSClass("IChatBaseComponent");
        private static final Logger logger = Logger.getLogger(IChatBaseComponent.class.getName());
        private static Method newIChatBaseComponent = null;

        static {
            try {
                newIChatBaseComponent = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
            } catch (NoSuchMethodException e) {
                logger.log(Level.SEVERE, "An error occurred while initializing IChatBaseComponent.");
            }
        }

        public static Object of(String string) {
            try {
                return newIChatBaseComponent.invoke(null, "{\"text\": \"" + string + "\"}");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
