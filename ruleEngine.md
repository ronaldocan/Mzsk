# 规则引擎

规则引擎是一种嵌入在应用程序中的组件，实现了将业务决策从应用程序代码中分离出来，并使用预定义的语义模块编写业务决策。接受数据输入，解释业务规则，并根据业务规则做出业务决策。

# 规则引擎选型介绍

目前的规则引擎系统中，使用较多的开源规则引擎是Drools，该规则引擎设计和实现都比较复杂，学习成本高，适用于大型应用系统；接下来主要介绍Drools、Aviator、EasyRules、QLExpress。

## Drools

### 简介

Drools 是用 Java 语言编写的开放源码规则引擎，基于Apache协议，基于RETE算法，于2005年被JBoss收购。

### 特性

1. 简化系统架构，优化应用
2. 提高系统的可维护性和维护成本
3. 方便系统的整合
4. 减少编写“硬代码”业务规则的成本和风险

### 规则配置流程

![规则配置流程](https://p0.meituan.net/travelcube/d3c7fd9501028545fd2a6a7f52b58a02117959.png)

### 接入demo

1. maven依赖

```
<dependencies>
    <dependency>
        <groupId>org.kie</groupId>
        <artifactId>kie-api</artifactId>
        <version>6.5.0.Final</version>
    </dependency>
    <dependency>
        <groupId>org.drools</groupId>
        <artifactId>drools-compiler</artifactId>
        <version>6.5.0.Final</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
</dependencies>
```

2. 新建配置文件/src/resources/META-INF/kmodule.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<kmodule xmlns="http://jboss.org/kie/6.0.0/kmodule">
    <kbase name="rules" packages="rules">
        <ksession name="myAgeSession"/>
    </kbase>
</kmodule>
```

3. 新建drools规则文件/src/resources/rules/age.drl

```
// 导入类
import com.lrq.wechatDemo.domain.User
dialect  "mvel"
// 规则名，唯一
rule "age"                                      
    when
        //规则的条件部分
        $user : User(age<15 || age>60)     
    then
        System.out.println("年龄不符合要求！");
end
```

4. 测试用例

```java
/**
 * CreateBy: ronaldocan
 * CreateDate: 2021/10/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class TestUser {

    private static KieContainer container = null;
    private KieSession statefulKieSession = null;

    @Test
    public void test(){
        KieServices kieServices = KieServices.Factory.get();
        container = kieServices.getKieClasspathContainer();
        statefulKieSession = container.newKieSession("myAgeSession");
        User user = new User("can",12);
        statefulKieSession.insert(user);
        statefulKieSession.fireAllRules();
        statefulKieSession.dispose();
    }
}
```

**优点**

* 功能较为完善，具有如系统监控、操作平台等功能。

**缺点**

* 学习曲线陡峭，其引入的DRL语言较复杂，独立的系统很难进行二次开发。
* 因为Rete算法是空间换时间，所以当规则很多的时候，可能会很耗系统资源。

## Aviator

### 简介

Aviator是一个高性能、轻量级的 java 语言实现的[表达式求值](https://so.csdn.net/so/search?from=pc_blog_highlight&q=%E8%A1%A8%E8%BE%BE%E5%BC%8F%E6%B1%82%E5%80%BC)引擎，它动态地将表达式编译成**字节码**并运行，主要用于各种表达式的动态求值。

### 特性

1. 支持绝大多数运算操作符，包括算术操作符、关系运算符、逻辑操作符、位运算符、正则匹配操作符(=~)、三元表达式(?: )。
2. 支持操作符优先级和括号强制设定优先级。
3. 逻辑运算符支持短路运算。
4. 支持丰富类型，例如nil、整数和浮点数、字符串、正则表达式、日期、变量等，支持自动类型转换。
5. 内置一套强大的常用函数库。
6. 可自定义函数，易于扩展。
7. 可重载操作符。
8. 支持大数运算(BigInteger)和高精度运算(BigDecimal)

### 接入

1. maven 依赖

```
<dependency>
	<groupId>com.googlecode.aviator</groupId>
        <artifactId>aviator</artifactId>
        <version>3.3.0</version>
</dependency>
```

2. demo

（1） 带入参数求和

```
package x.expression.aviator;
import java.util.HashMap;
import java.util.Map;
import com.googlecode.aviator.AviatorEvaluator;

public class AviatorDemo {
public static void main(String[] args) {

    String expression = "a + b + c";
    Map<String, Object> params = new HashMap<>();
    params.put("a", 1);
    params.put("b", 2);
    params.put("c", 3);

    long result = (long) AviatorEvaluator.execute(expression, params);
    System.out.printf("result : " + result);
}}
```

（2） 自定义函数

```
package rona.expression.aviator;
import java.util.HashMap;
import java.util.Map;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;
 
public class AviatorSelfFunctionDemo {
 
    public static void main(String[] args) {
        //注册函数
        AviatorEvaluator.addFunction(new MySumFunction());
        String expression = "my_sum(a,b,c)";
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);
        params.put("c", 3);
 
        long result = (long) AviatorEvaluator.execute(expression, params);
        System.out.printf("result : " + result);
    }
 
 
    /**
     * 自定义函数，实现三元数据求和
     */
    static class MySumFunction extends AbstractFunction {
 
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject a, AviatorObject b, AviatorObject c) {
            Number numA = FunctionUtils.getNumberValue(a, env);
            Number numB = FunctionUtils.getNumberValue(b, env);
            Number numC = FunctionUtils.getNumberValue(c, env);
 
            long result = numA.longValue() + numB.longValue() + numC.longValue();
            return new AviatorLong(result);
        }
 
        /**
         * 获取函数名
         *
         * @return 函数名
         */
        public String getName() {
            return "my_sum";
        }
    }
}
```



**优点**

* Aviator则是直接将表达式编译成Java字节码，交给JVM去执行，性能较好

**缺点**

* Aviator的限制，没有if else、do while等语句，没有赋值语句，仅支持逻辑表达式、算术表达式、三元表达式和正则匹配，不支持位运算符。

## EasyRules

### 简介

Easy Rules 是一款 Java 规则引擎，它的诞生启发自有Martin Fowler 一篇名为 “Should I use a Rules Engine?” 文章。Easy Rules 提供了规则抽象来创建带有条件和操作的规则，以及运行一组规则来评估条件和执行操作的RulesEngine API。

### 特性：

1. 轻量级框架和易于学习的API。
2. 基于POJO的开发。
3. 通过高效的抽象来定义业务规则并轻松应用它们。
4. 支持创建复合规则。
5. 使用表达式语言定义规则的能力。

### 接入

1. maven 依赖

```
<dependency>
    <groupId>org.jeasy</groupId>
    <artifactId>easy-rules-core</artifactId>
    <version>4.0.0</version>
</dependency>
```

2. 新建rule

```
@Rule(name = "weather rule", description = "if it rains then take an umbrella")
public class WeatherRule {
	@Condition
	public boolean itRains(@Fact("rain") boolean rain) {
    		return rain;
	}
	@Action
	public void takeAnUmbrella() {
    		System.out.println("It rains, take an umbrella!");
	}
}
```

```
Rule weatherRule = new RuleBuilder()
        .name("weather rule")
        .description("if it rains then take an umbrella")
        .when(facts -> facts.get("rain").equals(true))
        .then(facts -> System.out.println("It rains, take an umbrella!"))
        .build();
```

```
Rule weatherRule = new MVELRule()
        .name("weather rule")
        .description("if it rains then take an umbrella")
        .when("rain == true")
        .then("System.out.println(\"It rains, take an umbrella!\");");
```

3. 测试用例

```
public class Test {
    public static void main(String[] args) {
        // define facts
        Facts facts = new Facts();
        facts.put("rain", true);

        // define rules
        Rule weatherRule = ...
        Rules rules = new Rules();
        rules.register(weatherRule);

        // fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
    }
}
```

**优点**

* 学习成本低
* 支持使用表达式语言（如MVEL和SpEL）定义规则的能力

**缺点**

* 不包含规则编排功能，如果规则的条件本身是很复杂的，只能自己对这些条件进行编排

## QLExpress

### 简介

QLExpress从一开始就是从复杂的阿里电商业务系统出发，并且不断完善的脚本语言解析引擎框架，在不追求java语法的完整性的前提下（比如异常处理，foreach循环，lambda表达式，这些都是groovy是强项），定制了很多普遍存在的业务需求解决方案（比如变量解析，spring打通，函数封装，操作符定制，宏替换），同时在高性能、高并发、线程安全等方面也下足了功夫，久经考验。

### 特性

1. 线程安全，引擎运算过程中的产生的临时变量都是threadlocal类型。
2. 高效执行，比较耗时的脚本编译过程可以缓存在本地机器，运行时的临时变量创建采用了缓冲池的技术，和groovy性能相当。
3. 弱类型脚本语言，和groovy，javascript语法类似，虽然比强类型脚本语言要慢一些，但是使业务的灵活度大大增强。
4. 安全控制,可以通过设置相关运行参数，预防死循环. 高危系统api调用等情况。
5. 代码精简，依赖最小，250k的jar包适合所有java的运行环境。

### 接入demo

1. maven依赖

```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>QLExpress</artifactId>
    <version>3.2.0</version>
</dependency>
```

2. demo

```
//1.语法分析和计算
ExpressRunner runner = new ExpressRunner();
//2.存储上下文信息
DefaultContext<String, Object> context = new DefaultContext<String, Object>();
context.put("a",1);
context.put("b",2);
context.put("c",3);
String express = "a+b*c";
//3.计算结果
Object result = runner.execute(express, context, null, true, false);
System.out.println(result);
```
**优点**

* 容量小，性能强

**缺点**

* 开源文档对比前二者少，有一定的学习成本

# 选型结果对比

| 规则引擎 | 学习成本  | 维护成本 | 规则编排能力 | 执行效率 | 是否开源 |
| :---: | :---: | :---: | :---: | :---: | :---: |
| Drools |高  | 高 | 强| 高 | 是 |
|  Aviator| 一般 | 一般 |弱|较高|是 |
|  EasyRules| 低 | 低 |弱|较低|是 |
|  QLExpress| 一般 | 一般 |一般|较高|是 |

# 相关链接
* [Drools](https://www.drools.org/)
* [Aviator](https://github.com/killme2008/aviatorscript)
* [EasyRules](https://github.com/j-easy/easy-rules)
* [QLExpress](https://github.com/alibaba/QLExpress)
* [Flink整合Aviator](https://mp.weixin.qq.com/s/mh--wQvAWQq2tDPKq0-m8Q)
