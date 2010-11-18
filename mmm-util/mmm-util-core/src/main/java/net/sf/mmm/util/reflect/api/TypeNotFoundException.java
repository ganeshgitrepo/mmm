/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.reflect.api;

import net.sf.mmm.util.NlsBundleUtilCore;

/**
 * A {@link TypeNotFoundException} is thrown if a {@link java.lang.reflect.Type}
 * was requested (e.g. via {@link Class#forName(String)}) but could NOT be
 * found. Unlike {@link ClassNotFoundException} this is a
 * {@link RuntimeException} and has {@link net.sf.mmm.util.nls.api.NlsThrowable
 * native-language-support}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 2.0.0
 */
public class TypeNotFoundException extends ReflectionException {

  /** UID for serialization. */
  private static final long serialVersionUID = 8148127703407920465L;

  /**
   * The constructor.
   * 
   * @param type is the representation of the {@link java.lang.reflect.Type}
   *        that could NOT be found.
   */
  public TypeNotFoundException(String type) {

    super(NlsBundleUtilCore.ERR_CLASS_NOT_FOUND, toMap(KEY_TYPE, type));
  }

  /**
   * The constructor.
   * 
   * @param nested is the exception to adapt.
   * @param type is the representation of the {@link java.lang.reflect.Type}
   *        that could NOT be found.
   */
  public TypeNotFoundException(Throwable nested, String type) {

    super(nested, NlsBundleUtilCore.ERR_CLASS_NOT_FOUND, toMap(KEY_TYPE, type));
  }

}