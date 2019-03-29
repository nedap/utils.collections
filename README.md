# nedap.utils.collections

Utilities for collection processing, organised by purpose.

## Installation

```clojure
[com.nedap.staffing-solutions/utils.collections "0.2.0"]
```

## ns organisation

Everything not under an `impl` folder or suffix is a public API, and should be concise and optimized for readability.

## Documentation

Please browse the public namespaces, which should be concise, documented, speced and tested.

## Development

I'd suggest that every function should be heavily speced. That way, consumer projects get a free layer of checking.

`clojure.core` has a different philosophy (it can return meaningful results for nil arguments, and dubious results for arguments of unexpected type),
which is a good idea for a general-purpose language aiming a variety of use cases and styles.

In practice, for a lot of use-cases one certainly wants things to be well-formed and to fail fast.

## License

Copyright © Nedap

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
