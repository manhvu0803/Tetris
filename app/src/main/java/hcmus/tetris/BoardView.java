package hcmus.tetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: document your custom view class.
 */
public class BoardView extends View {
    final Paint boardPaint = new Paint();
    final Paint borderPaint = new Paint();

    int rows, columns;
    float unit;

    Board board;
    Timer timer;
    Handler handler;

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

        board = new Board(rows, columns);
        board.startGame();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage());
            }
        }, 1000, 1000);

        handler = new Handler(getContext().getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                invalidate();
            }
        };

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);
        borderPaint.setColor(Color.BLACK);

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
        canvas.drawRect(y + pad, x + pad, y + unit - pad, x + unit - pad, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background/board
        boardPaint.setColor(Color.LTGRAY);
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
    }
}