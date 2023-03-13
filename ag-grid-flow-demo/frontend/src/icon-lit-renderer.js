import { LitElement, html } from 'lit';
import '@vaadin/icon';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import '@vaadin/icons';

class IconLitRenderer extends LitElement {
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
            this.value = 'vaadin:'+ params.value;
        }
    }
    render() {
        return html`<vaadin-icon icon=${this.value}></vaadin-icon>`;
    }
}

customElements.define('icon-lit-renderer', IconLitRenderer);