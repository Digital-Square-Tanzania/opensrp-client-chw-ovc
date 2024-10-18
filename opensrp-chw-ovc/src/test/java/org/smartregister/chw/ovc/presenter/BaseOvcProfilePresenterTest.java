package org.smartregister.chw.ovc.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.ovc.contract.OvcProfileContract;
import org.smartregister.chw.ovc.domain.MemberObject;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseOvcProfilePresenterTest {

    @Mock
    private OvcProfileContract.View view = Mockito.mock(OvcProfileContract.View.class);

    @Mock
    private OvcProfileContract.Interactor interactor = Mockito.mock(OvcProfileContract.Interactor.class);

    @Mock
    private MemberObject memberObject = new MemberObject();

    private BaseOvcProfilePresenter profilePresenter = new BaseOvcProfilePresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void malariaTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordOvcButton("");
        verify(view).hideView();
    }

    @Test
    public void malariaTestDatePeriodGreaterThanTen() {
        profilePresenter.recordOvcButton("OVERDUE");
        verify(view).setOverDueColor();
    }

    @Test
    public void malariaTestDatePeriodIsMoreThanFourteen() {
        profilePresenter.recordOvcButton("EXPIRED");
        verify(view).hideView();
    }

    @Test
    public void refreshProfileBottom() {
        profilePresenter.refreshProfileBottom();
        verify(interactor).refreshProfileInfo(memberObject, profilePresenter.getView());
    }

    @Test
    public void saveForm() {
        profilePresenter.saveForm(null);
        verify(interactor).saveRegistration(null, view);
    }
}
