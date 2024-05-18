package org.smartregister.chw.ovc.actionhelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class MvcNeedAndRiskAssessmentActionHelper extends OvcVisitActionHelper {
    private String hasNeedAssessmentBeenConducted;
    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            hasNeedAssessmentBeenConducted = JsonFormUtils.getValue(payload,"has_need_assessment_been_conducted");
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
        if(hasNeedAssessmentBeenConducted != null){
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
