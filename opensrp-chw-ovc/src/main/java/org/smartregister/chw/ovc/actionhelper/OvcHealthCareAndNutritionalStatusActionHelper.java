package org.smartregister.chw.ovc.actionhelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public  class OvcHealthCareAndNutritionalStatusActionHelper extends OvcVisitActionHelper {
    private String childReceivedVaccinations;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            childReceivedVaccinations = JsonFormUtils.getValue(payload, "child_received_vaccinations");
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
        if (StringUtils.isNotBlank(childReceivedVaccinations)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
