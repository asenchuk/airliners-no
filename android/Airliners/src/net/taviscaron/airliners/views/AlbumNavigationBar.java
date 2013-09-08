package net.taviscaron.airliners.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.util.UIUtil;
import net.taviscaron.airliners.util.Validate;

public class AlbumNavigationBar extends LinearLayout {
    private static final float DISABLED_BUTTON_ALPHA = 0.3f;

    public interface AlbumNavigationBarListener {
        public void next();
        public void prev();
    }

    private AlbumNavigationBarListener listener;
    private TextView positionText;
    private Button prevButton;
    private Button nextButton;
    private long position;
    private long count;

    public AlbumNavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AlbumNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlbumNavigationBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.album_navigation_bar, this);

        prevButton = (Button)findViewById(R.id.album_navigation_bar_prev_button);
        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.prev();
                }
            }
        });

        nextButton = (Button)findViewById(R.id.album_navigation_bar_next_button);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.next();
                }
            }
        });

        positionText = (TextView)findViewById(R.id.album_navigation_bar_position);

        updateView();
    }

    public void setPosition(long position, long count) {
        Validate.makeSure(position >= 0 && count >= position, "should be: 0 <= position <= count");

        this.position = position;
        this.count = count;
        updateView();
    }

    private void updateView() {
        boolean hasPrev = position > 1;
        boolean hasNext = position < count;

        setButtonEnabled(prevButton, hasPrev);
        setButtonEnabled(nextButton, hasNext);

        if(hasNext || hasPrev) {
            positionText.setText(String.format("%d / %d", position, count));
        } else {
            positionText.setText("-");
        }
    }

    private void setButtonEnabled(Button button, boolean enabled) {
        button.setEnabled(enabled);
        UIUtil.setAlpha(button, (enabled) ? 1 : DISABLED_BUTTON_ALPHA);
    }

    public AlbumNavigationBarListener getListener() {
        return listener;
    }

    public void setListener(AlbumNavigationBarListener listener) {
        this.listener = listener;
    }
}
