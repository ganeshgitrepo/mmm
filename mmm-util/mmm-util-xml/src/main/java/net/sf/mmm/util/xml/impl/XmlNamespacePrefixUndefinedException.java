/* $Id$ */
package net.sf.mmm.util.xml.impl;

import net.sf.mmm.util.xml.NlsResourceBundle;
import net.sf.mmm.util.xml.XmlException;

/**
 * This is the exception thrown if a namespace prefix is used for XML that has
 * not been defined (mapped to an namespace URI).
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class XmlNamespacePrefixUndefinedException extends XmlException {

  /** uid for serialization */
  private static final long serialVersionUID = 99650559734074268L;

  /**
   * The constructor.
   * 
   * @param prefix
   *        is the namespace prefix that is undefined.
   */
  public XmlNamespacePrefixUndefinedException(String prefix) {

    super(NlsResourceBundle.ERR_NAMESPACE_NOT_DECLARED, prefix);
  }

  /**
   * This method gets the namespace prefix that was NOT defined.
   * 
   * @return the undefined namespace prefix.
   */
  public String getNamespacePrefix() {

    return (String) getNlsMessage().getArgument(0);
  }

}