package org.example.mybatis.scripting.xmltags;

import java.util.List;

public class MixedSqlNode implements SqlNode{

    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        contents.forEach(node -> node.apply(context));
        return true;
    }
}
