package hcmus.tetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: document your custom view class.
 */
public class BoardView extends View implements View.OnTouchListener {
    final Paint boardPaint = new Paint();
    final Paint borderPaint = new Paint();

    int rows, columns;
    float unit;
    int backgroundColor;

    Board board;
    Timer timer;
    Handler handler;

    GestureDetector gestureDetector;

    public BoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BoardView, defStyle, 0);

        rows = a.getInt(R.styleable.BoardView_rows, 20);
        columns = a.getInt(R.styleable.BoardView_columns, 10);
        backgroundColor = a.getColor(R.styleable.BoardView_backgroundColor, Color.BLACK);
        int maxSpeed = a.getInt(R.styleable.BoardView_maxSpeed, 1200);
        int minSpeed = a.getInt(R.styleable.BoardView_minSpeed, 100);

        board = new Board(rows, columns, minSpeed, maxSpeed);
        board.startGame();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage());
            }
        }, 1000, 100);

        handler = new Handler(getContext().getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                invalidate();
            }
        };

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.BLACK);

        this.setOnTouchListener(this);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent me1, MotionEvent me2, float vx, float vy) {
                float x1 = me1.getX(), y1 = me1.getY(), x2 = me2.getX(), y2 = me2.getY();
                if (Math.abs(x2 - x1) <= 100 && Math.abs(y2 - y1) > 50) {
                    if (y2 - y1 < 0)
                        board.rotate(1);
                    else
                        board.hardDrop();
                }
                return true;
            }
        });

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = getMeasuredWidth();
        unit = (float)width / columns;
        float newHeight = (float)rows / columns * width;
        setMeasuredDimension(width, (int)newHeight);
    }

    public void drawUnit(Canvas canvas, float x, float y, float pad, Paint paint) {
        x *= unit;
        y *= unit;
        float r = unit / 5; // Radius of the round edge
        canvas.drawRoundRect(y + pad, x + pad, y + unit - pad, x + unit - pad, r, r, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background/board
        boardPaint.setColor(backgroundColor);
        canvas.drawRect(0f, 0f, (float)getMeasuredWidth(), (float)getMeasuredHeight(), boardPaint);

        // Draw the current piece
        Piece piece = board.getCurrentPiece();
        boardPaint.setColor(ContextCompat.getColor(getContext(), piece.color));
        for (Coord coord : piece.getBlockPositions()) {
            drawUnit(canvas, coord.x, coord.y, 0, boardPaint);
            drawUnit(canvas, coord.x, coord.y, -1, borderPaint);
        }

        // Draw the pile
        int[][] pile = board.getPile();
        for (int i = 0; i < pile.length; ++i)
            for (int j = 0; j < pile[i].length; ++j)
                if (pile[i][j] != 0) {
                    boardPaint.setColor(ContextCompat.getColor(getContext(), pile[i][j]));
                    drawUnit(canvas, i, j, 0, boardPaint);
                    drawUnit(canvas, i, j, 2, borderPaint);
                }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (gestureDetector.onTouchEvent(motionEvent))
            return true;

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d("Touch", "Touch: " + motionEvent.getX() + "/" + view.getWidth());
            if (motionEvent.getX() < view.getWidth() / 2f)
                board.steer(-1);
            else
                board.steer(1);
            return true;
        }

        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void pause() {
        board.pause();
    }

    /**
     *
     * @return PieceType of holding piece
     */
    public PieceType hold() {
        return board.hold();
    }

    public void setOnNextPieceListener(Board.OnNextPieceListener listener) {
        board.nextPieceListener = listener;
    }

    public void setOnLineClearListener(Board.OnLineClearListener listener) {
        board.lineClearListener = listener;
    }
}