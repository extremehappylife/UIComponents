![image](https://img.shields.io/badge/slidebacklayout-1.0.0-blue.svg)  ![image](https://img.shields.io/badge/corner--gif-1.0.0-green.svg)

# UIComponents

这个项目整合了一些实用的UI组件、特效组件，包含：

CornerGifView（动图圆角控件）、SlideBackLayout（Activity滑动返回组件）、LikeAnimator（点赞动效）

## 目录
- [UIComponents](#uicomponents)
  * [CornerGifView（动图圆角控件）](#cornergifview)
  * [SlideBackLayout（Activity滑动返回组件）](#slidebacklayout)
  * [LikeAnimator（点赞动效）](#likeanimator)


## CornerGifView

常见的图片加载框架加载动图设置圆角属性无效时可以使用此控件。支持边框设置。

![image](https://github.com/extremehappylife/UIComponents/raw/master/app/src/main/res/drawable/gifhome_320x685_4s.gif)

### 用法
在root下的build.gradle中增加maven配置
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/extremehappy/maven' }
    }
}
```
在app目录下的build.gradle中增加依赖
```groovy
implementation 'com.happylife.uicomponents:corner-gif:1.0.0'
```
在布局文件中使用
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

![image](https://github.com/extremehappylife/UIComponents/blob/master/app/src/main/res/drawable/gifhome_320x693_5s.gif)

### 用法
在root下的build.gradle中增加maven配置
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/extremehappy/maven' }
    }
}
```
在app目录下的build.gradle中增加依赖
```groovy
implementation 'com.happylife.uicomponents:slidebacklayout:1.0.0'
```
Activity页面中使用
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

## LikeAnimator

点赞动效，可用于短视频等场景，支持自定义设置点赞动效的图片，默认为心形

![image](https://github.com/extremehappylife/UIComponents/blob/master/app/src/main/res/drawable/gifhome_320x693_like_animator.gif)

### 用法
在root下的build.gradle中增加maven配置
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://dl.bintray.com/extremehappy/maven' }
    }
}
```
在app目录下的build.gradle中增加依赖
```groovy
implementation 'com.happylife.uicomponents:likeanimator:1.0.0'
```
在布局文件中使用
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.happylife.likeanimator.LikeView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/like_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- 将自己的真正 view（比如短视频） 嵌套在内部-->
</com.happylife.likeanimator.LikeView>
```

```kotlin
like_view.setDrawableId(R.drawable.details_icon_like_pressed) // 可自行设置所需的动效图片，默认为心形
```
