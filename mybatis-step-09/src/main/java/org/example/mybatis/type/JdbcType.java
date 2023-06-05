package org.example.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    TIMESTAMP(Types.TIMESTAMP),
    ;


    private int code;

    private static Map<Integer, JdbcType> codeLookup = new HashMap<Integer, JdbcType>();

    static {
        for (JdbcType value : JdbcType.values()) {
            codeLookup.put(value.getCode(), value);
        }
    }

    JdbcType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static JdbcType getType(int code) {
        return codeLookup.get(code);
    }

}
