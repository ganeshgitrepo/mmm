/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.client.ui.api.handler.event;

import net.sf.mmm.client.ui.api.feature.UiFeatureCollapse;

/**
 * This is the {@link UiHandlerEvent} for the action
 * {@link #onCollapseOrExpand(UiFeatureCollapse, boolean, boolean)}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface UiHandlerEventCollapse extends UiHandlerEvent {

  /**
   * This method is invoked if an {@link UiFeatureCollapse collapsable object} is to be
   * {@link UiFeatureCollapse#setCollapsed(boolean) collapsed or expanded}.<br/>
   * <b>ATTENTION:</b><br/>
   * Implementations of this method should only
   * {@link net.sf.mmm.client.ui.api.attribute.AttributeWriteVisible#setVisible(boolean) change the
   * visibility} of objects. Do not misuse this feature for totally different things as this will cause
   * confusion and bad usability.
   * 
   * @param source is the object that triggered the collapse or expand.
   * @param collapse - <code>true</code> to collapse, <code>false</code> to expand.
   * @param programmatic - <code>true</code> if the
   *        {@link net.sf.mmm.client.ui.api.attribute.AttributeWriteCollapsed#setCollapsed(boolean) change was
   *        triggered by the program}, <code>false</code> if performed by the end-user.
   */
  void onCollapseOrExpand(UiFeatureCollapse source, boolean collapse, boolean programmatic);

}