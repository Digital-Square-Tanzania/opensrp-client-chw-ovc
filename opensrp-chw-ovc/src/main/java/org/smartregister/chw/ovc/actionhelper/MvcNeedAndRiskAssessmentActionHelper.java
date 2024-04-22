package org.smartregister.chw.ovc.actionhelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class MvcNeedAndRiskAssessmentActionHelper extends OvcVisitActionHelper {
    private String selectTheClientNeed;
    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectTheClientNeed = JsonFormUtils.getCheckBoxValue(payload,"select_the_client_need");
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        if(selectTheClientNeed != null){
            return null;
        } else {
            return null;
        }
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if(selectTheClientNeed != null){
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
