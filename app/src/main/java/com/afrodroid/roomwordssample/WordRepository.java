package com.afrodroid.roomwordssample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllwords;

    //Constructor
    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllwords = mWordDao.getAllWords();
    }

    //Method to call deleteAllWords in the aSyncTask
    public void deleteAll() {
        new deleteAllWordsAsyncTask(mWordDao).execute();
    }

    //Wrapper that return the cached words as LiveData
    LiveData<List<Word>> getAllWords() {
        return mAllwords;
    }

    //Wrapper for insert method
    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);//must be called off of the UI thread or it will crash

    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }

    }


}
