/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.client.ui.base.widget.window.adapter;

import net.sf.mmm.client.ui.api.attribute.AttributeWriteClosable;
import net.sf.mmm.client.ui.api.attribute.AttributeWriteMaximizable;
import net.sf.mmm.client.ui.api.attribute.AttributeWriteMaximized;
import net.sf.mmm.client.ui.api.attribute.AttributeWriteMovable;
import net.sf.mmm.client.ui.api.attribute.AttributeWriteResizable;

/**
 * This is the interface for a {@link net.sf.mmm.client.ui.base.widget.adapter.UiWidgetAdapter} adapting
 * {@link net.sf.mmm.client.ui.api.widget.window.UiWidgetAbstractDialogWindow}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface UiWidgetAdapterAbstractDialogWindow extends UiWidgetAdapterAbstractWindow, AttributeWriteResizable,
    AttributeWriteMovable, AttributeWriteClosable, AttributeWriteMaximized, AttributeWriteMaximizable {

  // nothing to add

}
