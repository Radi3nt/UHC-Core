package fr.radi3nt.uhc.api.scenarios;

import fr.radi3nt.uhc.api.exeptions.common.CannotFindMessageException;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.game.UHCGame;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioGetter;
import fr.radi3nt.uhc.api.scenarios.util.ScenarioSetter;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Scenario implements Listener {

    private static final ArrayList<Scenario> activeScenarios = new ArrayList<>();
    private static final Set<Class<? extends Scenario>> registredScenarios = new HashSet<>();


    private static final String DEFAULT_NAME = "NaN";
    protected boolean active = false;
    protected UHCGame game;

    public Scenario(UHCGame game) {
        this.game = game;
    }

    public static String getName() {
        return DEFAULT_NAME;
    }

    public static ItemStack getItem() {
        return new ItemStack(Material.BARRIER);
    }

    public static ArrayList<Scenario> getActiveScenarios() {
        return activeScenarios;
    }

    public static Set<Class<? extends Scenario>> getScenariosClasses() {
        return registredScenarios;
    }

    public void register() {
        if (!activeScenarios.contains(this)) {
            activeScenarios.add(this);
            Bukkit.getPluginManager().registerEvents(this, UHCCore.getPlugin());
        }
    }

    public void activate() {
        active = true;
        if (!activeScenarios.contains(this)) {
            register();
        }
    }

    public void unregister() {
        activeScenarios.remove(this);
        HandlerList.unregisterAll(this);
    }

    public void tick(GameTimer gameTimer, int tick) {
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


    protected String getMessage(Language lang, String id) throws CannotFindMessageException {
        return lang.getMessage(getMessagesId() + id);
    }

    protected String getMessagesId() {
        try {
            return "game.scenarios." + this.getClass().getMethod("getName").invoke(this) + ".";
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getGeneralLogger().log(e);
        }
        return "";
    }

    protected boolean isSameGame(Player player) {
        return isSameGame(UHCPlayer.thePlayer(player));
    }

    protected boolean isSameGame(UHCPlayer player) {
        return player.isInGame() && player.getGameData().getGame().equals(game);
    }

    public UHCGame getGame() {
        return game;
    }

    public void setGame(UHCGame game) {
        this.game = game;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

}
