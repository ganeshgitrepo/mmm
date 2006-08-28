/* $Id: CallableRunner.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.ui.toolkit.base;

import java.util.concurrent.Callable;

/**
 * This class allows executing a {@link java.util.concurrent.Callable} as
 * {@link java.lang.Runnable}.
 * 
 * @param <T>
 *        is the templated type of the callable
 *        {@link java.util.concurrent.Callable#call() result}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class CallableRunner<T> implements Runnable {

    /** the callable task to execute */
    private final Callable<T> task;

    /** the result of the callable */
    private T result;

    /**
     * The constructor.
     * 
     * @param callable
     *        is the callable to adapt.
     */
    public CallableRunner(Callable<T> callable) {

        super();
        this.task = callable;
        this.result = null;
    }

    /**
     * @see java.lang.Runnable#run()
     * {@inheritDoc}
     */
    public void run() {

        try {
            this.result = this.task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the {@link Callable#call() result} of the callable.
     * 
     * @return the result.
     */
    public T getResult() {

        return this.result;
    }

}
