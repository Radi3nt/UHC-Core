package fr.radi3nt.loupgarouuhc.classes.param;

import fr.radi3nt.loupgarouuhc.utilis.Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ParamLoader {

    public void saveParameters(Parameters parameters, Config config) {
        for (Field declaredField : parameters.getClass().getDeclaredFields()) {
            boolean savable = false;
            for (Annotation annotation : declaredField.getAnnotations()) {
                if (annotation instanceof Savable) {
                    savable = true;
                    break;
                }
            }
            if (savable) {
                try {
                    config.getConfiguration().set(declaredField.getName(), declaredField.get(parameters));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
