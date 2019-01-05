package cc.lzsou.lschat.views.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cc.lzsou.lschat.R;

public class LsLetterListView extends View {
    private String[] letterList = { "↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    // 是否被手势选中的变量
    private boolean is_select = false;
    private Paint paint = new Paint();
    // 被选中的字母
    private int choosed = -1;
    private onTouchLetterListListener listener;

    public LsLetterListView(Context context) {
        super(context);
    }

    public LsLetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LsLetterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!is_select) {// 如果没有被选中，那么设置背景为全透明
            canvas.drawColor(getResources().getColor(R.color.transparent));
            // 取消字幕提示框
            listener.selectedUp();
        } else {
            canvas.drawColor(getResources().getColor(R.color.gray_black));
        }
        // View的宽高
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        // 单个字母的宽高
        int sigleHeight = height / letterList.length;
        int sigleWidth = width;
        paint.setColor(getResources().getColor(R.color.black));// 设置颜色
        paint.setTypeface(Typeface.DEFAULT);// 设置字体
        paint.setTextSize(23);
        paint.setAntiAlias(true);// 抗锯齿
        for (int i = 0; i < letterList.length; i++) {
            float xPos = sigleWidth / 2 - paint.measureText(letterList[i]) / 2;// X轴距
            float yPos = sigleHeight * i + sigleHeight; // Y轴距
            canvas.drawText(letterList[i], xPos, yPos, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 触摸点的y坐标
        float y = event.getY();
        // 当前被选中的字母
        int newChoosed = (int) (y / getHeight() * letterList.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 触摸按下的时候
                if (newChoosed != choosed && listener != null && newChoosed >= 0
                        && newChoosed < letterList.length) {
                    is_select = true;
                    choosed = newChoosed;
                    listener.selectedDown(letterList[newChoosed]);
                    invalidate();// 刷新UI
                }
                break;
            case MotionEvent.ACTION_MOVE:// 触摸滑动的时候
                if (newChoosed != choosed && listener != null && newChoosed >= 0
                        && newChoosed < letterList.length) {
                    listener.selectedDown(letterList[newChoosed]);
                    choosed = newChoosed;
                    invalidate();// 刷新UI
                }
                break;
            case MotionEvent.ACTION_UP:// 触摸结束的时候
                if (listener != null) {
                    is_select = false;
                    listener.selectedUp();
                    invalidate();// 刷新UI
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setListener(onTouchLetterListListener l) {
        listener = l;
    }

    /**
     * 回调接口，在Activity中显示字母框
     *
     * @ClassName: onTouchABCListListener
     */
    public interface onTouchLetterListListener {
        void selectedDown(String letter);// 选中字母

        void selectedUp();// 结束选中
    }

    /**
     * 隐藏字母提示框
     *
     * @version V1.0
     * @return void
     */
    public void setSelectedUp() {
        is_select = false;
        listener.selectedUp();
    }
}
