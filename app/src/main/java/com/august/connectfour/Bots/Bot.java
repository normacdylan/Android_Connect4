package com.august.connectfour.Bots;

import com.august.connectfour.Analyzer;

/**
 * Created by mrx on 2017-12-11.
 */

public abstract class Bot {
    public Analyzer analyzer;
    public int id;

    public Bot(Analyzer analyzer, int id) {
        this.analyzer = analyzer;
        this.id = id;
    }

    public int makeMove() {
        return 3;
    }

    public int getOpId() {return id==1? 2:1;}

    public int getId() {return id;}
}
