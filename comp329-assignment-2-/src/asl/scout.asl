/* Initial goal */

!move(slots). 

/* Plans */

// Step through the grid world and then stop
// To achieve the goal !check(slots): if the robot isn't at the end of the
// world, move to the next slot, then reset the goal !check(slots)
+!move(slots) : not pos(r1,6,6)
   <- next(slot);
      !move(slots).
// Achieve the goal !check(slots) without doing anything.
+!move(slots).

// The second clause for +!check(slots) could be replaced by:
 +!move(slots) : pos(r1,6,6)
   <- .print("Done!").