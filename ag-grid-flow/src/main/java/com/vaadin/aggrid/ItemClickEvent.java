package com.vaadin.aggrid;

/*
 * #%L
 * ag-grid-flow
 * %%
 * Copyright (C) 2020 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
