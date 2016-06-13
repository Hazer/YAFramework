package io.vithor.yamvpframework.mvp.interactor

/**
 * Created by Vithorio Polten on 2/22/16.
 */
interface Interactor {
    operator fun invoke(): Event


    interface Executor {
        fun execute(interactor: Interactor,
                    priority: Priority = Priority.LOW,
                    requireNetwork: Boolean = true)
    }

    interface Event {

    }

    interface Job {

    }

    enum class Priority(val value: Int) {
        LOW(0),
        MID(500),
        HIGH(1000)
    }

    class Wrapper(val interactor: Interactor,
                  val priority: Priority,
                  val requireNetwork: Boolean = true) : Job {

        fun onRun() {
            val event = interactor.invoke()
            //        bus.post(event)
        }

        fun onAdded() = Unit
        fun onCancel() = Unit
        fun shouldReRunOnThrowable(throwable: Throwable?) = false
    }
}

interface JobManager {
    fun addJob(wrapper: Interactor.Job)
}

class InteractorExecutorImpl(val jobManager: JobManager) : Interactor.Executor {

    override fun execute(interactor: Interactor, priority: Interactor.Priority, requireNetwork: Boolean) {
        jobManager.addJob(Interactor.Wrapper(interactor, priority, requireNetwork))
    }
}

class GetArtistDetailInteractor(val artistRepository: ArtistRepository) : Interactor {

    var id: String? = null

    override fun invoke(): Interactor.Event {
        val id = this.id ?: throw IllegalStateException("id can´t be null")
        val artist = artistRepository.getArtist(id)
        return ArtistDetailEvent(artist)
    }
}

interface  ArtistRepository {
    fun getArtist(id: String): Artist
}

class ArtistDetailEvent(val artist: Artist) : Interactor.Event {

}

class Artist {

}
