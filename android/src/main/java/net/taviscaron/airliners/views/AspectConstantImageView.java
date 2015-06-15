package net.taviscaron.airliners.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import net.taviscaron.airliners.R;

/**
 * Image view which changes self non match_parent side to match aspect rate.
 * @author Andrei Senchuk
 */
public class AspectConstantImageView extends ImageView {
    private float aspectRatio = 1;

    public AspectConstantImageView(Context context) {
        super(context);
    }

    public AspectConstantImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithAttrs(attrs);
    }

    public AspectConstantImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithAttrs(attrs);
    }

    private void initWithAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AspectConstantImageView);
        aspectRatio = a.getFloat(R.styleable.AspectConstantImageView_aspectRatio, aspectRatio);
        a.recycle();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setAspectRatio(int width, int height) {
        this.aspectRatio = (float)width / height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        LayoutParams lp = getLayoutParams();
        if(lp.width != LayoutParams.MATCH_PARENT && lp.height == LayoutParams.MATCH_PARENT) {
            lp.width = (int)(aspectRatio * h);
            setLayoutParams(lp);
        } else if(lp.height != LayoutParams.MATCH_PARENT && lp.width == LayoutParams.MATCH_PARENT) {
            lp.height = (int)(w / aspectRatio);
            setLayoutParams(lp);
        }
    }
}
