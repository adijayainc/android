package org.owntracks.android.injection.modules

import android.content.Context
import androidx.test.espresso.idling.CountingIdlingResource
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.owntracks.android.App
import org.owntracks.android.EventBusIndex
import org.owntracks.android.data.repos.ContactsRepo
import org.owntracks.android.data.repos.LocationRepo
import org.owntracks.android.data.repos.MemoryContactsRepo
import org.owntracks.android.injection.qualifier.AppContext
import org.owntracks.android.injection.scopes.PerApplication
import org.owntracks.android.support.ContactImageProvider

@Module
class AppModule {
    @Provides
    @AppContext
    @PerApplication
    fun provideContext(app: App): Context {
        return app
    }

    @Provides
    @PerApplication
    fun provideEventbus(): EventBus {
        return EventBus.builder().addIndex(EventBusIndex()).sendNoSubscriberEvent(false).logNoSubscriberMessages(false).build()
    }

    @Provides
    @PerApplication
    fun provideContactsRepo(eventBus: EventBus?, contactImageProvider: ContactImageProvider?): ContactsRepo {
        return MemoryContactsRepo(eventBus!!, contactImageProvider!!)
    }

    @Provides
    @PerApplication
    fun provideLocationRepo(eventBus: EventBus?): LocationRepo {
        return LocationRepo(eventBus)
    }

    @Provides
    @PerApplication
    fun provideOutgoingQueueIdlingResource(): CountingIdlingResource {
        return CountingIdlingResource("outgoingQueueIdlingResource",true)
    }
}