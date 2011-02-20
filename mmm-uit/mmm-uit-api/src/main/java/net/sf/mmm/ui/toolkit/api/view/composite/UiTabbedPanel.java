/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.api.view.composite;

import net.sf.mmm.ui.toolkit.api.UiElement;

/**
 * This is the interface for a tabbed panel. Such component is a composite that
 * contains a number of components that can be switched via a tab-header.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface UiTabbedPanel extends UiPanel {

  /** the type of this object */
  String TYPE = "TabbedPanel";

  /**
   * This method adds the given component as new tab on the ride of all existing
   * tabs.
   * 
   * @param component is the component to add. The given component instance must
   *        be created by the same factory.
   * @param title is the title that will be displayed in the tab.
   */
  void addComponent(UiElement component, String title);

  /**
   * This method adds the given component as new tab on the ride of all existing
   * tabs.
   * 
   * @param component is the component to add. The given component instance must
   *        be created by the same factory.
   * @param title is the title that will be displayed in the tab.
   * @param position is the index position where the given component will be
   *        inserted.
   */
  void addComponent(UiElement component, String title, int position);

}