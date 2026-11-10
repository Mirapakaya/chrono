package com.chronoio.chrono.core_utils.text

import android.view.KeyEvent

fun KeyEvent?.isEnterPressed() = this != null
        && keyCode == KeyEvent.KEYCODE_ENTER
        && action == KeyEvent.ACTION_DOWN