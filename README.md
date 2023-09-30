# swipebutton
Simple Android SwipeButton
[![](https://jitpack.io/v/Sub4ikGG/swipebutton.svg)](https://jitpack.io/#Sub4ikGG/swipebutton)

![telegram-cloud-photo-size-2-5242238087529090585-y](https://github.com/Sub4ikGG/swipebutton/assets/98654420/b79897e9-6231-412a-baa5-ceacaebcdd32)

# How it looks like
![Screen_recording_20230930_144832](https://github.com/Sub4ikGG/swipebutton/assets/98654420/cb862181-f8bc-42ce-9682-e1fe99e35cb1)

# Installation
## JitPack repository
Step 1. Add the JitPack repository to your build file  
### Groovy
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
### Kts  
```
allprojects {
  repositories {
    ...
    maven(url = "https://jitpack.io")
  }
}
```
## Dependency
Step 2. Add the dependency  
Groovy
```
dependencies {
    ...
    // SwipeButton
    implementation 'com.github.Sub4ikGG:swipebutton:1.0.0'
}
```
Kts
```
dependencies {
    ...
    // SwipeButton
    implementation("com.github.Sub4ikGG:swipebutton:1.0.0")
}
```

# Start
Add View to your layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <...>

    <ru.chatan.swipebutton.SwipeButton
        android:id="@+id/swipe_button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="32dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:swipeDrawable="@drawable/arrow"
        app:swipeText="SWIPE ME!"
        app:swipeTextColor="@color/black"
        app:swipeTextSize="14sp"
        app:swipeThumbColor="@color/light_blue"
        app:swipeProgressColor="@color/light_blue" />

    <...>

</androidx.constraintlayout.widget.ConstraintLayout>
```
```xml
app:swipeDrawable - thumb drawable
app:swipeText - swipeButton text
app:swipeTextColor - color for your text
app:swipeTextSize - textSize for the text
app:swipeThumbColor - thumb color
app:swipeProgressColor - the color that will fill swipeButton
```
# Options
### OnSwipeButtonListener:
```kotlin
swipeButton.setListener(
    object : SwipeButton.OnSwipeButtonListener {
      override fun onSwiped() {
        // TODO: Do something here
      }
    }
)
```
### Reloading:
```kotlin
// if you want to return default text use 'setDefaultText = true'
// defaultText is taken from app:swipeText
swipeButton.reload(setDefaultText = true)
```
### Set text:
```kotlin
swipeButton.setText(text = "Text")
```
### Set default text:
```kotlin
// Changing default text that represented in app:swipeText
swipeButton.setDefaultText(defaultText = "Text")
```
