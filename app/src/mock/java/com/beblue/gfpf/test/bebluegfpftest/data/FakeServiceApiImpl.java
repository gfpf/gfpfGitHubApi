package com.beblue.gfpf.test.bebluegfpftest.data;

import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHSearchUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.service.ServiceApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import androidx.collection.ArrayMap;
import io.reactivex.Completable;
import io.reactivex.Single;

public class FakeServiceApiImpl implements ServiceApi {

    private static final ArrayMap<Integer, GHUser> GH_USER_SERVICE_DATA = FakeServiceApiClient.loadAllGHUsers();

    @Override
    public void createGHUser(GHUser... users) {
        for (GHUser GHUser : users) {
            GH_USER_SERVICE_DATA.put(GHUser.getId(), GHUser);
        }
    }

    @Override
    public Single<List<GHUser>> loadAllGHUsers() {
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //return Lists.newArrayList(GH_USER_SERVICE_DATA.values();

        Collection<GHUser> values = GH_USER_SERVICE_DATA.values();
        List<GHUser> list = new ArrayList<>(values);
        return Single.just(list);
    }

    @Override
    public Single<GHSearchUser> searchGHUserByName(String username, String sort, String order) {
        for (Map.Entry entry : GH_USER_SERVICE_DATA.entrySet()) {
            GHUser user = (GHUser) entry.getValue();

            List<GHUser> results = new ArrayList<>(1);
            results.add(user);

            GHSearchUser  searchUser = new GHSearchUser(1, false, user.getName(), results);
            searchUser.setUsers(results);

            String itemName = user.getLogin().trim().toLowerCase();
            username = username.trim().toLowerCase();

            if (itemName.equals(username) || itemName.contains(username)) {
                return Single.just(searchUser);
            }
        }

        return Single.error(new Throwable());
    }

    @Override
    public Single<GHUser> loadGHUserById(int id) {
        for (Map.Entry entry : GH_USER_SERVICE_DATA.entrySet()) {
            GHUser user = (GHUser) entry.getValue();

            int itemId = user.getId();

            if (itemId == id) {
                return Single.just(user);
            }
        }

        return Single.error(new Throwable());
    }

    @Override
    public Completable updateGHUser(int id, String GHUser) {
        return null;
    }

    @Override
    public Completable deleteGHUser(int id) {
        return null;
    }
}