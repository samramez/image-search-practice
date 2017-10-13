package com.stashinvest.stashchallenge.util;

import io.reactivex.disposables.Disposable;

public class RxUtils {

   /**
    * Dispose if the disposable is in flight
    */
   public static void disposeIfNeeded(final Disposable disposable) {
      if (!disposable.isDisposed()) {
         disposable.dispose();
      }
   }
}
