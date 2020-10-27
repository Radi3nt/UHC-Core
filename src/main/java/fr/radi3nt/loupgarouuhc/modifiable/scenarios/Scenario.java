package fr.radi3nt.loupgarouuhc.modifiable.scenarios;

import fr.radi3nt.loupgarouuhc.classes.game.LGGame;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioGetter;
import fr.radi3nt.loupgarouuhc.modifiable.scenarios.util.ScenarioSetter;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Scenario {

    private static final ArrayList<Scenario> activeScenarios = new ArrayList<>();
    private static final Set<Class<? extends Scenario>> registredScenarios = new HashSet<>();


    private static final String DEFAULT_NAME = "NaN";
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

    public static ArrayList<Scenario> getActiveScenarios() {
        return activeScenarios;
    }

    public static Set<Class<? extends Scenario>> getScenariosClasses() {
        return registredScenarios;
    }

    public void register() {
        if (!activeScenarios.contains(this))
            activeScenarios.add(this);
    }

    public void activate() {
        active = true;
        if (!activeScenarios.contains(this)) {
            register();
        }
    }

    public void unregister() {
        activeScenarios.remove(this);
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


    public LGGame getGame() {
        return game;
    }

    public void setGame(LGGame game) {
        this.game = game;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

}
