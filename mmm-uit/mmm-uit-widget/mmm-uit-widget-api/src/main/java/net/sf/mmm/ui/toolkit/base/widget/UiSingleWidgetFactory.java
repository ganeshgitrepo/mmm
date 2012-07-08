/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.base.widget;

import net.sf.mmm.ui.toolkit.api.widget.UiWidget;

/**
 * This is the interface for a factory of a single {@link UiWidget} of a particular
 * {@link #getWidgetInterface() type}.
 * 
 * @see net.sf.mmm.ui.toolkit.api.widget.UiWidgetFactory
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <WIDGET> the generic type of the {@link #getWidgetInterface() widget} to {@link #create() create}.
 */
public interface UiSingleWidgetFactory<WIDGET extends UiWidget> {

  /**
   * This method gets the interface of the widget interface handled by this factory.
   * 
   * @return the {@link UiWidget} interface.
   */
  Class<WIDGET> getWidgetInterface();

  /**
   * This method creates a new {@link UiWidget} instance of the {@link #getWidgetInterface() managed}
   * {@link UiWidget}.
   * 
   * @see net.sf.mmm.ui.toolkit.api.widget.UiWidgetFactory#create(Class)
   * 
   * @return the new {@link UiWidget}.
   */
  WIDGET create();

}
