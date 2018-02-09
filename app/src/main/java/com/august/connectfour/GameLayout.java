package com.august.connectfour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mrx on 2017-12-04.
 */

public class GameLayout extends SurfaceView implements Runnable {

    private  Thread thread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    volatile boolean playing;
    private float height, width, dropGoal;
    private Paint paint;
    private GameEngine engine;
    private Analyzer analyzer;
    private BoardGraphic board;
    private CoinGraphic redCoin, yellowCoin, fallingCoin;
    private int turn, currentColumn;
    private boolean dropping;

    public GameLayout(Context context) {
        super(context);
        thread = null;
        setWillNotDraw(false);
        canvas = new Canvas();
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setAntiAlias(true);
        engine = new GameEngine(6, 7);
        analyzer = new Analyzer(engine);
        board = new BoardGraphic(0.1f, 0.15f, 0.9f, 1, engine.getRows(), engine.getColumns());
        redCoin = new CoinGraphic(0.05f, 0.075f, board.getYMargin()*0.75f, Color.RED);
        yellowCoin = new CoinGraphic(-1, 0.075f, board.getYMargin()*0.75f, Color.YELLOW);
        fallingCoin = new CoinGraphic(-1, 0.075f, board.getYMargin()*0.75f, Color.YELLOW);
        turn = 1;
     //   setBackgroundResource(R.drawable.back);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }
    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }
    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }
    public void control() {
        try {
            thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Log.i("best", ""+analyzer.bestArrayChance(2)[0]+" "+analyzer.bestArrayChance(2)[1]);
        if (dropping && y(fallingCoin.getY())<dropGoal)
            fallingCoin.fall(0.00001f);
        else if (dropping && y(fallingCoin.getY())>=dropGoal) {
            dropping = false;
            fallingCoin.updateX(-1);
            addCoin();
            nextTurn();
        }

        if (isGameOver())
            gameOver();
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            drawBoard(canvas);
            drawHoles(canvas);
            drawCoin(canvas, redCoin);
            drawCoin(canvas, yellowCoin);

            if (dropping)
                drawCoin(canvas, fallingCoin);

            if (isGameOver()) {
                drawEndText(canvas);
                drawWinningHoles(canvas);
            }


            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setDimensions(canvas);
    }

    private void drawHoles(Canvas canvas) {
        for (int i=0;i<engine.getRows();i++) {
            for (int j=0;j<engine.getColumns();j++) {
                paint.setColor(convertColor(engine.getBoard()[i][j]));
                canvas.drawCircle(x(board.getHolesX()[j]), y(board.getHolesY()[i]), y(board.getYMargin()*0.75f), paint);
            }
        }
    }
    private void drawBoard(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawRect(x(board.getLeft()), y(board.getTop()), x(board.getRight()), y(board.getBottom()), paint);
    }
    private void drawCoin(Canvas canvas, CoinGraphic coin) {
        paint.setColor(coin.getColor());
        canvas.drawCircle(x(coin.getX()), y(coin.getY()), y(coin.getRadius()), paint);
    }
    private void drawEndText(Canvas canvas) {
        paint.setTextSize(75);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);
        String message = analyzer.won(1)? "Red won!":analyzer.won(2)? "Yellow won!":"Draw";
        canvas.drawText(message, x(0.35f), y(0.1f), paint);
    }
    private void drawWinningHoles(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(13);
        canvas.drawLine(x(getWinningHolesX()[0]),
                        y(getWinningHolesY()[0]),
                        x(getWinningHolesX()[3]),
                        y(getWinningHolesY()[3]),
                        paint);
    }

    private float[] getWinningHolesX() {
        float[] result = new float[4];
        int startPos = analyzer.getWinningHoles()[1];

        if (analyzer.getWinningMode().equals("vertical")) {
            for (int i=0;i<4;i++)
                result[i] = board.getHolesX()[startPos];
        } else {
            for (int i=0;i<4;i++)
                result[i] = board.getHolesX()[startPos+i];
        }
        return result;
    }
    private float[] getWinningHolesY() {
        float[] result = new float[4];
        int startPos = analyzer.getWinningHoles()[0];

        if (analyzer.getWinningMode().equals("horizontal")) {
            for (int i=0;i<4;i++)
                result[i] = board.getHolesY()[startPos];
        } else if (analyzer.getWinningMode().equals("diagonalDown")) {
            for (int i=0;i<4;i++)
                result[i] = board.getHolesY()[startPos+i];
        } else {
            for (int i=0;i<4;i++)
                result[i] = board.getHolesY()[startPos-i];
        }
        return result;
    }

    private int convertColor(int player) {
        switch (player) {
            case 0:
                return Color.WHITE;
            case 1:
                return Color.RED;
            case 2:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    private void setCurrentColumn() {
        float missMargin = 0.025f;

        for (int i=0;i<board.getHolesX().length;i++) {
            if (Math.abs(getCurrentCoin().getX() - board.getHolesX()[i]) < missMargin) {
                currentColumn = i;
                break;
            } else
                currentColumn = -1;
        }
        if (getCurrentCoin().getX()<board.getLeft() || getCurrentCoin().getX()>board.getRight())
            currentColumn = -1;

        if (currentColumn>-1 && engine.getFree(currentColumn)<0)
            currentColumn = -1;
    }
    public int getCurrentColumn() {
        return currentColumn;
    }

    public void addCoin() {
        engine.addCoin(turn, currentColumn);
    }
    public void dropCoin() {
        setCurrentColumn();
        if (currentColumn>-1) {
            fallingCoin.copyCoin(getCurrentCoin());
            dropGoal = board.getTop()+board.getYMargin()+ engine.getFree(currentColumn)*board.getYMargin()*2;
            dropping = true;
        }
    }

    public void nextTurn() {
        if (turn==1) {
            redCoin.updateX(-1);
            yellowCoin.updateX(0.95f);
            turn = 2;
        } else {
            yellowCoin.updateX(-1);
            redCoin.updateX(0.05f);
            turn = 1;
        }
    }
    public boolean isGameOver() {
        return analyzer.won(1) || analyzer.won(2) || analyzer.isDraw();
    }
    private void gameOver() {
        redCoin.updateX(-1);
        yellowCoin.updateX(-1);
        fallingCoin.updateX(-1);
    }

    public void updateCoinX(float x) {
        getCurrentCoin().updateX(x/width);
    }
    public void updateCoinHole(int hole) {
       getCurrentCoin().updateX(board.getHolesX()[hole]);
    }
    private boolean touchedCoin(float x, float y, CoinGraphic coin) {
        return (Math.abs(x(coin.getX())-x)<x(coin.getRadius()) &&
                Math.abs(y(coin.getY())-y)<y(coin.getRadius()));
    }
    public boolean touchedCoin(float x, float y) {
        return touchedCoin(x, y, redCoin) || touchedCoin(x, y, yellowCoin);
    }

    public CoinGraphic getCurrentCoin() {
        return turn == 1? redCoin:yellowCoin;
    }

    private void setDimensions(Canvas canvas) {
        if (height==0) {
            height = canvas.getHeight();
            width = canvas.getWidth();
        }

    }
    private float x(float x) {
        return x*width;
    }
    private float y(float y) {
        return y*height;
    }

    public boolean isAboveBoard(float y) {
        return y<board.getTop()*height && y>0;
    }
    public boolean isLegalMove(int column) {
        return engine.getFree(column)>-1;
    }
    public int getAnyFree() {
        return engine.getAnyFreeColumn();
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
    public int getTurn() {return turn;}
}
