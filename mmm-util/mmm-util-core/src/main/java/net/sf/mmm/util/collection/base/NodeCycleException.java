/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.collection.base;

import net.sf.mmm.util.NlsBundleUtilCore;
import net.sf.mmm.util.nls.api.NlsRuntimeException;

/**
 * A {@link NodeCycleException} is thrown if a chain of nodes has a cyclic
 * dependency.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 2.0.0
 */
public class NodeCycleException extends NlsRuntimeException {

  /** UID for serialization. */
  private static final long serialVersionUID = 8888925901313417066L;

  /**
   * Key for the {@link net.sf.mmm.util.nls.api.NlsMessage#getArgument(String)
   * argument} {@value}.
   */
  public static final String KEY_CYCLE = "cycle";

  /**
   * The constructor.
   * 
   * @param cycle is the description of the cycle.
   */
  public NodeCycleException(NodeCycle<?> cycle) {

    this(cycle, cycle.getStartNode().getClass());
  }

  /**
   * The constructor.
   * 
   * @param cycle is the description of the cycle.
   * @param type describes the type of the nodes that form the cycle.
   */
  public NodeCycleException(NodeCycle<?> cycle, Object type) {

    super(NlsBundleUtilCore.ERR_NODE_CYCLE, toMap(KEY_CYCLE, cycle, KEY_TYPE, type));
  }

}