package utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CartesianDigits extends AbstractSpliterator<Long> {  
	static final int NUM_DIMENSIONS = 3;
	static final int TOP_DIGIT = 9;
    final ArrayDeque<Long> partialResults = new ArrayDeque<>(NUM_DIMENSIONS);
    final int[] onesDigits = new int[NUM_DIMENSIONS];

    protected CartesianDigits() {
        super(Long.MAX_VALUE, 0);
        partialResults.push(0L);
        Arrays.fill(onesDigits, 1);
    }

    public static Stream<Long> stream(){ return StreamSupport.stream(new CartesianDigits(), false); }

    @Override
    public boolean tryAdvance(Consumer<? super Long> action) {
        if (partialResults.isEmpty()) {
            return false;
        } else {
            // "Recurse" to the bottom from wherever we happen to be
            //
            while (partialResults.size() <= NUM_DIMENSIONS) {
                int dim = partialResults.size()-1;

                // Compute the partial result the same way we did in the
                // recursive implementation
                //
                partialResults.push(partialResults.peek() * 10 + onesDigits[dim]);
            }

            // Top of the stack now has the full result.
            //
            action.accept(partialResults.pop());

            // Advance the counters, starting with the least significant, popping
            // obsolete partialValues for any exhausted dimensions we encounter.
            // This corresponds to the increment and exit test from the
            // recursive version's for loop
            //
            for (int dim = NUM_DIMENSIONS-1; dim >= 0; dim--) {
                if (++onesDigits[dim] <= TOP_DIGIT) {
                    break;
                } else {
                    // This is where the recursive implementation returns.
                    // We also re-initialize the "frame" we just left so it's
                    // ready to use next time we need it
                    //
                    partialResults.pop();
                    onesDigits[dim] = 1;
                }
            }

            return true;
        }
    }
}
