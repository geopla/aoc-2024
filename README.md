# Advent of code puzzles

## Day 1 - Historian Hysteria
Lessons learned
- don't use regex to delimit input while reading. Failed on unknown reason. `Scanner` or `BufferedReader` are the way to go.
- reading JDK API docs doesn't hurt at all

## Day 2 - Red-Nosed Reports
Lessons learned
- found three variants to implement a sliding window of size n on a Java stream
  - with `Spliterator` (the actual implementation with fixed window size 2 being the most efficient one)
  - with a custom collector (usage like the spliterator solution)
  - with a deque (nice implementation but usage not that intuitive)
- try to gain an understanding of stream ordering
- state is the enemy of coding in functional style
- brute force is always an option
- streams can be reused with some effort

## Day 3 - Mull It Over
### From the Requirements
- Input might become huge
- Digit size of arguments is restricted
- Only a single operation name yet
- Binary operation only

### Assumptions
- No recursion in operation expected
- Constraint on digit size of arguments may change
- Another operation might be added
- N-ary operations with n >= 1 might be possible in future

### Decisions
- Implement a streaming solution taming probably huge input
- Allow detection of other operations
- Allow detection of n-ary operations
  - both points above will ease the implementation of the operations detector
- Filter the operations stream for name and arity to get the correct result
- Speed of development is irrelevant; focus on learnings with Spliterators

### Implementation

#### State Machine Happy Path
This is the state machine assuming when only valid input happens. It is not the real implementation but serves
to show the principle:

````mermaid
stateDiagram-v2
    [*] --> OP_NAME: [a..z]
    OP_NAME --> OP_NAME: [a..z]
    OP_NAME --> OP_NAME_TERMINATOR: '('
    OP_NAME_TERMINATOR --> ARGUMENT: [0..9]
    ARGUMENT --> ARGUMENT: [0..9]
    ARGUMENT --> ARGUMENT_TERMINATOR: ','
    ARGUMENT_TERMINATOR --> ARGUMENT: [0..9]
    ARGUMENT --> [*]: ')'
````

#### State Machine with Error Handling
- while in start state, simply discard any input characters not switching to OP_NAME
- in every other state, input that does not switch to the next state, or remains in the current state,
  might be the start of a valid operation name

This is the state machine implemented actually:

````mermaid
stateDiagram-v2
  START --> OP_NAME: 'm'
  note left of START: Any input not mentioned switches to this start state
  OP_NAME --> OP_NAME: [a..z]
  OP_NAME --> OP_NAME_TERMINATOR: '('
  OP_NAME_TERMINATOR --> ARGUMENT: [0..9]
  OP_NAME_TERMINATOR --> OP_NAME: 'm'
  ARGUMENT --> ARGUMENT: [0..9]
  ARGUMENT --> ARGUMENT_TERMINATOR: ','
  ARGUMENT --> OP_NAME: 'm'
  ARGUMENT_TERMINATOR --> ARGUMENT: [0..9]
  ARGUMENT_TERMINATOR --> OP_NAME: 'm'
  ARGUMENT --> [*]: ')'
````