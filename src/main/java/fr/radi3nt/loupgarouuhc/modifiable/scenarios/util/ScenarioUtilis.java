package fr.radi3nt.loupgarouuhc.modifiable.scenarios.util;

import fr.radi3nt.loupgarouuhc.modifiable.scenarios.Scenario;
import fr.radi3nt.loupgarouuhc.timer.GameTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScenarioUtilis {

    public static void callCommand(CommandSender sender, Command command, String label, String[] args) {
        for (Scenario scenario : Scenario.getActiveScenarios()) {
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
        for (Scenario scenario : Scenario.getActiveScenarios()) {
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
        for (Scenario scenario : Scenario.getActiveScenarios()) {
            scenario.tick(gameTimer, tick);
        }
    }

    public static void addScenario(Class<? extends Scenario> scenarioClass) {
        Scenario.getScenariosClasses().add(scenarioClass);
    }

}
