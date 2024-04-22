package org.smartregister.chw.ovc.actionhelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public class MvcEducationAndPsychosocialSupportActionHelper extends OvcVisitActionHelper {
    private String selectEducationVocationalTraining;
    private String selectEcdPsychosocialSupport;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectEducationVocationalTraining = JsonFormUtils.getValue(payload,"select_education_vocational_training");
            selectEcdPsychosocialSupport = JsonFormUtils.getCheckBoxValue(payload,"select_ecd_psychosocial_support");
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        if(selectEcdPsychosocialSupport != null){
            return null;
        } else {
            return null;
        }
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if(selectEcdPsychosocialSupport != null){
            return BaseOvcVisitAction.Status.COMPLETED;
        } else {
            return BaseOvcVisitAction.Status.PENDING;
        }
    }
}
