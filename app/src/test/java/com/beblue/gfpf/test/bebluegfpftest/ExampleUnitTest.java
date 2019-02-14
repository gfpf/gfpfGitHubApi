package com.beblue.gfpf.test.bebluegfpftest;

import com.beblue.gfpf.test.bebluegfpftest.user.GHUserViewModel;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.GHUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Mock
    private GHUserRepository mRepository;

    @Mock
    private GHUserContract.View mView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<DisposableSingleObserver<List<GHUser>>> mLoadGHUsersCallbackCaptor;

    private GHUserViewModel mViewModel;

    @Before
    public void setupOrderPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewModel = new GHUserViewModel(null);
    }

    @Test
    public void loadOrdersFromRepositoryAndLoadIntoView() {
        // Given an initialized OrderPresenter with initialized orders When loading of Order is requested
        mViewModel.loadAllGHUsers();

        // Callback is captured and invoked with stubbed orders
        Single<List<GHUser>> list = verify(mRepository).loadAllGHUsers(mLoadGHUsersCallbackCaptor.capture());
        mLoadGHUsersCallbackCaptor.getValue().onSuccess(list.blockingGet());

        // Then progress indicator is hidden and orders are shown in UI
        InOrder inOrder = Mockito.inOrder(mView);
        inOrder.verify(mView).setProgressIndicator(true);
        inOrder.verify(mView).setProgressIndicator(false);
        verify(mView).showGHUserListUI(list.blockingGet(), false);
    }

}