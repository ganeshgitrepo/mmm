/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.reflect.pojo.impl.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.mmm.util.reflect.pojo.api.accessor.PojoPropertyAccessorIndexedNonArg;
import net.sf.mmm.util.reflect.pojo.api.accessor.PojoPropertyAccessorIndexedNonArgBuilder;
import net.sf.mmm.util.reflect.pojo.api.accessor.PojoPropertyAccessorIndexedNonArgMode;
import net.sf.mmm.util.reflect.pojo.api.accessor.PojoPropertyAccessorOneArgBuilder;
import net.sf.mmm.util.reflect.pojo.api.accessor.PojoPropertyAccessorOneArgMode;
import net.sf.mmm.util.reflect.pojo.base.accessor.AbstractPojoPropertyAccessorBuilder;

/**
 * This is the implementation of the {@link PojoPropertyAccessorOneArgBuilder}
 * interface for
 * {@link PojoPropertyAccessorOneArgMode#GET_MAPPED mapped getter-access}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class PojoPropertyAccessorGetIndexedBuilder extends
    AbstractPojoPropertyAccessorBuilder<PojoPropertyAccessorIndexedNonArg> implements
    PojoPropertyAccessorIndexedNonArgBuilder {

  /** method name prefix for classic getter. */
  private static final String METHOD_PREFIX_GET = "get";

  /** alternative method name prefixes for boolean getters. */
  private static final String[] METHOD_PREFIXES = new String[] { METHOD_PREFIX_GET, "is", "has" };

  /** method name suffixes for indexed getters. */
  private static final String[] METHOD_SUFFIXES = new String[] { "", "At" };

  /**
   * The constructor.
   */
  public PojoPropertyAccessorGetIndexedBuilder() {

    super();
  }

  /**
   * {@inheritDoc}
   */
  public PojoPropertyAccessorIndexedNonArg create(Method method) {

    Class<?>[] parameterTypes = method.getParameterTypes();
    if (parameterTypes.length == 1) {
      Class<?> argumentClass = parameterTypes[0];
      if ((argumentClass == int.class) || (argumentClass == Integer.class)) {
        Class<?> propertyClass = method.getReturnType();
        if (propertyClass != Void.class) {
          String methodName = method.getName();
          // is property read method (getter)?
          String propertyName = getPropertyName(methodName, METHOD_PREFIXES, METHOD_SUFFIXES);
          boolean isBooblean = isBooleanType(propertyClass);
          if (!isBooblean && !propertyName.startsWith(METHOD_PREFIX_GET)) {
            // only boolean getters may use is* or has* ...
            propertyName = null;
          }
          if (propertyName != null) {
            return new PojoPropertyAccessorIndexedNonArgMethod(propertyName, method
                .getGenericReturnType(), propertyClass, method,
                PojoPropertyAccessorIndexedNonArgMode.GET_INDEXED);
          }
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public PojoPropertyAccessorIndexedNonArg create(Field field) {

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public PojoPropertyAccessorIndexedNonArgMode getMode() {

    return PojoPropertyAccessorIndexedNonArgMode.GET_INDEXED;
  }

}