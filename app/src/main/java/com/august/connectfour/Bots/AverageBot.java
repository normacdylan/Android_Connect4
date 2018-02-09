package com.august.connectfour.Bots;

import com.august.connectfour.Analyzer;

/**
 * Created by mrx on 2017-12-15.
 */

public class AverageBot extends Bot {
    public AverageBot(Analyzer analyzer, int id) {
        super(analyzer, id);
    }

    @Override
    public int makeMove() {
        if (analyzer.bestArrayChance(id)[1]>=analyzer.bestArrayChance(getOpId())[1])
            return analyzer.bestArrayChance(id)[0];
        if (analyzer.bestArrayChance(getOpId())[1]<3 && !analyzer.openChance(getOpId(), analyzer.bestArrayChance(getOpId())[0]))
            return analyzer.bestArrayChance(id)[0];
        return analyzer.bestArrayChance(getOpId())[0];
    }
}
