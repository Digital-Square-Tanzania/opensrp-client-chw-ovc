package org.smartregister.chw.ovc.listener;


import android.view.View;

import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.fragment.BaseOvcCallDialogFragment;
import org.smartregister.chw.ovc.util.OvcUtil;

import timber.log.Timber;

public class BaseOvcCallWidgetDialogListener implements View.OnClickListener {

    private BaseOvcCallDialogFragment callDialogFragment;

    public BaseOvcCallWidgetDialogListener(BaseOvcCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.gbv_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.gbv_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                OvcUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_gbv_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                OvcUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
