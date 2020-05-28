import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";

class CurrencyPolymerRenderer extends PolymerElement {
    static get template() {
        return html`<span><span class="renderer-currency" on-click="clickOnCurrency">{{currency}}</span> <span class="renderer-value">{{formattedValue}}</span></span>
        `;
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

    clickOnCurrency(e) {
        this.params.colDef.cellRendererFrameworkCallback.call(this, this.params.colDef.field,
            this.params.rowIndex, "currencyClicked");
    }

    static get properties() {
        return {
            currency: String,
            value: Number,
            formattedValue: {
                type: Number,
                computed: 'format(value)'
            }
        };
    }

    format(value) {
        return value ? value.toFixed(2) : value;
    }
}

customElements.define('currency-polymer-renderer', CurrencyPolymerRenderer);
