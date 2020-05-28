if (typeof window.Vaadin.Flow.agGridRenderers === 'undefined') {

    window.Vaadin.Flow.agGridRenderers = [];
}

let CurrencyHtmlRenderer  = function CurrencyHtmlRenderer () {}
// function to act as a class
window.Vaadin.Flow.agGridRenderers["currency-html-renderer"] = CurrencyHtmlRenderer;

// gets called once before the renderer is used
CurrencyHtmlRenderer.prototype.init = function(params) {
    //console.log("params {}", params);
    // create the cell
    this.eGui = document.createElement('span');
    this.eGui.innerHTML = '<span class="renderer-currency"></span> <span class="renderer-value"></span>';

    // get references to the elements we want
    this.eCurrency = this.eGui.querySelector('.renderer-currency');
    this.eValue = this.eGui.querySelector('.renderer-value');

    // set value into cell
    if (typeof params.value  === 'undefined') {
    } else {
        const value = params.value.price;
        this.eValue.innerHTML = value ? value.toFixed(2) : value;
        this.eCurrency.innerHTML = params.value.currency;
    }

    // add event listener to button
    this.eventListener = function() {
        params.colDef.cellRendererFrameworkCallback.call(this, params.colDef.field,
            params.rowIndex, "currencyClicked");
    };
    this.eCurrency.addEventListener('click', this.eventListener);
};

// gets called once (assuming destroy hasn't been called first) when grid ready to insert the element
CurrencyHtmlRenderer.prototype.getGui = function() {
    return this.eGui;
};

// gets called whenever the user gets the cell to refresh
CurrencyHtmlRenderer.prototype.refresh = function(params) {
    // set value into cell again
    this.eValue.innerHTML = params.valueFormatted ? params.valueFormatted : params.value;
    // return true to tell the grid we refreshed successfully
    return true;
};

// gets called when the cell is removed from the grid
CurrencyHtmlRenderer.prototype.destroy = function() {
    // do cleanup, remove event listener from button
    if (this.eCurrency) {
        // check that the button element exists as destroy() can be called before getGui()
        this.eCurrency.removeEventListener('click', this.eventListener);
    }
};