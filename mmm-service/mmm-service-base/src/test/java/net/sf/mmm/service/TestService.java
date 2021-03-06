/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.service;

import net.sf.mmm.service.api.RemoteInvocationService;

/**
 * This is a {@link RemoteInvocationService} for JUnit testing.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface TestService extends RemoteInvocationService {

  /** @see #getMagicValue() */
  String MAGIC_VALUE = "twenty-four";

  /**
   * @return {@link #MAGIC_VALUE}
   */
  String getMagicValue();

}
