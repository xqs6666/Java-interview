package com.xian.基础;

public class Test2 {
    //比较方式：对于基本数据类型来说，== 比较的是值。对于包装数据类型来说，
    // == 比较的是对象的内存地址。所有整型包装类对象之间值的比较，全部使用 equals() 方法。

    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        System.out.println(a == b); //false
        System.out.println(a.equals(b));

        Integer c=129;//Integer.valueOf(129)
        Integer d=129;//Integer.valueOf(129)
        System.out.println(c==d);

        Integer e=128;//Integer.valueOf(128)
        Integer f=128;//Integer.valueOf(128)
        System.out.println(e==f);
    }

}
