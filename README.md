# Advent of code puzzles


## Day One

### Domain concepts/language
- Historians
  - Chief Historian
  - Elvish Senior Historians
- Historically Significant Locations aka (significant to the north pole)
- Chief Historian must be in one of the first 50 places of the historically significant locations
- The historical significant locations is a collection of places
- the places can pre represented by a list of location ID's
- The historical significant locations has an initial ordering of places
- 
### The puzzle

- Lists containing a `locationID`
- Input: two lists of locationID's as non negativ integers (range assumed)
- compare the n-th smallest numbers in both lists
  - result of the comparison is the absolute amount of the difference of elements indexes
- compute total distance by adding up the comparison results
