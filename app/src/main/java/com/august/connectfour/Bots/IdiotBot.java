package com.august.connectfour.Bots;

import com.august.connectfour.Analyzer;

import java.util.Random;

/**
 * Created by mrx on 2017-12-11.
 */

public class IdiotBot extends Bot{

    private Random r;

    public IdiotBot(Analyzer analyzer, int id) {
        super(analyzer, id);
        r = new Random();
    }

    @Override
    public int makeMove() {
        return r.nextInt(7);
    }
}
