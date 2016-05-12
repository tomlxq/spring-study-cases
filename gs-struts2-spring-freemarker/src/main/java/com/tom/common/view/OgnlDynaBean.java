package com.tom.common.view;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 10:42
 */
/**
 * User: tom
 * Date: 14-3-19 上午10:55
 */

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.beanutils.LazyDynaBean;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;


public class OgnlDynaBean extends LazyDynaBean {
    // ~ Instance fields ////////////////////////////////////////////////////////

    private static final long serialVersionUID = 1L;

    /**
     * valueStack reference
     */
    ValueStack valueStack;

    // ~ Constructors ///////////////////////////////////////////////////////////

    /**
     * Constructs an instance of OgnlValueStackShadowMap.
     *
     * @param valueStack -
     *          the underlying valuestack
     */
    public OgnlDynaBean(ValueStack valueStack) {
        super(new OgnlDynaClass(valueStack));
        this.valueStack = valueStack;
    }

    // ~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Implementation of get(), overriding LazyDynaBean implementation.
     *
     * @param key -
     *          The key to get in DynaBean and if not found there from the
     *          valueStack.
     * @return value - The object from DynaBean or if null, from the valueStack.
     */
    public Object get(String key) {
        Object value = super.get(key);
        if (value != null) {
            if (value instanceof Map) {
                if (((Map) value).size() <= 0) {
                    value = null;
                }
            }

            if (value instanceof List) {
                if (((List) value).size() <= 0) {
                    value = null;
                }
            }
        }
        if ((value == null) && key != null) {
            value = valueStack.findValue(key);
        }

        return value;
    }

    public Object get(String name, String key) {
        Object value = super.get(name, key);
        if (value == null) {
            Object map = valueStack.findValue(name);
            if (map instanceof Map) {
                value = ((Map) map).get(key);
            }
        }
        return value;
    }

    public Object get(String name, int index) {
        Object value = super.get(name, index);
        if (value == null) {
            Object array = valueStack.findValue(name);
            if (array.getClass().isArray()) {
                return Array.get(array, index);
            } else if (array instanceof List) {
                return ((List) array).get(index);
            }
        }
        return value;
    }
}

