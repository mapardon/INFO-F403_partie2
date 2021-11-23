public class initActionTable {

    public int[][][] getActionTable(){

        int[][] actionTable = new int[][] {{0,0,1,1,0},{1,1,1,0,1}, {0,0,0,1,0}};
        int[][] matchTable = new int[][]{{1,0,0,0}, {0,2,0,0},{0,0,2,0},{0,0,0,2}}; // 1 = match, 2 = accept
        return new int[][][]{actionTable, matchTable};

    }
}
