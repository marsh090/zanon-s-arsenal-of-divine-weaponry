package com.zanon.zanons_arsenal.world.structure;

import net.minecraft.util.RandomSource;

/**
 * 5 by 5 rooms. A fixed path runs from the west entrance to the center vault, with a few empty side rooms.
 */
final class VaultLayout {
    static final int GRID = 5;
    static final int ROOM = 9;
    static final int STRIDE = 8;
    static final int CORRIDOR = 7;
    static final int HEIGHT = 6;
    static final int WIDTH = CORRIDOR + (GRID - 1) * STRIDE + ROOM;
    static final int DEPTH = (GRID - 1) * STRIDE + ROOM;

    final boolean[][] open = new boolean[GRID][GRID];
    final boolean[][] linkEast = new boolean[GRID][GRID];
    final boolean[][] linkSouth = new boolean[GRID][GRID];
    final Cell[] path;

    private VaultLayout(Cell[] path) {
        this.path = path;
    }

    static VaultLayout create(int seed) {
        Cell[] path = {
                new Cell(0, 2),
                new Cell(0, 1),
                new Cell(0, 0),
                new Cell(1, 0),
                new Cell(2, 0),
                new Cell(2, 1),
                new Cell(2, 2)
        };
        VaultLayout layout = new VaultLayout(path);
        for (int i = 0; i < path.length; i++) {
            layout.open[path[i].x][path[i].z] = true;
            if (i + 1 < path.length) {
                layout.link(path[i], path[i + 1]);
            }
        }
        RandomSource random = RandomSource.create(seed);
        Cell[] branches = {new Cell(1, 1), new Cell(1, 2), new Cell(3, 0), new Cell(3, 1), new Cell(2, 3), new Cell(4, 2)};
        int added = 0;
        for (Cell branch : branches) {
            if (added >= 3 || !random.nextBoolean()) {
                continue;
            }
            Cell parent = layout.adjacentOpen(branch);
            if (parent == null) {
                continue;
            }
            layout.open[branch.x][branch.z] = true;
            layout.link(parent, branch);
            added++;
        }
        return layout;
    }

    int roomX(int cellX) {
        return CORRIDOR + cellX * STRIDE;
    }

    int roomZ(int cellZ) {
        return cellZ * STRIDE;
    }

    boolean onPath(int x, int z) {
        for (Cell cell : this.path) {
            if (cell.x == x && cell.z == z) {
                return true;
            }
        }
        return false;
    }

    int pathIndex(int x, int z) {
        for (int i = 0; i < this.path.length; i++) {
            if (this.path[i].x == x && this.path[i].z == z) {
                return i;
            }
        }
        return -1;
    }

    private void link(Cell a, Cell b) {
        if (a.x == b.x && Math.abs(a.z - b.z) == 1) {
            int z = Math.min(a.z, b.z);
            this.linkSouth[a.x][z] = true;
        } else if (a.z == b.z && Math.abs(a.x - b.x) == 1) {
            int x = Math.min(a.x, b.x);
            this.linkEast[x][a.z] = true;
        }
    }

    private Cell adjacentOpen(Cell cell) {
        if (cell.x > 0 && this.open[cell.x - 1][cell.z]) {
            return new Cell(cell.x - 1, cell.z);
        }
        if (cell.x + 1 < GRID && this.open[cell.x + 1][cell.z]) {
            return new Cell(cell.x + 1, cell.z);
        }
        if (cell.z > 0 && this.open[cell.x][cell.z - 1]) {
            return new Cell(cell.x, cell.z - 1);
        }
        if (cell.z + 1 < GRID && this.open[cell.x][cell.z + 1]) {
            return new Cell(cell.x, cell.z + 1);
        }
        return null;
    }

    record Cell(int x, int z) {
    }
}
