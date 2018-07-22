import { Component } from "@angular/core";

@Component({
    selector: "ns-app",
    templateUrl: "app.component.html"
})
export class AppComponent {

    constructor() {
        let configManager = NEHotspotConfigurationManager.new();
        let config = NEHotspotConfiguration.alloc().initWithSSID('Guest');

        config.joinOnce = true;

        configManager.applyConfigurationCompletionHandler(config, (error: NSError) => {
            if (error && error.code !== NEHotspotConfigurationError.AlreadyAssociated) {
                console.log(`wifiManager.applyConfigurationCompletionHandler error code ${error.code}: ${error.localizedDescription}`);
            } else {
                console.log(`wifiManager.applyConfigurationCompletionHandler success!`);
            }
        });
    }

}
