/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.reflect.pojo.descriptor.base;

import java.util.Collection;

import net.sf.mmm.util.reflect.pojo.descriptor.api.PojoDescriptor;
import net.sf.mmm.util.reflect.pojo.descriptor.api.accessor.PojoPropertyAccessorIndexedNonArgMode;
import net.sf.mmm.util.reflect.pojo.descriptor.api.accessor.PojoPropertyAccessorIndexedOneArgMode;
import net.sf.mmm.util.reflect.pojo.descriptor.api.accessor.PojoPropertyAccessorNonArgMode;
import net.sf.mmm.util.reflect.pojo.descriptor.api.accessor.PojoPropertyAccessorOneArgMode;
import net.sf.mmm.util.reflect.pojo.descriptor.api.accessor.PojoPropertyAccessorTwoArgMode;
import net.sf.mmm.util.reflect.pojo.descriptor.impl.PojoPropertyDescriptorImpl;

/**
 * This is the abstract base implementation of the {@link PojoDescriptor}
 * interface.
 * 
 * @param <POJO> is the templated type of the {@link #getPojoType() POJO}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public abstract class AbstractPojoDescriptor<POJO> implements PojoDescriptor<POJO> {

  /** @see #getPojoType() */
  private final Class<POJO> pojoType;

  /**
   * The constructor.
   * 
   * @param pojoClass is the {@link #getPojoType() pojo-class}.
   */
  public AbstractPojoDescriptor(Class<POJO> pojoClass) {

    this.pojoType = pojoClass;
  }

  /**
   * {@inheritDoc}
   */
  public Class<POJO> getPojoType() {

    return this.pojoType;
  }

  /**
   * {@inheritDoc}
   */
  public abstract Collection<? extends AbstractPojoPropertyDescriptor> getPropertyDescriptors();

  /**
   * This method gets the property-descriptor for the given
   * <code>propertyName</code>.
   * 
   * @param propertyName is the name of the requested property-descriptor.
   * @return the requested property-descriptor or <code>null</code> if NO
   *         property exists with the given <code>propertyName</code>.
   */
  public abstract PojoPropertyDescriptorImpl getOrCreatePropertyDescriptor(String propertyName);

  /**
   * {@inheritDoc}
   */
  public Object getProperty(POJO pojoInstance, String propertyName) {

    Property property = new Property(propertyName);
    if (property.getIndex() != null) {
      return getAccessor(property.getName(), PojoPropertyAccessorIndexedNonArgMode.GET_INDEXED,
          true).invoke(pojoInstance, property.getIndex().intValue());
    } else if (property.getKey() != null) {
      return getAccessor(property.getName(), PojoPropertyAccessorOneArgMode.GET_MAPPED, true)
          .invoke(pojoInstance, property.getKey());
    } else {
      return getAccessor(propertyName, PojoPropertyAccessorNonArgMode.GET, true).invoke(
          pojoInstance);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object setProperty(POJO pojoInstance, String propertyName, Object value) {

    Property property = new Property(propertyName);
    if (property.getIndex() != null) {
      return getAccessor(property.getName(), PojoPropertyAccessorIndexedOneArgMode.SET_INDEXED,
          true).invoke(pojoInstance, property.getIndex().intValue(), value);
    } else if (property.getKey() != null) {
      return getAccessor(property.getName(), PojoPropertyAccessorTwoArgMode.SET_MAPPED, true)
          .invoke(pojoInstance, property.getKey(), value);
    } else {
      return getAccessor(propertyName, PojoPropertyAccessorOneArgMode.SET, true).invoke(
          pojoInstance, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public int getPropertySize(POJO pojoInstance, String propertyName) {

    Object result = getAccessor(propertyName, PojoPropertyAccessorNonArgMode.GET_SIZE, true).invoke(
        pojoInstance);
    try {
      Number size = (Number) result;
      return size.intValue();
    } catch (ClassCastException e) {
      throw new IllegalStateException("Size of property '" + propertyName + "' in pojo '"
          + pojoInstance + "' is no number: " + result);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object addPropertyItem(POJO pojoInstance, String propertyName, Object item) {

    return getAccessor(propertyName, PojoPropertyAccessorOneArgMode.ADD, true).invoke(pojoInstance,
        item);
  }

  /**
   * {@inheritDoc}
   */
  public Boolean removePropertyItem(POJO pojoInstance, String propertyName, Object item) {

    Object result = getAccessor(propertyName, PojoPropertyAccessorOneArgMode.REMOVE, true).invoke(
        pojoInstance, item);
    if (result instanceof Boolean) {
      return (Boolean) result;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public Object getPropertyItem(POJO pojoInstance, String propertyName, int index) {

    return getAccessor(propertyName, PojoPropertyAccessorIndexedNonArgMode.GET_INDEXED).invoke(
        pojoInstance, index);
  }

  /**
   * {@inheritDoc}
   */
  public Object setPropertyItem(POJO pojoInstance, String propertyName, int index, Object item) {

    return getAccessor(propertyName, PojoPropertyAccessorIndexedOneArgMode.SET_INDEXED).invoke(
        pojoInstance, index, item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {

    return "Descriptor for POJO " + this.pojoType;
  }

}
