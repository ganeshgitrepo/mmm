/* $Id$ */
package net.sf.mmm.search.base;

import net.sf.mmm.search.NlsSearchEngineApiBundle;
import net.sf.mmm.search.api.SearchException;

/**
 * This is the exception thrown from
 * {@link net.sf.mmm.search.indexer.api.SearchIndexer#update(net.sf.mmm.search.indexer.api.MutableSearchEntry)}
 * if the given entry has no ID set.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class SearchIoException extends SearchException {

  /** UID for serialization */
  private static final long serialVersionUID = -8896253123083824658L;

  /**
   * The constructor
   * 
   * @param ioException
   *        is the {@link java.io.IOException} that caused this error.
   */
  public SearchIoException(Throwable ioException) {

    super(ioException, NlsSearchEngineApiBundle.ERR_SEARCH_IO);
  }

}