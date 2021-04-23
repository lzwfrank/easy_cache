package pers.lzw.ecache.base;

import io.vavr.control.Either;
import io.vavr.control.Try;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义结果函数式接口
 *   对于使用者而言,只需要实现本身逻辑处理即可以
 *   方法定义者可以通过getResult()方法即可得到由Either包括的结果对象
 * @author: liu.zhengwei
 * @create: 2019/12/26 18:44
 */
@FunctionalInterface
public interface EitherFunction<T> {

    default Either<Throwable, T> getResult() {
        Try<T> tTry = Try.of(() -> get());
        if (tTry.isSuccess()) {
            return Either.right(tTry.get());
        } else {
            return Either.left(tTry.getCause());
        }
    }

    default Class<T> getType() {
        Class<T> tClass = (Class<T>) getTClass(getClass());
        return tClass;
    }

    T get();

    static Class<?> getClassType(Class<? extends EitherFunction> fClass) {
        Type genType = fClass.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) params[0];
    }

    // Lambda class name: test.Toto$$Lambda$1/1199823423
    // Implementation synthetic method: lambda$main$0
    //
    static Class<?> getTClass(Class<? extends EitherFunction> fClass) {
        String functionClassName = fClass.getName();
        int lambdaMarkerIndex = functionClassName.indexOf("$$Lambda$");
        if (lambdaMarkerIndex == -1) { // Not a lambda
            return getClassType(fClass);
        }

        String declaringClassName = functionClassName.substring(0, lambdaMarkerIndex);
        int lambdaIndex = Integer.parseInt(functionClassName.substring(lambdaMarkerIndex + 9, functionClassName.lastIndexOf('/')));

        Class<?> declaringClass;
        try {
            declaringClass = Class.forName(declaringClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find lambda's parent class " + declaringClassName);
        }

        for (Method method : declaringClass.getDeclaredMethods()) {
            if (method.isSynthetic()
                    && method.getName().startsWith("lambda$")
                    && method.getName().endsWith("$" + (lambdaIndex - 1))
                    && Modifier.isStatic(method.getModifiers())) {
                return method.getReturnType();
            }
        }

        throw new IllegalStateException("Unable to find lambda's implementation method");
    }
}
