package org.smartregister.chw.ovc.actionhelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class OvcChildProtectionActionHelper extends OvcVisitActionHelper {
    private String selectChildProtectionService;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectChildProtectionService = JsonFormUtils.getCheckBoxValue(payload,"child_protection_service");
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        if(selectChildProtectionService != null){
            return "Child protection service:"+selectChildProtectionService;
        } else {
            return null;
        }
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if(selectChildProtectionService != null){
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
