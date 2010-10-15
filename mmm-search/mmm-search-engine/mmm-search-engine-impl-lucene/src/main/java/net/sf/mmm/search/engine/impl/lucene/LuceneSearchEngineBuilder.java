/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.search.engine.impl.lucene;

import javax.inject.Inject;

import net.sf.mmm.search.api.config.SearchIndexConfiguration;
import net.sf.mmm.search.engine.api.ManagedSearchEngine;
import net.sf.mmm.search.engine.base.AbstractSearchEngineBuilder;
import net.sf.mmm.search.impl.lucene.LuceneAnalyzer;
import net.sf.mmm.search.impl.lucene.LuceneAnalyzerImpl;
import net.sf.mmm.search.impl.lucene.LuceneDirectoryBuilder;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.store.Directory;

/**
 * This is the implementation of the
 * {@link net.sf.mmm.search.engine.api.SearchEngineBuilder} using apache lucene
 * as underlying search-engine.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class LuceneSearchEngineBuilder extends AbstractSearchEngineBuilder {

  /** @see #setAnalyzer(Analyzer) */
  private Analyzer analyzer;

  /** @see #setLuceneAnalyzer(LuceneAnalyzer) */
  private LuceneAnalyzer luceneAnalyzer;

  /** @see #setLuceneDirectoryBuilder(LuceneDirectoryBuilder) */
  private LuceneDirectoryBuilder luceneDirectoryBuilder;

  /** @see #setHighlightFormatter(Formatter) */
  private Formatter highlightFormatter;

  /**
   * The constructor.
   */
  public LuceneSearchEngineBuilder() {

    super();
  }

  /**
   * @param analyzer is the analyzer to set
   */
  public void setAnalyzer(Analyzer analyzer) {

    getInitializationState().requireNotInitilized();
    this.analyzer = analyzer;
  }

  /**
   * @return the analyzer
   */
  protected Analyzer getAnalyzer() {

    return this.analyzer;
  }

  /**
   * This method sets (injects) the {@link LuceneAnalyzer}. You may also use
   * {@link #setAnalyzer(Analyzer)} if you assemble this component manually.
   * 
   * @param luceneAnalyzer is the luceneAnalyzer to set
   */
  @Inject
  public void setLuceneAnalyzer(LuceneAnalyzer luceneAnalyzer) {

    getInitializationState().requireNotInitilized();
    this.luceneAnalyzer = luceneAnalyzer;
  }

  /**
   * @param luceneDirectoryBuilder is the luceneDirectoryBuilder to set
   */
  @Inject
  public void setLuceneDirectoryBuilder(LuceneDirectoryBuilder luceneDirectoryBuilder) {

    getInitializationState().requireNotInitilized();
    this.luceneDirectoryBuilder = luceneDirectoryBuilder;
  }

  /**
   * @return the luceneDirectoryBuilder
   */
  protected LuceneDirectoryBuilder getLuceneDirectoryBuilder() {

    return this.luceneDirectoryBuilder;
  }

  /**
   * @param highlightFormatter is the highlightFormatter to set
   */
  @Inject
  public void setHighlightFormatter(Formatter highlightFormatter) {

    getInitializationState().requireNotInitilized();
    this.highlightFormatter = highlightFormatter;
  }

  /**
   * @return the highlightFormatter
   */
  protected Formatter getHighlightFormatter() {

    return this.highlightFormatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doInitialize() {

    super.doInitialize();
    if (this.analyzer == null) {
      if (this.luceneAnalyzer == null) {
        LuceneAnalyzerImpl luceneAnalyzerImpl = new LuceneAnalyzerImpl();
        luceneAnalyzerImpl.initialize();
        this.luceneAnalyzer = luceneAnalyzerImpl;
      }
      this.analyzer = this.luceneAnalyzer.getAnalyzer();
    }
    if (getSearchQueryBuilder() == null) {
      LuceneSearchQueryBuilder qb = new LuceneSearchQueryBuilder();
      qb.setAnalyzer(this.analyzer);
      setSearchQueryBuilder(qb);
    }
    if (this.highlightFormatter == null) {
      this.highlightFormatter = new HighlightFormatter();
    }
  }

  /**
   * {@inheritDoc}
   */
  public ManagedSearchEngine createSearchEngine(SearchIndexConfiguration configuration) {

    Directory directory = this.luceneDirectoryBuilder.createDirectory(configuration);
    LuceneSearchEngine engine = new LuceneSearchEngine(getSearchEngineRefresher(), directory,
        this.analyzer, getSearchQueryBuilder(), this.highlightFormatter);
    getSearchEngineRefresher().startup();
    return engine;
  }

}