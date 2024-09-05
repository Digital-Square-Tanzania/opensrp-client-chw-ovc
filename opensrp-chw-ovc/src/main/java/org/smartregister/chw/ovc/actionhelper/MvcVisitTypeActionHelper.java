package org.smartregister.chw.ovc.actionhelper;

import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static org.smartregister.AllConstants.OPTIONS;
import static org.smartregister.chw.ovc.util.Constants.STEP_ONE;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.Visit;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.JsonFormUtils;
import org.smartregister.chw.ovc.util.VisitUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public abstract class MvcVisitTypeActionHelper extends OvcVisitActionHelper {
    private final MemberObject memberObject;
    private String visitType;
    private JSONObject jsonForm;
    private boolean editMode;

    public MvcVisitTypeActionHelper(MemberObject memberObject, boolean editMode) {
        this.memberObject = memberObject;
        this.editMode = editMode;
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
            List<Visit> previousVisits = VisitUtils.getVisits(memberObject.getBaseEntityId(), Constants.EVENT_TYPE.MVC_CHILD_SERVICES_VISIT);
            if (editMode
                    && !previousVisits.isEmpty()
                    && previousVisits.size() > 1
            ) {
                JSONArray fields = jsonForm.getJSONObject(STEP_ONE).getJSONArray(FIELDS);
                JSONObject visitType = fields.getJSONObject(0);
                visitType.getJSONArray(OPTIONS).remove(0);
            } else if (!previousVisits.isEmpty() && previousVisits.get(0).getProcessed()) {
                JSONArray fields = jsonForm.getJSONObject(STEP_ONE).getJSONArray(FIELDS);
                JSONObject visitType = fields.getJSONObject(0);
                visitType.getJSONArray(OPTIONS).remove(0);
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
            visitType = JsonFormUtils.getValue(payload, "visit_type");
            processVisitType(visitType);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    public abstract void processVisitType(String visitType);

    @Override
    public String evaluateSubTitle() {
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
