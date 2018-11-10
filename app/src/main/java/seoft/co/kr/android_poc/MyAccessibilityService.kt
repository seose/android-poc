package seoft.co.kr.android_poc

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    companion object {
        val STATUS_BAR_OPEN = 123
    }


    override fun onInterrupt() {


    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        event?.let {
            if(it.action == STATUS_BAR_OPEN) performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)
        }
    }

}