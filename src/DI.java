import db.DatasourceConfig;
import db.DatasourceConfigInterface;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;

public class DI {
    private final ArrayList<Object> container = new ArrayList<>();

    private final Map<String, String> mapping = Map.of(DatasourceConfigInterface.class.getName(), DatasourceConfig.class.getName());

    public Object getInstance(String className) {
        for (Object existingInstance: this.container) {
            if (className.equals(existingInstance.getClass().getName())) {
                return existingInstance;
            }
        }
        try {
            Constructor<?> constructor = getInfo(className).getConstructors()[0];
            Object[] dependencies = new Object[constructor.getParameterCount()];
            int index = 0;
            for (Class<?> dependency: constructor.getParameterTypes()) {
                dependencies[index++] = this.getInstance(dependency.getName());
            }

            Object newInstance = constructor.newInstance(dependencies);
            this.container.add(newInstance);
            return newInstance;
        } catch (Throwable exception) {
            throw new RuntimeException("Ошибка создания объекта " + className + ": " + exception.getMessage());
        }
    }

    private Class<?> getInfo(String className) throws ClassNotFoundException {
        Class<?> classInfo = Class.forName(className);
        if (classInfo.isInterface()) {
            if (!mapping.containsKey(className)) {
                throw new ClassNotFoundException();
            }

            return Class.forName(mapping.get(className));
        }

        return classInfo;
    }
}
