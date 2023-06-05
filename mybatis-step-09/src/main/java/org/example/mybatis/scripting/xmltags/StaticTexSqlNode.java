package org.example.mybatis.scripting.xmltags;

public class StaticTexSqlNode implements SqlNode{

    private String text;

    public StaticTexSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext context) {
        context.appendSql(text);
        return true;
    }
}
