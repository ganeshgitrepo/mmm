/* $Id: QName.java 205 2006-08-10 19:04:59Z hohwille $ */
package net.sf.mmm.configuration.base;

import net.sf.mmm.configuration.api.ConfigurationIF;

/**
 * This class represents a qualified name. It is a container for a local
 * {@link #name} and a {@link #namespaceUri}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public final class QName {

    /** @see net.sf.mmm.configuration.api.ConfigurationIF#getName() */
    public final String name;

    /**
     * @see net.sf.mmm.configuration.api.ConfigurationIF#getNamespaceUri() May
     *      be null to {@link #equals(Object) match} any namespace.
     */
    public final String namespaceUri;

    /**
     * The constructor.
     * 
     * @param localName
     *        is the local {@link #name}.
     * @param nsUri
     *        is the {@link #namespaceUri}.
     */
    public QName(String localName, String nsUri) {

        super();
        this.name = localName;
        this.namespaceUri = nsUri;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object) {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if ((obj == null) || (obj.getClass() != QName.class)) {
            return false;
        }
        QName other = (QName) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        if ((this.namespaceUri == null) || (other.namespaceUri == null)
                || (this.namespaceUri.equals(other.namespaceUri))) {
            return true;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode() {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return this.name.hashCode();
    }

    /**
     * @see java.lang.Object#toString() {@inheritDoc}
     */
    @Override
    public String toString() {

        if ((this.namespaceUri == null)
                || (ConfigurationIF.NAMESPACE_URI_NONE.equals(this.namespaceUri))) {
            return this.name;
        } else {
            int len = 1 + this.name.length() + this.namespaceUri.length();
            StringBuffer sb = new StringBuffer(len);
            sb.append(this.namespaceUri);
            sb.append(':');
            sb.append(this.name);
            return sb.toString();
        }
    }

}
