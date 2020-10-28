package javasudoku.solver;

/**
 * A wrapper class for the recursive algorithms implemented in SudokuSolver.
 * This allows pass-by-reference behaviour to maintain and pass the state of the algorithm
 * up the call stack if a solution is found.
 * 
 * This class is not accessible outside of the javasudoku.solver package.
 * @author Ishaiah Cross
 */
class SolvedStatus {
    public boolean hasSolved;
}
