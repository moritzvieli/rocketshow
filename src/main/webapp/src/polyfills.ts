/**
 * This file includes polyfills needed by Angular and is loaded before the app.
 * You can add your own extra polyfills to this file.
 *
 * This file is divided into 2 sections:
 *   1. Browser polyfills. These are applied before loading ZoneJS and are sorted by browsers.
 *   2. Application imports. Files imported after ZoneJS that should be loaded before your main
 *      file.
 *
 * The current setup is for so-called "evergreen" browsers; the last versions of browsers that
 * automatically update themselves. This includes Safari >= 10, Chrome >= 55 (including Opera),
 * Edge >= 13 on the desktop, and iOS 10 and Chrome on mobile.
 *
 * Learn more in https://angular.io/guide/browser-support
 */

/***************************************************************************************************
* BROWSER POLYFILLS
*/

/** Evergreen browsers require these. **/
import 'core-js/es6/reflect';  // Run `npm install --save web-animations-js`.



/***************************************************************************************************
 * Zone JS is required by Angular itself.
 */

// Disable change detection cycle on request requestAnimationFrame, which runs 60 times a second in the
// preview component with threejs.
// See https://stackoverflow.com/questions/54416133/is-it-possible-to-use-ngzone-noop-for-particular-function-component-in-an
import './ngzone-flags';

import 'zone.js';  // Included with Angular CLI.



/***************************************************************************************************
 * APPLICATION IMPORTS
 */

/**
 * Date, currency, decimal and percent pipes.
 * Needed for: All but Chrome, Firefox, Edge, IE11 and Safari 10
 */
//import 'intl';  // Run `npm install --save intl`.
/**
 * Need to import at least one locale-data with intl.
 */
// import 'intl/locale-data/jsonp/en';
