package javasudoku.solver;

/**
 * A wrapper class for the recursive algorithms implemented in SudokuSolver.
 * This class is used to maintain the number of solutions that have been found
 * by the solver algorithm.
 * 
 * This class is not accessible outside of the javasudoku.solver package.
 * @author Ishaiah Cross
 */

class SolutionCounter {
    private int numSolutions = 0;
    
    /**
     * Increments the number of solutions by 1
     */
    public void addSolution() {
        numSolutions++;
    }
    
    /**
     * Gets the number of solutions that have been assigned
     * @return The number of solutions that have been found
     */
    public int getNumSolutions() {
        return numSolutions;
    }
}

