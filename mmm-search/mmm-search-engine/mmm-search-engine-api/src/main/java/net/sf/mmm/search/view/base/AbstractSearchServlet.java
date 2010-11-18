/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.search.view.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.mmm.search.api.SearchEntry;
import net.sf.mmm.search.api.config.SearchConfiguration;
import net.sf.mmm.search.engine.api.ComplexSearchQuery;
import net.sf.mmm.search.engine.api.ManagedSearchEngine;
import net.sf.mmm.search.engine.api.SearchEngineBuilder;
import net.sf.mmm.search.engine.api.SearchQuery;
import net.sf.mmm.search.engine.api.SearchQueryBuilder;
import net.sf.mmm.search.engine.api.SearchResultPage;
import net.sf.mmm.search.engine.api.config.SearchEngineConfiguration;
import net.sf.mmm.search.engine.api.config.SearchEngineConfigurationHolder;
import net.sf.mmm.search.engine.api.config.SearchEngineConfigurationLoader;
import net.sf.mmm.search.engine.api.config.SearchEntryType;
import net.sf.mmm.search.engine.api.config.SearchEntryTypeContainer;
import net.sf.mmm.search.engine.base.config.SearchEntryTypeDefaults;
import net.sf.mmm.search.view.api.SearchEntryTypeView;
import net.sf.mmm.search.view.api.SearchViewConfiguration;
import net.sf.mmm.search.view.api.SearchViewLogic;
import net.sf.mmm.search.view.api.SearchViewRequestParameters;
import net.sf.mmm.util.component.api.IocContainer;
import net.sf.mmm.util.xml.api.XmlUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the abstract base implementation of the controller
 * {@link javax.servlet.Servlet servlet} of the search.<br/>
 * <b>ATTENTION:</b><br>
 * Please set <code>URIEncoding="UTF-8"</code> or better
 * <code>useBodyEncodingForURI="true"</code> for the connector in the server.xml
 * of your tomcat.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public abstract class AbstractSearchServlet extends HttpServlet implements SearchViewLogic,
    SearchViewConfiguration {

  /** The {@link Logger}. */
  private final Logger logger;

  /** The UID for serialization. */
  private static final long serialVersionUID = -2197345445602977147L;

  /** The name of the view (URL segment) for the actual search. */
  private String searchPath;

  /** The name of the view (URL segment) for the details. */
  private String detailsPath;

  /**
   * The name of the view (JSP without ".jsp") for the error page or
   * <code>null</code> to dispatch to the current view even in case of an error.
   */
  private String errorPath;

  /** @see #refresh() */
  private SearchEngineConfigurationLoader configurationReader;

  /** @see #init(ServletConfig) */
  private String configurationUri;

  /** @see #getConfiguration() */
  private SearchEngineConfigurationHolder configurationHolder;

  /** @see #getSearchEngine() */
  private ManagedSearchEngine searchEngine;

  /** @see #getXmlUtil() */
  private XmlUtil xmlUtil;

  /** @see #getEntryTypeViews() */
  private List<? extends SearchEntryTypeView> entryTypeViews;

  /** @see #updateEntryTypeViews() */
  private Map<String, SearchEntryTypeViewBean> id2viewMap;

  /**
   * The constructor.
   */
  public AbstractSearchServlet() {

    super();
    this.logger = LoggerFactory.getLogger(getClass());
  }

  /**
   * This method gets the {@link Logger}.
   * 
   * @return the {@link Logger}.
   */
  protected Logger getLogger() {

    return this.logger;
  }

  /**
   * {@inheritDoc}
   */
  public ManagedSearchEngine getSearchEngine() {

    return this.searchEngine;
  }

  /**
   * {@inheritDoc}
   */
  public SearchEngineConfiguration getConfiguration() {

    return this.configurationHolder.getBean();
  }

  /**
   * {@inheritDoc}
   */
  public XmlUtil getXmlUtil() {

    return this.xmlUtil;
  }

  /**
   * {@inheritDoc}
   */
  public String getSearchPath() {

    return this.searchPath;
  }

  /**
   * {@inheritDoc}
   */
  public String getDetailsPath() {

    return this.detailsPath;
  }

  /**
   * {@inheritDoc}
   */
  public String getErrorPath() {

    return this.errorPath;
  }

  /**
   * {@inheritDoc}
   */
  public List<? extends SearchEntryTypeView> getEntryTypeViews() {

    return this.entryTypeViews;
  }

  /**
   * {@inheritDoc}
   */
  public SearchEntryType getEntryType(String id) {

    SearchEntryType result = this.id2viewMap.get(id);
    if ((result == null) || (result.getIcon() == null)) {
      result = getConfiguration().getEntryTypes().getEntryType(id);
      if ((result == null) || (result.getIcon() == null)) {
        result = getConfiguration().getEntryTypes().getEntryType(SearchEntryType.ID_ANY);
        if ((result == null) || (result.getIcon() == null)) {
          // something is very wrong here...
          getLogger().debug("EntryType configuration broken, requested type id was '" + id + "'.");
          result = SearchEntryTypeDefaults.getEntryTypeAny();
        }
      }
    }
    return result;
  }

  /**
   * This method gets the {@link IocContainer} used to manage components with
   * their implementation. The {@link IocContainer} will be created and
   * initialized on the first call of this method.
   * 
   * @return the {@link IocContainer}.
   */
  protected abstract IocContainer getIocContainer();

  /**
   * This method gets an {@link ServletConfig#getInitParameter(String)
   * init-parameter}.
   * 
   * @param config is the {@link ServletConfig}.
   * @param key is the name of the requested parameter.
   * @param defaultValue the default value returned if the actual parameter is
   *        NOT set (<code>null</code>). May also be <code>null</code>.
   * @return the requested parameter or <code>defaultValue</code> if NOT set.
   */
  private static String getParameter(ServletConfig config, String key, String defaultValue) {

    String value = config.getInitParameter(key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) throws ServletException {

    super.init(config);
    try {
      this.detailsPath = getParameter(config, PARAMETER_DETAILS_PATH, DEFAULT_DETAILS_PATH);
      this.searchPath = getParameter(config, PARAMETER_SEARCH_PATH, DEFAULT_SEARCH_PATH);
      this.errorPath = getParameter(config, PARAMETER_ERROR_PATH, null);
      this.configurationUri = getParameter(config, PARAMETER_CONFIGURATION_URL,
          SearchConfiguration.DEFAULT_CONFIGURATION_URL);
      IocContainer container = getIocContainer();
      this.xmlUtil = container.getComponent(XmlUtil.class);
      this.configurationReader = container.getComponent(SearchEngineConfigurationLoader.class);
      this.configurationHolder = this.configurationReader.loadConfiguration(this.configurationUri);
      this.searchEngine = container.getComponent(SearchEngineBuilder.class).createSearchEngine(
          this.configurationHolder);
      updateEntryTypeViews();
    } catch (Exception e) {
      throw new ServletException("Initialization failed!", e);
    }
  }

  /**
   * This method extends the given <code>complexQuery</code> with a query for
   * the given <code>property</code>.
   * 
   * @param complexQuery is the existing query to extend.
   * @param field is the name of the field to add to the query.
   * @param value is the value to search in the given <code>property</code>.
   */
  private void appendFieldQuery(ComplexSearchQuery complexQuery, String field, String value) {

    if (value.length() > 0) {
      SearchQuery query = getSearchEngine().getQueryBuilder().createPhraseQuery(field, value);
      complexQuery.addRequiredQuery(query);
    }
  }

  /**
   * This method dispatches the request to the according view (by default
   * according JSP).
   * 
   * @param request is the {@link HttpServletRequest}.
   * @param response is the {@link HttpServletResponse}.
   * @param exception - <code>true</code> if an {@link Exception} occurred,
   *        <code>false</code> on success.
   * @throws ServletException if something goes wrong.
   * @throws IOException if something goes wrong.
   */
  protected void doDispatch(HttpServletRequest request, HttpServletResponse response,
      boolean exception) throws ServletException, IOException {

    String dispatchPath = request.getServletPath();
    if (dispatchPath.endsWith("/")) {
      dispatchPath = dispatchPath.substring(0, dispatchPath.length() - 1);
    }
    dispatchPath = dispatchPath + ".jsp";
    if (exception) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      if (this.errorPath != null) {
        dispatchPath = this.errorPath;
      }
    }
    request.getRequestDispatcher(dispatchPath).include(request, response);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    long time = System.currentTimeMillis();
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html");

    SearchViewContextBean searchContext = new SearchViewContextBean(request, this);
    SearchViewRequestParameters requestParameters = searchContext.getRequestParameters();

    String servletPath = request.getServletPath(); // .substring(1);
    boolean exception = false;
    try {
      if (servletPath.equals(this.detailsPath)) {
        String idString = requestParameters.getId();
        if (idString.length() > 0) {
          SearchEntry entry = getSearchEngine().getEntry(idString);
          searchContext.setEntry(entry);
        }
      } else if (servletPath.equals(this.searchPath)) {
        SearchQueryBuilder queryBuilder = getSearchEngine().getQueryBuilder();
        ComplexSearchQuery mainQuery = queryBuilder.createComplexQuery();
        SearchQuery query = null;
        String textQuery = requestParameters.getQuery();
        if (textQuery.length() > 0) {
          query = queryBuilder.parseStandardQuery(textQuery);
          mainQuery.addRequiredQuery(query);
        }
        appendFieldQuery(mainQuery, SearchEntry.FIELD_CREATOR, requestParameters.getCreator());
        appendFieldQuery(mainQuery, SearchEntry.FIELD_TITLE, requestParameters.getTitle());
        appendFieldQuery(mainQuery, SearchEntry.FIELD_SOURCE, requestParameters.getSource());
        String type = requestParameters.getType();
        if (!type.isEmpty()) {
          SearchEntryTypeView typeView = this.id2viewMap.get(type);
          if (typeView != null) {
            // refresh may happened during this request...
            Collection<String> combinedIds = typeView.getCombinedIds();
            if ((combinedIds != null) && (combinedIds.size() > 0)) {
              if (combinedIds.size() == 1) {
                appendFieldQuery(mainQuery, SearchEntry.FIELD_TYPE, combinedIds.iterator().next());
              } else {
                ComplexSearchQuery typesQuery = queryBuilder.createComplexQuery();
                for (String typeId : combinedIds) {
                  SearchQuery typeQuery = queryBuilder.createPhraseQuery(SearchEntry.FIELD_TYPE,
                      typeId);
                  typesQuery.addOptionalQuery(typeQuery);
                }
                mainQuery.addRequiredQuery(typesQuery);
              }
            }
          }
        }
        appendFieldQuery(mainQuery, SearchEntry.FIELD_SOURCE, type);
        int querySize = mainQuery.getSubQueryCount();
        if (querySize > 0) {
          if ((query == null) || (querySize > 1)) {
            query = mainQuery;
          }
          // perform search...
          SearchResultPage result;
          int pageNumber = requestParameters.getPageNumber();
          int hitsPerPage = requestParameters.getHitsPerPage();
          if (pageNumber == 0) {
            // initial search
            result = getSearchEngine().search(query, hitsPerPage);
          } else {
            // subsequent search
            int totalHitCount = requestParameters.getTotalHitCount();
            result = getSearchEngine().search(query, hitsPerPage, pageNumber, totalHitCount);
          }
          searchContext.setResultPage(result);
        }
      }
    } catch (Exception e) {
      searchContext.setException(e);
      exception = true;
    }
    doDispatch(request, response, exception);
    if (this.logger.isDebugEnabled()) {
      long delay = System.currentTimeMillis() - time;
      this.logger.debug("Request for '" + servletPath + "' took " + delay + " ms.");
    }
  }

  /**
   * This method updates the {@link #getEntryTypeViews() entry type views}.<br/>
   * The {@link SearchEngineConfiguration} defines the {@link SearchEntryType}s.
   * For this view these {@link SearchEntryType}s are reduced to those types
   * that actually exist in the search-index. Then they are
   * {@link SearchEntryTypeViewBean#combine(SearchEntryType) combined} and
   * finally sorted according to the {@link SearchEntryType#getTitle() title}.<br/>
   * This way the view can present the proper titles of the types and the search
   * can filter even on combined types.
   */
  private void updateEntryTypeViews() {

    SearchEntryTypeContainer typeContainer = this.configurationHolder.getBean().getEntryTypes();
    Map<String, SearchEntryTypeViewBean> title2viewMap = new HashMap<String, SearchEntryTypeViewBean>();
    SearchEntryType anyType = null;
    for (SearchEntryType type : typeContainer) {
      // check if type exists in index...
      String id = type.getId();
      if (SearchEntryType.ID_ANY.equals(id)) {
        anyType = type;
      } else {
        int count = this.searchEngine.count(SearchEntry.FIELD_TYPE, id);
        if (count > 0) {
          String title = type.getTitle();
          SearchEntryTypeViewBean view = title2viewMap.get(title);
          if (view == null) {
            view = new SearchEntryTypeViewBean();
            title2viewMap.put(title, view);
          }
          view.combine(type);
        }
      }
    }
    List<String> titles = new ArrayList<String>(title2viewMap.keySet());
    // do we need locale and collator here?
    Collections.sort(titles);
    SearchEntryTypeViewBean anyView = new SearchEntryTypeViewBean();
    if (anyType == null) {
      anyType = SearchEntryTypeDefaults.getEntryTypeAny();
    }
    anyView.combine(anyType);
    List<SearchEntryTypeViewBean> viewList = new ArrayList<SearchEntryTypeViewBean>();
    viewList.add(anyView);
    for (String title : titles) {
      viewList.add(title2viewMap.get(title));
    }
    this.id2viewMap = new HashMap<String, SearchEntryTypeViewBean>(viewList.size());
    for (SearchEntryTypeViewBean view : viewList) {
      this.id2viewMap.put(view.getId(), view);
    }
    this.entryTypeViews = viewList;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void refresh() {

    this.searchEngine.refresh();
    updateEntryTypeViews();
  }

  /**
   * {@inheritDoc}
   */
  public String getDisplayTitle(SearchEntry searchEntry) {

    String uri = searchEntry.getUri();
    String filename;
    if (uri == null) {
      filename = "-";
    } else {
      int lastSlash = uri.lastIndexOf('/');
      filename = uri.substring(lastSlash + 1);
    }
    String title = searchEntry.getTitle();
    if (title == null) {
      return filename;
    } else {
      StringBuffer buffer = new StringBuffer(filename);
      buffer.append(" (");
      buffer.append(title);
      buffer.append(')');
      return buffer.toString();
    }
  }

}
