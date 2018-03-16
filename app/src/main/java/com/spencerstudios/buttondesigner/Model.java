package com.spencerstudios.buttondesigner;

public class Model {
    // border..
    private int strokeWidth;
    private String strokeColor;

    //corners..
    private int cornerAll;
    private int topLeft;
    private int topRight;
    private int bottomLeft;
    private int bottomRight;
    private boolean switchCorner;

    //size...
    private boolean switchWidth;
    private int sizeWidth;
    private boolean switchHeight;
    private int sizeHeight;

    //color..
    private boolean useGradient;
    private boolean useRadial;
    private boolean useThreeColors;
    private int angle;
    private String colorOne, colorTwo, colorThree;
    private int centerX, centerY, radius;

    //text..
    private String text;
    private int textSize;
    private String textColor;
    private boolean allCaps;
    private String typeFace;

    Model(int strokeWidth, String strokeColor, int cornerAll, int topLeft, int topRight, int bottomLeft, int bottomRight, boolean switchCorner, boolean switchWidth, int sizeWidth, boolean switchHeight, int sizeHeight, boolean useGradient, boolean useRadial, boolean useThreeColors, int angle, String colorOne, String colorTwo, String colorThree, int centerX, int centerY, int radius, String text, int textSize, String textColor, boolean allCaps, String typeFace) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.cornerAll = cornerAll;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.switchCorner = switchCorner;
        this.switchWidth = switchWidth;
        this.sizeWidth = sizeWidth;
        this.switchHeight = switchHeight;
        this.sizeHeight = sizeHeight;
        this.useGradient = useGradient;
        this.useRadial = useRadial;
        this.useThreeColors = useThreeColors;
        this.angle = angle;
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
        this.colorThree = colorThree;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.allCaps = allCaps;
        this.typeFace = typeFace;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getCornerAll() {
        return cornerAll;
    }

    public void setCornerAll(int cornerAll) {
        this.cornerAll = cornerAll;
    }

    public int getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(int topLeft) {
        this.topLeft = topLeft;
    }

    public int getTopRight() {
        return topRight;
    }

    public void setTopRight(int topRight) {
        this.topRight = topRight;
    }

    public int getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(int bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public int getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(int bottomRight) {
        this.bottomRight = bottomRight;
    }

    public boolean isSwitchCorner() {
        return switchCorner;
    }

    public void setSwitchCorner(boolean switchCorner) {
        this.switchCorner = switchCorner;
    }

    public boolean isSwitchWidth() {
        return switchWidth;
    }

    public void setSwitchWidth(boolean switchWidth) {
        this.switchWidth = switchWidth;
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(int sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public boolean isSwitchHeight() {
        return switchHeight;
    }

    public void setSwitchHeight(boolean switchHeight) {
        this.switchHeight = switchHeight;
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(int sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    public boolean isUseGradient() {
        return useGradient;
    }

    public void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
    }

    public boolean isUseRadial() {
        return useRadial;
    }

    public void setUseRadial(boolean useRadial) {
        this.useRadial = useRadial;
    }

    public boolean isUseThreeColors() {
        return useThreeColors;
    }

    public void setUseThreeColors(boolean useThreeColors) {
        this.useThreeColors = useThreeColors;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public String getColorOne() {
        return colorOne;
    }

    public void setColorOne(String colorOne) {
        this.colorOne = colorOne;
    }

    public String getColorTwo() {
        return colorTwo;
    }

    public void setColorTwo(String colorTwo) {
        this.colorTwo = colorTwo;
    }

    public String getColorThree() {
        return colorThree;
    }

    public void setColorThree(String colorThree) {
        this.colorThree = colorThree;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean isAllCaps() {
        return allCaps;
    }

    public void setAllCaps(boolean allCaps) {
        this.allCaps = allCaps;
    }

    public String getTypeFace() {
        return typeFace;
    }

    public void setTypeFace(String typeFace) {
        this.typeFace = typeFace;
    }
}
