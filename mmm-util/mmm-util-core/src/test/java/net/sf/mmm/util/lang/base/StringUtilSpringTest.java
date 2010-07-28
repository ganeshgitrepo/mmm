/* $Id: $
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.lang.base;

import net.sf.mmm.framework.base.SpringContainerPool;
import net.sf.mmm.util.lang.api.StringUtil;

import org.junit.AfterClass;

/**
 * This is the test-case for {@link StringUtil} configured using spring.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class StringUtilSpringTest extends StringUtilTest {

  /**
   * {@inheritDoc}
   */
  @Override
  public StringUtil getStringUtil() {

    return SpringContainerPool.getInstance().getComponent(StringUtil.class);
  }

  /**
   * This method is invoked after all tests of this class have completed.
   */
  @AfterClass
  public static void tearDown() {

    SpringContainerPool.dispose();
  }

}
