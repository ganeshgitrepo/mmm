/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.pojo.descriptor.api.accessor;

import java.lang.reflect.AccessibleObject;

import net.sf.mmm.util.pojo.descriptor.api.attribute.PojoAttributeName;
import net.sf.mmm.util.reflect.api.GenericType;

/**
 * A {@link PojoPropertyAccessor} gives {@link #getAccessibleObject() access} to
 * a specific {@link #getName() property} of a POJO.<br>
 * 
 * @see net.sf.mmm.util.pojo.descriptor.api.PojoPropertyDescriptor
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public abstract interface PojoPropertyAccessor extends PojoAttributeName {

  /**
   * This method gets the mode of this accessor.
   * 
   * @return the access mode.
   */
  PojoPropertyAccessorMode<? extends PojoPropertyAccessor> getMode();

  /**
   * This method gets the {@link AccessibleObject accessible object} used to
   * access (read/write) this property.
   * 
   * @see java.beans.PropertyDescriptor#getReadMethod()
   * @see java.lang.reflect.Method
   * @see java.lang.reflect.Constructor
   * @see java.lang.reflect.Field
   * 
   * @return the method to read this property or <code>null</code> if this
   *         property is NOT readable.
   */
  AccessibleObject getAccessibleObject();

  /**
   * This method gets the {@link java.lang.reflect.Modifier modifiers} of the
   * {@link #getAccessibleObject() accessible object}.
   * 
   * @see java.lang.reflect.Modifier
   * @see java.lang.reflect.Field#getModifiers()
   * @see java.lang.reflect.Method#getModifiers()
   * 
   * @return the modifiers of the field or method.
   */
  int getModifiers();

  /**
   * This method gets the {@link GenericType} of the object returned when this
   * accessor is <code>invoked</code>.
   * 
   * @see java.lang.reflect.Method#getGenericReturnType()
   * @see java.lang.reflect.Field#getGenericType()
   * 
   * @return the return type.
   */
  GenericType getReturnType();

  /**
   * This method gets the {@link Class} of the object returned when this
   * accessor is <code>invoked</code>.<br>
   * is a convenience method for {@link #getReturnType()}.{@link GenericType#getUpperBound() getUpperBound()}
   * 
   * @see java.lang.reflect.Method#getReturnType()
   * @see java.lang.reflect.Field#getType()
   * 
   * @return the return class.
   */
  Class<?> getReturnClass();

  /**
   * This method gets the {@link GenericType} of this property.<br>
   * For a {@link PojoPropertyAccessorNonArgMode#GET getter} this will be the
   * {@link #getReturnType() return-type} while a
   * {@link PojoPropertyAccessorOneArgMode#SET setter} typically has
   * <code>void</code> as {@link #getReturnType() return-type} and this method
   * will return the type of its argument. For mapped or indexed getters/setters
   * this method will return the item type.
   * 
   * @see #getPropertyClass()
   * @see java.beans.PropertyDescriptor#getPropertyType()
   * 
   * @return the {@link GenericType} reflecting the property.
   */
  GenericType getPropertyType();

  /**
   * This method gets the {@link #getPropertyType() type} as {@link Class}. It
   * is a convenience method for {@link #getPropertyType()}.{@link GenericType#getUpperBound() getUpperBound()}
   * if the {@link #getMode() mode} is for
   * {@link PojoPropertyAccessorMode#isReading() reading} and
   * {@link #getPropertyType()}.{@link GenericType#getLowerBound() getLowerBound()}
   * otherwise.
   * 
   * @return the {@link Class} reflecting the property.
   */
  Class<?> getPropertyClass();

  /**
   * This method gets the class reflecting the type that declared this accessor.
   * It may be the pojo-class this accessor was created from but it may also be
   * a super-class where the {@link #getAccessibleObject() field or method} of
   * this accessor is inherited from and is NOT overridden.<br>
   * E.g. the read-accessor for the property "class" will have the
   * declaring-class {@link java.lang.Object}.
   * 
   * @return the class reflecting the type that declared this accessor.
   */
  Class<?> getDeclaringClass();

}
