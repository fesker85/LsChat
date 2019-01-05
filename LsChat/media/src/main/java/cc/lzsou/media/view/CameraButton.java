package cc.lzsou.media.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CameraButton extends View implements View.OnTouchListener{

    private Paint paint;
    private Context context;
    public CameraButton(Context context) {
        super(context);
    }
    public CameraButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
        this.setOnTouchListener(this);
        this.context=context;
        this.paint= new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
    }
    //00B8FB
   // private int ringColor= Color.parseColor("#45C01A");
    private int ringColor= Color.parseColor("#00B8FB");
    private int backColor1=Color.parseColor("#f5f5f5");
    private int backColor2=Color.WHITE;
    private void setBackColor(int backColor1,int backColor2){
        this.backColor1=backColor1;
        this.backColor2=backColor2;
        invalidate();
    }
    private float progress=0;
    public void setProgress(float progress){
        this.progress=progress;
        if(this.progress>360)
            this.progress=360;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //大圆
        paint.setColor(backColor1);
        paint.setStrokeWidth(width/2);
        canvas.drawCircle( width/2, height/2, width/4, paint);

        //小圆
        paint.setColor(backColor2);
        paint.setStrokeWidth(width/4);
        canvas.drawCircle( width/2, height/2, (width/8), paint);

        //画环形
        float ringWidth = dip2px(context, 5);
        float ringLeft = ringWidth/2f;
        float ringTop = ringWidth/2f;
        float ringRight = width-ringWidth/2f;
        float ringBottom = height-ringWidth/2f;
        this.paint.setColor(ringColor);
        this.paint.setStrokeWidth(ringWidth);
        RectF rf = new RectF(ringLeft,ringTop,ringRight,ringBottom);
        canvas.drawArc(rf,-90,progress,false,this.paint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int action;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.action=event.getAction();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackColor(Color.parseColor("#ffffff"),Color.parseColor("#f5f5f5"));
                islongclick=false;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,300);
                break;
            case MotionEvent.ACTION_UP:
                setBackColor(Color.parseColor("#f5f5f5"),Color.parseColor("#ffffff"));
                setProgress(0);
                timeHandler.removeCallbacks(timeRunnable);
                if(islongclick){
                    islongclick=false;
                    if(clickListener!=null)clickListener.onTouchEnd();
                }
                break;
        }
        return false;
    }
    private boolean islongclick=false;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(action==MotionEvent.ACTION_UP&&!islongclick){
                if(clickListener!=null)clickListener.onClick();
                return;
            }
            islongclick=true;
            timeHandler.postDelayed(timeRunnable,100);
            if(clickListener!=null)clickListener.onTouchStart();

        }
    };

    private int time=0;
    private Handler timeHandler = new Handler();
    private Runnable timeRunnable=new Runnable() {
        @Override
        public void run() {
            if(time>100){
                timeHandler.removeCallbacks(timeRunnable);
                time=0;
                return;
            }
            time+=1;
            setProgress(time*3.6f);
            timeHandler.postDelayed(timeRunnable,100);
        }
    };
    private OnClickListener clickListener;
    public void addOnClickListener(OnClickListener onClickListener){
        this.clickListener=onClickListener;
    }

    public interface OnClickListener{
        void onClick();
        void onTouchStart();
        void onTouchEnd();
    }

}
