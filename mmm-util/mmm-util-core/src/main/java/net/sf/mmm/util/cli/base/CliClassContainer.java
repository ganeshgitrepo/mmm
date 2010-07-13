/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.cli.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.util.cli.api.CliClass;
import net.sf.mmm.util.cli.api.CliMode;
import net.sf.mmm.util.cli.api.CliModeCycleException;
import net.sf.mmm.util.cli.api.CliModeObject;
import net.sf.mmm.util.cli.api.CliModes;
import net.sf.mmm.util.cli.api.CliStyle;
import net.sf.mmm.util.cli.api.CliStyleHandling;
import net.sf.mmm.util.component.api.InitState;
import net.sf.mmm.util.nls.api.DuplicateObjectException;
import net.sf.mmm.util.nls.api.ObjectNotFoundException;

import org.slf4j.Logger;

/**
 * A {@link CliClassContainer} determines and holds the class-specific
 * CLI-informations of a {@link #getStateClass() state-class}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 2.0.0
 */
public class CliClassContainer {

  /** @see #getStateClass() */
  private final Class<?> stateClass;

  /** @see #getCliStyle() */
  private final CliStyle cliStyle;

  /** @see #getCliClass() */
  private final CliClass cliClass;

  /** @see #getMode(String) */
  private final Map<String, CliModeContainer> id2ModeMap;

  /** @see #getName() */
  private final String name;

  /** @see #getLogger() */
  private final Logger logger;

  /**
   * The constructor.
   * 
   * @param stateClass is the {@link #getStateClass() state-class}.
   * @param logger is the {@link Logger} to use (e.g. for
   *        {@link CliStyleHandling}).
   */
  public CliClassContainer(Class<?> stateClass, Logger logger) {

    super();
    this.stateClass = stateClass;
    this.logger = logger;
    this.id2ModeMap = new HashMap<String, CliModeContainer>();
    CliStyle cliStyleAnnotation = null;
    CliClass cliClassAnnotation = null;
    Class<?> currentClass = stateClass;
    while (currentClass != null) {
      if (cliStyleAnnotation == null) {
        cliStyleAnnotation = currentClass.getAnnotation(CliStyle.class);
      }
      if (cliClassAnnotation == null) {
        cliClassAnnotation = currentClass.getAnnotation(CliClass.class);
      }
      currentClass = currentClass.getSuperclass();
    }

    if (cliStyleAnnotation == null) {
      cliStyleAnnotation = CliDefaultAnnotations.CLI_STYLE;
    }
    this.cliStyle = cliStyleAnnotation;
    if (cliClassAnnotation == null) {
      cliClassAnnotation = CliDefaultAnnotations.CLI_CLASS;
    }
    this.cliClass = cliClassAnnotation;
    if (this.cliClass.name().length() == 0) {
      this.name = this.stateClass.getName();
    } else {
      this.name = this.cliClass.name();
    }

    currentClass = stateClass;
    while (currentClass != null) {
      CliMode cliMode = currentClass.getAnnotation(CliMode.class);
      if (cliMode != null) {
        addMode(cliMode, currentClass);
      }
      CliModes cliModes = currentClass.getAnnotation(CliModes.class);
      if (cliModes != null) {
        for (CliMode mode : cliModes.value()) {
          addMode(mode, currentClass);
        }
      }
      currentClass = currentClass.getSuperclass();
    }
    for (CliModeContainer modeContainer : this.id2ModeMap.values()) {
      initializeRecursive(modeContainer);
    }
  }

  /**
   * This method gets the {@link Logger}.
   * 
   * @return the {@link Logger}.
   */
  public Logger getLogger() {

    return this.logger;
  }

  /**
   * This method initializes the given {@link CliModeContainer}.
   * 
   * @param mode is the {@link CliModeContainer} to initialize.
   * @return a {@link CliModeCycle} if a cyclic dependency has been detected but
   *         is NOT yet complete or <code>null</code> if the initialization was
   *         successful.
   * @throws CliModeCycleException if a cyclic dependency was detected and
   *         completed.
   */
  private CliModeCycle initializeRecursive(CliModeContainer mode) throws CliModeCycleException {

    InitState initState = mode.getState();
    if (initState != InitState.INITIALIZED) {
      if (initState == InitState.INITIALIZING) {
        // cycle detected
        return new CliModeCycle(mode);
      } else {
        mode.setState(InitState.INITIALIZING);
      }
      for (String parentId : mode.getMode().parentIds()) {
        CliModeContainer parentContainer = this.id2ModeMap.get(parentId);
        if (parentContainer == null) {
          throw new ObjectNotFoundException(CliMode.class, parentId);
        }
        CliModeCycle cycle = initializeRecursive(parentContainer);
        if (cycle != null) {
          cycle.inverseCycle.add(mode);
          if (cycle.startNode == mode) {
            throw new CliModeCycleException(cycle);
          }
          return cycle;
        }
        mode.getExtendedModes().addAll(parentContainer.getExtendedModes());
      }
    }
    return null;
  }

  /**
   * This method adds the given {@link CliMode}.
   * 
   * @param mode is the {@link CliMode} to add.
   * @param annotatedClass is the Class where the {@link CliMode} was annotated.
   */
  private void addMode(CliMode mode, Class<?> annotatedClass) {

    CliModeContainer container = new CliModeContainer(mode, annotatedClass);
    addMode(container);
  }

  /**
   * This method adds the given {@link CliMode}.
   * 
   * @param mode is the {@link CliMode} to add.
   */
  protected void addMode(CliModeContainer mode) {

    CliModeObject old = this.id2ModeMap.put(mode.getMode().id(), mode);
    if (old != null) {
      CliStyleHandling handling = this.cliStyle.modeDuplicated();
      DuplicateObjectException exception = new DuplicateObjectException(mode, mode.getMode().id());
      if (handling != CliStyleHandling.OK) {
        handling.handle(this.logger, exception);
      }
    }
  }

  /**
   * This method gets the {@link Class} reflecting the
   * {@link AbstractCliParser#getState() state-object}.
   * 
   * @return the state-class.
   */
  public Class<?> getStateClass() {

    return this.stateClass;
  }

  /**
   * This method gets the {@link CliStyle style} configured for the
   * {@link #getStateClass() state-class}. If no such annotation is present, a
   * default instance is returned.
   * 
   * @return the {@link CliStyle}.
   */
  public CliStyle getCliStyle() {

    return this.cliStyle;
  }

  /**
   * This method gets the {@link CliClass} configured for the
   * {@link #getStateClass() state-class}. If no such annotation is present, a
   * default instance is returned.
   * 
   * @return the {@link CliClass}.
   */
  public CliClass getCliClass() {

    return this.cliClass;
  }

  /**
   * This method gets the {@link CliModeContainer mode} associated with the
   * given {@link CliMode#id() ID}.
   * 
   * @param id the {@link CliMode#id() ID} of the requested {@link CliMode}.
   * @return the requested {@link CliMode} or <code>null</code> if no such
   *         {@link CliMode} exists.
   */
  public CliModeObject getMode(String id) {

    return this.id2ModeMap.get(id);
  }

  /**
   * This method gets the {@link Collection} with the {@link CliMode#id() IDs}
   * of all {@link #getMode(String) registered} {@link CliMode}s.
   * 
   * @return the mode-IDs.
   */
  public Collection<String> getModeIds() {

    return this.id2ModeMap.keySet();
  }

  /**
   * This method gets the {@link CliClass#name() name} configured for the
   * {@link #getStateClass() state-class}.
   * 
   * @return the annotated {@link CliClass#name() name} or the
   *         {@link Class#getName() class-name} of the {@link #getStateClass()
   *         state-class} if NOT configured.
   */
  public String getName() {

    return this.name;
  }

  /**
   * This inner class is a dummy for getting default instances of CLI
   * annotations.
   */
  @CliClass
  @CliStyle
  private static final class CliDefaultAnnotations {

    /** The default instance of {@link CliClass}. */
    private static final CliClass CLI_CLASS = CliDefaultAnnotations.class
        .getAnnotation(CliClass.class);

    /** The default instance of {@link CliStyle}. */
    private static final CliStyle CLI_STYLE = CliDefaultAnnotations.class
        .getAnnotation(CliStyle.class);

    /**
     * The forbidden constructor.
     */
    private CliDefaultAnnotations() {

      throw new IllegalStateException();
    }
  }

  /**
   * This inner class is used to detect a cyclic {@link CliMode#parentIds()
   * dependency} of {@link CliMode}s.
   */
  protected static class CliModeCycle {

    /** @see #getInverseCycle() */
    private List<CliModeContainer> inverseCycle;

    /** The start node where the detected cycle begins. */
    private CliModeContainer startNode;

    /**
     * The constructor.
     * 
     * @param startNode is the {@link #startNode}.
     */
    public CliModeCycle(CliModeContainer startNode) {

      super();
      this.startNode = startNode;
      this.inverseCycle = new ArrayList<CliModeContainer>();
      this.inverseCycle.add(this.startNode);
    }

    /**
     * @return the {@link List} of {@link CliModeContainer modes} that build a
     *         cycle. It is stored in reverse order so the last node is the
     *         start of the cycle from top-level. The first node will be the
     *         same node as the last one.
     */
    public List<CliModeContainer> getInverseCycle() {

      return this.inverseCycle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

      StringBuilder sb = new StringBuilder();
      for (int i = this.inverseCycle.size() - 1; i >= 0; i--) {
        CliModeContainer container = this.inverseCycle.get(i);
        sb.append(container.getMode().id());
        if (i > 0) {
          sb.append("-->");
        }
      }
      return sb.toString();
    }
  }

}
