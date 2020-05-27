import { LitElement, html } from 'lit-element';
import '@vaadin/vaadin-icons';
import '@polymer/iron-icon';

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
        return html`<iron-icon icon=${this.value}></iron-icon>`;
    }
}

customElements.define('icon-lit-renderer', IconLitRenderer);