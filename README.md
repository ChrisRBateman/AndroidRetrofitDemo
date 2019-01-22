AndroidRetrofitDemo
===================

This app is an updated version of <a href="https://github.com/ChrisRBateman/RESTApiAndroidDemo">RESTApiAndroidDemo</a>.

Demo uses REST Apis to fetch list of products and product details. <a href="https://square.github.io/retrofit/">Retrofit</a> 
replaces the <a href="http://square.github.io/okhttp/">OkHttp</a> and <a href="https://github.com/google/gson">gson</a> libraries 
to fetch and parse data. <a href="https://github.com/bumptech/glide">Glide</a> is still used to retrieve remote images.

The product list now uses a RecyclerView to display products and ConstraintLayouts replaces the LinearLayouts and RelativeLayouts.

<img src="screenshots/device-2019-01-21-154917.png" width="160" height="284" title="Screen Shot 1">  <img src="screenshots/device-2019-01-21-154952.png" width="160" height="284" title="Screen Shot 2">

Retrofit and Glide are added as dependencies in the gradle build file.   
Written in <b>Java</b> using Android Studio 3 and tested on Android 7.0.
