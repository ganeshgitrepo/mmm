/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.client.ui.impl.test;

import net.sf.mmm.client.ui.api.UiContext;
import net.sf.mmm.client.ui.api.widget.field.UiWidgetField;
import net.sf.mmm.client.ui.base.widget.factory.AbstractUiSingleWidgetFactoryDatatype;
import net.sf.mmm.client.ui.base.widget.factory.UiWidgetFactoryDatatypeSimple;
import net.sf.mmm.client.ui.impl.test.widget.custom.UiWidgetCustomFieldPostalCode;
import net.sf.mmm.util.datatype.api.address.PostalCode;

/**
 * TODO: this class ...
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class UiWidgetFactoryDatatypeTest extends UiWidgetFactoryDatatypeSimple {

  /**
   * The constructor.
   */
  public UiWidgetFactoryDatatypeTest() {

    super();
    register(new UiSingleWidgetFactoryDatatypePostalCode());
  }

  /**
   * This inner class is the {@link AbstractUiSingleWidgetFactoryDatatype context} for the datatype
   * {@link PostalCode}.
   */
  public static class UiSingleWidgetFactoryDatatypePostalCode extends AbstractUiSingleWidgetFactoryDatatype<PostalCode> {

    /**
     * The constructor.
     */
    public UiSingleWidgetFactoryDatatypePostalCode() {

      super(PostalCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UiWidgetField<PostalCode> create(UiContext context) {

      return new UiWidgetCustomFieldPostalCode(context);
    }
  }

}
