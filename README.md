<div style="text-align:center">
    <h1>AureliaStorm Community</h1>
    <img alt="GitHub Workflow Status" src="https://github.com/CollinHerber/AureliaStorm/workflows/Build/badge.svg">
    <a href="https://plugins.jetbrains.com/plugin/21949-aureliastorm-community" ><img alt="Build" src="https://img.shields.io/jetbrains/plugin/v/21949-aureliastorm-community.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/21949-aureliastorm-community"><img alt="Downloads" src="https://img.shields.io/jetbrains/plugin/d/21949-aureliastorm-community.svg"></a>
    <a href="https://github.com/CollinHerber/AureliaStorm"><img alt="Stars" src="https://img.shields.io/github/stars/CollinHerber/AureliaStorm"></a>
</div>

<!-- Plugin description -->

![](https://raw.githubusercontent.com/CollinHerber/AureliaStorm/master/plugin-demo.gif)

This plugin brings improved support for [Aurelia Framework](https://aurelia.io) to
the [IntelliJ platform](https://www.jetbrains.com/products.html?fromMenu#lang=js&type=ide)

> Plugin Supports Aurelia 1.x and 2.x

[Report an issue](https://github.com/CollinHerber/AureliaStorm/issues) | [GitHub](https://github.com/CollinHerber/AureliaStorm) | [JetBrains](https://plugins.jetbrains.com/plugin/21949-aureliastorm-community)

Either `aurelia`(v2) or `aurelia-cli`,`aurelia-framework`(v1) must be present in the project npm dependencies

### Custom elements & attributes

* Declaration resolving using `@customElement` and `@customAttribute` annotation or class names
* Custom element property recognition (`@bindable` annotation)
* Require and import tag reference detection for typescript files
* Component and property navigation (ctrl+click)
* Component lifecycle hooks (like `attached`, `detached`)
* Detecting bindable HTML attributes and events (such as `class.bind` or `click.delegate`)
* Custom element/attribute and property suggestions (ctrl+space)
* GoTo HTML/ts file action when in same folder (Default Alt+Ctrl+Shift+O)

### Insight for bindings and interpolation

* Can be enabled/disabled in the **plugin settings** (ony works with public members and can sometimes resolve references wrong)
* Code insight for `${}` and binding attribute values
* Controller properties completion and navigation
* `$this`, `$parent`, `$index`, `$event` support

<!-- Plugin description end -->

## Contributing

You can run the plugin using the [Run Plugin](/.run/Run%20Plugin.run.xml) configuration. This will launch an instance of intellij with the
plugin loaded (for more information check out the IntelliJ Template).

> This project is based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)