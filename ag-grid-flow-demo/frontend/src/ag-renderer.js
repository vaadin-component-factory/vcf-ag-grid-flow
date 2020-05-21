
if (typeof window.Vaadin.Flow.agGridRenderers === 'undefined') {

    window.Vaadin.Flow.agGridRenderers = [];
}

window.Vaadin.Flow.agGridRenderers["AdultRenderer"] = function AdultRenderer(params) {
    // debugger;
    if (typeof params.value  === 'undefined') {
        return document.createElement("span");
    }
    const element = document.createElement("span");
    let color = "red";
    if ( params.value  > 18 ) {
        color = "green";
    }
    element.appendChild(document.createTextNode(params.value));
    element.style.color = color;
    return element;
}
let MyCellRenderer  = function MyCellRenderer () {}
// function to act as a class
window.Vaadin.Flow.agGridRenderers["AdultRenderer"] = MyCellRenderer;

// gets called once before the renderer is used
MyCellRenderer.prototype.init = function(params) {
    // create the cell
    this.eGui = document.createElement('div');
    this.eGui.innerHTML = '<span class="my-css-class"><button class="btn-simple">Push Me</button><span class="my-value"></span></span>';

    // get references to the elements we want
    this.eButton = this.eGui.querySelector('.btn-simple');
    this.eValue = this.eGui.querySelector('.my-value');

    // set value into cell
    this.eValue.innerHTML = params.valueFormatted ? params.valueFormatted : params.value;

    // add event listener to button
    this.eventListener = function() {
        console.log('button was clicked!!');
    };
    this.eButton.addEventListener('click', this.eventListener);
};

// gets called once (assuming destroy hasn't been called first) when grid ready to insert the element
MyCellRenderer.prototype.getGui = function() {
    return this.eGui;
};

// gets called whenever the user gets the cell to refresh
MyCellRenderer.prototype.refresh = function(params) {
    // set value into cell again
    this.eValue.innerHTML = params.valueFormatted ? params.valueFormatted : params.value;
    // return true to tell the grid we refreshed successfully
    return true;
};

// gets called when the cell is removed from the grid
MyCellRenderer.prototype.destroy = function() {
    // do cleanup, remove event listener from button
    if (this.eButton) {
        // check that the button element exists as destroy() can be called before getGui()
        this.eButton.removeEventListener('click', this.eventListener);
    }
};


import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";

export default class CurrencyCellRenderer extends PolymerElement {
    static get template() {
        return html`
            <span>{{currency}}{{formattedValue}}</span>
        `;
    }

    agInit(params) {
        console.log("params {}", params);
        console.log("params.value {}", params.value);
        this.params = params;
        if (typeof params.value  === 'undefined') {

        } else {
            // todo jcg rowId, colId, action name

            this.currency = this.params.value.currency;
            this.value = this.params.value.price;
        }
    }

    handleClick(e) {
        this.params.colDef.cellRendererFrameworkCallback.call(this, 1,2,"action");
        console.log(this.prop);
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

customElements.define('currency-cell-renderer', CurrencyCellRenderer);

import { LitElement, html as htmllit } from 'lit-element';

class SimpleGreeting extends LitElement {
    static get properties() {
        return { name: { type: String } };
    }

    constructor() {
        super();
        this.name = 'World';
    }

    agInit(params) {
        console.log("params {}", params);
        this.params = params;
        if (typeof params.value  === 'undefined') {
        } else {
            console.log("params.value {}", params.value);
            this.name = this.params.value;
           // this.name = this.params.value.currency;

            //  this.currency = this.params.value.currency;
          //  this.value = this.params.value.price;
        }
    }

    handleClick(e) {
        // todo jcg rowId, colId, action name
        debugger;
        this.params.colDef.cellRendererFrameworkCallback.call(this, this.params.colDef.field,
            this.params.rowIndex, "handleClick");
    }
    createRenderRoot() {
        /**
         * Render template without shadow DOM. Note that shadow DOM features like
         * encapsulated CSS and slots are unavailable.
         */
        return this;
    }
    render() {
        return htmllit`<p @click="${this.handleClick}">Hello, ${this.name}!</p>`;
    }
}

customElements.define('simple-greeting', SimpleGreeting);