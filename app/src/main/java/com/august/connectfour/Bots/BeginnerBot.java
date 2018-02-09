package com.august.connectfour.Bots;

import com.august.connectfour.Analyzer;

/**
 * Created by mrx on 2017-12-13.
 */

public class BeginnerBot extends Bot {
    public BeginnerBot(Analyzer analyzer, int id) {
        super(analyzer, id);
    }

    @Override
    public int makeMove() {
        if (analyzer.bestChance(id)[1]>=analyzer.bestChance(getOpId())[1])
            return analyzer.bestChance(id)[0];
        return analyzer.bestChance(getOpId())[0];
    }


}
