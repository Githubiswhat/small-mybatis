package org.example.mybatis.reflection.property;

import javax.swing.undo.UndoableEditSupport;
import java.util.Iterator;

public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {

    private String name;

    private String indexedName;

    private String index;

    private String children;

    public PropertyTokenizer(String fullName) {
        int delim = fullName.indexOf(".");
        if (delim > -1){
            name = fullName.substring(0, delim);
            children = fullName.substring(delim + 1);
        }else {
            name = fullName;
            children = null;
        }
        indexedName = name;
        delim = name.indexOf("[");
        if (delim > -1){
            index = name.substring(delim + 1, name.length() - 1);
            name = name.substring(0, delim);
        }
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning int the context of properties. ");
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    public String getName() {
        return name;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getIndex() {
        return index;
    }

    public String getChildren() {
        return children;
    }
}
