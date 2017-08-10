package com.example.yinshuai.thelabel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 尹帅 on 2017/8/7.
 *
 * 瀑布流的标签选择
 *
 * 作者:尹帅 1317972280@qq.com
 */
public class Lable extends ViewGroup {
    private final static int SCENEWIDTH = 1;
    private final static int SCENEHEIGHT = 2;
    private float textsize = 0;
    private int select_textcolor=0;
    private int no_textcolor=0;
    private int padding = 0;
    private int margin = 0;
    private boolean ismultiple=true;

    private int scenewidth;
    private boolean isbr = true;
    private boolean isclick=true;
    private Paint paint;
    private List<String> listcount = new ArrayList<>();
    private Context mContext;
    private List<LableItem> lableItems = new ArrayList<>();

    public OnItemSelectClickListener onItemSelectClick;
    public OnCancelSelectClickListener onCancelSelectClick;
    public OnCancelAllSelectListener onCancelAllSelectListener;

    public Lable(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public Lable(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.Lable);
        textsize=px2dip(typedArray.getDimensionPixelSize(R.styleable.Lable_lable_textSize,dip2px(16)));
        select_textcolor=typedArray.getColor(R.styleable.Lable_lable_opttextColor, Color.BLACK);
        no_textcolor=typedArray.getColor(R.styleable.Lable_lable_notextColor,Color.BLACK);
        padding=typedArray.getInteger(R.styleable.Lable_lable_padding,10);
        margin=typedArray.getInteger(R.styleable.Lable_lable_margin,8);
        ismultiple=typedArray.getBoolean(R.styleable.Lable_lable_ismultiple,true);
        isclick=typedArray.getBoolean(R.styleable.Lable_lable_isclick,true);
        init();
    }

    /**
     * 对外提供选中监听的接口
     */
    public interface OnItemSelectClickListener {
        void selectclick(String text, int position);
    }

    public void setOnItemSelectClickListener(OnItemSelectClickListener onItemClick) {
        this.onItemSelectClick = onItemClick;
    }

    /**
     * 对外提供取消item选中的接口
     */
    public interface OnCancelSelectClickListener {
        void cancelselectclick(String text, int position);
    }

    public void setOnCancelSelectClickListener(OnCancelSelectClickListener onCancelSelectClick) {
        this.onCancelSelectClick = onCancelSelectClick;
    }

    public interface OnCancelAllSelectListener{
        void cancelalllistener();
    }

    public void setOnCancelAllSelectListener(OnCancelAllSelectListener l){
         onCancelAllSelectListener=l;
    }

    /**
     * 初始化
     */
    public void initLableItem() {
        if (listcount == null || listcount.size() == 0)
            return;
        for (int i = 0; i < listcount.size(); i++) {
            LableItem lableItem = new LableItem(mContext);
            lableItem.setText(listcount.get(i));
            lableItem.setTag(i + 1);
            addView(lableItem);
        }
    }

    public void init() {
        paint = new Paint();
        paint.setTextSize(dip2px(textsize));
        scenewidth = getScene(SCENEWIDTH);
    }

    /**
     * 设置数据
     * @param data
     */
    public void setDataList(List<String> data) {
        this.listcount.addAll(data);
        initLableItem();
    }

    /**
     * 控件的宽高测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = getLableHeight(paint) + margin + padding;

        int widths = 0;

        if (listcount.size() == 0) {
            width = 0;
            height = 0;

        } else {
            for (int i = 0; i < listcount.size(); i++) {
                int itemWidth = getLableWidth(paint, listcount.get(i));
                if (widths + itemWidth > scenewidth) { //判断有没有换行
                    height += getLableHeight(paint) + margin + padding;
                    widths = margin;
                    isbr = false; //换行标识
                }
                if (isbr) {
                    width += itemWidth + margin;//如果没有换行 就按实际的宽度去测量
                } else {
                    width = widthSize; //只要换行了 就证明宽度是充满的
                }
                widths += itemWidth + margin;
            }
            height += margin;//在底部加上一个外边距
            if (isbr)
                width += margin;//这是为了在没有换行的情况下 最后在加上一个外边距
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 安排摆放位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        int width = margin;
        int height = margin;

        if (count == 0) {
            width = 0;
            height = 0;
        }

        lableItems.clear();

        // 获取标签高度
        int itemheight = getLableHeight(paint);
        for (int i = 0; i < count; i++) {
            LableItem lableItem = (LableItem) getChildAt(i);
            lableItems.add(lableItem);
            // 获取标签宽度
            int itemWidth = getLableWidth(paint, listcount.get(i))+margin;

            if (width + itemWidth > scenewidth) { //如果item的宽度加上下一个item的宽度大于屏幕宽度的话 那么这个时候就要换行了
                height += itemheight + margin + padding;
                width = margin;
            }
            lableItem.layout(width, height, width + itemWidth, height + itemheight + padding);
            width += itemWidth + margin;
        }
    }


    /**
     * 获取选中的内容字符串
     */
    public List<String> getSelectContent() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < lableItems.size(); i++) {
            if (lableItems.get(i).isSelect()) {
                list.add(lableItems.get(i).getText().toString());
            }
        }
        return list;
    }

    /**
     * 取消全部选中
     */
    public void clearSelect(){

       for (int i=0;i<getChildCount();i++){
           LableItem lableItem= (LableItem) getChildAt(i);
           if (lableItem.isSelect()){
               lableItem.setChecked(false);
               lableItem.setTextColor(no_textcolor);
               lableItem.setSelect(0);
               if (onCancelAllSelectListener!=null){
                   onCancelAllSelectListener.cancelalllistener();
               }
           }
       }
    }

    /**
     * 获取标签的高度
     *
     * @param paint
     * @return
     */
    public int getLableHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + padding;
    }

    /**
     * 获取标签的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public int getLableWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet + padding;
    }


    /**
     * 标签的Item
     */
    class LableItem extends RadioButton {

        private int select = 0;

        public LableItem(Context context) {
            super(context);
            initStyle();
            Click();
        }

        public LableItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            initStyle();
            Click();
        }

        /**
         * 设置标签item的样式 需要修改样式 直接修改drawable文件夹下select文件即可
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void initStyle() {
            Bitmap bitmap = null;
            setButtonDrawable(new BitmapDrawable(bitmap));
            setBackground(getResources().getDrawable(R.drawable.select));
            setTextSize(textsize);
            setTextColor(no_textcolor);
            setPadding(padding, padding, padding, padding);
            setGravity(Gravity.CENTER);

            if (!isclick){
                setEnabled(false);
            }
        }


        public void Click() {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ismultiple){
                        clearSelect();
                    }
                    if (select == 0) {
                        setChecked(true);
                        select = 1;
                        setTextColor(select_textcolor);
                        if (onItemSelectClick != null) {
                            onItemSelectClick.selectclick(getText().toString(), Integer.parseInt(getTag().toString()));
                        }
                    } else if (select == 1) {
                        setChecked(false);
                        select = 0;
                        setTextColor(no_textcolor);
                        if (onCancelSelectClick != null) {
                            onCancelSelectClick.cancelselectclick(getText().toString(), Integer.parseInt(getTag().toString()));
                        }
                    }
                }
            });
        }

        /**
         * 判断是否选中
         *
         * @return
         */
        public boolean isSelect() {
            return select == 0 ? false : true;
        }

        public void setSelect(int select){
            this.select=select;
        }

    }


    /**
     * 获取屏幕的高宽
     *
     * @param i
     * @return
     */
    public int getScene(int i) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (i == SCENEWIDTH) {
            return width;
        } else if (i == SCENEHEIGHT) {
            return height;
        }
        return 0;
    }


    /**
     * dp-->px
     *
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @return
     */

    public int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, mContext.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip( float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public  int px2sp( float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
