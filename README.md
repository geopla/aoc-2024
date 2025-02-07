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
Lessons learned
- good ol' state machine as consumer to the rescue
- spliterators are fun
- stick to TDD

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
- digit size of argument is actually not a constraint but a hint that we don't need to take care in the input length 
- Speed of development is irrelevant; focus on learnings with Spliterators
- Regarding part two with disabling and enabling whole memory sections, the decision is to implement a dedicated
`OperationActivatorSpliterator`, throwing away all operations within a deactivated section.

### Implementation

#### State machine happy path
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

#### State machine with error handling
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

#### State machine allowing 0-ary operations too
The second part of the task requires detection of 0-ary operations too. The additional operation names are `do` and `don't`.
````mermaid
stateDiagram-v2
  START --> OP_NAME: 'm'
  note left of START: Any input not mentioned switches to this start state
  OP_NAME --> OP_NAME: [a..z] and '
  OP_NAME --> OP_NAME_TERMINATOR: '('
  OP_NAME_TERMINATOR --> ARGUMENT: [0..9]
  OP_NAME_TERMINATOR --> OP_NAME: 'm,d'
  OP_NAME_TERMINATOR --> [*]: ')'
  ARGUMENT --> ARGUMENT: [0..9]
  ARGUMENT --> ARGUMENT_TERMINATOR: ','
  ARGUMENT --> OP_NAME: 'm,d'
  ARGUMENT_TERMINATOR --> ARGUMENT: [0..9]
  ARGUMENT_TERMINATOR --> OP_NAME: 'm,d'
  ARGUMENT --> [*]: ')'
````

## Day 4

### Star search idea
The idea is to build a star from every input point and check each ray for `XMAS`.
```
\  |  /
 \ | /
  \|/
---X---
  /|\
 / | \
/  |  \
```
### Domain concepts
**Text Block** a sequenced list of text lines  
**Text Line** a single line of the text block, a `CharSequence`  
**X** column coordinates eastwards (from left to right), counting from 0
**Y** line coordinates southwards (from top to down), counting from 0  
**Cardinal direction** N, NE, E, SE, S, SW, W, NW  
**Ray** a sequence of at least two characters with a given max length, starting from a position in a cardinal direction  
**Star**  a collection of rays; note that rays might be shorter than given or even being empty  

### The PLN
- Implement `TextBlock`
  - Input from file
  - Input from CharSequence
- Implement `Star`
  - Input `TextBlock`, Position as tupel `(line, column)`
  - Output `Stream<Ray>`
- Get a `Stream<Star>` from `TextBlock`
- Process all stars by evaluating its rays (the XMAS count)

### Technical Debts
#### Computing of rays
Computing of rays may return one element rays. This contradicts the semantics of rays.
**Debt** Fixed by filtering rays  
**Payback** Implement returning an `Optional<Ray>` when no ray is computed because the star is on a text block border.
This is the very purpose of an `Optional` anyway.

Actually the constraint won't have any impact on the planned solution finding 'XMAS' on the rays. But it will result
into a slightly better performance because the one element rays are sorted out (round about 2 * lines + 2 * columns)

## Day 5 Print Queue

### Domain concepts
**PageOrderRule** represents the `X|Y` ordering rule
**Update** contains the page numbers to produce in an update

### Technical challenge
There is only a single input source providing different types when parsing. Modelling that with a sealed interface seems
reasonable. But when using that interface type in collections, type safety errors or problems will pop out of nowhere.

## Lessons learned
- clearly separate abstraction layers (example Update handles index related operations itself)

## Day 6 Guard Gullivant

### Domain Language and Concepts
- **Guard:** patrols a lab (back in year 1518)
- **Laboratory:**  aka **Room** the guard patrols
- **Patrol Protocol:** Rules of movement (strategy) - might be dependent on the current year?
- **Obstruction:** changes the direction of the guards movement  
- **Position:** Position of guard or obstacles, cartesian coordinates
- **Coordinates**: expressed as tupel `(x, y)`; direction from left to right (x) and top to bottom (y) aka screen coordinates
- **Direction:** cardinal direction, north, east, south, west; iteration order clockwise
- **Walk:** a path consisting of a sequence of legs, a guard takes
- **Leg:** a straight part of walk, having a length given in steps
- **Step:** a position change

### The problems to solve
Just reasoning ...

#### Guard Movement
- steps into a direction
  - number of steps into one direction might be limited 
- direction modeled as cardinal points gives us the opportunity to extend to diagonal moves too
- rule(s) how to handle an obstacle
  - might depend on the obstacles type
  - might depend on the year (back in time) too?
- leaving the room
  - not sure but it should happen when reaching labs border
- number of **distinct** locations visited
  - does the starting location count too?
  - recording the steps will do

## Laboratory
- rectangular, dimension?
- obstacles are quite sparse


## Lessons learned
- upfront thinking and bottom up design/implementation shines when the shit hits the fan
- unit test meticulous, don't get sloppy
- invest in readability