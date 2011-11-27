/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.data.base.link;

import java.util.Collections;
import java.util.List;

import net.sf.mmm.data.api.entity.DataEntity;
import net.sf.mmm.data.api.link.Link;

/**
 * This is an implementation of {@link net.sf.mmm.data.api.link.LinkList} that is
 * immutable. This is useful to create an unmodifiable view on a
 * {@link net.sf.mmm.data.api.link.MutableLinkList}.
 * 
 * @param <TARGET> is the type of the linked {@link Link#getTarget() target
 *        entity}. See
 *        {@link net.sf.mmm.data.api.reflection.DataClass#getJavaClass()}.
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class ImmutableLinkList<TARGET extends DataEntity> extends AbstractLinkList<TARGET> {

  /** @see #getLinkList() */
  private final List<Link<TARGET>> linkList;

  /**
   * The constructor.
   * 
   * @param linkList is the {@link net.sf.mmm.data.api.link.LinkList} to adapt.
   */
  public ImmutableLinkList(AbstractLinkList<TARGET> linkList) {

    super();
    this.linkList = Collections.unmodifiableList(linkList.getLinkList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<Link<TARGET>> getLinkList() {

    return this.linkList;
  }

}
