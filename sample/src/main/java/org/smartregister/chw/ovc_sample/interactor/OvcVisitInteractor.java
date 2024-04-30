package org.smartregister.chw.ovc_sample.interactor;

import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.interactor.BaseOvcVisitInteractor;
import org.smartregister.chw.ovc_sample.activity.EntryActivity;

public class OvcVisitInteractor extends BaseOvcVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID) {
        return EntryActivity.getSampleMember();
    }
}
