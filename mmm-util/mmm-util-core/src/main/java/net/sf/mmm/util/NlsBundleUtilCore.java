/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util;

import net.sf.mmm.util.nls.base.AbstractResourceBundle;
import net.sf.mmm.util.nls.base.DuplicateObjectException;
import net.sf.mmm.util.nls.base.NlsIllegalArgumentException;
import net.sf.mmm.util.nls.base.NlsNullPointerException;

/**
 * This class holds the internationalized messages for this module.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class NlsBundleUtilCore extends AbstractResourceBundle {

  /** @see net.sf.mmm.util.value.api.WrongValueTypeException */
  public static final String ERR_VALUE_WRONG_TYPE = "The value \"{0}\" with the "
      + "type \"{1}\" can NOT be converted to the requested type \"{2}\"!";

  /** @see net.sf.mmm.util.value.api.WrongValueTypeException */
  public static final String ERR_VALUE_WRONG_TYPE_SOURCE = "The value \"{0}\" "
      + "from \"{3}\" with the type \"{1}\" can NOT be converted to the requested type \"{2}\"!";

  /** @see net.sf.mmm.util.value.api.ValueNotSetException */
  public static final String ERR_VALUE_NOT_SET = "The value \"{0}\" is not set!";

  /** @see net.sf.mmm.util.value.api.ValueParseGenericException */
  public static final String ERR_PARSE = "Failed to parse value \"{0}\" as value"
      + " of the type \"{1}\"!";

  /** @see net.sf.mmm.util.value.api.ValueParseGenericException */
  public static final String ERR_PARSE_SOURCE = "Failed to parse value \"{0}\" from "
      + "\"{2}\" as value of the type \"{1}\"!";

  /** @see net.sf.mmm.util.value.api.ValueOutOfRangeException */
  public static final String ERR_VALUE_OUT_OF_RANGE = "The value \"{0}\" "
      + "is not in the expected range of \"[{1}-{2}]\"!";

  /** @see net.sf.mmm.util.value.api.ValueOutOfRangeException */
  public static final String ERR_VALUE_OUT_OF_RANGE_SOURCE = "The value \"{0}\" from \"{3}\" "
      + "is not in the expected range of \"[{1}-{2}]\"!";

  /** @see net.sf.mmm.util.component.api.ResourceMissingException */
  public static final String ERR_RESOURCE_MISSING = "The required resource \"{0}\" is missing!";

  /** @see net.sf.mmm.util.component.api.AlreadyInitializedException */
  public static final String ERR_ALREADY_INITIALIZED = "The object is already initialized!";

  /** @see net.sf.mmm.util.component.api.NotInitializedException */
  public static final String ERR_NOT_INITIALIZED = "The object is NOT initialized!";

  /** @see NlsIllegalArgumentException */
  public static final String ERR_ILLEGAL_ARGUMENT = "The given argument \"{0}\" is illegal!";

  /** @see net.sf.mmm.util.date.api.IllegalDateFormatException */
  public static final String ERR_ILLEGAL_DATA_FORMAT = "Illegal date-format \"{0}\"!";

  /** @see net.sf.mmm.util.math.api.NumberConversionException */
  public static final String ERR_NUMBER_CONVERSION = "Can not convert number \"{0}\" to \"{1}\"!";

  /** @see net.sf.mmm.util.io.api.RuntimeIoException */
  public static final String ERR_IO = "An unexpected input/output error has ocurred!";

  /** @see net.sf.mmm.util.nls.base.NlsNullPointerException */
  public static final String ERR_ARGUMENT_NULL = "The argument \"{0}\" is null!";

  /** @see net.sf.mmm.util.nls.base.DuplicateObjectException */
  public static final String ERR_DUPLICATE_OBJECT = "Duplicate object \"{0}\"!";

  /** @see net.sf.mmm.util.nls.base.DuplicateObjectException */
  public static final String ERR_DUPLICATE_OBJECT_WITH_KEY = "Duplicate object \"{0}\" for key \"{1}\"!";

  /** @see net.sf.mmm.util.nls.base.IllegalStateException */
  public static final String ERR_ILLEGAL_STATE = "Illegal state!";

}