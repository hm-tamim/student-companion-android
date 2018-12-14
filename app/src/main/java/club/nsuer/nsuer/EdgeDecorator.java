package club.nsuer.nsuer;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;



public class EdgeDecorator extends RecyclerView.ItemDecoration {

    private final int edgePaddingLeft;
    private final int edgePaddingRight;

    public EdgeDecorator(int edgePaddingLeft, int edgePaddingRight) {
        this.edgePaddingLeft = edgePaddingLeft;
        this.edgePaddingRight = edgePaddingRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        int itemCount = state.getItemCount();

        final int itemPosition = parent.getChildAdapterPosition(view);

        // no position, leave it alone
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        // first item
        if (itemPosition == 0) {
            outRect.set(edgePaddingLeft, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
        // last item
        else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.set(view.getPaddingLeft(), view.getPaddingTop(), edgePaddingRight, view.getPaddingBottom());
        }
        // every other item
        else {
            outRect.set(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }
}