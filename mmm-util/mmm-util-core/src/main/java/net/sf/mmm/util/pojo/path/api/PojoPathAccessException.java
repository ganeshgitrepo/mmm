/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.pojo.path.api;

import java.lang.reflect.Type;

import net.sf.mmm.util.pojo.NlsBundleUtilPojo;

/**
 * A {@link PojoPathAccessException} is thrown if a {@link PojoPath} could NOT
 * be evaluated because it lead to an invalid access on some object.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class PojoPathAccessException extends PojoPathException {

  /** UID for serialization. */
  private static final long serialVersionUID = -377916670743969317L;

  /**
   * The constructor.
   * 
   * @param pojoPath is the {@link PojoPath} that could NOT be accessed in the
   *        intended way.
   * @param currentPojoType is the type of the current
   *        {@link net.sf.mmm.util.pojo.api.Pojo} for which the
   *        {@link PojoPath#getSegment() segment} of the given
   *        <code>pojoPath</code> could NOT be accessed.
   */
  public PojoPathAccessException(String pojoPath, Type currentPojoType) {

    super(NlsBundleUtilPojo.ERR_PATH_ACCESS, toMap(KEY_PATH, pojoPath, KEY_TYPE, currentPojoType));
  }

  /**
   * The constructor.
   * 
   * @param nested is the {@link #getCause() cause} of this exception.
   * @param pojoPath is the {@link PojoPath} that could NOT be accessed in the
   *        intended way.
   * @param currentPojoType is the type of the current
   *        {@link net.sf.mmm.util.pojo.api.Pojo} for which the
   *        {@link PojoPath#getSegment() segment} of the given
   *        <code>pojoPath</code> could NOT be accessed.
   */
  public PojoPathAccessException(Throwable nested, String pojoPath, Type currentPojoType) {

    super(nested, NlsBundleUtilPojo.ERR_PATH_ACCESS, toMap(KEY_PATH, pojoPath, KEY_TYPE,
        currentPojoType));
  }

}