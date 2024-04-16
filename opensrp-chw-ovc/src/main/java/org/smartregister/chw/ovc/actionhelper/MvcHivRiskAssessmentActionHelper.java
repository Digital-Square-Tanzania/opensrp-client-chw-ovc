package org.smartregister.chw.ovc.actionhelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class MvcHivRiskAssessmentActionHelper extends OvcVisitActionHelper {
    private String childEverTestedForHiv;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            childEverTestedForHiv = JsonFormUtils.getValue(payload, "child_ever_tested_for_hiv");
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(childEverTestedForHiv)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
