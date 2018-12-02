package team25.conveniencestore.SqlProvider;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public class GooglePlacesRepository {

    private GooglePlacesDAO mDao;
    private List<GooglePlace> mAll;

    public GooglePlacesRepository(Application application) {
        GooglePlacesDatabase db = GooglePlacesDatabase.getInstance(application);
        mDao = db.placesDAO();
        mAll = mDao.getAllPlace();
//        new getAllAsyncTask(mDao,new AsyncResponse(){
//
//            @Override
//            public void processFinish(List<FavoritePlaces> output) {
//                mAll = (List<FavoritePlaces>) output;
//            }
//
//        }).execute();


    }


    public GooglePlace getPlaceById(int id) {
//        new getAllAsyncTask(mDao,new AsyncResponse(){
//
//            @Override
//            public void processFinish(List<FavoritePlaces> output) {
//                mAll = (List<FavoritePlaces>) output;
//            }
//
//        }).execute();
        return mDao.getPlaceById(id);
    }


    public List<GooglePlace> getAllPlace() {
//        new getAllAsyncTask(mDao,new AsyncResponse(){
//
//            @Override
//            public void processFinish(List<FavoritePlaces> output) {
//                mAll = (List<FavoritePlaces>) output;
//            }
//
//        }).execute();
        return mAll;
    }

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<GooglePlace>> {

        public AsyncResponse delegate = null;

        private GooglePlacesDAO mAsyncTaskDao;
        getAllAsyncTask(GooglePlacesDAO dao, AsyncResponse delegate) {
            mAsyncTaskDao = dao;
            this.delegate = delegate;
        }

        @Override
        protected List<GooglePlace> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllPlace();
        }

        @Override
        protected void onPostExecute(List<GooglePlace> listResult) {
            delegate.processFinish(listResult);
        }
    }

    //insert
    public void insertPlace (GooglePlace place) {
        new insertAsyncTask(mDao).execute(place);
    }

    private static class insertAsyncTask extends AsyncTask<GooglePlace, Void, Void> {

        private GooglePlacesDAO mAsyncTaskDao;

        insertAsyncTask(GooglePlacesDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GooglePlace... params) {
            mAsyncTaskDao.insertPlace(params[0]);
            return null;
        }
    }

    //delete
    public void deletePlace (GooglePlace place) {
        new deleteAsyncTask(mDao).execute(place);
    }

    private static class deleteAsyncTask extends AsyncTask<GooglePlace, Void, Void> {

        private GooglePlacesDAO mAsyncTaskDao;

        deleteAsyncTask(GooglePlacesDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GooglePlace... params) {
            mAsyncTaskDao.deletePlace(params[0]);
            return null;
        }
    }



}