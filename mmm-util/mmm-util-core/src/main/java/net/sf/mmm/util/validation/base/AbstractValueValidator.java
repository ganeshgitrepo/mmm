/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.validation.base;

import net.sf.mmm.util.validation.api.ValidationFailure;
import net.sf.mmm.util.validation.api.ValueValidator;

/**
 * This is the abstract base implementation of {@link ValueValidator}.
 * 
 * @param <V> is the generic type of the value to {@link #validate(Object) validate}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 3.0.0
 */
public abstract class AbstractValueValidator<V> implements ValueValidator<V> {

  /**
   * The constructor.
   */
  public AbstractValueValidator() {

    super();
  }

  /**
   * This is the default implementation to retrieve the {@link ValidationFailure#getCode() code} of this
   * {@link ValueValidator}.<br/>
   * <b>ATTENTION:</b><br/>
   * This default implementation returns the {@link Class#getSimpleName() classname} of the actual
   * {@link ValueValidator} implementation. This strategy is chosen for simplicity when implementing a new
   * validator. To ensure stable codes override this method and return a string constant. This shall at least
   * be done when the name of the class is changed.
   * 
   * @return the {@link ValidationFailure#getCode() code}.
   */
  protected String getCode() {

    return getClass().getSimpleName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final ValidationFailure validate(V value) {

    return validate(value, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ValidationFailure validate(V value, Object valueSource) {

    String failureMessage;
    if (value == null) {
      failureMessage = validateNull();
    } else {
      failureMessage = validateNotNull(value);
    }
    ValidationFailure result = null;
    if (failureMessage != null) {
      String source = null;
      if (valueSource != null) {
        source = valueSource.toString();
      }
      result = new SimpleValidationFailure(getCode(), source, failureMessage);
    }
    return result;
  }

  /**
   * This method performs the validation in case <code>null</code> was provided as value. By default
   * <code>null</code> should be considered as a legal value. Only for validators such as
   * {@link ValidatorMandatory} this method should be overridden.
   * 
   * @return the {@link ValidationFailure#getMessage() failure message} or <code>null</code> if the
   *         <code>null</code>-value is valid.
   */
  protected String validateNull() {

    return null;
  }

  /**
   * This method performs the validation in case <code>value</code> is NOT <code>null</code>. This method
   * contains the actual custom logic for the validation. It is therefore designed in a way that makes it most
   * simple to implement custom validators.<br/>
   * <b>ATTENTION:</b><br/>
   * For internationalization you should not directly return string literals but use
   * {@link net.sf.mmm.util.nls.api.NlsMessage} instead.
   * 
   * @param value is the value to validate.
   * @return the {@link ValidationFailure#getMessage() failure message} or <code>null</code> if the the given
   *         <code>value</code> is valid.
   */
  protected abstract String validateNotNull(V value);

}
