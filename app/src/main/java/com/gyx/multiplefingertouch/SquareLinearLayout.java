package com.gyx.multiplefingertouch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by gaoyuxiang on 2019/4/24.
 * ==============================
 * 功能描述：宽高相等的LinerLayout 专门用于 recyclerview 显示表格的时候，在item 布局中使用，用在xml 根布局
 */
public class SquareLinearLayout extends LinearLayout {

	private static Long lastClickTime = 0L;
	private static int hash = 0;

	private Context context;
	private int widthSize;
	private AnimatorSet animatorSet;

	public SquareLinearLayout(Context context) {
		//super(context);
		this(context, null);
		this.context = context;

	}

	public SquareLinearLayout(Context context, @Nullable AttributeSet attrs) {
		//super(context, attrs);
		this(context, attrs, 0);
		this.context = context;
	}

	public SquareLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//设置测量尺寸
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		//控件宽度
		widthSize = getMeasuredWidth();
		//获取 当前宽度下，spec参数
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
		//重新测量
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);




	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);




	}

	private int mTouchRepeat = 0; //过滤掉长按的情况
	private boolean mPoint2Down = false;  //是否出现双指按下的情况
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPoint2Down = false;
				mTouchRepeat = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				mTouchRepeat++;
				break;
			case MotionEvent.ACTION_POINTER_2_DOWN:
				mPoint2Down = true;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (mPoint2Down && mTouchRepeat < 10) {
					//do something here
					Log.e("tap_tap_event", "It works!");
				}
				break;
		}
		return true;
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		View childAt = getChildAt(0);
//        //孩子分发
		childAt.dispatchTouchEvent(event);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				//beginScale(R.anim.zoom_in);
				//beginScale(R.anim.zoom_out);
				zoomInAnimation();
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				//beginScale(R.anim.zoom_out);
				zoomOutAnimation();
				break;
			case MotionEvent.ACTION_CANCEL:
				//beginScale(R.anim.zoom_out);
				zoomOutAnimation();
				break;
		}
		return super.onTouchEvent(event);

	}





	private synchronized void beginScale(int animation) {

		Animation an = AnimationUtils.loadAnimation(context, animation);
		an.setInterpolator( new LinearInterpolator());
		an.setDuration(35);
		an.setFillAfter(true);
		this.startAnimation(an);
	}

	private void zoomInAnimation() {
		ObjectAnimator anim_X = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.92f);
		ObjectAnimator anim_Y = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.92f);
		animatorSet = new AnimatorSet();
		animatorSet.playTogether(anim_X,anim_Y);
		animatorSet.setDuration(23);
		animatorSet.setInterpolator(new DecelerateInterpolator());

		animatorSet.start();

	}

	private void zoomOutAnimation() {
		ObjectAnimator anim_X = ObjectAnimator.ofFloat(this, "scaleX",  0.92f, 1f);
		ObjectAnimator anim_Y = ObjectAnimator.ofFloat(this, "scaleY", 0.92f, 1f);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(anim_X,anim_Y);
		animatorSet.setDuration(23);
		animatorSet.setInterpolator(new DecelerateInterpolator());
		animatorSet.start();

	}









	private void animationStr() {
		ValueAnimator animator = ValueAnimator.ofInt(0,300);
		animator.setDuration(1000);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int curValue = (int)animation.getAnimatedValue();
				Log.d("qijian","curValue:"+curValue);
				ViewGroup.LayoutParams layoutParams = getLayoutParams();
				layoutParams.width = layoutParams.width + curValue;
				layoutParams.height = layoutParams.height + curValue;
				setLayoutParams(layoutParams);


			}
		});
		animator.start();
	}



}
