import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import '@vaadin/vaadin-text-field';

class TextfieldPolymerRenderer extends PolymerElement {
    static get template() {
        return html`<vaadin-text-field value="{{value}}" style="width:100%;"/>`;
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

customElements.define('textfield-polymer-renderer', TextfieldPolymerRenderer);
