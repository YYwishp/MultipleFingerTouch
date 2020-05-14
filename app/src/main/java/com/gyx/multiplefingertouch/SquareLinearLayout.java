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

	private static final int PRESS_TIME = 3000;
	private static int hash = 0;

	private Context context;
	private int widthSize;
	private AnimatorSet animatorSet;

	// 用于判断第2个手指是否存在
	boolean haveSecondPoint = false;
	// 用于判断第3个手指是否存在
	boolean haveThirdPoint = false;

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


	interface MultiClickListener{
		void onSingleClickListener();

		void onSingleLongClickListener();

		void onSecondPointClickListener();

		void onSecondPointLongClickListener();

		public void onThirdPointClickListener();
	}

	private MultiClickListener mMultiClickListener;
	public void setOnMultiTouchListener(MultiClickListener multiTouchListener) {
		mMultiClickListener = multiTouchListener;
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.e("按下几个手指", event.getPointerCount() + "个");
		int index = event.getActionIndex();
		long downTime = event.getDownTime();
		//Log.e("TouchEvent", "action== " + event.getActionMasked());
		//Log.e("index", index+"");
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				//for (int i = 0; i < event.getPointerCount(); i++) {
				//	Log.e("按下", "pointerIndex="+i+", pointerId="+event.getPointerId(i));
				//}

				//-->一开始 第一个手指点击的时候，第一步要初始化
				haveSecondPoint = false;
				haveThirdPoint = false;


				int pointerId = event.getPointerId(index);
				Log.e("ACTION_DOWN","第1个手指按下 pointerId = "+pointerId+" 手指数量 "+event.getPointerCount());


				zoomInAnimation();
				break;


			case MotionEvent.ACTION_POINTER_DOWN: // 有非主要的手指按下(即按下之前已经有手指在屏幕上)。
				int pointerId_2 = event.getPointerId(index);

				Log.e("ACTION_POINTER_DOWN","第"+(index+1)+"个手指按下  pointId = "+pointerId_2 +" 手指数量 "+event.getPointerCount());
				// 判断是否是第2个手指按下
				if (event.getPointerId(index) == 1) {
					haveSecondPoint = true;
					Log.e("TAG","有2个手指");
				}
				// 判断是否是第3个手指按下
				if (event.getPointerId(index) == 2) {
					haveThirdPoint = true;
					Log.e("TAG","有3个手指");
				}


				break;
			case MotionEvent.ACTION_MOVE:
				//for (int i = 0; i < event.getPointerCount(); i++) {
				//	Log.e("ACTION_MOVE", "pointerIndex="+i+", pointerId="+event.getPointerId(i));
				//}
				//int index_move = event.getActionIndex(); //在 ACTION_MOVE 情况下，获取始终是0
				//Log.e("index_move","index = "+index_move);
				Log.e("ACTION_MOVE","EventTime = "+event.getEventTime()+"");
				Log.e("ACTION_MOVE","getDownTime = "+event.getDownTime()+"");





				break;

			case MotionEvent.ACTION_POINTER_UP: //有非主要的手指抬起(即抬起之后仍然有手指在屏幕上)。
				//if (event.getPointerCount() == 2) {
				//	haveSecondPoint = false;
				//}
				//if (event.getPointerCount() == 3) {
				//	haveThirdPoint = false;
				//}


				//Log.e("Tag", "ACTION_POINTER_UP")
				int pointerId_4 = event.getPointerId(index);
				Log.e("ACTION_POINTER_UP","第"+(index+1)+"个手指抬起 pointId = "+pointerId_4+" 手指数量 "+event.getPointerCount());
				break;


			case MotionEvent.ACTION_UP:
				//Log.e("Tag", "ACTION_UP");
				int pointerId_3 = event.getPointerId(index);

				Log.e("ACTION_UP","最后1个手指抬起  pointId = "+pointerId_3+" 手指数量 "+event.getPointerCount() +"haveThirdPoint = "+haveThirdPoint +"   haveSecondPoint = "+haveSecondPoint);

				//for (int i = 0; i < event.getPointerCount(); i++) {
				//	Log.e("抬起", "pointerIndex="+i+", pointerId="+event.getPointerId(i));
				//}
				if (!haveThirdPoint && !haveSecondPoint) {
					if (mMultiClickListener != null) {
						mMultiClickListener.onSingleClickListener();
					}
				} else if (haveThirdPoint) {
					haveSecondPoint = false;
					if (mMultiClickListener != null) {
						mMultiClickListener.onThirdPointClickListener();
					}
				} else if (haveSecondPoint) {
					if (mMultiClickListener != null) {
						mMultiClickListener.onSecondPointClickListener();
					}
				}


				zoomOutAnimation();

				break;


			case MotionEvent.ACTION_CANCEL:
				zoomOutAnimation();
				break;
		}
		//return super.onTouchEvent(event);
		return true;

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
