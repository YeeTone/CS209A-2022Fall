import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

class MethodEntity {
    private final int modifier;
    private final String name;
    private final Class<?> returnType;
    private final Class<?>[] argTypes;

    public MethodEntity(int modifier, String name, Class<?> returnType, Class<?>... argTypes) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.argTypes = argTypes;
    }

    public MethodEntity(String name, Class<?> returnType, Class<?>... argTypes) {
        this(Modifier.PUBLIC, name, returnType, argTypes);
    }

    public boolean checkForClass(Class<?> clazz) {
        try {
            Method m = clazz.getMethod(name, argTypes);
            return m.getModifiers() == modifier && m.getReturnType().equals(returnType);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s(%s)", Modifier.toString(modifier), name,
                Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(",")));
    }
}