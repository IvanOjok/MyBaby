package inc.pneuma.mybaby.data.model

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import inc.pneuma.mybaby.UserProto
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserProto> {
    override val defaultValue: UserProto = UserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProto {
        try {
            return UserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserProto,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.userDataStore: DataStore<UserProto> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)