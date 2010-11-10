/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.resource.api;

import net.sf.mmm.util.NlsBundleUtilCore;
import net.sf.mmm.util.nls.api.NlsRuntimeException;

/**
 * This exception is thrown if a resource should be written that is read-only or
 * NOT writable for any other reason.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.2
 */
public class ResourceNotWritableException extends NlsRuntimeException {

  /** UID for serialization. */
  private static final long serialVersionUID = -7527198711344080897L;

  /**
   * The constructor.
   * 
   * @param absolutePath is the absolute path of the resource that could NOT be
   *        found.
   */
  public ResourceNotWritableException(String absolutePath) {

    super(NlsBundleUtilCore.ERR_RESOURCE_NOT_WRITABLE, toMap(KEY_RESOURCE, absolutePath));
  }

  /**
   * The constructor.
   * 
   * @param nested is the {@link #getCause() cause} of this exception.
   * @param absolutePath is the absolute path of the resource that could NOT be
   *        found.
   */
  public ResourceNotWritableException(Throwable nested, String absolutePath) {

    super(nested, NlsBundleUtilCore.ERR_RESOURCE_NOT_WRITABLE, toMap(KEY_RESOURCE, absolutePath));
  }

}