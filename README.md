# Advent of code puzzles

## Day One - Historian Hysteria
Lessons learned
- don't use regex to delimit input while reading. Failed on unknown reason. `Scanner` or `BufferedReader` are the way to go.
- reading JDK API docs doesn't hurt at all

## Day Two - Red-Nosed Reports
Lessons learned
- found three variants to implement a sliding window of size n on a Java stream
  - with `Spliterator` (the actual implementation with fixed window size 2 being the most efficient one)
  - with a custom collector (usage like the spliterator solution)
  - with a deque (nice implementation but usage not that intuitive)
- try to gain an understanding of stream ordering
- state is the enemy of coding in functional style