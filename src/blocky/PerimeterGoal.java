// Vlad Arama
package blocky;

import java.awt.Color;

public class PerimeterGoal extends Goal {

    public PerimeterGoal(Color c) {
        super(c);
    }

    @Override
    public int score(Block board) {
        Color targetColor = this.targetGoal;

        Color[][] arr = board.flatten();
        int arrSize = arr.length;

        int points = 0;
        for (int i = 0; i < arrSize; i++) {
            for (int j = 0; j < arrSize; j++) {
                if (i == 0 || j == 0 || i == (arrSize - 1) || j == (arrSize - 1)) {
                    if (arr[i][j] == targetColor) {
                        points++;
                        if ((i == 0 && j == 0) || (i == 0 && j == arrSize - 1) || (i == arrSize - 1 && j == 0) || (i == arrSize - 1 && j == arrSize - 1)) {
                            points++;
                        }
                    }
                }
            }
        }
        return points;
    }

    @Override
    public String description() {
        return "Place the highest number of " + GameColors.colorToString(targetGoal)
                + " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
    }

}
