package com.tom.common.view;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 10:43
 */



import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaClass;


public class OgnlDynaClass extends LazyDynaClass {

    private static final long serialVersionUID = 1L;

    ValueStack valueStack;

    /**
     * Constructor
     *
     * @param valueStack
     */
    public OgnlDynaClass(ValueStack valueStack) {
        super();
        this.valueStack = valueStack;
        setReturnNull(true);
    }

    /**
     * getDynaProperty
     *
     * @param name
     * @return DynaProperty
     */
    public DynaProperty getDynaProperty(String name) {
        DynaProperty property = super.getDynaProperty(name);
        if (null == property) {
            Object value = valueStack.findValue(name);
            if (null != value) {
                property = new DynaProperty(name, value.getClass());
            }
        }
        return property;
    }
}