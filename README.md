# Codecium
A *Fabric* mod for *Minecraft* that vastly improves the error messages in *Codecs*, a portion in [*Mojang's DataFixerUpper*](https://github.com/Mojang/DataFixerUpper) library dedicated to data structuring and transformations.
The name is a play on the term *Codec* and the common use of elements for Minecraft mods.
Below you can find the way the error messages are constructed by the rules and terminology to stay consistent across the board, as well as a list of common keywords and their descriptions for use in error messages.

## Rules and Terminology
### General
> Errors must **always** contain the following unless stated otherwise:
- The type of the expected element.
- The expected value.
- The actual value if and only if it differs from the input.
- The description comparing the expected and actual value or input.
- The element as provided on a single line with **no or minimal** whitespace between elements.

Example: `Float must be at least 1.0: -100.0`
- Type expected element: `Float`.
- Expected value: `at least 1.0`.
- Actual value: `-100.0`.
- Description comparing expected and actual value: `must be at least 1.0`.
- Element: `-100.0`.

Example: `String must have a length of 10 characters, but got 5: "hello"`
- Type expected element: `String`.
- Expected value: `10`.
- Actual value: `5`.
- Description comparing expected and actual value: `must have a length of 10 characters, but got 5`.
- Element: `"hello"`.

> Do not repeat the input as the actual value if it is the same to prevent useless duplication of elements.

For example, require `Float must be at least 1.0: -100.0` over `Float must be at least 1.0, but got -100.0: -100.0`

> If an error contains one or more inner errors (an error collection), it must **always** contain the following:
- The type of the error containing the inner errors.
- The description for the collection of the inner errors.
- Each inner error as described by the rules, separated by a unique key (if none exists an incrementing integer starting from 1), a colon (*Unicode* code point 58) and a space (*Unicode* code point 32).

> **Always** expand the inner errors of error collections as much as possible for maximum readability.
- **Always** use two space characters (*Unicode* code point 32) for indenting inner errors, **never** horizontal tabs (*Unicode* code point 9) or any other whitespace characters.
- **Always** use the line feed character (*Unicode* code point 10) for new lines separating inner errors and the initial line, **never** carriage returns (*Unicode* code point 13).

Example:
```
Could not decode either:
  1: Inner error: 123
  2: Other inner error: 123
```
- Type error containing inner errors: `either`
- Description collection of inner errors: `Could not decode either`
- Each inner error and its assigned key:
    - `Inner error: 123`, `1`
    - `Other inner error: 123`, `2`

Example with a collection of errors containing another collection of errors:
```
Could not decode either:
  1: Could not decode either:
    1: Inner error A1: 123
    2: Inner error A2: 123
  2: Inner error B: 123
```

### Wording
> Message descriptions **must** read as correct US English sentences and use as little punctuation and symbols as possible.

For example, require `Float must be less than 10.0` over `Float is outside of range: [1.0, 10.0)`.

> Messages **must not** include any joining clauses (*and*, *or*, etc.) to be as brief and specific as possible in order to prevent any ambiguity and to prevent misreading an error message as much as possible.
> The **only** exception to this rule is *but* to describe a comparison between an expected and actual value that does **not** immediately come from the input itself.

For example, *but* is allowed when comparing string lengths, as it is not immediately clear where the expected number comes from.
However, *but* is **not** allowed when comparing the range of a number, as it is operating on the input itself.

For example, require the two following messages:
- `Float must be at least 1.0: -100.0`
- `Float must be less than 10.0: 100.0`

Over the following single message:
- `Float must be at least 1.0 and less than 10.0: -100.0`

Notice how a difference in input is required for the two separate messages and can therefore not lead to ambiguity or someone accidentally misreading the two clauses.

For example, allow `String must have a length of 10 characters, but got 5: "hello"`, because the expected and actual element (`10` and `5` respectively) do **not** come from the actual input (`"hello"`).

> Prefer full words over contractions.

For example, prefer `cannot` over `can't`.

> Use assertions rather than expectations.

For example, require `Element must ...` over `Expected element to ...`.
This causes the sentence to be more active and directly targets the required element.

> Statements must not be unnecessarily negated.

For example, require `Float must be at most 10.0` over `Float must be no more than 10.0`.
This makes the description clearer (`value <= 10.0`) by not using an unnecessary negation (`not (value > 10.0)`)

### Exceptions
> Error messages for exceptions must **always** contain the following:
- The full message of the exception as given by `Exception::getMessage`.
- The element as provided on a new single line with no or minimal whitespace, indented with two spaces (*Unicode* code point 32), the word `Element`, a colon (*Unicode* code point 58) and another space (*Unicode* code point 32) before the element.

Example:
```
Unterminated string at line 1 column 13 path $
  Element: '"hello world'
```

> Error messages for exceptions must **never** include the stack trace.

> Error messages for exceptions must **always** follow all inner error rules regarding expansion.

## Keywords
These are commonly used words in *Codecs* with their descriptions.

### Elements
| Term    | Description                                     |
|---------|-------------------------------------------------|
| Element | Any describable value.                          |
| Boolean | An element of primitive type `boolean`.         |
| Byte    | An element of primitive type `byte`.            |
| Short   | An element of primitive type `short`.           |
| Integer | An element of primitive type `int`.             |
| Long    | An element of primitive type `long`.            |
| Float   | An element of primitive type `float`.           |
| Double  | An element of primitive type `double`.          |
| String  | An element of type `String`.                    |
| Encode  | Converting a Java type to a serialised element. |
| Decode  | Converting a serialised element to a Java type. |

### Numbers
| Term          | Description                                                       |
|---------------|-------------------------------------------------------------------|
| Value         | The actual quantity of a number.                                  |
| Exactly `N`   | A number that has a value equal to `N`.                           |
| More than `N` | A number that has a value larger than `N`.                        |
| At least `N`  | A number that has a value larger than or equal to `N`.            |
| Less than `N` | A number that has a value smaller than `N`.                       |
| At most `N`   | A number that has a value smaller than or equal to `N`.           |
| Positive      | A number that has a value larger than 0.                          |
| Non-negative  | A number that has a value larger than or equal to 0.              |
| Negative      | A number that has a value smaller than 0.                         |
| Non-positive  | A number that has a value smaller than or equal to 0.             |
| Range         | A boundary made up of two numbers to limit the value of a number. |

### Strings
| Term      | Description                                                    |
|-----------|----------------------------------------------------------------|
| Length    | An integer representing the number of characters in a string.  |
| Index     | An integer describing the position of a character in a string. |
| Too long  | A string that has too many characters.                         |
| Too short | A string that has too few characters.                          |

### Collections
| Term       | Description                                                                                                     |
|------------|-----------------------------------------------------------------------------------------------------------------|
| Collection | Some combination of elements.                                                                                   |
| Key        | An element describing another element in an unordered collection. Usually a string, but not strictly necessary. |
| Index      | An integer describing the position of another element in an ordered collection.                                 |
| Value      | An element associated with a key or index in a collection.                                                      |
| Field      | A key-value pair.                                                                                               |
| Map        | An unordered collection of fields.                                                                              |
| List       | An ordered collection of values accessible by index.                                                            |
| Size       | An integer for the number of values in a collection.                                                            |
| Too large  | A collection that has too many values.                                                                          |
| Too small  | A collection that has too few values.                                                                           |
