/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.client.ui.dialog.api;

/**
 * This is the interface for a dialog of the client application. It displays itself in the page of the main
 * application window. It is a singleton instance that is addressed via a {@link DialogPlace}. A
 * {@link Dialog} should NOT be mixed with a {@link PopupDialog}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface Dialog extends AbstractDialog {

  /**
   * The {@link net.sf.mmm.client.ui.dialog.api.Dialog#getId() dialog-id} of the {@link #TYPE_ROOT root}
   * dialog.
   */
  String DIALOG_ID_ROOT = "root";

  /**
   * The {@link #getType() type} of a <em>popup</em> dialog. Popup means that the {@link Dialog} is opened in
   * a new window that is typically modal and smaller than a main {@link Dialog}.
   */
  String TYPE_POPUP = "Popup";

  /**
   * The {@link #getType() type} of a <em>main</em> dialog. This is a regular dialog displayed in the main
   * content area of the client application window.
   */
  String TYPE_MAIN = "Main";

  /**
   * The {@link #getType() type} of a <em>header</em> dialog. This is a dialog displayed at the top of the
   * main content area of the client application window above a {@link #TYPE_MAIN main dialog}.
   */
  String TYPE_HEADER = "Header";

  /**
   * The {@link #getType() type} of a <em>footer</em> dialog. This is a dialog displayed at the bottom of the
   * main content area of the client application window below a {@link #TYPE_MAIN main dialog}.
   */
  String TYPE_FOOTER = "Footer";

  /**
   * The {@link #getType() type} of a <em>navigation</em> dialog. The navigation offers a structured way for
   * the user to navigate through the application. This can happen in form of a menu-bar that is typically
   * placed below the {@link #TYPE_HEADER header} and above the {@link #TYPE_MAIN main dialog}. However it is
   * often realized as a list or tree of links that is displayed beside the {@link #TYPE_MAIN main dialog}
   * (typically on the left).
   */
  String TYPE_NAVIGATION = "Navigation";

  /**
   * The {@link #getType() type} of a <em>side</em> dialog. This is a dialog typically displayed at on the
   * right of the main content area of the client application window beside a {@link #TYPE_MAIN main dialog}.
   * It typically shows additional information or a summary of the current state (e.g. shopping cart).
   * However, it may also be used to display advertisements.
   */
  String TYPE_SIDE = "Side";

  /**
   * The {@link #getType() type} of the <em>root</em> dialog. This is the top-level dialog of the application
   * and contains the other {@link Dialog}s.
   */
  String TYPE_ROOT = "Root";

  /**
   * This method determines the type of this {@link Dialog}. There are predefined TYPE_* constants for common
   * types. You can simply create additional types.
   * 
   * @see #TYPE_MAIN
   * @see #TYPE_POPUP
   * @see #TYPE_HEADER
   * @see #TYPE_FOOTER
   * @see #TYPE_NAVIGATION
   * @see #TYPE_SIDE
   * @see #TYPE_ROOT
   * 
   * @return the type of this {@link Dialog}.
   */
  String getType();

}