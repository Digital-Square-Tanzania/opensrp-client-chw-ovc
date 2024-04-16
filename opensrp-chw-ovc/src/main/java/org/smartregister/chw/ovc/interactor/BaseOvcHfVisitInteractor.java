package org.smartregister.chw.ovc.interactor;


import android.content.Context;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.ovc.OvcLibrary;
import org.smartregister.chw.ovc.R;
import org.smartregister.chw.ovc.actionhelper.MvcEducationAndPsychosocialSupportActionHelper;
import org.smartregister.chw.ovc.actionhelper.OvcVisitActionHelper;
import org.smartregister.chw.ovc.actionhelper.MvcVisitTypeActionHelper;
import org.smartregister.chw.ovc.contract.BaseOvcVisitContract;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.Visit;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.repository.VisitRepository;
import org.smartregister.chw.ovc.util.AppExecutors;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.chw.ovc.util.JsonFormUtils;
import org.smartregister.chw.ovc.util.NCUtils;
import org.smartregister.chw.ovc.util.VisitUtils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class BaseOvcHfVisitInteractor implements BaseOvcVisitContract.Interactor {

    private final OvcLibrary ovcLibrary;

    private final LinkedHashMap<String, BaseOvcVisitAction> actionList;

    protected AppExecutors appExecutors;

    private ECSyncHelper syncHelper;

    private Context mContext;

    private Map<String, List<VisitDetail>> details = null;

    private BaseOvcVisitContract.InteractorCallBack callBack;

    private MemberObject memberObject;

    private String mCurrentPregnancyStatus;

    private String mTypeOfAssault;

    private String mHhivStatus;


    @VisibleForTesting
    public BaseOvcHfVisitInteractor(AppExecutors appExecutors, OvcLibrary OvcLibrary, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.ovcLibrary = OvcLibrary;
        this.syncHelper = syncHelper;
        this.actionList = new LinkedHashMap<>();
    }

    public BaseOvcHfVisitInteractor() {
        this(new AppExecutors(), OvcLibrary.getInstance(), OvcLibrary.getInstance().getEcSyncHelper());
    }

    @Override
    public void reloadMemberDetails(String memberID, BaseOvcVisitContract.InteractorCallBack callBack) {
        this.memberObject = getMemberClient(memberID);
        if (memberObject != null) {
            final Runnable runnable = () -> {
                appExecutors.mainThread().execute(() -> callBack.onMemberDetailsReloaded(memberObject));
            };
            appExecutors.diskIO().execute(runnable);
        }
    }

    /**
     * Override this method and return actual member object for the provided user
     *
     * @param memberID unique identifier for the user
     * @return MemberObject wrapper for the user's data
     */
    @Override
    public MemberObject getMemberClient(String memberID) {
        return OvcDao.getMember(memberID);
    }

    @Override
    public void saveRegistration(String jsonString, boolean isEditMode, BaseOvcVisitContract.InteractorCallBack callBack) {
        Timber.v("saveRegistration");
    }

    @Override
    public void calculateActions(final BaseOvcVisitContract.View view, MemberObject memberObject, final BaseOvcVisitContract.InteractorCallBack callBack) {
        mContext = view.getContext();
        this.callBack = callBack;
        if (view.getEditMode()) {
            Visit lastVisit = ovcLibrary.visitRepository().getLatestVisit(memberObject.getBaseEntityId(), Constants.EVENT_TYPE.GBV_FOLLOW_UP_VISIT);
            if (lastVisit != null) {
                details = VisitUtils.getVisitGroups(ovcLibrary.visitDetailsRepository().getVisits(lastVisit.getVisitId()));
            }
        }

        final Runnable runnable = () -> {
            try {
                createGbvHfVisitTypeAction(memberObject, details);
            } catch (BaseOvcVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    protected void createGbvHfVisitTypeAction(MemberObject memberObject, Map<String, List<VisitDetail>> details) throws BaseOvcVisitAction.ValidationException {
        OvcVisitActionHelper actionHelper = new MvcVisitTypeActionHelper() {
            @Override
            public void processVisitType(String visitType) {
                // This is used to show other actions only when the visit type is new_client or active_continuing_with_services
                if (visitType != null && (visitType.equals("new_client") || visitType.equals("active_continuing_with_services"))) {
                    try {
                        createEducationAndPsychosocialSupportAction(memberObject, details);
                        createHealthCareAndNutritionalStatusAction(memberObject, details);
                        createHivRiskAssessmentsAction(memberObject, details);
                    } catch (BaseOvcVisitAction.ValidationException e) {
                        Timber.e(e);
                    }
                } else {
                    actionList.remove(mContext.getString(R.string.mvc_education_and_psychosocial_title));
                }
                appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
            }
        };

        String actionName =
                mContext.getString(R.string.mvc_visit_type_action_title);

        BaseOvcVisitAction action = getBuilder(actionName)
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.MVC_VISIT_TYPE_FORM)
                .build();

        actionList.put(actionName, action);
    }

    protected void createEducationAndPsychosocialSupportAction(MemberObject memberObject, Map<String, List<VisitDetail>> details) throws BaseOvcVisitAction.ValidationException {
        OvcVisitActionHelper actionHelper = new MvcEducationAndPsychosocialSupportActionHelper();

        String actionName =
                mContext.getString(R.string.mvc_education_and_psychosocial_title);

        BaseOvcVisitAction action = getBuilder(actionName)
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.OVC_EDUCATION_AND_PSYCHOSOCICAL_SUPPORT_FORM)
                .build();

        actionList.put(actionName, action);
    }

    protected void createHealthCareAndNutritionalStatusAction(MemberObject memberObject, Map<String, List<VisitDetail>> details) throws BaseOvcVisitAction.ValidationException {
        OvcVisitActionHelper actionHelper = new MvcEducationAndPsychosocialSupportActionHelper();

        String actionName =
                mContext.getString(R.string.mvc_health_care_and_nutritional_status_title);

        BaseOvcVisitAction action = getBuilder(actionName)
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.MVC_HEALTHCARE_AND_NUTRITION_STATUS_FORM)
                .build();

        actionList.put(actionName, action);
    }

    protected void createHivRiskAssessmentsAction(MemberObject memberObject, Map<String, List<VisitDetail>> details) throws BaseOvcVisitAction.ValidationException {
        OvcVisitActionHelper actionHelper = new MvcEducationAndPsychosocialSupportActionHelper();

        String actionName =
                mContext.getString(R.string.mvc_hiv_risk_assessment_title);

        BaseOvcVisitAction action = getBuilder(actionName)
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.OVC_HIV_RISK_ASSESSMENT_FORM)
                .build();

        actionList.put(actionName, action);
    }


    public BaseOvcVisitAction.Builder getBuilder(String title) {
        return new BaseOvcVisitAction.Builder(mContext, title);
    }

    @Override
    public void submitVisit(final boolean editMode, final String memberID, final Map<String, BaseOvcVisitAction> map, final BaseOvcVisitContract.InteractorCallBack callBack, Constants.SaveType saveType) {
        final Runnable runnable = () -> {
            boolean result = true;
            try {
                submitVisit(editMode, memberID, map, "");
            } catch (Exception e) {
                Timber.e(e);
                result = false;
            }

            final boolean finalResult = result;
            appExecutors.mainThread().execute(() -> callBack.onSubmitted(finalResult, saveType));
        };

        appExecutors.diskIO().execute(runnable);
    }

    protected void submitVisit(final boolean editMode, final String memberID, final Map<String, BaseOvcVisitAction> map, String parentEventType) throws Exception {
        // create a map of the different types

        Map<String, BaseOvcVisitAction> externalVisits = new HashMap<>();
        Map<String, String> combinedJsons = new HashMap<>();
        String payloadType = null;
        String payloadDetails = null;

        // aggregate forms to be processed
        for (Map.Entry<String, BaseOvcVisitAction> entry : map.entrySet()) {
            String json = entry.getValue().getJsonPayload();
            if (StringUtils.isNotBlank(json)) {
                // do not process events that are meant to be in detached mode
                // in a similar manner to the the aggregated events

                BaseOvcVisitAction action = entry.getValue();
                BaseOvcVisitAction.ProcessingMode mode = action.getProcessingMode();

                if (mode == BaseOvcVisitAction.ProcessingMode.SEPARATE && StringUtils.isBlank(parentEventType)) {
                    externalVisits.put(entry.getKey(), entry.getValue());
                } else {
                    if (action.getActionStatus() != BaseOvcVisitAction.Status.PENDING)
                        combinedJsons.put(entry.getKey(), json);
                }

                payloadType = action.getPayloadType().name();
                payloadDetails = action.getPayloadDetails();
            }
        }

        String type = StringUtils.isBlank(parentEventType) ? getEncounterType() : getEncounterType();

        // persist to database
        Visit visit = saveVisit(editMode, memberID, type, combinedJsons, parentEventType);
        if (visit != null) {
            saveVisitDetails(visit, payloadType, payloadDetails);
            processExternalVisits(visit, externalVisits, memberID);
        }

        if (ovcLibrary.isSubmitOnSave()) {
            List<Visit> visits = new ArrayList<>(1);
            visits.add(visit);
            VisitUtils.processVisits(visits, ovcLibrary.visitRepository(), ovcLibrary.visitDetailsRepository());

            Context context = ovcLibrary.getInstance().context().applicationContext();

        }
    }

    /**
     * recursively persist visits to the db
     *
     * @param visit
     * @param externalVisits
     * @param memberID
     * @throws Exception
     */
    protected void processExternalVisits(Visit visit, Map<String, BaseOvcVisitAction> externalVisits, String memberID) throws Exception {
        if (visit != null && !externalVisits.isEmpty()) {
            for (Map.Entry<String, BaseOvcVisitAction> entry : externalVisits.entrySet()) {
                Map<String, BaseOvcVisitAction> subEvent = new HashMap<>();
                subEvent.put(entry.getKey(), entry.getValue());

                String subMemberID = entry.getValue().getBaseEntityID();
                if (StringUtils.isBlank(subMemberID)) subMemberID = memberID;

                submitVisit(false, subMemberID, subEvent, visit.getVisitType());
            }
        }
    }

    protected @Nullable Visit saveVisit(boolean editMode, String memberID, String encounterType, final Map<String, String> jsonString, String parentEventType) throws Exception {

        AllSharedPreferences allSharedPreferences = ovcLibrary.getInstance().context().allSharedPreferences();

        String derivedEncounterType = StringUtils.isBlank(parentEventType) ? encounterType : "";
        Event baseEvent = JsonFormUtils.processVisitJsonForm(allSharedPreferences, memberID, derivedEncounterType, jsonString, getTableName());

        // only tag the first event with the date
        if (StringUtils.isBlank(parentEventType)) {
            prepareEvent(baseEvent);
        } else {
            prepareSubEvent(baseEvent);
        }

        if (baseEvent != null) {
            baseEvent.setFormSubmissionId(JsonFormUtils.generateRandomUUIDString());
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent);

            String visitID = (editMode) ? visitRepository().getLatestVisit(memberID, getEncounterType()).getVisitId() : JsonFormUtils.generateRandomUUIDString();

            // reset database
            if (editMode) {
                Visit visit = visitRepository().getVisitByVisitId(visitID);
                if (visit != null) baseEvent.setEventDate(visit.getDate());

                VisitUtils.deleteProcessedVisit(visitID, memberID);
                deleteOldVisit(visitID);
            }

            Visit visit = NCUtils.eventToVisit(baseEvent, visitID);
            visit.setPreProcessedJson(new Gson().toJson(baseEvent));
            visit.setParentVisitID(getParentVisitEventID(visit, parentEventType));

            visitRepository().addVisit(visit);
            return visit;
        }
        return null;
    }

    protected String getParentVisitEventID(Visit visit, String parentEventType) {
        return visitRepository().getParentVisitEventID(visit.getBaseEntityId(), parentEventType, visit.getDate());
    }

    @VisibleForTesting
    public VisitRepository visitRepository() {
        return OvcLibrary.getInstance().visitRepository();
    }

    protected void deleteOldVisit(String visitID) {
        visitRepository().deleteVisit(visitID);
        OvcLibrary.getInstance().visitDetailsRepository().deleteVisitDetails(visitID);

        List<Visit> childVisits = visitRepository().getChildEvents(visitID);
        for (Visit v : childVisits) {
            visitRepository().deleteVisit(v.getVisitId());
            OvcLibrary.getInstance().visitDetailsRepository().deleteVisitDetails(v.getVisitId());
        }
    }


    protected void saveVisitDetails(Visit visit, String payloadType, String payloadDetails) {
        if (visit.getVisitDetails() == null) return;

        for (Map.Entry<String, List<VisitDetail>> entry : visit.getVisitDetails().entrySet()) {
            if (entry.getValue() != null) {
                for (VisitDetail d : entry.getValue()) {
                    d.setPreProcessedJson(payloadDetails);
                    d.setPreProcessedType(payloadType);
                    OvcLibrary.getInstance().visitDetailsRepository().addVisitDetails(d);
                }
            }
        }
    }

    /**
     * Injects implementation specific changes to the event
     *
     * @param baseEvent
     */
    protected void prepareEvent(Event baseEvent) {
        if (baseEvent != null) {
            // add gbv date obs and last
            List<Object> list = new ArrayList<>();
            list.add(new Date());
            baseEvent.addObs(new Obs("concept", "text", "vmmc_visit_date", "", list, new ArrayList<>(), null, "vmmc_visit_date"));
        }
    }

    /**
     * injects additional meta data to the event
     *
     * @param baseEvent
     */
    protected void prepareSubEvent(Event baseEvent) {
        Timber.v("You can add information to sub events");
    }

    protected String getEncounterType() {
        return Constants.EVENT_TYPE.GBV_FOLLOW_UP_VISIT;
    }

    protected String getTableName() {
        return Constants.TABLES.GBV_REGISTER;
    }

}