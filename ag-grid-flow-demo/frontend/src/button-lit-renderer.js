import { LitElement, html } from 'lit-element';
import '@vaadin/vaadin-button';

class ButtonLitRenderer extends LitElement {
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
        return html`<vaadin-button>${this.value}</vaadin-button>`;
    }
}

customElements.define('button-lit-renderer', ButtonLitRenderer);