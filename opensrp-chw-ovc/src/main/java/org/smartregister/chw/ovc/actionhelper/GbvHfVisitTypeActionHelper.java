package org.smartregister.chw.ovc.actionhelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import timber.log.Timber;

public abstract class GbvHfVisitTypeActionHelper extends GbvVisitActionHelper {
    String visitStatus;
    String canManageCase;

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            visitStatus = JsonFormUtils.getValue(payload, "visit_status");
            canManageCase = JsonFormUtils.getValue(payload, "can_manage_case");
        } catch (JSONException e) {
            Timber.d(e);
        }
        processCanManageCase(canManageCase);
    }

    public abstract void processCanManageCase(String canManageCase);

    @Override
    public String evaluateSubTitle() {
        if (StringUtils.isNotBlank(visitStatus)) {
            return "Visit Status: " + visitStatus + "\n" +
                    "Can Manage Case: " + canManageCase;

        }

        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(visitStatus)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
