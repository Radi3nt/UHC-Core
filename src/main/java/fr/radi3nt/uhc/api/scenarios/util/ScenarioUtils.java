package fr.radi3nt.uhc.api.scenarios.util;

import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.game.GameTimer;
import fr.radi3nt.uhc.api.lang.Logger;
import fr.radi3nt.uhc.api.scenarios.Scenario;
import fr.radi3nt.uhc.api.scenarios.ScenarioData;
import fr.radi3nt.uhc.uhc.UHCCore;
import org.bukkit.ChatColor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        ScenarioData name = null;
        try {
            name = (ScenarioData) scenarioClass.getMethod("getData").invoke(null);
        } catch (Exception e) {
            Logger.getGeneralLogger().log(e);
            Logger.getGeneralLogger().logInConsole(UHCCore.getPrefix() + ChatColor.DARK_RED  + " Cannot add scenario " + scenarioClass.getSimpleName() + " to repertoried scenarios: invalid scenario id");
            return;
        }
        if (name.getName().equals(Scenario.getData().getName())) {
            Logger.getGeneralLogger().logInConsole(UHCCore.getPrefix() + ChatColor.DARK_RED  + " Cannot add scenario " + scenarioClass.getSimpleName() + " to repertoried scenarios: no scenario id");
            return;
        }
        List<ScenarioData> repertoriedScenariosName = new ArrayList<>();
        for (Class<? extends Scenario> repertoriedScenariosClass : Scenario.getRepertoriedScenariosClasses()) {
            try {
                repertoriedScenariosName.add(((ScenarioData) repertoriedScenariosClass.getMethod("getData").invoke(null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
        }
        if (repertoriedScenariosName.contains(name)) {
            Logger.getGeneralLogger().logInConsole(UHCCore.getPrefix() + ChatColor.DARK_RED  + " Cannot add scenario " + scenarioClass.getTypeName() + " to repertoried scenarios: scenario id is duplicated");
            return;
        }
        Scenario.getRepertoriedScenariosClasses().add(scenarioClass);
    }

}
