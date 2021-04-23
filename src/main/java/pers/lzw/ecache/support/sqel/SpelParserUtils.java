package pers.lzw.ecache.support.sqel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import pers.lzw.ecache.config.context.CommonContext;
import pers.lzw.ecache.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenwen
 * Created on 2019/8/8
 */
public class SpelParserUtils {

    private static ExpressionParser parser = new SpelExpressionParser();

    /**
     * 解析 spel 表达式
     *
     * @param params    参数名称
     * @param arguments 参数值
     * @param spels     el表达式
     * @return 执行spel表达式后的结果
     */
    public static List<String> parseSpelToString(String[] params, Object[] arguments, String[] spels) {
        List<String> result = new ArrayList<>();
        EvaluationContext context = buildContext(params, arguments);
        for (String spel : spels) {
            result.add(parseSpelToString(context, null, null, spel));
        }
        return result;
    }

    /**
     * 解析 spel 表达式 将结果转为String类型
     *
     * @param param 上下文信息
     * @param spel  el表达式
     * @return
     */
    public static String parseSpelToString(String param, Object argument, String spel) {
        EvaluationContext context = buildContext(param, argument);
        return StringUtils.toStringValue(parseSpel(context, spel));
    }

    /**
     * 解析 spel 表达式 将结果转为String类型
     *
     * @param context
     * @param param
     * @param argument
     * @param spel
     * @return
     */
    public static String parseSpelToString(EvaluationContext context, String param, Object argument, String spel) {
        if (context == null) {
            context = buildContext(param, argument);
        }
        return StringUtils.toStringValue(parseSpel(context, spel));
    }

    /**
     * 解析 spel 表达式
     *
     * @param params        参数名称
     * @param arguments     参数值
     * @param spels         el表达式
     * @param clazz         结果类型
     * @param defaultResult 默认值
     * @return 执行spel表达式后的结果
     */
    public static <T> List<T> parseSpel(String[] params, Object[] arguments, String[] spels, Class<T> clazz, T defaultResult) {
        List<T> result = new ArrayList<>();
        EvaluationContext context = buildContext(params, arguments);

        for (String spel : spels) {
            result.add(parseSpel(context, spel, clazz, defaultResult));
        }
        return result;
    }

    /**
     * 解析 spel 表达式
     *
     * @param params
     * @param arguments
     * @param spel
     * @param clazz
     * @param defaultResult
     * @param <T>
     * @return
     */
    public static <T> T parseSpel(String[] params, Object[] arguments, String spel, Class<T> clazz, T defaultResult) {
        EvaluationContext context = buildContext(params, arguments);
        return parseSpel(context, spel, clazz, defaultResult);
    }

    /**
     * 解析 spel 表达式 并将结果存到一个map中，key为spels值，value是对象值
     *
     * @param params    参数名称
     * @param arguments 参数值
     * @param spels     el表达式
     * @return
     */
    public static Map<String, Object> parseSpelToMap(String[] params, Object[] arguments, String[] spels) {
        Map<String, Object> result = new HashMap();
        EvaluationContext context = buildContext(params, arguments);
        for (String spel : spels) {
            result.put(spel, parseSpel(context, spel, Object.class, null));
        }
        return result;
    }

    /**
     * 解析 spel 表达式
     *
     * @param param         参数名称
     * @param argument      参数值
     * @param spels         el表达式
     * @param clazz         返回类型
     * @param defaultResult 默认值
     * @param <T>
     * @return
     */
    public static <T> T parseSpel(String param, Object argument, String spels, Class<T> clazz, T defaultResult) {
        return parseSpel(buildContext(param, argument), spels, clazz, defaultResult);
    }

    /**
     * 解析 spel 表达式
     *
     * @param context       环境
     * @param spel          表达式
     * @param clazz         返回结果的类型
     * @param defaultResult 默认结果
     * @return 执行spel表达式后的结果
     */
    public static <T> T parseSpel(EvaluationContext context, String spel, Class<T> clazz, T defaultResult) {
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    public static Object parseSpel(EvaluationContext context, String spel) {
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 构建context
     *
     * @param params
     * @param arguments
     * @return
     */
    private static EvaluationContext buildContext(String[] params, Object[] arguments) {
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            buildContext(context, params[len], arguments[len]);
        }

        return context;
    }

    /**
     * 构建context
     *
     * @param param
     * @param argument
     * @return
     */
    private static EvaluationContext buildContext(String param, Object argument) {
        return buildContext(null, param, argument);
    }

    /**
     * 构建context
     *
     * @param context
     * @param param
     * @param argument
     * @return
     */
    private static EvaluationContext buildContext(EvaluationContext context, String param, Object argument) {
        context = context == null ? new StandardEvaluationContext() : context;
        //当参数是CommonContext类型时，将其转换成Map
        if (argument instanceof CommonContext) {
            CommonContext commonContext = (CommonContext) argument;
            Map map = commonContext.toMap();
            context.setVariable(param, map);
        } else {
            context.setVariable(param, argument);
        }

        return context;
    }
}
