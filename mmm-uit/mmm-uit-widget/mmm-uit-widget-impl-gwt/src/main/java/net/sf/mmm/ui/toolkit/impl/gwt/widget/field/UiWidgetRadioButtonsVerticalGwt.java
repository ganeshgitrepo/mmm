/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.gwt.widget.field;

import net.sf.mmm.ui.toolkit.api.widget.field.UiWidgetRadioButtonsVertical;
import net.sf.mmm.ui.toolkit.base.widget.AbstractUiSingleWidgetFactory;
import net.sf.mmm.ui.toolkit.base.widget.AbstractUiWidgetFactory;
import net.sf.mmm.ui.toolkit.base.widget.field.AbstractUiWidgetOptionsField;
import net.sf.mmm.ui.toolkit.impl.gwt.widget.field.adapter.UiWidgetAdapterGwtCellPanelRadiosVertical;

/**
 * This is the implementation of {@link UiWidgetRadioButtonsVertical} using GWT.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <VALUE> is the generic type of the {@link #getValue() value}.
 */
public class UiWidgetRadioButtonsVerticalGwt<VALUE> extends
    AbstractUiWidgetOptionsField<UiWidgetAdapterGwtCellPanelRadiosVertical<VALUE>, VALUE> implements
    UiWidgetRadioButtonsVertical<VALUE> {

  /**
   * The constructor.
   * 
   * @param factory is the {@link #getFactory() factory}.
   */
  public UiWidgetRadioButtonsVerticalGwt(AbstractUiWidgetFactory<?> factory) {

    super(factory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UiWidgetAdapterGwtCellPanelRadiosVertical<VALUE> createWidgetAdapter() {

    return new UiWidgetAdapterGwtCellPanelRadiosVertical<VALUE>();
  }

  /**
   * This inner class is the {@link AbstractUiSingleWidgetFactory factory} for this widget.
   */
  @SuppressWarnings("rawtypes")
  public static class Factory extends AbstractUiSingleWidgetFactory<UiWidgetRadioButtonsVertical> {

    /**
     * The constructor.
     */
    public Factory() {

      super(UiWidgetRadioButtonsVertical.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public UiWidgetRadioButtonsVertical create(AbstractUiWidgetFactory<?> factory) {

      return new UiWidgetRadioButtonsVerticalGwt(factory);
    }

  }

}
