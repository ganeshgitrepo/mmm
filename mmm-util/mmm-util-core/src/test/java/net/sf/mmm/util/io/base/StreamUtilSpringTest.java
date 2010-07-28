/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.io.base;

import net.sf.mmm.framework.base.SpringContainerPool;
import net.sf.mmm.util.io.api.StreamUtil;

/**
 * This is the test-case for {@link StreamUtil} configured using spring.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class StreamUtilSpringTest extends StreamUtilTest {

  /**
   * {@inheritDoc}
   */
  @Override
  protected StreamUtil getStreamUtil() {

    return SpringContainerPool.getInstance().getComponent(StreamUtil.class);
  }

}
