package test.trs.tcache;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import pers.lzw.ecache.config.context.CommonContext;
import pers.lzw.ecache.support.sqel.SpelParserUtils;

import java.util.*;

/**
 * @author wenwen
 * Created on 2019/8/13
 */
public class SpelTests {
    private static ExpressionParser parser = new SpelExpressionParser();
    @Test
    public void parseTests() {
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        // 测试#this
        evaluationContext.setVariable("list", new ArrayList<Integer>()
        {
            {
                add(0);
                add(1);
                add(2);
                add(3);
                add(4);
            }
        });
        Object thisvalue =
                parser.parseExpression("#list.?[#this>3]").getValue(
                        evaluationContext);
        System.out.println(thisvalue);
        // 测试#root

        CommonContext commonContext = new CommonContext();
        commonContext.addProperty("test", "test");
        commonContext.addProperty("test2", "test2");
        commonContext.addProperty("test3", "test3");
        commonContext.addProperty("test4", "test4");
        List<String> result1 = SpelParserUtils.parseSpelToString(new String[]{"pageContext"}, new Object[]{commonContext}, new String[]{"#pageContext['test']","#pageContext['test2']"});
        Map<String, Object> map = SpelParserUtils.parseSpelToMap(new String[]{"pageContext"}, new Object[]{commonContext}, new String[]{"#pageContext['test']","#pageContext['test2']"});
        String test = SpelParserUtils.parseSpelToString("pageContext", commonContext, "T(com.trs.common.utils.StringUtils).convertToMD5('#pageContext')");
        String result2 = SpelParserUtils.parseSpelToString("pageContext", commonContext, "#pageContext['test'].length() > 10");
        Map result4 = SpelParserUtils.parseSpel("commonContext", commonContext, "#commonContext", Map.class, null);
        System.out.println(result1);
    }

    @Test
    public void testParse() {
        ExpressionParser parser = new SpelExpressionParser();
        //表达式解析
        List<String> list = new ArrayList<>();
        CollectionUtils.addAll(list, Arrays.asList("123","456","789"));
        Map map = new HashMap();
        map.put("test", "test");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("test", "123");
        context.setVariable("map", map);
        context.setVariable("list", list);
        String result1 = parser.parseExpression("#test").getValue(context, String.class);
        String result3 = parser.parseExpression("#map['test']").getValue(context, String.class);
        List result4 = parser.parseExpression("#list").getValue(context, List.class);
        String result5 = parser.parseExpression("#list[1]").getValue(context, String.class);
        Map result6 = parser.parseExpression("#map").getValue(context, Map.class);
        System.out.println(result1);
    }
}
