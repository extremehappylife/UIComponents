# UIComponents

动图圆角控件

## CornerGifView

常见的图片加载框架加载动图设置圆角属性无效时可以使用此控件。

![image](https://raw.githubusercontent.com/extremehappylife/UIComponents/master/app/src/main/res/drawable/gifhome_448x960_4s.gif)

### 用法
```xml
    <com.example.uicomponents.cornergifview.CornerGifView
        android:id="@+id/iv_gif_corner"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        app:cornerColor="#ffffff"
        app:leftBottomCorner="10dp"
        app:leftTopCorner="10dp"
        app:rightBottomCorner="10dp"
        app:rightTopCorner="10dp" />
```
