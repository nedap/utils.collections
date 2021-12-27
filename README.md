# utils.collections

<!-- (In short: What does the project do?) -->

Utilities for collection processing, organised by purpose (or collection type).

Also, for certain error-prone `clojure.core` functions, drop-in replacements are offered, providing stronger specing (plus type hinting), addressing their error-prone aspects.

## Installation

```clojure
[com.nedap.staffing-solutions/utils.collections "2.2.1-alpha1"]
```

## ns organisation

Everything not under an `impl` folder or suffix is a public API, and should be concise and optimized for readability.

## Documentation

Please browse the public namespaces, which are documented, speced and tested.

## Development

I'd suggest that every function should be heavily speced. That way, consumer projects get a free layer of checking, and type hinting.

`clojure.core` has a different philosophy (it can return meaningful results for nil arguments, and dubious results for arguments of unexpected type),
which is a good idea for a general-purpose language aiming a variety of use cases and styles.

The default namespace is `dev`. Under it, `(refresh)` is available, which should give you a basic "Reloaded workflow".

> It is recommended that you use `(clojure.tools.namespace.repl/refresh :after 'formatting-stack.core/format!)`.

## License

Copyright © Nedap

This program and the accompanying materials are made available under the terms of the [Eclipse Public License 2.0](https://www.eclipse.org/legal/epl-2.0).
