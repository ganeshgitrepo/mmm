/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.text.base;

import net.sf.mmm.framework.base.SpringContainerPool;
import net.sf.mmm.util.text.api.Singularizer;

/**
 * This is the test-case for {@link EnglishSingularizer} configured using
 * spring.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("all")
public class EnglishSingularizerSpringTest extends EnglishSingularizerTest {

  /**
   * {@inheritDoc}
   */
  @Override
  public Singularizer getEnglishSingularizer() {

    return SpringContainerPool.getContainer("net/sf/mmm/util/text/beans-util-text.xml")
        .getComponent(Singularizer.class);
  }

}