package com.vaadin.aggrid;

/*
 * #%L
 * ag-grid-flow
 * %%
 * Copyright (C) 2020 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import com.vaadin.flow.component.ClickEvent;

/**
 * Click event for the grid
 * @param <T> Type of the row
 */
public class ItemClickEvent<T> extends ClickEvent<AgGrid<T>> {

    private final T item;

    private final String columnKey;

    /**
     * Creates a new item click event.
     *
     * @param source
     *            the component that fired the event
     * @param fromClient
     *            <code>true</code> if the event was originally fired on the
     *            client, <code>false</code> if the event originates from
     *            server-side logic
     * @param item the item
     * @param columnId
     *            the internal id of the column associated with
     *            the click event
     * @param screenX
     *            the x coordinate of the click event, relative to the upper
     *            left corner of the screen, -1 if unknown
     * @param screenY
     *            the y coordinate of the click event, relative to the upper
     *            left corner of the screen, -i if unknown
     * @param clientX
     *            the x coordinate of the click event, relative to the upper
     *            left corner of the browser viewport, -1 if unknown
     * @param clientY
     *            the y coordinate of the click event, relative to the upper
     *            left corner of the browser viewport, -1 if unknown
     * @param clickCount
     *            the number of consecutive clicks recently recorded
     * @param button
     *            the id of the pressed mouse button
     * @param ctrlKey
     *            <code>true</code> if the control key was down when the event
     *            was fired, <code>false</code> otherwise
     * @param shiftKey
     *            <code>true</code> if the shift key was down when the event was
     *            fired, <code>false</code> otherwise
     * @param altKey
     *            <code>true</code> if the alt key was down when the event was
     *            fired, <code>false</code> otherwise
     * @param metaKey
     *            <code>true</code> if the meta key was down when the event was
     *            fired, <code>false</code> otherwise
     *
     */
    public ItemClickEvent(AgGrid<T> source, boolean fromClient, T item, String columnId, int screenX, int screenY,
                          int clientX, int clientY, int clickCount, int button, boolean ctrlKey, boolean shiftKey, boolean altKey,
                          boolean metaKey) {
        super(source, fromClient, screenX, screenY, clientX, clientY,
                clickCount, button, ctrlKey, shiftKey, altKey, metaKey);
        this.item = item;
        columnKey = columnId;
    }

    /**
     * Gets the clicked item.
     *
     * @return the clicked item
     */
    public T getItem() {
        return item;
    }

    /**
     * Gets the column key that was clicked.
     *
     * @return the clicked column key, not {@code null}
     */
    public String getColumnKey() {
        return columnKey;
    }
}
