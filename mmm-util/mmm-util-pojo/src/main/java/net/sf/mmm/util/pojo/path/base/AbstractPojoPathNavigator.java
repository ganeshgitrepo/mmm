/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.pojo.path.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.mmm.util.GenericBean;
import net.sf.mmm.util.HashKey;
import net.sf.mmm.util.collection.CollectionList;
import net.sf.mmm.util.component.AbstractLoggable;
import net.sf.mmm.util.nls.base.NlsNullPointerException;
import net.sf.mmm.util.pojo.path.api.PojoPathAccessException;
import net.sf.mmm.util.pojo.path.api.PojoPathContext;
import net.sf.mmm.util.pojo.path.api.PojoPathConversionException;
import net.sf.mmm.util.pojo.path.api.PojoPathCreationException;
import net.sf.mmm.util.pojo.path.api.PojoPathFunction;
import net.sf.mmm.util.pojo.path.api.PojoPathFunctionManager;
import net.sf.mmm.util.pojo.path.api.PojoPathFunctionUndefinedException;
import net.sf.mmm.util.pojo.path.api.PojoPathMode;
import net.sf.mmm.util.pojo.path.api.PojoPathNavigator;
import net.sf.mmm.util.pojo.path.api.PojoPathRecognizer;
import net.sf.mmm.util.pojo.path.api.PojoPathSegmentIsNullException;
import net.sf.mmm.util.pojo.path.api.PojoPathUnsafeException;
import net.sf.mmm.util.reflect.CollectionUtil;
import net.sf.mmm.util.reflect.ReflectionUtil;
import net.sf.mmm.util.value.api.ComposedValueConverter;
import net.sf.mmm.util.value.impl.DefaultComposedValueConverter;

/**
 * This is the abstract base implementation of the {@link PojoPathNavigator}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public abstract class AbstractPojoPathNavigator extends AbstractLoggable implements
    PojoPathNavigator {

  /**
   * The reserved {@link net.sf.mmm.util.pojo.path.api.PojoPath}-suffix used to
   * cache a {@link CollectionList}.
   */
  private static final String PATH_SUFFIX_COLLECTION_LIST = "._collection2list";

  /** @see #getReflectionUtil() */
  private ReflectionUtil reflectionUtil;

  /** @see #getCollectionUtil() */
  private CollectionUtil collectionUtil;

  /** @see #getFunctionManager() */
  private PojoPathFunctionManager functionManager;

  /** @see #getValueConverter() */
  private ComposedValueConverter valueConverter;

  /**
   * The constructor.
   */
  public AbstractPojoPathNavigator() {

    super();
  }

  /**
   * This method gets the optional {@link PojoPathFunctionManager} for
   * {@link PojoPathFunction}s that are global for this
   * {@link PojoPathNavigator} instance.<br>
   * <b>ATTENTION:</b><br>
   * {@link PojoPathFunction}s provided by this {@link PojoPathFunctionManager}
   * need to be stateless / thread-safe.
   * 
   * @return the {@link PojoPathFunctionManager} or <code>null</code> if NOT
   *         available.
   */
  protected PojoPathFunctionManager getFunctionManager() {

    return this.functionManager;
  }

  /**
   * This method sets the {@link #getFunctionManager() function-manager} used
   * for global {@link net.sf.mmm.util.pojo.path.api.PojoPathFunction}s.
   * 
   * @param functionManager is the {@link PojoPathFunctionManager}.
   */
  @Resource
  public void setFunctionManager(PojoPathFunctionManager functionManager) {

    getInitializationState().requireNotInitilized();
    this.functionManager = functionManager;
  }

  /**
   * This method gets the {@link ComposedValueConverter} used by default to
   * convert values that are NOT compatible.
   * 
   * @see PojoPathContext#getAdditionalConverter()
   * 
   * @return the valueConverter
   */
  protected ComposedValueConverter getValueConverter() {

    return this.valueConverter;
  }

  /**
   * This method sets the {@link #getValueConverter() value-converter} used by
   * default.
   * 
   * @param valueConverter is the {@link ComposedValueConverter} to set.
   */
  @Resource
  public void setValueConverter(ComposedValueConverter valueConverter) {

    getInitializationState().requireNotInitilized();
    this.valueConverter = valueConverter;
  }

  /**
   * This method gets the {@link CollectionUtil} instance to use.
   * 
   * @return the {@link CollectionUtil} to use.
   */
  public CollectionUtil getCollectionUtil() {

    return this.collectionUtil;
  }

  /**
   * @param collectionUtil is the collectionUtil to set
   */
  @Resource
  public void setCollectionUtil(CollectionUtil collectionUtil) {

    getInitializationState().requireNotInitilized();
    this.collectionUtil = collectionUtil;
  }

  /**
   * This method gets the {@link ReflectionUtil} instance to use.
   * 
   * @return the {@link ReflectionUtil} to use.
   */
  public ReflectionUtil getReflectionUtil() {

    return this.reflectionUtil;
  }

  /**
   * @param reflectionUtil is the reflectionUtil to set
   */
  @Resource
  public void setReflectionUtil(ReflectionUtil reflectionUtil) {

    getInitializationState().requireNotInitilized();
    this.reflectionUtil = reflectionUtil;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doInitialize() {

    super.doInitialize();
    if (this.reflectionUtil == null) {
      this.reflectionUtil = ReflectionUtil.getInstance();
    }
    if (this.collectionUtil == null) {
      this.collectionUtil = CollectionUtil.getInstance();
    }
    if (this.valueConverter == null) {
      DefaultComposedValueConverter converter = new DefaultComposedValueConverter();
      converter.initialize();
      this.valueConverter = converter;
    }
  }

  /**
   * This method gets the {@link PojoPathState} for the given
   * <code>context</code>.
   * 
   * @param initialPojo is the initial {@link net.sf.mmm.util.pojo.api.Pojo}
   *        this {@link PojoPathNavigator} was invoked with.
   * @param pojoPath is the {@link net.sf.mmm.util.pojo.path.api.PojoPath} to
   *        navigate.
   * @param mode is the {@link PojoPathMode mode} that determines how to deal
   *        <code>null</code> values.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @return the {@link PojoPathState} or <code>null</code> if caching is
   *         disabled.
   */
  @SuppressWarnings("unchecked")
  protected PojoPathState createState(Object initialPojo, String pojoPath, PojoPathMode mode,
      PojoPathContext context) {

    if (mode == null) {
      throw new NlsNullPointerException("mode");
    }
    Map<Object, Object> rawCache = context.getCache();
    if (rawCache == null) {
      CachingPojoPath rootPath = new CachingPojoPath(initialPojo, initialPojo.getClass());
      return new PojoPathState(rootPath, mode, pojoPath);
    }
    HashKey<Object> hashKey = new HashKey<Object>(initialPojo);
    PojoPathCache masterCache = (PojoPathCache) rawCache.get(hashKey);
    if (masterCache == null) {
      masterCache = new PojoPathCache(initialPojo);
      rawCache.put(hashKey, masterCache);
    }
    return masterCache.createState(mode, pojoPath);
  }

  /**
   * This method gets the {@link PojoPathState} for the given
   * <code>context</code>.
   * 
   * @param initialPojoType is the initial pojo-type this
   *        {@link PojoPathNavigator} was invoked with.
   * @param pojoPath is the {@link net.sf.mmm.util.pojo.path.api.PojoPath} to
   *        navigate.
   * @param mode is the {@link PojoPathMode mode} that determines how to deal
   *        with <em>unsafe</em>
   *        {@link net.sf.mmm.util.pojo.path.api.PojoPath}s.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @return the {@link PojoPathState} or <code>null</code> if caching is
   *         disabled.
   */
  @SuppressWarnings("unchecked")
  protected PojoPathState createStateByType(Type initialPojoType, String pojoPath,
      PojoPathMode mode, PojoPathContext context) {

    Map<Object, Object> rawCache = context.getCache();
    if (rawCache == null) {
      Class<?> initialPojoClass = getReflectionUtil().getClass(initialPojoType, false);
      CachingPojoPath rootPath = new CachingPojoPath(null, initialPojoClass, initialPojoType);
      return new PojoPathState(rootPath, mode, pojoPath);
    }
    PojoPathCache masterCache = (PojoPathCache) rawCache.get(initialPojoType);
    if (masterCache == null) {
      Class<?> initialPojoClass = getReflectionUtil().getClass(initialPojoType, false);
      masterCache = new PojoPathCache(initialPojoClass, initialPojoType);
      rawCache.put(initialPojoType, masterCache);
    }
    return masterCache.createState(mode, pojoPath);
  }

  /**
   * {@inheritDoc}
   */
  public Object get(Object pojo, String pojoPath, PojoPathMode mode, PojoPathContext context) {

    if (pojo == null) {
      if (mode == PojoPathMode.RETURN_IF_NULL) {
        return null;
      } else if (mode == PojoPathMode.RETURN_IF_NULL) {
        throw new PojoPathCreationException(null, pojoPath);
      } else {
        throw new PojoPathSegmentIsNullException(null, pojoPath);
      }
    }
    PojoPathState state = createState(pojo, pojoPath, mode, context);
    CachingPojoPath path = getRecursive(pojoPath, context, state);
    return path.pojo;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <TYPE> TYPE get(Object pojo, String pojoPath, PojoPathMode mode, PojoPathContext context,
      Class<TYPE> targetClass) {

    if (pojo == null) {
      if (mode == PojoPathMode.RETURN_IF_NULL) {
        return null;
      } else if (mode == PojoPathMode.RETURN_IF_NULL) {
        throw new PojoPathCreationException(null, pojoPath);
      } else {
        throw new PojoPathSegmentIsNullException(null, pojoPath);
      }
    }
    PojoPathState state = createState(pojo, pojoPath, mode, context);
    CachingPojoPath path = getRecursive(pojoPath, context, state);
    return (TYPE) convert(path, context, path.pojo, targetClass, targetClass);
  }

  /**
   * This method recursively navigates the given <code>pojoPath</code>.
   * 
   * @param pojoPath is the current
   *        {@link net.sf.mmm.util.pojo.path.api.PojoPath} to navigate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @return the result of the navigation of the given <code>pojoPath</code>
   *         starting at the given <code>pojo</code>. It may be
   *         <code>null</code> according to the given
   *         {@link PojoPathMode mode}.
   */
  @SuppressWarnings("unchecked")
  protected CachingPojoPath getRecursive(String pojoPath, PojoPathContext context,
      PojoPathState state) {

    // try to read from cache...
    CachingPojoPath currentPath = state.getCached(pojoPath);
    if (currentPath != null) {
      if (state.isGetType()) {
        // called from getType...
        if (currentPath.pojoType != null) {
          return currentPath;
        }
      } else {
        // called from get or set...
        if (currentPath.pojo != null) {
          return currentPath;
        }
      }
    } else {
      currentPath = new CachingPojoPath(pojoPath);
    }

    String parentPathString = currentPath.getParentPath();
    CachingPojoPath parentPath;
    if (parentPathString == null) {
      parentPath = state.rootPath;
    } else {
      // if our pojoPath is more than a segment, we do a recursive invocation
      parentPath = getRecursive(parentPathString, context, state);
    }
    // connect the path
    currentPath.parent = parentPath;

    // handle null value according to mode...
    if ((parentPath.pojo == null) && (!state.isGetType())) {
      switch (state.mode) {
        case FAIL_IF_NULL:
          throw new PojoPathSegmentIsNullException(state.rootPath.pojo, pojoPath);
        case RETURN_IF_NULL:
          return currentPath;
        default :
          // this is actually an internal error
          throw new PojoPathSegmentIsNullException(state.rootPath.pojo, pojoPath);
      }
    }

    // now we start our actual evaluation...
    if ((parentPath.pojoType == null) && (state.isGetType())) {
      // deal with "unsafe" path...
      if (state.mode == PojoPathMode.FAIL_IF_NULL) {
        throw new PojoPathSegmentIsNullException(state.rootPath.pojo, pojoPath);
      }
    } else {
      Object result = get(currentPath, context, state);
      currentPath.pojo = result;
      state.setCached(pojoPath, currentPath);
      if (result == null) {
        // creation has already taken place...
        if (state.mode != PojoPathMode.RETURN_IF_NULL) {
          if (state.isGetType()) {
            if ((currentPath.pojoType == null)) {
              throw new PojoPathUnsafeException(state.rootPath.pojoType, pojoPath);
            }
          } else {
            if (pojoPath != state.pojoPath) {
              throw new PojoPathSegmentIsNullException(state.rootPath.pojo, pojoPath);
            }
          }
        }
      } else {
        PojoPathRecognizer recognizer = context.getRecognizer();
        if (recognizer != null) {
          recognizer.recognize(result, currentPath);
        }
      }
    }
    return currentPath;
  }

  /**
   * This method gets the value for the single
   * {@link CachingPojoPath#getSegment() segment} of the given
   * <code>currentPath</code> from the {@link CachingPojoPath#getPojo() pojo}
   * of its {@link CachingPojoPath#getParent() parent}.<br>
   * If the <code>state</code> {@link PojoPathState#isGetType() indicates} an
   * invocation from
   * {@link #getType(Type, String, boolean, PojoPathContext) getType}, only the
   * {@link CachingPojoPath#setPojoType(Type) pojo-type} should be determined.
   * Otherwise if the result is <code>null</code> and
   * <code>{@link PojoPathState#getMode() mode}</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it creates and attaches (sets) the
   * missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) cache}
   *        to use or <code>null</code> to disable caching.
   * @return the result of the evaluation of the given <code>pojoPath</code>
   *         starting at the given <code>pojo</code>. It may be
   *         <code>null</code> according to the given
   *         {@link PojoPathMode mode}.
   */
  @SuppressWarnings("unchecked")
  protected Object get(CachingPojoPath currentPath, PojoPathContext context, PojoPathState state) {

    Object result;
    String functionName = currentPath.getFunction();
    if (functionName != null) {
      // current segment is a function...
      PojoPathFunction function = getFunction(functionName, context);
      if (state.isGetType()) {
        result = null;
        currentPath.pojoType = function.getOutputClass();
      } else {
        result = getFromFunction(currentPath, context, state, function);
      }
    } else {
      Object parentPojo = currentPath.parent.pojo;
      // current segment is NOT a function
      if ((parentPojo instanceof Map)
          || (state.isGetType() && Map.class.isAssignableFrom(currentPath.parent.pojoClass))) {
        result = getFromMap(currentPath, context, state, (Map) parentPojo);
      } else {
        Integer index = currentPath.getIndex();
        if (index != null) {
          // handle indexed segment for list or array...
          result = getFromList(currentPath, context, state, index.intValue());
        } else {
          // in all other cases get via reflection from POJO...
          result = getFromPojo(currentPath, context, state);
        }
      }
    }
    if (result != null) {
      Class<?> resultType = result.getClass();
      if (currentPath.pojoClass != resultType) {
        if (currentPath.pojoType == currentPath.pojoClass) {
          currentPath.pojoType = resultType;
        }
        currentPath.pojoClass = resultType;
      }
    }
    return result;
  }

  /**
   * This method
   * {@link PojoPathFunction#get(Object, String, PojoPathContext) gets} the
   * single {@link CachingPojoPath#getSegment() segment} of the given
   * <code>currentPath</code> from the {@link net.sf.mmm.util.pojo.api.Pojo}
   * given by <code>parentPojo</code>. If the result is <code>null</code>
   * and <code>{@link PojoPathState#getMode() mode}</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it
   * {@link PojoPathFunction#create(Object, String, PojoPathContext) creates}
   * the missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @param function is the {@link PojoPathFunction} for evaluation.
   * @return the result of the evaluation. It might be <code>null</code>
   *         according to the {@link PojoPathState#getMode() mode}.
   */
  @SuppressWarnings("unchecked")
  protected Object getFromFunction(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, PojoPathFunction function) {

    Object parentPojo = currentPath.parent.pojo;
    // TODO: convert parentPojo from parentPojoType to function.getInputClass()
    // if necessary.
    Object result = function.get(parentPojo, currentPath.getFunction(), context);
    if ((result == null) && (state.mode == PojoPathMode.CREATE_IF_NULL)) {
      result = function.create(parentPojo, currentPath.getFunction(), context);
      if (result == null) {
        throw new PojoPathCreationException(state.rootPath.pojo, currentPath.getPojoPath());
      }
    }
    if (!function.isDeterministic()) {
      state.setCachingDisabled();
    }
    return result;
  }

  /**
   * This method {@link Map#get(Object) gets} the single
   * {@link CachingPojoPath#getSegment() segment} of the given
   * <code>currentPath</code> from the {@link Map} given by
   * <code>parentPojo</code>. If the result is <code>null</code> and
   * <code>{@link PojoPathState#getMode() mode}</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it creates and
   * {@link Map#put(Object, Object) attaches} the missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @param parentPojo is the parent object to work on.
   * @return the result of the evaluation. It might be <code>null</code>
   *         according to the {@link PojoPathState#getMode() mode}.
   */
  @SuppressWarnings("unchecked")
  protected Object getFromMap(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, Map parentPojo) {

    // determine pojo type
    Type pojoType = currentPath.parent.pojoType;
    if (pojoType instanceof ParameterizedType) {
      ParameterizedType type = (ParameterizedType) pojoType;
      Type[] genericArgs = type.getActualTypeArguments();
      if (genericArgs.length == 2) {
        currentPath.pojoType = genericArgs[1];
        currentPath.pojoClass = getReflectionUtil().getClass(currentPath.pojoType, false);
      }
    } else {
      currentPath.pojoClass = Object.class;
      currentPath.pojoType = Object.class;
    }
    Object result = null;
    if (!state.isGetType()) {
      result = parentPojo.get(currentPath.getSegment());
      if ((result == null) && (state.mode == PojoPathMode.CREATE_IF_NULL)) {
        result = create(currentPath, context, state, currentPath.pojoClass);
        parentPojo.put(currentPath.getSegment(), result);
      }
    }
    return result;
  }

  /**
   * This method {@link CollectionUtil#get(Object, int) gets} the single
   * {@link CachingPojoPath#getIndex() segment} of the given
   * <code>currentPath</code> from the array or {@link java.util.List} given
   * by <code>parentPojo</code>. If the result is <code>null</code> and
   * <code>{@link PojoPathState#getMode() mode}</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it creates and
   * {@link CollectionUtil#set(Object, int, Object) sets} the missing object.
   * For arrays a new array has to be created and set in the parent of the
   * <code>parentPojo</code>.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @param index is the {@link java.util.List}-{@link java.util.List#get(int) index}.
   * @return the result of the evaluation. It might be <code>null</code>
   *         according to the {@link PojoPathState#getMode() mode}.
   */
  @SuppressWarnings("unchecked")
  protected Object getFromList(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, int index) {

    // handle indexed segment for collection/list or array...
    currentPath.pojoType = getReflectionUtil().getComponentType(currentPath.parent.pojoType, true);
    currentPath.pojoClass = getReflectionUtil().getClass(currentPath.pojoType, false);
    Object result = null;
    if (!state.isGetType()) {
      Object parentPojo = currentPath.parent.pojo;
      Object arrayOrList = convertList(currentPath, context, state, parentPojo);
      boolean ignoreIndexOverflow = (state.mode != PojoPathMode.FAIL_IF_NULL);
      result = getCollectionUtil().get(arrayOrList, index, ignoreIndexOverflow);
      if ((result == null) && (state.mode == PojoPathMode.CREATE_IF_NULL)) {
        result = create(currentPath, context, state, currentPath.pojoClass);
        setInList(currentPath, context, state, parentPojo, result, index);
      }
    }
    return result;
  }

  /**
   * This method creates a {@link net.sf.mmm.util.pojo.api.Pojo} of the given
   * <code>pojoType</code>.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @param pojoClass is the {@link Class} reflecting the
   *        {@link net.sf.mmm.util.pojo.api.Pojo} to create.
   * @return the created {@link net.sf.mmm.util.pojo.api.Pojo}.
   * @throws PojoPathCreationException if the creation failed.
   */
  protected Object create(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, Class<?> pojoClass) throws PojoPathCreationException {

    if ((pojoClass == null) || Object.class.equals(pojoClass)) {
      throw new PojoPathCreationException(state.rootPath.pojo, currentPath.getPojoPath());
    }
    Object result;
    try {
      result = context.getPojoFactory().newInstance(pojoClass);
    } catch (RuntimeException e) {
      throw new PojoPathCreationException(e, state.rootPath.pojo, currentPath.getPojoPath());
    }
    if (result == null) {
      throw new PojoPathCreationException(state.rootPath.pojo, currentPath.getPojoPath());
    }
    return result;
  }

  /**
   * This method reflectively gets the single
   * {@link CachingPojoPath#getSegment() segment} of the given
   * <code>currentPath</code> from the {@link net.sf.mmm.util.pojo.api.Pojo}
   * given by <code>parentPojo</code>. If the result is <code>null</code>
   * and <code>mode</code> is {@link PojoPathMode#CREATE_IF_NULL} it creates
   * and attaches (sets) the missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to evaluate.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        of this operation.
   * @return the result of the evaluation. It might be <code>null</code>
   *         according to the {@link PojoPathState#getMode() mode}.
   */
  protected abstract Object getFromPojo(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state);

  /**
   * This method gets the {@link PojoPathFunction} for the given
   * <code>functionName</code>.
   * 
   * @param functionName is the
   *        {@link PojoPathFunctionManager#getFunction(String) name} of the
   *        requested {@link PojoPathFunction}.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @return the requested {@link PojoPathFunction}.
   * @throws PojoPathFunctionUndefinedException if no {@link PojoPathFunction}
   *         is defined for the given <code>functionName</code>.
   */
  @SuppressWarnings("unchecked")
  protected PojoPathFunction getFunction(String functionName, PojoPathContext context)
      throws PojoPathFunctionUndefinedException {

    PojoPathFunction function = null;
    // context overrides functions...
    PojoPathFunctionManager manager = context.getAdditionalFunctionManager();
    if (manager != null) {
      function = manager.getFunction(functionName);
    }
    if (function == null) {
      // global functions as fallback...
      manager = getFunctionManager();
      if (manager != null) {
        function = manager.getFunction(functionName);
      }
    }
    if (function == null) {
      throw new PojoPathFunctionUndefinedException(functionName);
    }
    return function;
  }

  /**
   * {@inheritDoc}
   */
  public Type getType(Type pojoType, String pojoPath, boolean failOnUnsafePath,
      PojoPathContext context) {

    if (pojoType == null) {
      throw new NullPointerException();
    }
    PojoPathMode mode;
    if (failOnUnsafePath) {
      mode = PojoPathMode.FAIL_IF_NULL;
    } else {
      mode = PojoPathMode.RETURN_IF_NULL;
    }
    PojoPathState state = createStateByType(pojoType, pojoPath, mode, context);
    CachingPojoPath path = getRecursive(pojoPath, context, state);
    return path.pojoType;
  }

  /**
   * {@inheritDoc}
   */
  public Object set(Object pojo, String pojoPath, PojoPathMode mode, PojoPathContext context,
      Object value) {

    if (pojo == null) {
      if (mode == PojoPathMode.RETURN_IF_NULL) {
        return null;
      } else if (mode == PojoPathMode.RETURN_IF_NULL) {
        throw new PojoPathCreationException(null, pojoPath);
      } else {
        throw new PojoPathSegmentIsNullException(null, pojoPath);
      }
    }
    Object current = pojo;
    boolean addToCache = false;
    PojoPathState state = createState(pojo, pojoPath, mode, context);
    CachingPojoPath currentPath = state.getCached(pojoPath);
    if (currentPath == null) {
      addToCache = true;
      currentPath = new CachingPojoPath(pojoPath);
      String parentPathString = currentPath.getParentPath();
      if (parentPathString != null) {
        CachingPojoPath parentPath = getRecursive(parentPathString, context, state);
        current = parentPath.pojo;
        currentPath.setParent(parentPath);
      }
    } else if (currentPath.parent != null) {
      current = currentPath.parent.pojo;
    }
    if (current == null) {
      if (mode == PojoPathMode.RETURN_IF_NULL) {
        return null;
      }
      // this applies for all other modes, because otherwise the segment would
      // have been created.
      throw new PojoPathSegmentIsNullException(pojo, pojoPath);
    }
    Object old = set(currentPath, context, state, current, value);
    // update cache...
    currentPath.pojo = value;
    if (addToCache) {
      state.setCached(pojoPath, currentPath);
    }
    return old;
  }

  /**
   * This method sets the single {@link CachingPojoPath#getSegment() segment} of
   * the given <code>currentPath</code> from the
   * {@link net.sf.mmm.util.pojo.api.Pojo} given by <code>parentPojo</code>.
   * If the result is <code>null</code> and <code>mode</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it creates and attaches (sets) the
   * missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to set.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) cache}
   *        to use or <code>null</code> to disable caching.
   * @param parentPojo is the parent {@link net.sf.mmm.util.pojo.api.Pojo} to
   *        work on.
   * @param value is the value to set in <code>parentPojo</code>.
   * @return the replaced value. It may be <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  protected Object set(CachingPojoPath currentPath, PojoPathContext context, PojoPathState state,
      Object parentPojo, Object value) {

    Object result;
    String functionName = currentPath.getFunction();
    if (functionName != null) {
      // current segment is a function...
      PojoPathFunction function = getFunction(functionName, context);
      result = function.set(parentPojo, functionName, value, context);
    } else {
      // current segment is NOT a function
      if (parentPojo instanceof Map) {
        Map map = (Map) parentPojo;
        result = map.put(currentPath.getSegment(), value);
      } else {
        Integer index = currentPath.getIndex();
        if (index != null) {
          // handle indexed segment for list or array...
          result = setInList(currentPath, context, state, parentPojo, value, index.intValue());
        } else {
          // in all other cases get via reflection from POJO...
          result = setInPojo(currentPath, context, state, parentPojo, value);
        }
      }
    }
    return result;
  }

  /**
   * This method sets the single {@link CachingPojoPath#getSegment() segment} of
   * the given <code>currentPath</code> from the
   * {@link net.sf.mmm.util.pojo.api.Pojo} given by <code>parentPojo</code>.
   * If the result is <code>null</code> and <code>mode</code> is
   * {@link PojoPathMode#CREATE_IF_NULL} it creates and attaches (sets) the
   * missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to set.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        to use.
   * @param parentPojo is the parent {@link net.sf.mmm.util.pojo.api.Pojo} to
   *        work on.
   * @param value is the value to set in <code>parentPojo</code>.
   * @return the replaced value. It may be <code>null</code>.
   */
  protected abstract Object setInPojo(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, Object parentPojo, Object value);

  /**
   * This method converts the given <code>pojo</code> to the given
   * <code>targetClass</code> (or even <code>targetType</code>) as
   * necessary.
   * 
   * @param currentPath is the current {@link CachingPojoPath} that lead to
   *        <code>pojo</code>.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param pojo is the {@link net.sf.mmm.util.pojo.api.Pojo} to convert as
   *        necessary.
   * @param targetClass is the expected {@link Class}.
   * @param targetType is the expected {@link Type}.
   * @return the <code>pojo</code> converted to the <code>targetType</code>
   *         as necessary.
   * @throws PojoPathConversionException if the given <code>pojo</code> is NOT
   *         compatible and could NOT be converted.
   */
  protected Object convert(CachingPojoPath currentPath, PojoPathContext context, Object pojo,
      Class<?> targetClass, Type targetType) throws PojoPathConversionException {

    Object result = pojo;
    // null does NOT need to be converted...
    if (pojo != null) {
      Class<?> pojoClass = pojo.getClass();
      // only convert if NOT compatible
      if (!targetClass.isAssignableFrom(pojoClass)) {
        // conversion required...
        ComposedValueConverter converter = context.getAdditionalConverter();
        result = null;
        try {
          if (converter != null) {
            result = converter.convert(pojo, currentPath, targetClass, currentPath.pojoType);
          }
          if (result == null) {
            result = this.valueConverter.convert(pojo, currentPath, targetClass,
                currentPath.pojoType);
          }
        } catch (RuntimeException e) {
          throw new PojoPathConversionException(currentPath.getPojoPath(), pojoClass,
              currentPath.pojoType);
        }
        if (result == null) {
          throw new PojoPathConversionException(currentPath.getPojoPath(), pojoClass,
              currentPath.pojoType);
        }
        if (!targetClass.isAssignableFrom(result.getClass())) {
          IllegalStateException illegalState = new IllegalStateException("Illegal conversion!");
          throw new PojoPathConversionException(illegalState, currentPath.getPojoPath(), pojoClass,
              targetType);
        }
      }
    }
    return result;
  }

  /**
   * This method converts the given <code>arrayOrCollection</code> to a
   * {@link List} as necessary.
   * 
   * @param currentPath is the current {@link CachingPojoPath} that lead to
   *        <code>arrayOrCollection</code>.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        to use.
   * @param arrayOrCollection is the object to be accessed at a given index.
   * @return a {@link List} that adapts <code>arrayOrCollection</code> if it
   *         is a {@link Collection} but NOT a {@link List}. Otherwise the
   *         given <code>arrayOrCollection</code> itself.
   */
  @SuppressWarnings("unchecked")
  protected Object convertList(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, Object arrayOrCollection) {

    Object arrayOrList = arrayOrCollection;
    if (arrayOrCollection instanceof Collection) {
      if (!(arrayOrCollection instanceof List)) {
        // non-list collection (e.g. Set) - create Proxy-List
        if (state.cachingDisabled) {
          PojoPathCachingDisabledException cachingException = new PojoPathCachingDisabledException(
              currentPath.getPojoPath());
          throw new PojoPathAccessException(cachingException, currentPath.getPojoPath(),
              arrayOrCollection.getClass());
        }
        String collection2ListPath = currentPath.getPojoPath() + PATH_SUFFIX_COLLECTION_LIST;
        CachingPojoPath listPath = state.getCached(collection2ListPath);
        if (listPath == null) {
          listPath = new CachingPojoPath(collection2ListPath);
          listPath.parent = currentPath;
          listPath.pojo = new CollectionList((Collection) arrayOrCollection);
          state.setCached(collection2ListPath, listPath);
        }
        arrayOrList = listPath.pojo;
      }
    }
    return arrayOrList;
  }

  /**
   * This method
   * {@link CollectionUtil#set(Object, int, Object, GenericBean) sets} the
   * single {@link CachingPojoPath#getSegment() segment} of the given
   * <code>currentPath</code> from the array or {@link java.util.List} given
   * by <code>parentPojo</code>. If the result is <code>null</code> and
   * <code>mode</code> is {@link PojoPathMode#CREATE_IF_NULL} it creates and
   * attaches (sets) the missing object.
   * 
   * @param currentPath is the current {@link CachingPojoPath} to set.
   * @param context is the {@link PojoPathContext context} for this operation.
   * @param state is the
   *        {@link #createState(Object, String, PojoPathMode, PojoPathContext) state}
   *        to use.
   * @param parentPojo is the parent {@link net.sf.mmm.util.pojo.api.Pojo} to
   *        work on.
   * @param value is the value to set in <code>parentPojo</code>.
   * @param index is the position of the <code>value</code> to set in the
   *        array or {@link java.util.List} given by <code>parentPojo</code>.
   * @return the replaced value. It may be <code>null</code>.
   */
  protected Object setInList(CachingPojoPath currentPath, PojoPathContext context,
      PojoPathState state, Object parentPojo, Object value, int index) {

    Object arrayOrList = convertList(currentPath, context, state, parentPojo);
    GenericBean<Object> arrayReceiver = new GenericBean<Object>();
    Object result = getCollectionUtil().set(arrayOrList, index, value, arrayReceiver);
    Object newArray = arrayReceiver.getValue();
    if (newArray != null) {
      if (currentPath.parent.parent == null) {
        throw new PojoPathCreationException(state.rootPath.pojo, currentPath.getPojoPath());
      }
      Object parentParentPojo;
      if (currentPath.parent.parent == null) {
        parentParentPojo = state.rootPath.pojo;
      } else {
        parentParentPojo = currentPath.parent.parent.pojo;
      }
      set(currentPath.parent, context, state, parentParentPojo, newArray);
    }
    return result;
  }

  /**
   * This inner class represents the cache for {@link CachingPojoPath}s based
   * on an initial {@link net.sf.mmm.util.pojo.api.Pojo}.
   */
  protected static class PojoPathCache {

    /**
     * The actual cache that maps a
     * {@link net.sf.mmm.util.pojo.path.api.PojoPath#getPojoPath() PojoPath} to
     * the resulting {@link CachingPojoPath}.
     */
    private final Map<String, CachingPojoPath> cache;

    /** The root path. */
    private CachingPojoPath rootPath;

    /**
     * The cached {@link Object#hashCode() hash-code} of the
     * {@link CachingPojoPath#pojo} from the {@link #rootPath}.
     */
    private int cachedHash;

    /**
     * The constructor.
     * 
     * @param initialPojo is the initial {@link net.sf.mmm.util.pojo.api.Pojo}
     *        for this cache.
     */
    public PojoPathCache(Object initialPojo) {

      super();
      this.rootPath = new CachingPojoPath(initialPojo, initialPojo.getClass());
      this.cachedHash = initialPojo.hashCode();
      this.cache = new HashMap<String, CachingPojoPath>();
    }

    /**
     * The constructor.
     * 
     * @param initialPojoClass is the initial
     *        {@link net.sf.mmm.util.pojo.api.Pojo}-class for this cache.
     * @param initialPojoType is the initial
     *        {@link net.sf.mmm.util.pojo.api.Pojo}-type for this cache.
     */
    public PojoPathCache(Class<?> initialPojoClass, Type initialPojoType) {

      super();
      this.rootPath = new CachingPojoPath(null, initialPojoClass, initialPojoType);
      this.cachedHash = 0;
      this.cache = new HashMap<String, CachingPojoPath>();
    }

    /**
     * This method creates a new {@link PojoPathState} instance based on this
     * {@link PojoPathCache}.
     * 
     * @param mode is the {@link PojoPathMode mode} that determines how to deal
     *        <code>null</code> values.
     * @param pojoPath is the initial pojo-path.
     * @return the new {@link PojoPathState} instance.
     */
    protected PojoPathState createState(PojoPathMode mode, String pojoPath) {

      if (this.rootPath.pojo != null) {
        int currentHash = this.rootPath.pojo.hashCode();
        if (currentHash != this.cachedHash) {
          // initial POJO has changed, lets nuke the cached POJOs...
          for (CachingPojoPath path : this.cache.values()) {
            path.pojo = null;
            path.pojoType = null;
          }
          this.cachedHash = currentHash;
        }
      }
      return new PojoPathState(this.rootPath, mode, pojoPath, this.cache);
    }
  }

  /**
   * This inner class represents the state for a
   * {@link net.sf.mmm.util.pojo.path.api.PojoPath} evaluation.
   */
  protected static class PojoPathState {

    /**
     * The actual cache that maps a
     * {@link net.sf.mmm.util.pojo.path.api.PojoPath#getPojoPath() PojoPath} to
     * the resulting {@link net.sf.mmm.util.pojo.api.Pojo}.
     */
    private final Map<String, CachingPojoPath> cache;

    /** The root path. */
    private CachingPojoPath rootPath;

    /** @see #getMode() */
    private final PojoPathMode mode;

    /** @see #getPojoPath() */
    private final String pojoPath;

    /** @see #setCachingDisabled() */
    private boolean cachingDisabled;

    /**
     * The constructor for no caching.
     * 
     * @param rootPath is the {@link net.sf.mmm.util.pojo.path.api.PojoPath}
     *        with the initial {@link net.sf.mmm.util.pojo.api.Pojo}.
     * @param mode is the {@link PojoPathMode mode} that determines how to deal
     *        <code>null</code> values.
     * @param pojoPath is the {@link #getPojoPath() pojo-path}.
     */
    @SuppressWarnings("unchecked")
    protected PojoPathState(CachingPojoPath rootPath, PojoPathMode mode, String pojoPath) {

      this(rootPath, mode, pojoPath, Collections.EMPTY_MAP);
      this.cachingDisabled = true;
    }

    /**
     * The constructor.
     * 
     * @param rootPath is the {@link net.sf.mmm.util.pojo.path.api.PojoPath}
     *        with the initial {@link net.sf.mmm.util.pojo.api.Pojo}.
     * @param mode is the {@link PojoPathMode mode} that determines how to deal
     *        <code>null</code> values.
     * @param pojoPath is the {@link #getPojoPath() pojo-path}.
     * @param cache is the underlying {@link Map} used for caching.
     */
    protected PojoPathState(CachingPojoPath rootPath, PojoPathMode mode, String pojoPath,
        Map<String, CachingPojoPath> cache) {

      super();
      this.rootPath = rootPath;
      this.mode = mode;
      this.pojoPath = pojoPath;
      this.cache = cache;
    }

    /**
     * This method gets the {@link CachingPojoPath} from the cache.
     * 
     * @param currentPojoPath is the
     *        {@link net.sf.mmm.util.pojo.path.api.PojoPath} to lookup.
     * @return the cached {@link CachingPojoPath} or <code>null</code> if NOT
     *         (yet) cached.
     */
    public CachingPojoPath getCached(String currentPojoPath) {

      return this.cache.get(currentPojoPath);
    }

    /**
     * This method stored a {@link CachingPojoPath} in the cache. This method
     * will do nothing if this state is {@link #isCachingDisabled() disabled}.
     * 
     * @param currentPojoPath is the
     *        {@link net.sf.mmm.util.pojo.path.api.PojoPath} leading to the
     *        given <code>pojo</code>.
     * @param evaluatedPojoPath is the {@link CachingPojoPath} that has been
     *        evaluated and should be cached.
     */
    public void setCached(String currentPojoPath, CachingPojoPath evaluatedPojoPath) {

      if (!this.cachingDisabled) {
        this.cache.put(currentPojoPath, evaluatedPojoPath);
      }
    }

    /**
     * @return the rootPath
     */
    public CachingPojoPath getRootPath() {

      return this.rootPath;
    }

    /**
     * This method gets the {@link PojoPathMode} that determines how to deal
     * with <code>null</code>-values.
     * 
     * @return the mode or <code>null</code> for
     *         {@link PojoPathNavigator#getType(Type, String, boolean, PojoPathContext) getType}.
     */
    public PojoPathMode getMode() {

      return this.mode;
    }

    /**
     * @return the initial pojoPath
     */
    public String getPojoPath() {

      return this.pojoPath;
    }

    /**
     * This method determines if we have been invoked from
     * {@link PojoPathNavigator#getType(Type, String, boolean, PojoPathContext) getType}.
     * 
     * @return <code>true</code> if invoked via getType, <code>false</code>
     *         if invoked from <code>get</code> or <code>set</code>.
     */
    public boolean isGetType() {

      if (this.rootPath.pojo == null) {
        return true;
      }
      return false;
    }

    /**
     * This method determines if this cache has been
     * {@link #setCachingDisabled() disabled}.
     * 
     * @return <code>true</code> if this cache is disabled, <code>false</code>
     *         otherwise.
     */
    public boolean isCachingDisabled() {

      return this.cachingDisabled;
    }

    /**
     * This method disables further
     * {@link #setCached(String, CachingPojoPath) caching}.
     */
    public void setCachingDisabled() {

      this.cachingDisabled = true;
    }

  }

  /**
   * This class represents a {@link net.sf.mmm.util.pojo.path.api.PojoPath}. It
   * contains the internal logic to validate and parse the
   * {@link net.sf.mmm.util.pojo.path.api.PojoPath}. Additional it can also
   * hold the {@link #getPojo() result} of the evaluation and the
   * {@link #getPojoType() generic type}.
   * 
   * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
   */
  protected static class CachingPojoPath extends BasicPojoPath {

    /** @see #getParent() */
    private CachingPojoPath parent;

    /** @see #getPojoType() */
    private Type pojoType;

    /** @see #getPojoClass() */
    private Class<?> pojoClass;

    /** @see #getPojo() */
    private Object pojo;

    /**
     * The constructor.
     * 
     * @param pojoPath is the {@link #getPojoPath() path} to represent.
     */
    public CachingPojoPath(String pojoPath) {

      super(pojoPath);
    }

    /**
     * The constructor for the root-path.
     * 
     * @param pojo is the initial {@link #getPojo() pojo}. It may be
     *        <code>null</code> if invoked via
     *        {@link PojoPathNavigator#getType(Type, String, boolean, PojoPathContext) getType}.
     * @param pojoClass is the initial {@link #getPojoClass() pojo-class}.
     */
    public CachingPojoPath(Object pojo, Class<?> pojoClass) {

      this(pojo, pojoClass, pojoClass);
    }

    /**
     * The constructor for the root-path.
     * 
     * @param pojo is the initial {@link #getPojo() pojo}. It may be
     *        <code>null</code> if invoked via
     *        {@link PojoPathNavigator#getType(Type, String, boolean, PojoPathContext) getType}.
     * @param pojoClass is the initial {@link #getPojoClass() pojo-class}.
     * @param pojoType is the initial {@link #getPojoType() pojo-type}.
     */
    public CachingPojoPath(Object pojo, Class<?> pojoClass, Type pojoType) {

      super("");
      this.pojo = pojo;
      this.pojoClass = pojoClass;
      this.pojoType = pojoType;
    }

    /**
     * @return the parent
     */
    public CachingPojoPath getParent() {

      return this.parent;
    }

    /**
     * @param parent is the parent to set
     */
    public void setParent(CachingPojoPath parent) {

      this.parent = parent;
    }

    /**
     * This method gets the {@link Type type} of the {@link #getPojo() Pojo}
     * this {@link net.sf.mmm.util.pojo.path.api.PojoPath} is leading to.
     * 
     * @return the pojo-type or <code>null</code> if NOT set.
     */
    public Type getPojoType() {

      return this.pojoType;
    }

    /**
     * This method sets the {@link #getPojoType() pojo-type}.
     * 
     * @param pojoType is the pojo-type to set.
     */
    public void setPojoType(Type pojoType) {

      this.pojoType = pojoType;
    }

    /**
     * This method get the {@link Class} of the {@link #getPojo() Pojo} this
     * {@link net.sf.mmm.util.pojo.path.api.PojoPath} is leading to.
     * 
     * @return the pojo-class or <code>null</code> if NOT set.
     */
    public Class<?> getPojoClass() {

      return this.pojoClass;
    }

    /**
     * This method sets the {@link #getPojoClass() pojo-class}.
     * 
     * @param pojoClass is the pojo-class to set.
     */
    public void setPojoClass(Class<?> pojoClass) {

      this.pojoClass = pojoClass;
    }

    /**
     * This method gets the {@link net.sf.mmm.util.pojo.api.Pojo} instance this
     * {@link net.sf.mmm.util.pojo.path.api.PojoPath} is leading to.
     * 
     * @return the {@link net.sf.mmm.util.pojo.api.Pojo} or <code>null</code>.
     */
    public Object getPojo() {

      return this.pojo;
    }

    /**
     * This method sets the {@link #getPojo() pojo-instance}.
     * 
     * @param pojo is the {@link #getPojo() pojo-instance}.
     */
    public void setPojo(Object pojo) {

      this.pojo = pojo;
    }

  }

}