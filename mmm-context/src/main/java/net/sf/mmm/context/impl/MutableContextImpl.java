/* $Id$ */
package net.sf.mmm.context.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.mmm.context.api.Context;
import net.sf.mmm.context.api.MutableContext;
import net.sf.mmm.value.api.GenericValue;
import net.sf.mmm.value.api.ValueNotSetException;
import net.sf.mmm.value.base.EmptyValue;
import net.sf.mmm.value.impl.ObjectValue;
import net.sf.mmm.value.impl.StringValue;

/**
 * This class is the basic implementation of the {@link Context} interface.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class MutableContextImpl implements MutableContext {

    /** maps variable names to {@link #getValue(String) values} */
    private final Map<String, GenericValue> variableTable;

    /** the parent context */
    private final Context parent;

    /** the {@link #getImmutableContext() "immutable context"} */
    private final Context immutableContext;

    /**
     * The constructor for a root-context.
     */
    public MutableContextImpl() {

        this(null);
    }

    /**
     * The constructor for a {@link #createChildContext() sub-context}.
     * 
     * @param parentContext
     *        is the context the created one will derive from.
     */
    public MutableContextImpl(Context parentContext) {

        super();
        this.parent = parentContext;
        this.variableTable = new HashMap<String, GenericValue>();
        this.immutableContext = new ContextProxy(this);
    }

    /**
     * @see net.sf.mmm.context.api.Context#getValue(java.lang.String)
     *      
     * 
     * @param variableName
     *        is the name of the requested context value.
     * @return the requested value or <code>null</code> if no such value
     *         exists.
     */
    private GenericValue get(String variableName) {

        GenericValue result = this.variableTable.get(variableName);
        if ((result == null) && (this.parent != null)) {
            result = this.parent.getValue(variableName);
        }
        return result;
    }

    /**
     * @see net.sf.mmm.context.api.Context#getValue(java.lang.String)
     */
    public GenericValue getValue(String variableName) {

        GenericValue result = get(variableName);
        if (result == null) {
            result = new EmptyValue(variableName);
        }
        return result;
    }

    /**
     * @see net.sf.mmm.context.api.Context#getObject(java.lang.String)
     */
    public Object getObject(String variableName) {

        GenericValue value = get(variableName);
        if (value == null) {
            throw new ValueNotSetException(variableName);
        }
        return value.getObject(null);
    }

    /**
     * @see net.sf.mmm.context.api.Context#hasValue(java.lang.String)
     */
    public boolean hasValue(String variableName) {

        if (this.variableTable.containsKey(variableName)) {
            return true;
        } else {
            if (this.parent == null) {
                return false;
            } else {
                return this.parent.hasValue(variableName);
            }
        }
    }

    /**
     * @see net.sf.mmm.context.api.Context#getVariableNames() 
     */
    public Set<String> getVariableNames() {

        Set<String> result;
        if (this.parent == null) {
            result = new HashSet<String>();
        } else {
            result = this.parent.getVariableNames();
        }
        result.addAll(this.variableTable.keySet());
        return result;
    }

    /**
     * @see net.sf.mmm.context.api.MutableContext#setValue(java.lang.String,
     *      net.sf.mmm.value.api.GenericValue) 
     */
    public void setValue(String variableName, GenericValue value) {

        this.variableTable.put(variableName, value);
    }

    /**
     * @see net.sf.mmm.context.api.MutableContext#setObject(java.lang.String,
     *      java.lang.Object) 
     */
    public void setObject(String variableName, Object value) {

        if ((value != null) && (value instanceof String)) {
            setValue(variableName, new StringValue((String) value));
        } else {
            setValue(variableName, new ObjectValue(value));
        }
    }

    /**
     * @see net.sf.mmm.context.api.Context#createChildContext() 
     */
    public MutableContext createChildContext() {

        return new MutableContextImpl(this);
    }

    /**
     * @see net.sf.mmm.context.api.MutableContext#unsetValue(java.lang.String)
     */
    public void unsetValue(String variableName) {

        this.variableTable.remove(variableName);
    }

    /**
     * @see net.sf.mmm.context.api.MutableContext#getImmutableContext()
     */
    public Context getImmutableContext() {

        return this.immutableContext;
    }

}