package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.scenario.*;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioCommand;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioEvent;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
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


    private static final String DEFAULT_NAME = "NaN";
    private static final int MAX_RETURN_METHODS = 64;
    protected boolean active = false;
    protected LGGame game;

    public Scenario(LGGame game) {
        this.game = game;
    }

    public static String getName() {
        return DEFAULT_NAME;
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
            ArrayList<Method> method = new ArrayList<>();
            Method[] methods = scenario.getClass().getMethods();
            for (Method method1 : methods) {
                boolean hasAnnontation = false;
                for (Annotation declaredAnnotation : method1.getAnnotations()) {
                    if (declaredAnnotation.annotationType().equals(ScenarioEvent.class)) {
                        hasAnnontation = true;
                        break;
                    }
                }
                if (!hasAnnontation) {
                    continue;
                }
                if (method1.getParameterTypes().length == 1) {
                    for (Class<?> parameterType : method1.getParameterTypes()) {
                        if (parameterType.equals(e.getClass())) {
                            method.add(method1);
                        }
                    }
                }
            }
            if (!method.isEmpty()) {
                method.forEach(method1 -> {
                    try {
                        method1.invoke(scenario, e);
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                });
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
        registred.add(DeadlyWater.class);
        registred.add(FastFurnace.class);
        registred.add(HastyBoys.class);
        registred.add(LimitedEnchants.class);
        registred.add(NoPoison.class);
        registred.add(RodLess.class);
    }

    public static ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

    public static Set<Class<? extends Scenario>> getScenariosClasses() {
        return registred;
    }

    public void register() {
        if (!scenarios.contains(this))
            scenarios.add(this);
    }

    public void unregister() {
        if (isActive())
            scenarios.remove(this);
    }

    public void tick(GameTimer gameTimer, int tick) {
    }

    public void activate() {
        if (!isActive()) {
            active = true;
            register();
        }
    }

    public Method[] getScenarioSetMethods() {
        ArrayList<Method> methodFinal = new ArrayList<>();
        Method[] methods = this.getClass().getMethods();
        for (Method method1 : methods) {
            for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioSetter.class)) {
                    methodFinal.add(method1);
                }
            }
        }
        return methodFinal.toArray(new Method[methodFinal.size()]);
    }

    public ScenarioSetter[] getScenarioSetAnnotations() {
        ArrayList<ScenarioSetter> methodFinal = new ArrayList<>();
        Method[] methods = this.getClass().getMethods();
        for (Method method1 : methods) {
            for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioSetter.class)) {
                    methodFinal.add((ScenarioSetter) declaredAnnotation);
                }
            }
        }
        return methodFinal.toArray(new ScenarioSetter[methodFinal.size()]);
    }

    public ScenarioGetter[] getScenarioGetAnnotations() {
        ArrayList<ScenarioGetter> methodFinal = new ArrayList<>();
        Method[] methods = this.getClass().getMethods();
        for (Method method1 : methods) {
            for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioGetter.class)) {
                    methodFinal.add((ScenarioGetter) declaredAnnotation);
                }
            }
        }
        return methodFinal.toArray(new ScenarioGetter[methodFinal.size()]);
    }

    public Method[] getScenarioGetMethods() {
        ArrayList<Method> methodFinal = new ArrayList<>();
        Method[] methods = this.getClass().getMethods();
        for (Method method1 : methods) {
            boolean hasAnnontation = false;
            for (Annotation declaredAnnotation : method1.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioGetter.class)) {
                    hasAnnontation = true;
                }
            }
            if (!hasAnnontation) {
                continue;
            }
            methodFinal.add(method1);
        }
        return methodFinal.toArray(new Method[methodFinal.size()]);
    }

    public Method getScenarioGetMethod(String name) throws NoSuchMethodException {
        for (Method scenarioGetMethod : getScenarioGetMethods()) {
            for (Annotation declaredAnnotation : scenarioGetMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioGetter.class)) {
                    ScenarioGetter scenarioGetter = (ScenarioGetter) declaredAnnotation;
                    if (scenarioGetter.name().equals(name)) {
                        return scenarioGetMethod;
                    }
                }
            }
        }
        throw new NoSuchMethodException("Cannot get method with name: " + name);
    }

    public Method getScenarioSetMethod(String name) throws NoSuchMethodException {
        for (Method scenarioSetMethod : getScenarioSetMethods()) {
            for (Annotation declaredAnnotation : scenarioSetMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ScenarioSetter.class)) {
                    ScenarioSetter scenarioGetter = (ScenarioSetter) declaredAnnotation;
                    if (scenarioGetter.name().equals(name)) {
                        return scenarioSetMethod;
                    }
                }
            }
        }
        throw new NoSuchMethodException("Cannot get method with name: " + name);
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
