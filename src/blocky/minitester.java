package blocky;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Part1Test {   // =======  12 points =======
    @Test
    @Tag("score:1")
    @DisplayName("Block constructor test1")
    void BlockConstructorTest1() throws NoSuchFieldException, IllegalAccessException {
        Block b = new Block(0, 1);
        b.updateSizeAndPosition(2, 0, 0);

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        assertNull(colorField.get(b));
        assertInstanceOf(Block[].class, childrenField.get(b));

        Block[] children = (Block[]) childrenField.get(b);

        assertEquals(4, children.length);

        for (Block child : children) {
            assertInstanceOf(Color.class, colorField.get(child));
            assertInstanceOf(Block[].class, childrenField.get(child));
            assertArrayEquals(new Block[0], (Block[]) childrenField.get(child));
        }
    }

    @Test
    @Tag("score:3")
    @DisplayName("Block constructor test2")
    void BlockConstructorTest2() throws NoSuchFieldException, IllegalAccessException {
        Block b = new Block(0, 2);
        b.printBlock();
        b.updateSizeAndPosition(4, 0, 0);

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        assertNull(colorField.get(b));
        assertInstanceOf(Block[].class, childrenField.get(b));

        Block[] children = (Block[]) childrenField.get(b);

        assertEquals(4, children.length);

        for (Block child : children) {
            if (colorField.get(child) == null) {
                Object grandChildren = childrenField.get(child);
                assertInstanceOf(Block[].class, grandChildren);
                Block[] grandChildrenArray = (Block[]) grandChildren;
                assertEquals(4, grandChildrenArray.length);

                for (Block grandChild : grandChildrenArray) {
                    assertInstanceOf(Color.class, colorField.get(grandChild));
                    assertInstanceOf(Block[].class, childrenField.get(grandChild));
                    assertArrayEquals(new Block[0], (Block[]) childrenField.get(grandChild));
                }
            } else {
                assertInstanceOf(Color.class, colorField.get(child));
                assertInstanceOf(Block[].class, childrenField.get(child));
                assertArrayEquals(new Block[0], (Block[]) childrenField.get(child));
            }
        }
    }

    @Test
    @Tag("score:3")
    @DisplayName("Block updateSizeAndPosition() test1")
    void UpdateSizeAndPositionTest1() throws NoSuchFieldException, IllegalAccessException {
        Block[] children = new Block[]{
                new Block(0, 0, 0, 1, 2, GameColors.YELLOW, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.RED, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.GREEN, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.RED, new Block[0])
        };

        Block b = new Block(0, 0, 0, 0, 2, null, children);
        b.updateSizeAndPosition(16, 0, 0);

        Field childrenField = Block.class.getDeclaredField("children");
        Field sizeField = Block.class.getDeclaredField("size");
        Field xcoordField = Block.class.getDeclaredField("xCoord");
        Field ycoordField = Block.class.getDeclaredField("yCoord");

        childrenField.setAccessible(true);
        sizeField.setAccessible(true);
        xcoordField.setAccessible(true);
        ycoordField.setAccessible(true);

        assertEquals(16, (int) sizeField.get(b));
        assertEquals(0, (int) xcoordField.get(b));
        assertEquals(0, (int) ycoordField.get(b));


        ArrayList<Integer> actualSize = new ArrayList<>();
        ArrayList<Integer> Coords = new ArrayList<>();

        for (Block child : (Block[]) childrenField.get(b)) {
            actualSize.add((int) sizeField.get(child));
            Coords.add((int) xcoordField.get(child));
            Coords.add((int) ycoordField.get(child));
        }

        List<Integer> expectedSize = List.of(8, 8, 8, 8);
        List<Integer> expectedCoords = List.of(8, 0, 0, 0, 0, 8, 8, 8);  // UL x, UL y, UR x, UR y, LL x, LL y, LR x, LR y

        assertEquals(expectedSize, actualSize);
        assertEquals(expectedCoords, Coords);
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block updateSizeAndPosition() test2")
    void UpdateSizeAndPositionTest2() {
        Block b = new Block(0, 0, 0, 0, 2, null, new Block[0]);

        assertThrows(IllegalArgumentException.class, () -> b.updateSizeAndPosition(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> b.updateSizeAndPosition(3, 0, 0));
    }

    @Test
    @Tag("score:2")
    @DisplayName("Block getBlocksToDraw() test1")
    void GetBlocksToDrawTest1() throws NoSuchFieldException, IllegalAccessException {
        Block b = new Block(0, 2);
        b.updateSizeAndPosition(16, 0, 0);

        ArrayList<BlockToDraw> blocksToDraw = b.getBlocksToDraw();

        assertEquals(Part3Test.getNumberOfBlocks(b) * 2, blocksToDraw.size());

        int frameCount = 0;
        int blockCount = 0;

        for (BlockToDraw btd : blocksToDraw) {
            if (btd.getColor() == GameColors.FRAME_COLOR) {
                frameCount++;
            } else if (btd.getStrokeThickness() == 0) {
                blockCount++;
            }
        }

        assertEquals(blockCount, frameCount);
    }

    @Test
    @Tag("score:2")
    @DisplayName("Block getBlocksToDraw() test2")
    void GetBlocksToDrawTest2() {
        Block[] children = new Block[0];
        Block b = new Block(0, 0, 16, 0, 2, GameColors.YELLOW, children);

        ArrayList<BlockToDraw> blocksToDraw = b.getBlocksToDraw();
        assertEquals(2, blocksToDraw.size());

        for (BlockToDraw btd : blocksToDraw) {
            boolean frame = btd.getStrokeThickness() == 0 && btd.getColor() == GameColors.YELLOW;
            boolean block = btd.getStrokeThickness() == 3 && btd.getColor() == GameColors.FRAME_COLOR;
            assertTrue(frame || block);
        }
    }
}

class Part2Test {  // ========= 12 points =========
    @Test
    @Tag("score:2")
    @DisplayName("Block getSelectedBlock() test1")
    void getSelectedBlock1() {
        Block b = new Block(0, 0, 0, 0, 2, null, new Block[0]);

        assertThrows(IllegalArgumentException.class, () -> b.getSelectedBlock(2, 15, 4));
        assertThrows(IllegalArgumentException.class, () -> b.getSelectedBlock(15, 2, -1));
    }

    @Test
    @Tag("score:2")
    @DisplayName("Block getSelectedBlock() test2")
    void getSelectedBlock2() throws NoSuchFieldException, IllegalAccessException {
        Block[] children = new Block[]{
                new Block(1, 0, 1, 1, 1, GameColors.YELLOW, new Block[0]),
                new Block(0, 0, 1, 1, 1, GameColors.RED, new Block[0]),
                new Block(0, 1, 1, 1, 1, GameColors.GREEN, new Block[0]),
                new Block(1, 1, 1, 1, 1, GameColors.BLUE, new Block[0])
        };

        Block b = new Block(0, 0, 2, 0, 1, null, children);

        Field xCoordField = Block.class.getDeclaredField("xCoord");
        Field yCoordField = Block.class.getDeclaredField("yCoord");
        Field colorField = Block.class.getDeclaredField("color");

        xCoordField.setAccessible(true);
        yCoordField.setAccessible(true);
        colorField.setAccessible(true);

        Block res = b.getSelectedBlock(0, 0, 1);

        assertEquals(0, (int) xCoordField.get(res));
        assertEquals(0, (int) yCoordField.get(res));
        assertEquals(GameColors.RED, colorField.get(res));
    }

    @Test
    @Tag("score:2")
    @DisplayName("Block getSelectedBlock() test3")
    void getSelectedBlock3() {
        Block b = new Block(0, 3);
        b.updateSizeAndPosition(16, 0, 0);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                final int x = i;
                final int y = j;
                assertDoesNotThrow(() -> b.getSelectedBlock(x, y, 2));
                Block res = b.getSelectedBlock(i, j, 2);

                assertNotNull(res);
                assertTrue(2 >= res.getLevel());
            }
        }
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block reflect() test1")
    void reflect1() {
        Block b = new Block(0, 0, 0, 0, 2, null, new Block[0]);

        assertThrows(IllegalArgumentException.class, () -> b.reflect(2));
        assertThrows(IllegalArgumentException.class, () -> b.reflect(-1));
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block reflect() test2")
    void reflect2() throws NoSuchFieldException, IllegalAccessException {
        Block[] children = new Block[]{
                new Block(1, 0, 1, 1, 1, GameColors.YELLOW, new Block[0]),  // UR
                new Block(0, 0, 1, 1, 1, GameColors.RED, new Block[0]),   // UL
                new Block(0, 1, 1, 1, 1, GameColors.GREEN, new Block[0]), // LL
                new Block(1, 1, 1, 1, 1, GameColors.BLUE, new Block[0])  // LR
        };

        Block b = new Block(0, 0, 2, 0, 1, null, children);

        b.reflect(0);  // reflect horizontally

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        Block[] childrenLevel1 = (Block[]) childrenField.get(b);

        List<Color> expected = List.of(GameColors.BLUE, GameColors.GREEN, GameColors.RED, GameColors.YELLOW);
        List<Color> actual = new ArrayList<>();

        for (Block child : childrenLevel1) {
            actual.add((Color) colorField.get(child));
        }

        assertEquals(expected, actual);

    }

    @Test
    @Tag("score:1")
    @DisplayName("Block rotate() test1")
    void rotate1() {
        Block b = new Block();
        assertThrows(IllegalArgumentException.class, () -> b.rotate(2));
        assertThrows(IllegalArgumentException.class, () -> b.rotate(-1));
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block rotate() test2")
    void rotate2() throws NoSuchFieldException, IllegalAccessException {
        Block[] children = new Block[]{
                new Block(1, 0, 1, 1, 1, GameColors.GREEN, new Block[0]),  // UR
                new Block(0, 0, 1, 1, 1, GameColors.BLUE, new Block[0]),   // UL
                new Block(0, 1, 1, 1, 1, GameColors.RED, new Block[0]), // LL
                new Block(1, 1, 1, 1, 1, GameColors.BLUE, new Block[0])  // LR
        };

        Block b = new Block(0, 0, 2, 0, 1, null, children);

        b.rotate(1); // rotate counter-clockwise

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        Block[] childrenLevel1 = (Block[]) childrenField.get(b);

        List<Color> expected = List.of(GameColors.BLUE, GameColors.RED, GameColors.BLUE, GameColors.GREEN);

        List<Color> actual = new ArrayList<>();
        for (Block child : childrenLevel1) {
            actual.add((Color) colorField.get(child));
        }

        assertEquals(expected, actual);

    }

    @Test
    @Tag("score:1")
    @DisplayName("Block smash() test1")
    void smash1() {
        assertFalse(new Block().smash());

        Block[] children = new Block[]{
                new Block(1, 0, 1, 1, 1, GameColors.YELLOW, new Block[0]),  // UR
                new Block(0, 0, 1, 1, 1, GameColors.BLUE, new Block[0]),   // UL
                new Block(0, 1, 1, 1, 1, GameColors.GREEN, new Block[0]), // LL
                new Block(1, 1, 1, 1, 1, GameColors.BLUE, new Block[0])  // LR
        };

        Block b = new Block(0, 0, 2, 1, 2, null, children);

        assertTrue(b.smash());
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block smash() test2")
    void smash2() throws NoSuchFieldException, IllegalAccessException {
        Block b = new Block(1, 2);
        b.updateSizeAndPosition(4, 0, 0);

        b.smash();

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        Object children = childrenField.get(b);
        assertInstanceOf(Block[].class, children);
        Block[] childrenArray = (Block[]) children;
        assertEquals(4, childrenArray.length);

        for (int i = 0; i < 4; i++) {
            assertInstanceOf(Block.class, childrenArray[i]);
        }
    }
}

class Part3Test {  // ======== 16 points ========

    @Test // same as the one in the pdf
    @Tag("score:2")
    @DisplayName("Block flatten() test1")
    void Blockflatten1() throws NoSuchFieldException, IllegalAccessException {
        Block b = new Block(0, 2);
        b.updateSizeAndPosition(4, 0, 0);

        Color[][] c = b.flatten();

        assertEquals(4, c.length);

        for (int i = 0; i < 4; i++) {
            assertEquals(4, c[i].length);
        }

        Field childrenField = Block.class.getDeclaredField("children");
        Field colorField = Block.class.getDeclaredField("color");
        childrenField.setAccessible(true);
        colorField.setAccessible(true);

        Block[] children = (Block[]) childrenField.get(b);

        Object URColor = colorField.get(children[0]);
        if (URColor == null) {
            Block[] URChildren = (Block[]) childrenField.get(children[0]);
            assertEquals(colorField.get(URChildren[0]), c[0][3]);
            assertEquals(colorField.get(URChildren[1]), c[0][2]);
            assertEquals(colorField.get(URChildren[2]), c[1][2]);
            assertEquals(colorField.get(URChildren[3]), c[1][3]);
        } else {
            assertEquals(URColor, c[0][3]);
            assertEquals(URColor, c[0][2]);
            assertEquals(URColor, c[1][2]);
            assertEquals(URColor, c[1][3]);
        }

        Object ULColor = colorField.get(children[1]);
        if (ULColor == null) {
            Block[] ULChildren = (Block[]) childrenField.get(children[1]);
            assertEquals(colorField.get(ULChildren[0]), c[0][1]);
            assertEquals(colorField.get(ULChildren[1]), c[0][0]);
            assertEquals(colorField.get(ULChildren[2]), c[1][0]);
            assertEquals(colorField.get(ULChildren[3]), c[1][1]);
        } else {
            assertEquals(ULColor, c[0][1]);
            assertEquals(ULColor, c[0][0]);
            assertEquals(ULColor, c[1][0]);
            assertEquals(ULColor, c[1][1]);
        }

        Object LLColor = colorField.get(children[2]);
        if (LLColor == null) {
            Block[] LLChildren = (Block[]) childrenField.get(children[2]);
            assertEquals(colorField.get(LLChildren[0]), c[2][1]);
            assertEquals(colorField.get(LLChildren[1]), c[2][0]);
            assertEquals(colorField.get(LLChildren[2]), c[3][0]);
            assertEquals(colorField.get(LLChildren[3]), c[3][1]);
        } else {
            assertEquals(LLColor, c[2][1]);
            assertEquals(LLColor, c[2][0]);
            assertEquals(LLColor, c[3][0]);
            assertEquals(LLColor, c[3][1]);
        }

        Object LRColor = colorField.get(children[3]);
        if (LRColor == null) {
            Block[] LRChildren = (Block[]) childrenField.get(children[3]);
            assertEquals(colorField.get(LRChildren[0]), c[2][3]);
            assertEquals(colorField.get(LRChildren[1]), c[2][2]);
            assertEquals(colorField.get(LRChildren[2]), c[3][2]);
            assertEquals(colorField.get(LRChildren[3]), c[3][3]);
        } else {
            assertEquals(LRColor, c[2][3]);
            assertEquals(LRColor, c[2][2]);
            assertEquals(LRColor, c[3][2]);
            assertEquals(LRColor, c[3][3]);
        }
    }

    @Test
    @Tag("score:1")
    @DisplayName("Block flatten() test2")
    void Blockflatten2() {
        Block[] children = new Block[4];
        children[0] = new Block(0, 0, 8, 1, 1, GameColors.BLUE, new Block[0]);
        children[1] = new Block(0, 0, 8, 1, 1, GameColors.YELLOW, new Block[0]);
        children[2] = new Block(0, 0, 8, 1, 1, GameColors.RED, new Block[0]);
        children[3] = new Block(0, 0, 8, 1, 1, GameColors.GREEN, new Block[0]);

        Block b = new Block(0, 0, 16, 0, 1, null, children);

        Color[][] c = b.flatten();

        Color[][] expected = new Color[][]{
                {GameColors.YELLOW, GameColors.BLUE},
                {GameColors.RED, GameColors.GREEN}
        };

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(expected[i][j], c[i][j]);
            }
        }
    }

    @Test
    @Tag("score:3")
    @DisplayName("Block flatten() test3")
    void Blockflatten3() {
        Block[] children = new Block[4];
        Block[] llChildren = new Block[4];

        llChildren[0] = new Block(0, 0, 4, 2, 2, GameColors.RED, new Block[0]);
        llChildren[1] = new Block(0, 0, 4, 2, 2, GameColors.GREEN, new Block[0]);
        llChildren[2] = new Block(0, 0, 4, 2, 2, GameColors.GREEN, new Block[0]);
        llChildren[3] = new Block(0, 0, 4, 2, 2, GameColors.YELLOW, new Block[0]);

        children[0] = new Block(0, 0, 8, 1, 2, GameColors.RED, new Block[0]);
        children[1] = new Block(0, 0, 8, 1, 2, GameColors.BLUE, new Block[0]);
        children[2] = new Block(0, 0, 8, 1, 2, null, llChildren);
        children[3] = new Block(0, 0, 8, 1, 2, GameColors.YELLOW, new Block[0]);

        Block b = new Block(0, 0, 16, 0, 2, null, children);

        Color[][] c = b.flatten();

        Color[][] expected = new Color[][]{
                {GameColors.BLUE, GameColors.BLUE, GameColors.RED, GameColors.RED},
                {GameColors.BLUE, GameColors.BLUE, GameColors.RED, GameColors.RED},
                {GameColors.GREEN, GameColors.RED, GameColors.YELLOW, GameColors.YELLOW},
                {GameColors.GREEN, GameColors.YELLOW, GameColors.YELLOW, GameColors.YELLOW}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(expected[i][j], c[i][j]);
            }
        }
    }


    @Test
    @Tag("score:1")
    @DisplayName("PerimeterGoal score() test1")
    void PGscore1() {
        Block[] children = new Block[]{
                new Block(0, 0, 0, 1, 2, GameColors.GREEN, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.BLUE, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.RED, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.YELLOW, new Block[0])
        };

        Block b = new Block(0, 0, 16, 0, 2, null, children);
        b.updateSizeAndPosition(16, 0, 0);

        PerimeterGoal p = new PerimeterGoal(GameColors.RED);

        assertEquals(4, p.score(b));
    }

    @Test
    @Tag("score:1")
    @DisplayName("PerimeterGoal score() test2")
    void PGscore2() {
        Block b = new Block(0, 2);
        b.updateSizeAndPosition(16, 0, 0);

        int red = new PerimeterGoal(GameColors.RED).score(b);
        int yellow = new PerimeterGoal(GameColors.YELLOW).score(b);
        int blue = new PerimeterGoal(GameColors.BLUE).score(b);
        int green = new PerimeterGoal(GameColors.GREEN).score(b);

        assertEquals(16, red + yellow + blue + green);
    }

    @Test
    @Tag("score:2")
    @DisplayName("BlobGoal undiscoveredBlobSize() test1")
    void BGBlobSize1() {
        BlobGoal g = new BlobGoal(GameColors.BLUE);

        Color[][] c = new Color[][]{
                {GameColors.YELLOW, GameColors.BLUE},
                {GameColors.RED, GameColors.RED}
        };

        assertEquals(0, g.undiscoveredBlobSize(0, 0, c, new boolean[2][2]));
    }

    @Test
    @Tag("score:2")
    @DisplayName("BlobGoal undiscoveredBlobSize() test2")
    void BGBlobSize2() {
        BlobGoal g = new BlobGoal(GameColors.RED);

        Color[][] c = new Color[][]{
                {GameColors.BLUE, GameColors.RED, GameColors.GREEN},
                {GameColors.RED, GameColors.YELLOW, GameColors.RED},
                {GameColors.RED, GameColors.YELLOW, GameColors.GREEN},
                {GameColors.RED, GameColors.RED, GameColors.YELLOW}
        };

        assertEquals(1, g.undiscoveredBlobSize(0, 1, c, new boolean[4][3]));
    }

    @Test
    @Tag("score:2")
    @DisplayName("BlobGoal undiscoveredBlobSize() test3")
    void BGBlobSize3() {
        Block[] urChildren = new Block[]{
                new Block(6, 0, 2, 2, 2, GameColors.GREEN, new Block[0]),
                new Block(4, 0, 2, 2, 2, GameColors.BLUE, new Block[0]),
                new Block(4, 2, 2, 2, 2, GameColors.RED, new Block[0]),
                new Block(6, 2, 2, 2, 2, GameColors.YELLOW, new Block[0])
        };

        Block[] children = new Block[]{
                new Block(4, 0, 4, 1, 2, null, urChildren),
                new Block(0, 0, 4, 1, 2, GameColors.BLUE, new Block[0]),
                new Block(0, 4, 4, 1, 2, GameColors.RED, new Block[0]),
                new Block(4, 4, 4, 1, 2, GameColors.YELLOW, new Block[0])
        };

        Block b = new Block(0, 0, 8, 0, 2, null, children);

        BlobGoal red = new BlobGoal(GameColors.RED);
        BlobGoal blue = new BlobGoal(GameColors.BLUE);
        BlobGoal green = new BlobGoal(GameColors.GREEN);
        BlobGoal yellow = new BlobGoal(GameColors.YELLOW);

        assertEquals(4, red.undiscoveredBlobSize(2, 0, b.flatten(), new boolean[4][4]));
        assertEquals(5, blue.undiscoveredBlobSize(0, 0, b.flatten(), new boolean[4][4]));
        assertEquals(1, green.undiscoveredBlobSize(0, 3, b.flatten(), new boolean[4][4]));
        assertEquals(5, yellow.undiscoveredBlobSize(2, 2, b.flatten(), new boolean[4][4]));
    }

    @Test
    @Tag("score:1")
    @DisplayName("BlobGoal score() test1")
    void BGscore1() {
        Block[] urChildren = new Block[]{
                new Block(0, 0, 0, 2, 2, GameColors.GREEN, new Block[0]),
                new Block(0, 0, 0, 2, 2, GameColors.BLUE, new Block[0]),
                new Block(0, 0, 0, 2, 2, GameColors.RED, new Block[0]),
                new Block(0, 0, 0, 2, 2, GameColors.YELLOW, new Block[0])
        };

        Block[] children = new Block[]{
                new Block(0, 0, 0, 1, 2, null, urChildren),
                new Block(0, 0, 0, 1, 2, GameColors.BLUE, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.RED, new Block[0]),
                new Block(0, 0, 0, 1, 2, GameColors.YELLOW, new Block[0])
        };


        Block b = new Block(0, 0, 16, 0, 2, null, children);

        b.printBlock();
        BlobGoal g = new BlobGoal(GameColors.BLUE);
        assertEquals(5, g.score(b));
    }

    @Test
    @Tag("score:1")
    @DisplayName("BlobGoal score() test2")
    void BGscore2() {
        Block b = new Block(0, 3);
        b.updateSizeAndPosition(16, 0, 0);

        int red = new BlobGoal(GameColors.RED).score(b);
        int yellow = new BlobGoal(GameColors.YELLOW).score(b);
        int blue = new BlobGoal(GameColors.BLUE).score(b);
        int green = new BlobGoal(GameColors.GREEN).score(b);

        assertTrue(64 >= red + yellow + blue + green);
    }

    static int getNumberOfBlocks(Block b) throws NoSuchFieldException, IllegalAccessException {
        Field childrenField = Block.class.getDeclaredField("children");
        childrenField.setAccessible(true);

        Block[] children = (Block[]) childrenField.get(b);

        if (children.length == 0) {
            return 1;
        }

        int count = 0;

        for (Block child : children) {
            count += getNumberOfBlocks(child);
        }

        return count;
    }

    private static boolean[][] arrayClone(boolean[][] array) {
        boolean[][] clone = new boolean[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            clone[i] = array[i].clone();
        }

        return clone;
    }
}
