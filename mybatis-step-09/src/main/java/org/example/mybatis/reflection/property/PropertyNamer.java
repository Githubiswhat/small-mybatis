package org.example.mybatis.reflection.property;

import java.util.Locale;

public class PropertyNamer {

    public PropertyNamer() {
    }

    public static String methodToProperty(String name){
        if (name.startsWith("is")){
            name = name.substring(2);
        }else if (name.startsWith("get") || name.startsWith("set")){
            name = name.substring(3);
        }else {
            throw new RuntimeException("Error parsing property name '" + name + "'. Don't start with 'is', 'get' or 'set' .");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))){
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    public static boolean isProperty(String name){
        return name.startsWith("is") || name.startsWith("set") || name.startsWith("get");
    }

    public static boolean isGetter(String name){
        return name.startsWith("is") || name.startsWith("get");
    }

    public static boolean isSetter(String name){
        return name.startsWith("set");
    }


}
