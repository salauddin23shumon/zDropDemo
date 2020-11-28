package com.sss.zdropdemo.utility;

public class ImageList {
    private int index;
    private int drawableId;

    public ImageList(int index, int drawableId) {
        this.index = index;
        this.drawableId = drawableId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
