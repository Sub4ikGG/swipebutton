package ru.chatan.swipebutton

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.chatan.swipebutton.databinding.SwipeButtonLayoutBinding

@SuppressLint("ClickableViewAccessibility")
class SwipeButton(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {

    private var _binding: SwipeButtonLayoutBinding? = null
    private val binding get() = _binding!!

    private var animation: ObjectAnimator? = null
    private var onSwipeButtonListener: OnSwipeButtonListener? = null

    private var defaultText: String = ""

    init {
        _binding = SwipeButtonLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        setThumbTouchListener()
        binding.shimmerBackground.showShimmer(true)

        applyAttrs(context, attrs)
    }

    private fun applyAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SwipeButton, 0, 0
        )

        applyTextAttrs(typedArray, context)
        applyDrawableColorAttrs(typedArray, context)

        typedArray.recycle()
    }

    private fun applyDrawableColorAttrs(
        typedArray: TypedArray,
        context: Context
    ) {
        val drawable =
            typedArray.getResourceId(R.styleable.SwipeButton_swipeDrawable, R.drawable.swipe_arrow)
        val thumbColor = typedArray.getColor(
            R.styleable.SwipeButton_swipeThumbColor,
            ContextCompat.getColor(context, R.color.light_blue)
        )
        val progressColor = typedArray.getColor(
            R.styleable.SwipeButton_swipeProgressColor,
            ContextCompat.getColor(context, R.color.light_blue)
        )

        binding.thumb.setImageResource(drawable)
        binding.thumb.backgroundTintList = ColorStateList.valueOf(thumbColor)
        binding.backgroundProgress.setBackgroundColor(progressColor)
    }

    private fun applyTextAttrs(
        typedArray: TypedArray,
        context: Context
    ) {
        defaultText = typedArray.getString(R.styleable.SwipeButton_swipeText).orEmpty()
        val font =
            typedArray.getResourceId(R.styleable.SwipeButton_swipeFontFamily, R.font.inter_medium)
        val color = typedArray.getColor(
            R.styleable.SwipeButton_swipeTextColor,
            ContextCompat.getColor(context, R.color.light_blue)
        )

        val textSize = typedArray.getDimensionPixelSize(R.styleable.SwipeButton_swipeTextSize, 16)
        binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        binding.textView.typeface = ResourcesCompat.getFont(context, font)
        binding.textView.setTextColor(color)
        binding.textView.text = defaultText
    }

    private fun setThumbTouchListener() {
        var offsetX = 0F
        var endX = -1
        var layoutParams: ViewGroup.LayoutParams?

        binding.thumb.setOnTouchListener { view, event ->
            val textStartX = binding.textView.left
            val textEndX = binding.textView.right
            if (endX == -1) endX = binding.shimmerBackground.right

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    offsetX = event.rawX - view.x
                    animation?.cancel()
                }

                MotionEvent.ACTION_UP -> {
                    if (view.x + view.right < endX) {
                        animation = ObjectAnimator.ofFloat(view, "translationX", 0f)
                        animation?.duration = 200
                        animation?.interpolator = LinearInterpolator()

                        animation?.addUpdateListener {
                            layoutParams = binding.backgroundProgress.layoutParams
                            layoutParams?.width = (it.animatedValue as Float + (view.right / 2) + 15).toInt()
                            binding.backgroundProgress.layoutParams = layoutParams
                        }

                        animation?.addListener(object : AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {
                            }

                            override fun onAnimationEnd(animation: Animator) {
                                setTextVisibility(1f)
                            }

                            override fun onAnimationCancel(animation: Animator) {
                            }

                            override fun onAnimationRepeat(animation: Animator) {
                            }
                        })

                        animation?.start()
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX - offsetX
                    view.x = newX

                    if (view.x + view.right >= endX) {
                        view.x = endX.toFloat() - view.right

                        binding.thumb.isEnabled = false
                        binding.thumb.isClickable = false

                        onSwipeButtonListener?.onSwiped()
                    }
                    if (view.x <= 0) view.x = 0F

                    var rawAlpha = (textStartX - (view.x + view.right))
                    if (rawAlpha > 100) setTextVisibility(1f)
                    else if (rawAlpha in 0.0..100.0) setTextVisibility(rawAlpha / 100)
                    else if (view.x + view.right > textStartX && view.x + view.right < textEndX) setTextVisibility(0f)
                    else {
                        rawAlpha = (view.x + view.left) - textEndX
                        if (rawAlpha > 100) setTextVisibility(1f)
                        else if (rawAlpha in 0.0..100.0) setTextVisibility(rawAlpha / 100)
                    }
                }
            }

            layoutParams = binding.backgroundProgress.layoutParams
            layoutParams?.width = (view.x + (view.right / 2) + 15).toInt()
            binding.backgroundProgress.layoutParams = layoutParams

            true
        }
    }

    private fun setTextVisibility(alpha: Float) {
        binding.textView.alpha = alpha
    }

    /**
     * Set listener for SwipeButton
     */
    fun setListener(onSwipeButtonListener: OnSwipeButtonListener) {
        this.onSwipeButtonListener = onSwipeButtonListener
    }

    /**
     * Set SwipeButton text
     */
    fun setText(text: String) {
        binding.textView.text = text
    }

    /**
     * Set SwipeButton default text
     */
    fun setDefaultText(defaultText: String) {
        this.defaultText = defaultText
    }

    /**
     * Reload SwipeButton, set 'setDefaultText = true' if you want return defaultText
     */
    fun reload(setDefaultText: Boolean = false) {
        binding.thumb.isEnabled = true
        binding.thumb.isClickable = true

        binding.thumb.x = 0F
        val layoutParams = binding.backgroundProgress.layoutParams
        layoutParams.width = 0
        binding.backgroundProgress.layoutParams = layoutParams

        if (setDefaultText)
            binding.textView.text = defaultText
    }

    interface OnSwipeButtonListener {
        fun onSwiped()
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)

        _binding = null
        onSwipeButtonListener = null

        animation?.cancel()
        animation = null
    }

}
