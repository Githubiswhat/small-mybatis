package org.example.mybatis.builder;

import org.example.mybatis.mapping.ResultMap;
import org.example.mybatis.mapping.ResultMapping;

import java.util.List;

public class ResultMapResolver {

    private final MapperBuilderAssistant assistant;

    private String id;

    private Class<?> type;

    private List<ResultMapping> resultMappings;

    public ResultMapResolver(MapperBuilderAssistant assistant, String id, Class<?> type, List<ResultMapping> resultMappings) {
        this.assistant = assistant;
        this.id = id;
        this.type = type;
        this.resultMappings = resultMappings;
    }

    public ResultMap resolve(){
        return assistant.addResultMap(this.id, this.type, this.resultMappings);
    }

}
