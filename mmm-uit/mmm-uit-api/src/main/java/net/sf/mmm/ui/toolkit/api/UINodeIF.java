/* $Id: UINodeIF.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.ui.toolkit.api;

import net.sf.mmm.ui.toolkit.api.event.UIActionListenerIF;
import net.sf.mmm.ui.toolkit.api.window.UIFrameIF;
import net.sf.mmm.ui.toolkit.api.window.UIWindowIF;

/**
 * This is the interface for a node of the UI. A node is a UI object that has a
 * parent and or children (e.g. a window, widget, etc.).
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface UINodeIF extends UIObjectIF {

    /**
     * This method gets the parent component.
     * 
     * @return the parent component or <code>null</code> if this is a root
     *         frame.
     */
    UINodeIF getParent();

    /**
     * This method gets the parent frame of this component.
     * 
     * @return the parent frame or <code>null</code> if this is a root frame.
     */
    UIFrameIF getParentFrame();

    /**
     * This method gets the parent window of this component.
     * 
     * @return the parent window or <code>null</code> if this is a root frame.
     */
    UIWindowIF getParentWindow();

    /**
     * This method adds an action listener to this component. The listener will
     * be notified of changes of this component.
     * 
     * @param listener
     *        is the listener to add.
     */
    void addActionListener(UIActionListenerIF listener);

    /**
     * This method removes an action listener from this component.
     * 
     * @param listener
     *        is the listener to remove.
     */
    void removeActionListener(UIActionListenerIF listener);

    /**
     * This method refreshes this node. If not explicitly specified in the
     * according sub-interface, there is no need to call this method explicitly.
     */
    void refresh();

}
