# ScratchView


Intro
------

ScratchView repo is UX Design involving scratch cards like views which are scratched to reveal the information they conceal. There
are two types of ScratchView 
* <a href='https://github.com/cooltechworks/ScratchView/blob/master/views/src/main/java/com/cooltechworks/views/ScratchImageView.java'> ScratchImageView </a>
    - A Child of ImageView which conceals the image. Scratching over the view will reveal the hidden image.
  
* <a href='https://github.com/cooltechworks/ScratchView/blob/master/views/src/main/java/com/cooltechworks/views/ScratchTextView.java'> ScratchTextView </a>
    - A Child of TextView which conceals the text. Scratching over the view will reveal the hidden text.
    
Demo Screen
------    

|     ScratchImageView                |        ScratchTextView              | 
| ----------------------------  | ----------------------------- | 
| ![ScratchImageView][scratch_image] | ![ScratchTextView][scratch_text]   |

### Useful Methods

Both the views have following three methods which are useful to reveal or determine whether revealed and listener during revealing the hidden text/image.

* ```isRevealed()``` - tells whether the text/image has been revealed.
* ```reveal()``` - reveals the image/text if not revealed yet.
* ```setRevealListener(IRevealListener)``` - a callback listener interface which gets called back when user reveals the text/image
through onReveal() method.

Usage
--------

### ScratchImageView

##### XML

```xml
<com.cooltechworks.views.ScratchImageView
  android:id="@+id/sample_image"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:background="@android:color/white"
  android:src="@drawable/img_sample2"
  />

```

##### JAVA

```java
ScratchImageView scratchImageView = new ScratchImageView(this);

scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
    @Override
    public void onRevealed(ScratchImageView tv) {
        // on reveal
    }

    @Override
    public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
        // on image percent reveal
    }
});
```

### ScratchTextView

##### XML

```xml
<com.cooltechworks.views.ScratchTextView
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:gravity="center|end"
  android:text="@string/flat_200"
  android:textSize="15sp"
  android:textStyle="bold" />

```

##### JAVA

```java
ScratchTextView scratchTextView = new ScratchTextView(this);

scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
    @Override
    public void onRevealed(ScratchTextView tv) {
        //on reveal
    }


    @Override
    public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
        // on text percent reveal
    }
});
```

Adding to your project
------------------------

- Add the following configuration in your build.gradle file.

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.cooltechworks:ScratchView:v1.1'
}
```

Developed By
------------

* Harish Sridharan - <harish.sridhar@gmail.com>


License
--------
```
Copyright 2016 Harish Sridharan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


[scratch_image]:https://raw.githubusercontent.com/cooltechworks/ScratchView/2ec97c9a539d5976b68bf62ec07df8c727d72be2/screenshots/scratch_image_view_demo.gif
[scratch_text]:https://raw.githubusercontent.com/cooltechworks/ScratchView/master/screenshots/scratch_text_view_demo.gif



