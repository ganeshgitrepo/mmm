package net.sf.mmm.ui.toolkit.impl.swt.model;

import org.eclipse.swt.widgets.Combo;

import net.sf.mmm.ui.toolkit.api.event.EventType;
import net.sf.mmm.ui.toolkit.api.event.UIListModelEvent;
import net.sf.mmm.ui.toolkit.api.event.UIListModelListenerIF;
import net.sf.mmm.ui.toolkit.api.model.UIListModelIF;
import net.sf.mmm.ui.toolkit.impl.swt.sync.SyncComboAccess;

/**
 * This class adapts from {@link net.sf.mmm.ui.toolkit.api.model.UIListModelIF}
 * to a {@link org.eclipse.swt.widgets.Combo}. It is the controler of the MVC
 * pattern.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class ComboBoxModelAdapter implements UIListModelListenerIF, Runnable {

    /** sync access to the combo-box */
    private final SyncComboAccess syncAccess;

    /** the list model */
    private UIListModelIF<?> model;

    /** the event to handle */
    private UIListModelEvent event;

    /**
     * The constructor.
     * 
     * @param comboAccess
     *        is the access to the SWT combo-box widget.
     * @param listModel
     *        is the model defining the elements to select.
     */
    public ComboBoxModelAdapter(SyncComboAccess comboAccess, UIListModelIF<?> listModel) {

        this.syncAccess = comboAccess;
        this.model = listModel;
        // this.model.addListener(this);
    }

    /**
     * @see net.sf.mmm.ui.toolkit.api.event.UIListModelListenerIF#listModelChanged(net.sf.mmm.ui.toolkit.api.event.UIListModelEvent)
     * 
     */
    public synchronized void listModelChanged(UIListModelEvent changeEvent) {

        this.event = changeEvent;
        this.syncAccess.getDisplay().invokeSynchron(this);
    }

    /**
     * This method gets the list-model that is adapted.
     * 
     * @return the actual list-model.
     */
    public UIListModelIF<?> getModel() {

        return this.model;
    }

    /**
     * This method sets the list-model. If a model was already set, all items of
     * that model will be removed. The items of the new model will be added to
     * the widget.
     * 
     * @param newModel
     *        is the new list-model.
     */
    public synchronized void setModel(UIListModelIF<?> newModel) {

        this.model.removeListener(this);
        this.syncAccess.removeAll();
        this.model = newModel;
        initialize();
    }

    /**
     * This method initializes the combo after it has been creation or the model
     * changed.
     */
    public void initialize() {

        if (this.syncAccess.getSwtObject() != null) {
            this.event = null;
            this.syncAccess.getDisplay().invokeSynchron(this);
            this.model.addListener(this);            
        }
    }

    /**
     * This method is called synchron to handle an model update event.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

        if (this.event == null) {
            // initialize
            Combo combo = this.syncAccess.getSwtObject();
            synchronized (this.model) {
            int count = this.model.getElementCount();
            for (int i = 0; i < count; i++) {
                combo.add(this.model.getElementAsString(i));
            }            
            }
        } else {
            int start = this.event.getStartIndex();
            int end = this.event.getEndIndex();
            Combo combo = this.syncAccess.getSwtObject();
            if (this.event.getType() == EventType.ADD) {
                for (int i = start; i <= end; i++) {
                    combo.add(this.model.getElementAsString(i), i);
                }
            } else if (this.event.getType() == EventType.REMOVE) {
                // for (int i = start; i <= end; i++) { combo.remove(i); }
                combo.remove(start, end);
            } else if (this.event.getType() == EventType.UPDATE) {
                for (int i = start; i <= end; i++) {
                    combo.setItem(i, this.model.getElementAsString(i));
                }
            }            
        }
    }

}