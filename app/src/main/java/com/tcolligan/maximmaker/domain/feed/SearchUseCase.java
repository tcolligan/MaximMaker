package com.tcolligan.maximmaker.domain.feed;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * A use case for when we want to search through maxims.
 * <p>
 * Created on 6/22/18.
 *
 * @author Thomas Colligan
 */
public class SearchUseCase
{
    public static @NonNull Single<List<MaximFeedItemViewModel>> search(@NonNull final String searchTerm,
                                                               @NonNull final List<MaximFeedItemViewModel> feedViewModels)
    {
        final Locale defaultLocale = Locale.getDefault();
        final String searchText = searchTerm.toLowerCase(defaultLocale);

        return Observable.just(feedViewModels)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<MaximFeedItemViewModel>, List<MaximFeedItemViewModel>>()
                {
                    @Override
                    public List<MaximFeedItemViewModel> apply(List<MaximFeedItemViewModel> v)
                    {
                        return v;
                    }
                })
                .filter(new Predicate<MaximFeedItemViewModel>()
                {
                    @Override
                    public boolean test(MaximFeedItemViewModel v)
                    {
                        return (v.getMessageText().toLowerCase(defaultLocale).contains(searchText) ||
                                v.getAuthorText().toLowerCase(defaultLocale).contains(searchText) ||
                                v.getTagsText().toLowerCase(defaultLocale).contains(searchText));
                    }
                })
                .toList();
    }
}
