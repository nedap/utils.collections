# nedap.utils.collections

Utilities for collection processing, organised by purpose (or collection type).

Also, for certain error-prone `clojure.core` functions, drop-in replacements are offered, providing stronger specing (plus type hinting), addressing their error-prone aspects.

## Installation

```clojure
[com.nedap.staffing-solutions/utils.collections "1.0.0"]
```

## ns organisation

Everything not under an `impl` folder or suffix is a public API, and should be concise and optimized for readability.

## Documentation

Please browse the public namespaces, which should be concise, documented, speced and tested.

## Development

I'd suggest that every function should be heavily speced. That way, consumer projects get a free layer of checking, and type hinting.

`clojure.core` has a different philosophy (it can return meaningful results for nil arguments, and dubious results for arguments of unexpected type),
which is a good idea for a general-purpose language aiming a variety of use cases and styles.

In practice, for a lot of use-cases one certainly wants things to be well-formed and to fail fast.

## License

Copyright © Nedap

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0.
