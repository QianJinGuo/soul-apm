package com.furioussoulk.default_method;

import com.furioussoulk.delegate.p5.Main;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.SuperMethodCall;

import java.util.Arrays;

import static net.bytebuddy.matcher.ElementMatchers.named;


/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 16:52
 */
public class Main2 {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {

        new ByteBuddy()
                .subclass(Object.class)
                .annotateType(new RuntimeDefinitionImpl())
                .make();

        DefaultObj ins = new ByteBuddy()
                .subclass(DefaultObj.class)
                .annotateType(new RuntimeDefinitionImpl())
                .method(named("toString"))
                .intercept(SuperMethodCall.INSTANCE)
                .annotateMethod(new RuntimeDefinitionImpl())
                .defineField("foo", Object.class)
                .annotateField(new RuntimeDefinitionImpl())
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        System.out.println(ins.toString());
    }
}
