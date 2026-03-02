## Design Decision — Strict `DataClassification` Union Type

### Decision

Use a TypeScript union type to restrict asset classification to:

`PUBLIC | INTERNAL | CONFIDENTIAL | RESTRICTED`

### Rationale

This enforces valid classification values at compile time.

It prevents:

- Typos  
- Inconsistent values  
- Breaking risk logic  
- Breaking PIPEDA compliance modeling :contentReference[oaicite:0]{index=0}  
- Breaking dashboard calculations :contentReference[oaicite:1]{index=1}  

Moreover, "type" allows us to do the union between types (as we did above), which is not a feature in interfaces.
### Architectural Impact

The frontend enforces governance rules before the backend receives the request.

This ensures domain integrity, compliance alignment, and predictable risk modeling.
