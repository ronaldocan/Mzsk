package com.rona.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author zhengguican
 * @date 2021/11/01 15:56:45
 */
@SpringBootTest
public class AviatorFunctionTest {

    @Test
    public void testAviatorFunction() {

        AviatorEvaluator.addFunction(new MultiFunction());
        System.out.println(AviatorEvaluator.execute("mult(2,2)"));
        System.out.println(AviatorEvaluator.execute("mult(mult(2,2),100)"));
    }

    class MultiFunction extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return FunctionUtils.wrapReturn(left.doubleValue() * right.doubleValue());
        }

        @Override
        public String getName() {
            return "mult";
        }

    }
}
