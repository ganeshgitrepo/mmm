/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.data.resource.api;


/**
 * This is the interface for the regular
 * {@link net.sf.mmm.data.api.DataObject#isFolder() folder}-
 * {@link ContentResource resource}. Like a folder in the local filesystem, this
 * is a folder in the repository.<br>
 * <b>ATTENTION:</b><br>
 * Please note that any other {@link net.sf.mmm.data.api.DataObject
 * entity} may also be a {@link net.sf.mmm.data.api.DataObject#isFolder()
 * folder}. So you need to use {@link #isFolder()} when
 * {@link ContentResource#getChildren() browsing} rather than checking if a
 * child is a {@link ContentFolder}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface ContentFolder extends ContentResource {

  /**
   * The name of the {@link net.sf.mmm.data.api.reflection.DataClass}
   * reflecting {@link ContentFolder}.
   */
  String CLASS_NAME = "ContentFolder";

  /**
   * The ID of the {@link net.sf.mmm.data.api.reflection.DataClass}
   * reflecting this type.
   */
  short CLASS_ID = 21;

  /**
   * The {@link net.sf.mmm.data.api.DataObject#getTitle() name} of the
   * root-{@link ContentFolder folder}.
   */
  String NAME_ROOT = "";

  /** The path of the root-{@link ContentFolder folder} */
  String PATH_ROOT = PATH_SEPARATOR;

  /**
   * {@inheritDoc}
   */
  ContentFolder getParent();

}