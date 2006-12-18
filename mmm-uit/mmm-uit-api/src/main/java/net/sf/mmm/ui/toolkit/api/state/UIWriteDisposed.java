/* $Id$ */
package net.sf.mmm.ui.toolkit.api.state;

/**
 * This is the interface for an object of the UI framework that can be disposed.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface UIWriteDisposed {

  /**
   * This method disposes this object. The resources of the object are
   * deallocated and the object will be made undisplayable.
   */
  void dispose();

  /**
   * This method tests if this object has been disposed.
   * 
   * @return <code>true</code> if this object has been disposed.
   */
  boolean isDisposed();

}