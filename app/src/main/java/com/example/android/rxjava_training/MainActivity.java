package com.example.android.rxjava_training;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
private static final String TAG=MainActivity.class.getSimpleName();
private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Maybe<Note>observableNote=getNoteObservable();
        MaybeObserver<Note>observerNote=getNoteObserver();
        observableNote
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerNote);
    }
    private MaybeObserver<Note>getNoteObserver(){
        return new MaybeObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        };
    }
    private Maybe<Note>getNoteObservable(){
        return Maybe.create(new MaybeOnSubscribe<Note>() {
            @Override
            public void subscribe(MaybeEmitter<Note> emitter) throws Exception {
                Note note=new Note(1,"Call brother!");
                if(!emitter.isDisposed()){
                    emitter.onSuccess(note);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
