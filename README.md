FloatingActionButton
====================

It is project merged from FloatingActionButton by Faiz Malkani and FloatingActionButton-Compat by Quinny898.
I make some changes and fixes, most important I think is preview while adding it to layout.

Library to use the Floating Action Button (FAB) from Android 2.1+

Use Drawables of size `24dp` as FabDrawables to get the desired look. Drawables can be generated via [Android Asset Studio](http://romannurik.github.io/AndroidAssetStudio/).

## How add to project

1. Downlad
2. Copy all files to:	your_project/libs/FloatingActionButton 
3. Add dependency in build.gradle of your project
	```gradle
	dependencies {
		compile fileTree(include: ['*.jar'], dir: 'libs')
		compile project(':FloatingActionButton')
	}
	```
4. Open module settings, add existing project and choose FloatingActionButton in your project
5. Sync and build

## Instructions

1. First of all add custom attributes in top layout

    ```xml
	xmlns:app="http://schemas.android.com/apk/res-auto"
	```
	
	For example:
	```xml	
	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		xmlns:app="http://schemas.android.com/apk/res-auto"
		android:layout_width="fill_parent"
		android:layout_height="wrap_content">
		
		<!-- Your layout elements -->
	</FrameLayout>
    ```


2. Add FAB to layout - whenever you want. If it will be on bottom right you can use layout gravity bottom|right. If it will be somewhere between two layouts I prefer to use RelativeLayout (remember that last item is on top so FAB should be defined last).

    ```xml
    <com.faizmalkani.floatingactionbutton.FloatingActionButton
		android:id="@+id/fab"
		android:layout_width="56dp"	<!-- mini; 72dp for normal -->
		android:layout_height="56dp"
		android:layout_gravity="bottom|right"
        android:layout_marginBottom="16dp"
        android:layout_marginRight="16dp"
        app:color="#ffffffff"
		app:drawable="@drawable/ic_action_new" />
    ```

	Also available attributes:	
        - shadowRadius
        - shadowDx
        - shadowDy
        - shadowColor
		
3. You can change these attributes programmatically, eg.:
	```java
	mFab.setFabColor(int fabColor);
	mFab.setFabDrawable(Drawable fabDrawable);
	```

4. If needed, call the other methods of the FAB

    ```java
    mFab.hideFab();
    mFab.showFab();
    mFab.setAlpha();
    mFab.setOnClickListener();
    ```

## License
License file

© 2014 Marcin Urbańczyk
