package com.xian.基础;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Test1 {
    private static float count = 0F;
    public static void method1(String ...a) {

        
        System.out.println(a instanceof String[]);
        System.out.println(Arrays.asList(a));
        for (String s : a){
            System.out.print(s);
        }
    }
    public static void method2(int a) {
        if (a==1) {
            // 表示结束方法的执行,下方的输出语句不会执行
            return;
        }
        System.out.println(a);
    }


    public static void main(String[] args) {
        method1("a","b","c");
        method2(1);
    }
}
