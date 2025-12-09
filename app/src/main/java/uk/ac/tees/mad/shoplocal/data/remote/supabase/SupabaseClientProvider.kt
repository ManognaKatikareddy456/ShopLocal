package uk.ac.tees.mad.planty.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {
    val client =
        createSupabaseClient(
            supabaseUrl = "https://ffvwqspjibnlrqwgvcoe.supabase.co",
            supabaseKey = "sb_publishable_G1kNbRL0-gGk5Vscp7nebg_VjBpz8VC"
        ) {
            install(GoTrue.Companion)
            install(Storage.Companion)
        }


}
