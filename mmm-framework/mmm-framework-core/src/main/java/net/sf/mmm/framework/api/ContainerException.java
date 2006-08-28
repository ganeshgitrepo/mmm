/* $Id: ContainerException.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.framework.api;

import net.sf.mmm.nls.base.NlsRuntimeException;

/**
 * A {@link ContainerException} is thrown the
 * {@link IocContainerIF IoC container} itself is in an exceptional state.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class ContainerException extends IocException {

    /** UID for serialization */
    private static final long serialVersionUID = -596276084369784042L;

    /**
     * @see NlsRuntimeException#NlsRuntimeException(String, Object[])
     * {@inheritDoc}
     */
    public ContainerException(String newInternaitionalizedMessage, Object... newArguments) {

        super(newInternaitionalizedMessage, newArguments);
    }

    /**
     * @see NlsRuntimeException#NlsRuntimeException(Throwable, String, Object[])
     * {@inheritDoc}
     */
    public ContainerException(Throwable newNested, String newInternaitionalizedMessage,
            Object... newArguments) {

        super(newNested, newInternaitionalizedMessage, newArguments);
    }

}
