package org.example.mybatis.builder;

import java.util.HashMap;

public class ParameterExpression extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;

    public ParameterExpression(String expression){
        parser(expression);
    }

    private void parser(String expression) {
        int p = skipWS(expression, 0);
        if (expression.charAt(p) == '('){
            expression(expression, p + 1);
        }else {
            property(expression, p);
        }
    }

    private void property(String expression, int left) {
        if (left < expression.length()){
            int right = skipUntil(expression, left, ",:");
            put("property", trimmedString(expression, left, right));
            jdbcTypeOpt(expression, right);
        }
    }

    private String trimmedString(String str, int start, int end) {
        while (str.charAt(start) <= 0x20){
            start++;
        }
        while (str.charAt(end - 1) <= 0x20){
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }

    private int skipUntil(String expression, int p, String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > - 1){
                return i;
            }
        }
        return expression.length();
    }

    private void expression(String expression, int left) {
        int match = 1;
        int right = left + 1;
        while (match > 0){
            if (expression.charAt(right) == ')'){
                match--;
            }else if (expression.charAt(right) == '('){
                match++;
            }
            right++;
        }
        put("expression", expression.substring(left, right - 1));
        jdbcTypeOpt(expression, right);
    }

    private void jdbcTypeOpt(String expression, int p) {
        p = skipWS(expression, p);
        if (p < expression.length()){
            if (expression.charAt(p) == ':'){
                jdbcType(expression, p + 1);
            }else if (expression.charAt(p) == ','){
                option(expression, p + 1);
            }else {
                throw new RuntimeException("Parsing error in {" + new String(expression) + "} in position " + p);
            }
        }
    }

    private void option(String expression, int i) {

    }

    private void jdbcType(String expression, int p) {
        int left = skipWS(expression, p);
        if (left < expression.length()){
            int right = skipUntil(expression, left, "=");
            String name = trimmedString(expression, left, right);
            left = right + 1;
            right = skipUntil(expression, left, ",");
            String value = trimmedString(expression, left, right);
            put(name, value);
            option(expression, right + 1);
        }
    }

    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
            if (expression.charAt(i) > 0x20){
                return i;
            }
        }
        return expression.length();
    }

}
