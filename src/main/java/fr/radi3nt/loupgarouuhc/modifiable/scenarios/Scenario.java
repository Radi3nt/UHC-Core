package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.*;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioCommand;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Scenario {

    private static final ArrayList<Scenario> scenarios = new ArrayList<>();
    private static final Set<Class<? extends Scenario>> registred = new HashSet<>();

    private static final String defaultName = "NaN";
    protected boolean active = false;
    protected LGGame game;

    public Scenario(LGGame game) {
        this.game = game;
    }

    public static String getName() {
        return defaultName;
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BARRIER);
    }

    public static void callCommand(CommandSender sender, Command command, String label, String[] args) {
        for (Scenario scenario : scenarios) {
            ArrayList<Method> method = new ArrayList<>();
            Method[] methods = scenario.getClass().getMethods();
            for (Method method1 : methods) {
                for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                    if (declaredAnnotation.annotationType().equals(ScenarioCommand.class)) {
                        method.add(method1);
                    }
                }
            }
            if (!method.isEmpty()) {
                for (Method method1 : method) {
                    try {
                        method1.invoke(scenario, sender, command, label, args);
                    } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
            }
        }
    }

    public static void callEvent(Event e) {
        for (Scenario scenario : scenarios) {
            Method method = null;
            Method[] methods = scenario.getClass().getMethods();
            for (Method method1 : methods) {
                boolean hasAnnontation = false;
                for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                    if (declaredAnnotation.annotationType().equals(ScenarioEvent.class)) {
                        hasAnnontation = true;
                    }
                }
                if (!hasAnnontation) {
                    continue;
                }
                if (method1.getParameterTypes().length == 1) {
                    for (Class<?> parameterType : method1.getParameterTypes()) {
                        if (parameterType.equals(e.getClass())) {
                            method = method1;
                            break;
                        }
                    }
                }
            }
            if (method != null) {
                try {
                    method.invoke(scenario, e);
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
        }
    }

    public static void tickAll(GameTimer gameTimer, int tick) {
        for (Scenario scenario : scenarios) {
            scenario.tick(gameTimer, tick);
        }
    }

    public static void staticRegisterAll() {
        registred.clear();
        registred.add(XPBoost.class);
        registred.add(VanillaPlus.class);
        registred.add(TimedCommands.class);
        registred.add(Timber.class);
        registred.add(StarterItems.class);
        registred.add(OreLimiter.class);
        registred.add(NoFall.class);
        registred.add(NoClean.class);
        registred.add(FireLess.class);
        registred.add(FinalHeal.class);
        registred.add(CutClean.class);
        registred.add(AutoBreak.class);
    }

    public static ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

    public static Set<Class<? extends Scenario>> getScenariosClasses() {
        return registred;
    }

    public void register() {
        scenarios.add(this);
    }

    public void unregister() {
        scenarios.remove(this);
    }

    public void tick(GameTimer gameTimer, int tick) {
    }

    public void activate() {
        active = true;
        register();
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }
}
