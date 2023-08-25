import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class DI {
    private final ArrayList<Object> container = new ArrayList<>();

    public Object getInstance(String className) {
        for (Object existingInstance: this.container) {
            if (className.equals(existingInstance.getClass().getName())) {
                return existingInstance;
            }
        }
        try {
            Constructor<?> constructor = Class.forName(className).getConstructors()[0];
            Object[] dependencies = new Object[constructor.getParameterCount()];
            int index = 0;
            for (Class dependency: constructor.getParameterTypes()) {
                dependencies[index++] = this.getInstance(dependency.getName());
            }

            Object newInstance = constructor.newInstance(dependencies);
            this.container.add(newInstance);
            return newInstance;
        } catch (Throwable exception) {
            return null;
        }
    }
}
