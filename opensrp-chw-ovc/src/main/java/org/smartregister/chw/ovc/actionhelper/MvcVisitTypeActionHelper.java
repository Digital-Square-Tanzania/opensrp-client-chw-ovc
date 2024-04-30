package org.smartregister.chw.ovc.actionhelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public  abstract class MvcVisitTypeActionHelper extends OvcVisitActionHelper {
    private String visitType;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            visitType = JsonFormUtils.getValue(payload, "visit_type");
            processVisitType(visitType);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    public abstract void processVisitType(String visitType);

    @Override
    public String evaluateSubTitle() {
        if (StringUtils.isNotBlank(visitType)) {
            return null;
        }
        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(visitType)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
