package com.xian.基础;

public class Test3 {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("xqs").append("yuan");
        stringBuilder.insert(3,"666");
        System.out.println(stringBuilder);

        String[] arr1 = {"he", "llo", "world"};
        String a="";
        for (int i = 0; i < arr1.length; i++) {
            a=a+arr1[i];
        }
        System.out.println(a);

        String[] arr2 = {"he", "llo", "world"};
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < arr2.length; i++) {
            string.append(arr2[i]);
        }
        System.out.println(string.toString());
        
    }
}
