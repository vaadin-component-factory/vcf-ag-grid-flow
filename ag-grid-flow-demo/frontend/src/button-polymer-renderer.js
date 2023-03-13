import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import '@vaadin/button';

class ButtonPolymerRenderer extends PolymerElement {
    static get template() {
        return html`<vaadin-button>[[value]]</vaadin-button>`;
    }

    agInit(params) {
        //console.log("params {}", params);
        if (typeof params.value  === 'undefined') {

        } else {
            this.value = params.value;
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

customElements.define('button-polymer-renderer', ButtonPolymerRenderer);
