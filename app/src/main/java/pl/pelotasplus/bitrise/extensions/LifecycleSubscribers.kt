package pl.pelotasplus.bitrise.extensions

// https://github.com/stanwood/framework-arch-android/blob/develop/core/src/main/java/io/stanwood/framework/arch/core/rx/LifecycleSubscribers.kt

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

private val onErrorStub: (Throwable) -> Unit = { println(it) }
private val onCompleteStub: () -> Unit = {}
private val onNextStub: (Any) -> Unit = {}

fun <T : Any> Observable<T>.subscribeBy(
    lifecycleOwner: LifecycleOwner,
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit = onNextStub
) {
    LifecycleSubscriber(lifecycleOwner) { subscribeBy(onError, onComplete, onNext) }
}

private class LifecycleSubscriber(
    private val lifecycleOwner: LifecycleOwner,
    val subscribe: () -> Disposable
) : LifecycleObserver {
    private var disposable: Disposable? = null

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        disposable = subscribe.invoke()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable?.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        disposable?.dispose()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}
