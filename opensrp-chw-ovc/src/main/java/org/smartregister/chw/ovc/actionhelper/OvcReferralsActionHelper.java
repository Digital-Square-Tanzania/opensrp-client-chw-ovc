package org.smartregister.chw.ovc.actionhelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class OvcReferralsActionHelper extends OvcVisitActionHelper {
    private String selectReferralsProvided;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectReferralsProvided = JsonFormUtils.getValue(payload,"referrals_provided");
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(selectReferralsProvided)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
