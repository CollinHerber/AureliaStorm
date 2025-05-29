# AureliaStorm Community Changelog

## [Unreleased]

### Changed

- Combining plugins AureliaStorm Re and AureliaStorm Community
- Injection now enables as a default

## [2.3.1] - 2025-05-26

### Changed

- Description has now a gif showing some features
- Lifecycle method detection for all exported js classes

## [2.3.0] - 2025-05-12

### Added

- Hook support for components (eg. `attached`,`detached` functions)
- Change callback detection (only works for `propertyChanged` methods)

## [2.2.0]

### Added

- Proper `repeat.for` detection and reference resolving
- Ignoring binding behaviors and value converts for code injection
- Suppressing of missing promise for aurelia js injected code
- `$this`, `$parent`, `$index`, `$event` support for code injections
- GoTo Declaration/definition (Default Alt+Ctrl+Shift+O)
- Custom element completion (ctrl+space)
- Property and custom attribute completion (ctrl+space)

## [2.1.0]

### Added

- Support for 251

## [2.0.1]

### Fixed

- Reference detection getting called for files outside of aurelia

## [2.0.0]

### Added

- Custom element / attribute recognition
- References gets resolved over <require from=""> tags
- Without require a class with matching @customElement will be taken (also works with name suffix)
- Developer Note: Having custom elements with the same name might not recognize correctly in some instances
- Require and import tag reference detection and navigation
- Detecting bindable HTML attributes and events
- Require and import tag support
- Custom element bindable property recognition (by @bindable)
- Suppress <template> and <require> element warnings

## [1.2.1]

### Changed

- Support older versions

## [1.2.0]

### Added

- Support <let> element recognition
- Support the `else` attribute
- Adds support for `promise.bind` recognition

### Fixed

- Remove deprecated getDependencies Call
- Remove deprecated getBaseDir call

[Unreleased]: https://github.com/Readock/AureliaStormRe/compare/v2.3.1...HEAD

[2.3.1]: https://github.com/Readock/AureliaStormRe/compare/v2.3.0...v2.3.1

[2.3.0]: https://github.com/Readock/AureliaStormRe/compare/v2.2.0...v2.3.0

[2.2.0]: https://github.com/Readock/AureliaStormRe/compare/v2.1.0...v2.2.0

[2.1.0]: https://github.com/Readock/AureliaStormRe/compare/v2.0.1...v2.1.0

[2.0.1]: https://github.com/Readock/AureliaStormRe/compare/v2.0.0...v2.0.1

[2.0.0]: https://github.com/Readock/AureliaStormRe/compare/v1.2.1...v2.0.0

[1.2.1]: https://github.com/Readock/AureliaStormRe/compare/v1.2.0...v1.2.1

[1.2.0]: https://github.com/Readock/AureliaStormRe/commits/v1.2.0
