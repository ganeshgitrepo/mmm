/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.collection.api;

import java.util.SortedSet;

import net.sf.mmm.util.collection.base.TreeSetFactory;

/**
 * This is the interface for a {@link CollectionFactory} that
 * {@link #create() creates} instances of {@link SortedSet}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("unchecked")
public interface SortedSetFactory extends CollectionFactory<SortedSet> {

  /**
   * {@inheritDoc}
   */
  <E> SortedSet<E> create();

  /**
   * {@inheritDoc}
   */
  <E> SortedSet<E> create(int capacity);

  /** The default instance creating a {@link java.util.TreeSet}. */
  SortedSetFactory INSTANCE_TREE_SET = new TreeSetFactory();

}
