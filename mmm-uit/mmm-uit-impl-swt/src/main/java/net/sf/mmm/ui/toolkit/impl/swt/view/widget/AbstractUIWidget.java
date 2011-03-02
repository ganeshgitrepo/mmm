/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.swt.view.widget;

import net.sf.mmm.ui.toolkit.api.view.UiElement;
import net.sf.mmm.ui.toolkit.api.view.widget.UiWidget;
import net.sf.mmm.ui.toolkit.impl.swt.AbstractUiElement;
import net.sf.mmm.ui.toolkit.impl.swt.UiFactorySwt;
import net.sf.mmm.ui.toolkit.impl.swt.view.UiSwtNode;
import net.sf.mmm.ui.toolkit.impl.swt.view.composite.AbstractUiComposite;

/**
 * This class is the implementation of the
 * {@link net.sf.mmm.ui.toolkit.api.view.widget.UiWidget} interface using SWT as
 * the UI toolkit.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public abstract class AbstractUIWidget extends AbstractUiElement implements UiWidget {

  /**
   * The constructor.
   * 
   * @param uiFactory is the UIFactorySwt instance.
   * @param parentObject is the parent of this object (may be <code>null</code>
   *        ).
   */
  public AbstractUIWidget(UiFactorySwt uiFactory, UiSwtNode parentObject) {

    super(uiFactory, parentObject);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public AbstractUiComposite<? extends UiElement> getParent() {

    return (AbstractUiComposite<? extends UiElement>) super.getParent();
  }

}
