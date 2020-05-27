import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import '@vaadin/vaadin-icons';
import '@polymer/iron-icon';

class IconPolymerRenderer extends PolymerElement {
    static get template() {
        return html`<iron-icon icon='[[value]]'></iron-icon>`;
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
