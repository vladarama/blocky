// Vlad Arama
package blocky;

import java.awt.Color;

public class BlobGoal extends Goal {

    public BlobGoal(Color c) {
        super(c);
    }

    @Override
    public int score(Block board) {
        Color[][] colorArr = board.flatten();
        boolean[][] visited = new boolean[colorArr.length][colorArr.length];

        int blobSize = 0;

        for (int i = 0; i < colorArr.length; i++) {
            for (int j = 0; j < colorArr.length; j++) {
                int tmpSize = undiscoveredBlobSize(i, j, colorArr, visited);
                if (tmpSize > blobSize) {
                    blobSize = tmpSize;
                }
            }
        }

        return blobSize;
    }

    @Override
    public String description() {
        return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
                + " blocks, anywhere within the block";
    }


    public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {

        if (i < 0 || j < 0 || i >= unitCells.length || j >= unitCells.length || visited[i][j] || unitCells[i][j] != targetGoal) {
            return 0;
        }

        visited[i][j] = true;

        int size = 1;

        size += undiscoveredBlobSize(i - 1, j, unitCells, visited);
        size += undiscoveredBlobSize(i + 1, j, unitCells, visited);

        size += undiscoveredBlobSize(i, j - 1, unitCells, visited);
        size += undiscoveredBlobSize(i, j + 1, unitCells, visited);


        return size;
    }

}
