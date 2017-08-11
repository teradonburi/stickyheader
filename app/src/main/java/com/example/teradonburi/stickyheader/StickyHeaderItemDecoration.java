package com.example.teradonburi.stickyheader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daiki on 2017/07/18.
 */

public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private StickyHeaderInterface mListener;
    private View currentHeader;

    public StickyHeaderItemDecoration(@NonNull StickyHeaderInterface listener) {
        mListener = listener;
    }



    // RecyclerViewのセルが表示されたときに呼ばれる
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // 一番上のビュー
        View topChild = parent.getChildAt(0);
        if (topChild == null) {
            return; // RecyclerViewの中身がない
        }

        int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }


        int prevHeaderPosition = mListener.getHeaderPositionForItem(topChildPosition);
        if(prevHeaderPosition == -1){
           return;
        }

        // ヘッダービューが表示された
        currentHeader = getHeaderViewForItem(topChildPosition, parent);
        fixLayoutSize(parent, currentHeader);
        int contactPoint = currentHeader.getBottom();
        // 次のセルを取得
        View childInContact = getChildInContact(parent, contactPoint);
        if (childInContact == null) {
            return; // 次のセルがない
        }

        // ヘッダーの判定
        if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            // 既存のStickyヘッダーを押し上げる
            moveHeader(c, currentHeader, childInContact);
            return;
        }

        // Stickyヘッダーの描画
        drawHeader(c, currentHeader);

    }

    // dp <=> pixel変換
    public static float convertDp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }


    // Stickyヘッダービューの取得
    private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
        int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
        int layoutResId = mListener.getHeaderLayout(headerPosition);
        // Stickyヘッダーレイアウトをinflateする
        View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        //header.setElevation(header,convertDp2Px(R.dimen.shadow,header.getContext()));
        // Stickyレイアウトにデータバインドする
        mListener.bindHeaderData(header, headerPosition);
        return header;
    }

    // Stickyヘッダーを描画する
    private void drawHeader(Canvas c, View header) {
        c.save();
        c.translate(0, 0);
        header.draw(c);
        drawShadow(header,c);
        c.restore();
    }

    // Stickyヘッダーを動かす
    private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
        c.save();
        c.translate(0, nextHeader.getTop() - currentHeader.getHeight());
        currentHeader.draw(c);
        c.restore();
    }

    private void drawShadow(View target,Canvas c){
        Paint paint = new Paint();
        paint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xff000000);
        ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        c.drawRect(0, 0, layoutParams.width, layoutParams.height, paint);
    }

    // 座標から次のRecyclerViewのセル位置を取得
    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getBottom() > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    // Stickyヘッダーのレイアウトサイズを取得
    private void fixLayoutSize(ViewGroup parent, View view) {

        // RecyclerViewのSpec
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // headersのSpec
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    // Stickyヘッダーインタフェース
    public interface StickyHeaderInterface {

        int getHeaderPositionForItem(int itemPosition);

        int getHeaderLayout(int headerPosition);

        void bindHeaderData(View header, int headerPosition);

        boolean isHeader(int itemPosition);
    }
}
