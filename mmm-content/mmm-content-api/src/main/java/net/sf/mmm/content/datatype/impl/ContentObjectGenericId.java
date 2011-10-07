/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.content.datatype.impl;

/**
 * This is the implementation of the
 * {@link net.sf.mmm.content.datatype.api.ContentId} interface for a generic
 * resource including identification of {@link #getRevision() revision} and
 * {@link #getStoreId() store}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class ContentObjectGenericId extends ContentObjectId {

  /** UID for serialization. */
  private static final long serialVersionUID = 1553359087706129686L;

  /** @see #getRevision() */
  private final int revision;

  /** @see #getStoreId() */
  private final int storeId;

  /**
   * The constructor.
   * 
   * <b>ATTENTION:</b><br>
   * Please use {@link net.sf.mmm.content.api.ContentIdManager} to create
   * instances.
   * 
   * @param objectId is the {@link #getObjectId() object-ID}.
   * @param classId is the {@link #getClassId() class-ID}.
   * @param revision is the is the {@link #getRevision() revision}.
   */
  public ContentObjectGenericId(long objectId, int classId, int revision) {

    this(objectId, classId, revision, 0);
  }

  /**
   * The constructor.
   * 
   * <b>ATTENTION:</b><br>
   * Please use {@link net.sf.mmm.content.api.ContentIdManager} to create
   * instances.
   * 
   * @param objectId is the {@link #getObjectId() object-ID}.
   * @param classId is the {@link #getClassId() class-ID}.
   * @param revision is the is the {@link #getRevision() revision}.
   * @param storeId is the {@link #getStoreId() store-ID}.
   */
  public ContentObjectGenericId(long objectId, int classId, int revision, int storeId) {

    super(objectId, classId);
    this.revision = revision;
    this.storeId = storeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getRevision() {

    return this.revision;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getStoreId() {

    return this.storeId;
  }

}
