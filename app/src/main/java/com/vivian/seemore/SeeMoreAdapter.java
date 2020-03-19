package com.vivian.seemore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vivian.seemore.PathDrawable;
import com.vivian.seemore.R;

import java.util.ArrayList;
import java.util.List;

public abstract class SeeMoreAdapter<T> extends RecyclerView.Adapter {

    int TYPE_SEE_MORE = 0;
    int TYPE_ITEM = 1;

    public List<T> mList = new ArrayList<>();
    Context mContext;

    public SeeMoreAdapter(Context context) {
        this.mContext = context;
    }

    public void setmList(List<T> mList) {
        this.mList = mList;
    }

    /**
     * 自定义处理onCreateViewHolder
     *
     * @return
     */
    public abstract RecyclerView.ViewHolder generateViewHolder(ViewGroup parent, int viewType);

    /**
     * 自定义处理bindViewHolder
     */
    public abstract void bindviewholder(RecyclerView.ViewHolder holder, int position);

    RecyclerView parent;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        if (viewType == TYPE_SEE_MORE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.see_more, parent, false);
            SeeMoreViewHolder vh = new SeeMoreViewHolder(view);
            monitorScroll(vh);
            return vh;
        } else {
            return generateViewHolder(parent, viewType);
        }
    }

    boolean needHide = true;
    Rect rect = new Rect();

    public abstract void load();

    /**
     * SeeMore的View是否有可见区域在当前屏幕内
     *
     * @param seeMore
     * @return
     */
    int[] location = new int[2];

    boolean isSeeMoreHasVisibleRect(View seeMore) {
        seeMore.getLocationOnScreen(location);
        Log.e("seemore","location x:"+location[0]);
        return location[0] > 0;
    }

    boolean isNeedLoad(View seeMore) {
        seeMore.getLocalVisibleRect(rect);
        return rect.width() > seeMore.getWidth() * 3 / 4;
    }

    boolean isFingerUp = true;

    @SuppressLint("ClickableViewAccessibility")
    void monitorScroll(final SeeMoreViewHolder vh) {
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        isFingerUp = true;
                        if (isSeeMoreHasVisibleRect(vh.itemView)) {
                            vh.hide();
                        }
                        break;
                    default:
                    case MotionEvent.ACTION_DOWN:
                        isFingerUp = false;
                        break;
                }
                return false;
            }
        });
        //处理查看更多
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && needHide) {
                    vh.hide();
                    needHide = false;
                    if (isSeeMoreHasVisibleRect(vh.itemView) && isNeedLoad(vh.itemView)) {
                        load();
                    }
                    isFingerUp = true;
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    needHide = true;
                    isFingerUp = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                vh.itemView.getLocalVisibleRect(rect);
                if (!isSeeMoreHasVisibleRect(vh.itemView)) {
                    return;
                }
                vh.pathDrawable.setScrollX(dx, rect.right);

                float angle = rect.width() * 180 / vh.itemView.getWidth();
                if (rect.width() <= vh.itemView.getWidth() * 3 / 4) {
                    vh.rotate(angle);
                    vh.load(false);
                } else {
                    vh.rotate(angle);
                    vh.load(true);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEE_MORE) {
            final SeeMoreViewHolder vh = (SeeMoreViewHolder) holder;
        } else {
            bindviewholder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_SEE_MORE;
        } else {
            int type = itemViewType(position);
            if (type != 0) {
                return type;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    public abstract int itemViewType(int position);

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    class SeeMoreViewHolder extends RecyclerView.ViewHolder {
        Rect rect = new Rect();
        ImageView icon;
        TextView title;
        DecelerateInterpolator interpolator;
        PathDrawable pathDrawable;

        public SeeMoreViewHolder(View view) {
            super(view);
            pathDrawable = new PathDrawable();
            itemView.setBackground(pathDrawable);
            icon = view.findViewById(R.id.icon);
            title = view.findViewById(R.id.title);
            interpolator = new DecelerateInterpolator();
        }

        public void hide() {
            itemView.getLocalVisibleRect(rect);
            parent.smoothScrollBy(-rect.width(), 0, interpolator);
        }

        public void load(boolean load) {
            if (load) {
                title.setText("松开即可加载");
            } else {
                title.setText("左滑加载更多");
            }
        }

        public void rotate(float angle) {
            icon.setRotation(angle - 90);
        }
    }
}
