/* $Id: UIListModelEvent.java 191 2006-07-24 21:00:49Z hohwille $ */
package net.sf.mmm.ui.toolkit.api.event;

/**
 * This class represents the event sent by the list model to its listeners to
 * notify about changes of the list.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public final class UIListModelEvent extends UIModelEvent {

    /** the index of the first item that has changed */
    private int startIndex;

    /** the index of the last item that has changed */
    private int endIndex;

    /**
     * The constructor.
     * 
     * @param eventType
     *        is the type for the new event.
     * @param startItemIndex
     *        is the index of the first item that has changed.
     * @param endItemIndex
     *        is the index of the last item that has changed.
     */
    public UIListModelEvent(EventType eventType, int startItemIndex, int endItemIndex) {

        super(eventType);
        this.startIndex = startItemIndex;
        this.endIndex = endItemIndex;
    }

    /**
     * The constructor.
     * 
     * @param eventType
     *        is the type for the new event.
     * @param itemIndex
     *        is the index of the according item.
     */
    public UIListModelEvent(EventType eventType, int itemIndex) {

        super(eventType);
        this.startIndex = itemIndex;
        this.endIndex = itemIndex;
    }

    /**
     * This method gets the index of the first item that has changed (updated,
     * removed or added/inserted). If only one item has changed, the value is
     * equal to {@link #getEndIndex()}
     * 
     * @return the index of the first item that has changed.
     */
    public int getStartIndex() {

        return this.startIndex;
    }

    /**
     * This method gets the index of the last item that has changed (updated,
     * removed or added/inserted). If only one item has changed, the value is
     * equal to {@link #getStartIndex()}.
     * 
     * @return the index of the last item that has changed.
     */
    public int getEndIndex() {

        return this.endIndex;
    }

}