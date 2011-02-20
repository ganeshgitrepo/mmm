/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.swing.composite;

import net.sf.mmm.ui.toolkit.api.UiElement;
import net.sf.mmm.ui.toolkit.api.UiNode;
import net.sf.mmm.ui.toolkit.api.view.composite.UiPanel;
import net.sf.mmm.ui.toolkit.impl.swing.AbstractUIComponent;
import net.sf.mmm.ui.toolkit.impl.swing.UIFactorySwing;

/**
 * This class is an abstract base implementation of the {@link UiPanel}
 * interface using swing as the UI toolkit.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractUIPanel extends AbstractUIMultiComposite implements UiPanel {

  /**
   * The constructor.
   * 
   * @param uiFactory is the UIFactorySwing instance.
   * @param parentObject is the parent of this object (may be <code>null</code>).
   */
  public AbstractUIPanel(UIFactorySwing uiFactory, UiNode parentObject) {

    super(uiFactory, parentObject);
  }

  /**
   * {@inheritDoc}
   */
  public AbstractUIComponent removeChild(int index) {

    AbstractUIComponent component = (AbstractUIComponent) doRemoveComponent(index);
    component.removeFromParent();
    return component;
  }

  /**
   * {@inheritDoc}
   */
  public boolean removeChild(UiElement component) {

    // return this.components.remove(component);
    int index = indexOfComponent(component);
    if (index >= 0) {
      removeChild(index);
      return true;
    } else {
      return false;
    }
  }

}
