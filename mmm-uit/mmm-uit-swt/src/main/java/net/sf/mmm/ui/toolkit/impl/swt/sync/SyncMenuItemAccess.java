/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.ui.toolkit.impl.swt.sync;

import org.eclipse.swt.widgets.MenuItem;

import net.sf.mmm.ui.toolkit.impl.swt.UIFactorySwt;

/**
 * This class is used for synchronous access on a SWT
 * {@link org.eclipse.swt.widgets.MenuItem}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class SyncMenuItemAccess extends AbstractSyncWidgetAccess {

  /**
   * operation to set the
   * {@link org.eclipse.swt.widgets.MenuItem#setText(String) text} of the
   * menu-item.
   */
  protected static final String OPERATION_SET_TEXT = "setText";

  /**
   * operation to set the
   * {@link org.eclipse.swt.widgets.MenuItem#setSelection(boolean) selection-status}
   * of the menu-item.
   */
  protected static final String OPERATION_SET_SELECTED = "setSelected";

  /**
   * operation to get the
   * {@link org.eclipse.swt.widgets.MenuItem#getSelection() selection-status} of
   * the menu-item.
   */
  protected static final String OPERATION_IS_SELECTED = "getSelected";

  /** the menu-item to access */
  private final MenuItem menuItem;

  /** the text of the menu-item */
  private String text;

  /** the selection-status of the menu-item */
  private boolean selected;

  /**
   * The constructor.
   * 
   * @param uiFactory
   *        is used to do the synchronization.
   * @param swtStyle
   *        is the {@link org.eclipse.swt.widgets.Widget#getStyle() style} of
   *        the menu.
   * @param swtMenuItem
   *        is the menu-item to access.
   * @param itemText
   *        is the text of the <code>swtMenuItem</code>.
   */
  public SyncMenuItemAccess(UIFactorySwt uiFactory, int swtStyle, MenuItem swtMenuItem,
      String itemText) {

    super(uiFactory, swtStyle);
    this.menuItem = swtMenuItem;
    this.selected = false;
    this.text = itemText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void performSynchron(String operation) {

    if (operation == OPERATION_SET_TEXT) {
      this.menuItem.setText(this.text);
    } else if (operation == OPERATION_SET_SELECTED) {
      this.menuItem.setSelection(this.selected);
    } else if (operation == OPERATION_IS_SELECTED) {
      this.selected = this.menuItem.getSelection();
    } else {
      super.performSynchron(operation);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MenuItem getSwtObject() {

    return this.menuItem;
  }

  /**
   * This method sets the {@link MenuItem#setText(String) text} of the
   * menu-item.
   * 
   * @param newText
   *        is the text to set.
   */
  public void setText(String newText) {

    assert (checkReady());
    this.text = newText;
    invoke(OPERATION_SET_TEXT);
  }

  /**
   * This method gets the {@link MenuItem#getText() text} of the menu-item.
   * 
   * @return the text.
   */
  public String getText() {

    return this.text;
  }

  /**
   * This method gets the
   * {@link org.eclipse.swt.widgets.MenuItem#getSelection() selection-status} of
   * the menu-item.
   * 
   * @return the selection-status.
   */
  public boolean isSelected() {

    assert (checkReady());
    invoke(OPERATION_IS_SELECTED);
    return this.selected;
  }

  /**
   * This method sets the
   * {@link org.eclipse.swt.widgets.MenuItem#setSelection(boolean) selection-status}
   * of the menu-item.
   * 
   * @param selection
   *        is the selection-status to set.
   */
  public void setSelected(boolean selection) {

    assert (checkReady());
    this.selected = selection;
    invoke(OPERATION_SET_SELECTED);
  }

}
