/* $Id$ */
package net.sf.mmm.content.validator.api;

import net.sf.mmm.nls.api.NlsMessageIF;

/**
 * This is the interface for the result of a validation. If an object is invalid
 * the validation result will include ALL reasons explaining why (not only the
 * first). Therefore it has a complex structure allowing composition of
 * validation results analog to the structure of validators.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface ValidationResultIF {

    /**
     * This method determines the status of the validation result.
     * 
     * @return <code>true</code> if the validation was successful and
     *         <code>false</code> if the validation failed because the object
     *         is invalid.
     */
    boolean isValid();

    /**
     * This method gets a short internationalized message describing the result.
     * If the result is invalid the message must excactly explain why the object
     * is invalid. The message should only include a string representation of
     * the object itself if this is the result of a top-level validator.
     * 
     * @return the message describing why the object is invalid or
     *         <code>null</code> if the object is valid. If this is a
     *         {@link ValidationResultIF#getDetailCount() composite} result, the
     *         real reason why the object is invalid may be described in the
     *         {@link #getDetail(int) details}.
     */
    NlsMessageIF getMessage();

    /**
     * This method gets the number of details in this result. A validation
     * result is a recursive structure and may be composite, meaning that it has
     * children that give more detailed information why the validation failed.
     * 
     * @return the number of details available.
     */
    int getDetailCount();

    /**
     * This method gets the detailed child result at the given position.
     * 
     * @see ValidationResultIF#getDetailCount()
     * 
     * @param index
     *        is the position of the requested detail.
     * @return the detailed child result at the given index.
     */
    ValidationResultIF getDetail(int index);

}