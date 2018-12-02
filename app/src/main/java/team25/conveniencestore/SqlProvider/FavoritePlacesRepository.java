package team25.conveniencestore.SqlProvider;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class FavoritePlacesRepository {

    private FavoritePlacesDao mDao;
    private List<FavoritePlaces> mAll;

    public FavoritePlacesRepository(Application application) {
        FavoritePlacesDatabase db = FavoritePlacesDatabase.getInstance(application);
        mDao = db.placesDAO();
        mAll = mDao.getALlPlace();
//        new getAllAsyncTask(mDao,new AsyncResponse(){
//
//            @Override
//            public void processFinish(List<FavoritePlaces> output) {
//                mAll = (List<FavoritePlaces>) output;
//            }
//
//        }).execute();


    }


    public FavoritePlaces getPlaceById(int id) {
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


    public List<FavoritePlaces> getALlPlace() {
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

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<FavoritePlaces>> {

        public AsyncResponse delegate = null;

        private FavoritePlacesDao mAsyncTaskDao;
        getAllAsyncTask(FavoritePlacesDao dao,AsyncResponse delegate) {
            mAsyncTaskDao = dao;
            this.delegate = delegate;
        }

        @Override
        protected List<FavoritePlaces> doInBackground(Void... voids) {
            return mAsyncTaskDao.getALlPlace();
        }

        @Override
        protected void onPostExecute(List<FavoritePlaces> listResult) {
            delegate.processFinish(listResult);
        }
    }

    //insert
    public void insertPlace (FavoritePlaces place) {
        new insertAsyncTask(mDao).execute(place);
    }

    private static class insertAsyncTask extends AsyncTask<FavoritePlaces, Void, Void> {

        private FavoritePlacesDao mAsyncTaskDao;

        insertAsyncTask(FavoritePlacesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoritePlaces... params) {
            mAsyncTaskDao.insertPlace(params[0]);
            return null;
        }
    }

    //delete
    public void deletePlace (FavoritePlaces place) {
        new deleteAsyncTask(mDao).execute(place);
    }

    private static class deleteAsyncTask extends AsyncTask<FavoritePlaces, Void, Void> {

        private FavoritePlacesDao mAsyncTaskDao;

        deleteAsyncTask(FavoritePlacesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoritePlaces... params) {
            mAsyncTaskDao.deletePlace(params[0]);
            return null;
        }
    }



}