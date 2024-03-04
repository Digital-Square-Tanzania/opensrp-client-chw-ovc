package org.smartregister.chw.ovc.contract;

import android.content.Context;

public interface BaseOvcCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
