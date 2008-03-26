/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.value.impl;

import java.lang.reflect.Type;

import net.sf.mmm.util.math.MathUtil;
import net.sf.mmm.util.math.NumberType;
import net.sf.mmm.util.nls.base.NlsIllegalArgumentException;
import net.sf.mmm.util.value.api.ValueConverter;

/**
 * This is an implementation of the {@link ValueConverter} interface that
 * converts an {@link Object} to a {@link Number}. It supports objects given as
 * {@link CharSequence} (e.g. {@link String}) or {@link Number}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class PojoPathConverterToNumber implements ValueConverter<Object, Number> {

  /**
   * The constructor.
   */
  public PojoPathConverterToNumber() {

    super();
  }

  /**
   * This method gets the {@link MathUtil} to use.
   * 
   * @return the {@link MathUtil}.
   */
  protected MathUtil getMathUtil() {

    return MathUtil.getInstance();
  }

  /**
   * This method determines if the conversion from one
   * {@link Number number-type} to another should
   * {@link net.sf.mmm.util.math.NumberConversionException fail} if it is
   * unprecise.
   * 
   * @see NumberType#valueOf(Number, boolean)
   * 
   * @return the fail-if-unprecise flag.
   */
  protected boolean isFailIfUnprecise() {

    return true;
  }

  /**
   * {@inheritDoc}
   */
  public Number convert(Object value, Object valueSource, Class<? extends Number> targetClass,
      Type targetType) {

    if (value == null) {
      throw new NlsIllegalArgumentException(null);
    }
    NumberType<? extends Number> numberType = getMathUtil().getNumberType(targetClass);
    if (numberType != null) {
      if (value instanceof Number) {
        return numberType.valueOf((Number) value, isFailIfUnprecise());
      } else if (value instanceof CharSequence) {
        return numberType.valueOf(value.toString());
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public Class<Object> getSourceType() {

    return Object.class;
  }

  /**
   * {@inheritDoc}
   */
  public Class<Number> getTargetType() {

    return Number.class;
  }

}