package cn.refactor.lib.colordialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.concurrent.atomic.AtomicInteger;

import cn.refactor.lib.colordialog.util.DisplayUtil;

/**
 * 作者 : andy
 * 日期 : 15/11/10 11:28
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 提示性的Dialog
 */
public class PromptDialog extends Dialog {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_RADIUS = 6;
    public static final int DIALOG_TYPE_INFO = 0;
    public static final int DIALOG_TYPE_HELP = 1;
    public static final int DIALOG_TYPE_WRONG = 2;
    public static final int DIALOG_TYPE_SUCCESS = 3;
    public static final int DIALOG_TYPE_WARNING = 4;
    public static final int DIALOG_TYPE_PROGRESS = 10;
    public static final int DIALOG_TYPE_NONE = 11;
    public static final int DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO;

    private AnimationSet mAnimIn, mAnimOut;
    private View mDialogView;
    private TextView mTitleTv, mContentTv, mPositiveBtn, mNegativeBtn;
    private ImageButton mCloseBtn;
    private OnButtonListener mOnPositiveListener, mOnNegativeListener;

    //custom view
    private double dialogSize = 0.7;
    private FrameLayout mAdditionContainerView;
    private View mCustomView;

    private ProgressBar mProgressBar;

    private int mDialogType;
    private boolean mIsShowAnim;
    private CharSequence mTitle, mContent, mOkBtnText, mCancelBtnText;
    private boolean isPositiveBtnEnabled, isNegativeBtnEnabled;
    private boolean isButtonCloseVisible;
    private int mPositiveBtnDrawableId, mNegativeBtnDrawableId;
    private AtomicInteger mPositiveBtnTextColor, mNegativeBtnTextColor;
    private int mContentTextAlignment = Gravity.LEFT;

    public PromptDialog(Context context) {
        this(context, 0);
    }

    public PromptDialog(Context context, int theme) {
        super(context, R.style.color_dialog);
        init();
    }

    private void init() {
        mAnimIn = AnimationLoader.getInAnimation(getContext());
        mAnimOut = AnimationLoader.getOutAnimation(getContext());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initListener();

    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.layout_promptdialog, null);
        setContentView(contentView);
        resizeDialog();

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mAdditionContainerView = (FrameLayout) contentView.findViewById(R.id.additionContainer);
        mTitleTv = (TextView) contentView.findViewById(R.id.tvTitle);
        mContentTv = (TextView) contentView.findViewById(R.id.tvContent);
        mPositiveBtn = (TextView) contentView.findViewById(R.id.btnPositive);
        mNegativeBtn = (TextView) contentView.findViewById(R.id.btnNegative);
        mCloseBtn = (ImageButton) contentView.findViewById(R.id.btnClose);

        if (isButtonCloseVisible) {
            mCloseBtn.setVisibility(View.VISIBLE);
        }

        View llBtnGroup = findViewById(R.id.llBtnGroup);
        ImageView logoIv = (ImageView) contentView.findViewById(R.id.logoIv);

        setBtnBackground(mPositiveBtn, mNegativeBtn);
        setBottomCorners(llBtnGroup);

        LinearLayoutCompat topLayout = (LinearLayoutCompat) contentView.findViewById(R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
        triangleIv.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);

        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(mDialogType)));
        RelativeLayout llTop = (RelativeLayout) findViewById(R.id.llTop);
        llTop.setBackgroundDrawable(shapeDrawable);

        if(mDialogType < DIALOG_TYPE_PROGRESS) {
            logoIv.setBackgroundResource(getLogoResId(mDialogType));
        }else if(mDialogType == DIALOG_TYPE_PROGRESS){
            logoIv.setVisibility(View.GONE);
            mProgressBar = (ProgressBar) contentView.findViewById(R.id.progressBarTop);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        mTitleTv.setText(mTitle);

        if(mContent != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mContentTv.setText(Html.fromHtml(mContent.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mContentTv.setText(Html.fromHtml(mContent.toString()));
            }
            mContentTv.setGravity(mContentTextAlignment);
        }else{
            mContentTv.setVisibility(View.GONE);
        }


//        mContentTv.setText(Html.fromHtml(mContent.toString()));
        mPositiveBtn.setText(mOkBtnText);
        mNegativeBtn.setText(mCancelBtnText);

        if (mPositiveBtnDrawableId > 0) {
            mPositiveBtn.setCompoundDrawablesWithIntrinsicBounds(mPositiveBtnDrawableId, 0, 0, 0);
        }
        if (mNegativeBtnDrawableId > 0) {
            mNegativeBtn.setCompoundDrawablesWithIntrinsicBounds(mNegativeBtnDrawableId, 0, 0, 0);
        }

        if (mPositiveBtnTextColor != null) {
            mPositiveBtn.setTextColor(mPositiveBtnTextColor.get());
        }
        if (mNegativeBtnTextColor != null) {
            mNegativeBtn.setTextColor(mNegativeBtnTextColor.get());
        }

        if (isNegativeBtnEnabled) {
            contentView.findViewById(R.id.btnNegativeContainer).setVisibility(View.VISIBLE);
            LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) mPositiveBtn.getLayoutParams();
            params.weight = 1;
            mPositiveBtn.setLayoutParams(params);
        }

        if(!isPositiveBtnEnabled){
            mPositiveBtn.setVisibility(View.GONE);
        }

        if(mCustomView != null){
            mAdditionContainerView.setVisibility(View.VISIBLE);
            mAdditionContainerView.addView(mCustomView);
        }
    }

    private void resizeDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (DisplayUtil.getScreenSize(getContext()).x * dialogSize);
        getWindow().setAttributes(params);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWithAnimation(mIsShowAnim);
    }

    @Override
    public void dismiss() {
        dismissWithAnimation(mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation) {
        if (showInAnimation) {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation) {
        if (showOutAnimation) {
            mDialogView.startAnimation(mAnimOut);
        } else {
            super.dismiss();
        }
    }

    private int getLogoResId(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.mipmap.ic_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.mipmap.ic_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.mipmap.ic_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.mipmap.icon_warning;
        }
        return R.mipmap.ic_info;
    }

    private int getColorResId(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.color.color_type_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.color.color_type_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.color.color_type_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.color.color_type_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.color.color_type_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.color.color_type_warning;
        }
        return R.color.color_type_info;
    }

    private int getSelBtn(int mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.drawable.sel_btn;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.drawable.sel_btn_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.drawable.sel_btn_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.drawable.sel_btn_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.drawable.sel_btn_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.drawable.sel_btn_warning;
        }
        return R.drawable.sel_btn;
    }

    private void initAnimListener() {
        mAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        callDismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void initListener() {
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPositiveListener != null) {
                    mOnPositiveListener.onClick(PromptDialog.this);
                }
            }
        });
        if (isNegativeBtnEnabled) {
            mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNegativeListener != null) {
                        mOnNegativeListener.onClick(PromptDialog.this);
                    }
                }
            });
        }

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        initAnimListener();
    }

    private void callDismiss() {
        super.dismiss();
    }

    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(mDialogType)));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width / 2, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void setBtnBackground(final TextView... btns) {
        for (TextView btn : btns) {
            btn.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(mDialogType)),
                    getContext().getResources().getColor(R.color.color_dialog_gray)));
            btn.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(mDialogType)));
        }
    }

    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public PromptDialog setAnimationEnable(boolean enable) {
        mIsShowAnim = enable;
        return this;
    }

    public PromptDialog setTitleText(CharSequence title) {
        mTitle = title;
        return this;
    }

    public PromptDialog setTitleText(int resId) {
        return setTitleText(getContext().getString(resId));
    }

    public PromptDialog setContentText(CharSequence content) {
        mContent = content;
        return this;
    }

    public PromptDialog setContentText(int resId) {
        return setContentText(getContext().getString(resId));
    }

    public PromptDialog setContentTextAlignment(int alignment) {
        this.mContentTextAlignment = alignment;
        return this;
    }

    public TextView getTitleTextView() {
        return mTitleTv;
    }

    public TextView getContentTextView() {
        return mContentTv;
    }

    public PromptDialog setDialogType(int type) {
        mDialogType = type;
        return this;
    }

    public int getDialogType() {
        return mDialogType;
    }

    public PromptDialog setPositiveListener(CharSequence btnText, OnButtonListener l) {
        isPositiveBtnEnabled = true;

        mOkBtnText = btnText;
        return setPositiveListener(l);
    }

    public PromptDialog setPositiveListener(int stringResId, OnButtonListener l) {
        return setPositiveListener(getContext().getString(stringResId), l);
    }

    public PromptDialog setPositiveListener(OnButtonListener l) {
        isPositiveBtnEnabled = true;

        mOnPositiveListener = l;
        return this;
    }

    public PromptDialog setPositiveDrawable(int drawableId) {
        mPositiveBtnDrawableId = drawableId;
        return this;
    }

    public PromptDialog setNegativeDrawable(int drawableId) {
        mNegativeBtnDrawableId = drawableId;
        return this;
    }

    public PromptDialog setPositiveTextColor(int color) {
        mPositiveBtnTextColor = new AtomicInteger(color);
        return this;
    }

    public PromptDialog setNegativeTextColor(int color) {
        mNegativeBtnTextColor = new AtomicInteger(color);
        return this;
    }

    public PromptDialog setNegativeListener(CharSequence btnText, OnButtonListener l) {
        mCancelBtnText = btnText;
        return setNegativeListener(l);
    }

    public PromptDialog setNegativeListener(int btnTextResId, OnButtonListener l) {
        mCancelBtnText = getContext().getString(btnTextResId);
        return setNegativeListener(l);
    }

    private PromptDialog setNegativeListener(OnButtonListener l) {
        isNegativeBtnEnabled = true;
        mOnNegativeListener = l;
        return this;
    }

    public PromptDialog setAnimationIn(AnimationSet animIn) {
        mAnimIn = animIn;
        return this;
    }

    public PromptDialog setAnimationOut(AnimationSet animOut) {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public PromptDialog setButtonCloseVisible(boolean visible) {
        isButtonCloseVisible = visible;

        return this;
    }

    public PromptDialog setCustomView(View customView){
        mCustomView = customView;

        return this;
    }

    public PromptDialog setCancelable(boolean cancelable, boolean cancelOnTouchOutside) {
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(cancelOnTouchOutside);
        return this;
    }

    public PromptDialog setDialogSize(double size){
        if(size < 0){
            dialogSize = 0;
        }
        if(size > 1){
            dialogSize = 1;
        }
        dialogSize = size;

        return this;
    }

    public PromptDialog showDialog() {
        this.show();
        return this;
    }

    public interface OnButtonListener {
        void onClick(PromptDialog dialog);
    }

    public ProgressBar getProgressBar(){
        return mProgressBar;
    }
}
