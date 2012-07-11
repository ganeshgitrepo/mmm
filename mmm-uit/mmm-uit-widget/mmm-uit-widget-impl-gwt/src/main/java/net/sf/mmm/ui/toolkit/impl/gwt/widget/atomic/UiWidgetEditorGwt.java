/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.gwt.widget.atomic;

import net.sf.mmm.ui.toolkit.api.common.IconConstants;
import net.sf.mmm.ui.toolkit.api.handler.event.UiHandlerEventFocus;
import net.sf.mmm.ui.toolkit.api.handler.event.UiHandlerEventValueChange;
import net.sf.mmm.ui.toolkit.api.widget.atomic.UiWidgetEditor;
import net.sf.mmm.ui.toolkit.impl.gwt.handler.event.ChangeEventSenderGwt;
import net.sf.mmm.ui.toolkit.impl.gwt.handler.event.FocusEventSenderGwt;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the implementation of {@link UiWidgetEditor} using GWT.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <VALUE>
 * @param <WIDGET>
 */
public abstract class UiWidgetEditorGwt<VALUE, WIDGET extends Widget> extends UiWidgetAtomicRegularGwt<WIDGET>
    implements UiWidgetEditor<VALUE> {

  /** @see #addChangeHandler(UiHandlerEventValueChange) */
  private ChangeEventSenderGwt<VALUE> changeEventSender;

  /** @see #addFocusHandler(UiHandlerEventFocus) */
  private FocusEventSenderGwt focusEventSender;

  /** @see #getLabel() */
  private Label label;

  /** The alternative widget to display the value in view-mode (read-only). */
  private Label view;

  /** The icon for {@link #getValidationError()}. */
  private Image errorIcon;

  /** @see #getValidationError() */
  private String validationError;

  /**
   * The constructor.
   * 
   * @param widget is the {@link #getToplevelWidget() widget}.
   */
  public UiWidgetEditorGwt(WIDGET widget) {

    super(widget);
    this.label = new Label();
    this.errorIcon = new Image(IconConstants.ICON_VALIDATION_ERROR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLabel() {

    return this.label.getText();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLabel(String label) {

    this.label.setText(label);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChangeHandler(UiHandlerEventValueChange<VALUE> handler) {

    if (this.changeEventSender == null) {
      this.changeEventSender = new ChangeEventSenderGwt<VALUE>(this);
      registerChangeHandler(this.changeEventSender);
    }
    this.changeEventSender.addHandler(handler);
  }

  /**
   * This method has to be implemented to register the given {@link ChangeHandler} in the
   * {@link #getActiveWidget() active widget}.
   * 
   * @param handler is the {@link ChangeHandler}.
   */
  protected abstract void registerChangeHandler(ChangeHandler handler);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeChangeHandler(UiHandlerEventValueChange<VALUE> handler) {

    if (this.changeEventSender != null) {
      return this.changeEventSender.removeHandler(handler);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setValue(VALUE value) {

    doSetValue(value);
    if (this.changeEventSender != null) {
      this.changeEventSender.onValueChange(this, true);
    }
  }

  /**
   * Called from {@link #setValue(Object)} to set the value in the {@link #getActiveWidget() active widget}.
   * 
   * @param value is the new value to set.
   */
  protected abstract void doSetValue(VALUE value);

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFocusHandler(UiHandlerEventFocus handler) {

    if (this.focusEventSender == null) {
      this.focusEventSender = new FocusEventSenderGwt(this);

    }
    this.focusEventSender.addHandler(handler);
  }

  /**
   * This method has to be implemented to register the given {@link FocusHandler} and {@link BlurHandler} in
   * the {@link #getActiveWidget() active widget}.
   * 
   * @param focusHandler is the {@link FocusHandler}.
   * @param blurHandler is the {@link BlurHandler}.
   */
  protected abstract void registerFocusHandler(FocusHandler focusHandler, BlurHandler blurHandler);

  /**
   * {@inheritDoc}
   */
  public boolean removeFocusHandler(UiHandlerEventFocus handler) {

    if (this.focusEventSender != null) {
      return this.focusEventSender.removeHandler(handler);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValidationError() {

    return this.validationError;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValidationError(String validationError) {

    this.validationError = validationError;
    this.errorIcon.setTitle(validationError);
    this.errorIcon.setVisible((validationError != null));
  }

}