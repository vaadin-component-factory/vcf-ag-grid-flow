import { LitElement, html as html } from 'lit-element';

class CurrencyLitRenderer extends LitElement {
    static get properties() {
        return {
            currency: { type: String },
            value: { type: Number }
        };
    }

    constructor() {
        super();
    }

    agInit(params) {
        //console.log("params {}", params);
        this.params = params;
        if (typeof params.value  === 'undefined') {
        } else {
            this.currency = this.params.value.currency;
            this.value = this.params.value.price;
        }
    }

    get formattedValue() {
        return this.value ? this.value.toFixed(2) : this.value;
    }

    clickOnCurrency(e) {
        this.params.colDef.cellRendererFrameworkCallback.call(this, this.params.colDef.field,
            this.params.rowIndex, "currencyClicked");
    }
    /**
     * Render template without shadow DOM. Note that shadow DOM features like
     * encapsulated CSS and slots are unavailable.
     */
    /*createRenderRoot() {

        return this;
    }*/
    render() {
        return html`<span><span class="renderer-currency" @click="${this.clickOnCurrency}">${this.currency}</span> <span class="renderer-value">${this.formattedValue}</span></span>`;
    }
}

customElements.define('currency-lit-renderer', CurrencyLitRenderer);