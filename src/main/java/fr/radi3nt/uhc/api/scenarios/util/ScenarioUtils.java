package fr.radi3nt.uhc.api.scenarios.util;

import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.scenarios.Scenario;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScenarioUtils {

    public static void callCommand(CommandUtilis command) {
        for (Scenario scenario : Scenario.getActiveScenarios()) {
            if (scenario.isActive()) {
                ArrayList<Method> method = new ArrayList<>();
                Method[] methods = scenario.getClass().getMethods();
                for (Method method1 : methods) {
                    for (Annotation declaredAnnotation : method1.getAnnotations()) {
                        if (declaredAnnotation.annotationType().equals(ScenarioCommand.class)) {
                            method.add(method1);
                        }
                    }
                }
                for (Method method1 : method) {
                    try {
                        method1.invoke(scenario, command);
                    } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
            }
        }
    }

    public static void tickAll(GameTimer gameTimer, int tick) {
        for (Scenario scenario : Scenario.getActiveScenarios()) {
            if (scenario.getGame().equals(gameTimer.getGame()))
                if (scenario.isActive())
                    scenario.tick(gameTimer, tick);
        }
    }

    public static void addScenario(Class<? extends Scenario> scenarioClass) {
        Scenario.getScenariosClasses().add(scenarioClass);
    }

}
