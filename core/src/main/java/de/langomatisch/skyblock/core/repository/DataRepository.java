package de.langomatisch.skyblock.core.repository;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @param <I> id object. something like an integer or an uuid
 * @param <O> product you want to get
 */
public interface DataRepository<I, O> {

    ListenableFuture<O> findById(I id);

    ListenableFuture<O> update(O object);

    ListenableFuture<O> create(O object);

}
