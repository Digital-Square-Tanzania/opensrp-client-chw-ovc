package org.smartregister.chw.ovc.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.TYPE;

import android.content.Context;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class OvcReferralsActionHelper extends OvcVisitActionHelper {
    private String selectReferralsProvided;

    private String visitType;

    private JSONObject jsonForm;

    public OvcReferralsActionHelper(String visitType) {
        this.visitType = visitType;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        super.onJsonFormLoaded(jsonString, context, details);
        try {
            jsonForm = new JSONObject(jsonString);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Update the preprocessed form to pass client age as global parameter
     *
     * @return the updated form
     */
    @Override
    public String getPreProcessed() {
        try {
            if (visitType.equals("new_client")) {
                JSONArray fields = jsonForm.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS);
                JSONObject referralsCompletedByClient = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, "referrals_completed_by_client");
                if (referralsCompletedByClient != null) {
                    referralsCompletedByClient.put(TYPE, "hidden");
                }
            }

            return jsonForm.toString();
        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            selectReferralsProvided = JsonFormUtils.getValue(payload, "referrals_provided");
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
