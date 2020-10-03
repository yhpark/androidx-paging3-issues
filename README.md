# androidx-paging3-issues

This is a project containing an assortment of bug demonstrations for paging 3 library.

There are 3 issues demonstrated in this sample project. Set the `CurrentDemo` value [MainActivity.kt:32](https://github.com/yhpark/androidx-paging3-issues/blob/master/app/src/main/java/com/example/myapplication/MainActivity.kt#L32) to one of the three issues in order to reproduce the issue.

- Issue A: wrong scroll position when data change happens outside visible area.

  <img src="https://github.com/yhpark/androidx-paging3-issues/blob/master/gifs/a.gif?raw=true" width="40%" height="40%" />

- Issue B: refresh key is set to null when a PagingSource is invalidated before loading any pages.

  <img src="https://github.com/yhpark/androidx-paging3-issues/blob/master/gifs/b.gif?raw=true" width="40%" height="40%" />
  
- Issue C: jumpSupported

  <img src="https://github.com/yhpark/androidx-paging3-issues/blob/master/gifs/c.gif?raw=true" width="40%" height="40%" />
