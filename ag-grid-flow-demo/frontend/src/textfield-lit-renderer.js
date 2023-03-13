import { LitElement, html } from 'lit';
import '@vaadin/text-field';

class TextfieldLitRenderer extends LitElement {
    static get properties() {
        return {
            value: { type: String }
        };
    }

    constructor() {
        super();
        this.value = "";
    }

    agInit(params) {
        if (typeof params.value  === 'undefined') {
        } else {
            this.value = params.value;
        }
    }
    render() {
        return html`<vaadin-text-field value="${this.value}" style="width:100%;"/>`;
    }
}

customElements.define('textfield-lit-renderer', TextfieldLitRenderer);