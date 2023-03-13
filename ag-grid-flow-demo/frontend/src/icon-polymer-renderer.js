import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import '@vaadin/icon';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import '@vaadin/icons';

class IconPolymerRenderer extends PolymerElement {
    static get template() {
        return html`<vaadin-icon icon='[[value]]'></vaadin-icon>`;
    }

    agInit(params) {
        //console.log("params {}", params);
        if (typeof params.value  === 'undefined') {

        } else {
            this.value = 'vaadin:' + params.value;
        }
    }

    static get properties() {
        return {
            value: {
                type: String,
                value: ''
            }
        };
    }

}

customElements.define('icon-polymer-renderer', IconPolymerRenderer);
