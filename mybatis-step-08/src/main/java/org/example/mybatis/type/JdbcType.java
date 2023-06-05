package org.example.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {
    Integer(Types.INTEGER),

    DATE(Types.DATE),

    DOUBLE(Types.DOUBLE),

    FLOAT(Types.FLOAT),

    ARRAY(Types.ARRAY),

    CHAR(Types.CHAR);

    private int code;

    private static Map<Integer, JdbcType> codeLookup = new HashMap<>();

    static {
        for (JdbcType value : JdbcType.values()) {
            codeLookup.put(value.code, value);
        }
    }

    JdbcType(int code) {
        this.code = code;
    }

    public JdbcType codeForType(int code){
        return codeLookup.get(code);
    }
}
