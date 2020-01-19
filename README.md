# UIComponents

这个项目整合了一些实用的UI、特效组件，包含：

+ CornerGifView(动图圆角控件)

## CornerGifView

常见的图片加载框架加载动图设置圆角属性无效时可以使用此控件。

![image](https://github.com/extremehappylife/UIComponents/raw/master/app/src/main/res/drawable/gifhome_320x685_4s.gif)

### 用法
```xml
<com.happylife.cornergifview.CornerGifView
        android:id="@+id/iv_gif_corner"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        app:cornerColor="#ffffff"
        app:leftBottomCorner="10dp"
        app:leftTopCorner="10dp"
        app:rightBottomCorner="10dp"
        app:rightTopCorner="10dp"
        app:strokeColor="@color/colorPrimary"
        app:strokeWidth="5dp" />
```

## SlideBackLayout

大部分用户还是习惯滑动返回，对于某些手机系统不支持滑动返回的手机，可在自己的应用中进行配置

![image](https://github.com/extremehappylife/UIComponents/raw/master/app/src/main/res/drawable/gifhome_320x685_4s.gif)

### 用法
```kotlin
        val mSlideBackLayout: SlideBackLayout = SlideBackHelper.attach(this)
        // mSlideBackLayout.setRightSlideEnable(true);
        // mSlideBackLayout.setLeftSlideEnable(true);
        mSlideBackLayout.setSwipeBackListener(object : SlideBackLayout.OnSlideBackListener {
            override fun completeSwipeBack() {
                finish()
            }
        })
```
