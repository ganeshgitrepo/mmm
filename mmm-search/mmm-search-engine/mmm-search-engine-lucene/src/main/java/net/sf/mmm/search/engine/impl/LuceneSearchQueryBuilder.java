/* $Id$ */
package net.sf.mmm.search.engine.impl;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import net.sf.mmm.search.api.SearchEntry;
import net.sf.mmm.search.engine.api.ComplexSearchQuery;
import net.sf.mmm.search.engine.api.SearchException;
import net.sf.mmm.search.engine.api.SearchQuery;
import net.sf.mmm.search.engine.api.SearchQueryBuilder;
import net.sf.mmm.search.engine.base.AbstractSearchQueryBuilder;

/**
 * This is the implementation of the {@link SearchQueryBuilder} interface using
 * lucene as underlying search-engine.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class LuceneSearchQueryBuilder extends AbstractSearchQueryBuilder {

  /** @see #createNullQuery() */
  public static final LuceneSearchQuery NULL_QUERY = new LuceneSearchQuery(new MatchAllDocsQuery());

  /** the analyzer to use */
  private Analyzer analyzer;

  /**
   * The constructor
   * 
   * @param luceneAnalyzer
   *        is the analyzer to use.
   */
  public LuceneSearchQueryBuilder(Analyzer luceneAnalyzer) {

    super();
    this.analyzer = luceneAnalyzer;
  }

  /**
   * @see net.sf.mmm.search.engine.base.AbstractSearchQueryBuilder#createNullQuery()
   */
  @Override
  protected SearchQuery createNullQuery() {

    return NULL_QUERY;
  }

  /**
   * @see net.sf.mmm.search.engine.api.SearchQueryBuilder#createComplexQuery()
   */
  public ComplexSearchQuery createComplexQuery() {
  
    return new LuceneComplexSearchQuery();
  }
  
  /**
   * @see net.sf.mmm.search.engine.api.SearchQueryBuilder#createPhraseQuery(java.lang.String,
   *      java.lang.String)
   */
  public SearchQuery createPhraseQuery(String property, String phrase) {

    TokenStream tokenStream = this.analyzer.tokenStream(property, new StringReader(phrase));
    PhraseQuery luceneQuery = new PhraseQuery();
    while (true) {
      Token token;
      try {
        token = tokenStream.next();
      } catch (IOException e) {
        token = null;
      }
      if (token == null) {
        break;
      }
      luceneQuery.add(new Term(property, token.termText()));
    }
    return new LuceneSearchQuery(luceneQuery);
  }

  /**
   * @see net.sf.mmm.search.engine.api.SearchQueryBuilder#createTermQuery(java.lang.String,
   *      java.lang.String)
   */
  public SearchQuery createTermQuery(String property, String term) {

    StringBuffer buffer = new StringBuffer(term.length());
    char[] chars = term.toCharArray();
    boolean hasPattern = false;
    boolean isPrefixQuery = true;
    int index = 0;
    while (index < chars.length) {
      char c = chars[index++];
      // skip duplicate '*'
      while ((c == '*') && (index < chars.length) && (chars[index] == '*')) {
        index++;
      }
      if ((c == '*') || (c == '?')) {
        // ignore trailing wildcards
        if (buffer.length() > 0) {
          hasPattern = true;
          if (index < chars.length) {
            isPrefixQuery = false;
            buffer.append(c);
          } else if (!isPrefixQuery) {
            buffer.append(c);
          }
        }
      } else {
        buffer.append(Character.toLowerCase(c));
      }
    }
    String queryString = buffer.toString();
    Query luceneQuery;
    if (!hasPattern || isPrefixQuery) {
      TokenStream tokenStream = this.analyzer.tokenStream(property, new StringReader(term));
      Token token;
      try {
        token = tokenStream.next();
      } catch (IOException e) {
        token = null;
      }
      if (token == null) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        if (hasPattern) {
          luceneQuery = new PrefixQuery(new Term(property, token.termText()));
        } else {
          luceneQuery = new TermQuery(new Term(property, token.termText()));
        }
      }
    } else {
      luceneQuery = new WildcardQuery(new Term(property, queryString));
    }
    return new LuceneSearchQuery(luceneQuery);
  }

  /**
   * @see net.sf.mmm.search.engine.api.SearchQueryBuilder#parseNativeQuery(java.lang.String)
   */
  public SearchQuery parseNativeQuery(String query) throws SearchException {

    try {
      // according to javadoc the parser is NOT thread safe so we create an
      // instance per use...
      QueryParser parser = new QueryParser(SearchEntry.PROPERTY_TEXT, this.analyzer);
      Query luceneQuery = parser.parse(query);
      return new LuceneSearchQuery(luceneQuery);
    } catch (ParseException e) {
      throw new SearchException(e, "Failed to parse query \"{0}\"", query);
    }
  }

  public static void main(String[] args) throws Exception {

    Analyzer analyzer = new StandardAnalyzer();
    QueryParser qp = new QueryParser("text", analyzer);
    String query = "+(text:(Hallo Welt) title:Foo)";
    SearchQuery q = new LuceneSearchQueryBuilder(analyzer).parseStandardQuery(query, false);
    System.out.println(q);
    System.out.println(qp.parse(query));
  }

}