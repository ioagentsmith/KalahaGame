package za.co.ioagentsmith.game.kalaha.model;

import za.co.ioagentsmith.game.kalaha.util.BoardEnum;

import java.util.stream.IntStream;

public class Board {

    private Pit[] pits;

    public Board() {
        this.pits = new Pit[BoardEnum.TOTAL_PITS.getValue()];

        IntStream.range(0, BoardEnum.TOTAL_PITS.getValue()).forEach(i -> {
            Pit pit = new Pit();
            pits[i] = pit;
            pits[i].setId(i);
            if (((i + 1) % BoardEnum.PITS_PER_PLAYER.getValue()) == 0) {
                pits[i].setKalaha(true);
            } else {
                pits[i].setKalaha(false);
            }
            if (!pits[i].isKalaha()) {
                pits[i].setNumberOfSeeds(BoardEnum.SEEDS_PER_PIT.getValue());
            } else {
                pits[i].setNumberOfSeeds(0);
            }
        });
    }

    public Pit[] getPits() {
        return pits;
    }

    public void setPits(final Pit[] pits) {
        this.pits = pits;
    }

    public Pit getPitById(int pitId) {
        return this.getPits()[pitId];
    }
}
