/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.base.widget.field.adapter;

/**
 * This is the interface for a {@link net.sf.mmm.ui.toolkit.base.widget.adapter.UiWidgetAdapter} adapting
 * {@link net.sf.mmm.ui.toolkit.api.widget.field.UiWidgetIntegerField}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <WIDGET> is the generic type of {@link #getWidget()}.
 * @param <VALUE> is the generic type of the {@link #getValue() value}. Typically {@link Integer} but may also
 *        be a custom datatype.
 */
public interface UiWidgetAdapterIntegerField<WIDGET, VALUE> extends
    UiWidgetAdapterTextualInputField<WIDGET, VALUE, Integer> {

  // nothing to add

}