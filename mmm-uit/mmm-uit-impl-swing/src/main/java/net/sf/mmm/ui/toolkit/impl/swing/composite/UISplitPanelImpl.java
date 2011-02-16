/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.swing.composite;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import net.sf.mmm.ui.toolkit.api.UiElement;
import net.sf.mmm.ui.toolkit.api.event.UIRefreshEvent;
import net.sf.mmm.ui.toolkit.api.view.composite.Orientation;
import net.sf.mmm.ui.toolkit.api.view.composite.UiSplitPanel;
import net.sf.mmm.ui.toolkit.impl.swing.AbstractUIComponent;
import net.sf.mmm.ui.toolkit.impl.swing.UIFactorySwing;

/**
 * This class is the implementation of the
 * {@link net.sf.mmm.ui.toolkit.api.view.composite.UiSplitPanel} interface using
 * Swing as the UI toolkit.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class UISplitPanelImpl extends AbstractUIComposite implements UiSplitPanel {

  /** the swing split pane */
  private JSplitPane splitPanel;

  /** the component top or left */
  private AbstractUIComponent componentTopOrLeft;

  /** the component bottom or right */
  private AbstractUIComponent componentBottomOrRight;

  /** @see #getOrientation() */
  private Orientation orientation;

  /** @see #setDividerPosition(double) */
  private double dividerLocation;

  /**
   * The constructor.
   * 
   * @param uiFactory is the UIFactorySwing instance.
   * @param parentObject is the parent of this object (may be <code>null</code>).
   * @param orientation is the orientation of the two child-components in this
   *        split-panel.
   */
  public UISplitPanelImpl(UIFactorySwing uiFactory, AbstractUIComponent parentObject,
      Orientation orientation) {

    super(uiFactory, parentObject);
    this.splitPanel = new JSplitPane();
    this.componentTopOrLeft = null;
    this.componentBottomOrRight = null;
    setOrientation(orientation);
    this.dividerLocation = 0.5;
    initialize();
  }

  /**
   * {@inheritDoc}
   */
  public JComponent getSwingComponent() {

    return this.splitPanel;
  }

  /**
   * {@inheritDoc}
   */
  public void setOrientation(Orientation orientation) {

    boolean horizontal = (orientation == Orientation.HORIZONTAL);
    if (getFactory().isFlipHorizontal()) {
      horizontal = !horizontal;
    }
    if (horizontal) {
      this.splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    } else {
      this.splitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
    }
    this.orientation = orientation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Orientation getOrientation() {

    return this.orientation;
  }

  /**
   * {@inheritDoc}
   */
  public void setTopOrLeftComponent(UiElement component) {

    AbstractUIComponent newComponent = (AbstractUIComponent) component;
    if (newComponent.getParent() != null) {
      newComponent.removeFromParent();
    }
    if (this.componentTopOrLeft != null) {
      setParent(this.componentTopOrLeft, null);
    }
    this.componentTopOrLeft = newComponent;
    JComponent jComponent = this.componentTopOrLeft.getSwingComponent();
    this.splitPanel.setTopComponent(jComponent);
    setParent(this.componentTopOrLeft, this);
  }

  /**
   * {@inheritDoc}
   */
  public void setBottomOrRightComponent(UiElement component) {

    AbstractUIComponent newComponent = (AbstractUIComponent) component;
    if (newComponent.getParent() != null) {
      newComponent.removeFromParent();
    }
    if (this.componentBottomOrRight != null) {
      setParent(this.componentBottomOrRight, null);
    }
    this.componentBottomOrRight = newComponent;
    JComponent jComponent = this.componentBottomOrRight.getSwingComponent();
    this.splitPanel.setBottomComponent(jComponent);
    setParent(this.componentBottomOrRight, this);
  }

  /**
   * {@inheritDoc}
   */
  public void setDividerPosition(double proportion) {

    this.dividerLocation = proportion;
    this.splitPanel.setDividerLocation(proportion);
  }

  /**
   * {@inheritDoc}
   */
  public String getType() {

    return TYPE;
  }

  /**
   * {@inheritDoc}
   */
  public UiElement getTopOrLeftComponent() {

    return this.componentTopOrLeft;
  }

  /**
   * {@inheritDoc}
   */
  public UiElement getBottomOrRightComponent() {

    return this.componentBottomOrRight;
  }

  /**
   * {@inheritDoc}
   */
  public UiElement getComponent(int index) {

    if (index == 0) {
      return getTopOrLeftComponent();
    } else if (index == 1) {
      return getBottomOrRightComponent();
    } else {
      throw new IndexOutOfBoundsException("Illegal index (" + index + ") must be 0 or 1!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public int getComponentCount() {

    return 2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh(UIRefreshEvent event) {

    super.refresh(event);
    if (event.isOrientationModified()) {
      setOrientation(this.orientation);
    }
  }

}
