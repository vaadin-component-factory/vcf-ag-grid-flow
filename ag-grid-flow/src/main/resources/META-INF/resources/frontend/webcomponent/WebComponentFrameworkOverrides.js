
import {VanillaFrameworkOverrides} from "@ag-grid-community/core";
export default class WebComponentFrameworkOverrides extends VanillaFrameworkOverrides {
    frameworkComponent(name) {
        if (name.startsWith("ag")) {
            return null;
        }
        if (customElements.get(name)) {
            return name;
        }
        return name;
    };
}
