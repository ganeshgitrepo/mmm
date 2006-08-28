/* $Id: UIWriteIntegerRangeIF.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.ui.toolkit.api.state;

/**
 * This interface gives read and write access to a
 * {@link #getMinimumValue() minimum} and {@link #getMaximumValue() maximum}
 * that define a integer value range.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface UIWriteIntegerRangeIF extends UIReadIntegerRangeIF {

    /**
     * This method sets the minimum value.
     * 
     * @param newMinimum
     *        is the new minimum value. It must be less than (or equal to) the
     *        {@link #getMaximumValue() maximum}. Use {@link #LOWEST_MINIMUM} for
     *        no minimum. Also {@link #LOWEST_MINIMUM} will be set if the value is
     *        less than that.
     * @throws IllegalArgumentException
     *         if the given value is greater than the
     *         {@link #getMaximumValue() maximum}.
     */
    void setMinimumValue(int newMinimum) throws IllegalArgumentException;

    /**
     * This method sets the maximum value.
     * 
     * @param newMaximum
     *        is the new maximum value. It must be greater than (or equal to)
     *        the {@link #getMinimumValue() minimum}. Use {@link #HIGHEST_MAXIMUM}
     *        for no maximum. Also {@link #HIGHEST_MAXIMUM} will be set if the value
     *        is greater than that.
     * @throws IllegalArgumentException
     *         if the given value is less than the
     *         {@link #getMinimumValue() minimum}.
     */
    void setMaximumValue(int newMaximum) throws IllegalArgumentException;

}
