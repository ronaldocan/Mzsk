package com.rona.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhengguican
 * @date 2021/11/01 15:55:18
 */
@SpringBootTest
public class AviatorExpressTest {

    @Test
    public void testAviatorExpress() {

        String script = "let a = seq.array(int, 1, 2, 3, 4);\n" +
                "\n" +
                "println(\"type(a) is : \" + type(a));\n" +
                "println(\"count(a) is: \" + count(a));\n" +
                "\n" +
                "let s = seq.array(java.lang.String, \"hello\", \"world\", \"aviator\");\n" +
                "\n" +
                "println(string.join(s, \",\"));\n" +
                "\n" +
                "let a = seq.array_of(int, 3);\n" +
                "println(\"type(a) is : #{type(a)}\");\n" +
                "println(\"count(a) is: #{count(a)}\");\n" +
                "\n" +
                "println(\"before assignment:\");\n" +
                "for x in a {\n" +
                "  println(x);\n" +
                "}\n" +
                "\n" +
                "for i in range(0, 3) {\n" +
                "  a[i] = i;\n" +
                "}\n" +
                "\n" +
                "println(\"after assignment:\");\n" +
                "for x in a {\n" +
                "  println(x);\n" +
                "}\n" +
                "\n" +
                "\n" +
                "## create multidimensional array\n" +
                "\n" +
                "let a = seq.array_of(long, 3, 2);\n" +
                "\n" +
                "assert(3 == count(a));\n" +
                "assert(2 == count(a[0]));\n" +
                "\n" +
                "let x = 0;\n" +
                "for i in range(0, 3) {\n" +
                "  for j in range(0, 2) {\n" +
                "     a[i][j] = x;\n" +
                "     x = x + 1;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "for i in range(0, 3) {\n" +
                "  for j in range(0, 2) {\n" +
                "    p(\"a[#{i}][#{j}] = #{a[i][j]}\");\n" +
                "  }\n" +
                "}\n";
        Expression expression = AviatorEvaluator.compile(script);
        AviatorEvaluator.execute(script);
    }
}
